package com.target.barrenLandAnalysis;

import static org.junit.Assert.*;

import org.junit.Test;

import java.security.InvalidParameterException;

public class BarrenLandTest {

    BarrenLand testLand;

    public String simulateProg(String input){

        testLand.readBarrenLandsInput(input);
        testLand.fillBarrenArea();
        testLand.processFertileLand();
        return testLand.printOutput();
    }

    @Test(expected = InvalidParameterException.class)
    public void testNegativeFarm(){
        testLand = new BarrenLand(-10, -10);
    }

    @Test(expected = InvalidParameterException.class)
    public void testZeroAreaFarm(){
        testLand = new BarrenLand(0,0);
    }

    @Test
    public void testNoBarrenLand() {
        testLand = new BarrenLand();
        String input = new String ("{\"\"}");
        String expectedOutput = new String("240000");
        assertEquals(expectedOutput, simulateProg(input));
    }

    @Test
    public void testFirstGivenTest() {
        testLand = new BarrenLand();
        String input = new String ("{\"0 292 399 307\"}");
        String expectedOutput = new String("116800 116800");
        assertEquals(expectedOutput, simulateProg(input));
    }

    @Test
    public void testSecondGivenTest() {
        testLand = new BarrenLand();
        String input = new String ("{\"48 192 351 207\", \"48 392 351 407\", \"120 52 135 547\", \"260 52 275 547\"}");
        String expectedOutput = new String("22816 192608");
        assertEquals(expectedOutput, simulateProg(input));
    }

    @Test
    public void testSmallArea() {
        testLand = new BarrenLand(10, 10);
        String input = new String ("{\"0 0 5 5\", \"5 5 9 9\"}");
        String expectedOutput = new String("20 20");
        assertEquals(expectedOutput, simulateProg(input));
    }

    @Test
    public void testAllBarren() {
        testLand = new BarrenLand(200, 200);
        String input = new String ("{\"0 0 199 199\"}");
        String expectedOutput = new String("0");
        assertEquals(expectedOutput, simulateProg(input));
    }

}
