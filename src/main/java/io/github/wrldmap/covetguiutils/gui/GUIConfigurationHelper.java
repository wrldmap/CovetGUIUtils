package io.github.wrldmap.covetguiutils.gui;

import io.github.wrldmap.covetguiutils.CovetGUIUtils;
import io.github.wrldmap.covetguiutils.api.GuiConfig;
import io.github.wrldmap.covetguiutils.api.GuiItems;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

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
    public final ComponentLogger logger = CovetGUIUtils.getPlugin(CovetGUIUtils.class).getComponentLogger();

    public void loadGuis() {
        Path guiFolder = CovetGUIUtils.getPlugin(CovetGUIUtils.class).getDataFolder().toPath().resolve("gui");
        Map<String, GuiConfig> temporaryMap = new HashMap<>();

        if (!Files.exists(guiFolder)) {
            try {
                Files.createDirectories(guiFolder);
                logger.info("Created /gui/ folder successfully!");
                createExampleFile(guiFolder.resolve("example.conf"));
            } catch (IOException e) {
                logger.error("FAILED to create /gui/ folder: " + e.getMessage());
                return;
            }
        }

        try (Stream<Path> paths = Files.walk(guiFolder, 1)) {
            List<Path> files = paths
                    .filter(p -> Files.isRegularFile(p) && p.toString().endsWith(".conf"))
                    .toList();

            for (Path path : files) {
                String fileName = path.getFileName().toString();
                String name = fileName.substring(0, fileName.lastIndexOf('.'));

                try {
                    HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                            .path(path)
                            .build();

                    CommentedConfigurationNode root = loader.load();

                    // Read top-level fields
                    String title      = root.node("title").getString("");
                    int rows          = root.node("rows").getInt(3);
                    String background = root.node("background").virtual() ? null : root.node("background").getString("");
                    int shift         = root.node("shift").getInt(0);

                    GuiConfig.Builder builder = GuiConfig.builder()
                            .name(name)
                            .title(title)
                            .rows(rows)
                            .shift(shift);

                    if (background != null) builder.background(background);

                    // Read items block
                    CommentedConfigurationNode itemsNode = root.node("items");
                    if (!itemsNode.virtual()) {
                        for (var entry : itemsNode.childrenMap().entrySet()) {
                            int slot;
                            try {
                                slot = Integer.parseInt(entry.getKey().toString());
                            } catch (NumberFormatException e) {
                                logger.warn("Skipping non-integer slot key '" + entry.getKey() + "' in " + fileName);
                                continue;
                            }

                            CommentedConfigurationNode itemNode = entry.getValue();
                            GuiItems item = parseItem(itemNode);
                            builder.item(slot, item);
                        }
                    }

                    GuiConfig config = builder.build();
                    temporaryMap.put(name, config);

                } catch (Exception e) {
                    logger.error("Could not load config: " + fileName + " - " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            logger.error("Directory traversal failed: " + e.getMessage());
        }

        guiMap = temporaryMap;
    }

    private GuiItems parseItem(CommentedConfigurationNode node) throws Exception {
        String material    = node.node("material").getString("stone");
        String displayName = node.node("display-name").getString("");
        String command     = node.node("command").virtual() ? null : node.node("command").getString("");
        boolean exit       = node.node("exit").getBoolean(false);
        boolean playSound  = node.node("play-sound").getBoolean(false);
        String sound       = node.node("sound").getString("ui.button.click");

        List<String> lore = new ArrayList<>();
        CommentedConfigurationNode loreNode = node.node("lore");
        if (!loreNode.virtual()) {
            for (CommentedConfigurationNode line : loreNode.childrenList()) {
                lore.add(line.getString(""));
            }
        }

        // Adjust this builder chain to match whatever setters GuiItems.Builder exposes
        return GuiItems.builder()
                .material(material)
                .displayName(displayName)
                .lore(lore)
                .command(command)
                .exit(exit)
                .playSound(playSound)
                .sound(sound)
                .build();
    }

    private void createExampleFile(Path path) {
        String content = """
                title = "<blue><bold>Example Menu"
                rows = 3
                items {
                  13 {
                    material = "compass"
                    display-name = "<green>Welcome!"
                    lore = ["<gray>This is an auto-generated GUI"]
                    play-sound = true
                    sound = "ui.button.click"
                  }
                }
                """;
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            logger.error("Could not create example file: " + e.getMessage());
        }
    }
}