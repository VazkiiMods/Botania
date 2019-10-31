/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 8, 2014, 5:25:32 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import java.util.List;

public class TileTerraPlate extends TileMod implements ISparkAttachable, ITickableTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.TERRA_PLATE)
	public static TileEntityType<TileTerraPlate> TYPE;
	public static final int MAX_MANA = TilePool.MAX_MANA / 2;

	private static final BlockPos[] LAPIS_BLOCKS = {
			new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0),
			new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)
	};

	private static final BlockPos[] LIVINGROCK_BLOCKS = {
			new BlockPos(0, 0, 0), new BlockPos(1, 0, 1),
			new BlockPos(1, 0, -1), new BlockPos(-1, 0, 1),
			new BlockPos(-1, 0, -1)
	};

	private static final String TAG_MANA = "mana";

	private int mana;

	public TileTerraPlate() {
		super(TYPE);
	}

	@Override
	public void tick() {
		if(world.isRemote)
			return;

		boolean removeMana = true;

		if(hasValidPlatform()) {
			List<ItemEntity> items = getItems();
			if(areItemsValid(items)) {
				removeMana = false;
				ISparkEntity spark = getAttachedSpark();
				if(spark != null) {
					SparkHelper.getSparksAround(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, spark.getNetwork())
							.filter(otherSpark -> spark != otherSpark && otherSpark.getAttachedTile() instanceof IManaPool)
							.forEach(os -> os.registerTransfer(spark));
				}
				if(mana > 0) {
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
					PacketHandler.sendToNearby(world, getPos(),
						new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.TERRA_PLATE, getPos().getX(), getPos().getY(), getPos().getZ()));
				}

				if(mana >= MAX_MANA) {
					ItemEntity item = items.get(0);
					for(ItemEntity otherItem : items)
						if(otherItem != item)
							otherItem.remove();
						else item.setItem(new ItemStack(ModItems.terrasteel));
					world.playSound(null, item.posX, item.posY, item.posZ, ModSounds.terrasteelCraft, SoundCategory.BLOCKS, 1, 1);
					mana = 0;
					world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
				}
			}
		}

		if(removeMana)
			recieveMana(-1000);
	}

	private List<ItemEntity> getItems() {
		return world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)));
	}

	private boolean areItemsValid(List<ItemEntity> items) {
		if(items.size() != 3)
			return false;

		ItemStack ingot = ItemStack.EMPTY;
		ItemStack pearl = ItemStack.EMPTY;
		ItemStack diamond = ItemStack.EMPTY;
		for(ItemEntity item : items) {
			ItemStack stack = item.getItem();
			if(stack.getCount() != 1)
				return false;

			if(stack.getItem() == ModItems.manaSteel)
				ingot = stack;
			else if(stack.getItem() == ModItems.manaPearl)
				pearl = stack;
			else if(stack.getItem() == ModItems.manaDiamond)
				diamond = stack;
			else return false;
		}

		return !ingot.isEmpty() && !pearl.isEmpty() && !diamond.isEmpty();
	}

	private boolean hasValidPlatform() {
		return checkAll(LAPIS_BLOCKS, Blocks.LAPIS_BLOCK) && checkAll(LIVINGROCK_BLOCKS, ModBlocks.livingrock);
	}

	private boolean checkAll(BlockPos[] relPositions, Block block) {
		for (BlockPos position : relPositions) {
			if(!checkPlatform(position.getX(), position.getZ(), block))
				return false;
		}

		return true;
	}

	private boolean checkPlatform(int xOff, int zOff, Block block) {
		return world.getBlockState(pos.add(xOff, -1, zOff)).getBlock() == block;
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		cmp.putInt(TAG_MANA, mana);
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		mana = cmp.getInt(TAG_MANA);
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.max(0, Math.min(MAX_MANA, this.mana + mana));
		world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return areItemsValid(getItems());
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public void attachSpark(ISparkEntity entity) {}

	@Override
	public ISparkEntity getAttachedSpark() {
		List<Entity> sparks = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up(), pos.up().add(1, 1, 1)), Predicates.instanceOf(ISparkEntity.class));
		if(sparks.size() == 1) {
			Entity e = sparks.get(0);
			return (ISparkEntity) e;
		}

		return null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		return !areItemsValid(getItems());
	}

	@Override
	public int getAvailableSpaceForMana() {
		return Math.max(0, MAX_MANA - getCurrentMana());
	}

}
