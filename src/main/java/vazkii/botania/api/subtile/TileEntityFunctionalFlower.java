/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.subtile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaPool;

/**
 * The basic class for a Functional Flower.
 */
public class TileEntityFunctionalFlower extends TileEntitySpecialFlower {

	public static final int LINK_RANGE = 10;

	private static final String TAG_MANA = "mana";

	private static final String TAG_POOL_X = "poolX";
	private static final String TAG_POOL_Y = "poolY";
	private static final String TAG_POOL_Z = "poolZ";

	private int mana;

	public int redstoneSignal = 0;

	int sizeLastCheck = -1;
	TileEntity linkedPool = null;

	BlockPos cachedPoolCoordinates = null;

	public TileEntityFunctionalFlower(TileEntityType<?> type) {
		super(type);
	}

	/**
	 * If set to true, redstoneSignal will be updated every tick.
	 */
	public boolean acceptsRedstone() {
		return false;
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		linkPool();

		if (linkedPool != null && isValidBinding()) {
			IManaPool pool = (IManaPool) linkedPool;
			int manaInPool = pool.getCurrentMana();
			int manaMissing = getMaxMana() - mana;
			int manaToRemove = Math.min(manaMissing, manaInPool);
			pool.receiveMana(-manaToRemove);
			addMana(manaToRemove);
		}

		if (acceptsRedstone()) {
			redstoneSignal = 0;
			for (Direction dir : Direction.values()) {
				int redstoneSide = getWorld().getRedstonePower(getPos().offset(dir), dir);
				redstoneSignal = Math.max(redstoneSignal, redstoneSide);
			}
		}

		if (getWorld().isRemote) {
			double particleChance = 1F - (double) mana / (double) getMaxMana() / 3.5F;
			int color = getColor();
			float red = (color >> 16 & 0xFF) / 255F;
			float green = (color >> 8 & 0xFF) / 255F;
			float blue = (color & 0xFF) / 255F;
			if (Math.random() > particleChance) {
				BotaniaAPI.instance().internalHandler().sparkleFX(getWorld(), getPos().getX() + 0.3 + Math.random() * 0.5, getPos().getY() + 0.5 + Math.random() * 0.5, getPos().getZ() + 0.3 + Math.random() * 0.5, red, green, blue, (float) Math.random(), 5);
			}
		}
	}

	public void linkPool() {
		boolean needsNew = false;
		if (linkedPool == null) {
			needsNew = true;

			if (cachedPoolCoordinates != null) {
				needsNew = false;
				if (getWorld().isBlockLoaded(cachedPoolCoordinates)) {
					needsNew = true;
					TileEntity tileAt = getWorld().getTileEntity(cachedPoolCoordinates);
					if (tileAt instanceof IManaPool && !tileAt.isRemoved()) {
						linkedPool = tileAt;
						needsNew = false;
					}
					cachedPoolCoordinates = null;
				}
			}
		} else {
			TileEntity tileAt = getWorld().getTileEntity(linkedPool.getPos());
			if (tileAt instanceof IManaPool) {
				linkedPool = tileAt;
			}
		}

		if (needsNew && ticksExisted == 1) { // Only for new flowers
			IManaNetwork network = BotaniaAPI.instance().internalHandler().getManaNetworkInstance();
			int size = network.getAllPoolsInWorld(getWorld()).size();
			if (BotaniaAPI.instance().internalHandler().shouldForceCheck() || size != sizeLastCheck) {
				linkedPool = network.getClosestPool(getPos(), getWorld(), LINK_RANGE);
				sizeLastCheck = size;
			}
		}

		markDirty();
	}

	public void linkToForcefully(TileEntity pool) {
		linkedPool = pool;
		markDirty();
	}

	public int getMana() {
		return mana;
	}

	public void addMana(int mana) {
		this.mana = MathHelper.clamp(this.mana + mana, 0, getMaxMana());
		markDirty();
	}

	@Override
	public boolean onWanded(PlayerEntity player, ItemStack wand) {
		if (player == null) {
			return false;
		}

		SoundEvent evt = ForgeRegistries.SOUND_EVENTS.getValue(DING_SOUND_EVENT);
		if (evt != null) {
			player.playSound(evt, 0.1F, 1F);
		}

		return super.onWanded(player, wand);
	}

	public int getMaxMana() {
		return 20;
	}

	public int getColor() {
		return 0xFFFFFF;
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);
		mana = cmp.getInt(TAG_MANA);

		int x = cmp.getInt(TAG_POOL_X);
		int y = cmp.getInt(TAG_POOL_Y);
		int z = cmp.getInt(TAG_POOL_Z);

		cachedPoolCoordinates = y < 0 ? null : new BlockPos(x, y, z);
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_MANA, mana);

		if (cachedPoolCoordinates != null) {
			cmp.putInt(TAG_POOL_X, cachedPoolCoordinates.getX());
			cmp.putInt(TAG_POOL_Y, cachedPoolCoordinates.getY());
			cmp.putInt(TAG_POOL_Z, cachedPoolCoordinates.getZ());
		} else {
			int x = linkedPool == null ? 0 : linkedPool.getPos().getX();
			int y = linkedPool == null ? -1 : linkedPool.getPos().getY();
			int z = linkedPool == null ? 0 : linkedPool.getPos().getZ();

			cmp.putInt(TAG_POOL_X, x);
			cmp.putInt(TAG_POOL_Y, y);
			cmp.putInt(TAG_POOL_Z, z);
		}
	}

	@Override
	public BlockPos getBinding() {
		if (linkedPool == null) {
			return null;
		}
		return linkedPool.getPos();
	}

	@Override
	public boolean canSelect(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean bindTo(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		int range = 10;
		range *= range;

		double dist = pos.distanceSq(getPos());
		if (range >= dist) {
			TileEntity tile = player.world.getTileEntity(pos);
			if (tile instanceof IManaPool) {
				linkedPool = tile;
				return true;
			}
		}

		return false;
	}

	public boolean isValidBinding() {
		return linkedPool != null
				&& linkedPool.hasWorld()
				&& !linkedPool.isRemoved()
				&& getWorld().isBlockLoaded(linkedPool.getPos())
				&& getWorld().getTileEntity(linkedPool.getPos()) == linkedPool;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc) {
		String name = I18n.format(getBlockState().getBlock().getTranslationKey());
		int color = getColor();
		BotaniaAPI.instance().internalHandler().drawComplexManaHUD(color, getMana(), getMaxMana(), name, BotaniaAPI.instance().internalHandler().getBindDisplayForFlowerType(this), isValidBinding());
	}

}
