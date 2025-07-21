
package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import entity.Entity;
import object.SuperObject;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    public String currentDialogue = "";
    public int commandNum = 0;
    public String notificationText = "";
    int notificationCounter = 0;
    public final int notificationDuration = 180;
    public boolean inventoryOpen = false;
    public int currentTab = 0;

    public int slotCol = 0;
    public int slotRow = 0;
    public final int INVENTORY_COLS = 7;
    public int INVENTORY_ROWS; // Ya no es final
    public int MAX_INVENTORY_SIZE; // Ya no es final

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        // --- Cálculo dinámico de filas del inventario ---
        final int slotSize = gp.tileSize + 8;
        final int slotSpacing = 10;
        final int totalSlotHeight = slotSize + slotSpacing;
        final int invFrameHeight = (int)(gp.screenHeight * 0.75) - gp.tileSize;
        
        // El '- 40' representa un padding vertical (ej. 20 arriba y 20 abajo)
        this.INVENTORY_ROWS = (invFrameHeight - 40) / totalSlotHeight;
        this.MAX_INVENTORY_SIZE = INVENTORY_ROWS * INVENTORY_COLS;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        } else if (gp.gameState == gp.playState || gp.gameState == gp.pauseState) {
            if (inventoryOpen) {
                drawFullScreenInventory();
            }
        }

        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }

        if (notificationCounter > 0 && notificationText != null && !notificationText.equals("")) {
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            int textWidth = g2.getFontMetrics().stringWidth(notificationText);
            int x = gp.screenWidth / 2 - textWidth / 2;
            int y = gp.tileSize * 2;

            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRoundRect(x - 20, y - 30, textWidth + 40, 40, 20, 20);

            g2.setColor(Color.white);
            g2.drawString(notificationText, x, y);

            notificationCounter--;
            if (notificationCounter <= 0) notificationText = "";
        }
    }

    public void drawFullScreenInventory() {
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.screenWidth - (gp.tileSize * 2);

        final int invFrameHeight = (int)(gp.screenHeight * 0.75) - gp.tileSize;
        final int descFrameY = frameY + invFrameHeight + 10;
        final int descFrameHeight = gp.screenHeight - descFrameY - gp.tileSize;

        drawSubWindow(frameX, frameY, frameWidth, invFrameHeight);
        drawSubWindow(frameX, descFrameY, frameWidth, descFrameHeight);
        drawInventoryTabs(frameX, frameY);

        Entity character = (currentTab == 0) ? gp.player : gp.player.partyMembers.get(currentTab - 1);
        List<SuperObject> items = character.inventory;

        final int slotStartX = frameX + 20;
        final int slotStartY = frameY + 20;
        final int slotSize = gp.tileSize + 8;
        final int slotSpacing = 10;
        int slotX = slotStartX;
        int slotY = slotStartY;

        for (int i = 0; i < MAX_INVENTORY_SIZE; i++) {
            g2.setColor(new Color(255, 255, 255, 60));
            g2.setStroke(new BasicStroke(2));
            g2.fillRoundRect(slotX, slotY, slotSize, slotSize, 10, 10);

            if (i < items.size()) {
                g2.drawImage(items.get(i).image, slotX + 4, slotY + 4, gp.tileSize, gp.tileSize, null);
            }

            slotX += slotSize + slotSpacing;
            if ((i + 1) % INVENTORY_COLS == 0) {
                slotX = slotStartX;
                slotY += slotSize + slotSpacing;
            }
        }

        int cursorX = slotStartX + (slotCol * (slotSize + slotSpacing));
        int cursorY = slotStartY + (slotRow * (slotSize + slotSpacing));
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, slotSize, slotSize, 10, 10);

        int itemIndex = slotCol + (slotRow * INVENTORY_COLS);
        if (itemIndex < items.size()) {
            SuperObject selectedItem = items.get(itemIndex);

            int textX = frameX + 20;
            int textY = descFrameY + gp.tileSize - 10;

            g2.setFont(arial_40.deriveFont(28F));
            g2.setColor(Color.orange);
            g2.drawString(selectedItem.name, textX, textY);

            g2.setFont(arial_40.deriveFont(Font.PLAIN, 20F));
            g2.setColor(Color.white);
            textY += 35;

            String description = selectedItem.description != null ? selectedItem.description : "Sin descripción.";
            drawWrappedText(description, textX, textY, frameWidth - 40, g2);
        }
    }

    public void drawInventoryTabs(int frameX, int frameY) {
        int tabWidth = 120;
        int tabHeight = 35;
        int tabSpacing = 5;
        int baseY = frameY - tabHeight > 0 ? frameY - tabHeight : frameY;

        int totalTabs = 1 + gp.player.partyMembers.size();

        for (int i = 0; i < totalTabs; i++) {
            int x = frameX + i * (tabWidth + tabSpacing);
            Color tabColor = (i == currentTab) ? new Color(70, 70, 70, 240) : new Color(30, 30, 30, 210);
            g2.setColor(tabColor);
            g2.fillRoundRect(x, baseY, tabWidth, tabHeight, 15, 15);

            String name = (i == 0) ? gp.player.name : gp.player.partyMembers.get(i - 1).name;
            g2.setColor(Color.white);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + (tabWidth - fm.stringWidth(name)) / 2;
            g2.drawString(name, textX, baseY + 22);

            if (i == currentTab) {
                g2.setColor(Color.white);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(x, baseY, tabWidth, tabHeight, 15, 15);
            }
        }
    }

    public void drawTitleScreen() {
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "unbeasts";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        g2.setColor(Color.gray);
        g2.drawString(text, x + 5, y + 5);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        x = gp.screenWidth / 2 - gp.tileSize;
        y += gp.tileSize * 2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 36F));
        y += gp.tileSize * 2 + 60;

        String[] options = {"NEW GAME", "LOAD GAME", "QUIT"};
        for (int i = 0; i < options.length; i++) {
            text = options[i];
            x = getXforCenteredText(text);
            g2.drawString(text, x, y);
            if (commandNum == i) g2.drawString(">", x - gp.tileSize, y);
            y += gp.tileSize;
        }
    }

    public void drawPauseScreen() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen() {
        // --- Dimensiones y fondo de la ventana ---
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        // --- Estilo y posición del texto ---
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        int textX = x + gp.tileSize - 30;
        int textY = y + gp.tileSize;

        // --- Dibujar el diálogo principal con sombra ---
        for (String line : currentDialogue.split("\n")) {
            
            // Texto principal (en blanco)
            g2.setColor(Color.white);
            g2.drawString(line, textX, textY);
            
            textY += 40;
        }

        // --- Dibujar opciones del diálogo con sombra ---
        Entity npc = gp.npc[gp.currentMap][gp.currentNPCIndex];
        if (npc != null && npc.dialogueOptions != null) {
            int optionY = y + height - 60; // Posición Y para las opciones
            
            for (int i = 0; i < npc.dialogueOptions.length; i++) {
                // Aumentamos el espaciado para que no se solapen
                int optionX = textX + (i * 80); 
                String optionText = npc.dialogueOptions[i];
                
               
                
                // Texto de la opción (amarillo si está seleccionada, si no, blanco)
                if (commandNum == i) {
                    g2.setColor(Color.yellow);
                    g2.drawString(">", optionX - 20, optionY); // Indicador de selección
                } else {
                    g2.setColor(Color.white);
                }
                g2.drawString(optionText, optionX, optionY);
            }
        }
    }

    public void showNotification(String text) {
        notificationText = text;
        notificationCounter = notificationDuration;
    }

    public Entity getPartyMember(int index) {
        int count = 0;
        for (Entity npc : gp.npc[gp.currentMap]) {
            if (npc != null && npc.isInParty) {
                if (count == index) {
                    return npc;
                }
                count++;
            }
        }
        return null;
    }

    private void drawWrappedText(String text, int x, int y, int maxWidth, Graphics2D g2) {
        FontMetrics fm = g2.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder(words[0]);

        for (int i = 1; i < words.length; i++) {
            if (fm.stringWidth(currentLine + " " + words[i]) < maxWidth) {
                currentLine.append(" ").append(words[i]);
            } else {
                g2.drawString(currentLine.toString(), x, y);
                y += fm.getHeight();
                currentLine = new StringBuilder(words[i]);
            }
        }
        if (currentLine.length() > 0) {
            g2.drawString(currentLine.toString(), x, y);
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(30, 30, 30, 230); // Gris más oscuro
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(220, 220, 220, 80); // borde blanco tenue (ahora gris más claro)
        g2.setColor(c);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }
    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}

