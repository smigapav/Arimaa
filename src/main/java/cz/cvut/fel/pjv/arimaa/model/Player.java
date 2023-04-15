package cz.cvut.fel.pjv.arimaa.model;

import cz.cvut.fel.pjv.arimaa.model.figures.Figure;
import cz.cvut.fel.pjv.arimaa.model.tiles.Tile;

public class Player {
    private final Color playerColor;
    private int time;
    private int movesLeft;
    private final Board board;

    public Player(Color playerColor, int time, Board board) {
        this.playerColor = playerColor;
        this.time = time;
        this.movesLeft = 4;
        this.board = board;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void endTurn(){
        //TODO
        //Ends players turn and sets his movesLeft back to 4
    }

    public Figure placeFigures(Tile tile){
        //TODO
        //player places his figures at the start of the game
        return null;
    }
}
