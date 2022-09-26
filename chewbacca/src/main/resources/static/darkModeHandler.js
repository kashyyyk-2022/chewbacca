function toggleDarkMode() {
    let darkMode = getCookie('darkMode');
    if (darkMode == "true") {
        document.body.style.backgroundColor = "black";
        document.body.style.color="white"
    } else {
        document.body.style.backgroundColor = "white";
        document.body.style.color="black"
    }
}