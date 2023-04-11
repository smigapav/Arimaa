package cz.cvut.fel.pjv.arimaa.model;

public class Player {
    private final Color playerColor;
    private int time;

    public Player(Color playerColor, int time) {
        this.playerColor = playerColor;
        this.time = time;
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
    }
}
