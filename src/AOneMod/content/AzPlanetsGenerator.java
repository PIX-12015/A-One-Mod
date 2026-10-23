package AOneMod.content;

import arc.math.Mathf;
import arc.graphics.Color;
import arc.math.geom.Vec3;
import arc.util.Tmp;
import arc.util.noise.Simplex;
import mindustry.content.Blocks;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.world.Block;


public class AzPlanetsGenerator extends PlanetGenerator {

    Block[] arr = new Block[]{
            Blocks.water,Blocks.sand,Blocks.grass,Blocks.stone,Blocks.snow
    };


    @Override
    protected void generate() {
        pass((xx,yy) -> {
            if(xx % 10 == 0  || yy % 10 == 0)
                this.block = Blocks.copperWall;
        });

        median(3);

    }

    float rawHeight(Vec3 position) {
        final float SCALE = 1.2f; // ← 调整此值控制起伏大小

        position = Tmp.v33.set(position);
        return (Mathf.pow(
                Simplex.noise3d(
                        this.seed = 2,
                        (double) 4.0f,
                        //这下面两个参数是起伏
                        (double) 0.7f,
                        (double) 0.333334444f,
                        (double) position.x * SCALE,
                        (double) position.y * SCALE,
                        (double) position.z * SCALE),
                2f));
                //上面这一个参数用途不明，已知效果是提高参数会导致陆地消失
    }

    @Override
    public float getHeight(Vec3 position) {
        float v = rawHeight(position);
//         ThisItems.iron.description += ("\n" + v);
        return v;
    }

    @Override
    public void getColor(Vec3 position, Color out){
        float h = rawHeight(position) * 10;
        out.set(arr [(int) Mathf.clamp (h, 0, arr.length - 1)].mapColor);
    }
}
