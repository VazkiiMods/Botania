/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 30, 2014, 4:49:16 PM (GMT)]
 */
package vazkii.botania.common.item.material;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.EntityEnderAirBottle;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

// todo 1.13 further break this up
public class ItemManaResource extends ItemMod implements IFlowerComponent, IElvenItem {
	public ItemManaResource(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		ItemStack stack = player.getHeldItem(hand);

		if(this == ModItems.terrasteel || this == ModItems.gaiaIngot)
			return EntityDoppleganger.spawn(player, stack, world, pos, this == ModItems.gaiaIngot) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		else if(this == ModItems.livingroot && net.minecraft.item.ItemDye.applyBonemeal(stack, world, pos, player, hand)) {
			if(!world.isRemote)
				world.playEvent(2005, pos, 0);

			return EnumActionResult.SUCCESS;
		}

		return super.onItemUse(player, world, pos, hand, side, par8, par9, par10);
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return 0x9b0000;
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return this == ModItems.elementium || this == ModItems.pixieDust || this == ModItems.dragonstone;
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		return this == ModItems.placeholder ? itemStack.copy() : ItemStack.EMPTY;
	}

}
