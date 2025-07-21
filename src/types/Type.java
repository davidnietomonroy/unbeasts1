package types;
	public class Type {
		 public static final Type NORMAL = new Type("Normal");
		    public static final Type FIRE = new Type("Fire");
		    public static final Type WATER = new Type("Water");
		    public static final Type ELECTRIC = new Type("Electric");
		    public static final Type GRASS = new Type("Grass");
		    public static final Type ICE = new Type("Ice");
		    public static final Type FIGHTING = new Type("Fighting");
		    public static final Type POISON = new Type("Poison");
		    public static final Type GROUND = new Type("Ground");
		    public static final Type FLYING = new Type("Flying");
		    public static final Type PSYCHIC = new Type("Psychic");
		    public static final Type BUG = new Type("Bug");
		    public static final Type ROCK = new Type("Rock");
		    public static final Type GHOST = new Type("Ghost");
		    public static final Type DRAGON = new Type("Dragon");
		    public static final Type DARK = new Type("Dark");
		    public static final Type STEEL = new Type("Steel");
		    public static final Type FAIRY = new Type("Fairy");
	    // Agrega más tipos según lo necesites

	    public final String name;

	    public Type(String name) {
	        this.name = name;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (o instanceof Type) {
	            return this.name.equals(((Type) o).name);
	        }
	        return false;
	    }

	    @Override
	    public String toString() {
	        return name;
	    }
	}



