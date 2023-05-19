package cz.cvut.fel.pjv.arimaa.view;

import cz.cvut.fel.pjv.arimaa.model.Board;
import cz.cvut.fel.pjv.arimaa.model.Coords;
import cz.cvut.fel.pjv.arimaa.model.MoveType;
import cz.cvut.fel.pjv.arimaa.model.figures.Figure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static cz.cvut.fel.pjv.arimaa.model.PlayerColor.*;

public class Table extends JFrame {

    private final JFrame gameFrame;
    private BoardPanel boardPanel;
    private static Dimension OUTER_FRAME_DIMENSION = new Dimension(1000, 800);
    private JLabel message;
    private Board board;
    private HistoryPanel historyPanel;
    private Figure selectedFigure = null;
    private MoveType moveType = null;
    private JToolBar tools;

    public Table(Board board) {
        this.board = board;
        this.gameFrame = new JFrame("Arimaa");
        this.gameFrame.setLayout(new BorderLayout(3, 3));
        tools = new JToolBar();
        createToolBar();
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.boardPanel = new BoardPanel();
        this.historyPanel = new HistoryPanel();
        this.gameFrame.add(this.historyPanel, BorderLayout.EAST);
        this.gameFrame.add(boardPanel, BorderLayout.CENTER);
        pack();
        this.gameFrame.setVisible(true);
        this.gameFrame.setResizable(false);
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if (board.isLoggingOn()) {
            board.getLogger().log(Level.INFO, "Table GUI created");
        }
    }

    private void createToolBar() {
        tools.setFloatable(false);
        gameFrame.add(tools, BorderLayout.PAGE_START);
        JButton newButton = new JButton("New");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board = new Board(true);
                boardPanel.redrawBoard();
            }
        });
        tools.add(newButton);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.getGameSaver().saveGame(board);
            }
        });
        tools.add(saveButton);
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> file = board.getGameLoader().readFileRows();
                board = new Board(file, true);
                boardPanel.redrawBoard();
                historyPanel.redrawHistory();
                redrawToolBar();
            }
        });
        tools.add(loadButton);
        tools.addSeparator();
        message = getMessageText();
        tools.add(message);
        tools.addSeparator();
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        tools.add(exitButton);
        tools.addSeparator();
        JButton endTurn = new JButton("End Turn");
        endTurn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // if you didn't move any figure, you can't end your turn
                if (board.getCurrentPlayer().getMovesLeft() == 4 && board.getTurnNumber() != 0) {
                    return;
                }
                // if you pushed an enemy figure and didn't move your figure to its former place, you can't end the turn
                if (moveType == MoveType.PUSH) {
                    return;
                }
                board.setCanBePulled(new ArrayList<>());
                board.changeCurrentPlayer();
                redrawToolBar();
            }
        });
        tools.add(endTurn);
        if (board.isLoggingOn()) {
            board.getLogger().log(Level.FINE, "Toolbar created");
        }
    }


    private void redrawToolBar() {
        tools.removeAll(); // Remove all components from the existing JToolBar
        createToolBar(); // Create the JToolBar again
        tools.revalidate(); // Revalidate the toolbar to update the layout
        tools.repaint(); // Repaint the toolbar to reflect the changes
        if (board.isLoggingOn()) {
            board.getLogger().log(Level.FINER, "Toolbar redrawn");
        }
    }

    private JLabel getMessageText() {
        if (board.isLoggingOn()) {
            board.getLogger().log(Level.FINER, "Message text created");
        }
        if (board.getTurnNumber() == 0 && board.getCurrentPlayer().getPlayerColor() == GOLD) {
            return new JLabel("Gold player - Place your figures");
        }
        if (board.getTurnNumber() == 0 && board.getCurrentPlayer().getPlayerColor() == SILVER) {
            return new JLabel("Silver player - Place your figures");
        }
        if (board.getCurrentPlayer().getPlayerColor() == GOLD) {
            return new JLabel("Gold player - " + board.getCurrentPlayer().getMovesLeft() + " moves left");
        }
        if (board.getCurrentPlayer().getPlayerColor() == SILVER) {
            return new JLabel("Silver player - " + board.getCurrentPlayer().getMovesLeft() + " moves left");
        }
        return new JLabel("Error");
    }

    private class EndOfGameScreen extends JFrame {
        private final JFrame endOfGameFrame;
        private static final Dimension END_OF_GAME_FRAME_DIMENSION = new Dimension(400, 400);

        EndOfGameScreen() {
            this.endOfGameFrame = new JFrame("End of game");
            this.endOfGameFrame.setLayout(new BorderLayout(3, 3));
            this.endOfGameFrame.setSize(END_OF_GAME_FRAME_DIMENSION);
            this.endOfGameFrame.setVisible(true);
            this.endOfGameFrame.setResizable(false);
            this.endOfGameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            if (board.isLoggingOn()) {
                board.getLogger().log(Level.INFO, "End of game screen created");
            }
        }
    }

    private class BoardPanel extends JPanel {

        private JButton[][] chessBoardSquares = new JButton[8][8];
        private static final String COLS = "ABCDEFGH";

        BoardPanel() {
            super(new GridLayout(9, 9));
            fillPanelWithTilesAndCoords();
            assignFigureImagesToTiles(board);
            if (board.isLoggingOn()) {
                board.getLogger().log(Level.INFO, "Board panel created");
            }
        }

        private void redrawBoard() {
            removeAll();
            redrawToolBar();
            fillPanelWithTilesAndCoords();
            assignFigureImagesToTiles(board);
            validate();
            repaint();
            if (board.getWinner() != null && board.getTurnNumber() != 0) {
                EndOfGameScreen endOfGameScreen = new EndOfGameScreen();
                JLabel winner = new JLabel(board.getWinner() + " player won!");
                winner.setHorizontalAlignment(SwingConstants.CENTER);
                endOfGameScreen.endOfGameFrame.add(winner);
            }
            if (board.isLoggingOn()) {
                board.getLogger().log(Level.FINER, "Board panel redrawn");
            }
        }

        private void fillPanelWithTilesAndCoords() {
            if (board.isLoggingOn()) {
                board.getLogger().log(Level.FINER, "Board panel filled with tiles and coordinates");
            }
            this.add(new JLabel(""));
            // fill the top row
            for (int row = 0; row < 8; row++) {
                // add the column letters
                this.add(
                        new JLabel(COLS.substring(row, row + 1),
                                SwingConstants.CENTER));
            }
            // fill the rest
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (col == 0) {
                        // add the row number
                        this.add(new JLabel("" + (8 - row), SwingConstants.CENTER));
                    }
                    JButton b = new JButton();
                    addListenersToTiles(b, row, col);
                    if ((row == 2 || row == 5) && (col == 2 || col == 5)) {
                        b.setBackground(new Color(140, 33, 42));
                    } else {
                        b.setBackground(Color.WHITE);
                    }
                    chessBoardSquares[row][col] = b;
                    this.add(chessBoardSquares[row][col]);
                }
            }
        }

        private void assignFigureImagesToTiles(Board board) {
            if (board.isLoggingOn()) {
                board.getLogger().log(Level.FINER, "Board panel filled with figure images");
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    chessBoardSquares[i][j].removeAll();
                    Figure figure = board.getBoard()[7- i][j];
                    if (figure != null) {
                        Icon icon = getIconForFigure(figure);
                        this.chessBoardSquares[i][j].setIcon(icon);
                    }
                }
            }
        }

        private void addListenersToTiles(JButton b, int viewRow, int viewCol) {
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // listener for the first turn
                    if (board.getTurnNumber() == 0){
                        board.getCurrentPlayer().placeFigure(7 - viewRow, viewCol);
                    }
                    // listener for normal turns
                    else {
                        // if player has no moves left
                        if (board.getCurrentPlayer().getMovesLeft() == 0){
                            return;
                        }
                        // select a figure to move
                        if (selectedFigure == null) {
                            // select friendly figure
                            if (board.getCurrentPlayer().getAvailableFriendlyPieces().contains(board.getBoard()[7 - viewRow][viewCol]) && moveType != MoveType.PUSH) {
                                selectedFigure = board.getBoard()[7 - viewRow][viewCol];
                                moveType = MoveType.NORMAL_MOVE;
                            }
                            // if push happened last turn, select figure to pull to the pre-push position
                            else if (moveType == MoveType.PUSH && Math.abs(board.getPullPosition().getRow() - (7 - viewRow)) == 1 && board.getPullPosition().getCol() == viewCol ||
                                    Math.abs(board.getPullPosition().getCol() - viewCol) == 1 && board.getPullPosition().getRow() == (7 - viewRow)) {
                                selectedFigure = board.getBoard()[7 - viewRow][viewCol];
                                moveType = MoveType.NORMAL_MOVE;
                                selectedFigure.forceMove(board.getPullPosition().getRow(), board.getPullPosition().getCol());
                                board.setPullPosition(new Coords(-1, -1));
                                selectedFigure = null;  // deselect the figure
                            }
                            // select enemy figure for push
                            else if (board.getCurrentPlayer().getAvailableEnemyPieces().contains(board.getBoard()[7 - viewRow][viewCol]) && board.getCurrentPlayer().getMovesLeft() >= 2) {
                                selectedFigure = board.getBoard()[7 - viewRow][viewCol];
                                moveType = MoveType.PUSH;
                            }
                            // select enemy figure and pull it
                            else if (board.getCanBePulled().contains(board.getBoard()[7 - viewRow][viewCol])) {
                                selectedFigure = board.getBoard()[7 - viewRow][viewCol];
                                moveType = MoveType.PULL;
                                selectedFigure.forceMove(board.getPullPosition().getRow(), board.getPullPosition().getCol());
                                board.setPullPosition(new Coords(-1, -1));
                                selectedFigure = null;  // deselect the figure
                            }
                        }
                        // selectedFigure is not null, move that figure
                        else {
                            // if the selected figure is a friendly piece
                            if (selectedFigure.getFigureColor() == board.getCurrentPlayer().getPlayerColor()) {
                                if (Math.abs(selectedFigure.getRow() - (7 - viewRow)) == 1 && selectedFigure.getCol() == viewCol ||
                                        Math.abs(selectedFigure.getCol() - viewCol) == 1 && selectedFigure.getRow() == (7 - viewRow)) {
                                    selectedFigure.move(7 - viewRow, viewCol);
                                    selectedFigure = null;  // deselect the figure
                                } else {
                                    selectedFigure = null;  // deselect the figure
                                    moveType = null;
                                }
                            }
                            // if the selected figure is an enemy piece
                            else {
                                if (Math.abs(selectedFigure.getRow() - (7 - viewRow)) == 1 && selectedFigure.getCol() == viewCol ||
                                        Math.abs(selectedFigure.getCol() - viewCol) == 1 && selectedFigure.getRow() == (7 - viewRow)) {
                                    selectedFigure.forceMove(7 - viewRow, viewCol);
                                    selectedFigure = null;  // deselect the figure
                                } else {
                                    selectedFigure = null;  // deselect the figure
                                    moveType = null;
                                }
                            }
                        }
                    }
                    redrawBoard();
                    historyPanel.redrawHistory();
                }
            });
            if (board.isLoggingOn()) {
                board.getLogger().log(Level.FINER, "Listeners added to tiles");
            }
        }

        private Icon getIconForFigure(Figure figure) {
            URL imageUrl = getClass().getClassLoader().getResource("figures/");
            URI uri = null;
            String fileName = null;
            ImageIcon icon;
            if (figure.getFigureColor() == GOLD) {
                switch (figure.getStrength()) {
                    case 0:
                        fileName = "GOLD_RABBIT.png";
                        break;
                    case 1:
                        fileName = "GOLD_CAT.png";
                        break;
                    case 2:
                        fileName = "GOLD_DOG.png";
                        break;
                    case 3:
                        fileName = "GOLD_HORSE.png";
                        break;
                    case 4:
                        fileName = "GOLD_CAMEL.png";
                        break;
                    case 5:
                        fileName = "GOLD_ELEPHANT.png";
                        break;
                }
            } else {
                switch (figure.getStrength()) {
                    case 0:
                        fileName = "SILVER_RABBIT.png";
                        break;
                    case 1:
                        fileName = "SILVER_CAT.png";
                        break;
                    case 2:
                        fileName = "SILVER_DOG.png";
                        break;
                    case 3:
                        fileName = "SILVER_HORSE.png";
                        break;
                    case 4:
                        fileName = "SILVER_CAMEL.png";
                        break;
                    case 5:
                        fileName = "SILVER_ELEPHANT.png";
                        break;
                }
            }
            try {
                uri = imageUrl.toURI().resolve(fileName);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            try {
                icon = new ImageIcon(uri.toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            if (board.isLoggingOn()) {
                board.getLogger().log(Level.FINER, "Icon for figure " + figure + " created");
            }
            return icon;
        }

        public JButton[][] getChessBoardSquares() {
            return chessBoardSquares;
        }
    }

    private class HistoryPanel extends JPanel {
        private JTextArea historyTextArea;
        private JScrollPane scrollPane;

        public HistoryPanel() {
            setLayout(new BorderLayout());
            historyTextArea = new JTextArea(board.getHistory().toString());
            historyTextArea.setSize(200, 800);
            historyTextArea.setEditable(false);
            historyTextArea.setLineWrap(true);
            historyTextArea.setWrapStyleWord(true);
            scrollPane = new JScrollPane(historyTextArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            add(scrollPane, BorderLayout.CENTER);
        }

        public void updateHistory() {
            historyTextArea.setText(board.getHistory().toString());
        }

        public void redrawHistory() {
            historyTextArea.removeAll();
            historyTextArea.setText(board.getHistory().toString());
            historyTextArea.setCaretPosition(historyTextArea.getDocument().getLength());
        }
    }
}
