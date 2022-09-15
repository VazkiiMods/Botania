/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import vazkii.botania.client.lib.ResourcesLib;

public class ManufactoryHaloItem extends AssemblyHaloItem {

	private static final ResourceLocation glowTexture = new ResourceLocation(ResourcesLib.MISC_GLOW_CYAN);

	public ManufactoryHaloItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int pos, boolean equipped) {
		super.inventoryTick(stack, world, entity, pos, equipped);

		if (!world.isClientSide && entity instanceof Player player && !equipped) {

			for (int i = 1; i < SEGMENTS; i++) {
				tryCraft(player, stack, i, false);
			}
		}
	}

	@Override
	public ResourceLocation getGlowResource() {
		return glowTexture;
	}

}
