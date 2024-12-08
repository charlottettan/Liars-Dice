package org.cis1200.LiarsDice;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel {
    // Betting Board
    private boolean[][] validBets;
    private JButton[][] buttons;
    private Integer[] totalCounts = {0, 0, 0, 0, 0, 0};

    // Player and bet numbers
    private boolean player1 = true;
    private JLabel playerNumber = new JLabel("Player 1 is Playing", 0);
    private int numTurns = 0;
    private JLabel turns = new JLabel(numTurns + " bets so far", 0);

    // current bet [total of number, number], ex: [3, 6] = 3 dice with sixes
    private Integer[] currentBet = {0, 0};
    private boolean challengePass = false;
    private JPanel textPanel = new JPanel(new GridLayout(1, 2, 6, 6));

    // Challenge button
    private JButton challenge = new JButton("Challenge");
    private JPanel challengePanel = new JPanel();

    // Players
    private Dice p1 = new Dice();
    private Dice p2 = new Dice();
    private JLabel player1Label = new JLabel("Player 1");
    private JLabel player2Label = new JLabel("Player 2");

    // Scores/number of die remaining
    private JPanel dieRemaining = new JPanel(new GridLayout(1, 3, 6, 6));
    private JLabel p1DieRemain = new JLabel("Player 1: " + 5 + " Dice", 0);
    private JLabel p2DieRemain = new JLabel("Player 2: " + 5 + " Dice", 0);

    public Board() {
        // initializing sizes for betting board
        validBets = new boolean[12][6];
        buttons = new JButton[12][6];

        // adding labels for which player and how many bets have passed
        playerNumber.setBorder(new LineBorder(Color.BLACK));
        turns.setBorder(new LineBorder(Color.BLACK));
        textPanel.add(playerNumber);
        textPanel.add(turns);

        // adding labels for the number of dice per player
        JLabel remaining = new JLabel("  Die Remaining:", 2);
        dieRemaining.add(remaining);
        dieRemaining.add(p1DieRemain);
        dieRemaining.add(p2DieRemain);
        dieRemaining.setBorder(new LineBorder(Color.BLACK));

        // adding a button to challenge bets
        challengePanel.add(challenge);
        challenge.setEnabled(numTurns != 0);
        updateStates();
        recountDice();

        // start with player 1, only can see their dice
        player1Label.setVisible(true);
        player2Label.setVisible(false);
        p1.setVisible(true);
        p2.setVisible(false);

        // creating this layout for this component of the game
        setLayout(new GridLayout(12, 6, 6, 6));

        // Add the buttons.
        for (int i = 1; i < 13; i++) {
            for (int j = 1; j < 7; j++) {
                String number = "" + i;
                int total = i;
                int die = j;

                // creating and scaling the image
                ImageIcon dice = new ImageIcon("files/dice" + j + ".png");
                Image large = dice.getImage();

                Image scale = large.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                ImageIcon scaledDice = new ImageIcon(scale);

                // creating the button and setting text on left
                JButton button = new JButton(number, scaledDice);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        currentBet[0] = total;
                        currentBet[1] = die;
                        validBets[total - 1][die - 1] = false;
                        for (int m = 0; m < (total - 1); m++) {
                            for (int n = 0; n < 6; n++) {
                                validBets[m][n] = false;
                            }
                        }
                        for (int l = 0; l < die - 1; l++) {
                            validBets[total - 1][l] = false;
                        }

                        // update player and number of total bets
                        setPlayer();
                        numTurns++;
                        turns.setText(numTurns + " bets so far");

                        repaintButtons();
                        challenge.setEnabled(numTurns != 0);
                    }
                });
                button.setHorizontalTextPosition(SwingConstants.LEFT);
                buttons[total - 1][die - 1] = button;
                add(button);
            }
        }
    }

    public void updateStates() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 6; j++) {
                validBets[i][j] = true;
            }
        }
    }

    public void recountDice() {
        Integer[] p1Counts = p1.getTotalCounts();
        Integer[] p2Counts = p2.getTotalCounts();

        for (int i = 0; i < 6; i++) {
            totalCounts[i] = p1Counts[i] + p2Counts[i];
        }
    }

    public void repaintButtons() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 6; j++) {
                buttons[i][j].setEnabled(validBets[i][j]);
            }
        }
    }

    public void resetBoard() {
        updateStates();
        numTurns = 0;
        turns.setText(numTurns + " bets so far");
        player1 = true;
        playerNumber.setText("Player 1 is Playing");
        challenge.setEnabled(false);
        repaintButtons();
    }

    public boolean isChallengePass() {
        // start off so the challenge doesn't pass
        boolean pass = false;
        recountDice();
        if (currentBet[0] != 0 && currentBet[1] != 0) {
            int actual = totalCounts[currentBet[1] - 1];

            // if actual value is less than the amount of dice on board, then true
            if (actual < currentBet[0]) {
                pass = true;
            }
        }
        p1.checkWin();
        p2.checkWin();

        return pass;
    }

    public JPanel getTextPanel() {
        return textPanel;
    }

    public JPanel getChallengePanel() {return challengePanel; }

    public boolean getChallengePass() {
        return challengePass;
    }

    public JButton getChallenge() {
        return challenge;
    }

    public boolean getPlayer1() { return player1; }

    public Dice getP1() { return p1; }

    public Dice getP2() { return p2; }

    public void setPlayer() {
        player1 = !player1;
        if (player1) {
            playerNumber.setText("Player 1 is Playing");
            p1.setVisible(true);
            player1Label.setVisible(true);
            player2Label.setVisible(false);
            p2.setVisible(false);
        } else {
            playerNumber.setText("Player 2 is Playing");
            p2.setVisible(true);
            player2Label.setVisible(true);
            player1Label.setVisible(false);
            p1.setVisible(false);
        }
    }

    public void updateDieRemaining() {
        p1DieRemain.setText("Player 1: " + p1.getDice() + " Dice");
        p2DieRemain.setText("Player 2: " + p2.getDice() + " Dice");
    }

    public JPanel getDieRemaining() {
        return dieRemaining;
    }

    public JLabel getPlayer1Label() { return player1Label; }

    public JLabel getPlayer2Label() { return player2Label; }
}
