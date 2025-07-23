package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import main.GamePanel;
import types.Typechart;

public class Obj_Espada extends SuperObject {

    public Obj_Espada(GamePanel gp) {
        name = "Espada";  // <- corregido nombre
        description = "Espada, hace 2 de daño";
        collision = false;

        try {
            BufferedImage original = ImageIO.read(getClass().getResourceAsStream("/objects/espada.png"));
            image = uTool.scaleImage(original, gp.tileSize, gp.tileSize);  // debe devolver imagen escalada
        } catch (IOException e) {
            System.err.println("❌ Error al cargar la imagen de la espada: " + e.getMessage());
            e.printStackTrace();
        }

        // Asegúrate que 'types' no sea null y tenga tamaño suficiente

        types[0] = Typechart.STEEL;
        types[1] = Typechart.DARK;
    }
}
