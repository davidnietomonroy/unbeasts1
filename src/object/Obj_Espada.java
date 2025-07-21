package object;

import javax.imageio.ImageIO;
import java.io.IOException;

import main.GamePanel;
import types.Typechart;

public class Obj_Espada extends SuperObject {

    public Obj_Espada(GamePanel gp) {
        name = "Espada";
        collision = false;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/espada.png")); // Asegúrate de que exista
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Asignar tipos como si fuera un Pokémon
        types[0] = Typechart.STEEL;
        types[1] = Typechart.DARK;
    }
}
