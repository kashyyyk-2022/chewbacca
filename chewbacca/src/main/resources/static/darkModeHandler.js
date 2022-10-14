function toggleDarkMode() {

    let darkMode = getCookie('darkMode');
    if (darkMode == "true") {
        document.body.style.backgroundImage = "url('https://upload.wikimedia.org/wikipedia/commons/thumb/4/49/A_black_image.jpg/640px-A_black_image.jpg')";
        document.body.style.color="white"

        /*Script to toggle color of element to dark mode. Add name of class inside one of the selectors by
        typing ',.classname' after .card-body or .card-header */
        let cards = document.querySelectorAll(".card-body,.card,.modal-body");
        for (let i = 0; i < cards.length; i++) {
            cards[i].style.backgroundColor = "black";
        }
        cards = document.querySelectorAll(".card-header");
        for (let i = 0; i < cards.length; i++) {
            cards[i].style.backgroundColor = "darkgray";
        }
    } else {
        document.body.style.backgroundImage = "url('https://cdn.wallpapersafari.com/91/60/M9k5Ka.png')";
        document.body.style.backgroundRepeat = "no-repeat";
        document.body.style.backgroundSize = "cover";
        document.body.style.color="black"

        /*Script to toggle color of element to light mode. Add name of class inside one of the selectors by
        typing ',.classname' after .card-body or .card-header */
        let cards = document.querySelectorAll(".card-body,.card,.modal-body");
        for (let i = 0; i < cards.length; i++) {
            cards[i].style.backgroundColor = "white";
        }
        cards = document.querySelectorAll(".card-header");
        for (let i = 0; i < cards.length; i++) {
            cards[i].style.backgroundColor = "lightgray";
        }
    }
}