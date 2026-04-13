package io.github.wrldmap.covetGUIUtils.utils;

import io.github.wrldmap.covetGUIUtils.CovetGUIUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class TextUtils {
    public static Component parse(Player player, String text) {
        if (text == null) return Component.empty();

        String processed = text;
        if (CovetGUIUtils.isPapiPresent && player != null) {
            processed = PlaceholderAPI.setPlaceholders(player, processed);
        }

        return CovetGUIUtils.miniMessage.deserialize(processed);
    }
}