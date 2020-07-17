/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.*;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.common.block.mana.BlockSpreader;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileExposedSimpleInventory;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.entity.EntityManaBurst.PositionProperties;
import vazkii.botania.common.item.ItemLexicon;

import javax.annotation.Nullable;

import java.util.List;
import java.util.UUID;

public class TileSpreader extends TileExposedSimpleInventory implements IManaCollector, IWandBindable, IKeyLocked, IThrottledPacket, IManaSpreader, IDirectioned, ITickableTileEntity {
	private static final int TICKS_ALLOWED_WITHOUT_PINGBACK = 20;
	private static final double PINGBACK_EXPIRED_SEARCH_DISTANCE = 0.5;

	private static final String TAG_HAS_IDENTITY = "hasIdentity";
	private static final String TAG_UUID_MOST = "uuidMost";
	private static final String TAG_UUID_LEAST = "uuidLeast";
	private static final String TAG_MANA = "mana";
	private static final String TAG_REQUEST_UPDATE = "requestUpdate";
	private static final String TAG_ROTATION_X = "rotationX";
	private static final String TAG_ROTATION_Y = "rotationY";
	private static final String TAG_PADDING_COLOR = "paddingColor";
	private static final String TAG_CAN_SHOOT_BURST = "canShootBurst";
	private static final String TAG_PINGBACK_TICKS = "pingbackTicks";
	private static final String TAG_LAST_PINGBACK_X = "lastPingbackX";
	private static final String TAG_LAST_PINGBACK_Y = "lastPingbackY";
	private static final String TAG_LAST_PINGBACK_Z = "lastPingbackZ";

	private static final String TAG_FORCE_CLIENT_BINDING_X = "forceClientBindingX";
	private static final String TAG_FORCE_CLIENT_BINDING_Y = "forceClientBindingY";
	private static final String TAG_FORCE_CLIENT_BINDING_Z = "forceClientBindingZ";

	// Map Maker Tags

	private static final String TAG_INPUT_KEY = "inputKey";
	private static final String TAG_OUTPUT_KEY = "outputKey";

	private static final String TAG_MAPMAKER_OVERRIDE = "mapmakerOverrideEnabled";
	private static final String TAG_FORCED_COLOR = "mmForcedColor";
	private static final String TAG_FORCED_MANA_PAYLOAD = "mmForcedManaPayload";
	private static final String TAG_FORCED_TICKS_BEFORE_MANA_LOSS = "mmForcedTicksBeforeManaLoss";
	private static final String TAG_FORCED_MANA_LOSS_PER_TICK = "mmForcedManaLossPerTick";
	private static final String TAG_FORCED_GRAVITY = "mmForcedGravity";
	private static final String TAG_FORCED_VELOCITY_MULTIPLIER = "mmForcedVelocityMultiplier";

	private boolean mapmakerOverride = false;
	private int mmForcedColor = 0x20FF20;
	private int mmForcedManaPayload = 160;
	private int mmForcedTicksBeforeManaLoss = 60;
	private float mmForcedManaLossPerTick = 4F;
	private float mmForcedGravity = 0F;
	private float mmForcedVelocityMultiplier = 1F;

	private String inputKey = "";
	private final String outputKey = "";

	// End Map Maker Tags

	UUID identity;

	private int mana;
	public float rotationX, rotationY;

	@Nullable
	public DyeColor paddingColor = null;

	private boolean requestsClientUpdate = false;
	private boolean hasReceivedInitialPacket = false;

	private IManaReceiver receiver = null;
	private IManaReceiver receiverLastTick = null;

	private boolean redstoneLastTick = true;
	public boolean canShootBurst = true;
	public int lastBurstDeathTick = -1;
	public int burstParticleTick = 0;

	public int pingbackTicks = 0;
	public double lastPingbackX = 0;
	public double lastPingbackY = -1;
	public double lastPingbackZ = 0;

	private List<PositionProperties> lastTentativeBurst;
	private boolean invalidTentativeBurst = false;

	public TileSpreader() {
		super(ModTiles.SPREADER);
	}

	@Override
	public boolean isFull() {
		return mana >= getMaxMana();
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.min(this.mana + mana, getMaxMana());
		this.markDirty();
	}

	@Override
	public void remove() {
		super.remove();
		ManaNetworkEvent.removeCollector(this);
	}

	@Override
	public void onChunkUnloaded() {
		super.onChunkUnloaded();
		ManaNetworkEvent.removeCollector(this);
	}

	@Override
	public void tick() {
		boolean inNetwork = ManaNetworkHandler.instance.isCollectorIn(this);
		boolean wasInNetwork = inNetwork;
		if (!inNetwork && !isRemoved()) {
			ManaNetworkEvent.addCollector(this);
		}

		boolean redstone = false;

		for (Direction dir : Direction.values()) {
			TileEntity tileAt = world.getTileEntity(pos.offset(dir));
			if (world.isBlockLoaded(pos.offset(dir)) && tileAt instanceof IManaPool) {
				IManaPool pool = (IManaPool) tileAt;
				if (wasInNetwork && (pool != receiver || getVariant() == BlockSpreader.Variant.REDSTONE)) {
					if (pool instanceof IKeyLocked && !((IKeyLocked) pool).getOutputKey().equals(getInputKey())) {
						continue;
					}

					int manaInPool = pool.getCurrentMana();
					if (manaInPool > 0 && !isFull()) {
						int manaMissing = getMaxMana() - mana;
						int manaToRemove = Math.min(manaInPool, manaMissing);
						pool.receiveMana(-manaToRemove);
						receiveMana(manaToRemove);
					}
				}
			}

			int redstoneSide = world.getRedstonePower(pos.offset(dir), dir);
			if (redstoneSide > 0) {
				redstone = true;
			}
		}

		if (needsNewBurstSimulation()) {
			checkForReceiver();
		}

		if (!canShootBurst) {
			if (pingbackTicks <= 0) {
				double x = lastPingbackX;
				double y = lastPingbackY;
				double z = lastPingbackZ;
				AxisAlignedBB aabb = new AxisAlignedBB(x, y, z, x, y, z).grow(PINGBACK_EXPIRED_SEARCH_DISTANCE, PINGBACK_EXPIRED_SEARCH_DISTANCE, PINGBACK_EXPIRED_SEARCH_DISTANCE);
				@SuppressWarnings("unchecked")
				List<IManaBurst> bursts = (List<IManaBurst>) (List<?>) world.getEntitiesWithinAABB(ThrowableEntity.class, aabb, Predicates.instanceOf(IManaBurst.class));
				IManaBurst found = null;
				UUID identity = getIdentifier();
				for (IManaBurst burst : bursts) {
					if (burst != null && identity.equals(burst.getShooterUUID())) {
						found = burst;
						break;
					}
				}

				if (found != null) {
					found.ping();
				} else {
					setCanShoot(true);
				}
			} else {
				pingbackTicks--;
			}
		}

		boolean shouldShoot = !redstone;

		boolean isredstone = getVariant() == BlockSpreader.Variant.REDSTONE;
		if (isredstone) {
			shouldShoot = redstone && !redstoneLastTick;
		}

		if (shouldShoot && receiver != null && receiver instanceof IKeyLocked) {
			shouldShoot = ((IKeyLocked) receiver).getInputKey().equals(getOutputKey());
		}

		ItemStack lens = getItemHandler().getStackInSlot(0);
		ILensControl control = getLensController(lens);
		if (control != null) {
			if (isredstone) {
				if (shouldShoot) {
					control.onControlledSpreaderPulse(lens, this, redstone);
				}
			} else {
				control.onControlledSpreaderTick(lens, this, redstone);
			}

			shouldShoot &= control.allowBurstShooting(lens, this, redstone);
		}

		if (shouldShoot) {
			tryShootBurst();
		}

		if (receiverLastTick != receiver && !world.isRemote) {
			requestsClientUpdate = true;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}

		redstoneLastTick = redstone;
		receiverLastTick = receiver;
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		super.writePacketNBT(cmp);

		UUID identity = getIdentifier();
		cmp.putBoolean(TAG_HAS_IDENTITY, true);
		cmp.putLong(TAG_UUID_MOST, identity.getMostSignificantBits());
		cmp.putLong(TAG_UUID_LEAST, identity.getLeastSignificantBits());

		cmp.putInt(TAG_MANA, mana);
		cmp.putFloat(TAG_ROTATION_X, rotationX);
		cmp.putFloat(TAG_ROTATION_Y, rotationY);
		cmp.putBoolean(TAG_REQUEST_UPDATE, requestsClientUpdate);
		cmp.putInt(TAG_PADDING_COLOR, paddingColor == null ? -1 : paddingColor.getId());
		cmp.putBoolean(TAG_CAN_SHOOT_BURST, canShootBurst);

		cmp.putInt(TAG_PINGBACK_TICKS, pingbackTicks);
		cmp.putDouble(TAG_LAST_PINGBACK_X, lastPingbackX);
		cmp.putDouble(TAG_LAST_PINGBACK_Y, lastPingbackY);
		cmp.putDouble(TAG_LAST_PINGBACK_Z, lastPingbackZ);

		cmp.putString(TAG_INPUT_KEY, inputKey);
		cmp.putString(TAG_OUTPUT_KEY, outputKey);

		cmp.putInt(TAG_FORCE_CLIENT_BINDING_X, receiver == null ? 0 : ((TileEntity) receiver).getPos().getX());
		cmp.putInt(TAG_FORCE_CLIENT_BINDING_Y, receiver == null ? -1 : ((TileEntity) receiver).getPos().getY());
		cmp.putInt(TAG_FORCE_CLIENT_BINDING_Z, receiver == null ? 0 : ((TileEntity) receiver).getPos().getZ());

		cmp.putBoolean(TAG_MAPMAKER_OVERRIDE, mapmakerOverride);
		cmp.putInt(TAG_FORCED_COLOR, mmForcedColor);
		cmp.putInt(TAG_FORCED_MANA_PAYLOAD, mmForcedManaPayload);
		cmp.putInt(TAG_FORCED_TICKS_BEFORE_MANA_LOSS, mmForcedTicksBeforeManaLoss);
		cmp.putFloat(TAG_FORCED_MANA_LOSS_PER_TICK, mmForcedManaLossPerTick);
		cmp.putFloat(TAG_FORCED_GRAVITY, mmForcedGravity);
		cmp.putFloat(TAG_FORCED_VELOCITY_MULTIPLIER, mmForcedVelocityMultiplier);

		requestsClientUpdate = false;
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		super.readPacketNBT(cmp);

		if (cmp.getBoolean(TAG_HAS_IDENTITY)) {
			long most = cmp.getLong(TAG_UUID_MOST);
			long least = cmp.getLong(TAG_UUID_LEAST);
			UUID identity = getIdentifierUnsafe();
			if (identity == null || most != identity.getMostSignificantBits() || least != identity.getLeastSignificantBits()) {
				this.identity = new UUID(most, least);
			}
		} else {
			getIdentifier();
		}

		mana = cmp.getInt(TAG_MANA);
		rotationX = cmp.getFloat(TAG_ROTATION_X);
		rotationY = cmp.getFloat(TAG_ROTATION_Y);
		requestsClientUpdate = cmp.getBoolean(TAG_REQUEST_UPDATE);

		if (cmp.contains(TAG_INPUT_KEY)) {
			inputKey = cmp.getString(TAG_INPUT_KEY);
		}
		if (cmp.contains(TAG_OUTPUT_KEY)) {
			inputKey = cmp.getString(TAG_OUTPUT_KEY);
		}

		mapmakerOverride = cmp.getBoolean(TAG_MAPMAKER_OVERRIDE);
		mmForcedColor = cmp.getInt(TAG_FORCED_COLOR);
		mmForcedManaPayload = cmp.getInt(TAG_FORCED_MANA_PAYLOAD);
		mmForcedTicksBeforeManaLoss = cmp.getInt(TAG_FORCED_TICKS_BEFORE_MANA_LOSS);
		mmForcedManaLossPerTick = cmp.getFloat(TAG_FORCED_MANA_LOSS_PER_TICK);
		mmForcedGravity = cmp.getFloat(TAG_FORCED_GRAVITY);
		mmForcedVelocityMultiplier = cmp.getFloat(TAG_FORCED_VELOCITY_MULTIPLIER);

		if (cmp.contains(TAG_PADDING_COLOR)) {
			paddingColor = cmp.getInt(TAG_PADDING_COLOR) == -1 ? null : DyeColor.byId(cmp.getInt(TAG_PADDING_COLOR));
		}
		if (cmp.contains(TAG_CAN_SHOOT_BURST)) {
			canShootBurst = cmp.getBoolean(TAG_CAN_SHOOT_BURST);
		}

		pingbackTicks = cmp.getInt(TAG_PINGBACK_TICKS);
		lastPingbackX = cmp.getDouble(TAG_LAST_PINGBACK_X);
		lastPingbackY = cmp.getDouble(TAG_LAST_PINGBACK_Y);
		lastPingbackZ = cmp.getDouble(TAG_LAST_PINGBACK_Z);

		if (requestsClientUpdate && world != null) {
			int x = cmp.getInt(TAG_FORCE_CLIENT_BINDING_X);
			int y = cmp.getInt(TAG_FORCE_CLIENT_BINDING_Y);
			int z = cmp.getInt(TAG_FORCE_CLIENT_BINDING_Z);
			if (y != -1) {
				TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
				if (tile instanceof IManaReceiver) {
					receiver = (IManaReceiver) tile;
				} else {
					receiver = null;
				}
			} else {
				receiver = null;
			}
		}

		if (world != null && world.isRemote) {
			hasReceivedInitialPacket = true;
		}
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return true;
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	public void onWanded(PlayerEntity player, ItemStack wand) {
		if (player == null) {
			return;
		}

		if (!player.isSneaking()) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		} else {
			BlockRayTraceResult bpos = ItemLexicon.doRayTrace(world, player, RayTraceContext.FluidMode.NONE);
			if (!world.isRemote) {
				double x = bpos.getHitVec().x - getPos().getX() - 0.5;
				double y = bpos.getHitVec().y - getPos().getY() - 0.5;
				double z = bpos.getHitVec().z - getPos().getZ() - 0.5;

				if (bpos.getFace() != Direction.DOWN && bpos.getFace() != Direction.UP) {
					Vector3 clickVector = new Vector3(x, 0, z);
					Vector3 relative = new Vector3(-0.5, 0, 0);
					double angle = Math.acos(clickVector.dotProduct(relative) / (relative.mag() * clickVector.mag())) * 180D / Math.PI;

					rotationX = (float) angle + 180F;
					if (clickVector.z < 0) {
						rotationX = 360 - rotationX;
					}
				}

				double angle = y * 180;
				rotationY = -(float) angle;

				checkForReceiver();
				requestsClientUpdate = true;
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		}
	}

	private boolean needsNewBurstSimulation() {
		if (world.isRemote && !hasReceivedInitialPacket) {
			return false;
		}

		if (lastTentativeBurst == null) {
			return true;
		}

		for (PositionProperties props : lastTentativeBurst) {
			if (!props.contentsEqual(world)) {
				invalidTentativeBurst = props.invalid;
				return !invalidTentativeBurst;
			}
		}

		return false;
	}

	private void tryShootBurst() {
		boolean redstone = getVariant() == BlockSpreader.Variant.REDSTONE;
		if ((receiver != null || redstone) && !invalidTentativeBurst) {
			if (canShootBurst && (redstone || receiver.canReceiveManaFromBursts() && !receiver.isFull())) {
				EntityManaBurst burst = getBurst(false);
				if (burst != null) {
					if (!world.isRemote) {
						mana -= burst.getStartingMana();
						burst.setShooterUUID(getIdentifier());
						world.addEntity(burst);
						burst.ping();
						if (!ConfigHandler.COMMON.silentSpreaders.get()) {
							world.playSound(null, pos, ModSounds.spreaderFire, SoundCategory.BLOCKS, 0.05F * (paddingColor != null ? 0.2F : 1F), 0.7F + 0.3F * (float) Math.random());
						}
					}
				}
			}
		}
	}

	public BlockSpreader.Variant getVariant() {
		Block b = getBlockState().getBlock();
		if (b instanceof BlockSpreader) {
			return ((BlockSpreader) b).variant;
		} else {
			return BlockSpreader.Variant.MANA;
		}
	}

	public void checkForReceiver() {
		ItemStack stack = getItemHandler().getStackInSlot(0);
		ILensControl control = getLensController(stack);
		if (control != null && !control.allowBurstShooting(stack, this, false)) {
			return;
		}

		EntityManaBurst fakeBurst = getBurst(true);
		fakeBurst.setScanBeam();
		TileEntity receiver = fakeBurst.getCollidedTile(true);

		if (receiver instanceof IManaReceiver
				&& receiver.hasWorld()
				&& receiver.getWorld().isBlockLoaded(receiver.getPos())) {
			this.receiver = (IManaReceiver) receiver;
		} else {
			this.receiver = null;
		}
		lastTentativeBurst = fakeBurst.propsList;
	}

	@Override
	public IManaBurst runBurstSimulation() {
		EntityManaBurst fakeBurst = getBurst(true);
		fakeBurst.setScanBeam();
		fakeBurst.getCollidedTile(true);
		return fakeBurst;
	}

	private EntityManaBurst getBurst(boolean fake) {
		BlockSpreader.Variant variant = getVariant();
		float gravity = 0F;
		BurstProperties props = new BurstProperties(variant.burstMana, variant.preLossTicks, variant.lossPerTick, gravity, variant.motionModifier, variant.color);

		ItemStack lens = getItemHandler().getStackInSlot(0);
		if (!lens.isEmpty() && lens.getItem() instanceof ILensEffect) {
			((ILensEffect) lens.getItem()).apply(lens, props);
		}

		if (getCurrentMana() >= props.maxMana || fake) {
			EntityManaBurst burst = new EntityManaBurst(this, fake);
			burst.setSourceLens(lens);

			if (mapmakerOverride) {
				burst.setColor(mmForcedColor);
				burst.setMana(mmForcedManaPayload);
				burst.setStartingMana(mmForcedManaPayload);
				burst.setMinManaLoss(mmForcedTicksBeforeManaLoss);
				burst.setManaLossPerTick(mmForcedManaLossPerTick);
				burst.setGravity(mmForcedGravity);
				burst.setMotion(burst.getMotion().scale(mmForcedVelocityMultiplier));
			} else {
				burst.setColor(props.color);
				burst.setMana(props.maxMana);
				burst.setStartingMana(props.maxMana);
				burst.setMinManaLoss(props.ticksBeforeManaLoss);
				burst.setManaLossPerTick(props.manaLossPerTick);
				burst.setGravity(props.gravity);
				burst.setMotion(burst.getMotion().scale(props.motionModifier));
			}

			return burst;
		}
		return null;
	}

	public ILensControl getLensController(ItemStack stack) {
		if (!stack.isEmpty() && stack.getItem() instanceof ILensControl) {
			ILensControl control = (ILensControl) stack.getItem();
			if (control.isControlLens(stack)) {
				return control;
			}
		}

		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public void renderHUD(MatrixStack ms, Minecraft mc) {
		String name = new ItemStack(getBlockState().getBlock()).getDisplayName().getString();
		int color = getVariant().hudColor;
		BotaniaAPIClient.instance().drawSimpleManaHUD(ms, color, getCurrentMana(), getMaxMana(), name);

		ItemStack lens = getItemHandler().getStackInSlot(0);
		if (!lens.isEmpty()) {
			String lensName = lens.getDisplayName().getString();
			int width = 16 + mc.fontRenderer.getStringWidth(lensName) / 2;
			int x = mc.getMainWindow().getScaledWidth() / 2 - width;
			int y = mc.getMainWindow().getScaledHeight() / 2 + 50;

			mc.fontRenderer.drawStringWithShadow(ms, lensName, x + 20, y + 5, color);
			mc.getItemRenderer().renderItemAndEffectIntoGUI(lens, x, y);
			RenderSystem.disableLighting();
		}

		if (receiver != null) {
			TileEntity receiverTile = (TileEntity) receiver;
			ItemStack recieverStack = new ItemStack(world.getBlockState(receiverTile.getPos()).getBlock());
			if (!recieverStack.isEmpty()) {
				String stackName = recieverStack.getDisplayName().getString();
				int width = 16 + mc.fontRenderer.getStringWidth(stackName) / 2;
				int x = mc.getMainWindow().getScaledWidth() / 2 - width;
				int y = mc.getMainWindow().getScaledHeight() / 2 + 30;

				mc.fontRenderer.drawStringWithShadow(ms, stackName, x + 20, y + 5, color);
				mc.getItemRenderer().renderItemAndEffectIntoGUI(recieverStack, x, y);
			}

			RenderSystem.disableLighting();
		}

		RenderSystem.color4f(1F, 1F, 1F, 1F);
	}

	@Override
	public void onClientDisplayTick() {
		if (world != null) {
			EntityManaBurst burst = getBurst(true);
			burst.getCollidedTile(false);
		}
	}

	@Override
	public float getManaYieldMultiplier(IManaBurst burst) {
		return 1F;
	}

	@Override
	protected Inventory createItemHandler() {
		return new Inventory(1) {
			@Override
			public int getInventoryStackLimit() {
				return 1;
			}

			@Override
			public boolean isItemValidForSlot(int index, ItemStack stack) {
				return !stack.isEmpty() && stack.getItem() instanceof ILens;
			}
		};
	}

	@Override
	public void markDirty() {
		if (world != null) {
			checkForReceiver();
			if (!world.isRemote) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		}
	}

	@Override
	public BlockPos getBinding() {
		if (receiver == null) {
			return null;
		}

		TileEntity tile = (TileEntity) receiver;
		return tile.getPos();
	}

	@Override
	public int getMaxMana() {
		return getVariant().manaCapacity;
	}

	@Override
	public String getInputKey() {
		return inputKey;
	}

	@Override
	public String getOutputKey() {
		return outputKey;
	}

	@Override
	public boolean canSelect(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean bindTo(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		Vector3d thisVec = Vector3d.func_237489_a_(getPos());
		Vector3d blockVec = Vector3d.func_237489_a_(pos);

		VoxelShape shape = player.world.getBlockState(pos).getShape(player.world, pos);
		AxisAlignedBB axis = shape.isEmpty() ? new AxisAlignedBB(pos) : shape.getBoundingBox().offset(pos);

		if (!axis.contains(blockVec)) {
			blockVec = new Vector3d(axis.minX + (axis.maxX - axis.minX) / 2, axis.minY + (axis.maxY - axis.minY) / 2, axis.minZ + (axis.maxZ - axis.minZ) / 2);
		}

		Vector3d diffVec = blockVec.subtract(thisVec);
		Vector3d diffVec2D = new Vector3d(diffVec.x, diffVec.z, 0);
		Vector3d rotVec = new Vector3d(0, 1, 0);
		double angle = MathHelper.angleBetween(rotVec, diffVec2D) / Math.PI * 180.0;

		if (blockVec.x < thisVec.x) {
			angle = -angle;
		}

		rotationX = (float) angle + 90;

		rotVec = new Vector3d(diffVec.x, 0, diffVec.z);
		angle = MathHelper.angleBetween(diffVec, rotVec) * 180F / Math.PI;
		if (blockVec.y < thisVec.y) {
			angle = -angle;
		}
		rotationY = (float) angle;

		checkForReceiver();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		return true;
	}

	@Override
	public void markDispatchable() {}

	@Override
	public float getRotationX() {
		return rotationX;
	}

	@Override
	public float getRotationY() {
		return rotationY;
	}

	@Override
	public void setRotationX(float rot) {
		rotationX = rot;
	}

	@Override
	public void setRotationY(float rot) {
		rotationY = rot;
	}

	@Override
	public void rotate(Rotation rotationIn) {
		switch (rotationIn) {
		case CLOCKWISE_90:
			rotationX += 270F;
			break;
		case CLOCKWISE_180:
			rotationX += 180F;
			break;
		case COUNTERCLOCKWISE_90:
			rotationX += 90F;
			break;
		default:
			break;
		}

		if (rotationX >= 360F) {
			rotationX -= 360F;
		}
	}

	@Override
	public void mirror(Mirror mirrorIn) {
		switch (mirrorIn) {
		case LEFT_RIGHT:
			rotationX = 360F - rotationX;
			break;
		case FRONT_BACK:
			rotationX = 180F - rotationX;
			break;
		default:
			break;
		}

		if (rotationX < 0F) {
			rotationX += 360F;
		}
	}

	@Override
	public void commitRedirection() {
		checkForReceiver();
	}

	@Override
	public void setCanShoot(boolean canShoot) {
		canShootBurst = canShoot;
	}

	@Override
	public int getBurstParticleTick() {
		return burstParticleTick;
	}

	@Override
	public void setBurstParticleTick(int i) {
		burstParticleTick = i;
	}

	@Override
	public int getLastBurstDeathTick() {
		return lastBurstDeathTick;
	}

	@Override
	public void setLastBurstDeathTick(int i) {
		lastBurstDeathTick = i;
	}

	@Override
	public void pingback(IManaBurst burst, UUID expectedIdentity) {
		if (getIdentifier().equals(expectedIdentity)) {
			pingbackTicks = TICKS_ALLOWED_WITHOUT_PINGBACK;
			Entity e = (Entity) burst;
			lastPingbackX = e.getPosX();
			lastPingbackY = e.getPosY();
			lastPingbackZ = e.getPosZ();
			setCanShoot(false);
		}
	}

	@Override
	public UUID getIdentifier() {
		if (identity == null) {
			identity = UUID.randomUUID();
		}
		return identity;
	}

	private UUID getIdentifierUnsafe() {
		return identity;
	}

}
