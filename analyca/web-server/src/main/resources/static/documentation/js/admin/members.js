let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

$(function () {
    $.ajax({
        type: "POST",
        url: "/admin/roles",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (roles) {
            console.log(roles);
            $("#membersTable").jsGrid({
                width: "100%",
                height: "auto",

                inserting: false,
                editing: true,
                sorting: true,
                paging: true,

                autoload: true,
                controller: {
                    loadData: function () {
                        return $.ajax({
                            type: "POST",
                            url: "/admin/members",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader(header, token);
                            },
                            error: function (e) {
                                console.log(e);
                            }
                        })
                    },
                    updateItem: function (item) {
                        console.log(item);
                        $.ajax({
                            type: "PUT",
                            url: "/admin/member",
                            data: JSON.stringify(item),
                            contentType: "application/json",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader(header, token);
                            }
                        })
                    },
                    deleteItem: function (item) {
                        console.log(item);
                        $.ajax({
                            type: "DELETE",
                            url: "/admin/member",
                            data: JSON.stringify(item),
                            contentType: "application/json",
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader(header, token);
                            }
                        })
                    }
                },

                fields: [
                    {name: "username", title: "유저명", type: "text", editing: false},
                    {name: "password", title: "비밀번호", type: "text", editing: false},
                    {name: "role", title: "권한", type: "select", items: roles, valueField: "name", textField: "name"},
                    {type: "control"}
                ]
            });
        },
        error: function (e) {
            console.log(e);
        }
    });
});