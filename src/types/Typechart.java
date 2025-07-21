package types;

import java.util.HashMap;
import java.util.Map;

public class Typechart {
    // Definición de todos los tipos de Pokémon
    public static final String NORMAL = "Normal";
    public static final String FIRE = "Fire";
    public static final String WATER = "Water";
    public static final String ELECTRIC = "Electric";
    public static final String GRASS = "Grass";
    public static final String ICE = "Ice";
    public static final String FIGHTING = "Fighting";
    public static final String POISON = "Poison";
    public static final String GROUND = "Ground";
    public static final String FLYING = "Flying";
    public static final String PSYCHIC = "Psychic";
    public static final String BUG = "Bug";
    public static final String ROCK = "Rock";
    public static final String GHOST = "Ghost";
    public static final String DRAGON = "Dragon";
    public static final String DARK = "Dark";
    public static final String STEEL = "Steel";
    public static final String FAIRY = "Fairy";

    // Mapa de efectividades: [attackingType][defendingType] = multiplicador
    private static final Map<String, Map<String, Double>> effectiveness = new HashMap<>();

    // Inicialización de todas las relaciones de tipos
    static {
        // Normal
        addEffectiveness(NORMAL, ROCK, 0.5);
        addEffectiveness(NORMAL, STEEL, 0.5);
        addEffectiveness(NORMAL, GHOST, 0.0);
        
        // Fire
        addEffectiveness(FIRE, GRASS, 2.0);
        addEffectiveness(FIRE, ICE, 2.0);
        addEffectiveness(FIRE, BUG, 2.0);
        addEffectiveness(FIRE, STEEL, 2.0);
        addEffectiveness(FIRE, FIRE, 0.5);
        addEffectiveness(FIRE, WATER, 0.5);
        addEffectiveness(FIRE, ROCK, 0.5);
        addEffectiveness(FIRE, DRAGON, 0.5);
        
        // Water
        addEffectiveness(WATER, FIRE, 2.0);
        addEffectiveness(WATER, GROUND, 2.0);
        addEffectiveness(WATER, ROCK, 2.0);
        addEffectiveness(WATER, WATER, 0.5);
        addEffectiveness(WATER, GRASS, 0.5);
        addEffectiveness(WATER, DRAGON, 0.5);
        
        // Electric
        addEffectiveness(ELECTRIC, WATER, 2.0);
        addEffectiveness(ELECTRIC, FLYING, 2.0);
        addEffectiveness(ELECTRIC, ELECTRIC, 0.5);
        addEffectiveness(ELECTRIC, GRASS, 0.5);
        addEffectiveness(ELECTRIC, DRAGON, 0.5);
        addEffectiveness(ELECTRIC, GROUND, 0.0);
        
        // Grass
        addEffectiveness(GRASS, WATER, 2.0);
        addEffectiveness(GRASS, GROUND, 2.0);
        addEffectiveness(GRASS, ROCK, 2.0);
        addEffectiveness(GRASS, FIRE, 0.5);
        addEffectiveness(GRASS, GRASS, 0.5);
        addEffectiveness(GRASS, POISON, 0.5);
        addEffectiveness(GRASS, FLYING, 0.5);
        addEffectiveness(GRASS, BUG, 0.5);
        addEffectiveness(GRASS, DRAGON, 0.5);
        addEffectiveness(GRASS, STEEL, 0.5);
        
        // Ice
        addEffectiveness(ICE, GRASS, 2.0);
        addEffectiveness(ICE, GROUND, 2.0);
        addEffectiveness(ICE, FLYING, 2.0);
        addEffectiveness(ICE, DRAGON, 2.0);
        addEffectiveness(ICE, FIRE, 0.5);
        addEffectiveness(ICE, WATER, 0.5);
        addEffectiveness(ICE, ICE, 0.5);
        addEffectiveness(ICE, STEEL, 0.5);
        
        // Fighting
        addEffectiveness(FIGHTING, NORMAL, 2.0);
        addEffectiveness(FIGHTING, ICE, 2.0);
        addEffectiveness(FIGHTING, ROCK, 2.0);
        addEffectiveness(FIGHTING, DARK, 2.0);
        addEffectiveness(FIGHTING, STEEL, 2.0);
        addEffectiveness(FIGHTING, POISON, 0.5);
        addEffectiveness(FIGHTING, FLYING, 0.5);
        addEffectiveness(FIGHTING, PSYCHIC, 0.5);
        addEffectiveness(FIGHTING, BUG, 0.5);
        addEffectiveness(FIGHTING, FAIRY, 0.5);
        addEffectiveness(FIGHTING, GHOST, 0.0);
        
        // Poison
        addEffectiveness(POISON, GRASS, 2.0);
        addEffectiveness(POISON, FAIRY, 2.0);
        addEffectiveness(POISON, POISON, 0.5);
        addEffectiveness(POISON, GROUND, 0.5);
        addEffectiveness(POISON, ROCK, 0.5);
        addEffectiveness(POISON, GHOST, 0.5);
        addEffectiveness(POISON, STEEL, 0.0);
        
        // Ground
        addEffectiveness(GROUND, FIRE, 2.0);
        addEffectiveness(GROUND, ELECTRIC, 2.0);
        addEffectiveness(GROUND, POISON, 2.0);
        addEffectiveness(GROUND, ROCK, 2.0);
        addEffectiveness(GROUND, STEEL, 2.0);
        addEffectiveness(GROUND, GRASS, 0.5);
        addEffectiveness(GROUND, BUG, 0.5);
        addEffectiveness(GROUND, FLYING, 0.0);
        
        // Flying
        addEffectiveness(FLYING, GRASS, 2.0);
        addEffectiveness(FLYING, FIGHTING, 2.0);
        addEffectiveness(FLYING, BUG, 2.0);
        addEffectiveness(FLYING, ELECTRIC, 0.5);
        addEffectiveness(FLYING, ROCK, 0.5);
        addEffectiveness(FLYING, STEEL, 0.5);
        
        // Psychic
        addEffectiveness(PSYCHIC, FIGHTING, 2.0);
        addEffectiveness(PSYCHIC, POISON, 2.0);
        addEffectiveness(PSYCHIC, PSYCHIC, 0.5);
        addEffectiveness(PSYCHIC, STEEL, 0.5);
        addEffectiveness(PSYCHIC, DARK, 0.0);
        
        // Bug
        addEffectiveness(BUG, GRASS, 2.0);
        addEffectiveness(BUG, PSYCHIC, 2.0);
        addEffectiveness(BUG, DARK, 2.0);
        addEffectiveness(BUG, FIRE, 0.5);
        addEffectiveness(BUG, FIGHTING, 0.5);
        addEffectiveness(BUG, POISON, 0.5);
        addEffectiveness(BUG, FLYING, 0.5);
        addEffectiveness(BUG, GHOST, 0.5);
        addEffectiveness(BUG, STEEL, 0.5);
        addEffectiveness(BUG, FAIRY, 0.5);
        
        // Rock
        addEffectiveness(ROCK, FIRE, 2.0);
        addEffectiveness(ROCK, ICE, 2.0);
        addEffectiveness(ROCK, FLYING, 2.0);
        addEffectiveness(ROCK, BUG, 2.0);
        addEffectiveness(ROCK, FIGHTING, 0.5);
        addEffectiveness(ROCK, GROUND, 0.5);
        addEffectiveness(ROCK, STEEL, 0.5);
        
        // Ghost
        addEffectiveness(GHOST, GHOST, 2.0);
        addEffectiveness(GHOST, PSYCHIC, 2.0);
        addEffectiveness(GHOST, DARK, 0.5);
        addEffectiveness(GHOST, NORMAL, 0.0);
        
        // Dragon
        addEffectiveness(DRAGON, DRAGON, 2.0);
        addEffectiveness(DRAGON, STEEL, 0.5);
        addEffectiveness(DRAGON, FAIRY, 0.0);
        
        // Dark
        addEffectiveness(DARK, GHOST, 2.0);
        addEffectiveness(DARK, PSYCHIC, 2.0);
        addEffectiveness(DARK, FIGHTING, 0.5);
        addEffectiveness(DARK, DARK, 0.5);
        addEffectiveness(DARK, FAIRY, 0.5);
        
        // Steel
        addEffectiveness(STEEL, ICE, 2.0);
        addEffectiveness(STEEL, ROCK, 2.0);
        addEffectiveness(STEEL, FAIRY, 2.0);
        addEffectiveness(STEEL, FIRE, 0.5);
        addEffectiveness(STEEL, WATER, 0.5);
        addEffectiveness(STEEL, ELECTRIC, 0.5);
        addEffectiveness(STEEL, STEEL, 0.5);
        
        // Fairy
        addEffectiveness(FAIRY, FIGHTING, 2.0);
        addEffectiveness(FAIRY, DRAGON, 2.0);
        addEffectiveness(FAIRY, DARK, 2.0);
        addEffectiveness(FAIRY, FIRE, 0.5);
        addEffectiveness(FAIRY, POISON, 0.5);
        addEffectiveness(FAIRY, STEEL, 0.5);
    }

    private static void addEffectiveness(String attackType, String defenseType, double multiplier) {
        effectiveness
            .computeIfAbsent(attackType, k -> new HashMap<>())
            .put(defenseType, multiplier);
    }

    // Obtiene el multiplicador para un defensor con un solo tipo
    public static double getMultiplier(String attackType, String defenseType) {
        return effectiveness
            .getOrDefault(attackType, new HashMap<>())
            .getOrDefault(defenseType, 1.0); // 1.0 = daño normal
    }

    // Obtiene el multiplicador para un defensor con dos tipos
    public static double getMultiplier(String attackType, String defenseType1, String defenseType2) {
        double mult1 = getMultiplier(attackType, defenseType1);
        double mult2 = getMultiplier(attackType, defenseType2);
        return mult1 * mult2; // Multiplicación de efectividades
    }
}

