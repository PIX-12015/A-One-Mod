package AOneMod.content;

import arc.graphics.Color;
import arc.math.geom.Vec3;
import mindustry.maps.generators.PlanetGenerator;


public class SvittonPlanetGenerator extends PlanetGenerator {

    public int octaves = 3;
    public float persistence = 0.52f;
    public float scale = 22f;
    public float mag = 0.721f;
    public float thresh = 1.152f;
    public int min = 150, max = 160;
    public int radMin = 220, radMax = 600;
    public float iceChance = 0.73f;
    public float carbonChance = 0.15f;
    public float berylChance = 0f;
    public float ferricChance = 0.12f;

    @Override
    public float getHeight(Vec3 position){
        return 0;
    }

    @Override
    public void getColor(Vec3 position, Color out){
        out.set(Color.white);
    }

    // 地图生成逻辑写在这里（无参数）
    @Override
    protected void generate(){
        // 可以使用 tiles, width, height, rand, sector 等继承字段
        // 例如：
        pass((x, y) -> {
            // 设置 floor, block, ore
        });
    }
}