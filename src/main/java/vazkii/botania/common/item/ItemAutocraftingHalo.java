/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import vazkii.botania.client.lib.LibResources;

public class ItemAutocraftingHalo extends ItemCraftingHalo {

	private static final Identifier glowTexture = new Identifier(LibResources.MISC_GLOW_CYAN);

	public ItemAutocraftingHalo(Item.Settings props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int pos, boolean equipped) {
		super.inventoryTick(stack, world, entity, pos, equipped);

		if (!world.isClient && entity instanceof PlayerEntity && !equipped) {
			PlayerEntity player = (PlayerEntity) entity;

			for (int i = 1; i < SEGMENTS; i++) {
				tryCraft(player, stack, i, false);
			}
		}
	}

	@Override
	public Identifier getGlowResource() {
		return glowTexture;
	}

}
