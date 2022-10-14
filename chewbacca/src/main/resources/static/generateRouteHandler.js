

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
        let distance = 0;
        for (let i = 0; i < waypoints.length - 1; i++) {
            var line = L.polyline([
                waypoints[i],
                waypoints[i+1]
            ]).addTo(map);

            //calculates actual distance of the route
            distance += PointDistance(waypoints[i],waypoints[i+1]);

            //Checks if the current position is the end
            if (waypoints[i][0] == data.start[0] && waypoints[i][1] == data.start[1]) {
                hasPassedEnd = true;
            }

            // TODO: Delete this
            if (hasPassedEnd) {
                line.setStyle({color: 'red'});
            }
        }

        //Set a cookie with the actual length of the calculated
        setCookie("DistanceLeft",(Math.round(distance*100)/100).toString(),365);
        document.getElementById("timeInfoData").innerText = Math.round(((distance / 5) / 60)).toString() + "h " + Math.round(((distance / 5) * 60) % 60).toString() + "min";


        // Focus the map on the route
        map.fitBounds([
            data.start,
            data.end
        ]);


    }, "json");
});