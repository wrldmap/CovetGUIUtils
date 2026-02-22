package gg.wrldmap.covetGUIUtils.gui;

import gg.wrldmap.covetGUIUtils.CovetGUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;

public class GUIListener implements Listener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof DynamicGUIHelper)) {
            return;
        }

        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null || event.getClickedInventory().getHolder() instanceof Player) {
            return;
        }

        InventoryView view = event.getView();
        String title = CovetGUIUtils.miniMessage.serialize(view.title());
        GUIConfigurationHelper.GuiConfig activeConfig = null;
        for (GUIConfigurationHelper.GuiConfig config : GUIConfigurationHelper.getGuiMap().values()) {
            if (config.title.equalsIgnoreCase(config.title)) {
                activeConfig = config;
                break;
            }
        }

        if (activeConfig == null) return;
        GUIConfigurationHelper.GuiItem itemData = activeConfig.items.get(event.getSlot());
        if (itemData != null) {

            if (itemData.playSound) {
                String s = itemData.sound != null ? itemData.sound.toLowerCase() : "ui.button.click";
                player.playSound(player.getLocation(), s, 1.0f, 1.0f);
            }

            if (itemData.exit) {
                player.closeInventory();
            }

            if (itemData.command != null && !itemData.command.isEmpty()) {
                String cmd = itemData.command.replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                player.closeInventory();
            }
        }
    }
}