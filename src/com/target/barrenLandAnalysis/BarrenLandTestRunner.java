package com.target.barrenLandAnalysis;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class BarrenLandTestRunner {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(BarrenLandTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        if (result.wasSuccessful()) {
            System.out.println("All tests passed!");
        } else {
            System.out.println("Tests have failed!");
        }
    }
}
