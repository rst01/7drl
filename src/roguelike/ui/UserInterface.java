package roguelike.ui;

import roguelike.world.World;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

public class UserInterface extends JFrame implements KeyListener, MouseListener {

    private GamePanel gamePanel;  // Using GamePanel instead of AsciiPanel
    private Queue<InputEvent> inputQueue;
    private int screenWidth;
    private int screenHeight;

    public UserInterface(int screenWidth, int screenHeight, Rectangle mapDimensions) {
        super("Roguelike");
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        inputQueue = new LinkedList<>();

        gamePanel = new GamePanel(screenWidth, screenHeight);

        super.add(gamePanel);
        super.addKeyListener(this);
        super.addMouseListener(this);
        super.setSize(screenWidth * 16, screenHeight * 16);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
        super.repaint();
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public InputEvent getNextInput() {
        return inputQueue.poll();
    }

    public void refresh() {
        gamePanel.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        inputQueue.add(e);
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        inputQueue.add(e);
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
}