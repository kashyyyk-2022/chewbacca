function toggleDarkMode() {

    let darkMode = getCookie('darkMode');
    if (darkMode == "true") {
        document.body.style.backgroundColor = "black";
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

        document.body.style.backgroundRepeat = "no-repeat";
        document.body.style.backgroundSize = "cover";
        //document.body.style.color="black";

        var icon = document.getElementById("icon-display")
        if (icon != null) {
            icon.style.backgroundColor = "#202F55";
        }
    } else {

        document.body.style.backgroundRepeat = "no-repeat";
        document.body.style.backgroundSize = "cover";
        document.body.style.color="black";

        var icon = document.getElementById("icon-display")
        if (icon != null) {
            icon.style.backgroundColor = "#B7CBFA";
        }

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