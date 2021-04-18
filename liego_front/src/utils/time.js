export function addZero(num) {
    return num < 10 ? '0' + num : num;
}
export function formatDateTime(date) {
    var time = new Date(Date.parse(date));
    time.setTime(time.setHours(time.getHours()));
    console.log(time);
    var Y = time.getFullYear() + '-';
    var M = addZero(time.getMonth() + 1) + '-';
    var D = addZero(time.getDate()) + ' ';
    var h = addZero(time.getHours()) + ':';
    var m = addZero(time.getMinutes()) + ':';
    var s = addZero(time.getSeconds());
    return Y + M + D + h + m + s;
}