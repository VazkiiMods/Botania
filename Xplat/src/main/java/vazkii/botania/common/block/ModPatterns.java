/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;

import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class ModPatterns {
	private static final List<BannerPattern> ALL = new ArrayList<>();
	public static final BannerPattern FLOWER = make(LibMisc.MOD_ID + ":flower");
	public static final BannerPattern LEXICON = make(LibMisc.MOD_ID + ":lexicon");
	public static final BannerPattern LOGO = make(LibMisc.MOD_ID + ":logo");
	public static final BannerPattern SAPLING = make(LibMisc.MOD_ID + ":sapling");
	public static final BannerPattern TINY_POTATO = make(LibMisc.MOD_ID + ":tiny_potato");
	public static final BannerPattern SPARK_DISPERSIVE = make(LibMisc.MOD_ID + ":spark_dispersive");
	public static final BannerPattern SPARK_DOMINANT = make(LibMisc.MOD_ID + ":spark_dominant");
	public static final BannerPattern SPARK_RECESSIVE = make(LibMisc.MOD_ID + ":spark_recessive");
	public static final BannerPattern SPARK_ISOLATED = make(LibMisc.MOD_ID + ":spark_isolated");

	public static final BannerPattern FISH = make(LibMisc.MOD_ID + ":fish");
	public static final BannerPattern AXE = make(LibMisc.MOD_ID + ":axe");
	public static final BannerPattern HOE = make(LibMisc.MOD_ID + ":hoe");
	public static final BannerPattern PICKAXE = make(LibMisc.MOD_ID + ":pickaxe");
	public static final BannerPattern SHOVEL = make(LibMisc.MOD_ID + ":shovel");
	public static final BannerPattern SWORD = make(LibMisc.MOD_ID + ":sword");

	private static BannerPattern make(String hashName) {
		var pattern = new BannerPattern(hashName);
		ALL.add(pattern);
		return pattern;
	}

	public static void submitRegistrations(BiConsumer<BannerPattern, ResourceLocation> consumer) {
		for (var pattern : ALL) {
			consumer.accept(pattern, new ResourceLocation(pattern.getHashname()));
		}
	}
}
