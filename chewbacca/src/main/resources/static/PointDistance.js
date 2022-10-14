function PointDistance(point1,point2){

    let R_EARTH = 6371.0;
    let lat1 = point1[0];
    let lon1 = point1[1];
    let lat2 = point2[0];
    let lon2 = point2[1];

    let dLat = (lat2-lat1)*(Math.PI/180);
    let dLon = (lon2-lon1)*(Math.PI/180);

    let a = Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(lat1*(Math.PI/180)) * Math.cos(lat2*(Math.PI/180)) *
            Math.sin(dLon/2) * Math.sin(dLon/2);

    let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

    return R_EARTH * c;
}
