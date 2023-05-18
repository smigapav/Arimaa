package cz.cvut.fel.pjv.arimaa.model;

import cz.cvut.fel.pjv.arimaa.model.figures.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private Figure[][] board;
    private Player goldPlayer;
    private Player silverPlayer;
    private List<Figure> canBePulled = new ArrayList<>();
    private Player currentPlayer;
    private int turnNumber = 0;
    private Coords pullPosition = new Coords(-1, -1);
    private GameSaver gameSaver = new GameSaver();
    private GameLoader gameLoader = new GameLoader();


    public Board() {
        board = new Figure[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
        goldPlayer = new Player(PlayerColor.GOLD, this);
        silverPlayer = new Player(PlayerColor.SILVER, this);
        currentPlayer = goldPlayer;
    }

    public Board(List<String> file) {
        goldPlayer = new Player(PlayerColor.GOLD, this);
        silverPlayer = new Player(PlayerColor.SILVER, this);
        turnNumber = Integer.parseInt(file.get(0));
        System.out.println(turnNumber);
        file.remove(0);
        PlayerColor playerColor = PlayerColor.valueOf(file.get(0));
        currentPlayer = playerColor == PlayerColor.GOLD ? goldPlayer : silverPlayer;
        file.remove(0);
        currentPlayer.setMovesLeft(Integer.parseInt(file.get(0)));
        file.remove(0);
        canBePulled = new ArrayList<>();
        pullPosition = new Coords(-1, -1);
        board = new Figure[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
        for (String row : file) {
            String[] figure = row.split(" ");
            switch (Integer.parseInt(figure[1])){
                case 0:
                    placeRabbit(Integer.parseInt(figure[3]), Integer.parseInt(figure[2]), PlayerColor.valueOf(figure[0]));
                    break;
                case 1:
                    placeCat(Integer.parseInt(figure[3]), Integer.parseInt(figure[2]), PlayerColor.valueOf(figure[0]));
                    break;
                case 2:
                    placeDog(Integer.parseInt(figure[3]), Integer.parseInt(figure[2]), PlayerColor.valueOf(figure[0]));
                    break;
                case 3:
                    placeHorse(Integer.parseInt(figure[3]), Integer.parseInt(figure[2]), PlayerColor.valueOf(figure[0]));
                    break;
                case 4:
                    placeCamel(Integer.parseInt(figure[3]), Integer.parseInt(figure[2]), PlayerColor.valueOf(figure[0]));
                    break;
                case 5:
                    placeElephant(Integer.parseInt(figure[3]), Integer.parseInt(figure[2]), PlayerColor.valueOf(figure[0]));
                    break;
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

    public Player getGoldPlayer() {
        return goldPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public GameSaver getGameSaver() {
        return gameSaver;
    }

    public GameLoader getGameLoader() {
        return gameLoader;
    }

    public void changeCurrentPlayer() {
        if (currentPlayer == goldPlayer){
            currentPlayer.resetMovesLeft();
            currentPlayer = silverPlayer;
        }
        else {
            currentPlayer.resetMovesLeft();
            turnNumber++;
            currentPlayer = goldPlayer;
        }
    }

    public Player getSilverPlayer() {
        return silverPlayer;
    }

    public PlayerColor getWinner(){
        int goldenRabbits = 0;
        int silverRabbits = 0;
        for (Figure tile : this.getBoard()[7]) {
            if (tile != null && tile.getFigureColor() == PlayerColor.GOLD && tile.getStrength() == 0) {
                return PlayerColor.GOLD;
            }
        }
        for (Figure tile : this.getBoard()[0]) {
            if (tile != null && tile.getFigureColor() == PlayerColor.SILVER && tile.getStrength() == 0) {
                return PlayerColor.SILVER;
            }
        }
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Figure tile = this.getBoard()[i][j];
                if (tile != null && tile.getStrength() == 0){
                    if (tile.getFigureColor() == PlayerColor.GOLD){
                        goldenRabbits++;
                    } else {
                        silverRabbits++;
                    }
                }
            }
        }
        if (goldenRabbits == 0){
            return PlayerColor.SILVER;
        }
        if (silverRabbits == 0){
            return PlayerColor.GOLD;
        }
        if (this.getCurrentPlayer().getAvailableFriendlyPieces().size() == 0 && this.getCurrentPlayer().getAvailableEnemyPieces().size() == 0) {
            if (this.getCurrentPlayer().getPlayerColor() == PlayerColor.GOLD) {
                return PlayerColor.SILVER;
            } else {
                return PlayerColor.GOLD;
            }
        }
        return null;
    }

    public Coords getPullPosition() {
        return pullPosition;
    }

    public void setPullPosition(Coords pullPosition) {
        this.pullPosition = pullPosition;
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

    // Updates isFrozen for all figures on the board
    public void checkIfFrozenForAllTiles(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Figure tile = this.board[i][j];
                if (tile != null){
                    tile.checkIfFrozen();
                }
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

    public class GameSaver {
        private static final String FILE_PATH = "src/main/resources/saveFile.txt";

        public static void saveGame(Board board) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                writer.write(String.valueOf(board.getTurnNumber()));
                writer.newLine();
                writer.write(board.getCurrentPlayer().getPlayerColor().toString());
                writer.newLine();
                writer.write(String.valueOf(board.getCurrentPlayer().getMovesLeft()));
                writer.newLine();
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        Figure figure = board.getBoard()[row][col];
                        if (figure != null) {
                            PlayerColor playerColor = figure.getFigureColor();
                            int strength = figure.getStrength();
                            writer.write(playerColor + " " + strength + " " + row + " " + col);
                            writer.newLine();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class GameLoader {
        private static final String FILE_PATH = "src/main/resources/saveFile.txt";

        public static List<String> readFileRows() {
            List<String> rows = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    rows.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return rows;
        }
    }
}
