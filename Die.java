package org.cis1200.LiarsDice;

import javax.swing.*;
import java.awt.*;

public class Die {
    private int number;
    private JButton dieButton;

    public Die(int number) {
        this.number = number;

        // creating and scaling the image
        ImageIcon dice = new ImageIcon("files/dice" + number + ".png");
        Image large = dice.getImage();

        Image scale = large.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledDice = new ImageIcon(scale);
        JButton button = new JButton(scaledDice);

        this.dieButton = button;
    }

    public void resetDice(int newNumber) {
        this.number = newNumber;
        ImageIcon dice = new ImageIcon("files/dice" + number + ".png");
        Image large = dice.getImage();

        Image scale = large.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledDice = new ImageIcon(scale);

        dieButton.setIcon(scaledDice);
    }

    public JButton getDieButton() {
        return this.dieButton;
    }

    public int getNumber() { return number; }
}
