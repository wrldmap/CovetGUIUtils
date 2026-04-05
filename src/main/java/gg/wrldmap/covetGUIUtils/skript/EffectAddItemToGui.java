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
import gg.wrldmap.covetGUIUtils.api.GuiItems;
import org.bukkit.event.Event;

@Name("Add Item to GUI Slot")
@Description("Places a GUI item into a specific slot of a GUI config.")
@Examples({
        "set {_item} to gui item with material \"diamond\" named \"&bGem\"",
        "add gui item {_item} to slot 13 of {_gui}"
})
@Since("1.0")
public class EffectAddItemToGui extends Effect {

    private Expression<GuiItems> item;
    private Expression<Number>   slot;
    private Expression<GuiConfig> config;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern,
                        Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        item   = (Expression<GuiItems>)  exprs[0];
        slot   = (Expression<Number>)    exprs[1];
        config = (Expression<GuiConfig>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event e) {
        GuiItems i  = item.getSingle(e);
        Number   s  = slot.getSingle(e);
        GuiConfig c = config.getSingle(e);
        if (i == null || s == null || c == null) return;
        c.addItem(s.intValue(), i);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "add gui item " + item.toString(e, debug) +
                " to slot " + slot.toString(e, debug) +
                " of " + config.toString(e, debug);
    }
}