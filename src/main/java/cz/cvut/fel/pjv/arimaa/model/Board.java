package cz.cvut.fel.pjv.arimaa.model;

import cz.cvut.fel.pjv.arimaa.model.figures.*;

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
            board[y][x] = new Elephant(color);
            return true;
        }
        return false;
    }

    public boolean placeCamel(int x, int y, Color color){
        if (board[y][x] == null){
            board[y][x] = new Camel(color);
            return true;
        }
        return false;
    }

    public boolean placeHorse(int x, int y, Color color){
        if (board[y][x] == null){
            board[y][x] = new Horse(color);
            return true;
        }
        return false;
    }

    public boolean placeDog(int x, int y, Color color){
        if (board[y][x] == null){
            board[y][x] = new Dog(color);
            return true;
        }
        return false;
    }

    public boolean placeCat(int x, int y, Color color){
        if (board[y][x] == null){
            board[y][x] = new Cat(color);
            return true;
        }
        return false;
    }

    public boolean placeRabbit(int x, int y, Color color){
        if (board[y][x] == null){
            board[y][x] = new Rabbit(color);
            return true;
        }
        return false;
    }
}
