package com.aegis_vitals.client.config;

import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class AegisConfig {
    private AegisConfig() {}

    public enum HudMode {
        AEGIS_PRIORITY, // Aegis is primary: cancels other health/armor HUD mods
        AUTO,           // Yields to external HUD mods if installed
        DISABLED        // Keep vanilla
    }

    private static final String KEY = "aegis_vitals.configuration.";
    private static final ModConfigSpec.Builder B = new ModConfigSpec.Builder();

    public static final ModConfigSpec.EnumValue<HudMode> HUD_MODE;
    public static final ModConfigSpec.BooleanValue SHOW_NUMERIC_HEALTH;
    public static final ModConfigSpec.BooleanValue SHOW_NUMERIC_ARMOR;
    public static final ModConfigSpec.BooleanValue COLOR_SYNC_NUMERIC_TEXT;
    public static final ModConfigSpec.BooleanValue SHOW_ARMOR_TOUGHNESS;
    public static final ModConfigSpec.BooleanValue ENABLE_TIERED_ARMOR;

    static {
        B.push("general");
        HUD_MODE = B.comment("AEGIS_PRIORITY: Aegis is the primary HUD and suppresses other health/armor HUD mods\n" +
                             "AUTO: Yields to external HUD mods if present\n" +
                             "DISABLED: Disables Aegis completely (vanilla HUD)")
                .translation(KEY + "hudMode")
                .defineEnum("hudMode", HudMode.AEGIS_PRIORITY);

        SHOW_NUMERIC_HEALTH = B.comment("Show the numeric health value left of the hearts")
                .translation(KEY + "showNumericHealth")
                .define("showNumericHealth", true);

        SHOW_NUMERIC_ARMOR = B.comment("Show the numeric armor value left of the armor icons")
                .translation(KEY + "showNumericArmor")
                .define("showNumericArmor", true);

        COLOR_SYNC_NUMERIC_TEXT = B.comment("Tint the numeric text with the current tier's color")
                .translation(KEY + "colorSyncNumericText")
                .define("colorSyncNumericText", true);

        SHOW_ARMOR_TOUGHNESS = B.comment("Show armor toughness next to the armor value")
                .translation(KEY + "showArmorToughness")
                .define("showArmorToughness", true);

        ENABLE_TIERED_ARMOR = B.comment("Render single-row tiered armor instead of the vanilla armor bar")
                .translation(KEY + "enableTieredArmor")
                .define("enableTieredArmor", true);
        B.pop();
    }

    public static final ModConfigSpec SPEC = B.build();

    private static final String[] EXTERNAL_HUD_MODS = {
            "overflowingbars", "colorfulhearts", "classicbar", "healthoverlay", "tieredhearts"
    };

    private static Boolean externalLoaded;

    /** Cached: mod list never changes after loading, and this is queried every frame. */
    public static boolean isExternalHealthHudLoaded() {
        if (externalLoaded == null) {
            boolean found = false;
            for (String id : EXTERNAL_HUD_MODS) {
                if (ModList.get().isLoaded(id)) { found = true; break; }
            }
            externalLoaded = found;
        }
        return externalLoaded;
    }
}
