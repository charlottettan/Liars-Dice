package org.cis1200.LiarsDice;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Dice extends JPanel {
    public final int MAX_DICE = 5;
    private ArrayList<Die> playerDice;
    private Integer[] totalCounts = {0, 0, 0, 0, 0, 0};
    private boolean won = false;

    public Dice() {
        playerDice = new ArrayList<>();
        initialRoll();

        setLayout(new GridLayout(1, 5, 6, 6));
        for (int i = 0; i < playerDice.size(); i++) {
            add(playerDice.get(i).getDieButton());
        }
    }

    // Initially adding the 5 dice in
    public void initialRoll() {
        for (int i = 0; i < MAX_DICE; i++) {
            int number = (int) (Math.random() * 6) + 1;
            Die roll = new Die(number);
            totalCounts[number - 1] ++;
            playerDice.add(roll);
        }
    }

    // calling reroll after one player has won the round
    public void roll() {
        for (int i = 0; i < totalCounts.length; i++) {
            totalCounts[i] = 0;
        }

        for (int i = 0; i < playerDice.size(); i++) {
            int number = (int) (Math.random() * 6) + 1;
            totalCounts[number - 1] ++;
            playerDice.get(i).resetDice(number);
        }
    }

    public ArrayList getPlayerDice() {
        return playerDice;
    }

    // Lose dice after you win
    public void loseDice() {
        if (!playerDice.isEmpty()) {
            Die removed = playerDice.remove(0); // store removed die
            remove(removed.getDieButton()); // get button of removed die
        }

        // updating layout
        setLayout(new GridLayout(1, playerDice.size(), 6, 6));
        removeAll(); // remove everything from panel
        for (int i = 0; i < playerDice.size(); i++) {
            add(playerDice.get(i).getDieButton());
        }

        updateUI();
    }

    public void resetDice() {
        for (int i = 0; i < totalCounts.length; i++) {
            totalCounts[i] = 0;
        }

        if (won) {
            won = false;
            playerDice = new ArrayList<>();
            setLayout(new GridLayout(1, 5, 6, 6));
            removeAll();
            initialRoll();
            for (int i = 0; i < playerDice.size(); i++) {
                add(playerDice.get(i).getDieButton());
            }

        } else {
            for (int i = 0; i < playerDice.size(); i++) {
                int number = (int) (Math.random() * 6) + 1;
                totalCounts[number - 1] ++;
                playerDice.get(i).resetDice(number);
            }

            if (playerDice.size() != 5) {
                for (int i = 0; i < MAX_DICE - playerDice.size(); i++) {
                    int number = (int) (Math.random() * 6) + 1;
                    totalCounts[number - 1] ++;
                    Die roll = new Die(number);
                    playerDice.add(roll);
                    add(roll.getDieButton());
                }
            }
        }
        updateUI();
    }

    public void checkWin() {
        if (playerDice.size() == 0) {
            won = true;
            setLayout(new BorderLayout());
            JLabel message = new JLabel("Player has won! Click reset to continue playing", 0);
            add(message);
            updateUI();
        }
    }

    public ArrayList<Integer> getRolls() {
        ArrayList list = new ArrayList();
        for (Die curr : playerDice) {
            list.add(curr.getNumber());
        }
        return list;
    }

    public int getDice() {
        return playerDice.size();
    }

    public Integer[] getTotalCounts() { return totalCounts; }

    public boolean getWon() { return won; }
}
