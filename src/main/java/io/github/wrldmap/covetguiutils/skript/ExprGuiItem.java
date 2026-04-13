package io.github.wrldmap.covetguiutils.skript;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.github.wrldmap.covetguiutils.api.GuiItems;
import org.bukkit.event.Event;

// TODO: This doesn't work, and I don't know why. Skript is stupid sometimes.
@Name("GUI Item")
@Description("Constructs a CovetGUI item with a material, optional display name, command, exit flag, and sound.")
@Examples("set {_item} to gui item with material \"diamond\" named \"&bGem\" running \"say hello\" with exit")
@Since("1.0")
public class ExprGuiItem extends SimpleExpression<GuiItems> {

    private Expression<String> material;
    private Expression<String> displayName;
    private Expression<String> command;
    private Expression<String> sound;
    private boolean exit;
    private boolean playSound;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern,
                        Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        material    = (Expression<String>) exprs[0];
        displayName = (Expression<String>) exprs[1];
        command     = (Expression<String>) exprs[2];
        sound       = (Expression<String>) exprs[3];
        exit      = parseResult.hasTag("exit");
        playSound = parseResult.hasTag("sound");
        return true;
    }

    @Override
    protected GuiItems[] get(Event e) {
        String mat = material.getSingle(e);
        if (mat == null) return new GuiItems[0];

        GuiItems.Builder builder = GuiItems.builder().material(mat);

        if (displayName != null) {
            String n = displayName.getSingle(e);
            if (n != null) builder.displayName(n);
        }
        if (command != null) {
            String cmd = command.getSingle(e);
            if (cmd != null) builder.command(cmd);
        }
        if (sound != null) {
            String s = sound.getSingle(e);
            if (s != null) builder.sound(s).playSound(true);
        } else {
            builder.playSound(playSound);
        }

        builder.exit(exit);
        return new GuiItems[]{ builder.build() };
    }

    @Override public boolean isSingle() { return true; }
    @Override public Class<? extends GuiItems> getReturnType() { return GuiItems.class; }

    @Override
    public String toString(Event e, boolean debug) {
        return "gui item with material " + material.toString(e, debug);
    }
}