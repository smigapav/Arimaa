package cz.cvut.fel.pjv.arimaa.model;

import cz.cvut.fel.pjv.arimaa.model.figures.Figure;
import cz.cvut.fel.pjv.arimaa.model.tiles.Tile;

public class Player {
    private final Color playerColor;
    private int time;
    private int movesLeft;
    private final Board board;
    private int elephants;
    private int camels;
    private int horses;
    private int dogs;
    private int cats;
    private int rabbits;

    public Player(Color playerColor, Board board) {
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

    public void placeFigure(int x, int y){
        if ((this.playerColor == Color.GOLD && y != 0 && y != 1) || (this.playerColor == Color.SILVER && y != 6 && y != 7)){
            return;
        }
        if (elephants < 1){
            if (board.placeElephant(x, y, this.playerColor)){
                this.elephants++;
            }
        }
        if (camels < 2){
            if (board.placeCamel(x, y, this.playerColor)){
                this.camels++;
            }
        }
        if (horses < 2){
            if (board.placeHorse(x, y, this.playerColor)){
                this.horses++;
            }
        }
        if (dogs < 2){
            if (board.placeDog(x, y, this.playerColor)){
                this.dogs++;
            }
        }
        if (cats < 2){
            if (board.placeCat(x, y, this.playerColor)){
                this.cats++;
            }
        }
        if (rabbits < 8){
            if (board.placeRabbit(x, y, this.playerColor)){
                this.rabbits++;
            }
        }
    }
}
