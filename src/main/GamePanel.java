package main;

import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;
import battle.BattleManager;

public class GamePanel extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;

    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    public final int maxWorldCol = 170;
    public final int maxWorldRow = 170;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    public final int maxMap = 10;
    public int currentMap = 0;
    public EventHandler eHandler = new EventHandler(this);

    // GAME STATE
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int battleState = 4;
    public final int inventoryState = 5;	

    public int gameState;
    public int currentNPCIndex = -1;

    // GAME SYSTEMS
    final int FPS = 60;
    Thread gameThread;
    public UI ui = new UI(this);
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public BattleManager battleManager = new BattleManager(this);
    public MouseHandler mouseH = new MouseHandler(this);
    
    

    // ENTITIES
    public Player player = new Player(this, keyH);
    
    public Entity obj[][] = new Entity[maxMap][10]; // Objetos por mapa
    public SuperObject superobj[][] = new SuperObject[maxMap][10]; // Super Objetos
    public Entity npc[][] = new Entity[maxMap][10]; // NPCs por mapa

    // PLAYER SETTINGS
    public int playerX = 100;
    public int playerY = 100;
    public int playerSpeed = 4;
    public boolean playerFrozen = false;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.addMouseListener(mouseH);
    
        
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        if (gameState == playState) {
            player.update();
            eHandler.checkEvent();

            // NPC updates
            for (int i = 0; i < npc[currentMap].length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }

            // Object updates (si es necesario)
            for (int i = 0; i < obj[currentMap].length; i++) {
                if (obj[currentMap][i] != null) {
                    obj[currentMap][i].update();
                }
            }

            // SuperObject updates (si necesitas lógica)
            // for (int i = 0; i < superobj[currentMap].length; i++) {
            //     if (superobj[currentMap][i] != null) {
            //         superobj[currentMap][i].update(); // Si tienes lógica en ellos
            //     }
            // }
        } else if (gameState == battleState) {
            battleManager.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        if (gameState == titleState) {
            ui.draw(g2);
        } else if (gameState == battleState) {
            battleManager.draw(g2);
            g2.dispose();
            return;
        } else {
            // TILES
            tileM.draw(g2);

            // OBJECTS
            for (int i = 0; i < obj[currentMap].length; i++) {
                if (obj[currentMap][i] != null) {
                    obj[currentMap][i].draw(g2);
                }
            }

            // SUPER OBJECTS (como la espada)
            for (int i = 0; i < superobj[currentMap].length; i++) {
                if (superobj[currentMap][i] != null) {
                    superobj[currentMap][i].draw(g2, this);
                }
            }

            // NPCS
            for (int i = 0; i < npc[currentMap].length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].draw(g2);
                }
            }

            // PLAYER
            player.draw(g2);

            // UI
            ui.draw(g2);
        }

        if (keyH.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time : " + passed, 10, 400);
            System.out.println("Draw Time: " + passed);
        }

        g2.dispose();
    }
}

