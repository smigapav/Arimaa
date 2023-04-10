package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Color;

public abstract class Figure {
    private int x;
    private int Y;
    private final Color figureColor;
    private final int strength;
    private boolean isFrozen;

    public Figure(int x, int y, Color figureColor, int strength) {
        this.x = x;
        this.Y = y;
        this.figureColor = figureColor;
        this.strength = strength;
        this.isFrozen = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return Y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        Y = y;
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

    public int[] getCoordinates() {
        return new int[]{getX(), getY()};
    }
}
