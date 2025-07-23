package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import main.GamePanel;
import types.Typechart;

public class Obj_raquetaEspada extends SuperObject {

    public Obj_raquetaEspada(GamePanel gp) {
        name = "Arma";  // <- corregido nombre
        description = "una raqueta de ping pong?, o una espada?";
        collision = false;
        detailedDescription = "daño= 0,tipos: NORMAL, PELEA";

        try {
            BufferedImage original = ImageIO.read(getClass().getResourceAsStream("/objects/espada_pingpong.png"));
            image = uTool.scaleImage(original, gp.tileSize, gp.tileSize);  // debe devolver imagen escalada
        } catch (IOException e) {
            System.err.println("❌ Error al cargar la imagen de la espada: " + e.getMessage());
            e.printStackTrace();
        }

        // Asegúrate que 'types' no sea null y tenga tamaño suficiente

        types[0] = Typechart.NORMAL;
        types[1] = Typechart.FIGHTING;
    }
}
