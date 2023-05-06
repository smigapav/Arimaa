package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.PlayerColor;

public class Rabbit extends Figure {
    public Rabbit(PlayerColor figurePlayerColor, Board board, int row, int col) {
        super(figurePlayerColor, 0, board, row, col);
    }

    @Override
    public String toString() {
        return "Rabbit";
    }
}
