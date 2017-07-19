package com.example.salfino.naviblind_110217;

/**
 * Created by Saviour on 23/06/2017.
 * Class providing straight line distance approximation between two points define by
 * latitude, longitude coordinates using the Haversine formula.
 * Haversine formula:	a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
 * c = 2 ⋅ atan2( √a, √(1−a) )
 * d = R ⋅ c
 * where	φ is latitude, λ is longitude and R is earth’s radius (mean radius = 6,378,137m).
 * angles are converted in radians using Math method toRadians
 */

public class Haversine {
    private static final double EARTH_RADIUS_KM = 6378.137; // Approximate radius of the Earth in KM

    public static double distance(double movingTargetLat, double movingTargetLong,
                                  double wayPointLat, double wayPointLong) {

        double differenceLat  = Math.toRadians((wayPointLat - movingTargetLat));
        double differenceLong = Math.toRadians((wayPointLong - movingTargetLong));

        movingTargetLat = Math.toRadians(movingTargetLat);
        wayPointLat   = Math.toRadians(wayPointLat);

        double a = hav(differenceLat) + Math.cos(movingTargetLat) * Math.cos(wayPointLat) * hav(differenceLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // Returns d as per above formula
    }

    public static double hav(double val) { //Haversine Function sin²(Δφ/2) or sin²(Δλ/2)
        return Math.pow(Math.sin(val / 2), 2);
    }
}
