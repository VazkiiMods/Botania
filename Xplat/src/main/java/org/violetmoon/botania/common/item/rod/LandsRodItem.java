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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.Avatar;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.item.BlockProvider;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.helper.PlayerHelper;

public class LandsRodItem extends Item {

	private static final ResourceLocation AVATAR_OVERLAY = ResourceLocation.parse(ResourcesLib.MODEL_AVATAR_DIRT);

	static final int COST = 75;

	public LandsRodItem(Properties props) {
		super(props);
	}

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
			InteractionResult result = PlayerHelper.substituteUse(ctx, new ItemStack(block));

			if (result.consumesAction()) {
				ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, true);
				SparkleParticleData data = SparkleParticleData.sparkle(1F, r, g, b, 5);
				for (int i = 0; i < 6; i++) {
					world.addParticle(data, pos.getX() + side.getStepX() + Math.random(), pos.getY() + side.getStepY() + Math.random(), pos.getZ() + side.getStepZ() + Math.random(), 0, 0, 0);
				}
				return result;
			}

			return InteractionResult.FAIL;
		}

		return InteractionResult.PASS;
	}

	public record BlockProviderImpl(ItemStack requestor, Player player) implements BlockProvider {

		@Override
		public boolean provideBlock(Block block, boolean doit) {
			return block == Blocks.DIRT
					&& ManaItemHandler.instance().requestManaExactForTool(requestor, player, COST, doit);
		}

		@Override
		public int getBlockCount(Block block) {
			return block == Blocks.DIRT
					? ManaItemHandler.instance().getInvocationCountForTool(requestor, player, COST)
					: 0;
		}

		@Override
		public Block getProvidedBlock() {
			return Blocks.DIRT;
		}
	}

	public record AvatarBehavior(ItemStack rod, Avatar avatar) implements AvatarWieldable {
		@Override
		public void onAvatarUpdate(ServerLevel world, BlockPos blockPos, ManaReceiver receiver) {
			if (receiver.getCurrentMana() >= COST && avatar.isEnabled() && getTimeSinceLastActivation(world) >= 4) {
				if (world.random.nextInt(8) == 0) {
					BlockPos pos = blockPos.relative(avatar.getAvatarFacing());
					BlockState state = world.getBlockState(pos);
					if (state.isAir()) {
						world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
						world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(Blocks.DIRT.defaultBlockState()));
						receiver.receiveMana(-COST);
					}
				}
				setLastActivationTime(world);
			}
		}

		@Override
		public ResourceLocation getOverlayResource() {
			return AVATAR_OVERLAY;
		}
	}

}
