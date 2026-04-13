package io.github.wrldmap.covetguiutils.api;

import com.nexomc.nexo.api.NexoItems;
import dev.lone.itemsadder.api.CustomStack;
import io.github.wrldmap.covetguiutils.CovetGUIUtils;
import io.github.wrldmap.covetguiutils.gui.DynamicGUIHelper;
import io.th0rgal.oraxen.api.OraxenItems;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiItems {
    private String material = "stone";
    private String displayName = "";
    private List<String> lore = new ArrayList<>();
    private String command = null;
    private Boolean exit = false;
    private Boolean playSound = false;
    private String sound = "ui.button.click";

    private GuiItems() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final GuiItems item = new GuiItems();

        public Builder material(String material)     { item.material = material;     return this; }
        public Builder displayName(String name)      { item.displayName = name;      return this; }
        public Builder lore(String... lines)         { item.lore = List.of(lines);   return this; }
        public Builder lore(List<String> lines)      { item.lore = new ArrayList<>(lines); return this; }
        public Builder command(String command)        { item.command = command;       return this; }
        public Builder exit(boolean exit)            { item.exit = exit;             return this; }
        public Builder playSound(boolean play)       { item.playSound = play;        return this; }
        public Builder sound(String sound)           { item.sound = sound;           return this; }

        public GuiItems build() {
            return item;
        }
    }

    // Getters
    public String getMaterial()     { return material; }
    public String getDisplayName()  { return displayName; }
    public List<String> getLore()   { return Collections.unmodifiableList(lore); }
    public String getCommand()      { return command; }
    public boolean isExit()         { return exit; }
    public boolean isPlaySound()    { return playSound; }
    public String getSound()        { return sound; }

    public ItemStack createItemStack(Player player) {
        ItemStack item = resolveItem();

        item.editMeta(meta -> {
            meta.displayName(DynamicGUIHelper.parseText(player, displayName));

            List<Component> adventureLore = new ArrayList<>();
            for (String line : lore) {
                adventureLore.add(DynamicGUIHelper.parseText(player, line));
            }
            meta.lore(adventureLore);
        });

        return item;
    }

    private ItemStack resolveItem() {
        Material vanillaMat = Material.getMaterial(material.toUpperCase());
        if (vanillaMat != null) return new ItemStack(vanillaMat);

        if (CovetGUIUtils.isItemsAdderPresent) {
            CustomStack stack = CustomStack.getInstance(material);
            if (stack != null) return stack.getItemStack();
        }

        if (CovetGUIUtils.isOraxenPresent) {
            io.th0rgal.oraxen.items.ItemBuilder oraxenItem = OraxenItems.getItemById(material);
            if (oraxenItem != null) return oraxenItem.build();
        }

        if (CovetGUIUtils.isNexoPresent) {
            com.nexomc.nexo.items.ItemBuilder nexoItem = NexoItems.itemFromId(material);
            if (nexoItem != null) return nexoItem.build();
        }

        return new ItemStack(Material.STONE);
    }
}
