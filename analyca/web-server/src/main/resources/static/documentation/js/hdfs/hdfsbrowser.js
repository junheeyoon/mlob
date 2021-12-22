let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

var current_directory = "";
var before_directory = "";
var current_path = "";

/**
 * Show Error Msg
 * @param msg error message
 */
function show_err_msg(msg) {
    $('#alert-panel-body').html(msg);
    $('#alert-panel').show();
}


/*************************************************************************************
 * hdfs Browser  - Start
 *************************************************************************************/

/**
 * Browser Init
 */
function initTable() {
    $('#table').bootstrapTable({
        // data: d["data"],
        search: true,
        columns: [
            {
                field: 'state',
                align: 'center',
                checkbox: true,
                valign: 'middle'
            }, {
                field: 'type',
                align: 'center',
                valign: 'middle'
            }, {
                title: '이름',
                field: 'name',
                align: 'left',
                valign: 'middle'
            }, {
                title: '권한',
                field: 'permission',
                align: 'center',
                valign: 'middle'
            }, {
                title: '소유자',
                field: 'owner',
                align: 'center',
                valign: 'middle'
            }, {
                title: '그룹',
                field: 'group',
                align: 'center',
                valign: 'middle'
            }, {
                title: '용량',
                field: 'size',
                valign: 'center',

            }, {
                title: '최종 수정 날짜',
                field: 'modification_time',
                align: 'center'
            }],
        onClickCell: function (field, value, row, $element) {
            var type = row['type'];
            var path = row['name'];
            if (field === "name") {

                var abs_path = '';

                before_directory = current_directory;

                if (type === 'DIRECTORY' || type === 'LOCAL') {
                    // $('#table').bootstrapTable('destroy');
                    abs_path = current_directory + path + "/";
                    loadTable(abs_path, "");
                } else {
                    abs_path = current_directory + path;
                    view_file_detail(abs_path);
                }
                return false;
            } else if (field === "permission") {
                current_path = current_directory + path;
                permission_set(value);
            }
        }
    });
}

/**
 * browser load data
 * @param dir : directory path
 * @param flag :
    */
function loadTable(dir, flag) {
    var url = "./files";
    if (dir !== "") {
        url = url + dir;
    } else {
        dir = "/";
    }

    $.ajax({
        "url": url + "?op=BROWSING&flag=" + flag,
        "type": "GET",
        success: function (data) {
            var d = JSON.parse(data);
            if (dir === "") {
                dir = "/";
            } else {
                dir = d['currentPath'];
            }

            current_directory = dir;

            if (d['result'] === 'fail') {
                show_err_msg(d['msg']);
                $('#directory').val(before_directory);
                current_directory = before_directory;
            } else {
                $('#directory').val(dir);
            }

            $('#table').bootstrapTable('load', d['data']);

            if (dir === "/") {
                $('#btn-up-arrow').attr('disabled', true);
                $('#btn-up-arrow').attr('style', 'pointer-events: none;');
            } else {
                $('#btn-up-arrow').attr('disabled', false);
                $('#btn-up-arrow').attr('style', 'pointer-events: auto;');
            }
        }
    });
}

$('#btn-up-arrow').click(function () {
    var arrPath = current_directory.split("/");
    var abs_path = '';
    for (var i = 0; i < arrPath.length - 2; i++) {
        abs_path = abs_path + arrPath[i] + "/";
    }
    before_directory = current_directory;
    loadTable(abs_path, "");
});

/*************************************************************************************
 * hdfs Browser  - end
 *************************************************************************************/

/*************************************************************************************
 * bootstrapTable column formatter and Style - Start
 *************************************************************************************/

/**
 * browser bootstrapTable permission Table Formatter
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function permFormatter(value, row, index) {
    var arrValue = value.split("_");

    if (arrValue[0] === '-') {
        return '<label><input id="' + arrValue[2] + '" type="checkbox"> ' + arrValue[1] + '</label>';
    } else {
        return '<label><input id="' + arrValue[2] + '" type="checkbox" checked> ' + arrValue[1] + '</label>';
    }
}

function dateFormatter(value, row, index) {
    if (row['type'] !== 'LOCAL') {
        var cutoff = moment().subtract(6, 'months').unix() * 1000;
        if (value < cutoff) {
            return moment(Number(value)).format('MMM DD YYYY');
        } else {
            return moment(Number(value)).format('MMM DD HH:mm');
        }
    }
}

function headerStyle(column) {
    return {
        state: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        type: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        name: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        permission: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        owner: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        group: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        size: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        modification_time: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        }
    }[column.field]

}

function rowStyle(row, index) {
    if (index % 2 === 0) {
        return {
            css: {
                background: '#2e2f39'
            }
        }
    } else {
        return {
            css: {
                background: '#7a7a7a'
            }
        }
    }
}

function folderFormatter(value, row) {
    var icon = '';
    if (row['type'] === 'FILE') {
        icon = 'fa-file';
    } else if (row['type'] === 'DIRECTORY') {
        icon = 'fa-folder-open';
    }
    return '<span class="fa ' + icon + '"></span> ';
}

function fmt_bytes(value, row, index) {
    var UNITS = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB'];
    var prev = 0, i = 0;
    while (Math.floor(value) > 0 && i < UNITS.length) {
        prev = value;
        value /= 1024;
        i += 1;
    }

    if (i > 0) {
        value = prev;
        i -= 1;
    }
    return Math.round(value * 100) / 100 + ' ' + UNITS[i];
}

/*************************************************************************************
 * bootstrapTable column formatter - End
 *************************************************************************************/


/*************************************************************************************
 * hdfs permission function - start
 *************************************************************************************/
/**
 * permission Table init and load Data
 * @param value
 * @param currentPath
 */
function permission_set(value) {
    var strArray = value.split("");
    var dataRow = [{
        "permUser": strArray[0] + "_Read_userR",
        "permGroup": strArray[3] + "_Read_groupR",
        "permOther": strArray[6] + "_Read_otherR"
        },
        {
            "permUser": strArray[1] + "_Write_userW",
            "permGroup": strArray[4] + "_Write_groupW",
            "permOther": strArray[7] + "_Write_otherW"
        },
        {
            "permUser": strArray[2] + "_Execute_userE",
            "permGroup": strArray[5] + "_Execute_groupE",
            "permOther": strArray[8] + "_Execute_otherE"
        }];

    if (strArray.length === 10) {
        if (strArray[9] === 't')
            $('#explorer-perm-sticky').attr('checked', true);
    }

    $('#permTable').bootstrapTable('load', dataRow);
    $('#modalTable').modal();
}

$('#modal-permission-set-button').click(function () {
    var permission = ['-', '-', '-', '-', '-', '-', '-', '-', '-', '-'];

    if ($('#userR').prop("checked")) {
        permission[0] = "r";
    }
    if ($('#userW').prop("checked")) {
        permission[1] = "w";
    }
    if ($('#userE').prop("checked")) {
        permission[2] = "x";
    }
    if ($('#groupR').prop("checked")) {
        permission[3] = "r";
    }
    if ($('#groupW').prop("checked")) {
        permission[4] = "w";
    }
    if ($('#groupE').prop("checked")) {
        permission[5] = "x";
    }
    if ($('#otherR').prop("checked")) {
        permission[6] = "r";
    }
    if ($('#otherW').prop("checked")) {
        permission[7] = "w";
    }
    if ($('#otherE').prop("checked")) {
        permission[8] = "x";
    }
    if ($('#explorer-perm-sticky').prop("checked")) {
        permission[9] = "t";
    }

    $.ajax({
            "url": "./files" + current_path + "?op=PERMISSION&flag=" + permission.join(''),
            "type": "GET",
            success: function (data) {
                var d = JSON.parse(data);
                if (d['result'] === "fail") {
                    show_err_msg(d['msg']);
                } else {
                    loadTable(current_directory, "");
                }
            }
        }
    ).done(function () {

    }).always(function () {
        $('#modalTable').modal('hide');
    });
});

/*************************************************************************************
 * hdfs permission function - end
 *************************************************************************************/


/*************************************************************************************
 * hdfs file upload function - start
 *************************************************************************************/
/**
 * file upload modal
 */
$('#modal-upload-file-input').change(function () {
    var path = "/";
    if (current_directory !== "") {
        path = current_directory
    }
    $('#file-upload-title').text("Up Load File Path - " + path);
    if ($('#modal-upload-file-input').prop('files').length > 0) {
        $('#modal-upload-file-button').prop('disabled', false);
    } else {
        $('#modal-upload-file-button').prop('disabled', true);
    }
});

/**
 * file upload button click Event
 */
$('#btn-upload-files').click(function () {
    $('#modal-upload-file-button').prop('disabled', true).button('reset');
    $('#modal-upload-file-input').val(null);
});


/**
 * file upload modal upload button event
 */
$('#modal-upload-file-button').click(function () {
    $('#modal-upload-file-button').prop('disabled', true);

    var form = $('#fileUploadFrom')[0];
    var data = new FormData(form);

    data.append("currentPath", current_directory);

    $.ajax({
        type: 'POST',
        enctype: 'multipart/form-data',
        url: "./upload/",
        data: data,
        processData: false,
        contentType: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            loadTable(current_directory, "");
        }
    }).always(function () {
        $('#modal-upload-file').modal('hide');
        $('#modal-upload-file-button').button('reset');

    });
});

/*************************************************************************************
 * hdfs file upload function - End
 *************************************************************************************/


/*************************************************************************************
 * hdfs file delete function - start
 *************************************************************************************/
/**
 * file delete button click event
 */
$('#btn-delete').click(function () {
    var rowDatas = JSON.stringify($('#table').bootstrapTable('getAllSelections'));
    var rowData = JSON.parse(rowDatas);
    delete_path(rowData);
});

function delete_path(rowDatas) {
    if (rowDatas.length === 1) {
        var name = rowDatas[0]['name'];
        var path = current_directory + name;
        $('#delete-modal-title').text("Delete - " + path);
        $('#delete-prompt').text("Are you sure you want to delete " + path + " ?");
    } else {
        $('#delete-modal-title').text("Delete - " + rowDatas.length + "개 파일");
        $('#delete-prompt').text("Are you sure you want to delete " + rowDatas.length + "개 파일");
    }

    $('#delete-button').click(function () {
        for (var num in rowDatas) {
            var name = rowDatas[num]['name'];
            var path = current_directory + name;

            var url = "./files" + path + "?op=DELETE&flag=";
            $.ajax({
                    "url": url,
                    "type": "GET",
                    success: function (data) {
                        var mData = JSON.parse(data);
                        if (mData['result'] === 'fail') show_err_msg(mData['msg']);
                    }
                }
            ).done(function () {
                loadTable(current_directory, "");
            }).always(function () {
                $('#delete-modal').modal('hide');
                $('#delete-button').button('reset');
                $('#delete-modal').modal();
            })
        }
    });
}

/*************************************************************************************
 * hdfs file delete function - end
 *************************************************************************************/


/*************************************************************************************
 * hdfs view Detail function - start
 *************************************************************************************/

function view_file_detail(path) {
    $('#file-info').modal();
    $('#file-info-tail').hide();
    $('#file-info-title').text("File information - " + path);
    $('#file-info-preview-head').off('click').on('click', function () {
        var url = "./files" + path + "?op=READ&flag=";
        $.ajax({
            "url": url,
            "type": "GET",
            success: function (data) {
                var mData = JSON.parse(data);
                if (mData['result'] === 'fail') {
                    show_err_msg(mData["data"]);
                    $('#file-info-preview-body').val("Error! Check Error Massage!!");
                } else {
                    $('#file-info-preview-body').val(mData["data"]);
                }
            }
        }).done(function (data) {
            $('#file-info-tail').show();
        })

    });

    var download_url = "./download" + path + "?op=DOWNLOAD";

    $('#file-info-download').off('click').on('click', function () {
        $('#file-info-download').attr('href', download_url);
        $('#file-info').modal('hide');
    });
}


/*************************************************************************************
 * hdfs view Detail function - end
 *************************************************************************************/


/*************************************************************************************
 * hdfs create directory function - start
 *************************************************************************************/
//CREATE Folder
$('#btn-create-directory').on('show.bs.modal', function (event) {
    var directory = "/";
    if (current_directory !== "") {
        directory = current_directory;
    }
    $('#new_directory').val('');
    $('#new_directory_pwd').text(directory);
});


//CREATE FOLDER SEND
$('#btn-create-directory-send').click(function () {
    // $(this).prop('disabled', true);
    // $(this).button('complete');
    var create_folder_path = current_directory + $('#new_directory').val();
    var url = "./files" + create_folder_path + "?op=MKDIR&flag=";

    $.ajax({
            "url": url,
            "type": "GET",
            success: function (data) {
                var mData = JSON.parse(data);
                if (mData['result'] === 'fail') show_err_msg(mData['msg']);
            }
        }
    ).done(function () {
        loadTable(current_directory, "");
    }).always(function () {
        $('#btn-create-directory').modal('hide');
        $('#btn-create-directory-send').button('reset');
    });
});

/*************************************************************************************
 * hdfs create directory function - end
 *************************************************************************************/


/*************************************************************************************
 * hdfs cut & paste function - start
 *************************************************************************************/
$('#explorer-cut').click(function () {
    var rowDatas = JSON.stringify($('#table').bootstrapTable('getAllSelections'));
    sessionStorage.setItem("data", rowDatas);
    sessionStorage.setItem("sourcePath", current_directory);
    alert("Cut " + JSON.parse(rowDatas).length + " file/directory");
});

$('#explorer-paste').click(function () {
    var rowData = JSON.parse(sessionStorage.getItem("data"));
    var source = sessionStorage.getItem("sourcePath");

    if (rowData.length === 0) {
        alert("file/directory not selected");
    }

    for (var num in rowData) {
        var name = rowData[num]['name'];
        var destinationPath = current_directory + name;
        var sourcePath = source + name;

        var url = "./rename" + sourcePath + "?op=RENAME&destination=" + destinationPath;
        $.ajax({
                "url": url,
                "type": "GET",
                success: function (data) {
                    var mData = JSON.parse(data);
                    if (mData['result'] === 'fail') show_err_msg(mData['msg']);
                }
            }
        ).done(function () {
            loadTable(current_directory, "");
            sessionStorage.clear();
        }).always(function () {

        })
    }
});


$(window).on('load', function () {
    initTable();
    loadTable("", "FIRST");
});
