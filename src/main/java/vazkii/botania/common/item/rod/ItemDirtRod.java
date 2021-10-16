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
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.helper.PlayerHelper;

import javax.annotation.Nonnull;

public class ItemDirtRod extends Item implements IManaUsingItem {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_DIRT);

	static final int COST = 75;

	public ItemDirtRod(Properties props) {
		super(props);
		IBlockProvider.API.registerForItems((stack, c) -> new BlockProvider(stack), this);
		IAvatarWieldable.API.registerForItems((stack, c) -> new AvatarBehavior(), this);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		return place(ctx, Blocks.DIRT, COST, 0.35F, 0.2F, 0.05F);
	}

	public static InteractionResult place(UseOnContext ctx, Block block, int cost, float r, float g, float b) {
		Player player = ctx.getPlayer();
		ItemStack stack = ctx.getItemInHand();
		Level world = ctx.getLevel();
		Direction side = ctx.getClickedFace();
		BlockPos pos = ctx.getClickedPos();

		if (player != null && ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, false)) {
			int entities = world.getEntitiesOfClass(LivingEntity.class,
					new AABB(pos.relative(side), pos.relative(side).offset(1, 1, 1))).size();

			if (entities == 0) {
				InteractionResult result = PlayerHelper.substituteUse(ctx, new ItemStack(block));

				if (result.consumesAction()) {
					ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, true);
					SparkleParticleData data = SparkleParticleData.sparkle(1F, r, g, b, 5);
					for (int i = 0; i < 6; i++) {
						world.addParticle(data, pos.getX() + side.getStepX() + Math.random(), pos.getY() + side.getStepY() + Math.random(), pos.getZ() + side.getStepZ() + Math.random(), 0, 0, 0);
					}
					return result;
				}
			}

			return InteractionResult.FAIL;
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	protected static class BlockProvider implements IBlockProvider {
		private final ItemStack stack;

		protected BlockProvider(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public boolean provideBlock(Player player, ItemStack requestor, Block block, boolean doit) {
			if (block == Blocks.DIRT) {
				return (doit && ManaItemHandler.instance().requestManaExactForTool(requestor, player, COST, true)) ||
						(!doit && ManaItemHandler.instance().requestManaExactForTool(requestor, player, COST, false));
			}
			return false;
		}

		@Override
		public int getBlockCount(Player player, ItemStack requestor, Block block) {
			if (block == Blocks.DIRT) {
				return ManaItemHandler.instance().getInvocationCountForTool(requestor, player, COST);
			}
			return 0;
		}
	}

	protected static class AvatarBehavior implements IAvatarWieldable {
		@Override
		public void onAvatarUpdate(IAvatarTile tile) {
			BlockEntity te = tile.tileEntity();
			Level world = te.getLevel();
			if (!world.isClientSide && tile.getCurrentMana() >= COST && tile.getElapsedFunctionalTicks() % 4 == 0 && world.random.nextInt(8) == 0 && tile.isEnabled()) {
				BlockPos pos = ((BlockEntity) tile).getBlockPos().relative(tile.getAvatarFacing());
				BlockState state = world.getBlockState(pos);
				if (state.isAir()) {
					world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
					world.levelEvent(2001, pos, Block.getId(Blocks.DIRT.defaultBlockState()));
					tile.receiveMana(-COST);
				}
			}
		}

		@Override
		public ResourceLocation getOverlayResource(IAvatarTile tile) {
			return avatarOverlay;
		}
	}

}
