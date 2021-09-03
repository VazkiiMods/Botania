/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.List;

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
		LOGO = addPattern("logo", true);
		SAPLING = addPattern("sapling", true);
		TINY_POTATO = addPattern("tiny_potato", true);

		SPARK_DISPERSIVE = addPattern("spark_dispersive", true);
		SPARK_DOMINANT = addPattern("spark_dominant", true);
		SPARK_RECESSIVE = addPattern("spark_recessive", true);
		SPARK_ISOLATED = addPattern("spark_isolated", true);

		FISH = addPattern("fish", false);
		AXE = addPattern("axe", false);
		HOE = addPattern("hoe", false);
		PICKAXE = addPattern("pickaxe", false);
		SHOVEL = addPattern("shovel", false);
		SWORD = addPattern("sword", false);
	}

	private static LoomPattern addPattern(String name, boolean special) {
		return Registry.register(LoomPatterns.REGISTRY, prefix(name), new BotaniaLoomPattern(special));
	}

	static class BotaniaLoomPattern extends LoomPattern {
		public BotaniaLoomPattern(boolean special) {
			super(special);
		}

		@Override
		public ResourceLocation getSpriteId(String type) {
			ResourceLocation myId = LoomPatterns.REGISTRY.getKey(this);
			return new ResourceLocation("entity/" + type + "/botania_" + myId.getPath());
		}

		@Override
		public void addPatternLine(List<Component> lines, DyeColor color) {
			String colorName = color.getName();
			if (colorName.equals("light_blue")) {
				colorName = "lightBlue";
			} else if (colorName.equals("light_gray")) {
				colorName = "silver";
			}
			ResourceLocation id = LoomPatterns.REGISTRY.getKey(this);
			lines.add(new TranslatableComponent("block.minecraft.banner.botania_" + id.getPath() + "." + colorName).withStyle(ChatFormatting.GRAY));
		}
	}
}
