package io.github.wrldmap.covetGUIUtils.api;

import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import io.github.wrldmap.covetGUIUtils.gui.DynamicGUIHelper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Manager {
    public void openCfgInventory(Player player, String name) {
        if (player != null) {
            Inventory inv = DynamicGUIHelper.getInstance().createConfigBasedGUI(player, name);
            TexturedInventoryWrapper invwrap = DynamicGUIHelper.getInstance().iawrapper;

            if (inv != null && invwrap != null) {
                invwrap.showInventory(player);
            } else if (inv != null) {
                player.openInventory(inv);
            } else {
            }
        }
    }

    public void openExtInventory(Player player, GuiConfig config) {
        if (player != null) {
            Inventory inv = DynamicGUIHelper.getInstance().createAPIBasedGUI(player, config);
            TexturedInventoryWrapper invwrap = DynamicGUIHelper.getInstance().iawrapper;

            if (inv != null && invwrap != null) {
                invwrap.showInventory(player);
            } else if (inv != null) {
                player.openInventory(inv);
            } else {
            }
        }
    }
}
