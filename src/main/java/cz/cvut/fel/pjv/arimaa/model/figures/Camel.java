package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.PlayerColor;

public class Camel extends Figure {
    public Camel(PlayerColor figurePlayerColor, Board board, int row, int col) {
        super(figurePlayerColor, 4, board, row, col);
    }

    @Override
    public String toString() {
        return "Camel";
    }
}
