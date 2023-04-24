package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.Color;
import cz.cvut.fel.pjv.arimaa.model.tiles.Tile;

public class Rabbit extends Figure {
    public Rabbit(Color figureColor, Board board, int row, int col) {
        super(figureColor, 1, board, row, col);
    }

    @Override
    public String toString() {
        return "Rabbit";
    }
}
