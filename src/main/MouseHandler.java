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
        // Si se está inspeccionando un ítem, solo revisa el clic en el botón de cerrar
        if (gp.ui.inspectingItem) {
            if (gp.ui.closeButtonRect != null && gp.ui.closeButtonRect.contains(e.getPoint())) {
                gp.ui.inspectingItem = false;
            }
            return;
        }

        // ✅ INICIO DE LA CORRECCIÓN: Manejo del clic izquierdo en el menú contextual
        if (gp.ui.contextMenuOpen && SwingUtilities.isLeftMouseButton(e)) {
            // Coordenadas del cursor del inventario (donde se originó el menú)
            final int frameX = gp.tileSize;
            final int frameY = gp.tileSize;
            final int slotStartX = frameX + 20;
            final int slotStartY = frameY + 20;
            final int slotSize = gp.tileSize + 8;
            final int slotSpacing = 10;
            int cursorX = slotStartX + (gp.ui.slotCol * (slotSize + slotSpacing));
            int cursorY = slotStartY + (gp.ui.slotRow * (slotSize + slotSpacing));

            // Coordenadas del menú (estas deben coincidir con las de UI.drawContextMenu)
            int menuX = cursorX + gp.tileSize / 2;
            int menuY = cursorY;
            int menuWidth = 200;
            int lineHeight = 35;
            int menuHeight = (lineHeight * gp.ui.contextMenuOptions.size()) + 15;

            // Verifica si el clic fue dentro de la ventana del menú contextual
            if (e.getX() >= menuX && e.getX() <= menuX + menuWidth &&
                e.getY() >= menuY && e.getY() <= menuY + menuHeight) {
                
                // Calcula qué opción fue clickeada
                int clickedOptionIndex = (e.getY() - menuY - 10) / lineHeight; // El 10 y 35 deben coincidir con el padding y alto de línea en drawContextMenu

                // Asegúrate de que el índice es válido
                if (clickedOptionIndex >= 0 && clickedOptionIndex < gp.ui.contextMenuOptions.size()) {
                    // Establece el comando y ejecútalo
                    gp.ui.contextMenuCommandNum = clickedOptionIndex;
                    gp.ui.executeContextAction();
                }
            } else {
                // Si se hace clic fuera del menú, simplemente ciérralo
                gp.ui.contextMenuOpen = false;
            }
            return; // Termina el procesamiento aquí
        }
        // ✅ FIN DE LA CORRECCIÓN

        // Lógica para abrir el menú con clic derecho (cuando no está ya abierto)
        if (gp.ui.inventoryOpen && SwingUtilities.isRightMouseButton(e)) {
            final int frameX = gp.tileSize;
            final int frameY = gp.tileSize;
            final int slotStartX = frameX + 20;
            final int slotStartY = frameY + 20;
            final int slotSize = gp.tileSize + 8;
            final int slotSpacing = 10;
            final int totalSlotSize = slotSize + slotSpacing;

            int mouseX = e.getX();
            int mouseY = e.getY();
            
            int col = (mouseX - slotStartX) / totalSlotSize;
            int row = (mouseY - slotStartY) / totalSlotSize;
            
            if (col >= 0 && col < gp.ui.INVENTORY_COLS && row >= 0 && row < gp.ui.INVENTORY_ROWS) {
                int slotX = slotStartX + col * totalSlotSize;
                int slotY = slotStartY + row * totalSlotSize;
                if (mouseX >= slotX && mouseX <= slotX + slotSize && mouseY >= slotY && mouseY <= slotY + slotSize) {
                    gp.ui.slotCol = col;
                    gp.ui.slotRow = row;
                    gp.ui.openContextMenu();
                }
            }
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}