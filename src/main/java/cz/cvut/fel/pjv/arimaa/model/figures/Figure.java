package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.PlayerColor;
import cz.cvut.fel.pjv.arimaa.model.Directions;
import cz.cvut.fel.pjv.arimaa.model.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

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

    public List<Figure> getAdjacentFigures() {
        List<Figure> out = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Figure tile = this.board.getBoard()[i][j];
                if ((i + 1 == this.row || i - 1 == this.row) && j == this.col && tile != null) {
                    out.add(tile);
                } else if ((j + 1 == this.col || j - 1 == this.col) && i == this.row && tile != null) {
                    out.add(tile);
                }
            }
        }
        return out;
    }

    public boolean isFigureSameColor(Figure figure) {
        if (this.figurePlayerColor == figure.getFigureColor()){
            return true;
        } else {
            return false;
        }
    }

    public List<Figure> getAdjacentEnemyFigures() {
        List<Figure> out = new ArrayList<>();
        for (Figure figure : getAdjacentFigures()) {
            if (!this.isFigureSameColor(figure)){
                out.add(figure);
            }
        }
        return out;
    }

    public List<Figure> getAdjacentFriendlyFigures() {
        List<Figure> out = new ArrayList<>();
        for (Figure figure : getAdjacentFigures()) {
            if (this.isFigureSameColor(figure)){
                out.add(figure);
            }
        }
        return out;
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
                    board.checkTraps();
                    return true;
                }
            }
            case DOWN -> {
                if (this.board.getBoard()[this.row-1][this.col] == null) {
                    board.getBoard()[this.row-1][this.col] = this;
                    board.getBoard()[this.row][this.col] = null;
                    this.row = this.row - 1;
                    board.checkTraps();
                    return true;
                }
            }
            case RIGHT -> {
                if (this.board.getBoard()[this.row][this.col+1] == null) {
                    board.getBoard()[this.row][this.col+1] = this;
                    board.getBoard()[this.row][this.col] = null;
                    this.col = this.col + 1;
                    board.checkTraps();
                    return true;
                }
            }
            case LEFT -> {
                if (this.board.getBoard()[this.row][this.col-1] == null) {
                    board.getBoard()[this.row][this.col-1] = this;
                    board.getBoard()[this.row][this.col] = null;
                    this.col = this.col - 1;
                    board.checkTraps();
                    return true;
                }
            }
        }
        board.checkTraps();
        return false;
    }

    public void push(Tile tile){
        //TODO
    }

    public void pull(Tile tile){
        //TODO
    }
}
