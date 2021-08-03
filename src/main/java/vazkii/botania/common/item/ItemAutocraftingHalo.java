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

import vazkii.botania.client.lib.LibResources;

public class ItemAutocraftingHalo extends ItemCraftingHalo {

	private static final ResourceLocation glowTexture = new ResourceLocation(LibResources.MISC_GLOW_CYAN);

	public ItemAutocraftingHalo(Item.Properties props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int pos, boolean equipped) {
		super.inventoryTick(stack, world, entity, pos, equipped);

		if (!world.isClientSide && entity instanceof Player && !equipped) {
			Player player = (Player) entity;

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
