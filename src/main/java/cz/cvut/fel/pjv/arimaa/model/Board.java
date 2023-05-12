package cz.cvut.fel.pjv.arimaa.model;

import cz.cvut.fel.pjv.arimaa.model.figures.*;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Figure[][] board;
    private final Player goldPlayer;
    private final Player silverPlayer;
    private List<Figure> canBePulled = new ArrayList<>();

    public Board() {
        board = new Figure[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
        goldPlayer = new Player(PlayerColor.GOLD, this);
        silverPlayer = new Player(PlayerColor.SILVER, this);
    }

    @Override
    public String toString() {
        String out = "";
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                out += (board[row][col] + " ");
            }
            out += "\n";
        }
        return out;
    }

    public Figure[][] getBoard() {
        return board;
    }

    public Player getGoldPlayer() {
        return goldPlayer;
    }

    public Player getSilverPlayer() {
        return silverPlayer;
    }

    public boolean isWinner(){
        //TODO
        return false;
    }

    public PlayerColor getWinner(){
        //TODO
        return null;
    }

    public List<Figure> getCanBePulled() {
        return canBePulled;
    }

    public void setCanBePulled(List<Figure> canBePulled) {
        this.canBePulled = canBePulled;
    }

    public void checkTraps(){
        Figure[] tiles = {board[2][2], board[2][5], board[5][2], board[5][5]};
        for (Figure tile : tiles) {
            if (tile != null && tile.getAdjacentFriendlyFigures().size() == 0){
                board[tile.getRow()][tile.getCol()] = null;
            }
        }
    }

    public boolean placeElephant(int col, int row, PlayerColor playerColor){
        if (board[row][col] == null){
            board[row][col] = new Elephant(playerColor, this, row, col);
            return true;
        }
        return false;
    }

    public boolean placeCamel(int col, int row, PlayerColor playerColor){
        if (board[row][col] == null){
            board[row][col] = new Camel(playerColor, this, row, col);
            return true;
        }
        return false;
    }

    public boolean placeHorse(int col, int row, PlayerColor playerColor){
        if (board[row][col] == null){
            board[row][col] = new Horse(playerColor, this, row, col);
            return true;
        }
        return false;
    }

    public boolean placeDog(int col, int row, PlayerColor playerColor){
        if (board[row][col] == null){
            board[row][col] = new Dog(playerColor, this, row, col);
            return true;
        }
        return false;
    }

    public boolean placeCat(int col, int row, PlayerColor playerColor){
        if (board[row][col] == null){
            board[row][col] = new Cat(playerColor, this, row, col);
            return true;
        }
        return false;
    }

    public boolean placeRabbit(int col, int row, PlayerColor playerColor){
        if (board[row][col] == null){
            board[row][col] = new Rabbit(playerColor, this, row, col);
            return true;
        }
        return false;
    }
}
