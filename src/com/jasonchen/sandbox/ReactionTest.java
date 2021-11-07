package com.jasonchen.sandbox;

import com.jasonchen.graphicsLib.G;
import com.jasonchen.graphicsLib.Window;
import com.jasonchen.music.I;
import com.jasonchen.music.UC;
import com.jasonchen.reaction.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ReactionTest extends Window {
    static {
        new Layer("Back");
        new Layer("Fore");
    }
    public ReactionTest() {
        super("ReactionTest", UC.windowWidth, UC.windowHeight);
        Reaction.initialReaction.addReaction(new Reaction("SW-SW") {
            public int bid(Gesture g) { return 0;}
            public void act(Gesture g) { new Box(g.vs);}
        });
    }
    public void paintComponent(Graphics g){
        G.fillBackground(g);
        g.setColor(Color.blue);
        Ink.BUFFER.show(g);
        Layer.ALL.show(g);
    }
    public void MousePressed(MouseEvent me){ Gesture.AREA.dn(me.getX(), me.getY()); repaint();}
    public void MouseDragged(MouseEvent me){ Gesture.AREA.drag(me.getX(), me.getY()); repaint();}
    public void MouseReleased(MouseEvent me){ Gesture.AREA.up(me.getX(), me.getY()); repaint();}
    public static class Box extends Mass {
        public G.VS vs;
        public Color c = G.rndColor();
        public Box(G.VS vs){
            super("Back");
            this.vs = vs;
            addReaction(new Reaction("S-S") {
                public int bid(Gesture gest) {
                    int x = gest.vs.xH(), y = gest.vs.yL();
                    if (!Box.this.vs.hit(x,y)){ return UC.noBid; }
                    return Math.abs(x - Box.this.vs.xH());
                }
                public void act(Gesture gest) { Box.this.delete(); }
            });
        }

        public void show(Graphics g){ vs.fill(g,c);}

    }
}
