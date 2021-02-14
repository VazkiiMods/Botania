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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
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

	private static final Identifier avatarOverlay = new Identifier(LibResources.MODEL_AVATAR_DIRT);

	static final int COST = 75;

	public ItemDirtRod(Settings props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		return place(ctx, Blocks.DIRT, COST, 0.35F, 0.2F, 0.05F);
	}

	public static ActionResult place(ItemUsageContext ctx, Block block, int cost, float r, float g, float b) {
		PlayerEntity player = ctx.getPlayer();
		ItemStack stack = ctx.getStack();
		World world = ctx.getWorld();
		Direction side = ctx.getSide();
		BlockPos pos = ctx.getBlockPos();

		if (player != null && ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, false)) {
			int entities = world.getNonSpectatingEntities(LivingEntity.class,
					new Box(pos.offset(side), pos.offset(side).add(1, 1, 1))).size();

			if (entities == 0) {
				ActionResult result = PlayerHelper.substituteUse(ctx, new ItemStack(block));

				if (result.isAccepted()) {
					ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, true);
					SparkleParticleData data = SparkleParticleData.sparkle(1F, r, g, b, 5);
					for (int i = 0; i < 6; i++) {
						world.addParticle(data, pos.getX() + side.getOffsetX() + Math.random(), pos.getY() + side.getOffsetY() + Math.random(), pos.getZ() + side.getOffsetZ() + Math.random(), 0, 0, 0);
					}
					return result;
				}
			}

			return ActionResult.FAIL;
		}

		return ActionResult.PASS;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public boolean provideBlock(PlayerEntity player, ItemStack requestor, ItemStack stack, Block block, boolean doit) {
		if (block == Blocks.DIRT) {
			return (doit && ManaItemHandler.instance().requestManaExactForTool(requestor, player, COST, true)) ||
					(!doit && ManaItemHandler.instance().requestManaExactForTool(requestor, player, COST, false));
		}
		return false;
	}

	@Override
	public int getBlockCount(PlayerEntity player, ItemStack requestor, ItemStack stack, Block block) {
		if (block == Blocks.DIRT) {
			return ManaItemHandler.instance().getInvocationCountForTool(requestor, player, COST);
		}
		return 0;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		BlockEntity te = tile.tileEntity();
		World world = te.getWorld();
		if (!world.isClient && tile.getCurrentMana() >= COST && tile.getElapsedFunctionalTicks() % 4 == 0 && world.random.nextInt(8) == 0 && tile.isEnabled()) {
			BlockPos pos = ((BlockEntity) tile).getPos().offset(tile.getAvatarFacing());
			BlockState state = world.getBlockState(pos);
			if (state.isAir()) {
				world.setBlockState(pos, Blocks.DIRT.getDefaultState());
				world.syncWorldEvent(2001, pos, Block.getRawIdFromState(Blocks.DIRT.getDefaultState()));
				tile.receiveMana(-COST);
			}
		}
	}

	@Override
	public Identifier getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}

}
