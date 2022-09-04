//Tara Feeley
//CIS2168
//Lab_04


import java.util.*;
import java.awt.GridLayout;

import javax.swing.*;

public class Maze extends JFrame {

    public Maze() throws InterruptedException {

        MazeGridPanel panel = new MazeGridPanel(100,100);
        this.add(panel);
        this.setSize(800, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        Thread.sleep(1500);
        panel.solveMaze();

    }

    public static void main(String[] args) throws InterruptedException {
        new Maze();
    }
}
