package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.PlayerColor;

public class Dog extends Figure {
    public Dog(PlayerColor figurePlayerColor, Board board, int row, int col) {
        super(figurePlayerColor, 2, board, row, col);
    }

    @Override
    public String toString() {
        return "Dog";
    }
}
