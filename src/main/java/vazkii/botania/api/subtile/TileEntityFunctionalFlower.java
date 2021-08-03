/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.subtile;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaPool;

/**
 * The basic class for a Functional Flower.
 */
public class TileEntityFunctionalFlower extends TileEntitySpecialFlower {
	private static final ResourceLocation POOL_ID = new ResourceLocation(BotaniaAPI.MODID, "mana_pool");
	public static final int LINK_RANGE = 10;

	private static final String TAG_MANA = "mana";

	private static final String TAG_POOL_X = "poolX";
	private static final String TAG_POOL_Y = "poolY";
	private static final String TAG_POOL_Z = "poolZ";

	private int mana;

	public int redstoneSignal = 0;

	int sizeLastCheck = -1;
	BlockEntity linkedPool = null;

	BlockPos cachedPoolCoordinates = null;

	public TileEntityFunctionalFlower(BlockEntityType<?> type) {
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

		redstoneSignal = 0;
		if (acceptsRedstone()) {
			for (Direction dir : Direction.values()) {
				int redstoneSide = getLevel().getSignal(getBlockPos().relative(dir), dir);
				redstoneSignal = Math.max(redstoneSignal, redstoneSide);
			}
		}

		if (getLevel().isClientSide) {
			double particleChance = 1F - (double) mana / (double) getMaxMana() / 3.5F;
			int color = getColor();
			float red = (color >> 16 & 0xFF) / 255F;
			float green = (color >> 8 & 0xFF) / 255F;
			float blue = (color & 0xFF) / 255F;
			if (Math.random() > particleChance) {
				BotaniaAPI.instance().sparkleFX(getLevel(), getBlockPos().getX() + 0.3 + Math.random() * 0.5, getBlockPos().getY() + 0.5 + Math.random() * 0.5, getBlockPos().getZ() + 0.3 + Math.random() * 0.5, red, green, blue, (float) Math.random(), 5);
			}
		}
	}

	public void linkPool() {
		boolean needsNew = false;
		if (linkedPool == null) {
			needsNew = true;

			if (cachedPoolCoordinates != null) {
				needsNew = false;
				if (getLevel().hasChunkAt(cachedPoolCoordinates)) {
					needsNew = true;
					BlockEntity tileAt = getLevel().getBlockEntity(cachedPoolCoordinates);
					if (tileAt instanceof IManaPool && !tileAt.isRemoved()) {
						linkedPool = tileAt;
						needsNew = false;
					}
					cachedPoolCoordinates = null;
				}
			}
		} else {
			BlockEntity tileAt = getLevel().getBlockEntity(linkedPool.getBlockPos());
			if (tileAt instanceof IManaPool) {
				linkedPool = tileAt;
			}
		}

		if (needsNew && ticksExisted == 1) { // Only for new flowers
			IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
			int size = network.getAllPoolsInWorld(getLevel()).size();
			if (BotaniaAPI.instance().shouldForceCheck() || size != sizeLastCheck) {
				linkedPool = network.getClosestPool(getBlockPos(), getLevel(), LINK_RANGE);
				sizeLastCheck = size;
			}
		}

		setChanged();
	}

	public void linkToForcefully(BlockEntity pool) {
		linkedPool = pool;
		setChanged();
	}

	public int getMana() {
		return mana;
	}

	public void addMana(int mana) {
		this.mana = Mth.clamp(this.mana + mana, 0, getMaxMana());
		setChanged();
	}

	@Override
	public boolean onWanded(Player player, ItemStack wand) {
		if (player == null) {
			return false;
		}

		Registry.SOUND_EVENT.getOptional(DING_SOUND_EVENT).ifPresent(evt -> player.playSound(evt, 0.1F, 1F));

		return super.onWanded(player, wand);
	}

	public int getMaxMana() {
		return 20;
	}

	public int getColor() {
		return 0xFFFFFF;
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);
		mana = cmp.getInt(TAG_MANA);

		int x = cmp.getInt(TAG_POOL_X);
		int y = cmp.getInt(TAG_POOL_Y);
		int z = cmp.getInt(TAG_POOL_Z);

		cachedPoolCoordinates = y < 0 ? null : new BlockPos(x, y, z);
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_MANA, mana);

		if (cachedPoolCoordinates != null) {
			cmp.putInt(TAG_POOL_X, cachedPoolCoordinates.getX());
			cmp.putInt(TAG_POOL_Y, cachedPoolCoordinates.getY());
			cmp.putInt(TAG_POOL_Z, cachedPoolCoordinates.getZ());
		} else {
			int x = linkedPool == null ? 0 : linkedPool.getBlockPos().getX();
			int y = linkedPool == null ? -1 : linkedPool.getBlockPos().getY();
			int z = linkedPool == null ? 0 : linkedPool.getBlockPos().getZ();

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
		return linkedPool.getBlockPos();
	}

	@Override
	public boolean canSelect(Player player, ItemStack wand, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
		int range = 10;
		range *= range;

		double dist = pos.distSqr(getBlockPos());
		if (range >= dist) {
			BlockEntity tile = player.level.getBlockEntity(pos);
			if (tile instanceof IManaPool) {
				linkedPool = tile;
				return true;
			}
		}

		return false;
	}

	public boolean isValidBinding() {
		return linkedPool != null
				&& linkedPool.hasLevel()
				&& !linkedPool.isRemoved()
				&& getLevel().hasChunkAt(linkedPool.getBlockPos())
				&& getLevel().getBlockEntity(linkedPool.getBlockPos()) == linkedPool;
	}

	public ItemStack getHudIcon() {
		return Registry.ITEM.getOptional(POOL_ID).map(ItemStack::new).orElse(ItemStack.EMPTY);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(PoseStack ms, Minecraft mc) {
		String name = I18n.get(getBlockState().getBlock().getDescriptionId());
		int color = getColor();
		BotaniaAPIClient.instance().drawComplexManaHUD(ms, color, getMana(), getMaxMana(), name, getHudIcon(), isValidBinding());
	}

}
