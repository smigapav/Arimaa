package cz.cvut.fel.pjv.arimaa.model.figures;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.Coords;
import cz.cvut.fel.pjv.arimaa.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
        if (board.isLoggingOn()) {
            board.getLogger().log(Level.INFO, "Figure created: " + this.toString());
        }
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

    // returns all tiles adjacent to this one
    public List<Figure> getAdjacentTiles() {
        List<Figure> out = new ArrayList<>();
        if (this.board.isLoggingOn()) {
            this.board.getLogger().log(Level.FINE, "Getting adjacent tiles for " + this.toString());
        }
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Figure tile = this.board.getBoard()[i][j];
                if ((i + 1 == this.row || i - 1 == this.row) && j == this.col) {
                    out.add(tile);
                } else if ((j + 1 == this.col || j - 1 == this.col) && i == this.row) {
                    out.add(tile);
                }
            }
        }
        return out;
    }

    // returns all figures on adjacent tiles from this one
    public List<Figure> getAdjacentFigures() {
        List<Figure> out = new ArrayList<>();
        if (this.board.isLoggingOn()) {
            this.board.getLogger().log(Level.FINE, "Getting adjacent figures for " + this.toString());
        }
        for (Figure figure : getAdjacentTiles()) {
            if (figure != null){
                if (this.board.isLoggingOn()) {
                    this.board.getLogger().log(Level.FINER, "Checking figure at " + figure.getRow() + " " + figure.getCol());
                }
                out.add(figure);
            }
        }
        return out;
    }

    // Checks if the figure on input is the same color as this one
    public boolean isFigureSameColor(Figure figure) {
        if (this.board.isLoggingOn()) {
            this.board.getLogger().log(Level.FINE, "Checking if " + this.toString() + " at " + this.getRow() + " " + this.getCol() + " is the same color as " + figure.toString() + " at " + figure.getRow() + " " + figure.getCol());
        }
        if (this.figurePlayerColor == figure.getFigureColor()){
            return true;
        } else {
            return false;
        }
    }

    // Returns only adjacent figures that aren't the same color as this one
    public List<Figure> getAdjacentEnemyFigures() {
        List<Figure> out = new ArrayList<>();
        if (this.board.isLoggingOn()) {
            this.board.getLogger().log(Level.FINE, "Getting adjacent enemy figures for " + this.toString());
        }
        for (Figure figure : getAdjacentFigures()) {
            if (!this.isFigureSameColor(figure)){
                out.add(figure);
                if (this.board.isLoggingOn()) {
                    this.board.getLogger().log(Level.FINER, "Adding figure at " + figure.getRow() + " " + figure.getCol());
                }
            }
        }
        return out;
    }

    // Returns only adjacent figures that are the same color as this one
    public List<Figure> getAdjacentFriendlyFigures() {
        List<Figure> out = new ArrayList<>();
        if (this.board.isLoggingOn()) {
            this.board.getLogger().log(Level.FINE, "Getting adjacent friendly figures for " + this.toString());
        }
        for (Figure figure : getAdjacentFigures()) {
            if (this.isFigureSameColor(figure)){
                out.add(figure);
                if (this.board.isLoggingOn()) {
                    this.board.getLogger().log(Level.FINER, "Adding figure at " + figure.getRow() + " " + figure.getCol());
                }
            }
        }
        return out;
    }

    // After each move, the pull pool is updated
    public void alterPullPool(){
        List<Figure> out = new ArrayList<>();
        List<Figure> adjacentEnemyFigures = getAdjacentEnemyFigures();
        if (this.board.isLoggingOn()) {
            this.board.getLogger().log(Level.FINE, "Altering pull pool");
        }
        for (Figure figure : adjacentEnemyFigures) {
            if (this.isStronger(figure)){
                out.add(figure);
                if (this.board.isLoggingOn()) {
                    this.board.getLogger().log(Level.FINER, "Adding figure at " + figure.getRow() + " " + figure.getCol() + " to pull pool");
                }
            }
        }
        if (out.size() > 0){
            board.setPullPosition(new Coords(this.getRow(), this.getCol()));
            if (this.board.isLoggingOn()) {
                this.board.getLogger().log(Level.FINER, "Setting coords for pull pool to " + this.getRow() + " " + this.getCol());
            }
        }
        board.setCanBePulled(out);
    }

    public boolean canBePushed(){
        List<Figure> adjacentEnemyFigures = getAdjacentEnemyFigures();
        if (this.board.isLoggingOn()) {
            this.board.getLogger().log(Level.FINE, "Checking if " + this.toString() + " at " + this.getRow() + " " + this.getCol() + " can be pushed");
        }
        for (Figure figure : adjacentEnemyFigures) {
            if (!figure.getIsFrozen() && figure.isStronger(this)){
                return true;
            }
        }
        return false;
    }

    // Checks if this figure is stronger than the input one
    public boolean isStronger(Figure figure){
        return this.strength > figure.strength;
    }


    // Checks if this figure is frozen
    public void checkIfFrozen(){
        List<Figure> adjacentEnemyFigures = getAdjacentEnemyFigures();
        if (this.board.isLoggingOn()) {
            this.board.getLogger().log(Level.FINE, "Checking if " + this.toString() + " at " + this.getRow() + " " + this.getCol() + " is frozen");
        }
        this.setFrozen(false);
        for (Figure figure : adjacentEnemyFigures) {
            if (figure.isStronger(this)){
                this.setFrozen(true);
                if (this.board.isLoggingOn()) {
                    this.board.getLogger().log(Level.FINER, "Setting " + this.toString() + " at " + this.getRow() + " " + this.getCol() + " to frozen");
                }
            }
        }
        if (this.getAdjacentFriendlyFigures().size() > 0){
            this.setFrozen(false);
            if (this.board.isLoggingOn()) {
                this.board.getLogger().log(Level.FINER, "Setting " + this.toString() + " at " + this.getRow() + " " + this.getCol() + " to not frozen");
            }
        }
    }


    public boolean move(int row, int col){
        if (this.board.isLoggingOn()) {
            this.board.getLogger().log(Level.FINE, "Moving " + this.toString() + " at " + this.getRow() + " " + this.getCol() + " to " + row + " " + col);
        }
        // check if player has any moves left
        if (this.board.getCurrentPlayer().getMovesLeft() == 0){
            return false;
        }
        // check if the figure is frozen
        if (this.isFrozen){
            return false;
        }
        // check if the new position is within the bounds of the board
        if (row < 0 || row > 7 || col < 0 || col > 7){
            return false;
        }
        // check if the new position is empty
        // move the figure to the new position
        if (this.board.getBoard()[row][col] == null){
            this.alterPullPool();
            this.board.getBoard()[row][col] = this;
            this.board.getBoard()[this.row][this.col] = null;
            this.row = row;
            this.col = col;
            board.checkTraps();
            this.board.checkIfFrozenForAllTiles();
            this.board.getCurrentPlayer().decreaseMovesLeft();
            return true;
        }
        return false;
    }

    public boolean forceMove(int row, int col){
        if (this.board.isLoggingOn()) {
            this.board.getLogger().log(Level.FINE, "Force moving " + this.toString() + " at " + this.getRow() + " " + this.getCol() + " to " + row + " " + col);
        }
        // check if player has any moves left
        if (this.board.getCurrentPlayer().getMovesLeft() == 0){
            return false;
        }
        // check if the new position is within the bounds of the board
        if (row < 0 || row > 7 || col < 0 || col > 7){
            return false;
        }
        // check if the new position is empty
        // move the figure to the new position
        if (this.board.getBoard()[row][col] == null){
            this.getBoard().setCanBePulled(new ArrayList<>());
            this.board.setPullPosition(new Coords(this.row, this.col));
            this.board.getBoard()[row][col] = this;
            this.board.getBoard()[this.row][this.col] = null;
            this.row = row;
            this.col = col;
            board.checkTraps();
            this.board.checkIfFrozenForAllTiles();
            this.board.getCurrentPlayer().decreaseMovesLeft();
            return true;
        }
        return false;
    }
}
