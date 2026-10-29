package AOneMod;

import AOneMod.content.*;
import arc.Events;
import arc.graphics.Color;
import arc.util.Time;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.mod.Mod;
import mindustry.type.TeamEntry;
import mindustry.ui.dialogs.BaseDialog;

public class ThisMod extends Mod {
    public ThisMod(){
        /*Events.on(EventType.ClientLoadEvent.class, e -> {
            Time.run(10f, ()->{
                BaseDialog dialog = new BaseDialog("hi!");
                dialog.cont.add("233");

                //一定要加这个代码，不然弹窗界面没有返回按钮。
                dialog.addCloseButton();
                dialog.show();
            });
        });*/

    }

    @Override
    public void loadContent() {
        /*// 1. 获取一个未被其他模组占用的 ID（7-255）
        Team BlueTeam = Team.get(5);

        // 2. 设置名称（用于本地化键值查找）
        BlueTeam.name = "BlueTeam";

        // 3. 设置颜色（单色，自动生成深色调色板）
        BlueTeam.color.set(Color.valueOf("#3e7eff"));
        BlueTeam.setPalette(BlueTeam.color);

        // 或者设置三色调色板（高亮色、主色、暗色）
        // BlueTeam.setPalette(
        //     Color.valueOf("66ccff"),
        //     Color.valueOf("00aaff"),
        //     Color.valueOf("005588")
        // );

        // 4. （可选）注册到内容数据库，使其在图鉴中可见
        new TeamEntry(BlueTeam.name, BlueTeam);*/

        super.loadContent();
        ThisItems.load();
        ThisUnitTypes.load();
        ThisBlocks.load();
        ThisStatusEffects.load();
        ThisPlanets.load();
        ThisSector.load();
        ThisTechTree.load();
    }
}
