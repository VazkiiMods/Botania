/**
 * This class was created by <PowerCrystals>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.lib.LibItemNames;

public class ItemSpawnerMover extends ItemMod {

	public static final String TAG_SPAWNER = "spawner";
	private static final String TAG_PLACE_DELAY = "placeDelay";

	public ItemSpawnerMover() {
		super(LibItemNames.SPAWNER_MOVER);
		setMaxStackSize(1);
		addPropertyOverride(new ResourceLocation("botania", "full"), (stack, worldIn, entityIn) -> hasData(stack) ? 1 : 0);
	}

	public static NBTTagCompound getSpawnerTag(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag != null) {
			if(tag.hasKey(TAG_SPAWNER))
				return tag.getCompoundTag(TAG_SPAWNER);
			if(tag.hasKey("EntityId"))
				return tag;
		}

		return null;
	}

	private static String getEntityId(ItemStack stack) {
		NBTTagCompound tag = getSpawnerTag(stack);
		if(tag != null)
			return tag.getString("EntityId");

		return null;
	}

	public static boolean hasData(ItemStack stack) {
		return getEntityId(stack) != null;
	}

	private static int getDelay(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag != null)
			return tag.getInteger(TAG_PLACE_DELAY);

		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> infoList, boolean advancedTooltips) {
		String id = getEntityId(stack);
		if (id != null)
			infoList.add(I18n.format("entity." + id + ".name"));
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag != null && tag.hasKey(TAG_PLACE_DELAY) && tag.getInteger(TAG_PLACE_DELAY) > 0)
			tag.setInteger(TAG_PLACE_DELAY, tag.getInteger(TAG_PLACE_DELAY) - 1);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float xOffset, float yOffset, float zOffset) {
		if(getEntityId(itemstack) == null) {
			if(world.getBlockState(pos).getBlock().equals(Blocks.MOB_SPAWNER)) {
				TileEntity te = world.getTileEntity(pos);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setTag(TAG_SPAWNER, new NBTTagCompound());
				te.writeToNBT(tag.getCompoundTag(TAG_SPAWNER));
				tag.setInteger(TAG_PLACE_DELAY, 20);
				itemstack.setTagCompound(tag);
				world.setBlockToAir(pos);
				player.renderBrokenItemStack(itemstack);
				for(int i = 0; i < 50; i++) {
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();
					Botania.proxy.wispFX(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, red, green, blue, (float) Math.random() * 0.1F + 0.05F, (float) (Math.random() - 0.5F) * 0.15F, (float) (Math.random() - 0.5F) * 0.15F, (float) (Math.random() - 0.5F) * 0.15F);
				}
				return EnumActionResult.SUCCESS;
			} else return EnumActionResult.PASS;
		} else {
			return getDelay(itemstack) <= 0 && placeBlock(itemstack, player, world, pos, side, xOffset, yOffset, zOffset) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
		}
	}

	// Adapted from ItemBlock.onItemUse
	private boolean placeBlock(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float xOffset, float yOffset, float zOffset) {
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if(!block.isReplaceable(world, pos)) {
			pos = pos.offset(side);
		}

		if(itemstack.stackSize == 0) {
			return false;
		} else if(!player.canPlayerEdit(pos, side, itemstack)) {
			return false;
		} else if(world.canBlockBePlaced(Blocks.MOB_SPAWNER, pos, false, side, null, itemstack)) {
			int meta = this.getMetadata(itemstack.getMetadata());
			IBlockState iblockstate1 = Blocks.MOB_SPAWNER.getStateForPlacement(world, pos, side, xOffset, yOffset, zOffset, meta, player);

			if (placeBlockAt(itemstack, player, world, pos, side, xOffset, yOffset, zOffset, iblockstate1)) {
				world.playSound(null, pos, Blocks.MOB_SPAWNER.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, (Blocks.MOB_SPAWNER.getSoundType().getVolume() + 1.0F) / 2.0F, Blocks.MOB_SPAWNER.getSoundType().getPitch() * 0.8F);
				--itemstack.stackSize;
				player.renderBrokenItemStack(itemstack);
				player.addStat(ModAchievements.spawnerMoverUse, 1);
				for(int i = 0; i < 100; i++)
					Botania.proxy.sparkleFX(pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 0.45F + 0.2F * (float) Math.random(), 6);
			}

			return true;
		} else {
			return false;
		}
	}

	// Adapted from ItemBlock.placeBlockAt
	private boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState state) {
		if (!world.setBlockState(pos, state, 3))
			return false;

		Block block = world.getBlockState(pos).getBlock();
		if(block.equals(Blocks.MOB_SPAWNER)) {
			TileEntity te = world.getTileEntity(pos);
			NBTTagCompound tag = stack.getTagCompound();
			if (tag.hasKey(TAG_SPAWNER))
				tag = tag.getCompoundTag(TAG_SPAWNER);
			tag.setInteger("x", pos.getX());
			tag.setInteger("y", pos.getY());
			tag.setInteger("z", pos.getZ());
			te.readFromNBT(tag);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
		}

		return true;
	}
}
