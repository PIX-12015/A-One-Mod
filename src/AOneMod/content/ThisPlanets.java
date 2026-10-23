package AOneMod.content;

import arc.func.Prov;
import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.g3d.*;
import mindustry.maps.planet.ErekirPlanetGenerator;
import mindustry.type.Planet;


public class ThisPlanets {
    public static Planet redSun, carol, Trial;

    public static void load() {

        redSun = new Planet("red-sun", null, 2f){{
            bloom = true;
            accessible = false;

            lightColor = Color.valueOf("ff4444");
            atmosphereColor = Color.valueOf("ff6666");

            meshLoader = () -> new SunMesh(
                    this, 4,
                    5, 0.3, 1.7, 1.2, 1,
                    1.1f,
                    Color.valueOf("ff6666"),
                    Color.valueOf("ff4444"),
                    Color.valueOf("ff3333"),
                    Color.valueOf("ff2222"),
                    Color.valueOf("ff1111"),
                    Color.valueOf("ff0000")
            );
        }};

        Trial =  new Planet("Trial", ThisPlanets.redSun, 1f, 2){{

            generator = new ThisPlanetsGenerator();
            meshLoader = () -> new HexMesh(this, 5);

            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 1, 0.12f, 0.14f, 5, Color.valueOf("eba768").a(0.75f), 2, 0.42f, 1f, 0.43f)
            );
            atmosphereColor = Color.valueOf("#f07218");
            iconColor = Color.valueOf("#b31824");
            defaultCore = Blocks.coreBastion;
            startSector = 0;
            tidalLock = true;

            ruleSetter = r -> {
                r.waveTeam = Team.crux;
                r.placeRangeCheck = false;
                r.fog = true;
            };
            alwaysUnlocked = true;
        }};

        carol =  new Planet("carol", Planets.sun, 1f, 1){{
            generator = new AzPlanetsGenerator();
            meshLoader = () -> new HexMesh(this, 5);
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 2, 0.21f, 0.14f, 5, Color.valueOf("d5f8ff").a(0.75f), 2, 0.42f, 1f, 0.43f),
                    new HexSkyMesh(this, 4, 0.15f, 0.24f, 5, Color.valueOf("d5f8ff").a(0.75f), 2, 0.42f, 1f, 0.43f)
            );
            atmosphereColor = Color.valueOf("#4bd9ff");
            iconColor = Color.valueOf("#4de2ff");
            defaultCore = Blocks.coreShard;
            startSector = 0;
            tidalLock = true;
            prebuildBase = true;
            allowCampaignRules = true;
            allowLaunchToNumbered = false;

            ruleSetter = r -> {
                r.waveTeam = Team.crux;
                r.placeRangeCheck = false;
                r.fog = true;
            };
            alwaysUnlocked = true;
        }};
    }
}
