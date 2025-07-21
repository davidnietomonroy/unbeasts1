package battle;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import main.GamePanel;

import java.io.IOException;

public class BattleManager {
    GamePanel gp;
    BufferedImage background, playerSprite, enemySprite;
    public String[] options = {"Atacar","Equipo", "Mochila", "Huir"};
    public int selectedOption = 0;

    public BattleManager(GamePanel gp) {
        this.gp = gp;
        loadImages();
    }

    private void loadImages() {
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/battle/background.png"));
            playerSprite = ImageIO.read(getClass().getResourceAsStream("/friends/sofia_back.png"));
            enemySprite = ImageIO.read(getClass().getResourceAsStream("/enemies/monstruo_front.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Aquí puedes añadir lógica de turnos, daños, etc.
    }

    public void draw(Graphics2D g2) {
        // Fondo
        g2.drawImage(background, 0, 0, gp.screenWidth, gp.screenHeight, null);

        // Tamaño de sprites
        int spriteW = gp.tileSize * 3;
        int spriteH = gp.tileSize * 3;

        // Coordenadas de sprites
        int playerX = gp.tileSize * 2;
        int playerY = gp.tileSize * 4;
        int enemyX = gp.tileSize * 10;
        int enemyY = gp.tileSize * 2;

        // Tamaño de sombra
        int shadowW = gp.tileSize * 3;       // más ancha
        int shadowH = gp.tileSize;           // más alta
        int offsetY = 30;                    // separa la sombra de la base

        g2.setColor(new Color(0, 0, 0, 80));  // sombra semi-transparente

        // Sombra del jugador (centrada bajo el sprite)
        int playerShadowX = playerX + (spriteW / 2) - (shadowW / 2);
        int playerShadowY = playerY + spriteH - offsetY;
        g2.fillOval(playerShadowX, playerShadowY, shadowW, shadowH);

        // Sombra del enemigo
        int enemyShadowX = enemyX + (spriteW / 2) - (shadowW / 2);
        int enemyShadowY = enemyY + spriteH - offsetY;
        g2.fillOval(enemyShadowX, enemyShadowY, shadowW, shadowH);

        // Dibujar sprites encima de las sombras
        g2.drawImage(playerSprite, playerX, playerY, spriteW, spriteH, null);
        g2.drawImage(enemySprite, enemyX, enemyY, spriteW, spriteH, null);

        // Opciones de batalla (cuadro de diálogo)
        int boxX = gp.tileSize * 2;
        int boxY = gp.tileSize * 9;
        int boxW = gp.screenWidth - gp.tileSize * 4;
        int boxH = gp.tileSize * 3;
        drawSubWindow(g2, boxX, boxY, boxW, boxH);

        // Texto de opciones
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        for (int i = 0; i < options.length; i++) {
            int textX = boxX + 30 + (i * 120);
            int textY = boxY + 40;

            if (i == selectedOption) {
                g2.setColor(Color.red);
            } else {
                g2.setColor(Color.white);
            }
            g2.drawString(options[i], textX, textY);
        }
    }

    private void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }
}
