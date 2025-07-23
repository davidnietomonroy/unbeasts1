package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle; // Asegúrate de que este import existe
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import entity.Entity;
import object.SuperObject;
import Inventory.Inventory;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    public String currentDialogue = "";
    public int commandNum = 0;
    public String notificationText = "";
    int notificationCounter = 0;
    public final int notificationDuration = 180;

    // --- Variables de Estado del Inventario ---
    public boolean inventoryOpen = false;
    public int currentTab = 0;
    public int slotCol = 0;
    public int slotRow = 0;
    
    // --- Variables para el Menú y la Inspección ---
    public boolean contextMenuOpen = false;
    public boolean inspectingItem = false;
    public int contextMenuCommandNum = 0;
    private int contextMenuItemIndex = 0;
    public ArrayList<String> contextMenuOptions = new ArrayList<>();
    
    public Rectangle closeButtonRect; // Rectángulo para el botón de cerrar

    public final int INVENTORY_COLS = 7;
    public int INVENTORY_ROWS;
    public int MAX_INVENTORY_SIZE;

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        
        // ✅ ✅ ✅ LA CORRECCIÓN ESTÁ AQUÍ ✅ ✅ ✅
        // Inicializa el rectángulo para evitar el NullPointerException.
        this.closeButtonRect = new Rectangle();

        final int slotSize = gp.tileSize + 8;
        final int slotSpacing = 10;
        final int totalSlotHeight = slotSize + slotSpacing;
        final int invFrameHeight = (int)(gp.screenHeight * 0.75) - gp.tileSize;
        
        this.INVENTORY_ROWS = (invFrameHeight - 40) / totalSlotHeight;
        this.MAX_INVENTORY_SIZE = INVENTORY_ROWS * INVENTORY_COLS;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        } 
        else if (inventoryOpen) {
            if (inspectingItem) {
                drawItemDetailScreen();
            } else {
                drawFullScreenInventory();
            }
        }
        else if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
        else if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }
        
        // Dibuja notificaciones
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
        }
    }
    
    private void drawItemDetailScreen() {
        List<SuperObject> items = getItemsForCurrentTab();
        if (contextMenuItemIndex >= items.size()) {
            inspectingItem = false;
            return;
        }
        SuperObject item = items.get(contextMenuItemIndex);

        // Ventana de Detalle
        int frameX = gp.tileSize * 2;
        int frameY = gp.tileSize;
        int frameWidth = gp.screenWidth - (gp.tileSize * 4);
        int frameHeight = gp.screenHeight - (gp.tileSize * 3);
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        // Nombre del Ítem
        g2.setColor(Color.orange);
        g2.setFont(arial_40.deriveFont(32F));
        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        g2.drawString(item.name, textX, textY);
        
        // Descripción Detallada
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 22F));
        textY += 40;
        String detailedDesc = item.detailedDescription != null ? item.detailedDescription : "No hay más detalles.";
        drawWrappedText(detailedDesc, textX, textY, frameWidth - 40, g2);

        // Botón de Cerrar (X)
        int buttonSize = 32;
        int buttonX = frameX + frameWidth - buttonSize - 15;
        int buttonY = frameY + 15;
        
        // Actualiza el rectángulo para que el MouseHandler lo detecte
        closeButtonRect.setBounds(buttonX, buttonY, buttonSize, buttonSize);
        
        g2.setColor(new Color(150, 0, 0));
        g2.fillRoundRect(buttonX, buttonY, buttonSize, buttonSize, 10, 10);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
        g2.drawString("X", buttonX + 9, buttonY + 25);
    }
    
    // El resto de los métodos de la clase...
    // ... (copia el resto de tus métodos aquí, no necesitan cambios)
    public void openContextMenu() {
        this.contextMenuItemIndex = slotCol + (slotRow * INVENTORY_COLS);
        
        List<SuperObject> items = getItemsForCurrentTab();
        if (contextMenuItemIndex >= items.size()) return;

        contextMenuOptions.clear();
        contextMenuOptions.add("Inspeccionar");
        
        if (currentTab == 0 && !gp.player.partyMembers.isEmpty()) {
            for (Entity member : gp.player.partyMembers) {
                contextMenuOptions.add("Pasar a " + member.name);
            }
        }

        this.contextMenuOpen = true;
        this.contextMenuCommandNum = 0;
    }
    
    public void executeContextAction() {
        if (contextMenuOptions.isEmpty()) return;
        String selectedOption = contextMenuOptions.get(contextMenuCommandNum);

        if (selectedOption.equals("Inspeccionar")) {
            inspectingItem = true;
        } 
        else if (selectedOption.startsWith("Pasar a ")) {
            // Llama al nuevo método de transferencia
            transferItem();
        }
        
        contextMenuOpen = false; // Cierra el menú después de la acción
    }
    private void transferItem() {
        // 1. Obtener el ítem que se quiere transferir desde el inventario del jugador
        List<SuperObject> playerItems = gp.player.playerInventory.getItems();
        if (contextMenuItemIndex >= playerItems.size()) {
            return; // Seguridad por si el ítem ya no existe
        }
        SuperObject itemToTransfer = playerItems.get(contextMenuItemIndex);

        // 2. Encontrar el NPC de destino por su nombre
        String targetName = contextMenuOptions.get(contextMenuCommandNum).substring(8); // Extrae "Darwin" de "Pasar a Darwin"
        Entity targetNPC = null;
        for (Entity member : gp.player.partyMembers) {
            if (member.name.equals(targetName)) {
                targetNPC = member;
                break;
            }
        }

        // 3. Realizar la transferencia si se encontró el NPC
        if (targetNPC != null && targetNPC.inventory != null) {
            // Intenta añadir el ítem al inventario del NPC
            if (targetNPC.inventory.addItem(itemToTransfer)) {
                // Si tuvo éxito, remueve el ítem del inventario del jugador
                gp.player.playerInventory.removeItem(itemToTransfer);
                gp.ui.showNotification("Has pasado " + itemToTransfer.name + " a " + targetNPC.name + ".");
            } else {
                // Si el inventario del NPC está lleno
                gp.ui.showNotification("¡El inventario de " + targetNPC.name + " está lleno!");
            }
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

        List<SuperObject> items = getItemsForCurrentTab();
        final int slotStartX = frameX + 20;
        final int slotStartY = frameY + 20;
        final int slotSize = gp.tileSize + 8;
        final int slotSpacing = 10;

        for (int i = 0; i < MAX_INVENTORY_SIZE; i++) {
            int slotX = slotStartX + (i % INVENTORY_COLS) * (slotSize + slotSpacing);
            int slotY = slotStartY + (i / INVENTORY_COLS) * (slotSize + slotSpacing);
            g2.setColor(new Color(255, 255, 255, 60));
            g2.fillRoundRect(slotX, slotY, slotSize, slotSize, 10, 10);
            if (i < items.size()) {
                if (items.get(i).image != null) g2.drawImage(items.get(i).image, slotX + 4, slotY + 4, gp.tileSize, gp.tileSize, null);
            }
        }
        
        int cursorX = slotStartX + (slotCol * (slotSize + slotSpacing));
        int cursorY = slotStartY + (slotRow * (slotSize + slotSpacing));
        int cursorIndex = slotCol + (slotRow * INVENTORY_COLS);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.white);
        g2.drawRoundRect(cursorX, cursorY, slotSize, slotSize, 10, 10);
        if (cursorIndex == 0 && currentTab == 0) {
            g2.setStroke(new BasicStroke(4));
            g2.setColor(new Color(200, 0, 0, 200));
            g2.drawRoundRect(cursorX, cursorY, slotSize, slotSize, 10, 10);
        }

        if (cursorIndex < items.size()) {
            SuperObject selectedItem = items.get(cursorIndex);
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
        
        if (contextMenuOpen) {
            drawContextMenu(cursorX, cursorY);
        }
    }
    
    
    private void drawContextMenu(int x, int y) {
        int menuX = x + gp.tileSize / 2;
        int menuY = y;
        int menuWidth = 200;
        int lineHeight = 35;
        int menuHeight = (lineHeight * contextMenuOptions.size()) + 15;

        drawSubWindow(menuX, menuY, menuWidth, menuHeight);
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.setColor(Color.white);
        for (int i = 0; i < contextMenuOptions.size(); i++) {
            int textX = menuX + 20;
            int textY = menuY + 25 + (i * lineHeight);
            if (i == contextMenuCommandNum) {
                g2.drawString(">", textX - 15, textY);
            }
            g2.drawString(contextMenuOptions.get(i), textX, textY);
        }
    }
    
    private List<SuperObject> getItemsForCurrentTab() {
        if (currentTab == 0) {
            // Para el jugador, ya se está llamando a getItems(), así que está bien.
            if (gp.player.playerInventory != null) {
                return gp.player.playerInventory.getItems();
            }
        } else if (gp.player.partyMembers.size() > currentTab - 1) {
            Entity partyMember = gp.player.partyMembers.get(currentTab - 1);
            if (partyMember.inventory != null) {
                // ✅ CORRECCIÓN: Llama al método getItems() del inventario del NPC.
                return partyMember.inventory.getItems();
            }
        }
        // Si no se encuentra nada, devuelve una lista vacía para evitar errores.
        return new ArrayList<>();
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
        if (inventoryOpen) return;
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

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        int textX = x + gp.tileSize - 30;
        int textY = y + gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.setColor(Color.white);
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        if (gp.currentNPCIndex != -1) {
            Entity npc = gp.npc[gp.currentMap][gp.currentNPCIndex];
            if (npc != null && npc.dialogueOptions != null) {
                int optionY = y + height - 60;
                for (int i = 0; i < npc.dialogueOptions.length; i++) {
                    int optionX = textX + (i * 80); 
                    String optionText = npc.dialogueOptions[i];
                    if (commandNum == i) {
                        g2.setColor(Color.yellow);
                        g2.drawString(">", optionX - 20, optionY);
                    } else {
                        g2.setColor(Color.white);
                    }
                    g2.drawString(optionText, optionX, optionY);
                }
            }
        }
    }

    public void showNotification(String text) {
        notificationText = text;
        notificationCounter = notificationDuration;
    }

    private void drawWrappedText(String text, int x, int y, int maxWidth, Graphics2D g2) {
        FontMetrics fm = g2.getFontMetrics();
        if (text == null) { return; }
        String[] words = text.split(" ");
        if(words.length == 0) return;

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
        Color c = new Color(30, 30, 30, 230);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(220, 220, 220, 80);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}