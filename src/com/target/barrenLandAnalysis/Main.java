package com.target.barrenLandAnalysis;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	// Test code
        BarrenLand bl = new BarrenLand(10,10);
        try {
            bl.readBarrenLandFromSTDIN();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bl.fillBarrenArea();
        bl.processFertileLand();
        System.out.println(bl.printOutput());

    }
}
