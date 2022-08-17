/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.TileEntityBindableSpecialFlower;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.xplat.IXplatAbstractions;

public class ItemObedienceStick extends Item {

	public ItemObedienceStick(Properties props) {
		super(props);
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		return applyStick(world, pos) ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	public static boolean applyStick(Level world, BlockPos pos) {
		var state = world.getBlockState(pos);
		var be = world.getBlockEntity(pos);
		var receiver = IXplatAbstractions.INSTANCE.findManaReceiver(world, pos, state, be, null);
		if (receiver instanceof IManaPool || receiver instanceof IManaCollector) {
			int range = receiver instanceof IManaPool ? TileEntityFunctionalFlower.LINK_RANGE : TileEntityGeneratingFlower.LINK_RANGE;

			for (BlockPos iterPos : BlockPos.betweenClosed(pos.offset(-range, -range, -range), pos.offset(range, range, range))) {
				if (MathHelper.distSqr(iterPos, pos) > range * range) {
					continue;
				}

				BlockEntity tile = world.getBlockEntity(iterPos);
				if (tile instanceof TileEntityBindableSpecialFlower<?>bindable && bindable.wouldBeValidBinding(pos)) {
					bindable.setBindingPos(pos);
					ItemTwigWand.doParticleBeamWithOffset(world, iterPos, pos);
				}
			}

			return true;
		}

		return false;
	}
}
