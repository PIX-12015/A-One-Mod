package AOneMod.content.CustomClass;

import arc.math.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;

import static mindustry.Vars.*;

public class MultiRecipeCrafter extends GenericCrafter {

    public Recipe[] recipes = {};

    public MultiRecipeCrafter(String name) {
        super(name);
        configurable = true;
        saveConfig = true;

        config(Integer.class, (MultiRecipeCrafterBuild build, Integer value) -> {
            if (build.currentRecipe != value) {
                build.currentRecipe = value;
                build.progress = 0f;
            }
        });
    }

    @Override
    public void init() {
        super.init();
        // 手动设置 itemFilter，让工厂接受所有配方的输入物品
        for (Recipe r : recipes) {
            for (ItemStack stack : r.inputs) {
                itemFilter[stack.item.id] = true;
            }
        }
        acceptsItems = true;
    }

    @Override
    public boolean outputsItems() {
        return recipes.length > 0;
    }

    public class MultiRecipeCrafterBuild extends GenericCrafterBuild {
        public int currentRecipe = 0;

        public Recipe getRecipe() {
            if (recipes.length == 0) return null;
            return recipes[currentRecipe];
        }

        @Override
        public void buildConfiguration(Table table) {
            table.background(Styles.black6);

            ButtonGroup<ImageButton> group = new ButtonGroup<>();
            group.setMinCheckCount(1);
            group.setMaxCheckCount(1);

            for (int i = 0; i < recipes.length; i++) {
                final int index = i;
                Recipe r = recipes[i];
                Item icon = r.outputs[0].item;

                ImageButton button = table.button(
                        new TextureRegionDrawable(icon.uiIcon),
                        Styles.clearNoneTogglei,
                        40f,
                        () -> configure(index)
                ).tooltip(icon.localizedName).group(group).get();

                button.update(() -> button.setChecked(currentRecipe == index));
            }
        }

        @Override
        public Integer config() {
            return currentRecipe;
        }

        @Override
        public boolean shouldConsume() {
            Recipe r = getRecipe();
            if (r == null) return false;
            for (ItemStack stack : r.inputs) {
                if (!items.has(stack.item, stack.amount)) return false;
            }
            for (ItemStack stack : r.outputs) {
                if (items.get(stack.item) + stack.amount > itemCapacity) return false;
            }
            return enabled;
        }

        @Override
        public void updateTile() {
            Recipe r = getRecipe();
            if (r == null) return;

            if (efficiency > 0) {
                progress += getProgressIncrease(r.craftTime);
                warmup = Mathf.approachDelta(warmup, warmupTarget(), warmupSpeed);

                if (wasVisible && Mathf.chanceDelta(updateEffectChance)) {
                    updateEffect.at(
                            x + Mathf.range(size * updateEffectSpread),
                            y + Mathf.range(size * updateEffectSpread)
                    );
                }
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            totalProgress += warmup * Time.delta;

            if (progress >= 1f) {
                craft();
            }

            dumpOutputs();
        }

        @Override
        public void craft() {
            Recipe r = getRecipe();
            if (r == null) return;

            // 手动消费输入（不走 consume()，因为没有注册消费者）
            for (ItemStack stack : r.inputs) {
                items.remove(stack.item, stack.amount);
            }

            // 产出输出
            for (ItemStack stack : r.outputs) {
                for (int i = 0; i < stack.amount; i++) {
                    offload(stack.item);
                }
            }

            if (wasVisible) craftEffect.at(x, y);
            progress %= 1f;
        }

        @Override
        public void dumpOutputs() {
            Recipe r = getRecipe();
            if (r != null && timer(timerDump, dumpTime / timeScale)) {
                for (ItemStack output : r.outputs) {
                    dump(output.item);
                }
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);       // 保存 progress、warmup（父类处理）
            write.i(currentRecipe);   // 额外保存当前配方索引
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision); // 读取 progress、warmup
            currentRecipe = read.i();   // 读取配方索引
        }
    }

    public static class Recipe {
        public ItemStack[] inputs;
        public ItemStack[] outputs;
        public float craftTime;

        public Recipe(ItemStack[] inputs, ItemStack[] outputs, float craftTime) {
            this.inputs = inputs;
            this.outputs = outputs;
            this.craftTime = craftTime;
        }
    }
}