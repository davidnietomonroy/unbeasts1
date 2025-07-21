package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import entity.Entity;

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
    public void showNotification(String text) {
        notificationText = text;
        notificationCounter = notificationDuration;
    }

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        } else if (gp.gameState == gp.playState || gp.gameState == gp.pauseState) {
            if (inventoryOpen) {
                drawInventoryTabs();
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
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25));
        x += gp.tileSize - 30;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }

        Entity npc = gp.npc[gp.currentMap][gp.currentNPCIndex];
        if (npc != null && npc.dialogueOptions != null) {
            for (int i = 0; i < npc.dialogueOptions.length; i++) {
                int tx = x + 30 + (i * 100);
                int ty = y + height - 120;
                g2.setColor(commandNum == i ? Color.red : Color.white);
                g2.drawString(npc.dialogueOptions[i], tx, ty);
            }
        }
    }

    public void drawInventoryTabs() {
        int tabWidth = 120;
        int tabHeight = 40;
        int tabSpacing = 10;
        int baseX = 20;
        int baseY = 20;

        int totalTabs = 1 + gp.player.partyMembers.size();

        for (int i = 0; i < totalTabs; i++) {
            int x = baseX + i * (tabWidth + tabSpacing);
            Color tabColor = (i == currentTab) ? new Color(255, 255, 255, 220) : new Color(0, 0, 0, 180);
            g2.setColor(tabColor);
            g2.fillRoundRect(x, baseY, tabWidth, tabHeight, 10, 10);
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, baseY, tabWidth, tabHeight, 10, 10);

            String name = (i == 0) ? "Jugador" : gp.player.partyMembers.get(i - 1).name;
            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            int textWidth = g2.getFontMetrics().stringWidth(name);
            g2.drawString(name, x + (tabWidth - textWidth) / 2, baseY + 25);
        }

        drawInventoryFor(currentTab);
    }

    private void drawInventoryFor(int tabIndex) {
        Entity character = (tabIndex == 0) ? gp.player : gp.player.partyMembers.get(tabIndex - 1);
        int x = gp.tileSize;
        int y = gp.tileSize * 3;

        drawSubWindow(x, y, gp.screenWidth - 2 * gp.tileSize, gp.screenHeight - 4 * gp.tileSize);

        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        g2.drawString("Inventario de: " + character.name, x + 40, y + 40);

        // Aquí podrías mostrar items, habilidades, stats, etc.
        // Ejemplo:
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.drawString("→ (Aquí va la información del personaje)", x + 40, y + 80);
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}

