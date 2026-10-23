package AOneMod.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class ThisItems {
    public static Item
            iron, ironPlate, nails, diamond, coke, rubber;

    public static void load(){
        iron = new Item("iron", Color.lightGray){{
            hardness = 2;
            cost = 0.5f;
            alwaysUnlocked = true;
        }};

        ironPlate = new Item("iron_plate", Color.gray){{
            cost = 0.5f;
            hardness = 1;
            alwaysUnlocked = true;
        }};

        nails = new Item("nails", Color.darkGray){{
            cost = 0.5f;
            hardness = 1;
            alwaysUnlocked = true;
        }};

        diamond = new Item("diamond", Color.valueOf("#9fddff")){{
            cost = 0.5f;
            hardness = 4;
            alwaysUnlocked = true;
        }};

        coke = new Item("coke", Color.valueOf("2e2e39")){{
            cost = 0.5f;
            explosiveness = 0.1f;
            flammability = 1f;
            hardness = 2;
            alwaysUnlocked = true;
        }};

        rubber = new Item("rubber", Color.valueOf("2f3346")){{
            cost = 0.5f;
            hardness = 1;
            alwaysUnlocked = true;
        }};
    }
}
