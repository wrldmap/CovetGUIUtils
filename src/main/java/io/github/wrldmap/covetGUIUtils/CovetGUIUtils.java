package io.github.wrldmap.covetGUIUtils;

import io.github.wrldmap.covetGUIUtils.command.OpenGUI;
import io.github.wrldmap.covetGUIUtils.gui.DynamicGUIHelper;
import io.github.wrldmap.covetGUIUtils.gui.GUIConfigurationHelper;
import io.github.wrldmap.covetGUIUtils.gui.GUIListener;
import io.github.wrldmap.covetGUIUtils.skript.SkriptSupport;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CovetGUIUtils extends JavaPlugin {
    public static boolean isItemsAdderPresent;
    public static boolean isOraxenPresent;
    public static boolean isNexoPresent;
    public static boolean isSkriptPresent;
    public static boolean isPapiPresent;
    public static boolean doesPluginFolderExist;
    public static final MiniMessage miniMessage = MiniMessage.miniMessage();
    public static final String mcver = Bukkit.getServer().getMinecraftVersion();
    public final ComponentLogger logger = getPlugin(CovetGUIUtils.class).getComponentLogger();

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
        if (getServer().getPluginManager().getPlugin("skript") != null) {
            isSkriptPresent = true;
        }
        if (isItemsAdderPresent && isOraxenPresent) {
            logger.warn("ItemsAdder and Oraxen both present. Oraxen will override ItemsAdder by default and cause issues. We will NOT provide support for these setups.");
            logger.info("I heard ResourcePackManager by MagmaGuy works well for this... #NotSponsored");
        }
        if (isNexoPresent) {
            logger.warn("Nexo is currently completely untested, and issues may arise. Please report issues that come up on GitHub.");
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

        if (isSkriptPresent) {
            SkriptSupport.register(this);
            logger.info("Skript support enabled!");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
