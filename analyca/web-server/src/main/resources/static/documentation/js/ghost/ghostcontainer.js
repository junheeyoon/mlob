document.write("<script language=\"javascript\" src=\"/documentation/js/ghost/rightSideBarFunc.js\"></script>");

let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

/******************************************
 *  Container Status Table
 ******************************************/
function initTable() {
    $('#container-status-table').bootstrapTable({
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

function findAllContainers() {
    // let jsonResult = '[{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"6260e26f7b8f_gitlab","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2019-08-07T08:08:10.218534875Z","status":"running","imageName":"gitlab/gitlab-ce","imageTag":"latest","binds":"[/bigdata/platform/gitlab/logs:/var/log/gitlab:rw /bigdata/platform/gitlab/config:/etc/gitlab:rw /bigdata/platform/gitlab/data:/var/opt/gitlab:rw /bigdata/platform/gitlab/backups:/var/opt/gitlab/backups:rw]","ports":" 22/tcp:8022  8101/tcp:8101 "},{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"gitlab-runner","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2019-08-07T08:08:10.220182313Z","status":"running","imageName":"gitlab/gitlab-runner","imageTag":"latest","binds":"[/bigdata/platform/gitlab/gitlab-runner/config:/etc/gitlab-runner:rw /var/run/docker.sock:/var/run/docker.sock:rw]","ports":""},{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"elated_benz","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2020-03-19T11:45:26.751665692Z","status":"running","imageName":"gitlab/gitlab-runner","imageTag":"","binds":"[/srv/gitlab-runner/config:/etc/gitlab-runner]","ports":""},{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"youthful_banach","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2020-01-30T02:26:52.653989212Z","status":"running","imageName":"telegraf","imageTag":"latest","binds":"[/bigdata/telegraf/conf/telegraf.conf:/etc/telegraf/telegraf.conf]","ports":""},{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"idc-registry","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2019-08-07T08:08:10.174996279Z","status":"running","imageName":"registry","imageTag":"2","binds":"[/bigdata/platform/docker/registry:/var/lib/registry]","ports":" 5000/tcp:4506 "}]';

    $.ajax({
        url: "/ghost/ps/",
        type: "POST",
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            // $('#container-status-table').bootstrapTable('load',JSON.parse(jsonResult));
            $('#container-status-table').bootstrapTable('removeAll');
            $('#container-status-table').bootstrapTable('load', data);
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
                color: '#b8cfe6',
                background: '#51525d'
            }
        }
    } else {
        return {
            css: {
                color: '#b8cfe6',
                background: '#636363'
            }
        }
    }
}

$(window).on('load', function () {
    let height = document.getElementById("navbarSupportedContent").clientHeight;
    document.getElementById("mySidenav").style.top = height + "px";
    initTable();
    createCpuChart("", [], [], 'containerCpuChart', 'container');
    createChartMem("", [], [], [], 'containerMemChart', 'container');
    findAllContainers();

});