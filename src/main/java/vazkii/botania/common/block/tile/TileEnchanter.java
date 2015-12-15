/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 15, 2014, 4:57:52 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.FlowerComponent;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;

public class TileEnchanter extends TileMod implements ISparkAttachable {

	private static final String TAG_STAGE = "stage";
	private static final String TAG_STAGE_TICKS = "stageTicks";
	private static final String TAG_STAGE_3_END_TICKS = "stage3EndTicks";
	private static final String TAG_MANA_REQUIRED = "manaRequired";
	private static final String TAG_MANA = "mana";
	private static final String TAG_ITEM = "item";
	private static final String TAG_ENCHANTS = "enchantsToApply";

	public int stage = 0;
	public int stageTicks = 0;

	public int stage3EndTicks = 0;

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

	public static MultiblockSet makeMultiblockSet() {
		Multiblock mb = new Multiblock();

		for(int[] o : OBSIDIAN_LOCATIONS)
			mb.addComponent(o[0], o[1] + 1, o[2], Blocks.obsidian, 0);
		for(int[] p : PYLON_LOCATIONS[0]) {
			mb.addComponent(p[0], p[1] + 1, p[2], ModBlocks.pylon, 0);
			mb.addComponent(new FlowerComponent(new ChunkCoordinates(p[0], p[1], p[2]), ModBlocks.flower));
		}
		for(int[] f : FLOWER_LOCATIONS)
			mb.addComponent(new FlowerComponent(new ChunkCoordinates(f[0], f[1] + 1, f[2]), ModBlocks.flower));

		mb.addComponent(0, 1, 0, Blocks.lapis_block, 0);

		return mb.makeSet();
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		if(stage != 0 || itemToEnchant == null || !itemToEnchant.isItemEnchantable())
			return;

		List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord - 2, yCoord, zCoord - 2, xCoord + 3, yCoord + 1, zCoord + 3));
		int count = items.size();

		if(count > 0 && !worldObj.isRemote) {
			for(EntityItem entity : items) {
				ItemStack item = entity.getEntityItem();
				if(item.getItem() == Items.enchanted_book) {
					NBTTagList enchants = Items.enchanted_book.func_92110_g(item);
					if(enchants != null && enchants.tagCount() > 0) {
						NBTTagCompound enchant = enchants.getCompoundTagAt(0);
						short id = enchant.getShort("id");
						if(isEnchantmentValid(id)) {
							advanceStage();
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public void updateEntity() {
		if(getBlockMetadata() < PYLON_LOCATIONS.length)
			for(int[] pylon : PYLON_LOCATIONS[getBlockMetadata()]) {
				TileEntity tile = worldObj.getTileEntity(xCoord + pylon[0], yCoord + pylon[1], zCoord + pylon[2]);
				if(tile != null && tile instanceof TilePylon)
					((TilePylon) tile).activated = false;
			}

		if(!canEnchanterExist(worldObj, xCoord, yCoord, zCoord, getBlockMetadata())) {

			worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.lapis_block, 0, 1 | 2);
			for(int i = 0; i < 50; i++) {
				float red = (float) Math.random();
				float green = (float) Math.random();
				float blue = (float) Math.random();
				Botania.proxy.wispFX(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, red, green, blue, (float) Math.random() * 0.15F + 0.15F, (float) (Math.random() - 0.5F) * 0.25F, (float) (Math.random() - 0.5F) * 0.25F, (float) (Math.random() - 0.5F) * 0.25F);
			}
			worldObj.playSoundEffect(xCoord, yCoord, zCoord, "botania:enchanterBlock", 0.5F, 10F);
		}

		switch(stage) {
		case 1 : { // Get books
			if(stageTicks % 20 == 0) {
				List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord - 2, yCoord, zCoord - 2, xCoord + 3, yCoord + 1, zCoord + 3));
				int count = items.size();
				boolean addedEnch = false;

				if(count > 0 && !worldObj.isRemote) {
					for(EntityItem entity : items) {
						ItemStack item = entity.getEntityItem();
						if(item.getItem() == Items.enchanted_book) {
							NBTTagList enchants = Items.enchanted_book.func_92110_g(item);
							if(enchants != null && enchants.tagCount() > 0) {
								NBTTagCompound enchant = enchants.getCompoundTagAt(0);
								short enchantId = enchant.getShort("id");
								short enchantLvl = enchant.getShort("lvl");
								if(!hasEnchantAlready(enchantId) && isEnchantmentValid(enchantId)) {
									this.enchants.add(new EnchantmentData(enchantId, enchantLvl));
									worldObj.playSoundEffect(xCoord, yCoord, zCoord, "botania:ding", 1F, 1F);
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
			for(int[] pylon : PYLON_LOCATIONS[getBlockMetadata()]) {
				TilePylon pylonTile = (TilePylon) worldObj.getTileEntity(xCoord + pylon[0], yCoord + pylon[1], zCoord + pylon[2]);
				if(pylonTile != null) {
					pylonTile.activated = true;
					pylonTile.centerX = xCoord;
					pylonTile.centerY = yCoord;
					pylonTile.centerZ = zCoord;
				}
			}

			if(manaRequired == -1) {
				manaRequired = 0;
				for(EnchantmentData data : enchants) {
					Enchantment ench = Enchantment.enchantmentsList[data.enchant];
					manaRequired += (int) (5000F * ((15 - Math.min(15, ench.getWeight())) * 1.05F) * ((3F + data.level * data.level) * 0.25F) * (0.9F + enchants.size() * 0.05F));
				}
			} else if(mana >= manaRequired) {
				manaRequired = 0;
				for(int[] pylon : PYLON_LOCATIONS[getBlockMetadata()])
					((TilePylon) worldObj.getTileEntity(xCoord + pylon[0], yCoord + pylon[1], zCoord + pylon[2])).activated = false;

				advanceStage();
			} else {
				ISparkEntity spark = getAttachedSpark();
				if(spark != null) {
					List<ISparkEntity> sparkEntities = SparkHelper.getSparksAround(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
					for(ISparkEntity otherSpark : sparkEntities) {
						if(spark == otherSpark)
							continue;

						if(otherSpark.getAttachedTile() != null && otherSpark.getAttachedTile() instanceof IManaPool)
							otherSpark.registerTransfer(spark);
					}
				}
			}

			break;
		}
		case 3 : { // Enchant
			if(stageTicks >= 100) {
				for(EnchantmentData data : enchants)
					if(EnchantmentHelper.getEnchantmentLevel(data.enchant, itemToEnchant) == 0)
						itemToEnchant.addEnchantment(Enchantment.enchantmentsList[data.enchant], data.level);

				enchants.clear();
				manaRequired = -1;
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
		stage++;

		if(stage == 4)
			stage3EndTicks = stageTicks;
		else if(stage == 5) {
			stage = 0;
			stage3EndTicks = 0;
		}

		stageTicks = 0;
		sync();
	}

	public void craftingFanciness() {
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "botania:enchanterEnchant", 1F, 1F);
		for(int i = 0; i < 25; i++) {
			float red = (float) Math.random();
			float green = (float) Math.random();
			float blue = (float) Math.random();
			Botania.proxy.sparkleFX(worldObj, xCoord + 0.5 + Math.random() * 0.4 - 0.2, yCoord + 1, zCoord + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= manaRequired;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(manaRequired, this.mana + mana);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return manaRequired > 0;
	}

	public void sync() {
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
		cmp.setInteger(TAG_MANA_REQUIRED, manaRequired);
		cmp.setInteger(TAG_STAGE, stage);
		cmp.setInteger(TAG_STAGE_TICKS, stageTicks);
		cmp.setInteger(TAG_STAGE_3_END_TICKS, stage3EndTicks);

		NBTTagCompound itemCmp = new NBTTagCompound();
		if(itemToEnchant != null)
			itemToEnchant.writeToNBT(itemCmp);
		cmp.setTag(TAG_ITEM, itemCmp);

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
		stage3EndTicks = cmp.getInteger(TAG_STAGE_3_END_TICKS);

		NBTTagCompound itemCmp = cmp.getCompoundTag(TAG_ITEM);
		itemToEnchant = ItemStack.loadItemStackFromNBT(itemCmp);

		enchants.clear();
		String enchStr = cmp.getString(TAG_ENCHANTS);
		if(!enchStr.isEmpty()) {
			String[] enchTokens = enchStr.split(",");
			for(String token : enchTokens) {
				String[] entryTokens = token.split(":");
				int id = Integer.parseInt(entryTokens[0]);
				int lvl = Integer.parseInt(entryTokens[1]);
				enchants.add(new EnchantmentData(id, lvl));
			}
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
			if(world.getBlock(obsidian[0] + x, obsidian[1] + y, obsidian[2] + z) != Blocks.obsidian)
				return false;

		for(int[] pylon : PYLON_LOCATIONS[meta])
			if(world.getBlock(pylon[0] + x, pylon[1] + y, pylon[2] + z) != ModBlocks.pylon || !BotaniaAPI.internalHandler.isBotaniaFlower(world, pylon[0] + x, pylon[1] + y - 1, pylon[2] + z))
				return false;

		for(int[] flower : FLOWER_LOCATIONS)
			if(!BotaniaAPI.internalHandler.isBotaniaFlower(world, flower[0] + x, flower[1] + y, flower[2] + z))
				return false;

		return true;
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public void attachSpark(ISparkEntity entity) {
		// NO-OP
	}

	@Override
	public ISparkEntity getAttachedSpark() {
		List<ISparkEntity> sparks = worldObj.getEntitiesWithinAABB(ISparkEntity.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
		if(sparks.size() == 1) {
			Entity e = (Entity) sparks.get(0);
			return (ISparkEntity) e;
		}

		return null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		return stage == 3;
	}

	@Override
	public int getAvailableSpaceForMana() {
		return Math.max(0, manaRequired - getCurrentMana());
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		if(manaRequired > 0 && itemToEnchant != null) {
			int x = res.getScaledWidth() / 2 + 20;
			int y = res.getScaledHeight() / 2 - 8;

			RenderHelper.renderProgressPie(x, y, (float) mana / (float) manaRequired, itemToEnchant);
		}
	}

	private static class EnchantmentData {

		public int enchant, level;

		public EnchantmentData(int enchant, int level) {
			this.enchant = enchant;
			this.level = level;
		}
	}

}
