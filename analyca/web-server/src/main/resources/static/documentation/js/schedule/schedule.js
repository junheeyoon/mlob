let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

const stepSize = 3;

function refresh(){
    location.href = "/schedule/crud"
    location.reload();
}



$(".create-button").click(function () {
    // location.href = "/schedule/detail?ptype=create&sid=0";
    // document.formDetail.ptype.value = "review";
    // document.formDetail.sid.value = "1";
    let myForm = document.formDetail;
    // $('<input>').attr('type', 'hidden').attr('id', header).attr('name', header).attr('value', token).appendTo(myForm);
    $('<input>').attr('type', 'hidden').attr('id', header).attr('name', "_csrf").attr('value', token).appendTo(myForm);
    let url = "/schedule/detail";
    myForm.action = url;
    myForm.method = "POST";
    myForm.submit();
});


$(".modal").on("hidden.bs.modal", function(){
    $(".form-control").val("");
    $(".select-val").val("batch");
});



$(document).on("click", "#stopSchedule", function () {
        var sendData = {
            'id' : $("#tdId").text(),
            'used': $("#tdUsed").text()
        }

    var sendData1 = {
        'userId' : $("#tdUserId").text(),
    }
    $.ajax({
        type: "POST",
        url: "/schedule/userId",
        data: JSON.stringify(sendData1),
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (user) {
            if (user.access == "true") {
                $.ajax({
                    type: "PUT",
                    url: "/schedule/used",
                    data: JSON.stringify(sendData),
                    contentType: "application/json",
                    success: function(data){

                    },
                    complete: function(data){
                        refresh();
                    },
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    error: function(e) {
                        console.log(e);
                    }
                });
            }
            else {
                alert("권한이 없습니다");
            }
        }
    });
});

$(document).on("click", "#startSchedule", function () {
    var sendData = {
        'id' : $("#tdId").text(),
        'used': $("#tdUsed").text()
    };
    var sendData1 = {
        'userId' : $("#tdUserId").text(),
    };
    $.ajax({
        type: "POST",
        url: "/schedule/userId",
        data: JSON.stringify(sendData1),
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (user) {
            if (user.access == "true") {
                $.ajax({
                    type: "PUT",
                    url: "/schedule/used",
                    data: JSON.stringify(sendData),
                    contentType: "application/json",
                    success: function (data) {

                    },
                    complete: function (data) {
                        refresh();
                    },
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    error: function (e) {
                        console.log(e);
                    }
                });
            }
            else {
                alert("권한이 없습니다");
            }
        }
    });

});

$(document).on("click", "#executeSchedule", function () {
    var sendData = {
        'id' : $("#tdId").text(),
    }
    var sendData1 = {
        'userId' : $("#tdUserId").text(),
    }
    $.ajax({
        type: "POST",
        url: "/schedule/userId",
        data: JSON.stringify(sendData1),
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (user) {
            if (user.access == "true") {
                $.ajax({
                    type: "POST",
                    url: "/schedule/service/run",
                    data: JSON.stringify(sendData),
                    contentType: "application/json",
                    success: function (data) {

                    },
                    complete: function (data) {
                        refresh();
                    },
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    error: function (e) {
                        console.log(e);
                    }
                });
            } else {
                alert("권한이 없습니다");
            }
        }
    });

});



$(document).ready(function () {

    $.ajax({
        type: "POST",
        url: "/schedule/crud",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            drawListTable(data);
        }
    });

});







function userCheck(userId){
    let check = false;
    $.ajax({
        type: "POST",
        url: "/schedule/userId",
        data: JSON.stringify({"userId":userId}),
        contentType: "application/json",
        async: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (user) {
            if (user.access == "true") {
                check = true;
            } else {
                check = false;
            }
        }
    });
    return check;
}


function fmtSteps(row, field) {
    //'<table style="width:100%;"><tr><td>??</td></tr><tr><td>bb</td></tr><tr><td>cc</td></tr></table>';
    let innertable = '<table style="width:100%;">';
    $.ajax({
        type: "POST",
        url: "/step/workflowDetail/"+field.id,
        data: JSON.stringify(row),
        contentType: "application/json",
        async: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            let steps = data.steps;
            for(let i = 0; i < Math.min(steps.length, stepSize) ; i++){
                innertable += "<tr><td>" + steps[i].stepName + "</td></tr>";
            }
            innertable += "</table>";
        }
    });
    return innertable;
}
function fmtEdit(value) {return '<a class="action rowEdit" href="javascript:void(0)" title="Edit"><i class="fa fa-edit"></i></a>';}
function fmtDeleteRow(value) {return '<a class="action rowDelete" href="javascript:void(0)" title="Delete"><i class="fa fa-trash"></i></a>';}
function fmtStatus(value) {
    if (value == "RUNNING") {
        return "ON";
    }
    else {
        return "OFF";
    }
}
function fmtStepCell(value, row, index) {
    return {
        css: {
            padding: 0
        }
    }
}
function fmtCronExpression(value, row, index, field) {
    let option = '<select id="nextExecutionList"><option value="" selected disabled hidden>Next Execution Time</option>';
    for(let i=0; i < row.cronNextExecution.length; i++){
        option += '<option disabled>'+row.cronNextExecution[i]+'</option>';
    }
    option += '</select>';
    // return row.cronExpressionStr + "</br>▶ Next Exec : " + row.cronNextExecution;
    return row.cronExpressionStr + "</br>" + option;
}

window.actionEvents = {
    'click .rowEdit': function (e, value, row, index) {
        if (userCheck(row.userId)) {
            document.formDetail.ptype.value = "modify";
            document.formDetail.scdstatus.value = "";
            document.formDetail.sid.value = row.id;
            let myForm = document.formDetail;
            // $('<input>').attr('type', 'hidden').attr('id', header).attr('name', header).attr('value', token).appendTo(myForm);
            $('<input>').attr('type', 'hidden').attr('id', header).attr('name', "_csrf").attr('value', token).appendTo(myForm);
            let url = "/schedule/detail";
            myForm.action = url;
            myForm.method = "POST";
            myForm.submit();
        } else{
            alert("권한이 없습니다");
        }
    },
    'click .rowDelete': function (e, value, row, index) {
        let con = confirm("삭제하시겠습니까?");
        if(con == false)
            return;
        if (userCheck(row.userId)) {
            $.ajax({
                type: "DELETE",
                url: "/schedule/crud",
                data: JSON.stringify(row),
                contentType: "application/json",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function (data) {
                    refresh();
                }
            });
        } else{
            alert("권한이 없습니다");
        }
    },
};

function drawListTable(tdata){
    let columns = [
        { field: "id", title: "id", visible: false },
        { field: "scheduleName", title: "Schedule Name", type: "text", align: "center" },
        // { field: "cronExpression", title: "Scheduling", type: "text", align: "center", visible: false},
        { field: "cronExpressionStr", title: "Scheduling/Next Execution", type: "text", align: "center", formatter:fmtCronExpression},
        { field: "cronNextExecution", title: "Scheduling/Next Execution", visible:false},
        { field: "scheduleDesc", title: "Description", type: "text", align: "left" },
        { field: "type", title: "Type", type: "select", valueField: "name", textField: "name", align: "center", items: [{name: "batch"}, {name: "service"}] },
        // { field: "sequential", title: "Seq/Par", type: "select", valueField: "name", textField: "name", align: "center", items: [{name: "CONCURRENT"}, {name: "SEQUENTIAL"}] },
        { field: "sequential", title: "Sequential/Parallel", type: "select", valueField: "name", textField: "name", align: "center", items: [{name: "PARALLEL"}, {name: "SEQUENTIAL"}] },
        { field: "userId", title: "User ID", type: "text", align: "center" },
        // { field: "used", title: "Status", type: "text", align: "center" },
        { field: "used", title: "On/Off", type: "select", align: "center", formatter:fmtStatus },
        { field: "step", title: "Step", type: "text", align: "center", class : "stepCell", formatter:fmtSteps, cellStyle:fmtStepCell},
        { field: "rowEdit", title:"Edit", align:"center", width:20, formatter:fmtEdit, events:actionEvents },
        { field: "rowDelete", title:"Del", align:"center", width:20, formatter:fmtDeleteRow, events:actionEvents }
    ];

    let $listTable = $("#listTable");
    $listTable.bootstrapTable('destroy');
    $listTable.bootstrapTable({
        data : tdata,
        columns: columns,
        onClickRow:function(row, $element, field){
            if(field == "rowEdit" || field == "rowDelete" || field == "cronExpressionStr")
                return;
            //alert(row.id);
            document.formDetail.ptype.value = "review";
            document.formDetail.scdstatus.value = row.used;
            document.formDetail.sid.value = row.id;
            let myForm = document.formDetail;
            // $('<input>').attr('type', 'hidden').attr('id', header).attr('name', header).attr('value', token).appendTo(myForm);
            $('<input>').attr('type', 'hidden').attr('id', header).attr('name', "_csrf").attr('value', token).appendTo(myForm);
            let url = "/schedule/detail";
            myForm.action = url;
            myForm.method = "POST";
            myForm.submit();
        }
    });
    $listTable.bootstrapTable('refresh');
    // $(".stepCell").attr("style", "padding:0;text-align: center;");
}
