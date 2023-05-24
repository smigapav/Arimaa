package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.Directions;
import cz.cvut.fel.pjv.arimaa.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Rabbit extends Figure {
    public Rabbit(PlayerColor figurePlayerColor, Board board, int row, int col) {
        super(figurePlayerColor, 0, board, row, col);
    }

    @Override
    public String toString() {
        return "Rabbit";
    }


    @Override
    public boolean move(int row, int col) {
        if (this.getBoard().isLoggingOn()){
            this.getBoard().getLogger().log(Level.FINE, "Moving Rabbit at " + this.getRow() + " " + this.getCol() + " to " + row + " " + col);
        }
        // check if player has any moves left
        if (this.getBoard().getCurrentPlayer().getMovesLeft() == 0){
            return false;
        }
        // check if the figure is frozen
        if (this.getIsFrozen()){
            return false;
        }
        // check if the new position is within the bounds of the board
        if (row < 0 || row > 7 || col < 0 || col > 7){
            return false;
        }
        // check if golden rabbit is trying to go up
        if (this.getFigureColor() == PlayerColor.GOLD && row < this.getRow()){
            return false;
        }
        // check if silver rabbit is trying to go down
        if (this.getFigureColor() == PlayerColor.SILVER && row > this.getRow()){
            return false;
        }
        // check if the new position is empty
        // move the figure to the new position
        if (getBoard().getBoard()[row][col] == null){
            this.alterPullPool();
            this.addMoveToHistory(row, col);
            this.getBoard().getBoard()[row][col] = this;
            this.getBoard().getBoard()[this.getRow()][this.getCol()] = null;
            this.setRow(row);
            this.setCol(col);
            this.getBoard().checkTraps();
            this.getBoard().checkIfFrozenForAllTiles();
            this.getBoard().getCurrentPlayer().decreaseMovesLeft();
            return true;
        }
        return false;
    }
}
