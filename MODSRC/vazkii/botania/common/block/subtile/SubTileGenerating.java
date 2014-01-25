/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 24, 2014, 8:03:36 PM (GMT)]
 */
package vazkii.botania.common.block.subtile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.api.subtile.SubTileEntity;

public class SubTileGenerating extends SubTileEntity {

	private static final String TAG_MANA = "mana";
	int mana;
	public int knownMana = 0;
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		int delay = getDelayBetweenPassiveGeneration();
		if(delay > 0 && supertile.worldObj.getWorldTime() % delay == 0)
			addMana(1);
	}
	
	public void addMana(int mana) {
		this.mana = Math.min(getMaxMana(), this.mana + mana);
	}
	
	public int getDelayBetweenPassiveGeneration() {
		return 30;
	}
	
	@Override
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		knownMana = mana;
		player.worldObj.playSoundAtEntity(player, "random.orb", 1F, 1F);
		
		// TODO
		if(!player.worldObj.isRemote)
			player.addChatMessage("mana: " + knownMana);
		
		return super.onWanded(player, wand);
	}
	
	public int getMaxMana() {
		return 2000;
	}
	
	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
	}
	
	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
	}
	
}
