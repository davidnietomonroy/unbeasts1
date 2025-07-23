package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import entity.Entity;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
    public boolean checkDrawTime;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // --- GESTIÓN DE APERTURA/CIERRE DEL INVENTARIO ---
        if (code == KeyEvent.VK_I) {
            if (gp.ui.inventoryOpen) {
                gp.ui.inventoryOpen = false;
                gp.ui.contextMenuOpen = false;
                gp.ui.inspectingItem = false;
                gp.ui.currentTab = 0;
            } else if (gp.gameState == gp.playState || gp.gameState == gp.pauseState) {
                gp.ui.inventoryOpen = true;
            }
            return;
        }

        // --- SI EL INVENTARIO ESTÁ ABIERTO ---
        if (gp.ui.inventoryOpen) {
            handleInventoryKeys(code);
            return;
        }

        // --- MANEJO GENERAL POR ESTADO DE JUEGO ---
        if (gp.gameState == gp.titleState) {
            handleTitleStateKeys(code);
        } else if (gp.gameState == gp.playState) {
            handlePlayStateKeys(code);
        } else if (gp.gameState == gp.pauseState) {
            handlePauseStateKeys(code);
        } else if (gp.gameState == gp.dialogueState) {
            handleDialogueStateKeys(code);
        } else if (gp.gameState == gp.battleState) {
            handleBattleStateKeys(code);
        }

<<<<<<< HEAD
        // Otras teclas útiles globales
=======
        // === PLAY STATE ===
        else if (gp.gameState == gp.playState && !gp.ui.inventoryOpen) {
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
            if (code == KeyEvent.VK_P) gp.gameState = gp.pauseState;
            if (code == KeyEvent.VK_ENTER) enterPressed = true;
        }

        // === PAUSE STATE ===
        else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_P) gp.gameState = gp.playState;
        }

        // === DIALOGUE STATE ===
        else if (gp.gameState == gp.dialogueState) {
            Entity npc = gp.npc[gp.currentMap][gp.currentNPCIndex];

            if (npc != null && npc.isInParty) {
                gp.ui.currentDialogue = "Ya estamos en esto.";
                if (code == KeyEvent.VK_ENTER) {
                    gp.ui.currentDialogue = "";
                    gp.gameState = gp.playState;
                }
                return;
            }

            if (npc != null && npc.dialogueOptions != null) {
                if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
                    gp.ui.commandNum = (gp.ui.commandNum + npc.dialogueOptions.length - 1) % npc.dialogueOptions.length;
                }
                if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
                    gp.ui.commandNum = (gp.ui.commandNum + 1) % npc.dialogueOptions.length;
                }

                if (code == KeyEvent.VK_ENTER) {
                    if (npc.dialogueIndex + 1 < npc.dialogues.length) {
                        if (gp.ui.commandNum == 0) {
                            npc.isInParty = true;
                            gp.ui.showNotification(npc.name + " se ha unido a tu equipo!");
                            gp.player.partyMembers.add(npc); 
                            
                            if (npc.responseYes != null && npc.responseYes.length > 0) {
                                npc.dialogues[npc.dialogueIndex + 1] = npc.responseYes[0];
                            } else {
                                npc.dialogues[npc.dialogueIndex + 1] = "Está bien.";
                            }
                        } else {
                            if (npc.responseNo != null && npc.responseNo.length > 0) {
                                npc.dialogues[npc.dialogueIndex + 1] = npc.responseNo[0];
                            } else {
                                npc.dialogues[npc.dialogueIndex + 1] = "Ey, ¿pero qué pasa vale??";
                            }
                        }
                    }

                    npc.dialogueOptions = null;
                    gp.ui.commandNum = 0;
                    advanceDialogue(npc);
                }
            } else {
                if (code == KeyEvent.VK_ENTER) {
                    advanceDialogue(npc);
                }
            }
        }

        // === BATTLE STATE ===
        else if (gp.gameState == gp.battleState) {
            int len = gp.battleManager.options.length;

            if (code == KeyEvent.VK_LEFT) {
                gp.battleManager.moverSeleccionIzquierda();
            }
            if (code == KeyEvent.VK_RIGHT) {
                gp.battleManager.moverSeleccionDerecha();
            }
            if (code == KeyEvent.VK_ENTER) {
                gp.battleManager.confirmarSeleccion();
            }
            if (code == KeyEvent.VK_ESCAPE) {
            	gp.battleManager.retroceder();
            }
            if (code == KeyEvent.VK_UP) {
                gp.battleManager.moverSeleccionArriba();
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.battleManager.moverSeleccionAbajo();
            }

         }
        
        // === DEBUG ===
>>>>>>> e1505726ca4940b223d554bf9abfd62fafdaa5cb
        if (code == KeyEvent.VK_T) checkDrawTime = !checkDrawTime;

        if (code == KeyEvent.VK_R) {
            switch (gp.currentMap) {
                case 0 -> gp.tileM.loadMap("/maps/map01.txt", 0);
                case 1 -> gp.tileM.loadMap("/maps/interior_cyt_01.txt", 0);
            }
        }
    }

    private void handleInventoryKeys(int code) {
        if (gp.ui.inspectingItem) {
            if (code == KeyEvent.VK_ESCAPE) {
                gp.ui.inspectingItem = false;
            }
        } else if (gp.ui.contextMenuOpen) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.contextMenuCommandNum--;
                if (gp.ui.contextMenuCommandNum < 0) {
                    gp.ui.contextMenuCommandNum = gp.ui.contextMenuOptions.size() - 1;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.ui.contextMenuCommandNum++;
                if (gp.ui.contextMenuCommandNum >= gp.ui.contextMenuOptions.size()) {
                    gp.ui.contextMenuCommandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                gp.ui.executeContextAction();
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.ui.contextMenuOpen = false;
            }
        } else {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) gp.ui.slotRow--;
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) gp.ui.slotRow++;
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) gp.ui.slotCol--;
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) gp.ui.slotCol++;

            if (gp.ui.slotRow < 0) gp.ui.slotRow = gp.ui.INVENTORY_ROWS - 1;
            if (gp.ui.slotRow >= gp.ui.INVENTORY_ROWS) gp.ui.slotRow = 0;
            if (gp.ui.slotCol < 0) gp.ui.slotCol = gp.ui.INVENTORY_COLS - 1;
            if (gp.ui.slotCol >= gp.ui.INVENTORY_COLS) gp.ui.slotCol = 0;

            if (code == KeyEvent.VK_SHIFT) {
                int maxTabs = 1 + gp.player.partyMembers.size();
                gp.ui.currentTab = (gp.ui.currentTab + 1) % maxTabs;
            }

            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E) {
                gp.ui.openContextMenu();
            }
        }
    }

    private void handleTitleStateKeys(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) gp.ui.commandNum = 2;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 2) gp.ui.commandNum = 0;
        }
        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNum == 0) gp.gameState = gp.playState;
            if (gp.ui.commandNum == 1) { /* Cargar partida */ }
            if (gp.ui.commandNum == 2) System.exit(0);
        }
    }

    private void handlePlayStateKeys(int code) {
        if (code == KeyEvent.VK_W) upPressed = true;
        if (code == KeyEvent.VK_A) leftPressed = true;
        if (code == KeyEvent.VK_S) downPressed = true;
        if (code == KeyEvent.VK_D) rightPressed = true;
        if (code == KeyEvent.VK_P) gp.gameState = gp.pauseState;
        if (code == KeyEvent.VK_ENTER) enterPressed = true;
    }

    private void handlePauseStateKeys(int code) {
        if (code == KeyEvent.VK_P) gp.gameState = gp.playState;
    }

    private void handleDialogueStateKeys(int code) {
        Entity npc = gp.npc[gp.currentMap][gp.currentNPCIndex];
        if (npc == null) return;

        if (npc.isInParty) {
            if (code == KeyEvent.VK_ENTER) {
                gp.ui.currentDialogue = "";
                gp.gameState = gp.playState;
            }
            return;
        }

        if (npc.dialogueOptions != null) {
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                gp.ui.commandNum = (gp.ui.commandNum + npc.dialogueOptions.length - 1) % npc.dialogueOptions.length;
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                gp.ui.commandNum = (gp.ui.commandNum + 1) % npc.dialogueOptions.length;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    npc.isInParty = true;
                    gp.player.partyMembers.add(npc);
                    gp.ui.showNotification(npc.name + " se ha unido a tu equipo!");
                }
                npc.dialogueOptions = null;
                gp.ui.commandNum = 0;
                advanceDialogue(npc);
            }
        } else {
            if (code == KeyEvent.VK_ENTER) {
                advanceDialogue(npc);
            }
        }
    }

    private void handleBattleStateKeys(int code) {
        if (gp.battleManager.options == null) return;
        int len = gp.battleManager.options.length;
        if (len == 0) return;

        if (code == KeyEvent.VK_LEFT) {
            gp.battleManager.selectedOption = (gp.battleManager.selectedOption + len - 1) % len;
        }
        if (code == KeyEvent.VK_RIGHT) {
            gp.battleManager.selectedOption = (gp.battleManager.selectedOption + 1) % len;
        }
        if (code == KeyEvent.VK_ENTER) {
            gp.battleManager.confirmarSeleccion();
        }
        if (code == KeyEvent.VK_ESCAPE) {
            gp.battleManager.retroceder();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_ENTER) enterPressed = false;
    }

    private void advanceDialogue(Entity npc) {
        if (npc == null || npc.dialogues == null) return;

        if (npc.dialogueIndex + 1 < npc.dialogues.length && npc.dialogues[npc.dialogueIndex + 1] != null) {
            npc.dialogueIndex++;
            gp.ui.currentDialogue = npc.dialogues[npc.dialogueIndex];

            if (npc.dialogueIndex == 1 && npc.dialogueOptions == null) {
                npc.dialogueOptions = new String[]{"Sí", "No"};
            }
        } else {
            npc.dialogueIndex = 0;
            npc.dialogueOptions = null;
            gp.ui.currentDialogue = "";
            gp.gameState = gp.playState;
        }
    }
}
