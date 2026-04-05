package gg.wrldmap.covetGUIUtils.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import gg.wrldmap.covetGUIUtils.gui.DynamicGUIHelper;
import gg.wrldmap.covetGUIUtils.gui.GUIConfigurationHelper;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Set;

public class OpenGUI {
    public LiteralCommandNode<CommandSourceStack> getCommandNode() {
        return Commands.literal("opengui")
                .then(Commands.argument("gui_name", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            Set<String> guis = GUIConfigurationHelper.getGuiMap().keySet();
                            String input = builder.getRemaining().toLowerCase();

                            for (String name : guis) {
                                if (name.toLowerCase().startsWith(input)) {
                                    builder.suggest(name);
                                }
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String guiName = StringArgumentType.getString(context, "gui_name");
                            return openSpecificGUI(context, guiName);
                        })
                )
                .executes(context -> {
                    context.getSource().getSender().sendMessage(
                            Component.text("Please specify a GUI name!", NamedTextColor.RED)
                    );
                    return 1;
                })
                .build();
    }

    private int openSpecificGUI(CommandContext<CommandSourceStack> context, String name) {
        if (context.getSource().getSender() instanceof Player player) {
            Inventory inv = DynamicGUIHelper.getInstance().createConfigBasedGUI(player, name);
            TexturedInventoryWrapper invwrap = DynamicGUIHelper.getInstance().iawrapper;

            if (inv != null && invwrap != null) {
                invwrap.showInventory(player);
                player.sendRichMessage("<gold>Opening GUI: <white>'" + name + "'");
            } else if (inv != null) {
                player.openInventory(inv);
                player.sendRichMessage("<gold>Opening GUI: <white>'" + name + "'");
            } else {
                player.sendRichMessage("<red>GUI '" + name + "' not found!");
            }
        }
        return 1;
    }
}