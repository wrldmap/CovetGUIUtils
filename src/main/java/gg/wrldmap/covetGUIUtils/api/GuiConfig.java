package gg.wrldmap.covetGUIUtils.api;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class GuiConfig {
    private String name = "";
    private String title = "";
    private int rows = 3;
    private String background = null;
    private Integer shift = 0;
    private final Map<Integer, GuiItems> items = new LinkedHashMap<>();

    private GuiConfig() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final GuiConfig config = new GuiConfig();

        public Builder name(String name)           { config.name = name;             return this; }
        public Builder title(String title)         { config.title = title;           return this; }
        public Builder rows(int rows)              { config.rows = rows;             return this; }
        public Builder background(String material) { config.background = material;   return this; }
        public Builder shift(int shift)            { config.shift = shift;           return this; }

        public Builder item(int slot, GuiItems item) {
            config.items.put(slot, item);
            return this;
        }

        public Builder item(int slot, GuiItems.Builder itemBuilder) {
            config.items.put(slot, itemBuilder.build());
            return this;
        }

        public GuiConfig build() {
            if (config.rows < 1 || config.rows > 6)
                throw new IllegalStateException("rows must be between 1 and 6");
            return config;
        }
    }

    public void addItem(int slot, GuiItems item) {
        items.put(slot, item);
    }

    // Getters
    public String getName()                   { return name; }
    public String getTitle()                  { return title; }
    public int getRows()                      { return rows; }
    public String getBackground()             { return background; }
    public int getShift()                     { return shift; }
    public Map<Integer, GuiItems> getItems()  { return Collections.unmodifiableMap(items); }
}
