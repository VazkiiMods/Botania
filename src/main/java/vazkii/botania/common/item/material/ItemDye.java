/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 19, 2014, 4:10:47 PM (GMT)]
 */
package vazkii.botania.common.item.material;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.ColorHelper;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.Item16Colors;

import javax.annotation.Nonnull;

public class ItemDye extends Item16Colors {

	public ItemDye(EnumDyeColor color, Properties props) {
		super(color, props);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemUseContext ctx) {
		ItemStack stack = ctx.getItem();
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		Block block = world.getBlockState(pos).getBlock();

		if(shouldRecolor(block)) {
			world.setBlockState(pos, recolor(block, color));
			stack.shrink(1);
			return EnumActionResult.SUCCESS;
		}

		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IManaPool) {
			IManaPool pool = (IManaPool) tile;
			if(color != pool.getColor()) {
				pool.setColor(color);
				stack.shrink(1);
				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		if(target instanceof EntitySheep) {
			EntitySheep entitysheep = (EntitySheep)target;

			if(!entitysheep.getSheared() && entitysheep.getFleeceColor() != color) {
				entitysheep.setFleeceColor(color);
				stack.shrink(1);
			}

			return true;
		}
		return false;
	}

	private boolean shouldRecolor(Block block) {
		EnumDyeColor woolColor = ColorHelper.WOOL_MAP.inverse().get(block);
		if(woolColor != null)
			return woolColor != this.color;

		EnumDyeColor carpetColor = ColorHelper.CARPET_MAP.inverse().get(block);
		if(carpetColor != null)
			return carpetColor != this.color;

		return false;
	}

	private IBlockState recolor(Block original, EnumDyeColor color) {
		if (ColorHelper.CARPET_MAP.values().contains(original)) {
			return ModBlocks.getCarpet(color).getDefaultState();
		} else {
			return ModBlocks.getWool(color).getDefaultState();
		}
	}

}
