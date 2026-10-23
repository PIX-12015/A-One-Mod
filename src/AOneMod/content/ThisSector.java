package AOneMod.content;


import mindustry.content.Planets;
import mindustry.type.SectorPreset;

public class ThisSector {
    public static SectorPreset Area1, LoginPoint1;

    public static void load(){
        Area1 = new SectorPreset("Area1", Planets.serpulo, 1){{
            this.alwaysUnlocked = true;
            this.addStartingItems = false;
            this.captureWave = 20;
            this.difficulty = 1.2F;
            this.overrideLaunchDefaults = true;
            this.noLighting = true;
            this.startWaveTimeMultiplier = 1.0F;
        }};
        LoginPoint1 =  new SectorPreset("LoginPoint1", ThisPlanets.carol, 0){{
            this.alwaysUnlocked = true;
            this.addStartingItems = true;
            this.captureWave = 5;
            this.difficulty = 1.0F;
            this.overrideLaunchDefaults = true;
            this.noLighting = true;
            this.startWaveTimeMultiplier = 1.0F;
        }};
    }
}
