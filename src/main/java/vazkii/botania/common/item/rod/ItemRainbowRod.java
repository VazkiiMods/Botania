/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 20, 2014, 7:09:51 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileBifrost;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemRainbowRod extends ItemMod implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_RAINBOW);

	private static final int MANA_COST = 750;
	private static final int MANA_COST_AVATAR = 10;
	private static final int TIME = 600;

	public ItemRainbowRod() {
		super(LibItemNames.RAINBOW_ROD);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote && ManaItemHandler.requestManaExactForTool(stack, player, MANA_COST, false)) {
			Block place = ModBlocks.bifrost;
			Vector3 vector = new Vector3(player.getLookVec()).normalize();

			double x = player.posX;
			double y = player.posY;
			double z = player.posZ;
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos((int) x, (int) y, (int) z);

			double lastX = 0;
			double lastY = -1;
			double lastZ = 0;
			BlockPos.MutableBlockPos lastChecker = new BlockPos.MutableBlockPos();

			int count = 0;
			boolean prof = IManaProficiencyArmor.Helper.hasProficiency(player, stack);
			int maxlen = prof ? 160 : 100;
			int time = prof ? (int) (TIME * 1.6) : TIME;

			BlockPos.MutableBlockPos placePos = new BlockPos.MutableBlockPos();
			while (count < maxlen) {
				lastChecker.setPos(lastX, lastY, lastZ);

				if (!lastChecker.equals(pos)) {
					if (y >= world.getHeight() || y <= 0
							|| !world.isAirBlock(pos) && world.getBlockState(pos).getBlock() != place)
						break;

					for(int i = -2; i < 1; i++)
						for(int j = -2; j < 1; j++) {
							placePos.setPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
							if(world.isAirBlock(placePos)
									|| world.getBlockState(placePos).getBlock() == place) {
								world.setBlockState(placePos, place.getDefaultState(), 2);
								TileBifrost tile = (TileBifrost) world.getTileEntity(placePos);
								if(tile != null) {
									tile.ticks = time;
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
				pos.setPos(x, y, z);
			}

			if(count > 0) {
				world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.bifrostRod, SoundCategory.PLAYERS, 0.5F, 0.25F);
				ManaItemHandler.requestManaExactForTool(stack, player, MANA_COST, false);
				player.getCooldownTracker().setCooldown(this, TIME);

				// Fix up rods from old versions which used meta instead of cooldown tracker
				if (stack.getItemDamage() > 0)
					stack.setItemDamage(0);
			}
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		return itemStack.copy();
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return !getContainerItem(stack).isEmpty();
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		TileEntity te = (TileEntity) tile;
		World world = te.getWorld();

		if(world.isRemote || tile.getCurrentMana() < MANA_COST_AVATAR * 25 || !tile.isEnabled())
			return;

		BlockPos tePos = te.getPos();
		int w = 1;
		int h = 1;
		int l = 20;

		AxisAlignedBB axis = null;
		switch(world.getBlockState(tePos).getValue(BotaniaStateProps.CARDINALS)) {
		case NORTH :
			axis = new AxisAlignedBB(tePos.add(-w, -h, -l), tePos.add(w + 1, h, 0));
			break;
		case SOUTH :
			axis = new AxisAlignedBB(tePos.add(-w, -h, 1), tePos.add(w + 1, h, l + 1));
			break;
		case WEST :
			axis = new AxisAlignedBB(tePos.add(-l, -h, -w), tePos.add(0, h, w + 1));
			break;
		case EAST :
			axis = new AxisAlignedBB(tePos.add(1, -h, -w), tePos.add(l + 1, h, w + 1));
		default: break;
		}

		List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, axis);
		for(EntityPlayer p : players) {
			int px = MathHelper.floor(p.posX);
			int py = MathHelper.floor(p.posY) - 1;
			int pz = MathHelper.floor(p.posZ);
			int dist = 5;
			int diff = dist / 2;

			for(int i = 0; i < dist; i++)
				for(int j = 0; j < dist; j++) {
					int ex = px + i - diff;
					int ez = pz + j - diff;

					if(!axis.contains(new Vec3d(ex + 0.5, py + 1, ez + 0.5)))
						continue;
					BlockPos pos = new BlockPos(ex, py, ez);
					Block block = world.getBlockState(pos).getBlock();
					if(block.isAir(world.getBlockState(pos), world, pos)) {
						world.setBlockState(pos, ModBlocks.bifrost.getDefaultState());
						TileBifrost tileBifrost = (TileBifrost) world.getTileEntity(pos);
						tileBifrost.ticks = 10;
						tile.recieveMana(-MANA_COST_AVATAR);
					} else if(block == ModBlocks.bifrost) {
						TileBifrost tileBifrost = (TileBifrost) world.getTileEntity(pos);
						if(tileBifrost.ticks < 2) {
							tileBifrost.ticks = 10;
							tile.recieveMana(-MANA_COST_AVATAR);
						}
					}
				}
		}


	}

	@Override
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}

}
