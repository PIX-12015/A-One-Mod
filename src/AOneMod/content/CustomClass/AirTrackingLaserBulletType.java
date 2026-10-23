package AOneMod.content.CustomClass;

import arc.util.Tmp;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.PointLaserBulletType;
import mindustry.gen.Bullet;

public class AirTrackingLaserBulletType extends PointLaserBulletType {

    public AirTrackingLaserBulletType() {
        super();
        // 不走普通碰撞系统，伤害完全由 update() 手动控制
        collides = false;
    }

    @Override
    public void update(Bullet b) {
        // 保留父类的轨迹/特效逻辑
        updateTrail(b);
        updateTrailEffects(b);
        updateBulletInterval(b);

        // 在目标点附近搜索飞行单位，直接施加持续伤害和状态效果
        // 与 TractorBeamTurret 相同：每帧调用 damageContinuous
        Units.nearbyEnemies(b.team, Tmp.r1.setCentered(b.aimX, b.aimY, 4f), u -> {
            if(!u.isFlying() || !u.hittable()) return;

            // 持续伤害（每帧，与牵引炮塔机制相同）
            if(damage > 0){
                u.damageContinuous(damage * b.damageMultiplier());
            }

            // 减速/状态效果（每帧刷新持续时间）
            if(status != StatusEffects.none){
                u.apply(status, statusDuration);
            }
        });

        // 命中特效
        if(b.timer.get(1, beamEffectInterval)){
            beamEffect.at(b.aimX, b.aimY, beamEffectSize * b.fslope(), hitColor);
        }

        if(shake > 0){
            Effect.shake(shake, shake, b);
        }
    }
}