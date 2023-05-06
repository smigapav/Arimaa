package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.PlayerColor;

public class Cat extends Figure {
    public Cat(PlayerColor figurePlayerColor, Board board, int row, int col) {
        super(figurePlayerColor, 1, board, row, col);
    }

    @Override
    public String toString() {
        return "Cat";
    }
}
