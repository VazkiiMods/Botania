/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 1, 2015, 2:23:26 AM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibItemNames;

public class ItemAutocraftingHalo extends ItemCraftingHalo {

	private static final ResourceLocation glowTexture = new ResourceLocation(LibResources.MISC_GLOW_CYAN);

	public ItemAutocraftingHalo() {
		super(LibItemNames.AUTOCRAFTING_HALO);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int pos, boolean equipped) {
		super.onUpdate(stack, world, entity, pos, equipped);

		if(entity instanceof EntityPlayer && !equipped) {
			EntityPlayer player = (EntityPlayer) entity;
			IItemHandler inv = getFakeInv(player);

			for(int i = 1; i < SEGMENTS; i++)
				tryCraft(player, stack, i, false, inv, false);
		}
	}

	@Override
	public ResourceLocation getGlowResource() {
		return glowTexture;
	}

}
