package AOneMod.content;

import AOneMod.content.CustomClass.SequentialRowPattern;
import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.ExplosionBulletType;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.unit.MissileUnitType;
import mindustry.world.blocks.units.UnitFactory;

import static mindustry.type.ItemStack.with;

public class ThisUnitTypes {
    public static UnitType
            sting, queenBee;
    public static void load (){
        /*tianBing = new UnitType("tian-bing"){{
            constructor = MechUnit::create;

            health = 2200f;
            armor = 6f;
            hitSize = 15f;
            speed = 0.9f;
            accel = 0.5f;
            drag = 0.3f;
            rotateSpeed = 1.2f;
            deathShake = -1f;
            aimDst = 4f;
            range = 264f;

            flying = false;
            canBoost = true;
            targetAir = true;
            targetGround = true;
            omniMovement = true;

            abilities.add(new ShieldArcAbility(){{
                region = "TB-shield";
                radius = 45f;
                angle = 82f;
                regen = 10f / 2f;
                cooldown = 50f / 0.5f;
                max = 1200f;
                y = -20f;
                width = 8f;
                whenShooting = false;
                chanceDeflect = 0.8f;
            }});

            weapons.add(new Weapon("TB-weapon"){{
                shootSound = Sounds.shootStell;
                layerOffset = 0.0001f;
                reload = 30f;
                shootY = 4.5f;
                recoil = 2f;
                rotateSpeed = 2.2f;
                rotate = true;
                mirror = false;
                x = 0f;
                y = 0.85f;
                heatColor = Color.valueOf("D8D8D8");
                cooldownTime = 30f;

                bullet = new BasicBulletType(){{
                    damage = 70;
                    splashDamage = 45;
                    splashDamageRadius = 20f;
                    speed = 5.5f;
                    lifetime = 56;
                    width = 16;
                    height = 16;
                    hitEffect = Fx.fireHit;
                    despawnEffect = Fx.blastExplosion;
                    ammoMultiplier = 0.8f;
                    collidesAir = false;
                    collidesGround = true;
                }};
            }});

            weapons.add(new Weapon("TB-weapon2"){{
                continuous = true;       // 持续激光模式
                autoTarget = true;       // 自动寻找目标
                controllable = false;    // AI 控制（改为 true 则玩家可手动瞄准）
                rotate = true;
                rotateSpeed = 10f;
                reload = 120f;
                cooldownTime = reload;

                // 对空对地都能锁定目标（但激光只对飞行单位有伤害）
                targetAir = true;
                targetGround = true;

                bullet = new AirTrackingLaserBulletType(){{
                    // damage 是每帧伤害量（×60 = 每秒伤害）
                    // 与 TractorBeamTurret 的 damage 字段语义相同
                    damage = 2f;       // 每帧 0.5，即每秒约 30 伤害

                    rangeOverride = 230f;
                    // 减速效果
                    status = StatusEffects.slow;
                    statusDuration = 60f;

                    lifetime = 180f; // 激光持续时间（与 reload 匹配）

                    color = Color.valueOf("#84EBFF");  // 激光颜色
//                    sprite = "point-laser"; // 激光贴图（可替换为自定义贴图）
                }};
            }});

        }};*/

        // ===== 导弹（原「蜂」，改为自杀式导弹）=====
        // 对照官方「天灾」(anthicus) 的 anthicus-missile 写法。
        sting = new MissileUnitType("sting"){{
            // MissileUnitType 的构造函数已经帮我们设好了这些，不用再写：
            //   flying = true
            //   constructor = TimedKillUnit::create   → 寿命到了自动炸
            //   controller = MissileAI                → 自动索敌并撞击
            //   hidden = true                         → 数据库里不显示
            //   useUnitCap = false                    → 不占单位上限，可以随便发
            //   createWreck = false / createScorch = false
            //   trailLength = 7, speed = 4f, lifetime = 102f（下面会覆盖）

            speed = 3.6f;                  // 导弹飞行速度
            lifetime = 60f * 2.2f;         // 飞 2.2 秒还没撞上就自爆，不会永远飞
            health = 35;                   // 可以被防空打掉
            hitSize = 5f;
            rotateSpeed = 3f;
            maxRange = 6f;

            lowAltitude = true;            // 低空，有地面阴影更好看

            // 尾焰
            engineColor = trailColor = Color.valueOf("ffd27f");
            engineSize = 1.9f;
            engineLayer = Layer.effect;
            outlineColor = Pal.darkOutline;

            weapons.add(new Weapon(){{
                shootSound = Sounds.none;
                shootCone = 360f;
                mirror = false;
                reload = 1f;
                shootOnDeath = true;                     // 撞上目标 / 寿命到 → 起爆
                shootOnDeathEffect = Fx.massiveExplosion;
                bullet = new ExplosionBulletType(62f, 34f); // 爆炸伤害 62，半径 34
            }});
        }};

        queenBee = new UnitType("queen-bee"){{
            constructor = UnitEntity::create;
            flying = true;
            lowAltitude = true;

            // ===== 生存属性 =====
            health = 1300f;
            armor = 4f;
            hitSize = 20f;

            // ===== 飞行参数 =====
            speed = 1.15f;
            accel = 0.08f;
            drag = 0.06f;
            rotateSpeed = 1.6f;

            // ===== 引擎特效 =====
            engineOffset = 12f;
            engineSize = 4.2f;

            // ===== 索敌 =====
            range = 260f;
            targetAir = true;
            targetGround = true;
            fogRadius = 26f;

            /*// ===== 自卫武器：尾针 =====
            weapons.add(new Weapon(){{
                reload = 45f;
                x = 8f;
                y = -1f;
                shootY = 3f;
                rotate = true;
                rotateSpeed = 4f;
                mirror = true;
                inaccuracy = 5f;
                recoil = 1.6f;
                bullet = new BasicBulletType(6.5f, 26){{
                    width = 5f;
                    height = 9f;
                    lifetime = 40f;
                    ammoMultiplier = 1f;
                    shootEffect = Fx.shootBig;
                    hitEffect = Fx.hitBulletColor;
                    despawnEffect = Fx.hitBulletColor;
                    frontColor = Pal.scrapAmmoFront;
                    backColor = Pal.scrapAmmoBack;
                }};
            }});*/

            // ===== 三排毒刺导弹发射器（一次扣扳机打完一轮）=====
            weapons.add(new Weapon("missile-launcher"){{
                // ⚠️ 必须大于整轮齐射耗时，否则最后一排会被下一轮打断打不出来
                // 当前齐射耗时 = rowDelay×(3-1) + sideDelay = 8×2 + 3 = 19 tick
                // 所以 60 - 19 = 41 tick ≈ 0.68 秒的装填停顿
                reload = 60f;
                x = 0f;
                y = 2f;                         // 发射器本体位置，pattern 的排偏移会叠加上去
                shootY = 6f;
                rotate = false;
                mirror = false;                 // 左右两枚由 pattern 自己发，不用镜像
                inaccuracy = 6f;
                recoil = 2f;
                recoils = 1;
                shootSound = Sounds.shootMissileLarge;
                shootSoundVolume = 0.6f;

                // 点一下 → 第1排 → 第2排 → 第3排 依次飞出（每排左右各一枚），然后进冷却
                shoot = new SequentialRowPattern(
                        new float[]{9f, 3f, -3f},  // 三排沿朝向的位置（正数=更靠前）
                        8f,                        // 同一排左右两枚的间距
                        8f,                        // 排与排之间的间隔（tick）
                        3f                         // 同一排左右两枚的间隔（tick）
                );

                bullet = new BulletType(){{
                    // 这颗子弹只是个载体：speed=0 让它在原地立刻生成导弹单位，
                    // 真正的飞行和伤害都由 MissileUnitType 负责。
                    speed = 0f;
                    keepVelocity = false;
                    spawnUnit = sting;
                }};
            }});
        }};
    }
}
