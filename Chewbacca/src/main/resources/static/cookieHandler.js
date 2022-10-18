function setCookie(name, value, days) {
    let d = new Date();
    d.setTime(d.getTime() + (days*24*60*60*1000));
    document.cookie = name + "=" + value + ";expires:" + d.toUTCString() + ";samesite=strict";
}

function getCookie(name, defaultValue = "") {
    let decodedC = decodeURIComponent(document.cookie);
    let arr = decodedC.split(";");
    for(let i = 0; i < arr.length; i++) {
        let c = arr[i];
        while(c.charAt(0) === " ") {
            c = c.substring(1);
        }
        if(c.indexOf(name) === 0) {
            var cookie = c.substring(name.length + 1);
            // Remove = from cookie
            cookie = cookie.replace("=", "");
            return cookie;
        }
    }
    return defaultValue;
}