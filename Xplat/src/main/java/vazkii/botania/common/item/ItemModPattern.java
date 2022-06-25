/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BannerPattern;

public class ItemModPattern extends Item implements ItemWithBannerPattern {
	private final TagKey<BannerPattern> pattern;

	public ItemModPattern(TagKey<BannerPattern> pattern, Properties settings) {
		super(settings);
		this.pattern = pattern;
	}

	@Override
	public TagKey<BannerPattern> getBannerPattern() {
		return pattern;
	}
}
