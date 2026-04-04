package gg.wrldmap.covetGUIUtils;

import gg.wrldmap.covetGUIUtils.command.OpenGUI;
import gg.wrldmap.covetGUIUtils.gui.DynamicGUIHelper;
import gg.wrldmap.covetGUIUtils.gui.GUIConfigurationHelper;
import gg.wrldmap.covetGUIUtils.gui.GUIListener;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

public final class CovetGUIUtils extends JavaPlugin {
    public static boolean isItemsAdderPresent;
    public static boolean isOraxenPresent;
    public static boolean isNexoPresent;
    public static boolean isPapiPresent;
    public static boolean doesPluginFolderExist;
    public static final MiniMessage miniMessage = MiniMessage.miniMessage();
    public static final ComponentLogger logger = ComponentLogger.logger();

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("itemsadder") != null) {
            isItemsAdderPresent = true;
        }
        if (getServer().getPluginManager().getPlugin("oraxen") != null) {
            isOraxenPresent = true;
        }
        if (getServer().getPluginManager().getPlugin("nexo") != null) {
            isNexoPresent = true;
        }
        if (isItemsAdderPresent && isOraxenPresent) {
            logger.debug("[CovetGUIUtils] ItemsAdder and Oraxen both present. Oraxen will override ItemsAdder by default and cause issues. We will NOT provide support for these setups.");
        }
        if (isNexoPresent) {
            logger.debug("[CovetGUIUtils] Nexo is currently completely untested, and issues may arise. Please report issues that come up on GitHub.");
        }
        if (getServer().getPluginManager().getPlugin("placeholderapi") != null) {
            isPapiPresent = true;
        }
        if (getDataFolder().exists()) {
            doesPluginFolderExist = true;
        } else {
            saveResource("config.yml", false);
        }

        new GUIConfigurationHelper().loadGuis();
        new DynamicGUIHelper();
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(new OpenGUI().getCommandNode(), "Opens a custom GUI", java.util.List.of("gui"));
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
