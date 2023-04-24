package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.Color;
import cz.cvut.fel.pjv.arimaa.model.tiles.Tile;

public class Camel extends Figure {
    public Camel(Color figureColor, Board board, int row, int col) {
        super(figureColor, 5, board, row, col);
    }

    @Override
    public String toString() {
        return "Camel";
    }
}
