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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.Botania;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemSpawnerMover extends ItemMod {

	private static final String TAG_SPAWNER = "spawner";
	private static final String TAG_SPAWN_DATA = "SpawnData";
	private static final String TAG_ID = "id";

	public ItemSpawnerMover(Properties props) {
		super(props);
		addPropertyOverride(new ResourceLocation("botania", "full"), (stack, worldIn, entityIn) -> hasData(stack) ? 1 : 0);
	}

	public static NBTTagCompound getSpawnerTag(ItemStack stack) {
		NBTTagCompound tag = stack.getTag();
		if(tag != null && tag.contains(TAG_SPAWNER)) {
			return tag.getCompound(TAG_SPAWNER);
		}

		return null;
	}

	private static ResourceLocation getEntityId(ItemStack stack) {
		NBTTagCompound tag = getSpawnerTag(stack);
		if(tag != null && tag.contains(TAG_SPAWN_DATA)) {
			tag = tag.getCompound(TAG_SPAWN_DATA);
			if(tag.contains(TAG_ID)) {
				return ResourceLocation.makeResourceLocation(tag.getString(TAG_ID));
			}
		}

		return null;
	}

	public static boolean hasData(ItemStack stack) {
		return getEntityId(stack) != null;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> infoList, ITooltipFlag flags) {
		ResourceLocation id = getEntityId(stack);
		if (id != null) {
			EntityType<?> type = ForgeRegistries.ENTITIES.getValue();
			if (type != null) {
				infoList.add(type.func_212546_e());
			}
		}
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float xOffset, float yOffset, float zOffset) {
		ItemStack itemstack = player.getHeldItem(hand);
		if(getEntityId(itemstack) == null) {
			if(world.getBlockState(pos).getBlock() == Blocks.SPAWNER) {
				if(!world.isRemote) {
					TileEntity te = world.getTileEntity(pos);
					NBTTagCompound tag = new NBTTagCompound();
					tag.put(TAG_SPAWNER, te.write(new NBTTagCompound()));
					player.getCooldownTracker().setCooldown(this, 20);
					itemstack.setTag(tag);
					world.destroyBlock(pos, false);
					UseItemSuccessTrigger.INSTANCE.trigger((EntityPlayerMP) player, itemstack, (WorldServer) world, pos.getX(), pos.getY(), pos.getZ());
					player.renderBrokenItemStack(itemstack);
				} else {
					for(int i = 0; i < 50; i++) {
						float red = (float) Math.random();
						float green = (float) Math.random();
						float blue = (float) Math.random();
						Botania.proxy.wispFX(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, red, green, blue, (float) Math.random() * 0.1F + 0.05F, (float) (Math.random() - 0.5F) * 0.15F, (float) (Math.random() - 0.5F) * 0.15F, (float) (Math.random() - 0.5F) * 0.15F);
					}
				}
				return EnumActionResult.SUCCESS;
			} else return EnumActionResult.PASS;
		} else {
			return placeBlock(itemstack, player, world, pos, side, xOffset, yOffset, zOffset) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
		}
	}

	// Adapted from ItemBlock.onItemUse
	private boolean placeBlock(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float xOffset, float yOffset, float zOffset) {
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if(!block.isReplaceable(world, pos)) {
			pos = pos.offset(side);
		}

		if(itemstack.isEmpty()) {
			return false;
		} else if(!player.canPlayerEdit(pos, side, itemstack)) {
			return false;
		} else if(world.mayPlace(Blocks.MOB_SPAWNER, pos, false, side, null)) {
			int meta = this.getMetadata(itemstack.getMetadata());
			IBlockState iblockstate1 = Blocks.MOB_SPAWNER.getStateForPlacement(world, pos, side, xOffset, yOffset, zOffset, meta, player);

			if (placeBlockAt(itemstack, player, world, pos, side, xOffset, yOffset, zOffset, iblockstate1)) {
				world.playSound(null, pos, Blocks.MOB_SPAWNER.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, (Blocks.MOB_SPAWNER.getSoundType().getVolume() + 1.0F) / 2.0F, Blocks.MOB_SPAWNER.getSoundType().getPitch() * 0.8F);
				player.renderBrokenItemStack(itemstack);
				itemstack.shrink(1);
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
		if(block == Blocks.SPAWNER) {
			TileEntity te = world.getTileEntity(pos);
			NBTTagCompound tag = stack.getTag();
			if (te instanceof TileEntityMobSpawner && tag.contains(TAG_SPAWNER)) {
				tag = tag.getCompound(TAG_SPAWNER);
				tag.putInt("x", pos.getX());
				tag.putInt("y", pos.getY());
				tag.putInt("z", pos.getZ());
				te.read(tag);
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
			}
		}

		return true;
	}
}
