package AOneMod.content.CustomClass;

import arc.math.geom.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.core.World;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class SmartSuicideAI extends FlyingAI {

    static boolean blockedByBlock;

    /**
     * 母单位引用，由 LimitedUnitSpawnAbility 在产出时注入
     */
    public Unit parent;

    /**
     * 距母单位多近时停止跟随（世界单位）
     */
    public float followRadius = 60f;

    /**
     * 无攻击目标时的累计时间（ticks）
     */
    protected float idleTime = 0f;

    /**
     * 无目标多久后自杀（ticks，60f = 1秒）
     */
    public float maxIdleTime = 60f * 30f;

    /**
     * 根据目标当前血量，返回允许集中攻击该目标的最大子单位数量
     */
    public static int maxUnitsForTarget(float health) {
        if (health <= 110f) return 0;
        if (health <= 135f) return 3;
        if (health <= 180f) return 4;
        if (health <= 225f) return 5;
        return 6;
    }

    /**
     * 统计当前正在攻击同一目标的同类子单位数量
     */
    public int countAttackingUnits(Teamc attackTarget) {
        int count = 0;
        for (Unit u : Groups.unit) {
            if (u.team == unit.team
                    && u.type == unit.type
                    && u.controller() instanceof SmartSuicideAI) {
                SmartSuicideAI ai = (SmartSuicideAI) u.controller();
                // 只统计同一母单位的子单位
                if (ai.target == attackTarget && ai.parent == this.parent) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 找到合适的目标（考虑血量阈值和分配数量）
     */
    protected Teamc findSuitableTarget() {
        Teamc candidate = Units.closestTarget(
                unit.team, unit.x, unit.y, unit.range(),
                u -> u.checkTarget(unit.type.targetAir, unit.type.targetGround),
                t -> unit.type.targetGround && !(t.block instanceof Conveyor || t.block instanceof Conduit)
        );

        if (candidate == null) return null;

        if (candidate instanceof Healthc) {
            Healthc h = (Healthc) candidate;
            int maxAllowed = maxUnitsForTarget(h.health());
            if (maxAllowed <= 0) return null;

            int currentCount = countAttackingUnits(candidate);
            if (currentCount >= maxAllowed) return null;
        }

        return candidate;
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


//    @Override
//    public void updateUnit() {
//        // 清理失效的 parent 引用
//        if (parent != null && !parent.isValid()) parent = null;
//
//        if (Units.invalidateTarget(target, unit.team, unit.x, unit.y, Float.MAX_VALUE)) {
//            target = null;
//        }
//
//        if (retarget()) {
//            target = findSuitableTarget();
//        }
//
//        Building core = unit.closestEnemyCore();
//        boolean rotate = false, shoot = false, moveToTarget = false;
//
//        // 只有在没有 parent 时才回退到核心
//        if (target == null && parent == null) {
//            target = core;
//        }
//
//        if (!Units.invalidateTarget(target, unit, unit.range()) && unit.hasWeapons()) {
//            rotate = true;
//
//            float targetSize;
//            if (target instanceof Building) {
//                targetSize = ((Building) target).block.size * Vars.tilesize / 2f;
//            } else {
//                targetSize = ((Hitboxc) target).hitSize() / 2f;
//            }
//            shoot = unit.within(target, unit.type.weapons.first().bullet.range + targetSize);
//
//            boolean skipMove = false;
//            if (target instanceof Building) {
//                Building build = (Building) target;
//                if (!(build.block instanceof CoreBlock) && (
//                        build.block.group == BlockGroup.walls ||
//                                build.block.group == BlockGroup.liquids ||
//                                build.block.group == BlockGroup.transportation
//                )) {
//                    skipMove = true;
//                }
//            }
//
//            if (!skipMove) {
//                blockedByBlock = false;
//
//                boolean blocked = World.raycast(unit.tileX(), unit.tileY(), target.tileX(), target.tileY(), (x, y) -> {
//                    for (Point2 p : Geometry.d4c) {
//                        Tile tile = Vars.world.tile(x + p.x, y + p.y);
//                        if (tile != null && tile.build == target) return false;
//                        if (tile != null && tile.build != null && tile.build.team != unit.team()) {
//                            blockedByBlock = true;
//                            return true;
//                        } else {
//                            return tile == null || tile.solid();
//                        }
//                    }
//                    return false;
//                });
//
//                if (blockedByBlock) shoot = true;
//
//                if (!blocked) {
//                    moveToTarget = true;
//                    unit.movePref(vec.set(target).sub(unit).limit(unit.speed()));
//                }
//            }
//        }
//
//        if (!moveToTarget) {
//            if (target != null) {
//                // 有目标但被阻挡，重置空闲计时器
//                idleTime = 0f;
//            } else {
//                // 没有攻击目标：累计空闲时间
//                idleTime += Time.delta;
//
//                if (idleTime >= maxIdleTime) {
//                    // 超过30秒无目标，自杀（触发 shootOnDeath 武器）
//                    unit.kill();
//                    return;
//                }
//
//                if (parent != null) {
//                    // 跟随母单位
//                    if (!unit.within(parent, followRadius)) {
//                        unit.movePref(vec.set(parent).sub(unit).limit(unit.speed()));
//                    }
//                } else {
//                    // 没有母单位，使用寻路
//                    boolean move = true;
//                    if (core == null && state.rules.waves && unit.team == state.rules.defaultTeam) {
//                        Tile spawner = getClosestSpawner();
//                        if (spawner != null && unit.within(spawner, state.rules.dropZoneRadius + 120f)) {
//                            move = false;
//                        }
//                    }
//                    if (move) pathfind(Pathfinder.fieldCore);
//                }
//            }
//        } else {
//            // 正在移动到目标，重置空闲计时器
//            idleTime = 0f;
//        }
//
//        unit.controlWeapons(rotate, shoot);
//        faceTarget();
//    }
//}