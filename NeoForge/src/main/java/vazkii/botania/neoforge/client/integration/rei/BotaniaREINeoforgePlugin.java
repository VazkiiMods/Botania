package vazkii.botania.neoforge.client.integration.rei;

import me.shedaniel.rei.forge.REIPluginClient;

import vazkii.botania.client.integration.rei.BotaniaREIPlugin;

/*
 * For some reason REI requires two different ways to register its plugins for (Neo)Forge and Fabric, respectively.
 * The annotation used here is not available as dummy in the Fabric version, so we can't just apply it in Xplat.
 */
@REIPluginClient
public class BotaniaREINeoforgePlugin extends BotaniaREIPlugin {
}
