package gg.wrldmap.covetGUIUtils.gui;

import com.nexomc.nexo.NexoPlugin;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import gg.wrldmap.covetGUIUtils.CovetGUIUtils;
import gg.wrldmap.covetGUIUtils.api.GuiConfig;
import io.th0rgal.oraxen.OraxenPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;


public class DynamicGUIHelper implements InventoryHolder {
    private static DynamicGUIHelper instance;
    private boolean guiWasSet;
    public Inventory gui;
    public TexturedInventoryWrapper iawrapper;

    public DynamicGUIHelper() {
        instance = this;
    }

    public Inventory createConfigBasedGUI(Player player, String guiName) {
        GUIConfigurationHelper.GuiConfig data = GUIConfigurationHelper.getGuiMap().get(guiName);
        if (data == null) return null;
        guiWasSet = false;
        iawrapper = null;
        this.gui = null;
        int slots = data.rows * 9;
        int shift = data.shift;

        Component title = parseText(player, data.title);
        String legacyTitle = LegacyComponentSerializer.legacySection().serialize(title);

        if (data.background != null) {
            if (CovetGUIUtils.isItemsAdderPresent && !guiWasSet) {
                if (FontImageWrapper.getNamespacedIdsInRegistry().contains(data.background)) {
                    FontImageWrapper IAbg = new FontImageWrapper(data.background);
                    iawrapper = new TexturedInventoryWrapper(this, slots, legacyTitle, IAbg);
                    this.gui = iawrapper.getInternal();
                    guiWasSet = true;
                }
            }
            if (CovetGUIUtils.isNexoPresent && !guiWasSet) {
                com.nexomc.nexo.glyphs.Glyph glyph = NexoPlugin.instance().fontManager().glyphFromName(data.background);
                if (glyph != null) {
                    iawrapper = null;
                    this.gui = Bukkit.createInventory(this, slots, buildNexoTitle(glyph, title, shift));
                    guiWasSet = true;
                }
            }
            if (CovetGUIUtils.isOraxenPresent && !guiWasSet) {
                io.th0rgal.oraxen.font.Glyph glyph = OraxenPlugin.get().getFontManager().getGlyphFromName(data.background);
                if (glyph != null) {
                    iawrapper = null;
                    this.gui = Bukkit.createInventory(this, slots, buildOraxenTitle(glyph, title, shift));
                    guiWasSet = true;
                }
            }
        }
        if (!guiWasSet) {
            iawrapper = null;
            this.gui = Bukkit.createInventory(this, slots, title);
        }

        data.items.forEach((slot, itemData) -> {
            if (slot >= 0 && slot < slots) {
                gui.setItem(slot, itemData.createItemStack(player));
            }
        });

        return gui;
    }

    private Component buildOraxenTitle(io.th0rgal.oraxen.font.Glyph glyph, Component displayTitle, int shiftint) {
        if (glyph == null) return displayTitle;

        String shift = OraxenPlugin.get().getFontManager().getShiftProvider().getShiftString(shiftint);

        return Component.text()
                .append(Component.text(shift).font(Key.key("oraxen", "shift")))
                .append(Component.text(glyph.getCharacter())
                        .font(Key.key("minecraft", "default"))
                        .color(NamedTextColor.WHITE) // FIXES THE DARKNESS
                        .decoration(TextDecoration.ITALIC, false)) // FIXES TILT
                .append(displayTitle)
                .build();
    }

    private Component buildNexoTitle(com.nexomc.nexo.glyphs.Glyph glyph, Component displayTitle, int shiftint) {
        try {
            if (glyph == null) return displayTitle;

            return Component.text(String.valueOf(glyph.getChars()))
                    .font(Key.key("minecraft", "default"))
                    .append(displayTitle);

        } catch (Exception e) {
            return displayTitle;
        }
    }

    public Inventory createAPIBasedGUI(Player player, GuiConfig config) {
        guiWasSet = false;
        iawrapper = null;
        this.gui = null;

        int slots = config.getRows() * 9;
        Component title = parseText(player, config.getTitle());
        String legacyTitle = LegacyComponentSerializer.legacySection().serialize(title);
        String background = config.getBackground();
        int shift = config.getShift();

        if (background != null) {
            if (CovetGUIUtils.isItemsAdderPresent && !guiWasSet) {
                if (FontImageWrapper.getNamespacedIdsInRegistry().contains(background)) {
                    FontImageWrapper IAbg = new FontImageWrapper(background);
                    iawrapper = new TexturedInventoryWrapper(this, slots, legacyTitle, IAbg);
                    this.gui = iawrapper.getInternal();
                    guiWasSet = true;
                }
            }
            if (CovetGUIUtils.isNexoPresent && !guiWasSet) {
                com.nexomc.nexo.glyphs.Glyph glyph = NexoPlugin.instance().fontManager().glyphFromName(background);
                if (glyph != null) {
                    iawrapper = null;
                    this.gui = Bukkit.createInventory(this, slots, buildNexoTitle(glyph, title, shift));
                    guiWasSet = true;
                }
            }
            if (CovetGUIUtils.isOraxenPresent && !guiWasSet) {
                io.th0rgal.oraxen.font.Glyph glyph = OraxenPlugin.get().getFontManager().getGlyphFromName(background);
                if (glyph != null) {
                    iawrapper = null;
                    this.gui = Bukkit.createInventory(this, slots, buildOraxenTitle(glyph, title, shift));
                    guiWasSet = true;
                }
            }
        }
        if (!guiWasSet) {
            iawrapper = null;
            this.gui = Bukkit.createInventory(this, slots, title);
        }

        config.getItems().forEach((slot, item) -> {
            int adjusted = slot + config.getShift();
            if (adjusted >= 0 && adjusted < slots) {
                gui.setItem(adjusted, item.createItemStack(player));
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