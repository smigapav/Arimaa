package cz.cvut.fel.pjv.arimaa.model;

import cz.cvut.fel.pjv.arimaa.model.figures.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Board {
    private Figure[][] board;
    private Player goldPlayer;
    private Player silverPlayer;
    private List<Figure> canBePulled = new ArrayList<>();
    private Player currentPlayer;
    private int turnNumber = 0;
    private StringBuilder history = new StringBuilder(turnNumber + "g ");
    private Coords pullPosition = new Coords(-1, -1);
    private GameSaver gameSaver = new GameSaver();
    private GameLoader gameLoader = new GameLoader();
    private boolean loggingOn;
    private boolean loadedGame;
    private boolean isPlayingAgainstBot;
    private static final Logger logger = Logger.getLogger(Board.class.getName());


    public Board(boolean logging, boolean isPlayingAgainstBot) {
        board = new Figure[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
        goldPlayer = new Player(PlayerColor.GOLD, this);
        silverPlayer = new Player(PlayerColor.SILVER, this);
        currentPlayer = goldPlayer;
        this.isPlayingAgainstBot = isPlayingAgainstBot;
        loadedGame = false;
        loggingOn = logging;
        if (loggingOn) {
            logger.log(Level.INFO, "New game started");
        }
        // Set the logging level to FINE
        logger.setLevel(Level.FINE);

        // Create a console handler to print logging messages to the console
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINE);
        logger.addHandler(consoleHandler);
    }

    public Board(List<String> file) {
        goldPlayer = new Player(PlayerColor.GOLD, this);
        silverPlayer = new Player(PlayerColor.SILVER, this);
        turnNumber = Integer.parseInt(file.get(0));
        file.remove(0);
        PlayerColor playerColor = PlayerColor.valueOf(file.get(0));
        currentPlayer = playerColor == PlayerColor.GOLD ? goldPlayer : silverPlayer;
        file.remove(0);
        currentPlayer.setMovesLeft(Integer.parseInt(file.get(0)));
        file.remove(0);
        canBePulled = new ArrayList<>();
        pullPosition = new Coords(-1, -1);
        history = new StringBuilder(file.get(0));
        file.remove(0);
        this.isPlayingAgainstBot = Boolean.valueOf(file.get(0));
        file.remove(0);
        this.loggingOn = Boolean.valueOf(file.get(0));
        file.remove(0);
        board = new Figure[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
        loadedGame = true;
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
        if (loggingOn) {
            logger.log(Level.FINE, "New game started");
        }
        // Set the logging level to FINE
        logger.setLevel(Level.FINE);

        // Create a console handler to print log messages to the console
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);
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

    public Logger getLogger() {
        return logger;
    }

    public boolean isLoggingOn() {
        return loggingOn;
    }

    public StringBuilder getHistory() {
        return history;
    }

    public boolean isLoadedGame() {
        return loadedGame;
    }

    public boolean isPlayingAgainstBot() {
        return isPlayingAgainstBot;
    }

    public Player getGoldPlayer() {
        return goldPlayer;
    }

    public Player getSilverPlayer() {
        return silverPlayer;
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
        history.append("\n");
        history.append(turnNumber);
        if (currentPlayer.getPlayerColor() == PlayerColor.GOLD) {
            history.append("g ");
        } else {
            history.append("s ");
        }
        if (isPlayingAgainstBot && currentPlayer.getPlayerColor() == PlayerColor.SILVER) {
            botPlay();
        }
        if (loggingOn) {
            logger.log(Level.FINE, "Current player changed to " + currentPlayer.getPlayerColor());
        }
    }

    private void botPlay() {
        Random random = new Random();
        int numberOfMoves = random.nextInt(3) + 1;

        if (loggingOn) {
            logger.log(Level.FINE, "Bot is playing");
        }
        if (this.turnNumber == 0) {
            botPlaceFigures();
        }
        // Picks a random figure that can move, and moves it
        else {
            int[] nextMove = new int[]{-1, -1};
            List<Figure> figuresThatCanPush = new ArrayList<>();
            for (int i = 0; i < numberOfMoves; i++) {
                // check if nextMove is set, if so, move there a figure from figuresThatCanPush
                if (nextMove[0] != -1) {
                    logger.log(Level.FINE, "Bot is pushed a figure, moving to it's former location");
                    Figure pushFigure = figuresThatCanPush.get(random.nextInt(figuresThatCanPush.size()));
                    // move there a figure from figuresThatCanPush
                    pushFigure.move(nextMove[0], nextMove[1]);
                    nextMove = new int[]{-1, -1};
                    figuresThatCanPush = new ArrayList<>();
                }
                else if ((numberOfMoves - i) >= 1) {
                    logger.log(Level.FINE, "More than 1 move left");
                    List<Figure> availableMoves = new ArrayList<>();
                    List<Figure> availableFriendlyPieces = silverPlayer.getAvailableFriendlyPieces();
                    List<Figure> availableEnemyPieces = silverPlayer.getAvailableEnemyPieces();
                    availableMoves.addAll(canBePulled);
                    for (Figure figure : availableFriendlyPieces) {
                        if (getViableMoves(figure).size() > 0) {
                            availableMoves.add(figure);
                        }
                    }
                    for (Figure figure : availableEnemyPieces) {
                        if (getViableMoves(figure).size() > 0) {
                            availableMoves.add(figure);
                        }
                    }
                    // get a random figure that can move
                    Figure selectedFigure = availableMoves.get(random.nextInt(availableMoves.size() - 1));
                    // if the figure is an enemy figure, push it
                    if (silverPlayer.getAvailableEnemyPieces().contains(selectedFigure)) {
                        nextMove = new int[]{selectedFigure.getRow(), selectedFigure.getCol()};
                        for (Figure fig : selectedFigure.getAdjacentTiles()) {
                            if (fig != null && fig.getFigureColor() == PlayerColor.SILVER && fig.isStronger(selectedFigure)) {
                                figuresThatCanPush.add(fig);
                                logger.log(Level.FINE, "Bot chose enemy figure");
                            }
                        }
                    }
                    // add all viable moves into a list
                    List<int[]> viableMoves = getViableMoves(selectedFigure);
                    // get a random viable move
                    int[] randomMove = viableMoves.get(random.nextInt(viableMoves.size()));
                    // move the figure to random free position adjacent to it and
                    selectedFigure.move(randomMove[0], randomMove[1]);
                } else {
                    logger.log(Level.FINE, "Only 1 move left");
                    List<Figure> availableMoves = new ArrayList<>();
                    List<Figure> availableFriendlyPieces = silverPlayer.getAvailableFriendlyPieces();
                    for (Figure figure : availableFriendlyPieces) {
                        if (getViableMoves(figure).size() > 0) {
                            availableMoves.add(figure);
                        }
                    }
                    // get a random figure that can move
                    Figure selectedFigure = availableMoves.get(random.nextInt(availableMoves.size() - 1));
                    // add all viable moves into a list
                    List<int[]> viableMoves = getViableMoves(selectedFigure);
                    // get a random viable move
                    int[] randomMove = viableMoves.get(random.nextInt(viableMoves.size()));
                    selectedFigure.move(randomMove[0], randomMove[1]);
                    logger.log(Level.FINE, "Bot is moving a figure");
                }
            }
        }
        this.changeCurrentPlayer();
    }

    private List<int[]> getViableMoves(Figure selectedFigure) {
        List<int[]> viableMoves = new ArrayList<>();
        for (int j = 0; j < 4; j++) {
            switch (j) {
                case 0 -> {
                    if (selectedFigure.getRow() + 1 <= 7 && board[selectedFigure.getRow() + 1][selectedFigure.getCol()] == null) {
                        viableMoves.add(new int[]{selectedFigure.getRow() + 1, selectedFigure.getCol()});
                    }
                }
                case 1 -> {
                    if (selectedFigure.getRow() - 1 >= 0 && board[selectedFigure.getRow() - 1][selectedFigure.getCol()] == null) {
                        viableMoves.add(new int[]{selectedFigure.getRow() - 1, selectedFigure.getCol()});
                    }
                }
                case 2 -> {
                    if (selectedFigure.getCol() + 1 <= 7 && board[selectedFigure.getRow()][selectedFigure.getCol() + 1] == null) {
                        viableMoves.add(new int[]{selectedFigure.getRow(), selectedFigure.getCol() + 1});
                    }
                }
                case 3 -> {
                    if (selectedFigure.getCol() - 1 >= 0 && board[selectedFigure.getRow()][selectedFigure.getCol() - 1] == null) {
                        viableMoves.add(new int[]{selectedFigure.getRow(), selectedFigure.getCol() - 1});
                    }
                }
            }
        }
        return viableMoves;
    }

    private void botPlaceFigures() {
        silverPlayer.placeFigure(6, 3);
        silverPlayer.placeFigure(6, 4);
        silverPlayer.placeFigure(6, 2);
        silverPlayer.placeFigure(6, 5);
        silverPlayer.placeFigure(6, 1);
        silverPlayer.placeFigure(6, 6);
        silverPlayer.placeFigure(6, 0);
        silverPlayer.placeFigure(6, 7);
        for (int i = 0; i < 8; i++) {
            silverPlayer.placeFigure(7, i);
        }
    }

    public PlayerColor getWinner(){
        int goldenRabbits = 0;
        int silverRabbits = 0;
        if (loggingOn) {
            logger.log(Level.FINE, "Checking for winner");
        }
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
        if (loggingOn) {
            logger.log(Level.FINE, "Checking traps");
        }
        for (Figure tile : tiles) {
            if (tile != null && tile.getAdjacentFriendlyFigures().size() == 0){
                tile.addDeathToHistory();
                board[tile.getRow()][tile.getCol()] = null;
                if (loggingOn) {
                    logger.log(Level.FINER, "Trap triggered at " + tile.getRow() + " " + tile.getCol());
                }
            }
        }
    }

    // Updates isFrozen for all figures on the board
    public void checkIfFrozenForAllTiles(){
        if (loggingOn) {
            logger.log(Level.FINE, "Checking if frozen for all tiles");
        }
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Figure tile = this.board[i][j];
                if (tile != null){
                    if (loggingOn) {
                        logger.log(Level.FINER, "Checking if frozen for tile at " + tile.getRow() + " " + tile.getCol());
                    }
                    tile.checkIfFrozen();
                }
            }
        }
    }

    public boolean placeElephant(int col, int row, PlayerColor playerColor){
        if (board[row][col] == null){
            board[row][col] = new Elephant(playerColor, this, row, col);
            if (loggingOn) {
                logger.log(Level.FINE, "Elephant placed at " + row + " " + col);
            }
            return true;
        }
        return false;
    }

    public boolean placeCamel(int col, int row, PlayerColor playerColor){
        if (board[row][col] == null){
            board[row][col] = new Camel(playerColor, this, row, col);
            if (loggingOn) {
                logger.log(Level.FINE, "Camel placed at " + row + " " + col);
            }
            return true;
        }
        return false;
    }

    public boolean placeHorse(int col, int row, PlayerColor playerColor){
        if (board[row][col] == null){
            board[row][col] = new Horse(playerColor, this, row, col);
            if (loggingOn) {
                logger.log(Level.FINE, "Horse placed at " + row + " " + col);
            }
            return true;
        }
        return false;
    }

    public boolean placeDog(int col, int row, PlayerColor playerColor){
        if (board[row][col] == null){
            board[row][col] = new Dog(playerColor, this, row, col);
            if (loggingOn) {
                logger.log(Level.FINE, "Dog placed at " + row + " " + col);
            }
            return true;
        }
        return false;
    }

    public boolean placeCat(int col, int row, PlayerColor playerColor){
        if (board[row][col] == null){
            board[row][col] = new Cat(playerColor, this, row, col);
            if (loggingOn) {
                logger.log(Level.FINE, "Cat placed at " + row + " " + col);
            }
            return true;
        }
        return false;
    }

    public boolean placeRabbit(int col, int row, PlayerColor playerColor){
        if (board[row][col] == null){
            board[row][col] = new Rabbit(playerColor, this, row, col);
            if (loggingOn) {
                logger.log(Level.FINE, "Rabbit placed at " + row + " " + col);
            }
            return true;
        }
        return false;
    }

    public static class GameSaver {
        private static final String FILE_PATH = "src/main/resources/saveFile.txt";

        public static void saveGame(Board board) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                writer.write(String.valueOf(board.getTurnNumber()));
                writer.newLine();
                writer.write(board.getCurrentPlayer().getPlayerColor().toString());
                writer.newLine();
                writer.write(String.valueOf(board.getCurrentPlayer().getMovesLeft()));
                writer.newLine();
                writer.write(String.valueOf(board.isPlayingAgainstBot()));
                writer.newLine();
                writer.write(String.valueOf(board.loggingOn));
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
                writer.write("HISTORY STARTS HERE");
                writer.newLine();
                writer.write(board.getHistory().toString().replace("\n", "<newline>"));
                if (board.loggingOn) {
                    logger.log(Level.FINE, "Game saved");
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (board.loggingOn) {
                    logger.log(Level.SEVERE, "Game not saved");
                }
            }
        }
    }

    public static class GameLoader {
        private static final String FILE_PATH = "src/main/resources/saveFile.txt";

        public static List<String> readFileRows() {
            List<String> rows = new ArrayList<>();
            boolean isMessage = false;
            StringBuilder message = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equals("HISTORY STARTS HERE")) {
                        isMessage = true;
                    }
                    if (!isMessage) {
                        rows.add(line);
                    } else {
                        if (!line.equals("HISTORY STARTS HERE")) {
                            message.append(line);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            rows.add(3, message.toString().replace("<newline>", "\n"));
            return rows;
        }
    }
}
