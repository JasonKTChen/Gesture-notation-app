package com.jasonchen.reaction;

import com.jasonchen.graphicsLib.G;
import com.jasonchen.music.I;

import java.util.ArrayList;

public class Gesture {
    public Shape shape;
    public G.VS vs;
    public static List UNDO = new List();

    public static I.Area AREA = new I.Area() {
        public void dn(int x, int y) { Ink.BUFFER.dn(x,y); }
        public void drag(int x, int y) { Ink.BUFFER.drag(x,y); }
        public void up(int x, int y) {
            Ink.BUFFER.add(x,y);
            Ink ink = new Ink();
            Gesture gest = Gesture.getNew(ink); // can fail if not recognized
            Ink.BUFFER.clear();
            if (gest != null){
                if (gest.shape.name.equals("N-N")) {
                    undo();
                }else {
                    gest.doGesture();
                }
            }
        }
        public boolean hit(int x, int y) { return true; }
    };

    private Gesture(Shape shape,G.VS vs){ this.shape = shape; this.vs = vs; }// singleton

    public void redoGesture(){ // call from the UNDO list
        Reaction r = Reaction.best(this);
        if (r != null){r.act(this);}
    }
    public void doGesture(){
        Reaction r = Reaction.best(this);
        if (r != null){ UNDO.add(this); r.act(this);}
    }
    public static void undo(){
        if (UNDO.size() > 0){
            UNDO.remove(UNDO.size() - 1); // remove the last element
            Layer.nukeAll(); // eliminate all the mass
            Reaction.nuke(); // clears byShape reload initial reaction
            UNDO.redo();
        }
    }
    public static Gesture getNew(Ink ink){ // factory method can return null
        Shape s = Shape.recognize(ink);
        return (s == null) ? null : new Gesture(s, ink.vs);
    }
    //--------------------List------------------------
    public static class List extends ArrayList<Gesture>{
        public void redo(){ for(Gesture g: this){ g.redoGesture();}}
    }
}
