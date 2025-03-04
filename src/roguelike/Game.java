package roguelike;

import roguelike.entities.Creature;
import roguelike.ui.UserInterface;
import roguelike.ui.GamePanel;
import roguelike.world.World;
import roguelike.world.WorldBuilder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Game {

    private boolean isRunning;
    private int framesPerSecond = 60;
    private int timePerLoop = 1000000000 / framesPerSecond;
    private World world;
    private Creature player;
    private Map<String, Map<String, String>> creatureData;
    private Map<String, Map<String, String>> tileData;
    private Map<String, Map<String, String>> itemData;
    private int screenWidth;
    private int screenHeight;
    private Rectangle gameViewArea;
    private UserInterface ui;

    private static final int mapWidth = 100;
    private static final int mapHeight = 100;

    public Game(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        gameViewArea = new Rectangle(screenWidth, screenHeight - 5);
        ui = new UserInterface(screenWidth, screenHeight, new Rectangle(mapWidth, mapHeight));
        creatureData = loadData(Paths.get("src", "roguelike", "creatures.txt").toString());
        tileData = loadData(Paths.get("src", "roguelike", "tiles.txt").toString());
        itemData = loadData(Paths.get("src", "roguelike", "items.txt").toString());
        createWorld();
        ui.getGamePanel().setWorld(world);
    }

    public Map<String, Map<String, String>> loadData(String file) {
        Map<String, Map<String, String>> entityMap = new HashMap<>();
        String line = "";
        String[] attributeNames = new String[10];
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            line = br.readLine();
            if (line != null) {
                attributeNames = line.split(", ");
            }
            while ((line = br.readLine()) != null) {
                String[] data = line.split(", ");
                Map<String, String> entityData = new HashMap<>();
                for (int i = 0; i < attributeNames.length; i++) {
                    entityData.put(attributeNames[i], data[i]);
                }
                String name = data[1];
                entityMap.put(name, entityData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entityMap;
    }

    private void createWorld() {
        player = new Creature(creatureData.get("player"), 10, 10);
        world = new WorldBuilder(tileData, creatureData, mapWidth, mapHeight)
                .fill("wall")
                .createRandomWalkCave(12232, 10, 10, 6000)
                .populateWorld(10)
                .build();
        world.player = player;
        world.addEntity(player);
    }

    private void startMenu() {
        GamePanel panel = ui.getGamePanel();

        Graphics g = panel.getGraphics();
        if (g != null) {
            g.setFont(new Font("Monospaced", Font.PLAIN, 16));

            g.setColor(Color.black);
            g.fillRect(0, 0, panel.getWidth(), panel.getHeight());

            String[] art = {
                    "                                                                                                                    ",
                    "                                                                                                                    ",
                    "                                                                                                                    ",
                    "               :                    +.                                                                              ",
                    "              .@@@*                  @@%.                                                          .@@.             ",
                    "              @@@@*                   @@@@                         :@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%            ",
                    "             :@@@=                     @@@+                                                     .@@@@@@@*           ",
                    "             @@@*                      -@%       :@@=                                         .@@@@=                ",
                    "            =@@@        .@% =@@@@@@@@@@@@@@@@@@@@@@@@@-                                     -@@@-                   ",
                    "            @@@@@@@@@@@@@@@@* .                                                           *@@+                      ",
                    "           #@@-        +@@@:      @@:       .                                      .@.  @@+                         ",
                    "          :@@-        -@@.       @@@@@:      %@*                                   .@@@@.                           ",
                    "          @@-        .@%        @@@@           @@@=                                .@@@@#                           ",
                    "         @@.   @@+   @-        @@@-             :@@@@.                             .@@@-                *@          ",
                    "        @@     @@@@          .@@@            :    @@@@%                             @@@-              =@@@@#        ",
                    "       @*      @@@          #@@. *#          @@@@: *@@@      +@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@-      ",
                    "      ..       @@@        .@@.    @-        +@@@.   :@+                             @@@-                            ",
                    "               @@@       *@       +@        @@@:                                    @@@-                            ",
                    "               @@@                 @@      @@@%                                     @@@-                            ",
                    "               @@@                 :@@    :@@@                                      @@@-                            ",
                    "               @@@                  =@@  :@@@                                       @@@-                            ",
                    "               @@@        *#         #@@-@@@                                        @@@-                            ",
                    "               @@@     .@@:           @@@@@                                         @@@-                            ",
                    "               @@@   *@@-             @@@@@                                         @@@-                            ",
                    "               @@@:@@@+             %@@@-@@@@                                       @@@-                            ",
                    "               @@@@@%             @@@*    =@@@@:                                    @@@-                            ",
                    "              %@@@@.           +@@@:        *@@@@@=.                      .@@#*+=--#@@@-                            ",
                    "               .@#          *@@#.             =@@@@@@@@#                     -@@@@@@@@@                             ",
                    "                        :@@%.                    %@@@:                          .@@@@+                              ",
                    "                      .-                                                         :+                              "
            };

            int artStartY = 20;
            for (int i = 0; i < art.length; i++) {
                g.setColor(Color.white);
                int artX = (panel.getWidth() - g.getFontMetrics().stringWidth(art[i])) / 2;
                g.drawString(art[i], artX, artStartY + (i + 1) * 16);
            }
            String title = "A slartibarti game";
            int titleX = (panel.getWidth() - g.getFontMetrics().stringWidth(title)) / 2;
            g.drawString(title, titleX, artStartY + art.length * 16 + 20);
            String prompt = "Press any key to start";
            int promptX = (panel.getWidth() - g.getFontMetrics().stringWidth(prompt)) / 2;
            g.drawString(prompt, promptX, artStartY + art.length * 16 + 40);
        }

        while (ui.getNextInput() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void processInput(InputEvent event) {
        if (event instanceof KeyEvent) {
            KeyEvent keypress = (KeyEvent) event;
            switch (keypress.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    player.move(world, -1, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.move(world, 1, 0);
                    break;
                case KeyEvent.VK_UP:
                    player.move(world, 0, -1);
                    break;
                case KeyEvent.VK_DOWN:
                    player.move(world, 0, 1);
                    break;
            }
        } else if (event instanceof MouseEvent) {
        }
    }

    public void render() {
        ui.refresh();
    }

    public void update() {
        world.update();
    }

    public void run() {
        startMenu();
        isRunning = true;
        while (isRunning) {

            InputEvent event = null;
            while ((event = ui.getNextInput()) == null) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            processInput(event);
            update();
            render();


            if (player.isDead()) {
                isRunning = false;
            }
        }

        gameOverScreen();
    }

    private void gameOverScreen() {
        GamePanel panel = ui.getGamePanel();
        panel.setGameOver(true);
        ui.refresh();

        // Drain any existing input events.
        while (ui.getNextInput() != null) {
        }

        // Wait for a new input to exit.
        while (ui.getNextInput() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        Game game = new Game(120, 80);
        game.run();
    }

    public static Color stringToColor(String colorString) {
        Color color;
        try {
            java.lang.reflect.Field field = Color.class.getField(colorString);
            color = (Color) field.get(null);
        } catch (Exception e) {
            color = null;
        }
        return color;
    }
}