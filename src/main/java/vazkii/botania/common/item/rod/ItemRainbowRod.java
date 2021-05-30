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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileBifrost;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.material.ItemSelfReturning;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemRainbowRod extends ItemSelfReturning implements IManaUsingItem, IAvatarWieldable {

	private static final Identifier avatarOverlay = new Identifier(LibResources.MODEL_AVATAR_RAINBOW);

	private static final int MANA_COST = 750;
	private static final int MANA_COST_AVATAR = 4;
	private static final int TIME = 600;

	public ItemRainbowRod(Settings props) {
		super(props);
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (!world.isClient && ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_COST, false)) {
			BlockState bifrost = ModBlocks.bifrost.getDefaultState();
			Vector3 vector = new Vector3(player.getRotationVector()).normalize();

			double x = player.getX();
			double y = player.getY() - 1;
			double z = player.getZ();
			BlockPos.Mutable pos = new BlockPos.Mutable((int) x, (int) y, (int) z);

			double lastX = 0;
			double lastY = -1;
			double lastZ = 0;
			BlockPos.Mutable previousPos = new BlockPos.Mutable();

			int count = 0;
			boolean placedAny = false;

			boolean prof = IManaProficiencyArmor.hasProficiency(player, stack);
			int maxlen = prof ? 160 : 100;
			int time = prof ? (int) (TIME * 1.6) : TIME;

			BlockPos.Mutable placePos = new BlockPos.Mutable();

			while (count < maxlen) {
				previousPos.set(lastX, lastY, lastZ);

				if (!previousPos.equals(pos)) { // Occasionally moving to the next segment stays on the same location, skip it
					if (!world.isAir(pos) && world.getBlockState(pos) != bifrost && count >= 4) {
						break; // Stop placing if you hit a wall (bifrost blocks are fine), but only after 4 segments.
					}
					if (World.isOutOfBuildLimitVertically(pos.getY())) {
						break;
					}
					if (placeBridgeSegment(world, pos, placePos, time)) {
						placedAny = true;
					}
				}

				count++;

				lastX = x;
				lastY = y;
				lastZ = z;

				x += vector.x;
				y += vector.y;
				z += vector.z;
				pos.set(x, y, z);
			}

			if (placedAny) {
				world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.bifrostRod, SoundCategory.PLAYERS, 0.5F, 0.25F);
				ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_COST, false);
				player.getItemCooldownManager().set(this, player.isCreative() ? 10 : TIME);
			}
		}

		return TypedActionResult.success(stack);
	}

	private static boolean placeBridgeSegment(World world, BlockPos center, BlockPos.Mutable placePos, int time) {
		BlockState bifrost = ModBlocks.bifrost.getDefaultState();
		boolean placed = false;

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				placePos.set(center.getX() + i, center.getY(), center.getZ() + j);
				if (world.isAir(placePos) || world.getBlockState(placePos) == bifrost) {
					world.setBlockState(placePos, bifrost, 2);

					TileBifrost tile = (TileBifrost) world.getBlockEntity(placePos);
					if (tile != null) {
						tile.ticks = time;
						placed = true;
					}
				}
			}
		}
		return placed;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		BlockEntity te = tile.tileEntity();
		World world = te.getWorld();

		if (world.isClient || tile.getCurrentMana() < MANA_COST_AVATAR * 25 || !tile.isEnabled()) {
			return;
		}

		BlockPos tePos = te.getPos();
		int w = 1;
		int h = 1;
		int l = 20;

		Box axis = null;
		switch (world.getBlockState(tePos).get(Properties.HORIZONTAL_FACING)) {
		case NORTH:
			axis = new Box(tePos.add(-w, -h, -l), tePos.add(w + 1, h, 0));
			break;
		case SOUTH:
			axis = new Box(tePos.add(-w, -h, 1), tePos.add(w + 1, h, l + 1));
			break;
		case WEST:
			axis = new Box(tePos.add(-l, -h, -w), tePos.add(0, h, w + 1));
			break;
		case EAST:
			axis = new Box(tePos.add(1, -h, -w), tePos.add(l + 1, h, w + 1));
			break;
		default:
		}

		List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, axis);
		for (PlayerEntity p : players) {
			int px = MathHelper.floor(p.getX());
			int py = MathHelper.floor(p.getY()) - 1;
			int pz = MathHelper.floor(p.getZ());
			int dist = 5;
			int diff = dist / 2;

			for (int i = 0; i < dist; i++) {
				for (int j = 0; j < dist; j++) {
					int ex = px + i - diff;
					int ez = pz + j - diff;

					if (!axis.contains(new Vec3d(ex + 0.5, py + 1, ez + 0.5))) {
						continue;
					}
					BlockPos pos = new BlockPos(ex, py, ez);
					Block block = world.getBlockState(pos).getBlock();
					if (world.getBlockState(pos).isAir()) {
						world.setBlockState(pos, ModBlocks.bifrost.getDefaultState());
						TileBifrost tileBifrost = (TileBifrost) world.getBlockEntity(pos);
						tileBifrost.ticks = 10;
						tile.receiveMana(-MANA_COST_AVATAR);
					} else if (block == ModBlocks.bifrost) {
						TileBifrost tileBifrost = (TileBifrost) world.getBlockEntity(pos);
						if (tileBifrost.ticks < 2) {
							tileBifrost.ticks += 10;
							tile.receiveMana(-MANA_COST_AVATAR);
						}
					}
				}
			}
		}

	}

	@Override
	public Identifier getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}

}
