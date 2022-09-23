function setCookie(name, value, days) {
    let d = new Date();
    d.setTime(d.getTime() + (days*24*60*60*1000));
    document.cookie = name + "=" + value + ";expires:" + d.toUTCString();
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
            return c.substring(name.length + 1);
        }
    }
    return defaultValue;
}