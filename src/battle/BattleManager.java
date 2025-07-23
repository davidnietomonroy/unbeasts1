package battle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import main.GamePanel;
import battle.Habilidad;
import battle.HabilidadDAO;
import entity.Entity;

public class BattleManager {
    GamePanel gp;
    BufferedImage background, playerSprite, enemySprite;
    private boolean turnoJugador = true;
    private List<String> mensajesBatalla = new ArrayList<>();

    public String[] options = {"Atacar", "Equipo", "Mochila", "Huir"};
    public String[] attackOptions = {"Espada", "Habilidad"};
    public int selectedOption = 0;
    public int selectedMainOption = 0;
    public int selectedSubOption = 0;

    public int menuState = 0;
    // 0 = principal, 1 = submenú atacar, 2 = habilidades

    private List<Habilidad> habilidades;
    
    public Entity enemigoActual;
    private Entity jugador;



    public BattleManager(GamePanel gp) {
        this.gp = gp;
        loadImages();
        enemigoActual = new Entity(gp);
        enemigoActual.name = "Monstruo";
        enemigoActual.vidaMaxima = 100;
        enemigoActual.vida = enemigoActual.vidaMaxima;
        
        jugador = new Entity(gp);
        jugador.name = "Sofía";
        jugador.vidaMaxima = 100;
        jugador.vida = jugador.vidaMaxima;

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
        
    }
  

    public void draw(Graphics2D g2) {
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
        
        int playerBarX = gp.tileSize;
        int playerBarY = gp.tileSize;
        int playerBarWidth = gp.tileSize * 4;
        int playerBarHeight = gp.tileSize / 2;

        float playerRatio = (float) jugador.vida / jugador.vidaMaxima;
        int currentPlayerWidth = (int) (playerRatio * playerBarWidth);
        
     // Vida jugador
        g2.setColor(Color.GRAY);
        g2.fillRect(playerBarX, playerBarY, playerBarWidth, playerBarHeight);
        g2.setColor(Color.GREEN);
        g2.fillRect(playerBarX, playerBarY, currentPlayerWidth, playerBarHeight);
        g2.setColor(Color.WHITE);
        g2.drawRect(playerBarX, playerBarY, playerBarWidth, playerBarHeight);
        
        
     // Vida del enemigo
        int enemyBarX = gp.tileSize * 9;
        int enemyBarY = gp.tileSize;
        int enemyBarWidth = gp.tileSize * 4;
        int enemyBarHeight = gp.tileSize / 2;

        float enemyRatio = (float) enemigoActual.vida / enemigoActual.vidaMaxima;
        int currentEnemyWidth = (int) (enemyRatio * enemyBarWidth);

        g2.setColor(Color.GRAY);
        g2.fillRect(enemyBarX, enemyBarY, enemyBarWidth, enemyBarHeight);
        g2.setColor(Color.RED);
        g2.fillRect(enemyBarX, enemyBarY, currentEnemyWidth, enemyBarHeight);
        g2.setColor(Color.WHITE);
        g2.drawRect(enemyBarX, enemyBarY, enemyBarWidth, enemyBarHeight);

        switch (menuState) {
            case 0 -> drawMainMenu(g2);
            case 1 -> drawAttackSubMenu(g2);
            case 2 -> drawHabilidadesDesdeBD(g2);
        }
        
        drawMensajesBatalla(g2);
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
    
    private void drawMensajesBatalla(Graphics2D g2) {
    	int x = gp.screenWidth - gp.tileSize * 10;
    	int y = gp.screenHeight - gp.tileSize * 6;
        int lineHeight = 30;

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24f));
        g2.setColor(Color.BLACK);

        // Solo mostrar los últimos 3 mensajes
        int start = Math.max(0, mensajesBatalla.size() - 3);
        for (int i = start; i < mensajesBatalla.size(); i++) {
            g2.drawString(mensajesBatalla.get(i), x, y);
            y += lineHeight;
        }
        if (mensajesBatalla.size() > 10) {
            mensajesBatalla.remove(0);
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

    public void drawHabilidadesDesdeBD(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        g2.setColor(Color.CYAN);
        g2.drawString("Selecciona una habilidad:", 50, 80);

        if (habilidades != null) {
            for (int i = 0; i < habilidades.size(); i++) {
                if (i == selectedSubOption) {
                    g2.setColor(Color.YELLOW);
                    g2.drawString("→ " + habilidades.get(i).getNombre(), 70, 120 + i * 30);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.drawString(habilidades.get(i).getNombre(), 90, 120 + i * 30);
                }
            }
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
        } else {
            // Si ya estás en el menú principal, puedes hacer que cancelar salga de la batalla o no haga nada.
        }
        selectedOption = 0; // Opcional: reiniciar selección
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
    
    public void moverSeleccionArriba() {
        if (menuState == 2 && habilidades != null && !habilidades.isEmpty()) {
            selectedSubOption = (selectedSubOption + habilidades.size() - 1) % habilidades.size();
        }
    }

    public void moverSeleccionAbajo() {
        if (menuState == 2 && habilidades != null && !habilidades.isEmpty()) {
            selectedSubOption = (selectedSubOption + 1) % habilidades.size();
        }
    }
    
    private void atacarConEspada() {
        Random rand = new Random();
        int danio = rand.nextInt(6) + 5; // 5 a 10

        if (enemigoActual != null) {
            enemigoActual.vida -= danio;
            if (enemigoActual.vida < 0) enemigoActual.vida = 0;

            mensajesBatalla.add("Sofía ataca con espada causando " + danio + " de daño.");
        }

        retroceder();
    }
    
    private void enemigoContraataca() {
        int daño = new Random().nextInt(11) + 10; // 10 a 20
        jugador.vida -= daño;
        if (jugador.vida < 0) jugador.vida = 0;

        mensajesBatalla.add(enemigoActual.name + " ataca causando " + daño + " de daño.");
    }


    public void confirmarSeleccion() {
        if (menuState == 0) {
            switch (selectedOption) {
                case 0 -> {
                    menuState = 1;
                    selectedOption = 0;
                }
                case 1 -> System.out.println("Equipo (por implementar)");
                case 2 -> System.out.println("Mochila (por implementar)");
                case 3 -> {
                    System.out.println("Huyendo de la batalla...");
                    gp.gameState = gp.playState;
                }
            }
        } else if (menuState == 1) {
            switch (selectedOption) {
                case 0 -> {
                    if (turnoJugador) {
                        atacarConEspada();
                        turnoJugador = false;
                        enemigoContraataca();
                        turnoJugador = true;
                    }
                    menuState = 0;
                }
                case 1 -> {
                    habilidades = HabilidadDAO.obtenerHabilidades();
                    if (habilidades != null && !habilidades.isEmpty()) {
                        menuState = 2;
                        selectedSubOption = 0;
                    } else {
                        System.out.println("No hay habilidades disponibles.");
                    }
                }
            }
        } else if (menuState == 2) {
            Habilidad habilidad = habilidades.get(selectedSubOption);
            mensajesBatalla.add("Has usado la habilidad: " + habilidad.getNombre());
            menuState = 0;
        }
    }
}
