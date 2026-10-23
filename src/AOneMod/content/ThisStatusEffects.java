package AOneMod.content;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import mindustry.content.Fx;
import mindustry.type.StatusEffect;

public class ThisStatusEffects {
    public static StatusEffect
            impair, solarFlare;
    public static void load () {
        impair = new StatusEffect("status-impair") {{
            damageMultiplier = 0.8f;
            healthMultiplier = 0.6f;
            reloadMultiplier = 0.7f;
            speedMultiplier = 0.7f;

           color = Color.valueOf("FFE92C");
        }};

        solarFlare =  new StatusEffect("status-solar-flare") {{
            color = Color.valueOf("FFE92C");
            speedMultiplier = 0.5f;
            buildSpeedMultiplier = 0.6f;
            reloadMultiplier = 0.7f;
            effect = Fx.none;
            transitionDamage = 0f;
        }};
    }
}
