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

public class ThisDiamondWall extends Wall {
    /**
     * 碎裂阈值 = 方块血量 × 该比例。
     * 用比例而不是绝对值，大小墙才会自动分级（900/3600/8100 各自缩放）。
     */
    public float shatterThresholdFrac = 0.25f;

    /** 基础碎裂概率（刚刚超过阈值时） */
    public float baseShatterChance = 0.02f;

    /** 累积伤害每超出阈值 1 点，碎裂概率的增加量 */
    public float damageMultiplier = 0.0006f;

    /** 碎裂概率上限 */
    public float maxShatterChance = 0.6f;

    /**
     * 护甲对"碎裂计量"的减免：每 1 点护甲让计量速度降低 3%。
     *
     * 原来是 -10%，导致：
     *   armor 6  → 1 - 0.6  = 0.4   正常
     *   armor 10 → 1 - 1.0  = 0.0   永远累积不上，大墙不可能碎
     *   armor 14 → 1 - 1.4  = -0.4  越打越负，巨型墙更不可能碎
     * 改成 3% 后：6→0.82、10→0.70、14→0.58，恒为正且随护甲递减。
     *
     * 注意：这里用 Math.max（java.lang.Math）而不是 Mathf.max ——
     * Arc 的 Mathf 里没有两参数的 max/min，只有 clamp 和 maxZero。
     */
    public float armorAbsorbPerPoint = 0.03f;

    /** 参与碎裂判定的单次伤害下限，避免高频小伤害反复掷骰 */
    public float minTriggerDamage = 30f;

    public ThisDiamondWall(String name) {
        super(name);
    }

    public class ThisDiamondWallBuild extends WallBuild {
        public float accumulatedDamage = 0f;

        /** 当前这堵墙的碎裂阈值 */
        float shatterThreshold() {
            return block.health * shatterThresholdFrac;
        }

        /** 护甲减免系数，保证恒为正 */
        float armorFactor() {
            return Math.max(0.05f, 1f - block.armor * armorAbsorbPerPoint);
        }

        @Override
        public void damage(float damage) {
            if (dead()) return;

            // 只把"穿透护甲后"的伤害计入碎裂计量
            float actualDamage = Math.max(0f, damage) * armorFactor();
            accumulatedDamage += actualDamage;

            float threshold = shatterThreshold();

            // 累积伤害超过阈值、且这一下够重，才掷骰子判定碎裂
            if (accumulatedDamage > threshold && damage > minTriggerDamage) {
                if (Mathf.chance(shatterChance(accumulatedDamage - threshold))) {
                    shatter();
                    return;
                }
            }

            super.damage(damage);
        }

        float shatterChance(float excessDamage) {
            // 概率随超出量平缓增长，并封顶
            return Math.min(maxShatterChance, baseShatterChance + Math.max(0f, excessDamage) * damageMultiplier);
        }

        void shatter() {
            // 碎裂效果
            Fx.breakProp.at(x, y);
            Sounds.acceleratorLightning2.at(tile);

            // 碎裂（直接摧毁）
            kill();

            // 创建碎片效果
            for (int i = 0; i < 8; i++) {
                float angle = Mathf.random(360f);
                float speed = Mathf.random(2f, 4f);
                Fx.casing4.at(x, y, angle, Color.grays(speed), Color.valueOf("e0f7ff"));
            }
        }

        @Override
        public void draw() {
            super.draw();

            // 显示累积伤害的视觉效果
            float threshold = shatterThreshold();
            if (accumulatedDamage > threshold * 0.5f) {
                // Mathf.clamp(单参数) 会把值夹到 [0,1]
                float damageRatio = Mathf.clamp(accumulatedDamage / (threshold * 2f));
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
