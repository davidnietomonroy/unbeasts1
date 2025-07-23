package battle;

public class Habilidad {
    private int id;
    private String nombre;
    private String tipo1;
    private String tipo2;
    private int damage;
    private int costeMana;

    public Habilidad(int id, String nombre, String tipo1, String tipo2, int damage, int costeMana) {
        this.id = id;
        this.nombre = nombre;
        this.tipo1 = tipo1;
        this.tipo2 = tipo2;
        this.damage = damage;
        this.costeMana = costeMana;
    }

    @Override
    public String toString() {
        return nombre + " (" + tipo1 + (tipo2 != null ? "/" + tipo2 : "") +
               ") - Daño: " + damage + ", Mana: " + costeMana;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public int getDamage() {
    	return damage;
    }
    
    public int getCosteMana() {
    	return costeMana;
    }

}
