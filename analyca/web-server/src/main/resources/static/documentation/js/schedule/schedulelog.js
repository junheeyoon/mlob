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
                        url: "/scheduleLog/admin",
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
                {name: "status", title: "상태", type: "text", editing: false},
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
                        url: "/scheduleLog/user",
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
                {name: "status", title: "상태", type: "text", editing: false},
                {name: "dtCommand", title: "시간", type: "text", editing: false},
            ]
        });
    }
}

$(function () {

    $.ajax({
        type: "POST",
        url: "/scheduleLog/access",
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
                            url: "/scheduleLog/" + data.access,
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
                    {name: "status", title: "상태", type: "text", editing: false},
                    {name: "dtCommand", title: "시간", type: "text", editing: false},
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
                controller: {
                    loadData: function () {
                        return $.ajax({
                            type: "POST",
                            url: "/scheduleLog/" + data.access,
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
                    {name: "status", title: "상태", type: "text", editing: false},
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