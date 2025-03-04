package roguelike.ui;

import javax.swing.*;
import java.awt.*;
import roguelike.world.World;
import roguelike.entities.Creature;
import roguelike.entities.Tile;

public class GamePanel extends JPanel {

    private int columns;
    private int rows;
    private World world;
    private boolean gameOver = false;

    public GamePanel(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        setPreferredSize(new Dimension(columns * 16, rows * 16));
        setBackground(Color.black);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (world != null) {
            int offsetX = 0;
            int offsetY = 0;
            if (world.player != null) {
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                //
                offsetX = centerX - (world.player.getX() * 16);
                offsetY = centerY - ((world.player.getY() + 1) * 16);
            }

            // Draw the tiles
            for (int x = 0; x < world.width(); x++) {
                for (int y = 0; y < world.height(); y++) {
                    Tile tile = world.tile(x, y);
                    if (tile != null) {
                        g.setColor(tile.getColor());
                        String s = String.valueOf(tile.getGlyph());
                        g.drawString(s, x * 16 + offsetX, (y + 1) * 16 + offsetY);
                    }
                }
            }

            // Draw all creatures
            for (Creature creature : world.creatures) {
                g.setColor(creature.getColor());
                String s = String.valueOf(creature.getGlyph());
                g.drawString(s, creature.getX() * 16 + offsetX, (creature.getY() + 1) * 16 + offsetY);
            }
        }

        if (gameOver) {
            // Draw game over overlay on top of everything
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            String gameOverText = "GAME OVER";
            String message = "Press any key to exit.";
            FontMetrics metrics = g.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth(gameOverText)) / 2;
            int y = getHeight() / 2;
            g.setColor(Color.red);
            g.drawString(gameOverText, x, y);
            int xMessage = (getWidth() - metrics.stringWidth(message)) / 2;
            g.drawString(message, xMessage, y + metrics.getHeight());
        }
    }
}