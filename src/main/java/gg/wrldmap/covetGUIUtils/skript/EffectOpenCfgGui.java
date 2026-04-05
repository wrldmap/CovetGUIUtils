package gg.wrldmap.covetGUIUtils.skript;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import gg.wrldmap.covetGUIUtils.api.Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Open Config GUI")
@Description("Opens a config-file-based CovetGUI by name for a player.")
@Examples("open covet gui named \"welcome_menu\" to player")
@Since("1.0")
public class EffectOpenCfgGui extends Effect {

    private Expression<String> name;
    private Expression<Player> player;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern,
                        Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        name   = (Expression<String>) exprs[0];
        player = (Expression<Player>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String guiName = name.getSingle(e);
        Player p       = player.getSingle(e);
        if (guiName == null || p == null) return;
        new Manager().openCfgInventory(p, guiName);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "open covet gui " + name.toString(e, debug) + " to " + player.toString(e, debug);
    }
}
