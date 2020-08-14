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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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

import javax.annotation.Nonnull;

import java.util.List;

public class ItemRainbowRod extends Item implements IManaUsingItem, IAvatarWieldable {

	private static final Identifier avatarOverlay = new Identifier(LibResources.MODEL_AVATAR_RAINBOW);

	private static final int MANA_COST = 750;
	private static final int MANA_COST_AVATAR = 10;
	private static final int TIME = 600;

	public ItemRainbowRod(Settings props) {
		super(props);
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (!world.isClient && ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_COST, false)) {
			Block place = ModBlocks.bifrost;
			Vector3 vector = new Vector3(player.getRotationVector()).normalize();

			double x = player.getX();
			double y = player.getY();
			double z = player.getZ();
			BlockPos.Mutable pos = new BlockPos.Mutable((int) x, (int) y, (int) z);

			double lastX = 0;
			double lastY = -1;
			double lastZ = 0;
			BlockPos.Mutable lastChecker = new BlockPos.Mutable();

			int count = 0;
			boolean prof = IManaProficiencyArmor.hasProficiency(player, stack);
			int maxlen = prof ? 160 : 100;
			int time = prof ? (int) (TIME * 1.6) : TIME;

			BlockPos.Mutable placePos = new BlockPos.Mutable();
			while (count < maxlen) {
				lastChecker.set(lastX, lastY, lastZ);

				if (!lastChecker.equals(pos)) {
					if (y >= world.getHeight() || y <= 0
							|| !world.isAir(pos) && world.getBlockState(pos).getBlock() != place) {
						break;
					}

					for (int i = -2; i < 1; i++) {
						for (int j = -2; j < 1; j++) {
							placePos.set(pos.getX() + i, pos.getY(), pos.getZ() + j);
							if (world.isAir(placePos)
									|| world.getBlockState(placePos).getBlock() == place) {
								world.setBlockState(placePos, place.getDefaultState(), 2);
								TileBifrost tile = (TileBifrost) world.getBlockEntity(placePos);
								if (tile != null) {
									tile.ticks = time;
								}
							}

						}
					}
					count++;
				}

				lastX = x;
				lastY = y;
				lastZ = z;

				x += vector.x;
				y += vector.y;
				z += vector.z;
				pos.set(x, y, z);
			}

			if (count > 0) {
				world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.bifrostRod, SoundCategory.PLAYERS, 0.5F, 0.25F);
				ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_COST, false);
				player.getItemCooldownManager().set(this, TIME);
			}
		}

		return TypedActionResult.success(stack);
	}

	/* todo 1.16
	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		return itemStack.copy();
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return !getContainerItem(stack).isEmpty();
	}
	*/

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		BlockEntity te = (BlockEntity) tile;
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
							tileBifrost.ticks = 10;
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
