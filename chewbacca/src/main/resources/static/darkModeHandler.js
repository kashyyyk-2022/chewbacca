function toggleDarkMode() {
    let darkMode = getCookie('darkMode');
    if (darkMode == "true") {
        document.body.style.backgroundColor = "black";
        document.body.style.color="white"

        /*Script to toggle color of element to dark mode. Add name of class inside one of the selectors by
        typing ',.classname' after .card-body or .card-header */
        let cards = document.querySelectorAll(".card-body");
        for (let i = 0; i < cards.length; i++) {
            cards[i].style.backgroundColor = "black";
        }
        cards = document.querySelectorAll(".card-header");
        for (let i = 0; i < cards.length; i++) {
            cards[i].style.backgroundColor = "darkgray";
        }
    } else {
        document.body.style.backgroundColor = "white";
        document.body.style.color="black"

        /*Script to toggle color of element to light mode. Add name of class inside one of the selectors by
        typing ',.classname' after .card-body or .card-header */
        let cards = document.querySelectorAll(".card-body");
        for (let i = 0; i < cards.length; i++) {
            cards[i].style.backgroundColor = "white";
        }
        cards = document.querySelectorAll(".card-header");
        for (let i = 0; i < cards.length; i++) {
            cards[i].style.backgroundColor = "lightgray";
        }
    }
}