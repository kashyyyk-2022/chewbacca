// Correct code; TODO: insert a correct array including waypoints data.
const waypoints = [
    [57.68939495267853,11.974072522154591],
    [57.690375578417566,11.973305940628054],
    [57.69218190152542,11.973670721054079],
    [57.69351221524594,11.972994804382326],
    [57.69710593033657,11.970380880735673],
    [57.69657814788056,11.966474609001716],
    [57.695820881360234,11.966646313253772],
    [57.69370143693639,11.966772079467775],
    [57.69370963050795,11.968148725459137],
    [57.69329654529788,11.96849213396321],
    [57.69359822522775,11.969239711761476],
    [57.692608059481266,11.970380880735673],
    [57.69309000092662,11.971582810499989],
    [57.69178185923145,11.972913518453298],
];

//Marks the first position (start) in the array with a blue marker.
var startPos = L.marker(waypoints[0]).addTo(map); //NOT THE CURRENT POSITION, ONLY A MOCK POS.
startPos._icon.classList.add("huechangeStart");
//Marks the last position (goal) in the array with a red marker.
var endPos = L.marker(waypoints[waypoints.length-1]).addTo(map);
endPos._icon.classList.add("huechangeGoal");

for(let i = 0; i < waypoints.length; i++){
    L.polyline([
        waypoints[i],
        waypoints[i+1]
    ]).addTo(map);
}