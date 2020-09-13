/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.helper.PlayerHelper;

import javax.annotation.Nonnull;

public class ItemDirtRod extends Item implements IManaUsingItem, IBlockProvider, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_DIRT);

	static final int COST = 75;

	public ItemDirtRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		return place(ctx, Blocks.DIRT, COST, 0.35F, 0.2F, 0.05F);
	}

	public static ActionResultType place(ItemUseContext ctx, Block block, int cost, float r, float g, float b) {
		PlayerEntity player = ctx.getPlayer();
		ItemStack stack = ctx.getItem();
		World world = ctx.getWorld();
		Direction side = ctx.getFace();
		BlockPos pos = ctx.getPos();

		if (player != null && ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, false)) {
			int entities = world.getEntitiesWithinAABB(LivingEntity.class,
					new AxisAlignedBB(pos.offset(side), pos.offset(side).add(1, 1, 1))).size();

			if (entities == 0) {
				ActionResultType result = PlayerHelper.substituteUse(ctx, new ItemStack(block));

				if (result.isSuccessOrConsume()) {
					ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, true);
					SparkleParticleData data = SparkleParticleData.sparkle(1F, r, g, b, 5);
					for (int i = 0; i < 6; i++) {
						world.addParticle(data, pos.getX() + side.getXOffset() + Math.random(), pos.getY() + side.getYOffset() + Math.random(), pos.getZ() + side.getZOffset() + Math.random(), 0, 0, 0);
					}
					return result;
				}
			}

			return ActionResultType.FAIL;
		}

		return ActionResultType.PASS;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public boolean provideBlock(PlayerEntity player, ItemStack requestor, ItemStack stack, Block block, boolean doit) {
		if (block == Blocks.DIRT) {
			return !doit || ManaItemHandler.instance().requestManaExactForTool(requestor, player, COST, true);
		}
		return false;
	}

	@Override
	public int getBlockCount(PlayerEntity player, ItemStack requestor, ItemStack stack, Block block) {
		if (block == Blocks.DIRT) {
			return -1;
		}
		return 0;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		TileEntity te = tile.tileEntity();
		World world = te.getWorld();
		if (!world.isRemote && tile.getCurrentMana() >= COST && tile.getElapsedFunctionalTicks() % 4 == 0 && world.rand.nextInt(8) == 0 && tile.isEnabled()) {
			BlockPos pos = te.getPos().offset(tile.getAvatarFacing());
			BlockState state = world.getBlockState(pos);
			if (state.getBlock().isAir(state, world, pos)) {
				world.setBlockState(pos, Blocks.DIRT.getDefaultState());
				world.playEvent(2001, pos, Block.getStateId(Blocks.DIRT.getDefaultState()));
				tile.receiveMana(-COST);
			}
		}
	}

	@Override
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}

}
