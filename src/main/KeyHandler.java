package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import entity.Entity;
import battle.BattleManager;

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

        // === INVENTARIO ===
        if (code == KeyEvent.VK_I && gp.gameState != gp.dialogueState && gp.gameState != gp.battleState) {
            gp.ui.inventoryOpen = !gp.ui.inventoryOpen;
            // Si lo cierras, reseteamos la pestaña actual
            if (!gp.ui.inventoryOpen) gp.ui.currentTab = 0;
        }

        if (gp.ui.inventoryOpen && code == KeyEvent.VK_SHIFT) {
            int maxTabs = 1 + gp.player.partyMembers.size(); // Jugador + NPCs en el equipo
            gp.ui.currentTab = (gp.ui.currentTab + 1) % maxTabs;
        }

        // === TITLE STATE ===
        if (gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) gp.ui.commandNum = 2;
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) gp.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) gp.gameState = gp.playState;
                if (gp.ui.commandNum == 1) {
                    // Cargar partida
                }
                if (gp.ui.commandNum == 2) System.exit(0);
            }
        }

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
                gp.battleManager.selectedOption = (gp.battleManager.selectedOption + len - 1) % len;
            }
            if (code == KeyEvent.VK_RIGHT) {
                gp.battleManager.selectedOption = (gp.battleManager.selectedOption + 1) % len;
            }
            if (code == KeyEvent.VK_ENTER) {
                System.out.println("Opción seleccionada: " + gp.battleManager.options[gp.battleManager.selectedOption]);
            }
        }

        // === DEBUG ===
        if (code == KeyEvent.VK_T) checkDrawTime = !checkDrawTime;

        if (code == KeyEvent.VK_R) {
            switch (gp.currentMap) {
                case 0 -> gp.tileM.loadMap("/maps/map01.txt", 0);
                case 1 -> gp.tileM.loadMap("/maps/interior_cyt_01.txt", 0);
            }
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
        if (npc != null && npc.dialogues != null) {
            if (npc.dialogueIndex + 1 < npc.dialogues.length &&
                npc.dialogues[npc.dialogueIndex + 1] != null) {

                npc.dialogueIndex++;
                gp.ui.currentDialogue = npc.dialogues[npc.dialogueIndex];

                if (npc.dialogueIndex == 1) {
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
}
