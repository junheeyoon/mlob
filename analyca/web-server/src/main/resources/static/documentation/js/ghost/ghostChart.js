let chartjs_cpu;
let chartjs_mem;
let chartjs_disk;
let chartjs_container_cpu;
let chartjs_container_mem;

function loadContainerCpuChart(containerName, period, type) {
    let chartLabelCpu = [];
    let chartDataCpu = [];
    $.ajax({
        url: "/ghost/metrics/" + containerName + "/" + period + "/cpu",
        type: "POST",
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            $.each(JSON.parse(data), function (inx, obj) {
                chartLabelCpu.push(obj.time);
                chartDataCpu.push(obj.cpu);
            });
            updateCpuChart(containerName, chartDataCpu, chartLabelCpu, type);
        }
    });
}

function loadContainerMemChart(containerName, period, type) {
    let chartLabelMem = [];
    let memTotal = [];
    let memUsed = [];
    $.ajax({
        url: "/ghost/metrics/" + containerName + "/" + period + "/mem",
        type: "POST",
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            $.each(JSON.parse(data), function (inx, obj) {
                chartLabelMem.push(obj.time);
                memTotal.push(obj.totalMemory);
                memUsed.push(obj.usedMemory)
            });
            updateMemChart(containerName, memUsed, memTotal, chartLabelMem, type);
        }
    });
}

function updateCpuChart(name, data, label, type) {
    if (type === "node") {
        chartjs_cpu.data.labels = label;
        chartjs_cpu.data.datasets[0].data = data;
        chartjs_cpu.data.datasets[0].label = name + ' Usage CPU(%)';
        chartjs_cpu.update();
    } else {
        chartjs_container_cpu.data.labels = label;
        chartjs_container_cpu.data.datasets[0].data = data;
        chartjs_container_cpu.data.datasets[0].label = name + ' Usage CPU(%)';
        chartjs_container_cpu.update();
    }
}

function updateMemChart(name, data1, data2, label, type) {
    if (type === "node") {
        chartjs_mem.data.labels = label;
        chartjs_mem.data.datasets[0].data = data1;
        chartjs_mem.data.datasets[1].data = data2;
        chartjs_mem.data.datasets[0].label = name + ' Total Mem(GB)';
        chartjs_mem.data.datasets[1].label = name + ' Used Mem(GB)';
        chartjs_mem.update();
    } else {
        chartjs_container_mem.data.labels = label;
        chartjs_container_mem.data.datasets[0].data = data1;
        chartjs_container_mem.data.datasets[1].data = data2;
        chartjs_container_mem.data.datasets[0].label = name + ' Total Mem(GB)';
        chartjs_container_mem.data.datasets[1].label = name + ' Used Mem(GB)';
        chartjs_container_mem.update();
    }
}

function updateDiskChart(name, data1, data2, label, type) {
    chartjs_disk.data.labels = label;
    chartjs_disk.data.datasets[0].data = data1;
    chartjs_disk.data.datasets[1].data = data2;
    chartjs_disk.data.datasets[0].label = name + ' Total DISK(TB)';
    chartjs_disk.data.datasets[1].label = name + ' Used DISK(TB)';
    chartjs_disk.update();
}

function createCpuChart(hostname, chartLabel, chartData, id, type) {
    let ctx = document.getElementById(id);

    let chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: chartLabel,
            datasets: [{
                label: hostname + ' Usage CPU(%)',
                data: chartData,
                backgroundColor: [
                    'rgba(54, 162, 235, 0.2)',
                ],
                borderColor: [
                    'rgba(54, 162, 235, 1)',
                ],
                borderWidth: 1,
                pointRadius: 0,
            }
            ]
        },
        options: {
            legend: {
                labels: {
                    fontColor: 'rgb(18,18,26)'
                }
            },
            scales: {
                yAxes: [{
                    ticks: {
                        min: 0,
                        max: 100,
                        fontColor: 'rgb(60,61,71)'
                    }
                }],
                xAxes: [{
                    ticks: {
                        display: false
                    }
                }]
            }
        }
    });

    if (type === "node") {
        chartjs_cpu = chart;
    } else {
        chartjs_container_cpu = chart;
    }
}


function createChartMem(hostname, chartLabel, chartData1, chartData2, id, type) {
    let ctx = document.getElementById(id);

    let chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: chartLabel,
            datasets: [{
                label: hostname + ' Total Mem(GB)',
                data: chartData1,
                backgroundColor: [
                    'rgba(54, 162, 235, 0.2)',
                ],
                borderColor: [
                    'rgba(54, 162, 235, 1)',
                ],
                borderWidth: 1,
                pointRadius: 0,
            }, {
                label: hostname + ' Used Mem(GB)',
                data: chartData2,
                backgroundColor: [
                    'rgba(255,20,147, 0.2)',
                ],
                borderColor: [
                    'rgba(255, 20, 147, 1)',
                ],
                borderWidth: 1,
                pointRadius: 0,
            }
            ]
        },
        options: {
            legend: {
                labels: {
                    fontColor: 'rgb(18,18,26)'
                }
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        fontColor: 'rgb(60,61,71)'
                    }
                }],
                xAxes: [{
                    ticks: {
                        display: false
                    }
                }]
            },
            zoom: {
                enabled: true,
                drag: false,
                mode: 'xy',
                limits: {
                    max: 10,
                    min: 0.5
                }
            }
        }
    });

    if (type === "node") {
        chartjs_mem = chart;
    } else {
        chartjs_container_mem = chart;
    }
}


function createChartDisk(hostname, chartLabel, chartData1, chartData2, id, type) {
    let ctx = document.getElementById(id);

    chartjs_disk = new Chart(ctx, {
        type: 'line',
        data: {
            labels: chartLabel,
            datasets: [{
                label: hostname + ' Total Disk(TB)',
                data: chartData1,
                backgroundColor: [
                    'rgba(54, 162, 235, 0.2)',
                ],
                borderColor: [
                    'rgba(54, 162, 235, 1)',
                ],
                borderWidth: 1,
                pointRadius: 0,
            }, {
                label: hostname + ' Used Disk(TB)',
                data: chartData2,
                backgroundColor: [
                    'rgba(255,20,147, 0.2)',
                ],
                borderColor: [
                    'rgba(255, 20, 147, 1)',
                ],
                borderWidth: 1,
                pointRadius: 0,
            }
            ]
        },
        options: {
            legend: {
                labels: {
                    fontColor: 'rgb(18,18,18)'
                }
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        fontColor: 'rgb(60,61,71)'
                    }
                }],
                xAxes: [{
                    ticks: {
                        display: false
                    }
                }]
            },
            zoom: {
                enabled: true,
                drag: false,
                mode: 'xy',
                limits: {
                    max: 10,
                    min: 0.5
                }
            }
        }
    });
}
