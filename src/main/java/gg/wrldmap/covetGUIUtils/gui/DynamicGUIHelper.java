package gg.wrldmap.covetGUIUtils.gui;

import gg.wrldmap.covetGUIUtils.CovetGUIUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;



public class DynamicGUIHelper implements InventoryHolder {
    private static DynamicGUIHelper instance;

    public DynamicGUIHelper() {
        instance = this;
    }

    public Inventory createGuiForPlayer(Player player, String guiName) {
        GUIConfigurationHelper.GuiConfig data = GUIConfigurationHelper.getGuiMap().get(guiName);

        if (data == null) return null;

        int slots = data.rows * 9;

        Component title = parseText(player, data.title);
        Inventory gui = Bukkit.createInventory(this, slots, title);

        data.items.forEach((slot, itemData) -> {
            if (slot >= 0 && slot < slots) {
                gui.setItem(slot, itemData.createItemStack(player));
            }
        });

        return gui;
    }

    public static Component parseText(Player player, String text) {
        String processed = text;
        if (CovetGUIUtils.isPapiPresent && player != null) {
            processed = PlaceholderAPI.setPlaceholders(player, processed);
        }
        return CovetGUIUtils.miniMessage.deserialize(processed);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return Bukkit.createInventory(this, 9);
    }

    public static DynamicGUIHelper getInstance() {
        return instance;
    }
}