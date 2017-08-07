package com.example.salfino.naviblind_alpha;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Saviour on 25/07/2017.
 */
@RunWith(Parameterized.class)
public class HaversineTest {

    private double ALat;
    private double ALong;
    private double BLat;
    private double BLong;
    private double expected;
    Haversine myHaversine;

    public HaversineTest(double ALat, double ALong, double BLat, double BLong, double expected) {
        this.ALat = ALat;
        this.ALong = ALong;
        this.BLat = BLat;
        this.BLong = BLong;
        this.expected = expected;
    }

    @Before
    public void setup(){
        myHaversine = new Haversine();

    }

    @Parameterized.Parameters
    public static Collection<Object[]> testConditions(){
        return Arrays.asList(new Object[][]{
                {51.52231720,-0.13089649,51.52210275,-0.13065711,29.00000000},
                {51.52222335,-0.13075031,51.52228758,-0.13059912,12.60000000},
                {51.52233179,-0.13061568,51.52222145,-0.13049584,14.80000000}
        });

    }
    @Test
    public void distanceCalculation() throws Exception {
        double myDistance = 1000*(myHaversine.distance(ALat,ALong,BLat,BLong));
        assertEquals(myDistance,expected,0.09);

    }

}