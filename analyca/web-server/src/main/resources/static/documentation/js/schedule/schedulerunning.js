let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

function checkFluency() {
    var checkbox = document.getElementById('fluency');
    if (checkbox.checked == true) {
        var content = '<div id="jsGrid1"></div>'
        $(".table-responsive").html(content);
        $("#jsGrid1").jsGrid({
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
                        url: "/scheduleRunning/admin",
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
                {name: "host", title: "Host", type: "text", editing: false},
                {name: "scheduleName", title: "이름", type: "text", editing: false},
                {name: "scheduleDesc", title: "설명", type: "text", editing: false},
                {name: "type", title: "Type", type: "text", editing: false},
                {name: "userId", title: "User ID", type: "text", editing: false},
                {name: "dtCommand", title: "시간", type: "text", editing: false},
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
                        url: "/scheduleRunning/user",
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
                {name: "host", title: "Host", type: "text", editing: false},
                {name: "scheduleName", title: "이름", type: "text", editing: false},
                {name: "scheduleDesc", title: "설명", type: "text", editing: false},
                {name: "type", title: "Type", type: "text", editing: false},
                {name: "userId", title: "User ID", type: "text", editing: false},
                {name: "dtCommand", title: "시간", type: "text", editing: false},
            ]
        });
    }
}

$(window).load(function () {
    $(".idSchedule").each(function () {
        var rows = $(".idSchedule:contains('" + $(this).text() + "')");
        var scheduleName_rows = rows.siblings(".scheduleName");
        var dtCommand_rows = rows.siblings(".dtCommand");
        var scheduleDesc_rows = rows.siblings(".scheduleDesc");
        var type_rows = rows.siblings(".type");
        var agent_rows = rows.siblings(".agent");

        if (rows.length > 1) {
            rows.eq(0).attr("rowspan", rows.length);
            scheduleName_rows.eq(0).attr("rowspan", rows.length);
            dtCommand_rows.eq(0).attr("rowspan", rows.length);
            scheduleDesc_rows.eq(0).attr("rowspan", rows.length);
            type_rows.eq(0).attr("rowspan", rows.length);
            agent_rows.eq(0).attr("rowspan", rows.length);

            rows.not(":eq(0)").remove();
            scheduleName_rows.not(":eq(0)").remove();
            dtCommand_rows.not(":eq(0)").remove();
            scheduleDesc_rows.not(":eq(0)").remove();
            type_rows.not(":eq(0)").remove();
            agent_rows.not(":eq(0)").remove();
        }

    });
});

$(function () {

    $.ajax({
        type: "POST",
        url: "/scheduleRunning/access",
        contentType: "application/json",
        success: function (data) {

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
                            url: "/scheduleRunning/" + data.access,
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
                    {name: "host", title: "Host", type: "text", editing: false},
                    {name: "scheduleName", title: "이름", type: "text", editing: false},
                    {name: "scheduleDesc", title: "설명", type: "text", editing: false},
                    {name: "type", title: "Type", type: "text", editing: false},
                    {name: "userId", title: "User ID", type: "text", editing: false},
                    {name: "dtCommand", title: "시간", type: "text", editing: false},
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


});