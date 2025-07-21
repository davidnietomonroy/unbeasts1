package entity;

import java.awt.Rectangle;
import main.GamePanel;

public class Monster1 extends Entity {

    public Rectangle visionRange;
    public boolean playerDetected = false;
    public boolean movingToPlayer = false;
    public int approachSpeed = 6;

    public Monster1(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 2;
        getImage();

        vidaMaxima = 100;
        vida = vidaMaxima;

        // Inicializa el rango con algún valor inicial
        visionRange = new Rectangle();
    }

    public void getImage() {
        up1 = setup("/enemies/monstruo_up");
        up2 = setup("/enemies/monstruo_up");
        down1 = setup("/enemies/monstruo_down");
        down2 = setup("/enemies/monstruo_down");
        left1 = setup("/enemies/monstruo_left");
        left2 = setup("/enemies/monstruo_left");
        right1 = setup("/enemies/monstruo_right");
        right2 = setup("/enemies/monstruo_right");
    }

    public String getDirectionToPlayer() {
        int dx = gp.player.worldX - this.worldX;
        int dy = gp.player.worldY - this.worldY;

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? "right" : "left";
        } else {
            return dy > 0 ? "down" : "up";
        }
    }

    public void startApproachPlayer() {
        gp.playerFrozen = true; // Congela al jugador
        movingToPlayer = true;
        speed = approachSpeed;
        direction = getDirectionToPlayer();
    }

    public void moveTowardsPlayer() {
        int dx = gp.player.worldX - worldX;
        int dy = gp.player.worldY - worldY;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                direction = "right";
                worldX += speed;
            } else {
                direction = "left";
                worldX -= speed;
            }
        } else {
            if (dy > 0) {
                direction = "down";
                worldY += speed;
            } else {
                direction = "up";
                worldY -= speed;
            }
        }

        // Verifica colisión con el jugador
        Rectangle monsterArea = new Rectangle(worldX, worldY, gp.tileSize, gp.tileSize);
        Rectangle playerArea = new Rectangle(gp.player.worldX, gp.player.worldY, gp.tileSize, gp.tileSize);

        if (monsterArea.intersects(playerArea)) {
            movingToPlayer = false;
            gp.playerFrozen = false;
            gp.gameState = gp.battleState;
        }
    }

    @Override
    public void update() {
        int tile = gp.tileSize;
        int rangeTiles = 5;

        // Campo de visión dinámico según dirección actual
        switch (direction) {
            case "up" -> visionRange.setBounds(worldX, worldY - rangeTiles * tile, tile, rangeTiles * tile);
            case "down" -> visionRange.setBounds(worldX, worldY + tile, tile, rangeTiles * tile);
            case "left" -> visionRange.setBounds(worldX - rangeTiles * tile, worldY, rangeTiles * tile, tile);
            case "right" -> visionRange.setBounds(worldX + tile, worldY, rangeTiles * tile, tile);
        }

        Rectangle playerArea = new Rectangle(gp.player.worldX, gp.player.worldY, tile, tile);

        if (!playerDetected && visionRange.intersects(playerArea)) {
            playerDetected = true;
            startApproachPlayer();
        }

        if (movingToPlayer) {
            moveTowardsPlayer();
        }
    }

    @Override
    public void speak() {
        super.speak();
    }
}

