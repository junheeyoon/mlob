let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

function refresh() {
    location.href = "/schedule/crud"
    location.reload();
}


$(".create-button").click(function () {
    $('.modal-title').text("스케줄 추가");
    var footer = '';
    footer += '<button type="button" class="btn btn-light" data-dismiss="modal">Cancel</button><button id="modalSubmit" type="button" class="btn btn-light">Commit</button>'
    $(".modal-footer").html(footer);
    $('.table').html('<tr style="display: none;"><td>id</td><td id="tdId"><input class="form-control" id="id" name="id" type="text"></td></tr><tr><td>이 름</td><td id="tdScheduleName"><input class="form-control" id="scheduleName" name="scheduleName" type="text"></td></tr><tr><td>Host</td><td id="tdHost"><select class="selectpicker show-tick" id="host" name="host"  style="width: 360px; height: 35px;"><option value="0.0.0.0">0.0.0.0</option></select></td></tr><tr><td>설 명</td><td id="tdScheduleDesc"><input class="form-control" id="scheduleDesc" name="scheduleDesc" type="text"></td></tr><tr><td>스케줄링</td><td id="tdCronExpression"><input class="form-control" id="cronExpression" name ="cronExpression" type="text"></td></tr><tr><td>Command</td><td id="tdCommand"><div class="custom-file"><input class="form-control custom-file-input" id="file" type="file" name="file" accept="*" /><label class="custom-file-label" id="fileVal" for="file">Select File</label></div></td></tr><tr><td>기 간</td><td id="tdDt"><input class="form-control datepicker" id="dtStart" name="dtStart" type="text"></td></tr><tr><td>Type</td><td id="tdType"><select class="selectpicker show-tick select-val" name="type" style="width: 360px; height: 35px;"><option value="batch">batch</option><option value="service">service</option></select></td></tr><tr id ="trUserId" style="display: none;"><td>User ID</td><td id="tdUserId"><input class="form-control" id="userId" name="userId" type="text"></td></tr><tr id ="trUsed" style="display: none;"><td>Used</td><td id="tdUsed"><input class="form-control" id="used" name="used" type="text"></td></tr>')
    bsCustomFileInput.init();

    $('#dtStart').datetimepicker({
        format: 'Y-m-d H:i'
    });
    //$('#dtEnd').datepicker();
    $.ajax({
        type: "POST",
        url: "/schedule/hostlist",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            var content = "";
            for (var i = 0; i < data.length; i++) {
                content += "<option value=" + data[i].host + ">" + data[i].host + "</option>"
            }
            $("#host").html(content)
        }
    });

    $('div.modal').modal();
});

$(".modal").on("hidden.bs.modal", function () {
    $(".form-control").val("");
    $(".select-val").val("batch");
});

$("#upload_file_btn").click(function () {
    console.log(new FormData($("#upload_file_frm")[0]));
    $.ajax({
        url: "/schedule/fileupload",
        type: "POST",
        data: new FormData($("#upload_file_frm")[0]),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function () {
            $("#uload_result_msg").text("File Upload Success");
        },
        error: function () {
            $("#uload_result_msg").text("File Upload Error");
        }
    });
});


$(document).on("click", "#modalSubmit", function () {


    if ($('.modal-title').text() == "스케줄 추가") {

        $.ajax({
            type: "POST",
            url: "/schedule/add",
            data: new FormData($("#upload-file-form")[0]),
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function () {

            },
            complete: function () {
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
    if ($('.modal-title').text() == "스케줄 수정") {

        $.ajax({
            type: "PUT",
            url: "/schedule/crud",
            data: new FormData($("#upload-file-form")[0]),
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
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
});

$(document).on("click", "#stopSchedule", function () {

    var sendData = {
        'id': $("#tdId").text(),
        'used': $("#tdUsed").text()
    }

    var sendData1 = {
        'userId': $("#tdUserId").text(),
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
$(document).on("click", "#startSchedule", function () {

    var sendData = {
        'id': $("#tdId").text(),
        'used': $("#tdUsed").text()
    }
    var sendData1 = {
        'userId': $("#tdUserId").text(),
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

$(document).on("click", "#executeSchedule", function () {

    var sendData = {
        'id': $("#tdId").text(),
    }
    var sendData1 = {
        'userId': $("#tdUserId").text(),
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

    $(function () {

        $.ajax({
            type: "POST",
            url: "/schedule/hostlist",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                var StartField = function (config) {
                    jsGrid.Field.call(this, config);
                };
                console.log(data);
                StartField.prototype = new jsGrid.Field({

                    css: "date-field",            // redefine general property 'css'
                    align: "center",              // redefine general property 'align'

                    myCustomProperty: "foo",      // custom property

                    sorter: function (date1, date2) {
                        return new Date(date1) - new Date(date2);
                    },

                    editTemplate: function (value) {
                        return this._editPicker = $("<input>").datetimepicker().setDate(new Date(value));
                    },

                    editValue: function () {
                        var d = this._editPicker.datetimepicker.getDate(),
                            month = '' + (d.getMonth() + 1),
                            day = '' + d.getDate(),
                            year = d.getFullYear();
                        if (month.length < 2) month = '0' + month;
                        if (day.length < 2) day = '0' + day;
                        return [year, month, day].join('-');
                    }
                });

                jsGrid.fields.dtStart = StartField;


                $("#jsGrid").jsGrid({
                    width: "100%",
                    height: "auto",
                    editing: true,
                    align: "center",
                    paging: true,
                    pageSize: 10,
                    autoload: true,
                    sorting: true, // 칼럼의 헤더를 눌렀을 때, 그 헤더를 통한 정렬
                    //modify setting
                    rowClick: function (args) {
                        $('.modal-title').text("세부사항");

                        var tdScheduleName = '<label id="scheduleName"></label>'
                        $('#tdScheduleName').html(tdScheduleName);
                        $('#scheduleName').text(args.item.scheduleName);

                        var tdHost = '<label id="Host"></label>'
                        $('#tdHost').html(tdHost);
                        $('#Host').text(args.item.host);

                        var tdScheduleDesc = '<label id="scheduleDesc"></label>'
                        $('#tdScheduleDesc').html(tdScheduleDesc);
                        $('#scheduleDesc').text(args.item.scheduleDesc);

                        var tdCronExpression = '<label id="cronExpression"></label>'
                        $('#tdCronExpression').html(tdCronExpression);
                        $('#cronExpression').text(args.item.cronExpression);

                        var tdCommand = '<label id="command"></label>'
                        $('#tdCommand').html(tdCommand);
                        $('#command').text(args.item.command);

                        var tdDt = '<label id="dt"></label>'
                        $('#tdDt').html(tdDt);
                        $('#dt').text(args.item.dtStart);

                        var tdType = '<label id="type"></label>'
                        $('#tdType').html(tdType);
                        $('#type').text(args.item.type);

                        var tdUserId = '<label id="userId"></label>'
                        $('#trUserId').css("display", "");
                        $('#tdUserId').html(tdUserId);
                        $('#userId').text(args.item.userId);

                        var tdId = '<label id="id"></label>'
                        $('#tdId').html(tdId);
                        $('#id').text(args.item.id);

                        var tdUsed = '<label id="used"></label>'
                        $('#tdUsed').html(tdUsed);
                        $('#used').text(args.item.used);

                        var content = "";
                        for (var i = 0; i < data.length; i++) {
                            content += "<option value=" + data[i].host + ">" + data[i].host + "</option>"
                        }
                        $("#host").html(content)
                        $('#host').val(args.item.host);
                        var footer = "";
                        footer += '<button type="button" class="btn btn-light" data-dismiss="modal">Cancel</button>'
                        if (args.item.used == "STOP") {
                            footer += '<button type="button" class="btn btn-light" id ="startSchedule">시 작</button>'
                        } else {
                            footer += '<button type="button" class="btn btn-light" id ="stopSchedule">중 지</button>'
                        }
                        footer += '<button type="button" class="btn btn-light" id="executeSchedule">즉시실행</button>'
                        $(".modal-footer").html(footer);


                        $('div.modal').modal('show');

                    },
                    controller: {
                        loadData: function () {
                            return $.ajax({
                                type: "POST",
                                url: "/schedule/crud",
                                beforeSend: function (xhr) {
                                    xhr.setRequestHeader(header, token);
                                },
                                success: function (ddata) {
                                    console.log(ddata);
                                }
                                ,
                                error: function (e) {
                                    console.log(e);
                                }
                            })
                        },
                        updateItem: function (item) {

                            $.ajax({
                                type: "PUT",
                                url: "/schedule/crud",
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
                                url: "/schedule/crud",
                                data: JSON.stringify(item),
                                contentType: "application/json",
                                beforeSend: function (xhr) {
                                    xhr.setRequestHeader(header, token);
                                }
                            })
                        }
                    },

                    fields: [
                        {name: "id", title: "id", visible: false, width: 0},
                        {name: "scheduleName", title: "이름", type: "text", align: "center"},
                        //{ name: "hostName", title: "Host", type: "text", align: "center"},
                        {
                            name: "host",
                            title: "Host",
                            type: "select",
                            items: data,
                            align: "center",
                            valueField: "host",
                            textField: "host"
                        },
                        {name: "scheduleDesc", title: "설명", type: "text", align: "left"},
                        {
                            name: "cronExpression",
                            title: "스케줄링",
                            type: "text",
                            align: "center"
                        },
                        {
                            name: "command",
                            title: "Command",
                            type: "text",
                            align: "center"
                        },
                        {
                            name: "dtStart",
                            title: "기간",
                            type: "dtStart",
                            filtering: true,
                            align: "center"
                        },

                        {
                            name: "type",
                            title: "Type",
                            type: "select",
                            items: [{name: "batch"}, {name: "service"}],
                            valueField: "name",
                            textField: "name",
                            align: "center"
                        },
                        {name: "userId", title: "User ID", type: "text", align: "center"},
                        {name: "used", title: "Used", type: "text", align: "center"},
                        {
                            type: "control", editButton: false, deleteButton: false,


                            itemTemplate: function (value, item) {

                                var $result = jsGrid.fields.control.prototype.itemTemplate.apply(this, arguments);

                                var $customEditButton = $("<button>").attr({class: "customGridEditbutton jsgrid-button jsgrid-edit-button"})
                                    .click(function (e) {
                                        e.stopPropagation();
                                        $.ajax({
                                            type: "POST",
                                            url: "/schedule/userId",
                                            data: JSON.stringify(item),
                                            contentType: "application/json",
                                            beforeSend: function (xhr) {
                                                xhr.setRequestHeader(header, token);
                                            },
                                            success: function (user) {
                                                if (user.access == "true") {
                                                    $('.modal-title').text("스케줄 수정");
                                                    var footer = '';
                                                    footer += '<button type="button" class="btn btn-light" data-dismiss="modal">Cancel</button><button id="modalSubmit" type="button" class="btn btn-light">Commit</button>'
                                                    $(".modal-footer").html(footer);
                                                    $('.table').html('<tr style="display: none;"><td>id</td><td id="tdId"><input class="form-control" id="id" name="id" type="text"></td></tr><tr><td>이 름</td><td id="tdScheduleName"><input class="form-control" id="scheduleName" name="scheduleName" type="text"></td></tr><tr><td>Host</td><td id="tdHost"><select class="selectpicker show-tick" id="host" name="host"  style="width: 360px; height: 35px;"><option value="0.0.0.0">0.0.0.0</option></select></td></tr><tr><td>설 명</td><td id="tdScheduleDesc"><input class="form-control" id="scheduleDesc" name="scheduleDesc" type="text"></td></tr><tr><td>스케줄링</td><td id="tdCronExpression"><input class="form-control" id="cronExpression" name ="cronExpression" type="text"></td></tr><tr><td>Command</td><td id="tdCommand"><div class="custom-file"><input class="form-control custom-file-input" id="file" type="file" name="file" accept="*" /><label class="custom-file-label" id="fileVal" for="file">Select File</label></div></td></tr><tr><td>기 간</td><td id="tdDt"><input class="form-control datepicker" id="dtStart" name="dtStart" type="text"></td></tr><tr><td>Type</td><td id="tdType"><select class="selectpicker show-tick select-val" name="type" style="width: 360px; height: 35px;"><option value="batch">batch</option><option value="service">service</option></select></td></tr><tr id ="trUserId" style="display: none;"><td>User ID</td><td id="tdUserId"><input class="form-control" id="userId" name="userId" type="text"></td></tr><tr id ="trUsed" style="display: none;"><td>Used</td><td id="tdUsed"><input class="form-control" id="used" name="used" type="text"></td></tr>')
                                                    var content = "";
                                                    for (var i = 0; i < data.length; i++) {
                                                        content += "<option value=" + data[i].host + ">" + data[i].host + "</option>"
                                                    }
                                                    $("#host").html(content)
                                                    bsCustomFileInput.init();

                                                    $('#dtStart').datetimepicker({
                                                        format: 'Y-m-d H:i'
                                                    });
                                                    //$('#dtEnd').datepicker();
                                                    $('#scheduleName').val(item.scheduleName);
                                                    $('#host').val(item.host);
                                                    $('#scheduleDesc').val(item.scheduleDesc);
                                                    $('#cronExpression').val(item.cronExpression);
                                                    $('#dtStart').val(item.dtStart.toString().split(" ~")[0]);
                                                    $('.select-val').val(item.type);
                                                    $('#id').val(item.id);
                                                    $('#fileVal').text(item.command);
                                                    $('div.modal').modal('show');


                                                } else {
                                                    alert("권한이 없습니다");
                                                }
                                                e.stopPropagation();
                                            }
                                        });

                                    });
                                var $customDeleteButton = $("<button>").attr({class: "customGridDeletebutton jsgrid-button jsgrid-delete-button"})
                                    .click(function (e) {
                                        e.stopPropagation();
                                        $.ajax({
                                            type: "POST",
                                            url: "/schedule/userId",
                                            data: JSON.stringify(item),
                                            contentType: "application/json",
                                            beforeSend: function (xhr) {
                                                xhr.setRequestHeader(header, token);
                                            },
                                            success: function (user) {
                                                if (user.access == "true") {
                                                    var confirmResult = confirm("Are you sure?")
                                                    if (confirmResult == true) {
                                                        e.stopPropagation();
                                                        $.ajax({
                                                            type: "DELETE",
                                                            url: "/schedule/crud",
                                                            data: JSON.stringify(item),
                                                            contentType: "application/json",
                                                            complete: function (data) {
                                                                refresh();
                                                            },
                                                            beforeSend: function (xhr) {
                                                                xhr.setRequestHeader(header, token);
                                                            },
                                                            error: function (e) {
                                                                console.log(e);
                                                            }
                                                        })
                                                    } else {

                                                    }
                                                } else {
                                                    alert("권한이 없습니다");
                                                }
                                            }
                                        });

                                    });

                                return $("<div>").append($customEditButton).append($customDeleteButton);
                                //return $result.add($customEditButton);
                            },
                        }

                    ]
                });
            },
            error: function (e) {
                console.log(e);
            }
        });

    });
});

