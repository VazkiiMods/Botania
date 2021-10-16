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
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.Random;

public class ItemDiviningRod extends Item implements IManaUsingItem {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_DIVINING);

	static final int COST = 3000;

	public ItemDiviningRod(Properties props) {
		super(props);
		IAvatarWieldable.API.registerForItems((stack, c) -> new AvatarBehavior(), this);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player p, @Nonnull InteractionHand hand) {
		ItemStack stack = p.getItemInHand(hand);
		if (ManaItemHandler.instance().requestManaExactForTool(stack, p, COST, true)) {
			if (world.isClientSide) {
				int range = IManaProficiencyArmor.hasProficiency(p, stack) ? 20 : 15;
				long seedxor = world.random.nextLong();
				doHighlight(world, p.blockPosition(), range, seedxor);
			} else {
				world.playSound(null, p.getX(), p.getY(), p.getZ(), ModSounds.divinationRod, SoundSource.PLAYERS, 1F, 1F);
			}
			return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
		}

		return InteractionResultHolder.pass(stack);
	}

	private static void doHighlight(Level world, BlockPos pos, int range, long seedxor) {
		for (BlockPos pos_ : BlockPos.betweenClosed(pos.offset(-range, -range, -range),
				pos.offset(range, range, range))) {
			BlockState state = world.getBlockState(pos_);

			Block block = state.getBlock();
			if (state.is(ModTags.Blocks.ORES)) {
				Random rand = new Random(Registry.BLOCK.getKey(block).hashCode() ^ seedxor);
				WispParticleData data = WispParticleData.wisp(0.25F, rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 8, false);
				world.addParticle(data, pos_.getX() + world.random.nextFloat(),
						pos_.getY() + world.random.nextFloat(),
						pos_.getZ() + world.random.nextFloat(),
						0, 0, 0);
			}
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	protected static class AvatarBehavior implements IAvatarWieldable {
		@Override
		public void onAvatarUpdate(IAvatarTile tile) {
			BlockEntity te = (BlockEntity) tile;
			Level world = te.getLevel();
			if (tile.getCurrentMana() >= COST && tile.getElapsedFunctionalTicks() % 200 == 0 && tile.isEnabled()) {
				ItemDiviningRod.doHighlight(world, te.getBlockPos(), 18, te.getBlockPos().hashCode());
				tile.receiveMana(-COST);
			}
		}

		@Override
		public ResourceLocation getOverlayResource(IAvatarTile tile) {
			return avatarOverlay;
		}
	}
}
