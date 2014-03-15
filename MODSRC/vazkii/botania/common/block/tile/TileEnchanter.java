/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 15, 2014, 4:57:52 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;

public class TileEnchanter extends TileMod implements IManaReceiver {

	private static final String TAG_STAGE = "stage";
	private static final String TAG_STAGE_TICKS = "stageTicks";
	private static final String TAG_MANA_REQUIRED = "manaRequired";
	private static final String TAG_MANA = "mana";
	private static final String TAG_ITEM = "item";
	private static final String TAG_ENCHANTS = "enchs";

	public int stage = -1;
	public int stageTicks = 0;
	
	int manaRequired = -1;
	int mana = 0;
	
	public ItemStack itemToEnchant = null;
	List<EnchantmentData> enchants = new ArrayList();
	
	private static final int[][] OBSIDIAN_LOCATIONS = new int[][] {
		{ 0, -1, 0 },
		{ 0, -1, 1 }, { 0, -1, -1 }, { 1, -1, 0 }, { -1, -1, 0 },
		{ 0, -1, 2 }, { -1, -1, 2 }, { 1, -1, 2 },
		{ 0, -1, -2 }, { -1, -1, -2 }, { 1, -1, -2 },
		{ 2, -1, 0 }, { 2, -1, 1 }, { 2, -1, -1 },
		{ -2, -1, 0 }, { -2, -1, 1 }, { -2, -1, -1 }
	};

	private static final int[][][] PYLON_LOCATIONS = new int[][][] { 
		{ { -5, 1, 0 }, { 5, 1, 0 }, { -4, 1, 3 }, { 4, 1, 3 }, { -4, 1, -3 }, { 4, 1, -3 } }, 
		{ { 0, 1, -5 }, { 0, 1, 5 }, { 3, 1, -4 }, { 3, 1, 4 }, { -3, 1, -4 }, { -3, 1, 4 } }
	};

	private static final int[][] FLOWER_LOCATIONS = new int[][] {
		{ -1, 0, -1 }, { 1, 0, -1 }, { -1, 0, 1 }, { 1, 0, 1 } 
	};
	
	public void onWanded(EntityPlayer player, ItemStack wand) {
		if(stage != 0 || itemToEnchant == null)
			return;
		
		List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord - 2, yCoord, zCoord - 2, xCoord + 3, yCoord + 1, zCoord + 3));
		int count = items.size();
		
		if(count > 0 && !worldObj.isRemote) {
			for(EntityItem entity : items) {
				ItemStack item = entity.getEntityItem();
				if(item.itemID == Item.enchantedBook.itemID) {
					NBTTagList enchants = item.getEnchantmentTagList();
					if(enchants.tagCount() > 0) {
						NBTTagCompound enchant = (NBTTagCompound) enchants.tagAt(0);
						short id = enchant.getShort("id");
						if(isEnchantmentValid(id)) {
							advanceStage();
							worldObj.playSoundEffect(xCoord, yCoord, zCoord, "random.orb", 1F, 1F);
							return;
						}
					}
				}
			}
		}
	}
	
	@Override
	public void updateEntity() {
		if(!canEnchanterExist(worldObj, xCoord, yCoord, zCoord, getBlockMetadata()))
			worldObj.setBlock(xCoord, yCoord, zCoord, Block.blockLapis.blockID, 0, 1 | 2);

		switch(stage) {
		case 1 : { // Get books
			if(stageTicks % 20 == 0) {
				List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord - 2, yCoord, zCoord - 2, xCoord + 3, yCoord + 1, zCoord + 3));
				int count = items.size();
				boolean addedEnch = false;
				
				if(count > 0 && !worldObj.isRemote) {
					for(EntityItem entity : items) {
						ItemStack item = entity.getEntityItem();
						if(item.itemID == Item.enchantedBook.itemID) {
							// XXX - This needs to be rewritten for the new book enchanting in 1.7
							NBTTagList enchants = item.getEnchantmentTagList();
							if(enchants.tagCount() > 0) {
								NBTTagCompound enchant = (NBTTagCompound) enchants.tagAt(0);
								short enchantId = enchant.getShort("id");
								short enchantLvl = enchant.getShort("lvl");
								if(!hasEnchantAlready(enchantId)) {
									this.enchants.add(new EnchantmentData(enchantId, enchantLvl));
									addedEnch = true;
									break;
								}
							}
						}
					}
				}
				
				if(!addedEnch) {
					if(enchants.isEmpty())
						stage = 0;
					else advanceStage();
				}
			}
			break;
		}
		case 2 : { // Get Mana
			if(manaRequired == -1) {
				manaRequired = 0;
				for(EnchantmentData data : enchants) {
					Enchantment ench = Enchantment.enchantmentsList[data.enchant];
					manaRequired += (int) (3000F * ((15 - ench.getWeight()) * 1.45F) * ((3F + (data.level * data.level)) * 0.25F) * (0.9F + enchants.size() * 0.05F));
				}
			} else if(mana > manaRequired) {
				manaRequired = 0;
				advanceStage();
			}
			
			break;
		}
		case 3 : { // Enchant
			if(stageTicks >= 100) {
				for(EnchantmentData data : enchants)
					itemToEnchant.addEnchantment(Enchantment.enchantmentsList[data.enchant], data.level);
				
				enchants.clear();
				mana = 0;
				
				craftingFanciness();
				advanceStage();
			}
			break;
		}
		case 4 : { // Reset
			if(stageTicks >= 20)
				advanceStage();
			
			break;
		}
		}

		if(stage != 0)
			stageTicks++;
	}

	public void advanceStage() {
		sync();
		
		stage++;
		if(stage == 5)
			stage = 0;

		stageTicks = 0;
	}
	
	public void craftingFanciness() {
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "random.levelup", 1F, 1F);
		for(int i = 0; i < 25; i++) {
			float red = (float) Math.random();
			float green = (float) Math.random();
			float blue = (float) Math.random();
			Botania.proxy.sparkleFX(worldObj, xCoord + 0.5 + Math.random() * 0.4 - 0.2, yCoord + 1, zCoord + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
		}
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana < manaRequired;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(manaRequired, mana); 
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return manaRequired > 0;
	}
	
	public void sync() {
		PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
		cmp.setInteger(TAG_MANA_REQUIRED, manaRequired);
		cmp.setInteger(TAG_STAGE, stage);
		cmp.setInteger(TAG_STAGE_TICKS, stageTicks);
		
		NBTTagCompound itemCmp = new NBTTagCompound();
		if(itemToEnchant != null)
			itemToEnchant.writeToNBT(itemCmp);
		cmp.setCompoundTag(TAG_ITEM, itemCmp);
		
		String enchStr = "";
		for(EnchantmentData data : enchants)
			enchStr = enchStr + data.enchant + ":" + data.level + ",";
		cmp.setString(TAG_ENCHANTS, enchStr.isEmpty() ? enchStr : enchStr.substring(0, enchStr.length() - 1));
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
		manaRequired = cmp.getInteger(TAG_MANA_REQUIRED);
		stage = cmp.getInteger(TAG_STAGE);
		stageTicks = cmp.getInteger(TAG_STAGE_TICKS);
		
		NBTTagCompound itemCmp = cmp.getCompoundTag(TAG_ITEM);
		itemToEnchant = ItemStack.loadItemStackFromNBT(itemCmp);
		
		enchants.clear();
		String enchStr = cmp.getString(TAG_ENCHANTS);
		String[] enchTokens = enchStr.split(",");
		for(String token : enchTokens) {
			String[] entryTokens = token.split(":");
			int id = Integer.parseInt(entryTokens[0]);
			int lvl = Integer.parseInt(entryTokens[1]);
			enchants.add(new EnchantmentData(id, lvl));
		}
	}
	
	private boolean hasEnchantAlready(int enchant) {
		for(EnchantmentData data : enchants)
			if(data.enchant == enchant)
				return true;
		
		return false;
	}
	
	public boolean isEnchantmentValid(short id) {
		Enchantment ench = Enchantment.enchantmentsList[id];
		if(!ench.canApply(itemToEnchant) || !ench.type.canEnchantItem(itemToEnchant.getItem()))
			return false;
		
		for(EnchantmentData data : enchants) {
			Enchantment otherEnch = Enchantment.enchantmentsList[data.enchant];
			if(!otherEnch.canApplyTogether(ench) || !ench.canApplyTogether(otherEnch))
				return false;
		}
		
		return true;
	}

	public static boolean canEnchanterExist(World world, int x, int y, int z, int meta) {
		for(int[] obsidian : OBSIDIAN_LOCATIONS)
			if(world.getBlockId(obsidian[0] + x, obsidian[1] + y, obsidian[2] + z) != Block.obsidian.blockID)
				return false;

		for(int[] pylon : PYLON_LOCATIONS[meta])
			if(world.getBlockId(pylon[0] + x, pylon[1] + y, pylon[2] + z) != ModBlocks.pylon.blockID || 
			world.getBlockId(pylon[0] + x, pylon[1] + y - 1, pylon[2] + z) != ModBlocks.flower.blockID)
				return false;

		for(int[] flower : FLOWER_LOCATIONS)
			if(world.getBlockId(flower[0] + x, flower[1] + y, flower[2] + z) != ModBlocks.flower.blockID)
				return false;

		return true;
	}
	
	private static class EnchantmentData {
		
		public int enchant, level;
		
		public EnchantmentData(int enchant, int level) {
			this.enchant = enchant;
			this.level = level;
		}
	}

}
