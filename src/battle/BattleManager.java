package battle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import main.GamePanel;


public class BattleManager {
    GamePanel gp;
    BufferedImage background, playerSprite, enemySprite;

    public String[] options = {"Atacar", "Equipo", "Mochila", "Huir"};
    public String[] attackOptions = {"Espada", "Habilidad"};
    public int selectedOption = 0;

    public int menuState = 0;
    // 0 = principal, 1 = submenú atacar, 2 = habilidades

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
        // Aquí podrías manejar animaciones o lógica de tiempo real.
    }

    
    public void draw(Graphics2D g2) {
        // Fondo y sprites
        g2.drawImage(background, 0, 0, gp.screenWidth, gp.screenHeight, null);

        int spriteW = gp.tileSize * 3;
        int spriteH = gp.tileSize * 3;
        int playerX = gp.tileSize * 2;
        int playerY = gp.tileSize * 4;
        int enemyX = gp.tileSize * 10;
        int enemyY = gp.tileSize * 2;
        int shadowW = gp.tileSize * 3;
        int shadowH = gp.tileSize;
        int offsetY = 30;

        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillOval(playerX + (spriteW / 2) - (shadowW / 2), playerY + spriteH - offsetY, shadowW, shadowH);
        g2.fillOval(enemyX + (spriteW / 2) - (shadowW / 2), enemyY + spriteH - offsetY, shadowW, shadowH);

        g2.drawImage(playerSprite, playerX, playerY, spriteW, spriteH, null);
        g2.drawImage(enemySprite, enemyX, enemyY, spriteW, spriteH, null);

        switch (menuState) {
            case 0 -> drawMainMenu(g2);
            case 1 -> drawAttackSubMenu(g2);
      
        }
    }

    private void drawMainMenu(Graphics2D g2) {
        int boxX = gp.tileSize * 2;
        int boxY = gp.tileSize * 9;
        int boxW = gp.screenWidth - gp.tileSize * 4;
        int boxH = gp.tileSize * 3;
        drawSubWindow(g2, boxX, boxY, boxW, boxH);

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        for (int i = 0; i < options.length; i++) {
            int textX = boxX + 30 + (i * 120);
            int textY = boxY + 40;

            g2.setColor(i == selectedOption ? Color.red : Color.white);
            g2.drawString(options[i], textX, textY);
        }
    }

    private void drawAttackSubMenu(Graphics2D g2) {
        int boxX = gp.tileSize * 2;
        int boxY = gp.tileSize * 9;
        int boxW = gp.screenWidth - gp.tileSize * 4;
        int boxH = gp.tileSize * 3;
        drawSubWindow(g2, boxX, boxY, boxW, boxH);

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        for (int i = 0; i < attackOptions.length; i++) {
            int textX = boxX + 30 + (i * 150);
            int textY = boxY + 40;

            g2.setColor(i == selectedOption ? Color.red : Color.white);
            g2.drawString(attackOptions[i], textX, textY);
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
    
    public void retroceder() {
        if (menuState == 2) {
            menuState = 1;
        } else if (menuState == 1) {
            menuState = 0;
        }
    }
    
    public void moverSeleccionIzquierda() {
        if (menuState == 0) {
            selectedOption = (selectedOption + options.length - 1) % options.length;
        } else if (menuState == 1) {
            selectedOption = (selectedOption + attackOptions.length - 1) % attackOptions.length;
        }
    }

    public void moverSeleccionDerecha() {
        if (menuState == 0) {
            selectedOption = (selectedOption + 1) % options.length;
        } else if (menuState == 1) {
            selectedOption = (selectedOption + 1) % attackOptions.length;
        }
    }

    // Este método debería ser llamado al presionar Enter o confirmar una opción
    public void confirmarSeleccion() {
        System.out.println("confirmarSeleccion() llamado | menuState=" + menuState + ", selectedOption=" + selectedOption);

        if (menuState == 0) {
            switch (selectedOption) {
                case 0 -> { // Atacar
                    System.out.println("Entrando a submenú de ataque");
                    menuState = 1;
                    selectedOption = 0;
                }
                case 1 -> System.out.println("Equipo (por implementar)");
                case 2 -> System.out.println("Mochila (por implementar)");
                case 3 -> { // Huir
                    System.out.println("Huyendo de la batalla...");
                    gp.gameState = gp.playState;
                }
            }
        } else if (menuState == 1) {
            switch (selectedOption) {
                case 0 -> System.out.println("Atacando con espada (por implementar)");
                case 1 -> {
                    System.out.println("Mostrando habilidades desde base de datos");
                    menuState = 2;
                    selectedOption = 0;
                }
            }
        } else if (menuState == 2) {
            System.out.println("Aquí podrías seleccionar una habilidad y usarla");
            // Si quieres volver atrás, podrías hacer que Enter también regrese
            menuState = 1;
        }
    }
}

