package gg.wrldmap.covetGUIUtils.skript;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import gg.wrldmap.covetGUIUtils.api.GuiConfig;
import gg.wrldmap.covetGUIUtils.api.GuiItems;
import org.bukkit.event.Event;

@Name("GUI Config")
@Description("Constructs a CovetGUI config with a title, row count, and optional items.")
@Examples("set {_gui} to gui config with title \"&6Shop\" with rows 3")
@Since("1.0")
public class ExprGuiConfig extends SimpleExpression<GuiConfig> {

    private Expression<String>   title;
    private Expression<Number>   rows;
    private Expression<GuiItems> items;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern,
                        Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        title = (Expression<String>)   exprs[0];
        rows  = (Expression<Number>)   exprs[1];
        return true;
    }

    @Override
    protected GuiConfig[] get(Event e) {
        String t = title.getSingle(e);
        Number r = rows.getSingle(e);
        if (t == null || r == null) return new GuiConfig[0];

        GuiConfig.Builder builder = GuiConfig.builder()
                .title(t)
                .rows(r.intValue());

        if (items != null) {
            GuiItems[] allItems = items.getAll(e);
            for (int i = 0; i < allItems.length; i++) {
                builder.item(i, allItems[i]);
            }
        }

        return new GuiConfig[]{ builder.build() };
    }

    @Override public boolean isSingle() { return true; }
    @Override public Class<? extends GuiConfig> getReturnType() { return GuiConfig.class; }

    @Override
    public String toString(Event e, boolean debug) {
        return "gui config with title " + title.toString(e, debug);
    }
}