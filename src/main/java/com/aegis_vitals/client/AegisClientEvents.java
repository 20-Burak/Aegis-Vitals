package com.aegis_vitals.client;

import com.aegis_vitals.AegisVitals;
import com.aegis_vitals.client.config.AegisConfig;
import com.aegis_vitals.client.gui.AegisVitalsOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.Locale;

@EventBusSubscriber(modid = AegisVitals.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class AegisClientEvents {
    private AegisClientEvents() {}

    /**
     * HIGHEST priority + receiveCanceled so Aegis still renders when another mod
     * already cancelled the vanilla layer (AEGIS_PRIORITY mode).
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void onRenderGuiLayerPre(RenderGuiLayerEvent.Pre event) {
        AegisConfig.HudMode mode = AegisConfig.HUD_MODE.get();
        if (mode == AegisConfig.HudMode.DISABLED) return;
        if (mode == AegisConfig.HudMode.AUTO && AegisConfig.isExternalHealthHudLoaded()) return;

        ResourceLocation layer = event.getName();

        // Suppress other mods' health/armor layers (never touches food, air, hotbar, chat...)
        if (mode == AegisConfig.HudMode.AEGIS_PRIORITY && isForeignHealthOrArmorLayer(layer)) {
            event.setCanceled(true);
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.gameMode == null) return;

        if (VanillaGuiLayers.PLAYER_HEALTH.equals(layer)) {
            event.setCanceled(true);
            if (mc.gameMode.canHurtPlayer()) {
                AegisVitalsOverlay.renderTieredHearts(event.getGuiGraphics(), mc, mc.player);
            }
        } else if (VanillaGuiLayers.ARMOR_LEVEL.equals(layer) && AegisConfig.ENABLE_TIERED_ARMOR.get()) {
            event.setCanceled(true);
            if (mc.gameMode.canHurtPlayer()) {
                AegisVitalsOverlay.renderTieredArmor(event.getGuiGraphics(), mc, mc.player);
            }
        }
    }

    private static boolean isForeignHealthOrArmorLayer(ResourceLocation layer) {
        String ns = layer.getNamespace();
        if (ns.equals("minecraft") || ns.equals(AegisVitals.MOD_ID)) return false;

        String path = layer.getPath().toLowerCase(Locale.ROOT);
        if (path.contains("food") || path.contains("hunger") || path.contains("saturation")
                || path.contains("mount") || path.contains("vehicle")) {
            return false;
        }
        return path.contains("health") || path.contains("heart")
                || path.contains("armor") || path.contains("armour")
                || path.contains("absorption");
    }
}
