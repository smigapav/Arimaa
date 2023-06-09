package cz.cvut.fel.pjv.arimaa.model;

import cz.cvut.fel.pjv.arimaa.model.figures.Figure;

import java.util.ArrayList;
import java.util.logging.Level;

public class Player {
    private final PlayerColor playerColor;
    private int time;
    private int movesLeft;
    private final Board board;
    private int elephants;
    private int camels;
    private int horses;
    private int dogs;
    private int cats;
    private int rabbits;

    public Player(PlayerColor playerColor, Board board) {
        this.playerColor = playerColor;
        this.time = 0;
        this.movesLeft = 4;
        this.board = board;
        this.elephants = 0;
        this.camels = 0;
        this.horses = 0;
        this.dogs = 0;
        this.cats = 0;
        this.rabbits = 0;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void decreaseMovesLeft(){
        this.movesLeft--;
    }

    public void resetMovesLeft() {
        this.movesLeft = 4;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public void setMovesLeft(int movesLeft) {
        this.movesLeft = movesLeft;
    }

    /**
     * Get all available friendly pieces that are not frozen
     * @return ArrayList of Figures
     */
    public ArrayList<Figure> getAvailableFriendlyPieces(){
        ArrayList<Figure> figures = new ArrayList<>();
        if (board.isLoggingOn()){
            board.getLogger().log(Level.FINE, "Getting available friendly pieces");
        }
        for (Figure[] row : this.board.getBoard()) {
            for (Figure figure : row) {
                // add all friendly figures that are not frozen
                if (figure != null && figure.getFigureColor().equals(this.playerColor) && !figure.getIsFrozen()) {
                    figures.add(figure);
                    if (board.isLoggingOn()){
                        board.getLogger().log(Level.FINER, "Adding " + figure.toString() + " at " + figure.getRow() + " " + figure.getCol() + " to available friendly pieces");
                    }
                }
            }
        }
        return figures;
    }

    /**
     * Get all available enemy pieces that can be pushed
     * @return ArrayList of Figures
     */
    public ArrayList<Figure> getAvailableEnemyPieces(){
        ArrayList<Figure> figures = new ArrayList<>();
        if (board.isLoggingOn()){
            board.getLogger().log(Level.FINE, "Getting available enemy pieces");
        }
        for (Figure[] row : this.board.getBoard()) {
            for (Figure figure : row) {
                // add all enemy figures that can be pushed
                if (figure != null && !figure.getFigureColor().equals(this.playerColor) && figure.canBePushed()) {
                    figures.add(figure);
                    if (board.isLoggingOn()){
                        board.getLogger().log(Level.FINER, "Adding " + figure.toString() + " at " + figure.getRow() + " " + figure.getCol() + " to available enemy pieces");
                    }
                }
            }
        }
        return figures;
    }

    /**
     * Places a figure on the board
     * @param row row to place the figure
     * @param col column to place the figure
     */
    public void placeFigure(int row, int col){
        if (board.isLoggingOn()){
            board.getLogger().log(Level.FINER, "Placing figure at " + row + " " + col);
        }
        if ((this.playerColor == PlayerColor.GOLD && row != 0 && row != 1) || (this.playerColor == PlayerColor.SILVER && row != 6 && row != 7)){
            return;
        }
        if (elephants < 1){
            if (board.placeElephant(col, row, this.playerColor)){
                this.elephants++;
            }
        }
        if (camels < 1){
            if (board.placeCamel(col, row, this.playerColor)){
                this.camels++;
            }
        }
        if (horses < 2){
            if (board.placeHorse(col, row, this.playerColor)){
                this.horses++;
            }
        }
        if (dogs < 2){
            if (board.placeDog(col, row, this.playerColor)){
                this.dogs++;
            }
        }
        if (cats < 2){
            if (board.placeCat(col, row, this.playerColor)){
                this.cats++;
            }
        }
        if (rabbits < 8){
            if (board.placeRabbit(col, row, this.playerColor)){
                this.rabbits++;
            }
        }
    }
}
