let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

function nodeClick(event) {
    //console.log(event.currentTarget.id);
    let listImages = "";
    let statusCSS = "";
    var search = {}
    search["workflow"] = event.currentTarget.id;
    $.ajax({
        type: "POST",
        url: "/ChainScheduleLog/steps",
        data: JSON.stringify(search),
        contentType: "application/json",
        success: function (data) {

            for (let i = 0; i < data.length; i++) {
                statusCSS = 'list-group-item';
                // listImages += '<a href="javascript:void(0);" id=' + data[i].id + ' onclick="stepClick(event);" class="list-group-item ' + statusCSS + ' list-group-item-action flex-column align-items-start">';
                listImages += '<a href="javascript:void(0);" id=' + data[i].id + ' onclick="stepClick(this);" class="list-group-item ' + statusCSS + ' list-group-item-action flex-column align-items-start">';
                listImages += '<div class="d-flex w-100 justify-content-between">';
                listImages += '<h5 class="mb-1">' + data[i].stepName + '</h5></div></a>';
            }

            document.getElementById("step-list-group").innerHTML = listImages;

        },
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        error: function (e) {
            console.log(e);
        }
    });
}

// function stepClick(event) {
function stepClick(e) {
    //console.log(event.currentTarget.id);
    let listImages = "";
    let statusCSS = "";
    var search = {}
    $("#log-list-group").text("");
    // search["workflow"] = event.currentTarget.id;
    search["workflow"] = e.id;
    $.ajax({
        type: "POST",
        url: "/ChainScheduleLog/logs",
        data: JSON.stringify(search),
        contentType: "application/json",
        success: function (data) {

            for (let i = 0; i < data.length; i++) {
                listImages += '<div class="d-flex w-100 justify-content-between">';
                listImages += '<h5 class="mb-1 my-1 ml-2 mr-2">' + data[i].message + '</h5></div>';
            }

            document.getElementById("log-list-group").innerHTML = listImages;

        },
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        error: function (e) {
            console.log(e);
        }
    });
    // document.getElementById("log-list-group").innerHTML = "Hello World ? " + e.id;
}

function checkFluency() {
    //console.log("Test");
    var checkbox = document.getElementById('fluency');
    if (checkbox.checked == true) {
        var content = '<div id="jsGrid"></div>'
        $(".table-responsive").html(content);
        $("#jsGrid").jsGrid({
            width: "100%",
            height: "auto",

            inserting: false,
            editing: false,
            sorting: true,
            paging: true,
            pageSize: 10,
            autoload: true,
            controller: {
                loadData: function () {
                    return $.ajax({
                        type: "POST",
                        url: "/ChainScheduleLog/admin",
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader(header, token);
                        },
                        error: function (e) {
                            console.log(e);
                        }
                    })
                },
            },

            fields: [
                {name: "id", title: "id", type: "text", visible: false, editing: false},
                {name: "dtCommand", title: "DtCommand", align: "center", type: "text", editing: false, visible: false},
                {name: "dtStart", title: "DtStart", align: "center", type: "text", editing: false},
                {name: "dtEnd", title: "DtEnd", align: "center", type: "text", editing: false},
                {name: "scheduleName", title: "Schedule Name", align: "center", type: "text", editing: false},
                {name: "stepName", title: "Step Name", align: "center", type: "text", editing: false},
                {name: "userId", title: "User ID", align: "center", type: "text", editing: false},
                {name: "status", title: "Status", align: "center", type: "text", editing: false},

            ]
        });
    }
    if (checkbox.checked != true) {
        var content = '<div id="jsGrid"></div>'
        $(".table-responsive").html(content);
        $("#jsGrid").jsGrid({
            width: "100%",
            height: "auto",

            inserting: false,
            editing: false,
            sorting: true,
            paging: true,
            pageSize: 10,
            autoload: true,
            controller: {
                loadData: function () {
                    return $.ajax({
                        type: "POST",
                        url: "/ChainScheduleLog/user",
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader(header, token);
                        },
                        error: function (e) {
                            console.log(e);
                        }
                    })
                },
            },

            fields: [
                {name: "id", title: "id", type: "text", visible: false, editing: false},
                {name: "dtCommand", title: "DtCommand", align: "center", type: "text", editing: false, visible: false},
                {name: "dtStart", title: "DtStart", align: "center", type: "text", editing: false},
                {name: "dtEnd", title: "DtEnd", align: "center", type: "text", editing: false},
                {name: "scheduleName", title: "Schedule Name", align: "center", type: "text", editing: false},
                {name: "stepName", title: "Step Name", align: "center", type: "text", editing: false},
                {name: "userId", title: "User ID", align: "center", type: "text", editing: false},
                {name: "status", title: "Status", align: "center", type: "text", editing: false},
            ]
        });
    }
}
function checkFluency1() {
    //console.log("Test");
    var checkbox = document.getElementById('fluency1');
    if (checkbox.checked == true) {
        var content = '<div id="jsGrid1"></div>'
        $(".table-responsive1").html(content);
        $("#jsGrid1").jsGrid({
            width: "100%",
            height: "auto",

            inserting: false,
            editing: false,
            sorting: true,
            paging: true,
            pageSize: 10,
            autoload: true,
            rowClick: function (args) {
                let listImages = "";
                let statusCSS = "";
                var search = {}
                search["workflow"] = args.item.id;
                $('.modal-title').text(args.item.scheduleName);
                $.ajax({
                    type: "POST",
                    url: "/ChainScheduleLog/steps",
                    data: JSON.stringify(search),
                    contentType: "application/json",
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function (ddata) {
                        for (let i = 0; i < ddata.length; i++) {
                            statusCSS = 'list-group-item';
                            // listImages += '<a href="javascript:void(0);" id=' + ddata[i].id + ' onclick="stepClick(event);" class="step_list_'+i+' list-group-item ' + statusCSS + ' list-group-item-action flex-column align-items-start">';
                            listImages += '<a href="javascript:void(0);" id=' + ddata[i].id + ' onclick="stepClick(this);" class="step_list_'+i+' list-group-item ' + statusCSS + ' list-group-item-action flex-column align-items-start">';
                            listImages += '<div class="d-flex w-100 justify-content-between">';
                            listImages += '<h5 class="mb-1">' + ddata[i].stepName + '</h5></div></a>';
                        }
                        document.getElementById("step-list-group").innerHTML = listImages;
                        //console.log(ddata);
                    }
                    ,
                    error: function (e) {
                        console.log(e);
                    }
                });

                $('div.modal').modal('show');
                $(".step_list_0").click();

            },
            controller: {
                loadData: function () {
                    return $.ajax({
                        type: "POST",
                        url: "/ChainScheduleLog/StepLogadmin",
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader(header, token);
                        },
                        error: function (e) {
                            console.log(e);
                        }
                    })
                },
            },

            fields: [
                {name: "id", title: "id", type: "text", visible: false, editing: false},
                {name: "dtCommand", title: "DtCommand", align: "center", type: "text", editing: false, visible: false},
                {name: "dtStart", title: "DtStart", align: "center", type: "text", editing: false},
                {name: "idSchedule", title: "idSchedule", align: "center", type: "text", visible: false, editing: false},
                {name: "scheduleName", title: "Schedule Name", align: "center", type: "text", editing: false},
                {name: "scheduleDesc", title: "Schedule Desc", align: "center", type: "text", editing: false},
                {name: "type", title: "Type", align: "center", type: "text", editing: false},
                {name: "sequential", title: "Sequential", align: "center", type: "text", editing: false},
                {name: "userId", title: "User ID", align: "center", type: "text", editing: false},
            ]
        });
    }
    if (checkbox.checked != true) {
        var content = '<div id="jsGrid1"></div>'
        $(".table-responsive1").html(content);
        $("#jsGrid1").jsGrid({
            width: "100%",
            height: "auto",

            inserting: false,
            editing: false,
            sorting: true,
            paging: true,
            pageSize: 10,
            autoload: true,
            rowClick: function (args) {
                let listImages = "";
                let statusCSS = "";
                var search = {}
                search["workflow"] = args.item.id;
                $('.modal-title').text(args.item.scheduleName);
                $.ajax({
                    type: "POST",
                    url: "/ChainScheduleLog/steps",
                    data: JSON.stringify(search),
                    contentType: "application/json",
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function (ddata) {
                        for (let i = 0; i < ddata.length; i++) {
                            statusCSS = 'list-group-item';
                            // listImages += '<a href="javascript:void(0);" id=' + ddata[i].id + ' onclick="stepClick(event);" class="step_list_'+i+' list-group-item ' + statusCSS + ' list-group-item-action flex-column align-items-start">';
                            listImages += '<a href="javascript:void(0);" id=' + ddata[i].id + ' onclick="stepClick(this);" class="step_list_'+i+' list-group-item ' + statusCSS + ' list-group-item-action flex-column align-items-start">';
                            listImages += '<div class="d-flex w-100 justify-content-between">';
                            listImages += '<h5 class="mb-1">' + ddata[i].stepName + '</h5></div></a>';
                        }
                        document.getElementById("step-list-group").innerHTML = listImages;
                        //console.log(ddata);
                    }
                    ,
                    error: function (e) {
                        console.log(e);
                    }
                });

                $('div.modal').modal('show');
                $(".step_list_0").click();

            },
            controller: {
                loadData: function () {
                    return $.ajax({
                        type: "POST",
                        url: "/ChainScheduleLog/StepLoguser",
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader(header, token);
                        },
                        error: function (e) {
                            console.log(e);
                        }
                    })
                },
            },

            fields: [
                {name: "id", title: "id", type: "text", visible: false, editing: false},
                {name: "dtCommand", title: "DtCommand", align: "center", type: "text", editing: false, visible: false},
                {name: "dtStart", title: "DtStart", align: "center", type: "text", editing: false},
                {name: "idSchedule", title: "idSchedule", align: "center", type: "text", visible: false, editing: false},
                {name: "scheduleName", title: "Schedule Name", align: "center", type: "text", editing: false},
                {name: "scheduleDesc", title: "Schedule Desc", align: "center", type: "text", editing: false},
                {name: "type", title: "Type", align: "center", type: "text", editing: false},
                {name: "sequential", title: "Sequential", align: "center", type: "text", editing: false},
                {name: "userId", title: "User ID", align: "center", type: "text", editing: false},
            ]
        });
    }
}

$(".modal").on("hidden.bs.modal", function () {
    $("#log-list-group").text("");
});

$(function () {
    $.ajax({
        type: "POST",
        url: "/ChainScheduleLog/access",
        contentType: "application/json",
        success: function (data) {
            console.log(data);
            $("#jsGrid").jsGrid({
                width: "100%",
                height: "auto",

                inserting: false,
                editing: false,
                sorting: true,
                paging: true,
                pageSize: 10,
                autoload: true,
                controller: {
                    loadData: function () {
                        return $.ajax({
                            type: "POST",
                            url: "/ChainScheduleLog/" + data.access,
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader(header, token);
                            },
                            error: function (e) {
                                console.log(e);
                            }
                        }).done(function (response) {
                            //console.log(response);
                            //d.resolve(response);
                        });
                    },
                },

                fields: [
                    {name: "id", title: "id", type: "text", visible: false, editing: false},
                    {name: "dtCommand", title: "DtCommand", align: "center", ype: "text", editing: false, visible:false},
                    {name: "dtStart", title: "DtStart", align: "center", ype: "text", editing: false},
                    {name: "dtEnd", title: "DtEnd", align: "center", type: "text", editing: false},
                    {name: "scheduleName", title: "Schedule Name", align: "center", type: "text", editing: false},
                    {name: "stepName", title: "Step Name", align: "center", type: "text", editing: false},
                    {name: "userId", title: "User ID", align: "center", type: "text", editing: false},
                    {name: "status", title: "Status", align: "center", type: "text", editing: false},
                ]
            });

            $("#jsGrid1").jsGrid({
                width: "100%",
                height: "auto",

                inserting: false,
                editing: false,
                sorting: true,
                paging: true,
                pageSize: 10,
                autoload: true,
                rowClick: function (args) {
                    let listImages = "";
                    let statusCSS = "";
                    var search = {}
                    search["workflow"] = args.item.id;
                    $('.modal-title').text(args.item.scheduleName);
                    $.ajax({
                        type: "POST",
                        url: "/ChainScheduleLog/steps",
                        data: JSON.stringify(search),
                        contentType: "application/json",
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader(header, token);
                        },
                        success: function (ddata) {
                            for (let i = 0; i < ddata.length; i++) {
                                statusCSS = 'list-group-item';
                                // listImages += '<a href="javascript:void(0);" id=' + ddata[i].id + ' onclick="stepClick(event);" class="step_list_'+i+' list-group-item ' + statusCSS + ' list-group-item-action flex-column align-items-start">';
                                listImages += '<a href="javascript:void(0);" id=' + ddata[i].id + ' onclick="stepClick(this);" class="step_list_'+i+' list-group-item ' + statusCSS + ' list-group-item-action flex-column align-items-start">';
                                listImages += '<div class="d-flex w-100 justify-content-between">';
                                listImages += '<h5 class="mb-1">' + ddata[i].stepName + '</h5></div></a>';
                            }
                            document.getElementById("step-list-group").innerHTML = listImages;
                            //console.log(ddata);
                        }
                        ,
                        error: function (e) {
                            console.log(e);
                        }
                    });

                    $('div.modal').modal('show');
                    $(".step_list_0").click();

                },
                controller: {
                    loadData: function () {
                        return $.ajax({
                            type: "POST",
                            url: "/ChainScheduleLog/StepLog" + data.access,
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader(header, token);
                            },
                            error: function (e) {
                                console.log(e);
                            }
                        }).done(function (response) {
                            //console.log(response);
                            //d.resolve(response);
                        });
                    },
                },

                fields: [
                    {name: "id", title: "id", type: "text", visible: false, editing: false},
                    {name: "dtCommand", title: "DtCommand", align: "center", type: "text", editing: false, visible: false},
                    {name: "dtStart", title: "DtStart", align: "center", type: "text", editing: false},
                    {name: "idSchedule", title: "idSchedule", align: "center", type: "text", visible: false, editing: false},
                    {name: "scheduleName", title: "Schedule Name", align: "center", type: "text", editing: false},
                    {name: "scheduleDesc", title: "Schedule Desc", align: "center", type: "text", editing: false},
                    {name: "type", title: "Type", align: "center", type: "text", editing: false},
                    {name: "sequential", title: "Sequential/Parallel", align: "center", type: "text", editing: false},
                    {name: "userId", title: "User ID", align: "center", type: "text", editing: false},
                ]
            });
        },
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        error: function (e) {
            console.log(e);
        }
    });

    // $(".step_list_0").click();
});