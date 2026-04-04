package gg.wrldmap.covetGUIUtils;

import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import gg.wrldmap.covetGUIUtils.gui.DynamicGUIHelper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class API {
    public void openInventory(Player player, String name) {
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
}
