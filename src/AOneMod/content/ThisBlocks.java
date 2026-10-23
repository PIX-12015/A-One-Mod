package AOneMod.content;

import AOneMod.content.CustomClass.MultiRecipeCrafter;
import AOneMod.content.CustomClass.ThisDiamondWall;
import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.pattern.ShootPattern;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.OverdriveProjector;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.production.GenericCrafter;

import static mindustry.type.ItemStack.with;

public class ThisBlocks {
    public static Block
            //铁矿产线 工厂
            strongPress, Processing_Factory, cokeOven,
            //辅助
            superOverdriveDome,
            //矿石
            ironOre, diamondOre,
            //炮塔
            trident, nailGun, bright,
            //墙
            diamondWall,
            diamondWallLarge,
            diamondWallHuge;
    public static void load () {
/*        strongPress = new GenericCrafter("strongPress") {{          //铜100，铅30，铁20
            requirements(Category.crafting, with(Items.copper, 100, Items.lead, 30, ThisItems.iron, 10));

            health = 660;
            alwaysUnlocked = true;
            craftEffect = Fx.pulverizeMedium;
            size = 3;

            outputItem = new ItemStack(ThisItems.ironPlate, 2);
            consumeItem(ThisItems.iron, 2);

            hasItems = true;
            craftTime = 40f;

            outputItem = new ItemStack(ThisItems.diamond, 2);
            consumeItem(Items.coal, 4);

            hasItems = true;
            craftTime = 55f;
        }};

 */
        strongPress = new MultiRecipeCrafter("strongPress") {{
            requirements(Category.crafting, with(Items.titanium, 90, Items.lead, 60, Items.graphite, 20));
            health = 660;
            alwaysUnlocked = true;
            craftEffect = Fx.pulverizeMedium;
            size = 3;

            recipes = new MultiRecipeCrafter.Recipe[]{
                    // 配方1：铁×3 → 铁板×3
                    new MultiRecipeCrafter.Recipe(
                            new ItemStack[]{new ItemStack(ThisItems.iron, 3)},
                            new ItemStack[]{new ItemStack(ThisItems.ironPlate, 3)},
                            40f
                    ),
                    // 配方2：煤炭×4 → 钻石×2
                    new MultiRecipeCrafter.Recipe(
                            new ItemStack[]{new ItemStack(Items.coal, 4)},
                            new ItemStack[]{new ItemStack(ThisItems.diamond, 2)},
                            75f
                    )
            };
        }};

        Processing_Factory = new GenericCrafter("processing_factory") {{
            requirements(Category.crafting, with(Items.copper, 100, Items.lead, 30, ThisItems.ironPlate, 20));

            health = 360;
            alwaysUnlocked = true;
            craftEffect = Fx.blockCrash;

            outputItem = new ItemStack(ThisItems.nails, 6);
            consumeItem(ThisItems.ironPlate, 1);

            size = 2;
            hasItems = true;
            craftTime = 110f;
        }};

        cokeOven = new GenericCrafter("coke_oven") {{
            requirements(Category.crafting, with(Items.copper, 80, Items.titanium, 40, Items.silicon, 50));
            consumePower(7f);
            health = 450;
            alwaysUnlocked = true;
            craftEffect = Fx.blockCrash;

            outputItem = new ItemStack(ThisItems.coke, 3);
            consumeItem(Items.coal, 4);

            size = 2;
            hasItems = true;
            craftTime = 80f;
        }};

        superOverdriveDome = new OverdriveProjector("super-overdrive-dome"){{
            requirements(Category.effect, with(Items.lead, 200, Items.titanium, 130, Items.silicon, 130, Items.plastanium, 80, Items.surgeAlloy, 120));
            consumePower(11.66666666666666f);
            size = 3;
            range = 280f;
            speedBoost = 3.5f;
            useTime = 300f;
            ambientSoundVolume = 0.12f;
            hasBoost = false;
            consumeItems(with(Items.phaseFabric, 1, Items.silicon, 1));
        }};


        diamondWall = new ThisDiamondWall("diamond-wall") {{
            requirements(Category.defense, with(ThisItems.diamond, 6));
            health = 900;
            size = 1;

            armor = 6f;                      // 高护甲，模拟钻石硬度
            chanceDeflect = 0.3f;            // 30%概率偏转子弹
            flashHit = true;                 // 被击中时闪光
            flashColor = Color.valueOf("e0f7ff"); // 钻石光泽色
            deflectSound = Sounds.acceleratorLightning2;
        }};

        diamondWallLarge = new ThisDiamondWall("diamond-wall-large") {{
            requirements(Category.defense, with(ThisItems.diamond, 24));
            health = 3600;
            size = 2;

            armor = 10f;
            chanceDeflect = 0.5f;            // 更大的偏转概率
            flashHit = true;
            flashColor = Color.valueOf("e0f7ff");
            deflectSound = Sounds.acceleratorLightning2;
//            buildCostMultiplier = 4f;
        }};

        diamondWallHuge = new ThisDiamondWall("diamond-wall-huge") {{
            requirements(Category.defense, with(ThisItems.diamond, 54));
            health = 8100;
            size = 3;

            armor = 14f;                    // 高护甲，模拟钻石硬度
            chanceDeflect = 0.7f;            // 70%概率偏转子弹
            flashHit = true;                 // 被击中时闪光
            flashColor = Color.valueOf("e0f7ff"); // 钻石光泽色
            deflectSound = Sounds.acceleratorLightning2;
        }};

        //矿
        ironOre = new OreBlock(ThisItems.iron) {{
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.45678f;
        }};

        diamondOre = new OreBlock(ThisItems.diamond) {{
            oreDefault = true;
            oreThreshold = 0.9f;
            oreScale = 24.45678f;
        }};

        //炮塔
        nailGun = new ItemTurret("nailGun") {{
            health = 630;
            reload = 5f;
            range = 280f;
            maxAmmo = 100;
            inaccuracy = 3f;
            rotateSpeed = 4.3f;
            targetGround = true;
            targetAir = true;
            alwaysUnlocked = true;
            requirements(Category.turret, with(ThisItems.ironPlate, 80, Items.copper, 20, Items.titanium, 40));
            ammo(
                    ThisItems.nails, new BasicBulletType(7f, 13) {{

                        width = 4f;
                        height = 7f;
                        lifetime = 40f;
                        ammoMultiplier = 2;
                        reloadMultiplier = 10f;
                        shootEffect = Fx.shootBig;
                    }}
            );
        }};

        bright = new ItemTurret("bright") {{
            health = 570;
            inaccuracy = 2f;
            size = 2;
            maxAmmo = 70;
            rotateSpeed = 4f;
            range = 224f;
            reload = 17f;
            alwaysUnlocked = true;
            requirements(Category.turret, with(Items.titanium, 160, Items.lead, 30, Items.graphite, 50, Items.silicon, 10));
            ammo(
                    ThisItems.diamond, new BasicBulletType(5.6f, 35) {{
                        shoot = new ShootPattern(){{
                            shots = 3;          // 每次触发发射的子弹数
                            firstShotDelay = 12f; // 第一发的延迟（ticks）
                            shotDelay = 4.5f;      // 每发之间的延迟（ticks）
                        }};

                        pierceArmor = true;         // 可穿透装甲单位
                        pierceCap = 2;              // 最多穿透3个目标
                        pierceDamageFactor = 0.5f;

                        //子弹存在时间
                        lifetime = 47f;
                        shootEffect = Fx.shootBig;
                        //以下两项为子弹的长和宽
                        width = 8f;
                        height = 8f;

                        hitEffect = Fx.flakExplosion;
                        frontColor = Pal.scrapAmmoFront;
                        backColor = hitColor = Pal.scrapAmmoBack;
                        despawnEffect = Fx.hitBulletColor;
                    }}
            );
        }};

        trident = new ItemTurret("trident") {{
            health = 1060;
            inaccuracy = 5f;
            size = 3;
            maxAmmo = 80;
            coolantMultiplier = 2;
            rotateSpeed = 1.5f;
            range = 304f;
            reload = 19f;
            alwaysUnlocked = true;
            requirements(Category.turret, with(ThisItems.iron, 140, Items.lead, 30, ThisItems.ironPlate, 20));
            ammo(
                    Items.silicon, new BasicBulletType(5f, 35) {{
                        //子弹存在时间
                        lifetime = 60f;
                        //装填倍率
                        ammoMultiplier = 3f;
                        shootEffect = Fx.shootBig;
                        //射击速度
                        reloadMultiplier = 1f;
                        //以下两项为子弹的长和宽
                        width = 9f;
                        height = 8f;

                        homingPower = 0.09f;
                        homingRange = 55f;

                        hitEffect = Fx.flakExplosion;
                        frontColor = Pal.scrapAmmoFront;
                        backColor = hitColor = Pal.scrapAmmoBack;
                        despawnEffect = Fx.hitBulletColor;
                    }},

                    Items.blastCompound, new BasicBulletType(4f, 65) {{
                        lifetime = 87f;
                        //装填倍率
                        ammoMultiplier = 1f;
                        shootEffect = Fx.shootBig;
                        //射击速度
                        reloadMultiplier = 0.6f;
                        rangeChange = 55f;
                        //以下两项为子弹的长和宽
                        width = 14f;
                        height = 14f;
                        splashDamage = 60;
                        splashDamageRadius = 30f * 0.75f;
                    }}
            );

            super.init();
        }};

//        @Override
//        public void shoot ( int barrelCounter, ShootBarrel.ShootConsumer consumer, Runnable barrelIncrement){
//            consumer.accept(-4f, 0f, 0f, 0f, null);
//            consumer.accept(0f, 0f, 0f, 0f, null);
//            consumer.accept(4f, 0f, 0f, 0f, null);
//            barrelIncrement.run();
//        }



//        trident = new ItemTurret("trident"){{
//            health = 1060;
//            inaccuracy = 8f;
//            size = 3;
//            maxAmmo = 80;
//            coolantMultiplier = 2;
//            rotateSpeed = 1.5f;
//            range = 304f;
//            reload = 19f;
//            alwaysUnlocked = true;
//            requirements(Category.turret, with(ThisItems.iron,140, Items.lead,30, ThisItems.ironPlate,20));
//            ammo(
//                    Items.graphite, new BasicBulletType(5f, 35){{
//                        //子弹存在时间
//                        lifetime = 60f;
//                        //装填倍率
//                        ammoMultiplier = 3f;
//                        shootEffect = Fx.shootBig;
//                        //射击速度
//                        reloadMultiplier = 1f;
////这个是；该子弹增加的射程                        rangeChange = 90f;
//                        //以下两项为子弹的长和宽
//                        width = 9f;
//                        height = 8f;
//
//                        homingPower = 0.09f;
//                        homingRange = 55f;
//
//                        hitEffect = Fx.flakExplosion;
//                        frontColor = Pal.scrapAmmoFront;
//                        backColor = hitColor = Pal.scrapAmmoBack;
//                        despawnEffect = Fx.hitBulletColor;
//                    }},
//
//                    Items.blastCompound, new BasicBulletType(4f, 65){{
//                        lifetime = 87f;
//                        //装填倍率
//                        ammoMultiplier = 1f;
//                        shootEffect = Fx.shootBig;
//                        //射击速度
//                        reloadMultiplier = 0.6f;
//                        rangeChange = 55f;
//                        //以下两项为子弹的长和宽
//                        width = 14f;
//                        height = 14f;
//                        splashDamage = 60;
//                        splashDamageRadius = 30f * 0.75f;
//
//                    }}
//
//            );
//        }};

    }
}
