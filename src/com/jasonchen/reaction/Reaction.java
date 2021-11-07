package com.jasonchen.reaction;

import com.jasonchen.music.I;
import com.jasonchen.music.UC;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Reaction implements I.React{
    public Shape shape;
    public static Map byShape = new Map();// list of reaction by the shape
    public static List initialReaction = new List();
    public Reaction(String shapeName){
        shape = Shape.DB.get(shapeName);
        if (shape == null){
            System.out.println("Error-shape DB doesn't have " + shapeName);
        }
    }
    public void enable(){ byShape.getList(shape).safeAdd(this); }
    public void disable(){ byShape.getList(shape).remove(this); }
    public static Reaction best(Gesture gest){ return byShape.getList(gest.shape).loBid(gest); }// can fail
    public static void nuke(){ // this is use for reset undo
        byShape = new Map();
        initialReaction.enable();
    }
    //---------------------List----------------------//
    public static class List extends ArrayList<Reaction>{
        public void safeAdd(Reaction r){ if (!contains(r)){ add(r); }}
        public void addReaction(Reaction r){ add(r); r.enable(); }
        public void removeReaction(Reaction r) { remove(r); r.disable(); }
        public void clearAll(){ for(Reaction r: this){ r.disable(); } this.clear();}
        public Reaction loBid(Gesture g){// can return null
            Reaction res = null; int bestSoFar = UC.noBid;
            for(Reaction r: this){
                int b = r.bid(g);
                if (b < bestSoFar){ bestSoFar = b; res = r; }
            }
            return res;
        }
        public void enable(){ for(Reaction r: this ){r.enable();}}
    }
    //---------------------Map-----------------------//
    public static class Map extends HashMap<Shape,List>{// like a marketplace
        public List getList(Shape s){// always succeeds
            List res = get(s);
            if (res == null) { res = new List(); put(s,res); }
            return res;
        }
    }
}
