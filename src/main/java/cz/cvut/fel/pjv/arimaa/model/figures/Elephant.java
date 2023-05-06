package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.PlayerColor;

public class Elephant extends Figure {
    public Elephant(PlayerColor figurePlayerColor, Board board, int row, int col) {
        super(figurePlayerColor, 5, board, row, col);
    }

    @Override
    public String toString() {
        return "Elephant";
    }
}
