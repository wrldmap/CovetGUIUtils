package io.github.wrldmap.covetguiutils.gui;

import io.github.wrldmap.covetguiutils.api.GuiConfig;
import io.github.wrldmap.covetguiutils.api.GuiItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {
    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof DynamicGUIHelper helper)) return;

        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null || event.getClickedInventory().getHolder() instanceof Player) return;

        GuiConfig config = helper.getActiveApiConfig();
        if (config == null) return;

        GuiItems itemData = config.getItems().get(event.getSlot());
        if (itemData == null) return;

        if (itemData.isPlaySound()) {
            String s = itemData.getSound() != null ? itemData.getSound().toLowerCase() : "ui.button.click";
            player.playSound(player.getLocation(), s, 1.0f, 1.0f);
        }
        if (itemData.isExit()) {
            player.closeInventory();
        }
        if (itemData.getCommand() != null && !itemData.getCommand().isEmpty()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), itemData.getCommand().replace("%player%", player.getName()));
            player.closeInventory();
        }
    }
}