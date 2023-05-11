package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.Directions;
import cz.cvut.fel.pjv.arimaa.model.PlayerColor;

public class Rabbit extends Figure {
    public Rabbit(PlayerColor figurePlayerColor, Board board, int row, int col) {
        super(figurePlayerColor, 0, board, row, col);
    }

    @Override
    public String toString() {
        return "Rabbit";
    }

    @Override
    public boolean move(Directions direction) {
        // check if the new position is within the bounds of the board
        if ((direction.equals(Directions.UP) && getRow() == 7) ||
                (direction.equals(Directions.DOWN) && getRow() == 0) ||
                (direction.equals(Directions.LEFT) && getCol() == 0) ||
                (direction.equals(Directions.RIGHT) && getCol() == 7)){
            return false;
        }
        // check if golden rabbit is trying to go up
        if (this.getFigureColor() == PlayerColor.GOLD && direction == Directions.DOWN){
            return false;
        }
        // check if silver rabbit is trying to go down
        if (this.getFigureColor() == PlayerColor.SILVER && direction == Directions.UP){
            return false;
        }
        // check if the new position is empty
        // move the figure to the new position
        switch (direction) {
            case UP -> {
                if (getBoard().getBoard()[getRow()+1][getCol()] == null) {
                    getBoard().getBoard()[getRow()+1][getCol()] = this;
                    getBoard().getBoard()[getRow()][getCol()] = null;
                    setRow(getRow() + 1);
                    return true;
                }
            }
            case DOWN -> {
                if (getBoard().getBoard()[getRow()-1][getCol()] == null) {
                    getBoard().getBoard()[getRow()-1][getCol()] = this;
                    getBoard().getBoard()[getRow()][getCol()] = null;
                    setRow(getRow() - 1);
                    return true;
                }
            }
            case RIGHT -> {
                if (getBoard().getBoard()[getRow()][getCol()+1] == null) {
                    getBoard().getBoard()[getRow()][getCol()+1] = this;
                    getBoard().getBoard()[getRow()][getCol()] = null;
                    setCol(getCol() + 1);
                    return true;
                }
            }
            case LEFT -> {
                if (getBoard().getBoard()[getRow()][getCol()-1] == null) {
                    getBoard().getBoard()[getRow()][getCol()-1] = this;
                    getBoard().getBoard()[getRow()][getCol()] = null;
                    setCol(getCol() - 1);
                    return true;
                }
            }
        }
        return false;
    }
}
