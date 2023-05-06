package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.PlayerColor;

public class Horse extends Figure {
    public Horse(PlayerColor figurePlayerColor, Board board, int row, int col) {
        super(figurePlayerColor, 3, board, row, col);
    }

    @Override
    public String toString() {
        return "Horse";
    }
}
