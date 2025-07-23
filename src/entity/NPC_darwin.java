package entity;

import java.util.Random;
import main.GamePanel;
import types.Typechart;

public class NPC_darwin extends Entity {
    
    public NPC_darwin(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 3;
        name="Darwin";
        getImage();
        setDialogue();
        vidaMaxima = 100;
        vida = vidaMaxima;
        types[0] = Typechart.FIRE;
        types[1] = Typechart.FIGHTING;
        
    }

    public void getImage() {
        up1 = setup("/friends/darwin_up_1");
        up2 = setup("/friends/darwin_up_2");
        down1 = setup("/friends/darwin_down_1");
        down2 = setup("/friends/darwin_down_2");
        left1 = setup("/friends/darwin_left_1");
        left2 = setup("/friends/darwin_left_2");
        right1 = setup("/friends/darwin_right_1");
        right2 = setup("/friends/darwin_right_2");
    }

    public void setDialogue() {
        dialogues[0] = "Hola Sofia, ¿Que te pasa hermano?";
        dialogues[1] = "Oh, ¿Asi que quieres que me una a tu grupo?";
        
        
      
        // Nuevas respuestas
        responseYes = new String[] {
            "Pues ya que"
        };
        responseNo = new String[] {
            "Mejor, no queria ir igual"
        };
    }
    

    @Override
    public void setAction() {
        actionLockCounter++;
        
        // Cambia de dirección cada 120 frames (~2 segundos a 60 FPS)
        if (actionLockCounter >= 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;  // Número aleatorio entre 1 y 100
            
            if (i <= 25) direction = "up";
            else if (i <= 50) direction = "down";
            else if (i <= 75) direction = "left";
            else direction = "right";
            
            actionLockCounter = 0;  // Reinicia el contador
        }
    }

    @Override
    public void speak() {
        super.speak();  // Hereda el método base de Entity
        facePlayer();   // Darwin mira al jugador al hablar
    }

    public void facePlayer() {
        switch(gp.player.direction) {
            case "up": direction = "down"; break;
            case "down": direction = "up"; break;
            case "left": direction = "right"; break;
            case "right": direction = "left"; break;
        }
    }
}