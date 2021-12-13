let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
var handle = null;

let columns = [
    { field: "idSchedule", title: "id", visible: false },
    { field: "dtCommand", title: "Start Time", type: "text", align: "center" },
    { field: "scheduleName", title: "Schedule Name", type: "text", align: "center" },
    { field: "scheduleDesc", title: "Description", type: "text", align: "center" },
    { field: "type", title: "Type", type: "text", align: "center"},
    { field: "parallel", title: "Sequential/Parallel", type: "text", align: "center"},
    // { field: "used", title: "Status", type: "text", align: "center" },
    { field: "step", title: "Step", type: "text", align: "center"},
    { field: "status", title: "Status", type: "text", align: "center"},

];


function checkFluency() {
    var checkbox = document.getElementById('fluency');
    let listImages = "";
    let statusCSS = "";

    if (checkbox.checked == true) {
        clearInterval(handle);
        $.ajax({
            type: "POST",
            url: "/ChainScheduleRunning/admin",
            contentType: "application/json",
            success: function (data) {
                for (let i = 0; i < data.length; i++) {

                    listImages += '<tr><td class="idSchedule" style="display: none;">' + data[i].idSchedule + '</td>';
                    listImages += '<td class="dtCommand" style="text-align: center;">' + data[i].dtCommand + '</td>';
                    listImages += '<td class="scheduleName" style="text-align: center;">' + data[i].scheduleName + '</td>';
                    listImages += '<td class="scheduleDesc" style="text-align: center;">' + data[i].scheduleDesc + '</td>';
                    listImages += '<td class="type" style="text-align: center;">' + data[i].type + '</td>';
                    listImages += '<td class="sequential" style="text-align: center;">' + data[i].sequential + '</td>';
                    listImages += '<td class="stepName" style="text-align: center;">' + data[i].stepName + '</td>';
                    listImages += '<td class="gubun" style="text-align: center;"><span style="display:inline-block; width: 90%; background-color: '+selectColor(data[i].status)+';">' + data[i].status + '</span></td></tr>';

                }
                if(data.length == 0){
                    listImages += '<tr><td colspan="8" style="text-align: center;">Not found</td></tr>';
                }
                document.getElementById("runningScheduleList").innerHTML = listImages;
                if(data.length == 0){

                }
                else{
                    rowspanList();
                }

                //console.log("succeess");
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            error: function (e) {
                console.log(e);
            }
        });
        handle = setInterval(ajaxCall, 3000); //300000 MS == 5 minutes
    }
    if (checkbox.checked != true) {
        clearInterval(handle);
        $.ajax({
            type: "POST",
            url: "/ChainScheduleRunning/user",
            contentType: "application/json",
            success: function (data) {
                for (let i = 0; i < data.length; i++) {

                    listImages += '<tr><td class="idSchedule" style="display: none;">' + data[i].idSchedule + '</td>';
                    listImages += '<td class="dtCommand" style="text-align: center;">' + data[i].dtCommand + '</td>';
                    listImages += '<td class="scheduleName" style="text-align: center;">' + data[i].scheduleName + '</td>';
                    listImages += '<td class="scheduleDesc" style="text-align: center;">' + data[i].scheduleDesc + '</td>';
                    listImages += '<td class="type" style="text-align: center;">' + data[i].type + '</td>';
                    listImages += '<td class="sequential" style="text-align: center;">' + data[i].sequential + '</td>';
                    listImages += '<td class="stepName" style="text-align: center;">' + data[i].stepName + '</td>';
                    listImages += '<td class="gubun" style="text-align: center;"><span style="display:inline-block; width: 90%; background-color: '+selectColor(data[i].status)+';">' + data[i].status + '</span></td></tr>';

                }
                if(data.length == 0){
                    listImages += '<tr><td colspan="8" style="text-align: center;">Not found</td></tr>';
                }
                document.getElementById("runningScheduleList").innerHTML = listImages;
                if(data.length == 0){

                }
                else{
                    rowspanList();
                }
                //console.log("succeess");
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            error: function (e) {
                console.log(e);
            }
        });
        handle = setInterval(ajaxCallUser, 3000); //300000 MS == 5 minutes
    }
}

$(window).load(function () {

});

function rowspanList(){
    var table = [];
    var table_count = 1;
    var k = 0;
    $("#listTable tbody").find("tr").each(function(i,v) {
        table.push($(v).find("td:nth-child(1)").text());
    })
    for(var i=0; i<table.length; i++) {
        for(var j=i+1; j<table.length; j++) {
            if(table[i] == table[j]) {
                table_count +=1;
                k+=1;
            }
        }
        $("#listTable tbody").find("tr")[i].children[0].setAttribute("rowspan", table_count);
        $("#listTable tbody").find("tr")[i].children[1].setAttribute("rowspan", table_count);
        $("#listTable tbody").find("tr")[i].children[2].setAttribute("rowspan", table_count);
        $("#listTable tbody").find("tr")[i].children[3].setAttribute("rowspan", table_count);
        $("#listTable tbody").find("tr")[i].children[4].setAttribute("rowspan", table_count);
        $("#listTable tbody").find("tr")[i].children[5].setAttribute("rowspan", table_count);

        if(table_count > 1) {
            for(var q = 1; q < table_count; q++) {
                $("#listTable tbody").find("tr")[i+q].children[0].style.display = "none";
                $("#listTable tbody").find("tr")[i+q].children[1].style.display = "none";
                $("#listTable tbody").find("tr")[i+q].children[2].style.display = "none";
                $("#listTable tbody").find("tr")[i+q].children[3].style.display = "none";
                $("#listTable tbody").find("tr")[i+q].children[4].style.display = "none";
                $("#listTable tbody").find("tr")[i+q].children[5].style.display = "none";
            }
        }
        table_count = 1;
        i=k;
        k++;
    }
    // $(".idSchedule").each(function () {
    //     var rows = $(".idSchedule:contains('" + $(this).text() + "')");
    //     console.log("=======");
    //     console.log($(this).text());
    //     console.log(rows.length);
    //     console.log("=====$$==");
    //     var scheduleName_rows = rows.siblings(".scheduleName");
    //     var dtCommand_rows = rows.siblings(".dtCommand");
    //     var scheduleDesc_rows = rows.siblings(".scheduleDesc");
    //     var type_rows = rows.siblings(".type");
    //     var sequential_rows = rows.siblings(".sequential");
    //
    //     if (rows.length > 1) {
    //         rows.eq(0).attr("rowspan", rows.length);
    //         scheduleName_rows.eq(0).attr("rowspan", rows.length);
    //         dtCommand_rows.eq(0).attr("rowspan", rows.length);
    //         scheduleDesc_rows.eq(0).attr("rowspan", rows.length);
    //         type_rows.eq(0).attr("rowspan", rows.length);
    //         sequential_rows.eq(0).attr("rowspan", rows.length);
    //
    //         rows.not(":eq(0)").remove();
    //         scheduleName_rows.not(":eq(0)").remove();
    //         dtCommand_rows.not(":eq(0)").remove();
    //         scheduleDesc_rows.not(":eq(0)").remove();
    //         type_rows.not(":eq(0)").remove();
    //         sequential_rows.not(":eq(0)").remove();
    //     }
    //
    // });
}

function selectColor(data){
    if(data == 'READY'){
        return 'none';
    }
    if(data == 'RUNNING'){
        return 'coral';
    }
    if(data == 'DONE'){
        return 'deepskyblue';
    }
}
function ajaxCall() {
    //do your AJAX stuff here
    let listImages = "";
    let statusCSS = "";
    //console.log("test");
    $.ajax({
        type: "POST",
        url: "/ChainScheduleRunning/admin",
        contentType: "application/json",
        success: function (data) {

            for (let i = 0; i < data.length; i++) {

                listImages += '<tr><td class="idSchedule" style="display: none;">' + data[i].idSchedule + '</td>';
                listImages += '<td class="dtCommand" style="text-align: center;">' + data[i].dtCommand + '</td>';
                listImages += '<td class="scheduleName" style="text-align: center;">' + data[i].scheduleName + '</td>';
                listImages += '<td class="scheduleDesc" style="text-align: center;">' + data[i].scheduleDesc + '</td>';
                listImages += '<td class="type" style="text-align: center;">' + data[i].type + '</td>';
                listImages += '<td class="sequential" style="text-align: center;">' + data[i].sequential + '</td>';
                listImages += '<td class="stepName" style="text-align: center;">' + data[i].stepName + '</td>';
                listImages += '<td class="gubun" style="text-align: center;"><span style="display:inline-block; width: 90%; background-color: '+selectColor(data[i].status)+';">' + data[i].status + '</span></td></tr>';

            }
            if(data.length == 0){
                listImages += '<tr><td colspan="8" style="text-align: center;">Not found</td></tr>';
            }
            document.getElementById("runningScheduleList").innerHTML = listImages;
            if(data.length == 0){

            }
            else{
                rowspanList();
            }
            //console.log("succeess");
        },
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        error: function (e) {
            console.log(e);
        }
    });
}
function ajaxCallUser() {
    //do your AJAX stuff here
    let listImages = "";
    let statusCSS = "";
    $.ajax({
        type: "POST",
        url: "/ChainScheduleRunning/user",
        contentType: "application/json",
        success: function (data) {
            for (let i = 0; i < data.length; i++) {

                listImages += '<tr><td class="idSchedule" style="display: none;">' + data[i].idSchedule + '</td>';
                listImages += '<td class="dtCommand" style="text-align: center;">' + data[i].dtCommand + '</td>';
                listImages += '<td class="scheduleName" style="text-align: center;">' + data[i].scheduleName + '</td>';
                listImages += '<td class="scheduleDesc" style="text-align: center;">' + data[i].scheduleDesc + '</td>';
                listImages += '<td class="type" style="text-align: center;">' + data[i].type + '</td>';
                listImages += '<td class="sequential" style="text-align: center;">' + data[i].sequential + '</td>';
                listImages += '<td class="stepName" style="text-align: center;">' + data[i].stepName + '</td>';
                listImages += '<td class="gubun" style="text-align: center;"><span style="display:inline-block; width: 90%; background-color: '+selectColor(data[i].status)+';">' + data[i].status + '</span></td></tr>';

            }
            if(data.length == 0){
                listImages += '<tr><td colspan="8" style="text-align: center;">Not found</td></tr>';
            }
            document.getElementById("runningScheduleList").innerHTML = listImages;
            if(data.length == 0){

            }
            else{
                rowspanList();
            }
            //console.log("succeess");
        },
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        error: function (e) {
            console.log(e);
        }
    });
}
$(function () {


    // $('#example').DataTable( {
    //     "pagingType": "simple_numbers"
    // } );
    let listImages = "";
    let statusCSS = "";
    $.ajax({
        type: "POST",
        url: "/ChainScheduleRunning/access",
        contentType: "application/json",
        success: function (data) {
            if(data.access == 'admin'){
                //console.log("admin");
                $.ajax({
                    type: "POST",
                    url: "/ChainScheduleRunning/admin",
                    contentType: "application/json",
                    success: function (data) {
                        for (let i = 0; i < data.length; i++) {

                            listImages += '<tr><td class="idSchedule" style="display: none;">' + data[i].idSchedule + '</td>';
                            listImages += '<td class="dtCommand" style="text-align: center;">' + data[i].dtCommand + '</td>';
                            listImages += '<td class="scheduleName" style="text-align: center;">' + data[i].scheduleName + '</td>';
                            listImages += '<td class="scheduleDesc" style="text-align: center;">' + data[i].scheduleDesc + '</td>';
                            listImages += '<td class="type" style="text-align: center;">' + data[i].type + '</td>';
                            listImages += '<td class="sequential" style="text-align: center;">' + data[i].sequential + '</td>';
                            listImages += '<td class="stepName" style="text-align: center;">' + data[i].stepName + '</td>';
                            listImages += '<td class="gubun" style="text-align: center;"><span style="display:inline-block; width: 90%; background-color: '+selectColor(data[i].status)+';">' + data[i].status + '</span></td></tr>';

                        }
                        if(data.length == 0){
                            listImages += '<tr><td colspan="8" style="text-align: center;">Not found</td></tr>';
                        }
                        document.getElementById("runningScheduleList").innerHTML = listImages;
                        if(data.length == 0){

                        }
                        else{
                            rowspanList();
                        }
                        //console.log("succeess");
                    },
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    error: function (e) {
                        console.log(e);
                    }
                });
                handle = setInterval(ajaxCall, 3000); //300000 MS == 5 minutes
            }
            else{
                console.log(data.access);
                $.ajax({
                    type: "POST",
                    url: "/ChainScheduleRunning/user",
                    contentType: "application/json",
                    success: function (data) {
                        for (let i = 0; i < data.length; i++) {

                            listImages += '<tr><td class="idSchedule" style="display: none;">' + data[i].idSchedule + '</td>';
                            listImages += '<td class="dtCommand" style="text-align: center;">' + data[i].dtCommand + '</td>';
                            listImages += '<td class="scheduleName" style="text-align: center;">' + data[i].scheduleName + '</td>';
                            listImages += '<td class="scheduleDesc" style="text-align: center;">' + data[i].scheduleDesc + '</td>';
                            listImages += '<td class="type" style="text-align: center;">' + data[i].type + '</td>';
                            listImages += '<td class="sequential" style="text-align: center;">' + data[i].sequential + '</td>';
                            listImages += '<td class="stepName" style="text-align: center;">' + data[i].stepName + '</td>';
                            listImages += '<td class="gubun" style="text-align: center;"><span style="display:inline-block; width: 90%; background-color: '+selectColor(data[i].status)+';">' + data[i].status + '</span></td></tr>';

                        }
                        if(data.length == 0){
                            listImages += '<tr><td colspan="8" style="text-align: center;">Not found</td></tr>';
                        }
                        document.getElementById("runningScheduleList").innerHTML = listImages;
                        if(data.length == 0){

                        }
                        else{
                            rowspanList();
                        }
                        //console.log("succeess");
                    },
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    error: function (e) {
                        console.log(e);
                    }
                });
                handle = setInterval(ajaxCallUser, 3000); //300000 MS == 5 minutes
            }

        },
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        error: function (e) {
            console.log(e);
        }
    });

});