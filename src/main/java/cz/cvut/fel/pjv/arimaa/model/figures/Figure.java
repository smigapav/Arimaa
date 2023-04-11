package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Color;
import cz.cvut.fel.pjv.arimaa.model.tiles.Tile;

public abstract class Figure {
    private Tile tile;
    private final Color figureColor;
    private final int strength;
    private boolean isFrozen;

    public Figure(Tile tile, Color figureColor, int strength) {
        this.tile = tile;
        tile.setFigure(this);
        this.figureColor = figureColor;
        this.strength = strength;
        this.isFrozen = false;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
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
}
