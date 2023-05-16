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

import static cz.cvut.fel.pjv.arimaa.model.PlayerColor.*;

public class Table extends JFrame {

    private final JFrame gameFrame;
    private BoardPanel boardPanel;
    private static Dimension OUTER_FRAME_DIMENSION = new Dimension(800, 800);
    private JLabel message;
    private Board board;
    private Figure selectedFigure = null;
    private MoveType moveType = null;

    public Table(Board board) {
        this.board = board;
        this.gameFrame = new JFrame("Arimaa");
        this.gameFrame.setLayout(new BorderLayout(3, 3));
        createToolBar();
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(boardPanel, BorderLayout.CENTER);
        pack();
        this.gameFrame.setVisible(true);
        this.gameFrame.setResizable(false);
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void createToolBar() {
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gameFrame.add(tools, BorderLayout.PAGE_START);
        // add some buttons to the JToolBar (TODO: add functionality to these buttons)
        JButton newButton = new JButton("New");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board = new Board();
                boardPanel.redrawBoard();
            }
        });
        tools.add(newButton);
        JButton saveButton = new JButton("Save");
        tools.add(saveButton);
        tools.addSeparator();
        // add the message label to the JToolBar
        message = new JLabel("Play Arimaa!!!");
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
                board.changeCurrentPlayer();
            }
        });
        tools.add(endTurn);
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    private class BoardPanel extends JPanel {

        private JButton[][] chessBoardSquares = new JButton[8][8];
        private static final Dimension tileDimensions = new Dimension(60, 60);
        private static final String COLS = "ABCDEFGH";

        BoardPanel() {
            super(new GridLayout(9, 9));
            fillPanelWithTilesAndCoords();
            assignFigureImagesToTiles(board);
        }

        private void redrawBoard() {
            removeAll();
            fillPanelWithTilesAndCoords();
            assignFigureImagesToTiles(board);
            validate();
            repaint();
        }

        private void fillPanelWithTilesAndCoords() {
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
                    b.setPreferredSize(tileDimensions);
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
                    System.out.println(board.getCanBePulled());
                    // listener for the first turn
                    if (board.getTurnNumber() == 0){
                        board.getCurrentPlayer().placeFigure(7 - viewRow, viewCol);
                    }
                    // listener for normal turns
                    else {
                        // select a figure to move
                        if (selectedFigure == null) {
                            // select friendly figure
                            if (board.getCurrentPlayer().getAvailableFriendlyPieces().contains(board.getBoard()[7 - viewRow][viewCol]) && moveType != MoveType.PUSH) {
                                selectedFigure = board.getBoard()[7 - viewRow][viewCol];
                                moveType = MoveType.NORMAL_MOVE;
                            }
                            // if push happend last turn, select figure to pull to the pre-push position
                            else if (moveType == MoveType.PUSH && Math.abs(board.getPullPosition().getRow() - (7 - viewRow)) == 1 && board.getPullPosition().getCol() == viewCol ||
                                    Math.abs(board.getPullPosition().getCol() - viewCol) == 1 && board.getPullPosition().getRow() == (7 - viewRow)) {
                                selectedFigure = board.getBoard()[7 - viewRow][viewCol];
                                moveType = MoveType.NORMAL_MOVE;
                                selectedFigure.forceMove(board.getPullPosition().getRow(), board.getPullPosition().getCol());
                                board.setPullPosition(new Coords(-1, -1));
                                selectedFigure = null;  // deselect the figure
                            }
                            // select enemy figure for push
                            else if (board.getCurrentPlayer().getAvailableEnemyPieces().contains(board.getBoard()[7 - viewRow][viewCol])) {
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
                }
            });
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
            return icon;
        }

        public JButton[][] getChessBoardSquares() {
            return chessBoardSquares;
        }
    }
}
