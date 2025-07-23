package battle;

import java.util.List;

public class PruebaHabilidades {
    public static void main(String[] args) {
        List<Habilidad> habilidades = HabilidadDAO.obtenerHabilidades();
        for (Habilidad h : habilidades) {
            System.out.println(h);
        }
    }
}
