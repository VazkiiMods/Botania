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

import com.google.common.base.Predicates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	final List<EnchantmentData> enchants = new ArrayList<>();

	private static final BlockPos[] OBSIDIAN_LOCATIONS = {
		new BlockPos(0, -1, 0),
		new BlockPos(0, -1, 1), new BlockPos(0, -1, -1), new BlockPos(1, -1, 0), new BlockPos(-1, -1, 0),
		new BlockPos(0, -1, 2), new BlockPos(-1, -1, 2), new BlockPos(1, -1, 2),
		new BlockPos(0, -1, -2), new BlockPos(-1, -1, -2), new BlockPos(1, -1, -2),
		new BlockPos(2, -1, 0), new BlockPos(2, -1, 1), new BlockPos(2, -1, -1 ),
		new BlockPos(-2, -1, 0), new BlockPos(-2, -1, 1), new BlockPos(-2, -1, -1)
	};

	private static final Map<EnumFacing.Axis, BlockPos[]> PYLON_LOCATIONS = new EnumMap<>(EnumFacing.Axis.class);

	static {
		PYLON_LOCATIONS.put(EnumFacing.Axis.X, new BlockPos[] { new BlockPos(-5, 1, 0), new BlockPos(5, 1, 0), new BlockPos(-4, 1, 3), new BlockPos(4, 1, 3), new BlockPos(-4, 1, -3 ), new BlockPos(4, 1, -3) });
		PYLON_LOCATIONS.put(EnumFacing.Axis.Z, new BlockPos[] { new BlockPos(0, 1, -5), new BlockPos(0, 1, 5), new BlockPos(3, 1, -4), new BlockPos(3, 1, 4), new BlockPos(-3, 1, -4 ), new BlockPos(-3, 1, 4) });
	}

	private static final BlockPos[] FLOWER_LOCATIONS = {
		new BlockPos(-1, 0, -1), new BlockPos(1, 0, -1), new BlockPos(-1, 0, 1), new BlockPos(1, 0, 1)
	};

	public static MultiblockSet makeMultiblockSet() {
		Multiblock mb = new Multiblock();

		for(BlockPos o : OBSIDIAN_LOCATIONS)
			mb.addComponent(o.up(), Blocks.OBSIDIAN.getDefaultState());
		for(BlockPos p : PYLON_LOCATIONS.get(EnumFacing.Axis.X)) {
			mb.addComponent(p.up(), ModBlocks.pylon.getDefaultState());
			mb.addComponent(new FlowerComponent(p, ModBlocks.flower));
		}
		for(BlockPos f : FLOWER_LOCATIONS)
			mb.addComponent(new FlowerComponent(f.up(), ModBlocks.flower));

		mb.addComponent(BlockPos.ORIGIN.up(), Blocks.LAPIS_BLOCK.getDefaultState());

		return mb.makeSet();
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		if(stage != 0 || itemToEnchant == null || !itemToEnchant.isItemEnchantable())
			return;

		List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - 2, pos.getY(), pos.getZ() - 2, pos.getX() + 3, pos.getY() + 1, pos.getZ() + 3));
		int count = items.size();

		if(count > 0 && !worldObj.isRemote) {
			for(EntityItem entity : items) {
				ItemStack item = entity.getEntityItem();
				if(item.getItem() == Items.ENCHANTED_BOOK) {
					NBTTagList enchants = Items.ENCHANTED_BOOK.getEnchantments(item);
					if(enchants != null && enchants.tagCount() > 0) {
						NBTTagCompound enchant = enchants.getCompoundTagAt(0);
						short id = enchant.getShort("id");
						if(isEnchantmentValid(Enchantment.getEnchantmentByID(id))) {
							advanceStage();
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public void update() {
		IBlockState state = worldObj.getBlockState(getPos());
		EnumFacing.Axis axis = state.getValue(BotaniaStateProps.ENCHANTER_DIRECTION);

		for(BlockPos pylon : PYLON_LOCATIONS.get(axis)) {
			TileEntity tile = worldObj.getTileEntity(pos.add(pylon));
			if(tile != null && tile instanceof TilePylon)
				((TilePylon) tile).activated = false;
		}

		if(!canEnchanterExist(worldObj, pos, axis)) {

			worldObj.setBlockState(pos, Blocks.LAPIS_BLOCK.getDefaultState(), 1 | 2);
			for(int i = 0; i < 50; i++) {
				float red = (float) Math.random();
				float green = (float) Math.random();
				float blue = (float) Math.random();
				Botania.proxy.wispFX(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, red, green, blue, (float) Math.random() * 0.15F + 0.15F, (float) (Math.random() - 0.5F) * 0.25F, (float) (Math.random() - 0.5F) * 0.25F, (float) (Math.random() - 0.5F) * 0.25F);
			}
			worldObj.playSound(null, pos, BotaniaSoundEvents.enchanterFade, SoundCategory.BLOCKS, 0.5F, 10F);
		}

		switch(stage) {
		case 1 : { // Get books
			if(stageTicks % 20 == 0) {
				List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - 2, pos.getY(), pos.getZ() - 2, pos.getX() + 3, pos.getY() + 1, pos.getZ() + 3));
				int count = items.size();
				boolean addedEnch = false;

				if(count > 0 && !worldObj.isRemote) {
					for(EntityItem entity : items) {
						ItemStack item = entity.getEntityItem();
						if(item.getItem() == Items.ENCHANTED_BOOK) {
							NBTTagList enchants = Items.ENCHANTED_BOOK.getEnchantments(item);
							if(enchants != null && enchants.tagCount() > 0) {
								NBTTagCompound enchant = enchants.getCompoundTagAt(0);
								short enchantId = enchant.getShort("id");
								short enchantLvl = enchant.getShort("lvl");
								Enchantment ench = Enchantment.getEnchantmentByID(enchantId);
								if(!hasEnchantAlready(ench) && isEnchantmentValid(ench)) {
									this.enchants.add(new EnchantmentData(ench, enchantLvl));
									worldObj.playSound(null, pos, BotaniaSoundEvents.ding, SoundCategory.BLOCKS, 1F, 1F);
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
			for(BlockPos pylon : PYLON_LOCATIONS.get(axis)) {
				TilePylon pylonTile = (TilePylon) worldObj.getTileEntity(pos.add(pylon));
				if(pylonTile != null) {
					pylonTile.activated = true;
					pylonTile .centerPos = pos;
				}
			}

			if(manaRequired == -1) {
				manaRequired = 0;
				for(EnchantmentData data : enchants) {
					manaRequired += (int) (5000F * ((15 - Math.min(15, data.enchantmentobj.getRarity().getWeight())) * 1.05F) * ((3F + data.enchantmentLevel * data.enchantmentLevel) * 0.25F) * (0.9F + enchants.size() * 0.05F));
				}
			} else if(mana >= manaRequired) {
				manaRequired = 0;
				for(BlockPos pylon : PYLON_LOCATIONS.get(axis))
					((TilePylon) worldObj.getTileEntity(pos.add(pylon))).activated = false;

				advanceStage();
			} else {
				ISparkEntity spark = getAttachedSpark();
				if(spark != null) {
					List<ISparkEntity> sparkEntities = SparkHelper.getSparksAround(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
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
					if(EnchantmentHelper.getEnchantmentLevel(data.enchantmentobj, itemToEnchant) == 0)
						itemToEnchant.addEnchantment(data.enchantmentobj, data.enchantmentLevel);

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

	private void craftingFanciness() {
		worldObj.playSound(null, pos, BotaniaSoundEvents.enchanterEnchant, SoundCategory.BLOCKS, 1F, 1F);
		for(int i = 0; i < 25; i++) {
			float red = (float) Math.random();
			float green = (float) Math.random();
			float blue = (float) Math.random();
			Botania.proxy.sparkleFX(worldObj, pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
		}
	}

	@Nonnull
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
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos);
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
		cmp.setInteger(TAG_MANA_REQUIRED, manaRequired);
		cmp.setInteger(TAG_STAGE, stage);
		cmp.setInteger(TAG_STAGE_TICKS, stageTicks);
		cmp.setInteger(TAG_STAGE_3_END_TICKS, stage3EndTicks);

		NBTTagCompound itemCmp = new NBTTagCompound();
		if(itemToEnchant != null)
			itemToEnchant.writeToNBT(itemCmp);
		cmp.setTag(TAG_ITEM, itemCmp);

		String enchStr = enchants.stream()
							.map(e -> Enchantment.REGISTRY.getNameForObject(e.enchantmentobj) + ":" + e.enchantmentLevel)
							.collect(Collectors.joining(","));
		cmp.setString(TAG_ENCHANTS, enchStr.isEmpty() ? enchStr : enchStr.substring(0, enchStr.length() - 1));
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
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
				Enchantment ench = Enchantment.getEnchantmentByLocation(entryTokens[0]);
				if(ench == null)
					ench = Enchantment.getEnchantmentByID(Integer.parseInt(entryTokens[0]));
				int lvl = Integer.parseInt(entryTokens[1]);
				enchants.add(new EnchantmentData(ench, lvl));
			}
		}
	}

	private boolean hasEnchantAlready(Enchantment enchant) {
		for(EnchantmentData data : enchants)
			if(data.enchantmentobj == enchant)
				return true;

		return false;
	}

	public boolean isEnchantmentValid(Enchantment ench) {
		if(!ench.canApply(itemToEnchant) || !ench.type.canEnchantItem(itemToEnchant.getItem()))
			return false;

		for(EnchantmentData data : enchants) {
			Enchantment otherEnch = data.enchantmentobj;
			if(!otherEnch.canApplyTogether(ench) || !ench.canApplyTogether(otherEnch))
				return false;
		}

		return true;
	}

	public static boolean canEnchanterExist(World world, BlockPos pos, EnumFacing.Axis axis) {
		for(BlockPos obsidian : OBSIDIAN_LOCATIONS)
			if(world.getBlockState(pos.add(obsidian)).getBlock() != Blocks.OBSIDIAN)
				return false;

		for(BlockPos pylon : PYLON_LOCATIONS.get(axis))
			if(world.getBlockState(pos.add(pylon)).getBlock() != ModBlocks.pylon || !BotaniaAPI.internalHandler.isBotaniaFlower(world, pos.add(pylon).down()))
				return false;

		for(BlockPos flower : FLOWER_LOCATIONS)
			if(!BotaniaAPI.internalHandler.isBotaniaFlower(world, pos.add(flower)))
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
		List<Entity> sparks = worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1), Predicates.instanceOf(ISparkEntity.class));
		if(sparks.size() == 1) {
			Entity e = sparks.get(0);
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

}
