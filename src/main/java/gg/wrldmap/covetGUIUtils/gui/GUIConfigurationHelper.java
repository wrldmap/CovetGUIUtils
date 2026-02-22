package gg.wrldmap.covetGUIUtils.gui;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import gg.wrldmap.covetGUIUtils.CovetGUIUtils;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class GUIConfigurationHelper {

    private static Map<String, GuiConfig> guiMap = new HashMap<>();

    public static Map<String, GuiConfig> getGuiMap() {
        return Collections.unmodifiableMap(guiMap);
    }

    public void loadGuis() {
        Path guiFolder = CovetGUIUtils.getPlugin(CovetGUIUtils.class).getDataFolder().toPath().resolve("gui");
        Map<String, GuiConfig> temporaryMap = new HashMap<>();

        if (!Files.exists(guiFolder)) {
            try {
                Files.createDirectories(guiFolder);
                CovetGUIUtils.getPlugin(CovetGUIUtils.class).getLogger().info("Created /gui/ folder successfully!");
                createExampleFile(guiFolder.resolve("example.conf"));
            } catch (IOException e) {
                CovetGUIUtils.getPlugin(CovetGUIUtils.class).getLogger().severe("FAILED to create /gui/ folder: " + e.getMessage());
                return;
            }
        }

        try (Stream<Path> paths = Files.walk(guiFolder, 1)) {
            List<Path> files = paths.filter(p -> Files.isRegularFile(p) && p.toString().endsWith(".conf")).toList();

            for (Path path : files) {
                String fileName = path.getFileName().toString();
                String name = fileName.substring(0, fileName.lastIndexOf('.'));

                try {
                    HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                            .path(path)
                            .build();

                    CommentedConfigurationNode node = loader.load();
                    GuiConfig config = node.get(GuiConfig.class);

                    if (config != null) {
                        temporaryMap.put(name, config);
                    }
                } catch (Exception e) {
                    CovetGUIUtils.getPlugin(CovetGUIUtils.class).getLogger().severe("Could not load config: " + fileName + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            CovetGUIUtils.getPlugin(CovetGUIUtils.class).getLogger().severe("Directory traversal failed: " + e.getMessage());
        }

        guiMap = temporaryMap;
    }

    private void createExampleFile(Path path) {
        String content = """
                type = "chest"
                title = "<blue><bold>Example Menu"
                rows = 3
                items {
                  13 {
                    material = "compass"
                    display-name = "<green>Welcome!"
                    lore = ["<gray>This is an auto-generated GUI"]
                    playSound = true
                    sound = "ui.button.click"
                  }
                }
                """;
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            CovetGUIUtils.getPlugin(CovetGUIUtils.class).getLogger().severe("Could not create example file: " + e.getMessage());
        }
    }

    @ConfigSerializable
    public static class GuiConfig {
        public String type = "chest";
        public String title = "Menu";
        public int rows = 3;
        public String background;
        public Map<Integer, GuiItem> items = new HashMap<>();
    }

    @ConfigSerializable
    public static class GuiItem {
        public String material = "stone";
        @Setting("display-name")
        public String displayName = "";
        public List<String> lore = new ArrayList<>();
        public String command = null;
        public boolean exit = false;
        @Setting("play-sound")
        public boolean playSound = false;
        public String sound = "ui.button.click";

        public ItemStack createItemStack(org.bukkit.entity.Player player) {
            ItemStack item = null;

            Material vanillaMat = Material.getMaterial(material.toUpperCase());
            if (vanillaMat != null) {
                item = new ItemStack(vanillaMat);
            }

            if (item == null && CovetGUIUtils.isItemsAdderPresent) {
                dev.lone.itemsadder.api.CustomStack stack = dev.lone.itemsadder.api.CustomStack.getInstance(material);
                if (stack != null) {
                    item = stack.getItemStack();
                }
            }

            if (item == null && CovetGUIUtils.isOraxenPresent) {
                ItemBuilder oraxenItem = OraxenItems.getItemById(material);
                if (oraxenItem != null) {
                    item = oraxenItem.build();
                }
            }

            if (item == null) item = new ItemStack(org.bukkit.Material.STONE);

            item.editMeta(meta -> {
                meta.displayName(DynamicGUIHelper.parseText(player, displayName));

                java.util.List<net.kyori.adventure.text.Component> adventureLore = new java.util.ArrayList<>();
                for (String line : lore) {
                    adventureLore.add(DynamicGUIHelper.parseText(player, line));
                }
                meta.lore(adventureLore);
            });

            return item;
        }
    }
}