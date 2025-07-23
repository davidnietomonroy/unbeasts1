package main;

import Inventory.Inventory_Darwin;
import Inventory.Inventory_Eduardo;
import entity.Entity;
import entity.Monster1;
import entity.NPC_darwin;
import entity.NPC_eduardo;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.Obj_Espada;
import object.Obj_raquetaEspada;
import object.OBJ_Key;
 	


public class AssetSetter {
    
    GamePanel gp;
    int mapNum = 0;
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    public void setEnemy(Entity enemy, int map, int col, int row, String direction) {
        enemy.worldX = col * gp.tileSize;
        enemy.worldY = row * gp.tileSize;
        enemy.direction = direction;
        
    }

    public void setObject() {
    	int mapNum = 0;
        // Objetos deben instanciarse como Entity
        gp.obj[mapNum][0] = new OBJ_Key(gp);
        gp.obj[mapNum][0].worldX = 23 * gp.tileSize;
        gp.obj[mapNum][0].worldY = 7 * gp.tileSize;
        
        // Repite para otros objetos...
        gp.obj[mapNum][7] = new OBJ_Boots(gp);
        gp.obj[mapNum][7].worldX = 19 * gp.tileSize;
        gp.obj[mapNum][7].worldY = 5 * gp.tileSize;
        
        gp.superobj[mapNum][1] = new Obj_Espada(gp);
        gp.superobj[mapNum][1].worldX = gp.tileSize * 1;
        gp.superobj[mapNum][1].worldY = gp.tileSize * 1;
        
        gp.superobj[mapNum][2] = new Obj_raquetaEspada(gp);
        gp.superobj[mapNum][2].worldX = gp.tileSize * 4;
        gp.superobj[mapNum][2].worldY = gp.tileSize * 4;

    }
    
    public void setNPC() {
        int map = 0;

        // NPC 0 - Eduardo
        gp.npc[map][0] = new NPC_eduardo(gp);
        gp.npc[map][0].worldX = gp.tileSize * 21;
        gp.npc[map][0].worldY = gp.tileSize * 21;
        gp.npc[map][0].isInParty = false;
        gp.npc[map][0].inventory = new Inventory_Eduardo(10);


        // NPC 1 - Darwin
        gp.npc[map][1] = new NPC_darwin(gp);
        gp.npc[map][1].worldX = gp.tileSize * 21;
        gp.npc[map][1].worldY = gp.tileSize * 22;
        gp.npc[map][1].isInParty = false;
        gp.npc[map][1].inventory = new Inventory_Darwin(10);


        // NPC 2 - Enemigo 1 (mirando izquierda)
        gp.npc[map][2] = new Monster1(gp);
        gp.npc[map][2].direction = "left";
        gp.npc[map][2].worldX = gp.tileSize * 21;
        gp.npc[map][2].worldY = gp.tileSize * 23;
        
        


    
    

        
}
    }
