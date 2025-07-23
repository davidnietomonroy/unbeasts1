package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;

public class MouseHandler implements MouseListener {

    GamePanel gp;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // No procesar clics si no estamos en el estado de juego o inventario
        if (gp.gameState != gp.playState && gp.gameState != gp.pauseState && !gp.ui.inventoryOpen) {
            return;
        }

        // --- CASO 1: Ventana de Inspección de Ítem está abierta ---
        if (gp.ui.inventoryOpen && gp.ui.inspectingItem) {
            // Solo se permite hacer clic en el botón de cerrar
            if (gp.ui.closeButtonRect != null && gp.ui.closeButtonRect.contains(e.getPoint())) {
                gp.ui.inspectingItem = false;
                 // Sonido de cancelar/cerrar (opcional)
            }
            return; // No procesar más clics
        }

        // --- CASO 2: Inventario está abierto ---
        if (gp.ui.inventoryOpen) {
            // Manejar Clic Izquierdo (Seleccionar opción del menú o cambiar de pestaña)
            if (SwingUtilities.isLeftMouseButton(e)) {
                // Si el menú contextual está abierto, el clic interactúa con él
                if (gp.ui.contextMenuOpen) {
                    handleContextMenuClick(e);
                } else {
                    // Si no, revisa si se hizo clic en una pestaña de personaje
                    handleTabClick(e);
                }
            }
            // Manejar Clic Derecho (Abrir menú contextual en un slot)
            else if (SwingUtilities.isRightMouseButton(e)) {
                // Solo abrir un menú nuevo si no hay uno ya abierto
                if (!gp.ui.contextMenuOpen) {
                    handleOpenContextMenu(e);
                } else {
                    // Si ya hay un menú abierto, un clic derecho lo cierra
                    gp.ui.contextMenuOpen = false;
                }
            }
            return; // Finaliza el procesamiento para el inventario
        }
    }

    /**
     * Procesa un clic izquierdo cuando el menú contextual está abierto.
     * Ejecuta una acción o cierra el menú si se hace clic fuera.
     */
    private void handleContextMenuClick(MouseEvent e) {
        // Coordenadas del slot donde se originó el menú
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int slotStartX = frameX + 20;
        final int slotStartY = frameY + 20;
        final int slotSize = gp.tileSize + 8;
        final int slotSpacing = 10;
        int cursorX = slotStartX + (gp.ui.slotCol * (slotSize + slotSpacing));
        int cursorY = slotStartY + (gp.ui.slotRow * (slotSize + slotSpacing));

        // ✅ CORRECCIÓN: Dimensiones exactas del menú como en UI.java
        int menuX = cursorX + gp.tileSize / 2;
        int menuY = cursorY;
        int menuWidth = 300; // Coincide con UI.java
        int lineHeight = 35; // Coincide con UI.java
        int menuHeight = (lineHeight * gp.ui.contextMenuOptions.size()) + 20; // Coincide con UI.java

        // Evita que el menú se salga de la pantalla (misma lógica que en UI.java)
        if (menuX + menuWidth > gp.screenWidth) {
            menuX = cursorX - menuWidth + gp.tileSize / 2;
        }
        
        // Verifica si el clic fue dentro de la ventana del menú
        if (e.getX() >= menuX && e.getX() <= menuX + menuWidth &&
            e.getY() >= menuY && e.getY() <= menuY + menuHeight) {
            
            // ✅ CORRECCIÓN: Cálculo preciso de la opción clickeada
            // El primer texto se dibuja en `menuY + 30`. El área clicable de cada opción tiene `lineHeight`.
            int initialPadding = 15; // Un área de padding superior razonable antes del primer texto
            int clickedOptionIndex = (e.getY() - menuY - initialPadding) / lineHeight;

            if (clickedOptionIndex >= 0 && clickedOptionIndex < gp.ui.contextMenuOptions.size()) {
                gp.ui.contextMenuCommandNum = clickedOptionIndex;
                gp.ui.executeContextAction();
               
            }
        } else {
            // Si se hace clic fuera del menú, simplemente ciérralo
            gp.ui.contextMenuOpen = false;
        }
    }

    /**
     * ⭐ NUEVO: Procesa un clic en las pestañas del inventario para cambiar de personaje.
     */
    private void handleTabClick(MouseEvent e) {
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        int tabWidth = 120;
        int tabHeight = 35;
        int tabSpacing = 5;
        int baseY = frameY - tabHeight;
        int totalTabs = 1 + gp.player.partyMembers.size();

        for (int i = 0; i < totalTabs; i++) {
            int tabX = frameX + i * (tabWidth + tabSpacing);
            if (e.getX() >= tabX && e.getX() <= tabX + tabWidth &&
                e.getY() >= baseY && e.getY() <= baseY + tabHeight) {
                
                if (gp.ui.currentTab != i) {
                    gp.ui.currentTab = i;
                    gp.ui.slotCol = 0; // Resetea el cursor al cambiar de pestaña
                    gp.ui.slotRow = 0;
                    
                }
                break; // Termina el bucle una vez que se encuentra la pestaña
            }
        }
    }
    
    /**
     * Procesa un clic derecho para abrir el menú contextual en un slot del inventario.
     */
    private void handleOpenContextMenu(MouseEvent e) {
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int slotStartX = frameX + 20;
        final int slotStartY = frameY + 20;
        final int slotSize = gp.tileSize + 8;
        final int slotSpacing = 10;
        final int totalSlotSize = slotSize + slotSpacing;

        int mouseX = e.getX();
        int mouseY = e.getY();
        
        // Solo procesar si el clic está dentro del área general de los slots
        if (mouseX < slotStartX || mouseY < slotStartY) return;

        int col = (mouseX - slotStartX) / totalSlotSize;
        int row = (mouseY - slotStartY) / totalSlotSize;
        
        if (col >= 0 && col < gp.ui.INVENTORY_COLS && row >= 0 && row < gp.ui.INVENTORY_ROWS) {
            // Verifica que el clic fue dentro del slot visible, no en el espacio entre ellos
            int slotX = slotStartX + col * totalSlotSize;
            int slotY = slotStartY + row * totalSlotSize;
            if (mouseX >= slotX && mouseX <= slotX + slotSize && mouseY >= slotY && mouseY <= slotY + slotSize) {
                gp.ui.slotCol = col;
                gp.ui.slotRow = row;
                gp.ui.openContextMenu();
                
            }
        }
    }

    // Métodos no utilizados de la interfaz MouseListener
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}