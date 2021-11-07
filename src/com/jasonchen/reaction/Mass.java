package com.jasonchen.reaction;

import com.jasonchen.music.I;

import java.awt.*;

public abstract class Mass extends Reaction.List implements I.Show{
    public Layer layer;
    public Mass(String layerName){
        this.layer = Layer.byName.get(layerName);
        if (layer != null){
            layer.add(this);
        }else{
            System.out.println("Bad layer name " + layerName);
        }
    }
    public void delete() { clearAll(); layer.remove(this); }// delete the mass from two places
    public void show(Graphics g){}
    //this will be explained later
    public boolean equals(Object o){ return this == o; }
}
