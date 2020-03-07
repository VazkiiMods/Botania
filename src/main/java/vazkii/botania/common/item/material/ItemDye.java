/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.material;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.ColorHelper;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.common.item.Item16Colors;

import javax.annotation.Nonnull;

public class ItemDye extends Item16Colors {

	public ItemDye(DyeColor color, Properties props) {
		super(color, props);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		ItemStack stack = ctx.getItem();
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		Block block = world.getBlockState(pos).getBlock();

		if (shouldRecolor(block)) {
			world.setBlockState(pos, recolor(block, color));
			stack.shrink(1);
			return ActionResultType.SUCCESS;
		}

		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IManaPool) {
			IManaPool pool = (IManaPool) tile;
			if (color != pool.getColor()) {
				pool.setColor(color);
				stack.shrink(1);
				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		if (target instanceof SheepEntity) {
			SheepEntity entitysheep = (SheepEntity) target;

			if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != color) {
				entitysheep.setFleeceColor(color);
				stack.shrink(1);
			}

			return true;
		}
		return false;
	}

	private boolean shouldRecolor(Block block) {
		DyeColor woolColor = ColorHelper.WOOL_MAP.inverse().get(block.delegate);
		if (woolColor != null) {
			return woolColor != this.color;
		}

		DyeColor carpetColor = ColorHelper.CARPET_MAP.inverse().get(block.delegate);
		if (carpetColor != null) {
			return carpetColor != this.color;
		}

		return false;
	}

	private BlockState recolor(Block original, DyeColor color) {
		if (ColorHelper.CARPET_MAP.containsValue(original.delegate)) {
			return ColorHelper.CARPET_MAP.get(color).get().getDefaultState();
		} else {
			return ColorHelper.WOOL_MAP.get(color).get().getDefaultState();
		}
	}

}
