/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 17, 2015, 1:32:58 AM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;
import vazkii.botania.common.lib.LibItemNames;

public class ItemSlimeBottle extends ItemMod {

	public ItemSlimeBottle() {
		super(LibItemNames.SLIME_BOTTLE);
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int something, boolean somethingelse) {
		if(!world.isRemote) {
			int x = MathHelper.floor(entity.posX);
			int z = MathHelper.floor(entity.posZ);
			boolean slime = SubTileNarslimmus.isSlimeChunk(world, x, z);
			int meta = stack.getItemDamage();
			int newMeta = slime ? 1 : 0;
			if(meta != newMeta)
				stack.setItemDamage(newMeta);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemAppendMeta(this, 2, LibItemNames.SLIME_BOTTLE);
	}

}
