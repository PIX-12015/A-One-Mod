package AOneMod.content.CustomClass;

import arc.util.Time;
import mindustry.ai.types.FlyingAI;
import mindustry.entities.Units;
import mindustry.gen.Groups;
import mindustry.gen.Healthc;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.liquid.Conduit;

/**
 * 蜂群自杀 AI。
 *
 * 关键点：不要去覆写 updateUnit()，而是覆写 findTarget()。
 * AIController.updateUnit() 在 retarget() 时会调用 findTarget() 来给 target 赋值，
 * 所以只要在这里接手，下面那套「按血量决定派几只蜂」的分配逻辑才会真正参与决策。
 */
public class SmartSuicideAI extends FlyingAI {

    /**
     * 母单位引用，由 LimitedUnitSpawnAbility 在产出时注入
     */
    public Unit parent;

    /**
     * 距母单位多近时停止跟随（世界单位）
     */
    public float followRadius = 60f;

    /**
     * 索敌半径（世界单位）。单位自身的 range 只有 40，蜂群会几乎撞不上敌人才开打，
     * 所以这里给一个保底半径。填 0 就退回"只用单位自身射程"。
     */
    public float searchRange = 240f;

    /**
     * 无攻击目标时的累计时间（ticks）
     */
    protected float idleTime = 0f;

    /**
     * 无目标多久后自杀（ticks，60f = 1秒）
     */
    public float maxIdleTime = 60f * 30f;

    /**
     * 根据目标当前血量，返回允许集中攻击该目标的最大子单位数量。
     * 血量太低的目标不值得浪费蜂群。
     */
    public static int maxUnitsForTarget(float health) {
        if (health <= 110f) return 0;
        if (health <= 135f) return 3;
        if (health <= 180f) return 4;
        if (health <= 225f) return 5;
        return 6;
    }

    /**
     * 统计当前正在攻击同一目标的同类子单位数量。
     * 只统计同一母单位下的蜂，不同蜂后的蜂群互不干扰。
     */
    public int countAttackingUnits(Teamc attackTarget) {
        int count = 0;
        for (Unit u : Groups.unit) {
            if (u.team == unit.team
                    && u.type == unit.type
                    && u.controller() instanceof SmartSuicideAI) {
                SmartSuicideAI ai = (SmartSuicideAI) u.controller();
                if (ai.target == attackTarget && ai.parent == this.parent) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 找到合适的目标：先找最近的，再按血量与已分配数量决定是否放弃。
     */
    protected Teamc findSuitableTarget(float x, float y, float range, boolean air, boolean ground) {
        float r = searchRange > 0f ? Math.max(range, searchRange) : range;

        Teamc candidate = Units.closestTarget(
                unit.team, x, y, r,
                u -> u.checkTarget(air, ground),
                t -> ground && !(t.block instanceof Conveyor || t.block instanceof Conduit)
        );

        if (candidate == null) return null;

        if (candidate instanceof Healthc) {
            Healthc h = (Healthc) candidate;

            int maxAllowed = maxUnitsForTarget(h.health());
            // 血量太低，不值得浪费蜂群
            if (maxAllowed <= 0) return null;

            // 已经够蜂在打了，换别的目标（返回 null 即本轮不参战）
            if (countAttackingUnits(candidate) >= maxAllowed) return null;
        }

        return candidate;
    }

    /**
     * 接管目标选取。返回 null 时 target 保持为空，
     * updateMovement() 会走"跟随母单位 / 空闲计时"分支。
     */
    @Override
    public Teamc findTarget(float x, float y, float range, boolean air, boolean ground) {
        return findSuitableTarget(x, y, range, air, ground);
    }

    @Override
    public void updateMovement() {
        // 清理失效的 parent 引用
        if (parent != null && !parent.isValid()) parent = null;

        if (target != null && unit.hasWeapons()) {
            // 有攻击目标：重置计时器，直接冲向目标
            idleTime = 0f;
            moveTo(target, 0f);
            unit.lookAt(target);
        } else {
            // 没有攻击目标：累计空闲时间
            idleTime += Time.delta;

            if (idleTime >= maxIdleTime) {
                unit.kill();
                return;
            }

            if (parent != null) {
                // 跟随母单位
                if (!unit.within(parent, followRadius)) {
                    moveTo(parent, followRadius);
                }
                unit.lookAt(parent);
            }
            // 没有 parent 时原地悬停（飞行单位默认行为）
        }
    }
}
