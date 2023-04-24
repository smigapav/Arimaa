package cz.cvut.fel.pjv.arimaa.model;

import cz.cvut.fel.pjv.arimaa.model.figures.*;

import java.util.Arrays;

public class Board {
    private final Figure[][] board;

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

    public boolean placeElephant(int x, int y, Color color){
        if (board[y][x] == null){
            board[y][x] = new Elephant(color, this, x, y);
            return true;
        }
        return false;
    }

    public boolean placeCamel(int x, int y, Color color){
        if (board[y][x] == null){
            board[y][x] = new Camel(color, this, x, y);
            return true;
        }
        return false;
    }

    public boolean placeHorse(int x, int y, Color color){
        if (board[y][x] == null){
            board[y][x] = new Horse(color, this, x, y);
            return true;
        }
        return false;
    }

    public boolean placeDog(int x, int y, Color color){
        if (board[y][x] == null){
            board[y][x] = new Dog(color, this, x, y);
            return true;
        }
        return false;
    }

    public boolean placeCat(int x, int y, Color color){
        if (board[y][x] == null){
            board[y][x] = new Cat(color, this, x, y);
            return true;
        }
        return false;
    }

    public boolean placeRabbit(int x, int y, Color color){
        if (board[y][x] == null){
            board[y][x] = new Rabbit(color, this, x, y);
            return true;
        }
        return false;
    }
}
