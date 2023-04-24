package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.Color;
import cz.cvut.fel.pjv.arimaa.model.tiles.Tile;

public class Horse extends Figure {
    public Horse(Color figureColor, Board board, int row, int col) {
        super(figureColor, 4, board, row, col);
    }

    @Override
    public String toString() {
        return "Horse";
    }
}
