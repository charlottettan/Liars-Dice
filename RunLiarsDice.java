package org.cis1200.LiarsDice;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunLiarsDice implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        JFrame frame = new JFrame("Liar's Dice");
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        Board board = new Board();
        Dice player1 = board.getP1();
        Dice player2 = board.getP2();
        JLabel player1Label = board.getPlayer1Label();
        JLabel player2Label = board.getPlayer2Label();
        JPanel textPanel = board.getTextPanel();
        JButton reset = new JButton("reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textPanel.setVisible(true);
                board.setVisible(true);
                player1.resetDice();
                player2.resetDice();
                player1.setVisible(true);
                player1Label.setVisible(true);
                player2Label.setVisible(false);
                player2.setVisible(false);
                board.resetBoard();
                board.updateDieRemaining();
            }
        });
        JButton challenge = board.getChallenge();
        challenge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // On click, check if the challenge is correct/incorrect
                boolean pass = board.isChallengePass();
                // this should be the player who pressed challenged
                boolean playerNum = board.getPlayer1();
                if (pass) {
                    if (playerNum) {
                        player1.loseDice();
                    } else {
                        player2.loseDice();
                    }
                } else {
                    if (playerNum) {
                        player2.loseDice();
                    } else {
                        player1.loseDice();
                    }
                }
                player1.roll();
                player2.roll();
                board.resetBoard();
                board.recountDice();
                player1.checkWin();
                player2.checkWin();
                player1.setVisible(true);
                player1Label.setVisible(true);
                player2Label.setVisible(false);
                player2.setVisible(false);

                if (player1.getWon() || player2.getWon()) {
                    board.setVisible(false);
                    textPanel.setVisible(false);
                    player2.setVisible(true);
                    player2Label.setVisible(true);
                    board.updateUI();
                }
                board.updateDieRemaining();
            }
        });

        panel.add(reset);
        panel.add(board.getDieRemaining());
        panel.add(player1Label);
        panel.add(player1);
        panel.add(player2Label);
        panel.add(player2);
        panel.add(textPanel);
        panel.add(board.getChallengePanel());
        panel.add(board);

        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}