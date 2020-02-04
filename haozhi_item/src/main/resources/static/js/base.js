var Length = document.documentElement.clientWidth;
var baseWidth = Length <= 1024 ? Length : 1024 < Length ? 750 : '';
document.documentElement.style.fontSize = baseWidth / 750 * 100 + 'px';

//回退
window.onload = function() {
    var $back = document.getElementsByClassName('backImg')[0];
    if ($back) {
        $back.onclick = function() {
            window.history.go(-1);
        }
    }
}