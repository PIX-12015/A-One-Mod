package AOneMod.content.CustomClass;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.Units;
import mindustry.entities.abilities.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.type.*;

import static mindustry.Vars.*;

public class LimitedUnitSpawnAbility extends UnitSpawnAbility {
    public int maxUnits = 6;

    /** 产出时子单位的圆形散布半径（避免重叠） */
    public float spawnRadius = 16f;

    public LimitedUnitSpawnAbility(UnitType unit, float spawnTime,
                                   float spawnX, float spawnY, int maxUnits) {
        super(unit, spawnTime, spawnX, spawnY);
        this.maxUnits = maxUnits;
    }

    protected int countAlive(Unit parentUnit) {
        int count = 0;
        for (Unit u : Groups.unit) {
            if (u.team == parentUnit.team
                    && u.type == this.unit
                    && u.controller() instanceof AOneMod.content.CustomClass.SmartSuicideAI) {
                AOneMod.content.CustomClass.SmartSuicideAI ai = (AOneMod.content.CustomClass.SmartSuicideAI) u.controller();
                if (ai.parent == parentUnit) count++;
            }
        }
        return count;
    }

    @Override
    public void update(Unit parent) {
        timer += Time.delta * state.rules.unitBuildSpeed(parent.team);

        // 触发条件：计时器到达 && 所有子单位都已死亡
        if (timer >= spawnTime && countAlive(parent) == 0) {
            for (int i = 0; i < maxUnits; i++) {
                // 以圆形排列产出，避免重叠
                float angle = 360f / maxUnits * i;
                float baseX = parent.x + Angles.trnsx(parent.rotation, spawnY, -spawnX);
                float baseY = parent.y + Angles.trnsy(parent.rotation, spawnY, -spawnX);
                float x = baseX + Mathf.cosDeg(angle) * spawnRadius;
                float y = baseY + Mathf.sinDeg(angle) * spawnRadius;

                spawnEffect.at(x, y, 0f, parentizeEffects ? parent : null);
                Unit u = this.unit.create(parent.team);
                u.set(x, y);
                u.rotation = parent.rotation;

                if (u.controller() instanceof AOneMod.content.CustomClass.SmartSuicideAI) {
                    ((AOneMod.content.CustomClass.SmartSuicideAI) u.controller()).parent = parent;
                }

                Events.fire(new UnitCreateEvent(u, null, parent));
                if (!net.client()) {
                    u.add();
                    Units.notifyUnitSpawn(u);
                }
            }
            timer = 0f;
        }
    }
}