package AOneMod.content.CustomClass;

import arc.util.Nullable;
import mindustry.entities.pattern.ShootPattern;

/**
 * 一次扣扳机，多排发射管依次打完（每排左右各一枚），排与排之间用延迟拉开。
 *
 * 写法参照 ThisBlocks 里 bright 炮塔的 ShootPattern：
 *      shots = 3;  firstShotDelay = 12f;  shotDelay = 4.5f;
 * 即「一次扣扳机 = 打完一整轮」，靠 delay 制造连发节奏，而不是靠多次扣扳机。
 *
 * ────────────────────────────────────────────────────────
 * 【为什么要这么改】
 * 旧版是「一次扣扳机只打一排」，排号由 barrelCounter 轮流。
 * 手操时问题很明显：第一次点击只飞出最前排两枚，必须连点三次才打完一轮；
 * 而且停下来歇一会儿再点，还是从"上次那一排"接着走，手感很怪。
 *
 * 改成 bright 这种方式后：点一下 = 三排全打完，之后进入 Weapon.reload 冷却。
 * 手操只需要点一次，剩下的由延迟自动播完。
 *
 * ────────────────────────────────────────────────────────
 * 【"射完三排停一下"怎么实现】
 * 停顿不再由 pattern 管，直接由 Weapon 的 reload 决定：
 *
 *     停顿时长 = reload - 整轮齐射耗时
 *     整轮齐射耗时 = rowDelay × (排数 - 1) + sideDelay
 *
 * 例：rows=3, rowDelay=8, sideDelay=3 → 齐射耗时 19 tick
 *     reload=60 → 停顿 41 tick ≈ 0.68 秒
 *
 * ⚠️ reload 必须大于齐射耗时，否则上一轮还没打完就被下一次扳机打断
 *    （实际表现是最后一两排经常发不出来）。
 */
public class SequentialRowPattern extends ShootPattern{
    /** 每一排沿单位朝向的位置偏移，正数 = 更靠前。数组长度 = 排数 */
    public float[] rowOffsets = {9f, 3f, -3f};

    /** 同一排左右两枚的横向间距 */
    public float sideOffset = 8f;

    /** 排与排之间的发射间隔（tick）—— 这就是"连发"的节奏 */
    public float rowDelay = 8f;

    /** 同一排左右两枚之间的发射间隔（tick） */
    public float sideDelay = 3f;

    public SequentialRowPattern(float[] rowOffsets, float sideOffset, float rowDelay, float sideDelay){
        this.rowOffsets = rowOffsets;
        this.sideOffset = sideOffset;
        this.rowDelay = rowDelay;
        this.sideDelay = sideDelay;
    }

    public SequentialRowPattern(){
    }

    @Override
    public void shoot(int totalShots, BulletHandler handler, @Nullable Runnable barrelIncrementer){
        int rows = rowOffsets.length;
        if(rows == 0) return;

        // 一次扣扳机，把所有排全打出去，靠 delay 制造依次发射的效果
        for(int i = 0; i < rows; i++){
            float y = rowOffsets[i];
            float baseDelay = firstShotDelay + rowDelay * i;

            // y 会被武器朝向变换，所以正数是"往前"
            handler.shoot(-sideOffset, y, 0f, baseDelay);
            handler.shoot( sideOffset, y, 0f, baseDelay + sideDelay);
        }

        // 整轮算一次，推进一格即可
        if(barrelIncrementer != null) barrelIncrementer.run();
    }
}
