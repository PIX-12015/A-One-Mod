package AOneMod.content;

import arc.struct.Seq;
import mindustry.content.*;
import mindustry.content.TechTree.TechNode;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Objectives;
import mindustry.type.ItemStack;

public class ThisTechTree {
    private static TechNode context = null;
    public static Seq<TechNode> all = new Seq();
    public static Seq<TechNode> roots = new Seq();

    public static void load(){
        // ===== 制造链：强力压制机 → 铸钉厂 / 炼焦炉 =====
        addToNext(Blocks.graphitePress, ()->{
            node(ThisBlocks.strongPress, Seq.with(new Objectives.Objective[]{new Objectives.Research(ThisItems.iron)}),() -> {
                node(ThisBlocks.Processing_Factory, () -> {
                    node(ThisBlocks.cokeOven);
                });
            });
        });

        // ===== 物品生产链（可视化）=====
        addToNext(Items.copper, () -> {
            nodeProduce(ThisItems.iron, () -> {
                nodeProduce(ThisItems.ironPlate, () -> {});
                nodeProduce(ThisItems.nails, () -> {});
            });
        });

        // ===== 煤 → 焦炭；煤 → 钻石 → 三级钻石墙 =====
        addToNext(Items.coal, () -> {
            nodeProduce(ThisItems.coke);

            nodeProduce(ThisItems.diamond, () -> {
                node(ThisBlocks.diamondWall, () -> {
                    node(ThisBlocks.diamondWallLarge, () -> {
                        node(ThisBlocks.diamondWallHuge);
                    });
                });
            });
        });

        // ===== 炮塔：bright → 钉枪 / 三叉戟 =====
        addToNext(Blocks.salvo, () -> {
            node(ThisBlocks.bright, () -> {
                node(ThisBlocks.nailGun, () -> {});
                node(ThisBlocks.trident);
            });
        });

        // ===== 效果：原版超频穹顶 → 终极超速穹顶 =====
        addToNext(Blocks.overdriveDome, () -> {
            node(ThisBlocks.superOverdriveDome);
        });

        // ===== 单位：地面 / 空中 =====
        /*addToNext(Blocks.groundFactory, () -> {
            node(ThisUnitTypes.tianBing);
        });*/

        addToNext(Blocks.airFactory, () -> {
            node(ThisUnitTypes.queenBee, () -> {
                node(ThisUnitTypes.sting);
            });
        });

        // ===== 区块 =====
        addToNext(SectorPresets.groundZero, () -> {
            node(ThisSector.Area1, Seq.with(new Objectives.Objective[]{new Objectives.SectorComplete(SectorPresets.groundZero)}),() -> {
                node(ThisSector.LoginPoint1);
            });
        });
    }

    public static void addToNext(UnlockableContent content, Runnable run){
        context = TechTree.all.find(t -> t.content == content);
        run.run();
    }



    public static TechNode nodeRoot(String name, UnlockableContent content, Runnable children) {
        return nodeRoot(name, content, false, children);
    }

    public static TechNode nodeRoot(String name, UnlockableContent content, boolean requireUnlock, Runnable children) {
        TechNode root = node(content, content.researchRequirements(), children);
        root.name = name;
        root.requiresUnlock = requireUnlock;
        roots.add(root);
        return root;
    }

    public static TechNode node(UnlockableContent content, Runnable children) {
        return node(content, content.researchRequirements(), children);
    }

    public static TechNode node(UnlockableContent content, ItemStack[] requirements, Runnable children) {
        return node(content, requirements, (Seq)null, children);
    }

    public static TechNode node(UnlockableContent content, ItemStack[] requirements, Seq<Objectives.Objective> objectives, Runnable children) {
        TechNode node = new TechNode(context, content, requirements);
        if (objectives != null) {
            node.objectives.addAll(objectives);
        }

        TechNode prev = context;
        context = node;
        children.run();
        context = prev;
        return node;
    }

    public static TechNode node(UnlockableContent content, Seq<Objectives.Objective> objectives, Runnable children) {
        return node(content, content.researchRequirements(), objectives, children);
    }

    public static TechNode node(UnlockableContent block) {
        return node(block, () -> {
        });
    }

    public static TechNode nodeProduce(UnlockableContent content, Seq<Objectives.Objective> objectives, Runnable children) {
        return node(content, content.researchRequirements(), objectives.add(new Objectives.Produce(content)), children);
    }

    public static TechNode nodeProduce(UnlockableContent content, Runnable children) {
        return nodeProduce(content, new Seq(), children);
    }

    public static TechNode nodeProduce(UnlockableContent content) {
        return nodeProduce(content, () -> {});
    }

}
