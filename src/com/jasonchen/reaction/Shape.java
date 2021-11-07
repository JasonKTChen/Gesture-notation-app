package com.jasonchen.reaction;

import java.io.*;
import java.util.ArrayList;

import com.jasonchen.graphicsLib.G;
import com.jasonchen.music.UC;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;

public class Shape implements Serializable {
    public String name;
    public Prototype.List prototypes = new Prototype.List();
    //------------------------database for shapes---------------------//
    public static HashMap<String, Shape> DB = loadShapeDB();
    public static Shape DOT = DB.get("DOT");
    public static Collection<Shape> SHAPES = DB.values();// collection for map values can loop
    public static String fileName = UC.pathToShapeDB;
    //---------------------------constructor------------------------------//
    public Shape(String name) {
        this.name = name;
    }

    public static HashMap<String,Shape> loadShapeDB(){
        HashMap<String,Shape> res = new HashMap<>();
        res.put("DOT",new Shape("DOT"));
        try{
            System.out.println("Attempting DB load...");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            res = (HashMap<String, Shape>) ois.readObject();
            System.out.println("Successful load...  Found- " + res.keySet());
            ois.close();
        }catch (Exception e){
            System.out.println("loadDB fail!");
            System.out.println(e);
        }
        return res;
    }
    public static void saveShapeDB(){
        try{
            System.out.println("attempting DB save...");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(DB);
            System.out.println("Successful save...  File name: " + fileName);
            oos.close();
        }catch (Exception e){
            System.out.println("saveDB fail!");
            System.out.println(e);
        }

    }
    public static Shape recognize(Ink ink){// can return null
        if(ink.vs.size.x < UC.dotsize && ink.vs.size.y < UC.dotsize){ return DOT; }
        Shape bestMatch = null; int bestSoFar = UC.noMatchDist;
        for(Shape s: SHAPES){
            int d = s.prototypes.bestDist(ink.norm);
            if(d < bestSoFar){ bestMatch = s; bestSoFar = d; }
        }
        return bestMatch;
    }
    //--------------------------Prototype-------------------------//
    public static class Prototype extends Ink.Norm implements Serializable{
        int nBlend = 1;
        public void blend(Ink.Norm norm) {
            blend(norm, nBlend++);
        }
        //--------------Prototype.List--------------------//
        public static class List extends ArrayList<Prototype> {
            public static Prototype bestMatch;// Set as a side affect
            private static int m = 10, w = 60;
            private static G.VS showBox = new G.VS(m, m, w, w);
            public void show(Graphics g) {
                g.setColor(Color.ORANGE);
                for(int i = 0; i < size(); i++) {
                    Prototype p = get(i);
                    int x = m + i * (m + w);
                    showBox.loc.set(x, m);
                    p.drawAt(g, showBox);
                    g.drawString("" + p.nBlend, x, 20);
                }
            }
            public int bestDist(Ink.Norm norm) {
                bestMatch = null;
                int bestSoFar = UC.noMatchDist;
                for(Prototype p: this) {
                    int d = p.dist(norm);
                    if(d < bestSoFar) {
                        bestMatch = p;
                        bestSoFar = d;
                    }
                }
                return bestSoFar;
            }
        }
    }
}
