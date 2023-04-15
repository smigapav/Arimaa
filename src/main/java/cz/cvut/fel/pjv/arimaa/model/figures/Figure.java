package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Color;
import cz.cvut.fel.pjv.arimaa.model.tiles.Tile;

public abstract class Figure {
    private final Color figureColor;
    private final int strength;
    private boolean isFrozen;

    public Figure(Color figureColor, int strength) {
        this.figureColor = figureColor;
        this.strength = strength;
        this.isFrozen = false;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public Color getFigureColor() {
        return figureColor;
    }

    public int getStrength() {
        return strength;
    }

    public void move(Tile tile){
        //TODO
    }

    public void push(Tile tile){
        //TODO
    }

    public void pull(Tile tile){
        //TODO
    }
}
