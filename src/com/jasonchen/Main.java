package com.jasonchen;

import com.jasonchen.graphicsLib.*;
import com.jasonchen.sandbox.*;


public class Main {
    public static void main(String [] args) {
        System.out.println("Music Editor!");
//            Window.PANEL = new Paint();
//            Window.PANEL = new Squares();
//            Window.PANEL = new Game();
//            Window.PANEL = new PaintInk();
//            Window.PANEL = new ShapeTrainer();
            Window.PANEL = new ReactionTest();
            Window.launch();

    }
}
