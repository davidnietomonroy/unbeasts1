package entity;

import java.util.Random;
import main.GamePanel;
import types.Typechart;

public class NPC_eduardo extends Entity {
	
    
	public NPC_eduardo(GamePanel gp) {
	    super(gp);
	    name = "Eduardo"; 
	    direction = "down";
	    speed = 1;
	    getImage();
	    setDialogue();
	    vidaMaxima = 100;
	    vida = vidaMaxima;
	    types[0] = Typechart.PSYCHIC;
	    types[1] = Typechart.STEEL;
	}

    public void getImage() {
        up1 = setup("/friends/eduardo_up_1");
        up2 = setup("/friends/eduardo_up_2");
        down1 = setup("/friends/eduardo_down_1");
        down2 = setup("/friends/eduardo_down_2");
        left1 = setup("/friends/eduardo_left_1");
        left2 = setup("/friends/eduardo_left_2");
        right1 = setup("/friends/eduardo_right_1");
        right2 = setup("/friends/eduardo_right_2");
    }

    public void setDialogue() {
        dialogues[0] = "Hola Sofia, ¿Que te pasa ?";
        dialogues[1] = "Oh, ¿Asi que quieres que me una a tu grupo?";
        
        
      
        // Nuevas respuestas
        responseYes = new String[] {
            "¡les go."
        };
        responseNo = new String[] {
            "falsisima"
        };
    }

    public void setAction() {
    	
    	actionLockCounter ++;
    	if (actionLockCounter == 120) {
    		Random random = new Random();
        	int i = random.nextInt(100+1);
        	
        	if(i<=25 ) {
        		direction="up";
        		   				
        	}
        	if(i>25 && i <= 50 ) {
        		direction="down";
        	}
        	if(i>50 && i <=75 ) {
        		direction="left";
        	}
        	if(i>75 && i <= 100) {
        		direction="right";
        	}
        	actionLockCounter =0;
    	}
    	
    	
    }

    @Override
    public void speak() {
        super.speak(); // Muestra el diálogo actual
        facePlayer();

        // Mostrar opciones solo en índice 2
        if (dialogueIndex == 2 && dialogueOptions == null) {
            dialogueOptions = new String[] { "Sí", "No" };
        }
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