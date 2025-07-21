package object;



import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import types.Typechart;

public class Obj_Espada extends SuperObject {

 
    public Obj_Espada(GamePanel gp) {
        
        name = "Espada";
        description= "espada, hace 2 de daño";
        
      
        collision = false;

        try {
            // Carga la imagen desde la carpeta de recursos.
            image = ImageIO.read(getClass().getResourceAsStream("/objects/espada.png"));
            // Escala la imagen al tamaño de un tile para que se vea bien en el juego.
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de la espada: " + e.getMessage());
            e.printStackTrace();
        }

        // Asigna los dos tipos a la espada, usando el array de la clase padre 'SuperObject'.
        types[0] = Typechart.STEEL;
        types[1] = Typechart.DARK;
    }
}