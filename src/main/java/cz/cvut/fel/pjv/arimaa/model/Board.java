package cz.cvut.fel.pjv.arimaa.model;

import cz.cvut.fel.pjv.arimaa.model.figures.*;

public class Board {
    private final Figure[][] board;
    private Player goldPlayer;
    private Player silverPlayer;

    public Board() {
        board = new Figure[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }

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

    public boolean isWinner(){
        //TODO
        return false;
    }

    public Color getWinner(){
        //TODO
        return null;
    }

    public void setGoldPlayer(Player player){
        this.goldPlayer = player;
    }

    public void setSilverPlayer(Player player){
        this.silverPlayer = player;
    }

    public boolean placeElephant(int col, int row, Color color){
        if (board[row][col] == null){
            board[row][col] = new Elephant(color, this, row, col);
            return true;
        }
        return false;
    }

    public boolean placeCamel(int col, int row, Color color){
        if (board[row][col] == null){
            board[row][col] = new Camel(color, this, row, col);
            return true;
        }
        return false;
    }

    public boolean placeHorse(int col, int row, Color color){
        if (board[row][col] == null){
            board[row][col] = new Horse(color, this, row, col);
            return true;
        }
        return false;
    }

    public boolean placeDog(int col, int row, Color color){
        if (board[row][col] == null){
            board[row][col] = new Dog(color, this, row, col);
            return true;
        }
        return false;
    }

    public boolean placeCat(int col, int row, Color color){
        if (board[row][col] == null){
            board[row][col] = new Cat(color, this, row, col);
            return true;
        }
        return false;
    }

    public boolean placeRabbit(int col, int row, Color color){
        if (board[row][col] == null){
            board[row][col] = new Rabbit(color, this, row, col);
            return true;
        }
        return false;
    }
}
