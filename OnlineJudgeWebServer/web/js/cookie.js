/**
 * @desc  设置Cookie
 * @param {String} name
 * @param {String} value
 * @param {Number} expires
 */
function setCookie(name, value, expires) {
    var cookieString = name + "=" + encodeURI(value);
    //判斷是否設置過期時間,0代表關閉瀏覽器時失效
    if (expires > 0) {
        var date = new Date();
        date.setTime(date.getTime() + expires * 1000);
        cookieString = cookieString + ";expires=" + date.toUTCString();
    }
    document.cookie=cookieString;
}

/**
 * @desc 根据name读取cookie
 * @param  {String} name
 * @return {String}
 */
function getCookie(name) {
    var arr = document.cookie.replace(/\s/g, "").split(';');
    for (var i = 0; i < arr.length; i++) {
        var tempArr = arr[i].split('=');
        if (tempArr[0] === name) {
            return decodeURI(tempArr[1]);
        }
    }
    return '';
}

/**
 * @desc 根据name修改cookie
 * @param  {String} name
 * @param  {String} value
 * @param  {Number} expires
 */
function editCookie(name, value, expires){
    var cookieString = name + "=" + encodeURI(value);
    if (expires > 0) {
        var date = new Date();
        date.setTime(date.getTime() + expires * 1000);
        cookieString = cookieString + ";expires=" + date.toGMTString();
    }
    document.cookie = cookieString;
}

/**
 * @desc 根据name删除cookie
 * @param  {String} name
 */
function removeCookie(name) {
    // 设置已过期，系统会立刻删除cookie
    setCookie(name, '', 1);
}