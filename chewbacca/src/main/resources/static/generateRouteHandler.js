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

        var hasPassedEnd = false;
        for (let i = 0; i < waypoints.length - 1; i++) {
            var line = L.polyline([
                waypoints[i],
                waypoints[i+1]
            ]).addTo(map);

            //Checks if the current position is the end
            if (waypoints[i][0] == data.start[0] && waypoints[i][1] == data.start[1]) {
                hasPassedEnd = true;
            }

            // TODO: Delete this
            if (hasPassedEnd) {
                line.setStyle({color: 'red'});
            }
        }

        const labels = data.labels;

        // TODO: Delete this
        for (let i = 0; i < labels.length; i++) {
            //L.marker(labels[i].position).addTo(map).bindPopup(labels[i].text);
        }

    }, "json");
});