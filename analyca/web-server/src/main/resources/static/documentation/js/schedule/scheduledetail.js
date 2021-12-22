let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

const ptype = document.getElementById("ptype").value;
const scdstatus = document.getElementById("scdstatus").value;
const sid = document.getElementById("sid").value;

function refresh(){
    location.href = "/schedule/detail"
    location.reload();
}
function finish(){
    location.href = "/schedule/crud"
    //location.reload();
}

/**
 * 취소
 * */
$(".cancel-button").click(function () {
    finish();
});

/**
 * [스텝 추가] 버튼 클릭 -> 모달 생성
 * */
$(".step-create-button").click(function () {
    $('#stepName').val("");
    $('#stepDesc').val("");

    $.ajax({
        type: "POST",
        url: "/step/list",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
             let content = "<option value='' selected disabled hidden>--Select--</option>";
            for(var i = 0; i < data.length; i++){
                let step = data[i].containerName + "(" + data[i].image + ":" + data[i].tag + ")";
                content += "<option value=" + data[i].id + ">" + step + "</option>";
            }
            $("#stepImage").html(content);
        }
    });

    $.ajax({
        type: "POST",
        url: "/schedule/hostlist",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            let content = "<option value='' selected disabled hidden>--Select--</option>";
            for(var i = 0; i < data.length; i++){
                let hostInfo = data[i].name + "(" + data[i].ip + ")";
                content += "<option value=" + hostInfo + ">" + hostInfo + "</option>"
            }
            $("#stepHost").html(content);
        }
    });

    $('div.modal').modal();
});

// $(".modal").on("hidden.bs.modal", function(){
//     $(".form-control").val("");
//     $(".select-val").val("batch");
// });

/**
 * [스텝 추가] 모달 -> Commit
 * */
$(document).on("click", "#modalSubmit", function () {

    if($('#stepName').val() == "" || $('#stepName').val() == null){
        alert("Check Step Name");
        return;
    }
    if($('#stepHost').val() == "" || $('#stepHost').val() == null){
        alert("Select Host");
        return;
    }
    if($('#stepImage').val() == "" || $('#stepImage').val() == null){
        alert("Select Image");
        return;
    }


    let tdata = $('#stepTable').bootstrapTable('getData');
    let newStep = {"stepName":$('#stepName').val(),
                    "stepDesc":$('#stepDesc').val(),
                    "host":$('#stepHost').val(),
                    "image":$("#stepImage option:selected").text(),
                    "idStepList":$("#stepImage option:selected").val()
                    };
    tdata.push(newStep);
    drawStepTable(tdata);

    $('.modal').modal("hide");
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



/**
 * [스케줄 추가/수정] Commit
 * */
$(".create-button").click(function () {
    if($('#scheduleName').val() == "" || $('#scheduleName').val() == null){
        alert("Check Schedule Name");
        return;
    }
    if($('#stepTable').bootstrapTable('getData').length == 0){
        alert("Add Steps");
        return;
    }


    let cronExp = getCronExpression();
    if(cronExp === undefined)
        return;

    let sdata = {
        'scheduleName'      : $('#scheduleName').val(),
        'scheduleDesc'      : $('#scheduleDesc').val(),
        // 'cronExpression' : $('#cronExpression').val(),
        // 'cronExpressionStr' : $('#cronExpression').val(),
        'cronExpression'    : cronExp.cronExpression,
        'cronExpressionStr' : cronExp.cronExpressionStr,
        'type'              : $('#tdType option:selected').val(),
        'parallel'          : $('#tdParallel option:selected').val(),
        'steps'             : JSON.stringify($('#stepTable').bootstrapTable('getData'))
    };

    if(ptype == "modify"){
        sdata.idSchedule = sid;
    }

    $.ajax({
        type: "POST",
        url: "/step/create",
        contentType: "application/json",
        data : JSON.stringify(sdata),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            if(data.result == true || data.result == "true") {
                finish();
            }
        },
        error: function (e) {
            alert("Error");
            console.log(e);
        }
    });
});



function fmtMoveUp(value) {return '<a class="action rowUp" href="javascript:void(0)" title="Move Up">▲</a>';};
function fmtMoveDown(value) {return '<a class="action rowDown" href="javascript:void(0)" title="Move Down">▼</a>';};
function fmtDeleteRow(value) {return '<a class="action rowDelete" href="javascript:void(0)" title="Delete Row">X</a>';};

window.actionEvents = {
    'click .rowUp': function (e, value, row, index) {
        var source = JSON.stringify($('#stepTable').bootstrapTable('getData')[index]);
        var target = JSON.stringify($('#stepTable').bootstrapTable('getData')[index - 1]);
        $('#stepTable').bootstrapTable('updateRow', {'index':index - 1, 'row': JSON.parse(source)});
        $('#stepTable').bootstrapTable('updateRow', {'index':index, 'row': JSON.parse(target)});
    },
    'click .rowDown': function (e, value, row, index) {
        var source = JSON.stringify($('#stepTable').bootstrapTable('getData')[index]);
        var target = JSON.stringify($('#stepTable').bootstrapTable('getData')[index + 1]);
        $('#stepTable').bootstrapTable('updateRow', {'index':index + 1, 'row': JSON.parse(source)});
        $('#stepTable').bootstrapTable('updateRow', {'index':index, 'row': JSON.parse(target)});
    },
    'click .rowDelete': function (e, value, row, index) {
        let tdata = $('#stepTable').bootstrapTable('getData');
        tdata.splice(index, 1);
        drawStepTable(tdata);
    },
};

function drawStepTable(tdata){
    let columns = [
        /*{ field:'no', title:'No', align:'center', width:20, visible: false},*/
        { field:'stepName', title:'Name', align:'center'},
        { field:'stepDesc', title:'Description', align:'center'},
        { field:'host', title:'Host', align:'center'},
        /*{ field:'agent', title:'Agent', align:'center', visible:false},*/
        { field:'image', title:'Image', align:'center'},
        { field:'idStepList', title:'IdStepList', align:'center', visible:false},
        { field:'rowUp', title:'▲', align:'center', width:20, formatter:fmtMoveUp, events:actionEvents},
        { field:'rowDown', title:'▼', align:'center', width:20, formatter:fmtMoveDown, events:actionEvents},
        { field:'rowDelete', title:'X', align:'center', width:20, formatter:fmtDeleteRow, events:actionEvents}
        ];

    let $stepTable = $("#stepTable");
    $stepTable.bootstrapTable('destroy');
    $stepTable.bootstrapTable({
        data : tdata,
        columns: columns
    });
    if (ptype == "review"){
        $stepTable.bootstrapTable('hideColumn', 'rowUp');
        $stepTable.bootstrapTable('hideColumn', 'rowDown');
        $stepTable.bootstrapTable('hideColumn', 'rowDelete');
    }
    $stepTable.bootstrapTable('refresh');
}



//
// var fakedata = [
//     {"no":0,"stepName":"new title 1", "stepDesc":"name 1", "host":"0.0.0.0", "image":"myImage"},
//     {"no":1,"stepName":"new title 2", "stepDesc":"name 2", "host":"0.0.0.0", "image":"myImage"},
//     {"no":2,"stepName":"new title 3", "stepDesc":"name 3", "host":"0.0.0.0", "image":"myImage"},
// ]

$(document).ready(function () {
    fnSetAddBatPeriod();
    fnSetDate();

    // 수정 혹은 상세보기
    if (sid != "" && ptype != ""){
        $.ajax({
            type: "POST",
            url: "/step/workflowDetail/"+sid,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                let schedule = data.schedule;
                let steps = data.steps;

                document.getElementById("scheduleName").value = schedule.scheduleName;
                document.getElementById("scheduleDesc").value = schedule.scheduleDesc;
                // document.getElementById("cronExpression").value = schedule.cronExpression;
                document.getElementById("cronExpression").value = schedule.cronExpressionStr;

                if(schedule.type == "batch") {
                    $("#tdType>select").val("batch").prop("selected", true);
                }else {
                    $("#tdType>select").val("service").prop("selected", true);
                }

                // if(schedule.sequential == "CONCURRENT"){
                if(schedule.sequential == "PARALLEL"){
                    // $("#tdParallel>select").val("concurrent").prop("selected", true);
                    $("#tdParallel>select").val("parallel").prop("selected", true);
                }else{
                    $("#tdParallel>select").val("sequential").prop("selected", true);
                }

                drawStepTable(steps);

            }
        });

        if (ptype.toString() == "modify"){       // 수정
            $(".card-header").text("Schedule Detail - Update");
            $(".create-button").text("Update");
            $(".colModify").show();
            $(".modifyChecker").show();
            $(".modifyTab").hide();
            $("#cronExpression").attr("readonly","");

        } else if (ptype.toString() == "review"){    // 상세보기
            //$(".card-header").text("Schedule Detail : " + sid);

            $(".step-create-button").hide();
            $(".modifyTab").hide();
            $(".create").hide();
            $(".review").show();
            $("#detailTable input").attr("readonly","");
            if(scdstatus == "RUNNING" || scdstatus == "running" || scdstatus == "ON" || scdstatus == "on")
                $(".stop-button").text("STOP");
            else
                $(".stop-button").text("RESUME");

            // $(".execute-button").hide();    //
        }
    } else {
        // Create New
        let tdata = {};
        drawStepTable(tdata);
    }
});




/**
 * [스케줄 멈춤/실행]
 * */
$(".stop-button").click(function () {
    let sdata = {
        'id'   : sid
    };
    // if(ptype == "modify"){
    //     sdata.id=sid;
    // }

    $.ajax({
        type: "PUT",
        url: "/schedule/used",
        contentType: "application/json",
        data : JSON.stringify(sdata),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            // if(data.result == true || data.result == "true") {
            //     finish();
            // }
            finish();
        },
        error: function (e) {
            // alert("Error");
            // console.log(e);
        }
    });
});


/**
 * [스케줄 즉시실행]
 * */
$(".execute-button").click(function () {
    let sdata = {
        'id'   : sid
    };
    // if(ptype == "modify"){
    //     sdata.id= sid;
    // }

    $.ajax({
        type: "POST",
        url: "/schedule/service/run",
        contentType: "application/json",
        data : JSON.stringify(sdata),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            // if(data.result == true || data.result == "true") {
            //     finish();
            // }
            finish();
        },
        error: function (e) {
            // alert("Error");
            console.log(e);
        }
    });
});






/********************************************************************************************
 * 해당 월의 일수 구해오기
 ********************************************************************************************/
function fnDayOfMonth(year, month){
    //month 는 0 부터 시작해서..
    return 32 - new Date(year, month-1, 32).getDate();
}

/********************************************************************************************
 * 스케쥴러 추가 팝업의 시간규칙 셀렉트 박스 이벤트 추가
 ********************************************************************************************/
function fnSetAddBatPeriod(){

    // 시간규칙 tab 선택
    $("#add_tabs").tabs();
    $(".SCHEDULE_ROLL").on("change", function(){
        var idx = $(this).val();
        if(idx == "D"){
            $("#add_tabs").tabs( "option", "active", 0);
            $(".default_date").show();
        }else if(idx == "W"){
            $("#add_tabs").tabs( "option", "active", 1);
            $(".default_date").show();
        }else if(idx == "M"){
            $("#add_tabs").tabs( "option", "active", 2);
            $("default_date").show();
        }else if(idx == "Y"){
            $("#add_tabs").tabs( "option", "active", 3);
            $(".default_date").show();
        /*}else if(idx == "O"){
            $("#add_tabs").tabs( "option", "active", 4);
            $(".default_date").show();*/
        }else if(idx == "I"){
            $("#add_tabs").tabs( "option", "active", 4);
            $(".default_date").hide();
        }
    });

    // 배치주기가 매년 일시
    $(".Y_MON").on("change", function(){
        var mon = $(this).val();
        var days = fnDayOfMonth(1900, mon);
        $(".Y_DAY").empty();
        for(var i=1; i<=days; i++){
            var val = i;
            if(i < 10){
                val = "0"+i;
            }
            $(".Y_DAY").append("<option value='"+val+"'>"+val+"</option>");
        }

    });

}




/********************************************************************************************
 * 스케쥴러 추가, 스케쥴러 수정 팝업의 배치 주기 날짜 관련
 ********************************************************************************************/
function fnSetDate(){

    $(".HOUR").append("<option value='*'>*</option>");
    $(".MIN").append("<option value='*'>*</option>");
    $(".SEC").append("<option value='*'>*</option>");

    for(let i=0; i<60; i++){
        let val = i;
        if(i < 10){
            val = "0"+i;
        }

        // 시간
        if(i < 24){
            $(".HOUR").append("<option value='"+i+"'>"+val+"</option>");
        }

        // 일
        if(i < 32 && i > 0){
            $(".M_DAY").append("<option value='"+i+"'>"+val+"</option>");
            $(".Y_DAY").append("<option value='"+i+"'>"+val+"</option>");
        }

        //월
        if(i < 13 && i > 0){
            $(".Y_MON").append("<option value='"+i+"'>"+val+"</option>");
        }

        // 분, 초
        $(".MIN").append("<option value='"+i+"'>"+val+"</option>");
        $(".SEC").append("<option value='"+i+"'>"+val+"</option>");
    }
}


function getCronExpression(){
    // 수정 안 함
    if(ptype == "modify" && !$("#modifyCron").is(":checked")){
        let result = {
            "cronExpression" : "",
            "cronExpressionStr" : ""
        };
        return result;
    }

    // 기본
    let sRole = $(".SCHEDULE_ROLL").val();
    let hour = $(".HOUR").val();
    let min = $(".MIN").val();
    let sec = $(".SEC").val();

    // 선택사항
    let sRoleStr = sec+" "+min+" "+hour+" ";
    let sRoleStr2 = hour + "시 " + min + "분 "+ sec + "초 ";

    if(sRole == "D"){
        sRoleStr += "* * ?";
    }else if(sRole == "W"){	// 매주
        var week_val = [];
        $("input[name='add_week[]']").each(function(i, chk){
            var isCheck = $(chk).is(':checked');
            if(isCheck){
                week_val.push($(chk).val());
            }
        });
        sRoleStr += "? * "+week_val.toLocaleString();
        sRoleStr2 = week_val.toLocaleString()+"요일 "+sRoleStr2+"마다";

    }else if(sRole == "M"){	// 매월
        var mon_val = $(".M_DAY").val();
        sRoleStr += mon_val+" * ?";
        sRoleStr2 = "매월 "+ mon_val+"일 "+sRoleStr2+"마다";

    }else if(sRole == "Y"){	// 매년
        var year_val1 = $(".Y_MON").val();
        var year_val2 = $(".Y_DAY").val();
        sRoleStr += year_val2+" "+year_val1+" ?";
        sRoleStr2 = "매년 "+year_val2+"월 "+year_val1+"일 "+sRoleStr2+"마다";

    }else if(sRole == "O"){	// 1회
        var one_val = $(".O_DATEPICKER").val();
        if(one_val == ""){
            alert("날짜를 입력해주세요.");
            return;
        }
        var one_val_arr = one_val.split("-");
        sRoleStr += parseInt(one_val_arr[2])+" "+parseInt(one_val_arr[1])+" ? "+one_val_arr[0];
        sRoleStr2 = "1회 실행 "+parseInt(one_val_arr[0])+"년 "+one_val_arr[1]+" 월 "+one_val_arr[2]+"일 "+sRoleStr2;

    }else if(sRole == "I"){
        var itv_sel_val = $(".I_INTERVAL_SEL").val();
        var itv_txt_val = $(".I_INTERVAL_TEXT").val();

        if(itv_txt_val == ""){
            alert("Check Increment Number");
            $(".I_INTERVAL_TEXT").focus();
            return;
        }

        var str = "";
        if(itv_sel_val == "S") {
            if(itv_txt_val < 0 || itv_txt_val > 59){
                alert("Set Increment Range from 0 to 59");
                return;
            }
            sRoleStr = "0/" + itv_txt_val + " * * * * ?";
            str = "초 ";
        }else if(itv_sel_val == "M"){
            if(itv_txt_val < 0 || itv_txt_val > 59){
                alert("Set Increment Range from 0 to 59");
                return;
            }
            sRoleStr = "0 0/"+itv_txt_val+" * * * ?";
            str = "분 ";
        }else if(itv_sel_val == "H"){
            if(itv_txt_val < 0 || itv_txt_val > 23){
                alert("Set Increment Range from 0 to 23");
                return;
            }
            sRoleStr = "0 0 0/"+itv_txt_val+" * * ?";
            str = "시 ";
        }else if(itv_sel_val == "D"){
            if(itv_txt_val < 1 || itv_txt_val > 31){
                alert("Set Increment Range from 1 to 31");
                return;
            }
            sRoleStr = "0 0 0 0/"+itv_txt_val+" * ?";
            str = "일 ";
        }
        sRoleStr2 = itv_txt_val+ str +"마다";
    }

    // console.log("주기 설명 : "+sRoleStr + " / " + sRoleStr2);
    // alert("주기 설명 : "+sRoleStr + " / " + sRoleStr2);

    let result = {
        "cronExpression" : sRoleStr,
        "cronExpressionStr" : cronStrKorean(sRoleStr2)
    };

    return result;
}
function cronStrKorean(str){
    let days = {
        'SUN' : '일',
        'MON' : '월',
        'TUE' : '화',
        'WED' : '수',
        'THU' : '목',
        'FRI' : '금',
        'SAT' : '토'
    };
    str = str.replace(/\*/g, "매");
    for(let k in days){
        str = str.replace(k , days[k]);
    }
    return str;
}

$("#modifyCron").click(function(){
    if($(this).is(":checked")){
        $(".modifyTab").show();
    }
    else{
        $(".modifyTab").hide();
    }
});

