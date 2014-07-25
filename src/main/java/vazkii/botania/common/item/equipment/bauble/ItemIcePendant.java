/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 26, 2014, 2:06:17 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;

public class ItemIcePendant extends ItemBauble {

	public static Map<String, List<IceRemover>> playerIceBlocks = new HashMap();

	public ItemIcePendant() {
		super(LibItemNames.ICE_PENDANT);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		super.onWornTick(stack, entity);

		if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;

			if(!player.worldObj.isRemote)
				tickIceRemovers(player);

			if(!player.isSneaking() && !player.isInsideOfMaterial(Material.water)) {
				int x = MathHelper.floor_double(player.posX);
				int y = MathHelper.floor_double(player.posY - (player.isInWater() ? 0 : 1) - (player.worldObj.isRemote ? 1 : 0));
				int z = MathHelper.floor_double(player.posZ);

				int range = 3;
				for(int i = -range - 1; i < range; i++)
					for(int j = -range; j < range + 1; j++) {
						int x1 = x + i;
						int z1 = z + j;

						addIceBlock(player, new ChunkCoordinates(x1, y, z1));
					}
			}
		}
	}

	private void addIceBlock(EntityPlayer player, ChunkCoordinates coords) {
		String user = player.getCommandSenderName();
		if(!playerIceBlocks.containsKey(user))
			playerIceBlocks.put(user, new ArrayList());

		List<IceRemover> ice = playerIceBlocks.get(user);
		if(player.worldObj.getBlock(coords.posX, coords.posY, coords.posZ) == Blocks.water && player.worldObj.getBlockMetadata(coords.posX, coords.posY, coords.posZ) == 0) {
			player.worldObj.setBlock(coords.posX, coords.posY, coords.posZ, Blocks.ice);

			if(!player.worldObj.isRemote)
				ice.add(new IceRemover(coords));
		}
	}

	private void tickIceRemovers(EntityPlayer player) {
		String user = player.getCommandSenderName();
		if(!playerIceBlocks.containsKey(user))
			return;

		List<IceRemover> removers = playerIceBlocks.get(user);
		for(IceRemover ice : new ArrayList<IceRemover>(removers))
			ice.tick(player.worldObj, removers);
	}

	class IceRemover {

		int time = 30;
		final ChunkCoordinates coords;

		public IceRemover(ChunkCoordinates coords) {
			this.coords = coords;
		}

		public void tick(World world, List<IceRemover> list) {
			if(world.getBlock(coords.posX, coords.posY, coords.posZ) == Blocks.ice) {
				if(time-- == 0)
					world.setBlock(coords.posX, coords.posY, coords.posZ, Blocks.water, 0, 1 | 2);
				else return;
				list.remove(this);
			}
		}
	}
}
