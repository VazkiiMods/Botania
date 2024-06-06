/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.block.Avatar;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.item.BlockProvider;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.xplat.XplatAbstractions;

public class DepthsRodItem extends Item {
	private static final ResourceLocation avatarOverlay = new ResourceLocation(ResourcesLib.MODEL_AVATAR_DIRT);

	public static final int COST = 150;

	public DepthsRodItem(Properties props) {
		super(props);
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		return LandsRodItem.place(ctx, Blocks.COBBLESTONE, COST, 0.3F, 0.3F, 0.3F);
	}

	public static class BlockProviderImpl implements BlockProvider {
		@Override
		public boolean provideBlock(Player player, ItemStack requestor, Block block, boolean doit) {
			if (block == Blocks.COBBLESTONE) {
				return (doit && ManaItemHandler.instance().requestManaExactForTool(requestor, player, COST, true)) ||
						(!doit && ManaItemHandler.instance().requestManaExactForTool(requestor, player, COST, false));
			}
			return false;
		}

		@Override
		public int getBlockCount(Player player, ItemStack requestor, Block block) {
			if (block == Blocks.COBBLESTONE) {
				return ManaItemHandler.instance().getInvocationCountForTool(requestor, player, COST);
			}
			return 0;
		}
	}

	public static class AvatarBehavior implements AvatarWieldable {
		@Override
		public void onAvatarUpdate(Avatar tile) {
			BlockEntity te = (BlockEntity) tile;
			Level world = te.getLevel();
			ManaReceiver receiver = XplatAbstractions.INSTANCE.findManaReceiver(world, te.getBlockPos(), te.getBlockState(), te, null);
			if (!world.isClientSide && receiver.getCurrentMana() >= COST && tile.getElapsedFunctionalTicks() % 4 == 0 && world.random.nextInt(8) == 0 && tile.isEnabled()) {
				BlockPos pos = te.getBlockPos().relative(tile.getAvatarFacing());
				BlockState state = world.getBlockState(pos);
				if (state.isAir()) {
					world.setBlockAndUpdate(pos, Blocks.COBBLESTONE.defaultBlockState());
					world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(Blocks.COBBLESTONE.defaultBlockState()));
					receiver.receiveMana(-COST);
				}
			}
		}

		@Override
		public ResourceLocation getOverlayResource(Avatar tile) {
			return avatarOverlay;
		}
	}

}
