package gg.wrldmap.covetGUIUtils.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import gg.wrldmap.covetGUIUtils.api.GuiConfig;
import gg.wrldmap.covetGUIUtils.api.GuiItems;
import org.bukkit.plugin.java.JavaPlugin;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class SkriptSupport {
    public static void register(JavaPlugin plugin) {
        Classes.registerClass(new ClassInfo<>(GuiConfig.class, "guiconfig")
                .user("gui ?configs?")
                .name("GuiConfig")
                .description("A CovetGUI configuration object built via the API.")
                .since("1.0")
                .parser(new Parser<>() {
                    @Override public GuiConfig parse(String s, ParseContext ctx) { return null; }
                    @Override public boolean canParse(ParseContext ctx) { return false; }
                    @Override public String toString(GuiConfig o, int flags) { return "gui config"; }
                    @Override public String toVariableNameString(GuiConfig o) { return "guiconfig"; }
                })
        );

        Classes.registerClass(new ClassInfo<>(GuiItems.class, "guiitem")
                .user("gui ?items?")
                .name("GuiItem")
                .description("A CovetGUI item object built via the API.")
                .since("1.0")
                .parser(new Parser<>() {
                    @Override public GuiItems parse(String s, ParseContext ctx) { return null; }
                    @Override public boolean canParse(ParseContext ctx) { return false; }
                    @Override public String toString(GuiItems o, int flags) { return "gui item"; }
                    @Override public String toVariableNameString(GuiItems o) { return "guiitems"; }
                })
        );

        SkriptAddon addon = Skript.instance().registerAddon(plugin.getClass(), plugin.getName());
        SyntaxRegistry registry = addon.syntaxRegistry();

        registry.register(SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffectOpenCfgGui.class)
                        .addPatterns("open [covet] gui [named] %string% to %player%")
                        .build()
        );

        registry.register(SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffectOpenExtGui.class)
                        .addPatterns("open [covet] custom gui %guiconfig% to %player%")
                        .build()
        );

        registry.register(SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffectAddItemToGui.class)
                        .addPatterns("add gui item %guiitem% to slot %number% of %guiconfig%")
                        .build()
        );

        registry.register(SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprGuiConfig.class, GuiConfig.class)
                        .priority(SyntaxInfo.COMBINED)
                        .addPatterns("gui config [with] title %string% [with] rows %number%")
                        .build()
        );

        // TODO: Fix this.
        /*registry.register(SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprGuiItem.class, GuiItems.class)
                        .priority(SyntaxInfo.COMBINED)
                        .addPatterns(
                                "gui item with material %string% [named %-string%] [running %-string%] " +
                                        "[exit:closing] [sound:playing sound %-string%]"
                        )
                        .build()
        );*/
    }
}
