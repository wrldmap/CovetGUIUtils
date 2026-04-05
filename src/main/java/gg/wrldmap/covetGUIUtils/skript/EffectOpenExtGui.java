package gg.wrldmap.covetGUIUtils.skript;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import gg.wrldmap.covetGUIUtils.api.GuiConfig;
import gg.wrldmap.covetGUIUtils.api.Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Open Custom GUI")
@Description("Opens a programmatically built CovetGUI config for a player.")
@Examples("open covet custom gui {_gui} to player")
@Since("1.0")
public class EffectOpenExtGui extends Effect {

    private Expression<GuiConfig> config;
    private Expression<Player>    player;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern,
                        Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        config = (Expression<GuiConfig>) exprs[0];
        player = (Expression<Player>)    exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        GuiConfig cfg = config.getSingle(e);
        Player    p   = player.getSingle(e);
        if (cfg == null || p == null) return;
        new Manager().openExtInventory(p, cfg);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "open covet custom gui " + config.toString(e, debug) + " to " + player.toString(e, debug);
    }
}