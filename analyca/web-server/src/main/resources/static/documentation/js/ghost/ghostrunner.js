document.write("<script language=\"javascript\" src=\"/documentation/js/ghost/rightSideBarFunc.js\"></script>");

let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");


$('.collapse').on('shown.bs.collapse', function () {
    $(this).parent().find(".fa-angle-down").removeClass("fa-angle-down").addClass("fa-angle-up");
}).on('hidden.bs.collapse', function () {
    $(this).parent().find(".fa-angle-up").removeClass("fa-angle-up").addClass("fa-angle-down");
});

$('.panel-heading a').click(function () {
    $('.panel-heading').removeClass('active');
    //If the panel was open and would be closed by this click, do not active it
    if (!$(this).closest('.panel').find('.panel-collapse').hasClass('in'))
        $(this).parents('.panel-heading').addClass('active');
});

$('#ghost-registory').slimScroll({
    height: window.innerHeight - 50
});

$('#service-zone').slimScroll({
    height: window.innerHeight - 50
});


$('#image-body-list-card').slimScroll({
    height: 450
});

/**
 * deploy modal open
 */
document.getElementById("btn-deploy").onclick = function () {
    getHostList();
    loadStepList();
    loadPrivateRegistry();
    // document.getElementById("hostNameInput").value = window.localStorage['selectHost'];
};

function getImageInfo() {
    let imageNTag = document.getElementById("inputImage").children[0].id.split('__');
    let cpuSpec = document.getElementById("inputSpec").value;
    let memSpec = document.getElementById("memSpec").value;
    let memMetric = document.getElementById("memStack").value;

    let memSpecFinal = "";
    if (memMetric === "mega") {
        memSpecFinal = memSpec + "m";
    } else {
        memSpecFinal = memSpec + "g";
    }

    return {
        hostName: document.getElementById("hostNameInput").value,
        imageName: imageNTag[0],
        tag: imageNTag[1],
        containerName: document.getElementById("inputContainerName").value,
        port: document.getElementById("inputPortInfo").value,
        volume: document.getElementById("inputVolume").value,
        options: document.getElementById("inputOptions").value,
        memory: memSpecFinal,
        cpu: cpuSpec,
        gpu: false,
        commands: document.getElementById("inputCommands").value
    };
}

/**
 * 아미지 배포 실행
 */
document.getElementById("image-deploy-button").onclick = function () {

    $.ajax({
        url: "/ghost/run",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(getImageInfo()),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }, success: function (mdata) {
            reloadTable(requestJson['hostName']);
        }
    });

};

function loadPrivateRegistry() {
    let imageNTagList = "";
    $.ajax({
        url: "/ghost/images",
        type: "POST",
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            for (let i = 0; i < data.length; i++) {
                imageNTagList += '<div class="card" id="heading' + i + '"><h5 class="mb-0">';
                imageNTagList += '<button class="btn btn-link collapsed" data-toggle="collapse" data-target="#collapse' + i + '" aria-controls="collapse' + i + '" aria-expanded="false">'
                imageNTagList += '<span class="fas fa-angle-down mr-3"></span><span>' + data[i].name + '</span>';
                imageNTagList += '</button></h5></div>';
                imageNTagList += '<div id="collapse' + i + '" class="collapse" aria-labelledby="heading' + i + '" data-parent="#image-accordion">';
                imageNTagList += '<div class="card" id="start-panel">';
                for (let j = 0; j < data[i].tags.length; j++) {
                    let imageTagName = data[i].name + "__" + data[i].tags[j];
                    // imageNTagList += '<button id="' +imageTagName +'" class="btn-outline-primary" draggable="true" ondragstart="onDragStart(event);">';
                    imageNTagList += '<button id="' + imageTagName + '" class="btn-outline-primary" onclick="registerImage(this)">';
                    imageNTagList += '<span>' + data[i].name + ':' + data[i].tags[j] + '</span></button>';
                }
                imageNTagList += '</div></div>';
            }

            document.getElementById("image-body-list-card").innerHTML = imageNTagList;
        }
    });

}

function registerImage(elmnt) {
    let targetId = elmnt.id;
    // let parentID = document.getElementById(targetId).parentElement.id;
    elmnt.effectAllowed = "copy";
    let nodeCopy = document.getElementById(targetId).cloneNode(true);
    // 기존에 있던 요소 모두 삭제
    document.getElementById("inputImage").querySelectorAll("*").forEach(n => n.remove());
    document.getElementById("inputImage").appendChild(nodeCopy);

    document.getElementById("image-deploy-button").disabled = false;
    document.getElementById("image-save-button").disabled = false;
}

/**
 * Service Node List 조회
 */
function getHostList() {
    let hostListHTML = "";
    let targetHostName = window.localStorage['selectHost'];
    $.ajax({
            "url": './node/status/all',
            "type": "GET",
            success: function (data) {
                for (let i = 0; i < data.length; i++) {
                    const hostname = data[i].hostname;
                    if (targetHostName === hostname) {
                        hostListHTML += '<option value="' + hostname + '" selected>' + hostname + '</option>'
                    } else {
                        hostListHTML += '<option value="' + hostname + '">' + hostname + '</option>'
                    }
                }

                document.getElementById("hostNameInput").innerHTML = hostListHTML;
            }
        }
    );
}

function initImageList() {
    let listImages = "";
    let statusCSS = "";

    $.ajax({
            "url": './node/status/all',
            "type": "GET",
            success: function (data) {
                for (let i = 0; i < data.length; i++) {
                    if (data[i].status === 'GOOD') {
                        statusCSS = 'list-group-item-success';
                    } else if (data[i].status === 'WARN') {
                        statusCSS = 'list-group-item-warning';
                    } else {
                        statusCSS = 'list-group-item-danger';
                    }

                    const hostname = data[i].hostname;

                    listImages += '<a href="javascript:void(0);" id=' + data[i].hostname + ' onclick="nodeClick(event);" class="list-group-item ' + statusCSS + ' list-group-item-action flex-column align-items-start">';
                    listImages += '<div class="d-flex w-100 justify-content-between">';
                    listImages += '<h5 class="mb-1">' + hostname + '</h5></div></a>';
                }

                document.getElementById("image-list-group").innerHTML = listImages;
                if (data.length > 0) getContainerbyHostname(data[0].hostname);
            }
        }
    );
}

function nodeClick(event) {
    loadCpuChart(event.currentTarget.id, '1h', 'node');
    loadMemChart(event.currentTarget.id, '1h', 'node');
    loadDiskChart(event.currentTarget.id, '1h', 'node');
    window.localStorage['selectHost'] = event.currentTarget.id;
    getContainerbyHostname(event.currentTarget.id);
}

function loadCpuChart(hostname, period) {
    let chartLabel = [];
    let chartData = [];
    $.ajax({
        "url": './node/status/cpu?hostname=' + hostname + '&period=' + period,
        "type": "GET",
        success: function (data) {
            $.each(JSON.parse(data), function (inx, obj) {
                chartLabel.push(obj.time);
                chartData.push(obj.usage_user);
            });
            updateCpuChart(hostname, chartData, chartLabel, 'node');
        }
    });
}

function loadMemChart(hostname, period, chartName) {
    let chartLabel = [];
    let memTotal = [];
    let memUsed = [];
    $.ajax({
        "url": './node/status/mem?hostname=' + hostname + '&period=' + period,
        "type": "GET",
        success: function (data) {
            $.each(JSON.parse(data), function (inx, obj) {
                chartLabel.push(obj.time);
                memTotal.push(obj.total);
                memUsed.push(obj.used);
            });
            updateMemChart(hostname, memTotal, memUsed, chartLabel, 'node');
        }
    });
}

function loadDiskChart(hostname, period, chartName) {
    let chartLabel = [];
    let diskTotal = [];
    let diskUsed = [];
    $.ajax({
        "url": './node/status/disk?hostname=' + hostname + '&period=' + period,
        "type": "GET",
        success: function (data) {
            $.each(JSON.parse(data), function (inx, obj) {
                chartLabel.push(obj.time);
                diskTotal.push(obj.total);
                diskUsed.push(obj.used);
            });
            updateDiskChart(hostname, diskTotal, diskUsed, chartLabel);
        }
    });
}


/******************************************
 *  Container ps Table
 ******************************************/
function initTable() {
    $('#container-ps-table').bootstrapTable({
        // data: d["data"],
        search: true,
        columns: [
            {
                title: 'HOSTNAME',
                field: 'hostName',
                align: 'center',
                valign: 'middle'
            }, {
                title: 'NAME',
                field: 'containerName',
                align: 'left',
                valign: 'middle'
            }, {
                title: 'CREATOR',
                field: 'creator',
                align: 'center',
                valign: 'middle'
            }, {
                title: 'CPU',
                field: 'cpu',
                align: 'center',
                valign: 'middle'
            }, {
                title: 'MEM',
                field: 'mem',
                align: 'center',
                valign: 'middle'
            }, {
                title: 'GPU',
                field: 'gpu',
                valign: 'center',
            }, {
                title: 'CREATEDAT',
                field: 'createdAt',
                valign: 'center'
            }, {
                title: 'STATUS',
                field: 'status',
                valign: 'center'
            }
        ],
        onClickCell: function (field, value, row, $element) {
            openNav(row);
        }
    });
}

/**
 * CORS 문제로 샘플 데이터만 가지고 동작을 구현
 */
function getContainerbyHostname(hostname) {
    // let jsonResult = '[{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"6260e26f7b8f_gitlab","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2019-08-07T08:08:10.218534875Z","status":"running","imageName":"gitlab/gitlab-ce","imageTag":"latest","binds":"[/bigdata/platform/gitlab/logs:/var/log/gitlab:rw /bigdata/platform/gitlab/config:/etc/gitlab:rw /bigdata/platform/gitlab/data:/var/opt/gitlab:rw /bigdata/platform/gitlab/backups:/var/opt/gitlab/backups:rw]","ports":" 22/tcp:8022  8101/tcp:8101 "},{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"gitlab-runner","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2019-08-07T08:08:10.220182313Z","status":"running","imageName":"gitlab/gitlab-runner","imageTag":"latest","binds":"[/bigdata/platform/gitlab/gitlab-runner/config:/etc/gitlab-runner:rw /var/run/docker.sock:/var/run/docker.sock:rw]","ports":""},{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"elated_benz","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2020-03-19T11:45:26.751665692Z","status":"running","imageName":"gitlab/gitlab-runner","imageTag":"","binds":"[/srv/gitlab-runner/config:/etc/gitlab-runner]","ports":""},{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"youthful_banach","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2020-01-30T02:26:52.653989212Z","status":"running","imageName":"telegraf","imageTag":"latest","binds":"[/bigdata/telegraf/conf/telegraf.conf:/etc/telegraf/telegraf.conf]","ports":""},{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"idc-registry","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2019-08-07T08:08:10.174996279Z","status":"running","imageName":"registry","imageTag":"2","binds":"[/bigdata/platform/docker/registry:/var/lib/registry]","ports":" 5000/tcp:4506 "}]';
    $.ajax({
        url: "/ghost/ps/" + hostname,
        type: "POST",
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            // console.log(JSON.parse(jsonResult));
            // $('#container-ps-table').bootstrapTable('load',JSON.parse(jsonResult));
            $('#container-ps-table').bootstrapTable('removeAll');
            $('#container-ps-table').bootstrapTable('load', data);

        }
    });
}

function headerStyle(column) {
    return {
        hostName: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        hostIp: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        containerName: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        creator: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        cpu: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        mem: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        gpu: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        createdAt: {
            css: {
                color: 'white',
                background: '#4d586d'
            }
        },
        status: {
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

$(window).on('load', function () {
    let height = document.getElementById("navbarSupportedContent").clientHeight;
    document.getElementById("mySidenav").style.top = height + "px";
    initTable();
    initImageList();

    createCpuChart("", [], [], 'cpuChart', 'node');
    createChartMem("", [], [], [], 'memChart', 'node');
    createChartDisk("", [], [], [], 'diskChart', 'node')
    createCpuChart("", [], [], 'containerCpuChart', 'container');
    createChartMem("", [], [], [], 'containerMemChart', 'container');
    loadCpuChart('ai-big-node04', '1h', 'node');
    loadMemChart('ai-big-node04', '1h', 'node');
    loadDiskChart('ai-big-node04', '1h');
});


/**
 * 아미지 배포 환경 세팅 저장
 */
document.getElementById("image-save-button").onclick = function () {

    $.ajax({
        url: "/step/save",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(getImageInfo()),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }, success: function (mdata) {
            // reloadTable(requestJson['hostName']);
            loadStepList();
            alert("Image Setting Saved");
        }, error: function (e) {
            console.log(e);
            alert("Error");
        }
    });

};

/**
 * Saved Step list 조회
 */
function loadStepList() {
    let stepListHTML = "<option>--Select--</option>";
    $.ajax({
            url: "/step/list",
            type: "POST",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                //window.localStorage['savedSettings'] = data;
                for (let i = 0; i < data.length; i++) {
                    stepListHTML += '<option value="' + data[i].id + '">' + data[i].containerName + '</option>'
                }
                document.getElementById("savedStack").innerHTML = stepListHTML;
            }
        }
    );
}

/**
 * 사용자가 선택한 세팅을 불러옴
 * */
function loadSelectedStep(opt) {
    $.ajax({
            url: "/step/list/" + opt.value,
            type: "POST",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                document.getElementById(data[0].image + "__" + data[0].tag).click();
                document.getElementById("inputContainerName").value = data[0].containerName;
                document.getElementById("inputSpec").value = data[0].cpu;
                if (data[0].memory != "") {
                    let mem = data[0].memory.match(/[a-zA-Z]+|[0-9]+/g);
                    document.getElementById("memSpec").value = mem[0];
                    if (mem[1] == "g")
                        $("#memStack").val("giga").prop("selected", true);
                    else
                        $("#memStack").val("mega").prop("selected", true);
                } else {
                    document.getElementById("memSpec").value = "";
                }
                document.getElementById("inputPortInfo").value = data[0].port;
                document.getElementById("inputVolume").value = data[0].volume;
                document.getElementById("inputOptions").value = data[0].options;
                document.getElementById("inputCommands").value = data[0].commands;
            }
        }
    );


}