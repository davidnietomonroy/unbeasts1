package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import battle.Move;
import main.GamePanel;
import main.UtilityTool;
import types.Type;

public class Entity {
    public GamePanel gp;
    public int worldX, worldY;
    public int speed;
    public String direction = "down";
    public String name;
    public boolean collision = false;
    public String[] dialogueOptions;
    public String[] responseYes;
    public String[] responseNo;

    // Sistema de tipos
    public Type type = Type.NORMAL;

    // Imágenes y animación
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage image;
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // Colisiones
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    // Diálogos
    public String dialogues[] = new String[20];
    public int dialogueIndex = 0;
    public int actionLockCounter = 0;

    // Atributos
    public int vidaMaxima = 100;
    public int vida = vidaMaxima;
    public boolean isInParty = false;
    public Rectangle visionRange = new Rectangle(0, 0, 48, 48);
    public boolean playerDetected = false;
    public String[] types = new String[2]; 
    public Move[] moves = new Move[4]; // Máximo 4 ataques por entidad


    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public void setAction() {} // Override en subclases

    public void speak() {
        if (isInParty) {
            gp.ui.currentDialogue = name + ": Ya estamos en esto, ¿recuerdas?";
            gp.gameState = gp.dialogueState;
            return;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        gp.gameState = gp.dialogueState;
    }

    public void update() {
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkPlayer(this);

        if (!collisionOn) {
            switch (direction) {
                case "up" -> worldY -= speed;
                case "down" -> worldY += speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            BufferedImage imageToDraw = image;

            if (up1 != null) {
                switch (direction) {
                    case "up" -> imageToDraw = (spriteNum == 1) ? up1 : up2;
                    case "down" -> imageToDraw = (spriteNum == 1) ? down1 : down2;
                    case "left" -> imageToDraw = (spriteNum == 1) ? left1 : left2;
                    case "right" -> imageToDraw = (spriteNum == 1) ? right1 : right2;
                }
            }

            g2.drawImage(imageToDraw, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    public BufferedImage setup(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
