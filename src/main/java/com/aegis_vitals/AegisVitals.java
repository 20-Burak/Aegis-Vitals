package com.aegis_vitals;

import com.aegis_vitals.client.config.AegisConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Aegis Vitals - client-only mod. Loading it with {@code dist = Dist.CLIENT} guarantees
 * that no client classes are ever touched on a dedicated server.
 */
@Mod(value = AegisVitals.MOD_ID, dist = Dist.CLIENT)
public class AegisVitals {
    public static final String MOD_ID = "aegis_vitals";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public AegisVitals(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, AegisConfig.SPEC);
        // Lets players edit the config in-game from the Mods menu.
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        LOGGER.info("Aegis Vitals initialized - single-row tiered health & armor.");
    }
}
