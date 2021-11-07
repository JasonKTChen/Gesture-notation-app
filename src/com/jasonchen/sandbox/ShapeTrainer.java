package com.jasonchen.sandbox;

import com.jasonchen.graphicsLib.G;
import com.jasonchen.graphicsLib.Window;
import com.jasonchen.music.UC;
import com.jasonchen.reaction.Ink;
import com.jasonchen.reaction.Shape;

import java.awt.*;
import java.awt.event.*;

public class ShapeTrainer extends Window{
    public static String UNKNOWN = "This name is currently unknown. ";
    public static String ILLEGAL = "This name is not a legal shape name. ";
    public static String KNOWN = "This name is a known shape. ";
    public static String currentName = "";
    public static String currentState = ILLEGAL;
    public static Shape.Prototype.List pList = null;

    public ShapeTrainer(){
        super("Shape Trainer ", UC.windowWidth, UC.windowHeight);
    } 
    public void setState() {
        currentState = (currentName.equals("") || currentName.equals("DOT")) ? ILLEGAL : UNKNOWN;
        if(currentState == UNKNOWN){
            if(Shape.DB.containsKey(currentName)){
                currentState = KNOWN;
                pList = Shape.DB.get(currentName).prototypes;
            }else{
                pList = null;
            }
        }
    }  
    public void paintComponent(Graphics g) {
        G.fillBackground(g);
        g.setColor(Color.BLACK);
        g.drawString(currentName, 600, 30);
        g.drawString(currentState, 700, 30);
        g.setColor(Color.red);
        Ink.BUFFER.show(g);
        if(pList != null){ pList.show(g); }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        System.out.println("typed " + c);
        currentName = (c == ' ' || c == 0x0D || c == 0x0A) ? "" : currentName + c;
        if(c == 0x0D || c == 0x0A){ Shape.saveShapeDB(); }// save to database
        setState();
        repaint();
    }
    public void mousePressed(MouseEvent me) {
        Ink.BUFFER.dn(me.getX(), me.getY());
        repaint();
    }
    public void mouseDragged(MouseEvent me) {
        Ink.BUFFER.drag(me.getX(), me.getY());
        repaint();
    }
    public void mouseReleased(MouseEvent me) {
        if(currentState != ILLEGAL){
            Ink ink = new Ink();
            Shape.Prototype proto;
    //        inkList.add(ink);
            if(pList == null){
                Shape s = new Shape(currentName);
                Shape.DB.put(currentName,s);
                pList = s.prototypes;
            }
            if(pList.bestDist(ink.norm) < UC.noMatchDist) {
                proto = Shape.Prototype.List.bestMatch;
                proto.blend(ink.norm);

            } else {
                proto = new Shape.Prototype();
                pList.add(proto);
            }
//            ink.BUFFER.clear();
//            setState();
            repaint();
        }
    }
}
