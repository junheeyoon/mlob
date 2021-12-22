document.write("<script language=\"javascript\" src=\"/documentation/js/ghost/ghostChart.js\"></script>");

function openNav(row) {
    document.getElementById("mySidenav").style.width = "55%";
    document.getElementById("container_name_hd").innerHTML = row['containerName'];
    document.getElementById("hostname").innerHTML = row['hostName'];
    // console.log(document.getElementById("hostname").textContent);
    document.getElementById("creator").innerHTML = row['creator'];
    document.getElementById("createdDate").innerHTML = row['createdAt'];
    document.getElementById("maxCpu").innerHTML = cpuFormatter(row['cpu']);
    document.getElementById("maxMem").innerHTML = fmt_bytes(row['mem']);
    getContainerInfo(row['hostName'], row['containerName']);
    loadContainerCpuChart(row['containerName'], '6h', 'container');
    loadContainerMemChart(row['containerName'], '6h', 'container');

    if (row['status'] === 'running') {
        document.getElementById("btn-start-container").disabled = true;
        document.getElementById("btn-delete-container").disabled = true;
        document.getElementById("btn-stop-container").disabled = false;
        document.getElementById("btn-stop-container").onclick = function () {
            stopNStartContainer(row['containerName'], row['hostName'], "stop")
        };
    } else {
        document.getElementById("btn-start-container").disabled = false;
        document.getElementById("btn-start-container").onclick = function () {
            stopNStartContainer(row['containerName'], row['hostName'], "start")
        };
        document.getElementById("btn-delete-container").disabled = false;
        document.getElementById("btn-stop-container").disabled = true;
        document.getElementById("btn-delete-container").onclick = function () {
            stopNStartContainer(row['containerName'], row['hostName'], "delete")
        };
    }
}

/**
 * Stop & Start Container Event
 * @param containerName
 * @param hostName
 * @param flagType
 */
function stopNStartContainer(containerName, hostName, flagType) {
    let modal_msg = "";
    let modal_action_msg = "";
    if (flagType === "stop") {
        modal_msg = "STOP";
        modal_action_msg = "stopping";
    } else if (flagType === "start") {
        modal_msg = "START";
        modal_action_msg = "starting";
    } else if (flagType === "delete") {
        modal_msg = "DELETE";
        modal_action_msg = "deleting";
    }

    document.getElementById("container-button-confirm").innerHTML = modal_msg;
    document.getElementById("modal-containerName").innerHTML = modal_action_msg + " " + containerName;
    document.getElementById("container-button-confirm").onclick = function () {
        stopNStartCommand(containerName, hostName, flagType)
    };

}

/**
 * send Stop API
 * @param containerName
 * @param hostName
 * @param flagType
 */
function stopNStartCommand(containerName, hostName, flagType) {
    let jsonBody = {"hostName": hostName, "containerName": containerName};
    let url = "";
    if (flagType === "stop") {
        url = "/ghost/stop";
    } else if (flagType === "start") {
        url = "/ghost/start";
    } else if (flagType === "delete") {
        url = "/ghost/rm";
    }

    $.ajax({
        url: url,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(jsonBody),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeNav();
            reloadTable(hostName);
        }
    });
    $("#container-modal").modal("hide");

}

function reloadTable(hostname) {
    if (window.location.href === "http://10.33.194.25:4206/ghost/containers") {
        setTimeout(findAllContainers, 3000);
    }
    if (window.location.href === "http://10.33.194.25:4206/ghost/runnerview") {
        setTimeout(getContainerbyHostname, 3000, hostname);
    }
}

/*
 * docker PS 정보를 따로 호출해서 container_name으로 매칭하여 필요한 정보만 가져온다.
 */
function getContainerInfo(hostname, tgContainerName) {
    // let jsonResult = '[{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"6260e26f7b8f_gitlab","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2019-08-07T08:08:10.218534875Z","status":"running","imageName":"gitlab/gitlab-ce","imageTag":"latest","binds":"[/bigdata/platform/gitlab/logs:/var/log/gitlab:rw /bigdata/platform/gitlab/config:/etc/gitlab:rw /bigdata/platform/gitlab/data:/var/opt/gitlab:rw /bigdata/platform/gitlab/backups:/var/opt/gitlab/backups:rw]","ports":" 22/tcp:8022  8101/tcp:8101 "},{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"gitlab-runner","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2019-08-07T08:08:10.220182313Z","status":"running","imageName":"gitlab/gitlab-runner","imageTag":"latest","binds":"[/bigdata/platform/gitlab/gitlab-runner/config:/etc/gitlab-runner:rw /var/run/docker.sock:/var/run/docker.sock:rw]","ports":""},{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"elated_benz","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2020-03-19T11:45:26.751665692Z","status":"running","imageName":"gitlab/gitlab-runner","imageTag":"","binds":"[/srv/gitlab-runner/config:/etc/gitlab-runner]","ports":""},{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"youthful_banach","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2020-01-30T02:26:52.653989212Z","status":"running","imageName":"telegraf","imageTag":"latest","binds":"[/bigdata/telegraf/conf/telegraf.conf:/etc/telegraf/telegraf.conf]","ports":""},{"hostName":"ai-big-node04","hostIp":"10.33.194.14","containerName":"idc-registry","creator":"","cpu":"0","mem":"0","gpu":"","createdAt":"2019-08-07T08:08:10.174996279Z","status":"running","imageName":"registry","imageTag":"2","binds":"[/bigdata/platform/docker/registry:/var/lib/registry]","ports":" 5000/tcp:4506 "}]';
    $.ajax({
        url: "/ghost/ps/" + hostname,
        type: "POST",
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            console.log(data);
            // let parsedJsonResult = JSON.parse(data);
            let parsedJsonResult = data;
            for (let i in parsedJsonResult) {
                let conName = parsedJsonResult[i].containerName;
                if (conName === tgContainerName) {
                    document.getElementById("ports").innerHTML = parsedJsonResult[i].ports;
                    document.getElementById("bindPath").innerHTML = parsedJsonResult[i].binds;
                    document.getElementById("imageName").innerHTML = parsedJsonResult[i].imageName;
                    document.getElementById("tagVersion").innerHTML = parsedJsonResult[i].imageTag;
                    return;
                }
            }
            // $('#container-ps-table').bootstrapTable('load',JSON.parse(jsonResult));

        }
    });
}

/* Set the width of the side navigation to 0 */
function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}


function cpuFormatter(value, row, index) {
    return value/1000000000;
}


function fmt_bytes(value, row, index) {
    let UNITS = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB'];
    let prev = 0, i = 0;
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

function dateFormatter(value, row, index) {
    return value.split('.')[0];
    // if (row['type'] !== 'LOCAL') {
    //     var cutoff = moment().subtract(6, 'months').unix() * 1000;
    //     if (value < cutoff) {
    //         return moment(Number(value)).format('MMM DD YYYY');
    //     } else {
    //         return moment(Number(value)).format('MMM DD HH:mm');
    //     }
    // }
}