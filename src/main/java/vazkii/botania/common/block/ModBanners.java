/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.Registry;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatterns;

public final class ModBanners {

	public static LoomPattern FLOWER, LEXICON, LOGO, SAPLING, TINY_POTATO;
	public static LoomPattern SPARK_DISPERSIVE, SPARK_DOMINANT, SPARK_RECESSIVE, SPARK_ISOLATED;
	public static LoomPattern FISH, AXE, HOE, PICKAXE, SHOVEL, SWORD;

	public static void init() {
		FLOWER = addPattern("flower", true);
		LEXICON = addPattern("lexicon", true);
		LOGO = addPattern("logo", false); //todo terrasteel
		SAPLING = addPattern("sapling", true);
		TINY_POTATO = addPattern("tiny_potato", false); //todo tiny potato :)

		SPARK_DISPERSIVE = addPattern("spark_dispersive", true);
		SPARK_DOMINANT = addPattern("spark_dominant", true);
		SPARK_RECESSIVE = addPattern("spark_recessive", true);
		SPARK_ISOLATED = addPattern("spark_isolated", true);

		FISH = addPattern("fish", false); //todo cod
		AXE = addPattern("axe", false); //todo iron axe
		HOE = addPattern("hoe", false); //todo iron hoe
		PICKAXE = addPattern("pickaxe", false); //todo iron pickaxe
		SHOVEL = addPattern("shovel", false); //todo iron shovel
		SWORD = addPattern("sword", false); //todo iron sword
	}

	private static LoomPattern addPattern(String name, boolean special) {
		return Registry.register(LoomPatterns.REGISTRY, prefix(name), new LoomPattern(special));
	}
}
