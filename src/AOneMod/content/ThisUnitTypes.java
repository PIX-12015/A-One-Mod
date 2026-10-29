package AOneMod.content;

import AOneMod.content.CustomClass.AirTrackingLaserBulletType;
import AOneMod.content.CustomClass.LimitedUnitSpawnAbility;
import AOneMod.content.CustomClass.SmartSuicideAI;
import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.abilities.ShieldArcAbility;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.MechUnit;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class ThisUnitTypes {
    public static UnitType
            bee, queenBee;
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

        bee = new UnitType("bee"){{
            aiController = SmartSuicideAI::new;
            constructor = UnitEntity::create;
            flying = true;
            createWreck = false;

            speed = 2.4f;
            hitSize = 6f;
            health = 80f;
            range = 40f;

            weapons.add(new Weapon(){{
                shootOnDeath = true;   // 死亡时触发武器（自杀爆炸）
                targetUnderBlocks = false;
                reload = 24f;
                shootCone = 180f;
                ejectEffect = Fx.none;
                x = shootY = 0f;
                mirror = false;
                bullet = new BulletType(){{
                    collidesTiles = false;
                    collides = false;
                    scaledSplashDamage = true;
                    rangeOverride = 25f;
                    hitEffect = Fx.pulverize;
                    speed = 0f;
                    splashDamageRadius = 30f;
                    instantDisappear = true;
                    splashDamage = 50f;   // 爆炸伤害 45
                    killShooter = true;   // 子弹杀死射击者（自杀）
                    hittable = false;
                    collidesAir = true;
                }};
            }});
        }};

        queenBee = new UnitType("queen-bee"){{
            constructor = UnitEntity::create;
            flying = true;

            health = 1300f;
            targetAir = true;
            targetGround = true;

            // 每 30 秒产出一个子单位，最多同时存活 6 个
            abilities.add(new LimitedUnitSpawnAbility(bee, 60f * 17f, 0f, 0f, 6));
        }};

    }
}
