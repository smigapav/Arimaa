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

    public boolean getIsFrozen() {
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

    // returns all figures on adjacent tiles from this one
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

    // Checks if the figure on input is the same color as this one
    public boolean isFigureSameColor(Figure figure) {
        if (this.figurePlayerColor == figure.getFigureColor()){
            return true;
        } else {
            return false;
        }
    }

    // Returns only adjacent figures that aren't the same color as this one
    public List<Figure> getAdjacentEnemyFigures() {
        List<Figure> out = new ArrayList<>();
        for (Figure figure : getAdjacentFigures()) {
            if (!this.isFigureSameColor(figure)){
                out.add(figure);
            }
        }
        return out;
    }

    // Returns only adjacent figures that are the same color as this one
    public List<Figure> getAdjacentFriendlyFigures() {
        List<Figure> out = new ArrayList<>();
        for (Figure figure : getAdjacentFigures()) {
            if (this.isFigureSameColor(figure)){
                out.add(figure);
            }
        }
        return out;
    }

    // After each move, the pull pool is updated
    public void alterPullPool(){
        List<Figure> out = new ArrayList<>();
        List<Figure> adjacentEnemyFigures = getAdjacentEnemyFigures();
        for (Figure figure : adjacentEnemyFigures) {
            if (figure.isStronger(this)){
                out.add(figure);
            }
        }
        board.setCanBePulled(out);
    }

    // Checks if this figure is stronger than the input one
    public boolean isStronger(Figure figure){
        return this.strength > figure.strength;
    }

    // Updates isFrozen for all figures on the board
    public void checkIfFrozenForAllTiles(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Figure tile = this.board.getBoard()[i][j];
                if (tile != null){
                    tile.checkIfFrozen();
                }
            }
        }
    }

    // Checks if this figure is frozen
    public void checkIfFrozen(){
        List<Figure> adjacentEnemyFigures = getAdjacentEnemyFigures();
        if (adjacentEnemyFigures.size() > 0){
            for (Figure figure : adjacentEnemyFigures) {
                if (figure.isStronger(this)){
                    this.setFrozen(true);
                    return;
                }
            }
        }
        this.setFrozen(false);
    }

    public boolean move(Directions direction){
        boolean out = false;
        // check if the figure is frozen
        if (this.isFrozen){
            return false;
        }
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
                    this.alterPullPool();
                    board.getBoard()[this.row+1][this.col] = this;
                    board.getBoard()[this.row][this.col] = null;
                    this.row = this.row + 1;
                    out = true;
                }
            }
            case DOWN -> {
                if (this.board.getBoard()[this.row-1][this.col] == null) {
                    this.alterPullPool();
                    board.getBoard()[this.row-1][this.col] = this;
                    board.getBoard()[this.row][this.col] = null;
                    this.row = this.row - 1;
                    out = true;
                }
            }
            case RIGHT -> {
                if (this.board.getBoard()[this.row][this.col+1] == null) {
                    this.alterPullPool();
                    board.getBoard()[this.row][this.col+1] = this;
                    board.getBoard()[this.row][this.col] = null;
                    this.col = this.col + 1;
                    out = true;
                }
            }
            case LEFT -> {
                if (this.board.getBoard()[this.row][this.col-1] == null) {
                    this.alterPullPool();
                    board.getBoard()[this.row][this.col-1] = this;
                    board.getBoard()[this.row][this.col] = null;
                    this.col = this.col - 1;
                    out = true;
                }
            }
        }
        board.checkTraps();
        this.checkIfFrozenForAllTiles();
        return out;
    }
}
