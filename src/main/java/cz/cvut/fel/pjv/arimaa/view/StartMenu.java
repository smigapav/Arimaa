package cz.cvut.fel.pjv.arimaa.view;

import cz.cvut.fel.pjv.arimaa.model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StartMenu extends JPanel {
    JFrame frame;
    public StartMenu() {
        frame = new JFrame("Menu");
        frame.setPreferredSize(new Dimension(400, 300));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(this);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        addButtons(gbc);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void addButtons(GridBagConstraints gbc) {
        JButton button1 = new JButton("Play against another player");
        JButton button2 = new JButton("Play against bot");
        JButton button3 = new JButton("Load game");

        button1.setPreferredSize(new Dimension(200, 50));
        button2.setPreferredSize(new Dimension(200, 50));
        button3.setPreferredSize(new Dimension(200, 50));

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(button1, gbc);


        gbc.gridy = 1;
        add(button2, gbc);

        gbc.gridy = 2;
        add(button3, gbc);

        addListenersToButtons(button1, button2, button3);
    }

    private void addListenersToButtons(JButton button1, JButton button2, JButton button3) {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Board board = new Board(true, false);
                Table table = new Table(board);
                frame.setVisible(false);
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Board board = new Board(true, true);
                Table table = new Table(board);
                frame.setVisible(false);
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> file = Board.GameLoader.readFileRows();
                Board board = new Board(file);
                Table table = new Table(board);
                frame.setVisible(false);
            }
        });
    }
}
