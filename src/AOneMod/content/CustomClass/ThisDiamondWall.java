package AOneMod.content.CustomClass;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.gen.Sounds;
import mindustry.world.blocks.defense.Wall;

import static mindustry.Vars.tilesize;

public class ThisDiamondWall extends Wall{
    public float damageThreshold = 200f;      // 伤害阈值
    public float baseShatterChance = 0.02f;   // 基础碎裂概率
    public float damageMultiplier = 0.0006f;   // 伤害对概率的影响系数

    public ThisDiamondWall(String name) {
        super(name);
    }

    public class ThisDiamondWallBuild extends WallBuild{
        public float accumulatedDamage = 0f;

        @Override
        public void damage(float damage){
            if(dead()) return;

            // 只记录实际受到的伤害（扣除护甲后）
            float actualDamage = damage * (1f - block.armor * 0.1f);
            accumulatedDamage += actualDamage;

            // 只有在累积伤害超过阈值且单次伤害较大时才检查碎裂
            if(accumulatedDamage > damageThreshold && damage > 30f){
                float shatterChance = calculateShatterChance();
                if(Mathf.chance(shatterChance)){
                    shatter();
                    return;
                }
            }

            super.damage(damage);
        }

        float calculateShatterChance(){
            float excessDamage = Math.max(0, accumulatedDamage - damageThreshold);
            // 更平缓的概率增长曲线
            return Math.min(0.6f, baseShatterChance + excessDamage * damageMultiplier);
        }

        void shatter(){
            // 碎裂效果
            Fx.breakProp.at(x, y);
            Sounds.acceleratorLightning2.at(tile);

            // 碎裂伤害
            kill();

            // 创建碎片效果
            for(int i = 0; i < 8; i++){
                float angle = Mathf.random(360f);
                float speed = Mathf.random(2f, 4f);
                Fx.casing4.at(x, y, angle, Color.grays(speed), Color.valueOf("e0f7ff"));
            }
        }

        @Override
        public void draw(){
            super.draw();

            // 显示累积伤害的视觉效果
            if(accumulatedDamage > damageThreshold * 0.5f){
                float damageRatio = Math.min(1f, accumulatedDamage / (damageThreshold * 2f));
                Draw.color(Color.valueOf("e0f7ff"));
                Draw.alpha(damageRatio * 0.3f);
                Draw.blend(Blending.additive);
                Fill.rect(x, y, tilesize * size, tilesize * size);
                Draw.blend();
                Draw.reset();
            }
        }
    }
}
