package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import main.GamePanel;
import main.KeyHandler;
import object.SuperObject;
import types.Typechart;

public class Player extends Entity {

    KeyHandler keyH;
    
    public final int screenX;
    public final int screenY;
    int hasKey = 0;
    public List<Entity> partyMembers = new ArrayList<>();
    
    public List<SuperObject> inventory = new ArrayList<>(); 
    
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;
        
        types[0] = Typechart.POISON;
        types[1] = Typechart.FLYING;
        name = "sofia";

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(13, 20, 20, 25);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 1;
        worldY = gp.tileSize * 2;
        speed = 6;
        direction = "down";
        vidaMaxima = 100;
        vida = vidaMaxima;
        maxInventorySize = 50; 
    }

    public void getPlayerImage() {
        up1 = setup("/player/sofia_up_1");
        up2 = setup("/player/sofia_up_2");
        down1 = setup("/player/sofia_down_1");
        down2 = setup("/player/sofia_down_2");
        left1 = setup("/player/sofia_left_1");
        left2 = setup("/player/sofia_left_2");
        right1 = setup("/player/sofia_right_1");
        right2 = setup("/player/sofia_right_2");
    }

    public void update() {
    	if (gp.playerFrozen) return;
        
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {
            if (keyH.upPressed) direction = "up";
            else if (keyH.downPressed) direction = "down";
            else if (keyH.leftPressed) direction = "left";
            else if (keyH.rightPressed) direction = "right";
            
            collisionOn = false;
            gp.cChecker.checkTile(this);
            
            // Esta llamada es la que causa el error si CollisionChecker no está actualizado.
            int objIndex = gp.cChecker.checkObject(this, true); 
            pickUpObject(objIndex);
            
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            if (!collisionOn && !keyH.enterPressed) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }
            
            gp.keyH.enterPressed = false;

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            if (gp.currentMap >= 0 && gp.currentMap < gp.superobj.length &&
                i >= 0 && i < gp.superobj[gp.currentMap].length) {

                SuperObject pickedObject = gp.superobj[gp.currentMap][i];

                if (pickedObject != null) {
                    switch (pickedObject.name) {
                        case "key":
                            hasKey++;
                            gp.superobj[gp.currentMap][i] = null;
                            gp.ui.showNotification("¡Has encontrado una llave!");
                            break;
                        case "Door":
                            if (hasKey > 0) {
                                gp.superobj[gp.currentMap][i] = null;
                                hasKey--;
                                gp.ui.showNotification("Puerta abierta.");
                            } else {
                                gp.ui.showNotification("Necesitas una llave.");
                            }
                            break;
                        case "Boots":
                            speed += 2;
                            gp.superobj[gp.currentMap][i] = null;
                            gp.ui.showNotification("¡Más velocidad!");
                            break;
                        case "Espada":
                            inventory.add(pickedObject);
                            gp.ui.showNotification("¡Has recogido una " + pickedObject.name + "!");
                            gp.superobj[gp.currentMap][i] = null;
                            break;
                    }
                }
            }
        }
    }
    
    public void interactNPC(int i) {
    	if (i != 999 && gp.keyH.enterPressed) {
    		gp.currentNPCIndex = i;
    		gp.gameState = gp.dialogueState;
    		gp.npc[gp.currentMap][i].speak();
    	}
    }

    public void draw(Graphics2D g2) {
        switch (direction) {
            case "up": image = (spriteNum == 1) ? up1 : up2; break;
            case "down": image = (spriteNum == 1) ? down1 : down2; break;
            case "left": image = (spriteNum == 1) ? left1 : left2; break;
            case "right": image = (spriteNum == 1) ? right1 : right2; break;
            default: image = down1; break;
        }
        g2.drawImage(image, screenX, screenY, null);
    }
}