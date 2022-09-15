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
import net.minecraft.world.level.block.entity.BannerPattern;

import vazkii.botania.common.item.material.ManaResourceItem;
import vazkii.botania.common.lib.ModTags;

public class TerrasteelIngotItem extends ManaResourceItem implements ItemWithBannerPattern {
	public TerrasteelIngotItem(Properties props) {
		super(props);
	}

	@Override
	public TagKey<BannerPattern> getBannerPattern() {
		return ModTags.BannerPatterns.PATTERN_ITEM_TERRASTEEL;
	}
}
