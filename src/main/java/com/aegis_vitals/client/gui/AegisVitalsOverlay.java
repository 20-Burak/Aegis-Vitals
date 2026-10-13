package com.aegis_vitals.client.gui;

import com.aegis_vitals.AegisVitals;
import com.aegis_vitals.client.config.AegisConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public final class AegisVitalsOverlay {
    private AegisVitalsOverlay() {}

    public record ColorRGB(int r, int g, int b) {
        public int toArgb() {
            return 0xFF000000 | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
        }
    }

    private static final ResourceLocation VANILLA_CONTAINER =
            ResourceLocation.withDefaultNamespace("hud/heart/container");
    private static final ResourceLocation VANILLA_ARMOR_EMPTY =
            ResourceLocation.withDefaultNamespace("hud/armor_empty");

    private static final ResourceLocation WHITE_HEART_FULL =
            ResourceLocation.fromNamespaceAndPath(AegisVitals.MOD_ID, "hud/heart/full_white");
    private static final ResourceLocation WHITE_HEART_HALF =
            ResourceLocation.fromNamespaceAndPath(AegisVitals.MOD_ID, "hud/heart/half_white");

    private static final ResourceLocation WHITE_ARMOR_FULL =
            ResourceLocation.fromNamespaceAndPath(AegisVitals.MOD_ID, "hud/armor/full_white");
    private static final ResourceLocation WHITE_ARMOR_HALF =
            ResourceLocation.fromNamespaceAndPath(AegisVitals.MOD_ID, "hud/armor/half_white");

    // =========================================================================
    // 32 HANDCRAFTED DISTINCT RPG & MANHWA HEALTH TIERS (0 to 640+ HP)
    // =========================================================================
    private static final ColorRGB[] HEART_PALETTE = {
            new ColorRGB(255, 34, 34),    // Tier 0  (0-20 HP):   Crimson Red (Classic)
            new ColorRGB(255, 120, 0),    // Tier 1  (21-40 HP):  Fiery Orange
            new ColorRGB(255, 204, 0),    // Tier 2  (41-60 HP):  Sunburst Gold / Amber
            new ColorRGB(100, 240, 20),   // Tier 3  (61-80 HP):  Lime Green
            new ColorRGB(0, 255, 170),    // Tier 4  (81-100 HP): Mint / Jade
            new ColorRGB(0, 220, 255),    // Tier 5  (101-120 HP): Electric Cyan
            new ColorRGB(20, 130, 255),   // Tier 6  (121-140 HP): Cerulean Blue
            new ColorRGB(80, 50, 255),    // Tier 7  (141-160 HP): Royal Indigo
            new ColorRGB(160, 40, 255),   // Tier 8  (161-180 HP): Arcane Violet
            new ColorRGB(235, 30, 220),   // Tier 9  (181-200 HP): Vivid Magenta
            new ColorRGB(255, 50, 130),   // Tier 10 (201-220 HP): Hot Rose Pink
            new ColorRGB(255, 110, 110),  // Tier 11 (221-240 HP): Coral Pink
            new ColorRGB(200, 100, 20),   // Tier 12 (241-260 HP): Bronze Amber
            new ColorRGB(255, 235, 120),  // Tier 13 (261-280 HP): Champagne Gold
            new ColorRGB(20, 180, 70),    // Tier 14 (281-300 HP): Forest Emerald
            new ColorRGB(0, 180, 180),    // Tier 15 (301-320 HP): Deep Teal
            new ColorRGB(160, 230, 255),  // Tier 16 (321-340 HP): Glacial Ice
            new ColorRGB(20, 60, 180),    // Tier 17 (341-360 HP): Deep Cobalt
            new ColorRGB(130, 20, 180),   // Tier 18 (361-380 HP): Shadow Orchid
            new ColorRGB(255, 170, 210),  // Tier 19 (381-400 HP): Cherry Blossom
            new ColorRGB(180, 10, 30),    // Tier 20 (401-420 HP): Blood Crimson
            new ColorRGB(220, 150, 30),   // Tier 21 (421-440 HP): Autumn Ochre
            new ColorRGB(150, 255, 70),   // Tier 22 (441-460 HP): Spring Chartreuse
            new ColorRGB(50, 220, 180),   // Tier 23 (461-480 HP): Seafoam Turquoise
            new ColorRGB(10, 100, 230),   // Tier 24 (481-500 HP): Sapphire Gem
            new ColorRGB(190, 140, 255),  // Tier 25 (501-520 HP): Twilight Lavender
            new ColorRGB(255, 0, 180),    // Tier 26 (521-540 HP): Neon Fuchsia
            new ColorRGB(255, 180, 60),   // Tier 27 (541-560 HP): Sunset Amber
            new ColorRGB(240, 245, 255),  // Tier 28 (561-580 HP): Platinum Silver
            new ColorRGB(70, 30, 100),    // Tier 29 (581-600 HP): Abyssal Void
            new ColorRGB(255, 255, 100),  // Tier 30 (601-620 HP): Solar Singularity
            new ColorRGB(100, 255, 255)   // Tier 31 (621-640 HP): Celestial Aether
    };

    // =========================================================================
    // 32 HANDCRAFTED DISTINCT METALLIC & GEM ARMOR TIERS (0 to 640+ Armor)
    // =========================================================================
    private static final ColorRGB[] ARMOR_PALETTE = {
            new ColorRGB(210, 215, 225),  // Tier 0  (0-20 Armor):   Iron / Silver (Vanilla)
            new ColorRGB(255, 215, 0),    // Tier 1  (21-40 Armor):  Gold / Brass
            new ColorRGB(0, 220, 255),    // Tier 2  (41-60 Armor):  Diamond / Cyan
            new ColorRGB(160, 60, 240),   // Tier 3  (61-80 Armor):  Netherite / Dark Amethyst
            new ColorRGB(240, 30, 60),    // Tier 4  (81-100 Armor): Ruby / Bloodstone
            new ColorRGB(30, 220, 80),    // Tier 5  (101-120 Armor): Emerald / Jade
            new ColorRGB(20, 80, 230),    // Tier 6  (121-140 Armor): Sapphire / Cobalt
            new ColorRGB(255, 170, 0),    // Tier 7  (141-160 Armor): Radiant Topaz
            new ColorRGB(255, 110, 180),  // Tier 8  (161-180 Armor): Rose Quartz / Pink Tourmaline
            new ColorRGB(180, 100, 40),   // Tier 9  (181-200 Armor): Molten Copper / Bronze
            new ColorRGB(0, 255, 180),    // Tier 10 (201-220 Armor): Malachite / Mint
            new ColorRGB(110, 40, 255),   // Tier 11 (221-240 Armor): Arcane Crystal
            new ColorRGB(255, 240, 140),  // Tier 12 (241-260 Armor): Celestial Sunstone
            new ColorRGB(20, 180, 140),   // Tier 13 (261-280 Armor): Prismarine Aegis
            new ColorRGB(170, 230, 255),  // Tier 14 (281-300 Armor): Glacial Permafrost
            new ColorRGB(220, 20, 120),   // Tier 15 (301-320 Armor): Garnet Magenta
            new ColorRGB(130, 170, 30),   // Tier 16 (321-340 Armor): Olivine Peridot
            new ColorRGB(255, 140, 80),   // Tier 17 (341-360 Armor): Fire Opal
            new ColorRGB(240, 248, 255),  // Tier 18 (361-380 Armor): Pure Mythril Platinum
            new ColorRGB(60, 30, 90),     // Tier 19 (381-400 Armor): Abyssal Obsidian
            new ColorRGB(190, 40, 40),    // Tier 20 (401-420 Armor): Dragon Scale Crimson
            new ColorRGB(230, 190, 50),   // Tier 21 (421-440 Armor): Auric Divine
            new ColorRGB(40, 230, 220),   // Tier 22 (441-460 Armor): Aquamarine Plate
            new ColorRGB(180, 100, 255),  // Tier 23 (461-480 Armor): Void Star Indigo
            new ColorRGB(255, 70, 70),    // Tier 24 (481-500 Armor): Scarlet Titanium
            new ColorRGB(100, 255, 100),  // Tier 25 (501-520 Armor): Radiant Kryptonite
            new ColorRGB(0, 140, 255),    // Tier 26 (521-540 Armor): Deep Ocean Lapis
            new ColorRGB(255, 160, 200),  // Tier 27 (541-560 Armor): Angelic Pearl
            new ColorRGB(140, 140, 160),  // Tier 28 (561-580 Armor): Meteoric Steel
            new ColorRGB(240, 220, 30),   // Tier 29 (581-600 Armor): Solar Singularity
            new ColorRGB(100, 240, 255),  // Tier 30 (601-620 Armor): Ethereal Diamond
            new ColorRGB(255, 255, 255)   // Tier 31 (621-640 Armor): Transcendent Godplate
    };

    /**
     * Resolves the distinct color for any Health tier.
     * Tiers 0-31 use handcrafted palettes.
     * Tiers 32+ use an infinite golden-ratio chromatic dispersion so EVERY SINGLE tier has a unique color!
     */
    public static ColorRGB getHeartTierColor(int tier) {
        if (tier < 0) return HEART_PALETTE[0];
        if (tier < HEART_PALETTE.length) {
            return HEART_PALETTE[tier];
        }
        float hue = (float) (((tier - HEART_PALETTE.length + 0.382) * 0.618033988749895) % 1.0);
        int rgb = Mth.hsvToRgb(hue, 0.85f, 1.0f);
        return new ColorRGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
    }

    /**
     * Resolves the distinct color for any Armor tier.
     * Tiers 0-31 use handcrafted metallic/gem palettes.
     * Tiers 32+ use infinite golden-ratio chromatic dispersion.
     */
    public static ColorRGB getArmorTierColor(int tier) {
        if (tier < 0) return ARMOR_PALETTE[0];
        if (tier < ARMOR_PALETTE.length) {
            return ARMOR_PALETTE[tier];
        }
        float hue = (float) (((tier - ARMOR_PALETTE.length + 0.618) * 0.618033988749895) % 1.0);
        int rgb = Mth.hsvToRgb(hue, 0.85f, 1.0f);
        return new ColorRGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
    }

    /**
     * Single-row tiered colored hearts (Aegis Vitals):
     * - Fixed 10-heart single row (no multi-row screen clutter up to 1024+ HP)
     * - Dynamic non-repeating color scale that evolves on every tier
     * - Left numeric HP text dynamically color-synced with the current tier
     * - Underneath layer displays the previous tier's hearts
     */
    public static void renderTieredHearts(GuiGraphics g, Minecraft mc, Player player) {
        // Round up like vanilla so 0.5 HP still shows a half heart
        int hp = (int) Math.ceil(Math.max(0.0f, player.getHealth()));
        int absorb = (int) Math.ceil(Math.max(0.0f, player.getAbsorptionAmount()));
        int totalEffectiveHp = hp + absorb;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int left = screenWidth / 2 - 91;
        int top = screenHeight - mc.gui.leftHeight;

        // Reserve one row so armor / other overlays stack above the hearts
        mc.gui.leftHeight += 10;

        // 1. Tier calculations (20 HP per tier, unlimited tiers)
        int tier = totalEffectiveHp <= 0 ? 0 : (totalEffectiveHp - 1) / 20;
        int remHp = totalEffectiveHp <= 0 ? 0 : totalEffectiveHp - tier * 20;

        ColorRGB curColor = getHeartTierColor(tier);
        ColorRGB prevColor = (tier > 0) ? getHeartTierColor(tier - 1) : null;

        // 2. Numeric HP text on the left (dynamically synced to the tier's color!)
        if (AegisConfig.SHOW_NUMERIC_HEALTH.get()) {
            String hpText = String.valueOf(hp);
            if (absorb > 0) {
                hpText += " §6(+" + absorb + ")";
            }
            int textX = left - mc.font.width(hpText) - 3;
            int textY = top + 1;
            int textColor = AegisConfig.COLOR_SYNC_NUMERIC_TEXT.get() ? curColor.toArgb() : 0xFFFF2222;
            g.drawString(mc.font, hpText, textX, textY, textColor, true);
        }

        // 3. Render 10 hearts
        for (int i = 0; i < 10; i++) {
            int heartX = left + i * 8;
            int heartY = top;

            // Subtle bounce on regeneration
            if (player.hasEffect(MobEffects.REGENERATION)) {
                int regenTick = (player.tickCount % 25);
                if (i == regenTick / 2) {
                    heartY -= 2;
                }
            }

            // Base: container outline
            g.blitSprite(VANILLA_CONTAINER, heartX, heartY, 9, 9);

            // Underneath: previous tier if higher tier
            if (prevColor != null) {
                renderTinted(g, mc, heartX, heartY, WHITE_HEART_FULL, prevColor);
            }

            // Top: current tier fill
            int slotHp = (i + 1) * 2;
            if (remHp >= slotHp) {
                renderTinted(g, mc, heartX, heartY, WHITE_HEART_FULL, curColor);
            } else if (remHp == slotHp - 1) {
                renderTinted(g, mc, heartX, heartY, WHITE_HEART_HALF, curColor);
            }
        }
    }

    /**
     * Single-row tiered colored armor (Aegis Vitals):
     * - Fixed 10-slot row stacked above the hearts
     * - Dynamic non-repeating metallic/gem scale that evolves on every tier
     * - Left numeric Armor text color-synced with current tier + toughness indicator
     */
    public static void renderTieredArmor(GuiGraphics g, Minecraft mc, Player player) {
        int armor = player.getArmorValue();
        if (armor <= 0) return;

        double toughness = player.getAttributeValue(Attributes.ARMOR_TOUGHNESS);

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int left = screenWidth / 2 - 91;
        int top = screenHeight - mc.gui.leftHeight;
        mc.gui.leftHeight += 10;

        // 1. Armor tier calculations (20 per tier, unlimited tiers)
        int armorTier = (armor - 1) / 20;
        int remArmor = armor - (armorTier * 20);

        ColorRGB curArmorColor = getArmorTierColor(armorTier);
        ColorRGB prevArmorColor = (armorTier > 0) ? getArmorTierColor(armorTier - 1) : null;

        // 2. Numeric Armor text on the left (dynamically synced to current tier)
        if (AegisConfig.SHOW_NUMERIC_ARMOR.get()) {
            String armorText = String.valueOf(armor);
            if (AegisConfig.SHOW_ARMOR_TOUGHNESS.get() && toughness > 0.0) {
                armorText += " §b(◆" + (int) Math.round(toughness) + ")";
            }
            int textX = left - mc.font.width(armorText) - 3;
            int textY = top + 1;
            int armorTextColor = AegisConfig.COLOR_SYNC_NUMERIC_TEXT.get() ? curArmorColor.toArgb() : 0xFF55FF55;
            g.drawString(mc.font, armorText, textX, textY, armorTextColor, true);
        }

        // 3. Render 10 armor icons
        for (int i = 0; i < 10; i++) {
            int slotX = left + i * 8;
            int slotY = top;

            if (armorTier == 0) {
                // Background empty container on Tier 0
                g.blitSprite(VANILLA_ARMOR_EMPTY, slotX, slotY, 9, 9);
            } else if (prevArmorColor != null) {
                // Background previous tier full icon
                renderTinted(g, mc, slotX, slotY, WHITE_ARMOR_FULL, prevArmorColor);
            }

            int slotVal = (i + 1) * 2;
            if (remArmor >= slotVal) {
                renderTinted(g, mc, slotX, slotY, WHITE_ARMOR_FULL, curArmorColor);
            } else if (remArmor == slotVal - 1) {
                renderTinted(g, mc, slotX, slotY, WHITE_ARMOR_HALF, curArmorColor);
            }
        }
    }

    private static void renderTinted(GuiGraphics g, Minecraft mc, int x, int y, ResourceLocation spriteLoc, ColorRGB color) {
        TextureAtlasSprite sprite = mc.getGuiSprites().getSprite(spriteLoc);
        g.blit(x, y, 0, 9, 9, sprite, color.r() / 255.0f, color.g() / 255.0f, color.b() / 255.0f, 1.0f);
    }
}
