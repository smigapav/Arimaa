package cz.cvut.fel.pjv.arimaa.model.tiles;

import cz.cvut.fel.pjv.arimaa.model.figures.Figure;

public abstract class Tile {
    private final int x;
    private final int y;
    private Figure figure;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.figure = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }
}
