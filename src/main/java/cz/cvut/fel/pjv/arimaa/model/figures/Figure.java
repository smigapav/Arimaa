package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.PlayerColor;
import cz.cvut.fel.pjv.arimaa.model.Directions;
import cz.cvut.fel.pjv.arimaa.model.tiles.Tile;

public abstract class Figure {
    private final PlayerColor figurePlayerColor;
    private final int strength;
    private boolean isFrozen;
    private final Board board;
    private int row;
    private int col;

    public Figure(PlayerColor figurePlayerColor, int strength, Board board, int row, int col) {
        this.figurePlayerColor = figurePlayerColor;
        this.strength = strength;
        this.isFrozen = false;
        this.board = board;
        this.row = row;
        this.col = col;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public PlayerColor getFigureColor() {
        return figurePlayerColor;
    }

    public int getColorForGUI() {
        if (figurePlayerColor == PlayerColor.GOLD){
            return 0;
        }
        else {
            return 1;
        }
    }

    public int getStrength() {
        return strength;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Board getBoard() {
        return board;
    }

    public boolean move(Directions direction){
        // check if the new position is within the bounds of the board
        if ((direction.equals(Directions.UP) && this.row == 7) ||
            (direction.equals(Directions.DOWN) && this.row == 0) ||
            (direction.equals(Directions.LEFT) && this.col == 0) ||
            (direction.equals(Directions.RIGHT) && this.col == 7)){
            return false;
        }
        // check if the new position is empty
        // move the figure to the new position
        switch (direction) {
            case UP -> {
                if (this.board.getBoard()[this.row+1][this.col] == null) {
                    board.getBoard()[this.row+1][this.col] = this;
                    board.getBoard()[this.row][this.col] = null;
                    this.row = this.row + 1;
                    return true;
                }
            }
            case DOWN -> {
                if (this.board.getBoard()[this.row-1][this.col] == null) {
                    board.getBoard()[this.row-1][this.col] = this;
                    board.getBoard()[this.row][this.col] = null;
                    this.row = this.row - 1;
                    return true;
                }
            }
            case RIGHT -> {
                if (this.board.getBoard()[this.row][this.col+1] == null) {
                    board.getBoard()[this.row][this.col+1] = this;
                    board.getBoard()[this.row][this.col] = null;
                    this.col = this.col + 1;
                    return true;
                }
            }
            case LEFT -> {
                if (this.board.getBoard()[this.row][this.col-1] == null) {
                    board.getBoard()[this.row][this.col-1] = this;
                    board.getBoard()[this.row][this.col] = null;
                    this.col = this.col - 1;
                    return true;
                }
            }
        }
        return false;
    }

    public void push(Tile tile){
        //TODO
    }

    public void pull(Tile tile){
        //TODO
    }
}
