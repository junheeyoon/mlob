var bno = 0;
var type = '';
var action = '';

$(document).ready(function () {
    $("#modify").click(function () {
        action = 'modify';
        type = 'PUT';
        bno = this.value;

        var row = $(this).parent().parent().parent();
        var tr = row.children();
        var schedulerName = tr.eq(2).text();
        var contents = tr.eq(1).text();
        console.log("!11");

        $("#modal-title").text("수정하기")
        $("#schedulerName").val(schedulerName);
        $("#contents").val(contents);
        $("#modifyModal").modal();
    });
});