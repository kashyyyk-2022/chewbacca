$(document).ready(function() {
    let routeID = getCookie("routeID");

    let url = "/route-api/content?id=" + routeID;

    $.get(url, function(data) {
        const waypoints = data.route; // 2D array of coordinates
        
        //Marks the first position (start) in the array with a blue marker.
        var startPos = L.marker(data.start).addTo(map); //NOT THE CURRENT POSITION, ONLY A MOCK POS.
        startPos._icon.classList.add("huechangeStart");
        //Marks the last position (goal) in the array with a red marker.
        var endPos = L.marker(data.end).addTo(map);
        endPos._icon.classList.add("huechangeGoal");

        for(let i = 0; i < waypoints.length - 1; i++) {
            L.polyline([
                waypoints[i],
                waypoints[i+1]
            ]).addTo(map);
        }

    }, "json");
});