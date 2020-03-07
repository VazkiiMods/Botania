/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2014, 9:40:57 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.IDirectioned;
import vazkii.botania.api.mana.IKeyLocked;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.ILensControl;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.api.mana.IThrottledPacket;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.mana.BlockSpreader;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.entity.EntityManaBurst.PositionProperties;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TileSpreader extends TileSimpleInventory implements IManaCollector, IWandBindable, IKeyLocked, IThrottledPacket, IManaSpreader, IDirectioned, ITickableTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.SPREADER)
	public static TileEntityType<TileSpreader> TYPE;
	private static final int TICKS_ALLOWED_WITHOUT_PINGBACK = 20;
	private static final double PINGBACK_EXPIRED_SEARCH_DISTANCE = 0.5;

	private static final String TAG_HAS_IDENTITY = "hasIdentity";
	private static final String TAG_UUID_MOST = "uuidMost";
	private static final String TAG_UUID_LEAST = "uuidLeast";
	private static final String TAG_MANA = "mana";
	private static final String TAG_KNOWN_MANA = "knownMana";
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
	private int knownMana = -1;
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
		super(TYPE);
	}

	@Override
	public boolean isFull() {
		return mana >= getMaxMana();
	}

	@Override
	public void recieveMana(int mana) {
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
		if(!inNetwork && !isRemoved()) {
			ManaNetworkEvent.addCollector(this);
		}

		boolean redstone = false;

		for(Direction dir : Direction.values()) {
			TileEntity tileAt = world.getTileEntity(pos.offset(dir));
			if(world.isBlockLoaded(pos.offset(dir)) && tileAt instanceof IManaPool) {
				IManaPool pool = (IManaPool) tileAt;
				if(wasInNetwork && (pool != receiver || getVariant() == BlockSpreader.Variant.REDSTONE)) {
					if(pool instanceof IKeyLocked && !((IKeyLocked) pool).getOutputKey().equals(getInputKey()))
						continue;

					int manaInPool = pool.getCurrentMana();
					if(manaInPool > 0 && !isFull()) {
						int manaMissing = getMaxMana() - mana;
						int manaToRemove = Math.min(manaInPool, manaMissing);
						pool.recieveMana(-manaToRemove);
						recieveMana(manaToRemove);
					}
				}
			}

			int redstoneSide = world.getRedstonePower(pos.offset(dir), dir);
			if(redstoneSide > 0)
				redstone = true;
		}

		if(needsNewBurstSimulation())
			checkForReceiver();

		if(!canShootBurst)
			if(pingbackTicks <= 0) {
				double x = lastPingbackX;
				double y = lastPingbackY;
				double z = lastPingbackZ;
				AxisAlignedBB aabb = new AxisAlignedBB(x, y, z, x, y, z).grow(PINGBACK_EXPIRED_SEARCH_DISTANCE, PINGBACK_EXPIRED_SEARCH_DISTANCE, PINGBACK_EXPIRED_SEARCH_DISTANCE);
				List bursts = world.getEntitiesWithinAABB(ThrowableEntity.class, aabb, Predicates.instanceOf(IManaBurst.class));
				IManaBurst found = null;
				UUID identity = getIdentifier();
				for(IManaBurst burst : (List<IManaBurst>) bursts)
					if(burst != null && identity.equals(burst.getShooterUUID())) {
						found = burst;
						break;
					}

				if(found != null)
					found.ping();
				else setCanShoot(true);
			} else pingbackTicks--;

		boolean shouldShoot = !redstone;

		boolean isredstone = getVariant() == BlockSpreader.Variant.REDSTONE;
		if(isredstone)
			shouldShoot = redstone && !redstoneLastTick;

		if(shouldShoot && receiver != null && receiver instanceof IKeyLocked)
			shouldShoot = ((IKeyLocked) receiver).getInputKey().equals(getOutputKey());

		ItemStack lens = itemHandler.getStackInSlot(0);
		ILensControl control = getLensController(lens);
		if(control != null) {
			if(isredstone) {
				if(shouldShoot)
					control.onControlledSpreaderPulse(lens, this, redstone);
			} else control.onControlledSpreaderTick(lens, this, redstone);

			shouldShoot &= control.allowBurstShooting(lens, this, redstone);
		}

		if(shouldShoot)
			tryShootBurst();

		if(receiverLastTick != receiver && !world.isRemote) {
			requestsClientUpdate = true;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
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

		if(cmp.getBoolean(TAG_HAS_IDENTITY)) {
			long most = cmp.getLong(TAG_UUID_MOST);
			long least = cmp.getLong(TAG_UUID_LEAST);
			UUID identity = getIdentifierUnsafe();
			if(identity == null || most != identity.getMostSignificantBits() || least != identity.getLeastSignificantBits())
				this.identity = new UUID(most, least);
		} else getIdentifier();

		mana = cmp.getInt(TAG_MANA);
		rotationX = cmp.getFloat(TAG_ROTATION_X);
		rotationY = cmp.getFloat(TAG_ROTATION_Y);
		requestsClientUpdate = cmp.getBoolean(TAG_REQUEST_UPDATE);

		if(cmp.contains(TAG_INPUT_KEY))
			inputKey = cmp.getString(TAG_INPUT_KEY);
		if(cmp.contains(TAG_OUTPUT_KEY))
			inputKey = cmp.getString(TAG_OUTPUT_KEY);

		mapmakerOverride = cmp.getBoolean(TAG_MAPMAKER_OVERRIDE);
		mmForcedColor = cmp.getInt(TAG_FORCED_COLOR);
		mmForcedManaPayload = cmp.getInt(TAG_FORCED_MANA_PAYLOAD);
		mmForcedTicksBeforeManaLoss = cmp.getInt(TAG_FORCED_TICKS_BEFORE_MANA_LOSS);
		mmForcedManaLossPerTick = cmp.getFloat(TAG_FORCED_MANA_LOSS_PER_TICK);
		mmForcedGravity = cmp.getFloat(TAG_FORCED_GRAVITY);
		mmForcedVelocityMultiplier = cmp.getFloat(TAG_FORCED_VELOCITY_MULTIPLIER);

		if(cmp.contains(TAG_KNOWN_MANA))
			knownMana = cmp.getInt(TAG_KNOWN_MANA);
		if(cmp.contains(TAG_PADDING_COLOR))
			paddingColor = cmp.getInt(TAG_PADDING_COLOR) == -1 ? null : DyeColor.byId(cmp.getInt(TAG_PADDING_COLOR));
		if(cmp.contains(TAG_CAN_SHOOT_BURST))
			canShootBurst = cmp.getBoolean(TAG_CAN_SHOOT_BURST);

		pingbackTicks = cmp.getInt(TAG_PINGBACK_TICKS);
		lastPingbackX = cmp.getDouble(TAG_LAST_PINGBACK_X);
		lastPingbackY = cmp.getDouble(TAG_LAST_PINGBACK_Y);
		lastPingbackZ = cmp.getDouble(TAG_LAST_PINGBACK_Z);

		if(requestsClientUpdate && world != null) {
			int x = cmp.getInt(TAG_FORCE_CLIENT_BINDING_X);
			int y = cmp.getInt(TAG_FORCE_CLIENT_BINDING_Y);
			int z = cmp.getInt(TAG_FORCE_CLIENT_BINDING_Z);
			if(y != -1) {
				TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
				if(tile instanceof IManaReceiver)
					receiver = (IManaReceiver) tile;
				else receiver = null;
			} else receiver = null;
		}

		if(world != null && world.isRemote)
			hasReceivedInitialPacket = true;
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	public void onWanded(PlayerEntity player, ItemStack wand) {
		if(player == null)
			return;

		if(!player.isSneaking()) {
			if(!world.isRemote) {
				CompoundNBT nbttagcompound = new CompoundNBT();
				writePacketNBT(nbttagcompound);
				nbttagcompound.putInt(TAG_KNOWN_MANA, mana);
				if(player instanceof ServerPlayerEntity)
					((ServerPlayerEntity) player).connection.sendPacket(new SUpdateTileEntityPacket(pos, -999, nbttagcompound));
			}
			world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ding, SoundCategory.PLAYERS, 0.1F, 1);
		} else {
			RayTraceResult pos = Item.rayTrace(world, player, RayTraceContext.FluidMode.ANY);
			if(pos instanceof BlockRayTraceResult && !world.isRemote) {
				double x = pos.getHitVec().x - getPos().getX() - 0.5;
				double y = pos.getHitVec().y - getPos().getY() - 0.5;
				double z = pos.getHitVec().z - getPos().getZ() - 0.5;

				BlockRayTraceResult bpos = (BlockRayTraceResult) pos;
				if(bpos.getFace() != Direction.DOWN && bpos.getFace() != Direction.UP) {
					Vector3 clickVector = new Vector3(x, 0, z);
					Vector3 relative = new Vector3(-0.5, 0, 0);
					double angle = Math.acos(clickVector.dotProduct(relative) / (relative.mag() * clickVector.mag())) * 180D / Math.PI;

					rotationX = (float) angle + 180F;
					if(clickVector.z < 0)
						rotationX = 360 - rotationX;
				}

				double angle = y * 180;
				rotationY = -(float) angle;

				checkForReceiver();
				requestsClientUpdate = true;
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, getPos());
			}
		}
	}

	private boolean needsNewBurstSimulation() {
		if(world.isRemote && !hasReceivedInitialPacket)
			return false;

		if(lastTentativeBurst == null)
			return true;

		for(PositionProperties props : lastTentativeBurst)
			if(!props.contentsEqual(world)) {
				invalidTentativeBurst = props.invalid;
				return !invalidTentativeBurst;
			}

		return false;
	}

	private void tryShootBurst() {
		boolean redstone = getVariant() == BlockSpreader.Variant.REDSTONE;
		if((receiver != null || redstone) && !invalidTentativeBurst) {
			if(canShootBurst && (redstone || receiver.canRecieveManaFromBursts() && !receiver.isFull())) {
				EntityManaBurst burst = getBurst(false);
				if(burst != null) {
					if(!world.isRemote) {
						mana -= burst.getStartingMana();
						burst.setShooterUUID(getIdentifier());
						world.addEntity(burst);
						burst.ping();
						if(!ConfigHandler.COMMON.silentSpreaders.get())
							world.playSound(null, pos, ModSounds.spreaderFire, SoundCategory.BLOCKS, 0.05F * (paddingColor != null ? 0.2F : 1F), 0.7F + 0.3F * (float) Math.random());
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
		ItemStack stack = itemHandler.getStackInSlot(0);
		ILensControl control = getLensController(stack);
		if(control != null && !control.allowBurstShooting(stack, this, false))
			return;

		EntityManaBurst fakeBurst = getBurst(true);
		fakeBurst.setScanBeam();
		TileEntity receiver = fakeBurst.getCollidedTile(true);

		if(receiver instanceof IManaReceiver
				&& receiver.hasWorld()
				&& receiver.getWorld().isBlockLoaded(receiver.getPos()))
			this.receiver = (IManaReceiver) receiver;
		else this.receiver = null;
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

		ItemStack lens = itemHandler.getStackInSlot(0);
		if(!lens.isEmpty() && lens.getItem() instanceof ILensEffect)
			((ILensEffect) lens.getItem()).apply(lens, props);

		if(getCurrentMana() >= props.maxMana || fake) {
			EntityManaBurst burst = new EntityManaBurst(this, fake);
			burst.setSourceLens(lens);

			if(mapmakerOverride) {
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
		if(!stack.isEmpty() && stack.getItem() instanceof ILensControl) {
			ILensControl control = (ILensControl) stack.getItem();
			if(control.isControlLens(stack))
				return control;
		}

		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public void renderHUD(Minecraft mc) {
		String name = new ItemStack(getBlockState().getBlock()).getDisplayName().getString();
		int color = getVariant().hudColor;
		HUDHandler.drawSimpleManaHUD(color, knownMana, getMaxMana(), name);

		ItemStack lens = itemHandler.getStackInSlot(0);
		if(!lens.isEmpty()) {
			String lensName = lens.getDisplayName().getString();
			int width = 16 + mc.fontRenderer.getStringWidth(lensName) / 2;
			int x = mc.getWindow().getScaledWidth() / 2 - width;
			int y = mc.getWindow().getScaledHeight() / 2 + 50;

			mc.fontRenderer.drawStringWithShadow(lensName, x + 20, y + 5, color);
			mc.getItemRenderer().renderItemAndEffectIntoGUI(lens, x, y);
			RenderSystem.disableLighting();
		}

		if(receiver != null) {
			TileEntity receiverTile = (TileEntity) receiver;
			ItemStack recieverStack = new ItemStack(world.getBlockState(receiverTile.getPos()).getBlock());
			if(!recieverStack.isEmpty()) {
				String stackName = recieverStack.getDisplayName().getString();
				int width = 16 + mc.fontRenderer.getStringWidth(stackName) / 2;
				int x = mc.getWindow().getScaledWidth() / 2 - width;
				int y = mc.getWindow().getScaledHeight() / 2 + 30;

				mc.fontRenderer.drawStringWithShadow(stackName, x + 20, y + 5, color);
				mc.getItemRenderer().renderItemAndEffectIntoGUI(recieverStack, x, y);
			}

			RenderSystem.disableLighting();
		}

		RenderSystem.color4f(1F, 1F, 1F, 1F);
	}

	@Override
	public void onClientDisplayTick() {
		if(world != null) {
			EntityManaBurst burst = getBurst(true);
			burst.getCollidedTile(false);
		}
	}

	@Override
	public float getManaYieldMultiplier(IManaBurst burst) {
		return 1F;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, true) {
			@Override
			protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
				return 1;
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				if(!stack.isEmpty() && stack.getItem() instanceof ILens)
					return super.insertItem(slot, stack, simulate);
				else return stack;
			}
		};
	}

	@Override
	public void markDirty() {
		checkForReceiver();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
	}

	@Override
	public BlockPos getBinding() {
		if(receiver == null)
			return null;

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
		Vector3 thisVec = Vector3.fromTileEntityCenter(this);
		Vector3 blockVec = new Vector3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

		VoxelShape shape = player.world.getBlockState(pos).getShape(player.world, pos);
		AxisAlignedBB axis = shape.isEmpty() ? new AxisAlignedBB(pos) : shape.getBoundingBox().offset(pos);

		if(!blockVec.isInside(axis))
			blockVec = new Vector3(axis.minX + (axis.maxX - axis.minX) / 2, axis.minY + (axis.maxY - axis.minY) / 2, axis.minZ + (axis.maxZ - axis.minZ) / 2);

		Vector3 diffVec =  blockVec.subtract(thisVec);
		Vector3 diffVec2D = new Vector3(diffVec.x, diffVec.z, 0);
		Vector3 rotVec = new Vector3(0, 1, 0);
		double angle = rotVec.angle(diffVec2D) / Math.PI * 180.0;

		if(blockVec.x < thisVec.x)
			angle = -angle;

		rotationX = (float) angle + 90;

		rotVec = new Vector3(diffVec.x, 0, diffVec.z);
		angle = diffVec.angle(rotVec) * 180F / Math.PI;
		if(blockVec.y < thisVec.y)
			angle = -angle;
		rotationY = (float) angle;

		checkForReceiver();
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
		switch (rotationIn)
		{
		case CLOCKWISE_90:
			rotationX += 270F;
			break;
		case CLOCKWISE_180:
			rotationX += 180F;
			break;
		case COUNTERCLOCKWISE_90:
			rotationX += 90F;
			break;
		default: break;
		}

		if(rotationX >= 360F)
			rotationX -= 360F;
	}

	@Override
	public void mirror(Mirror mirrorIn) {
		switch (mirrorIn)
		{
		case LEFT_RIGHT:
			rotationX = 360F - rotationX;
			break;
		case FRONT_BACK:
			rotationX = 180F - rotationX;
			break;
		default: break;
		}

		if(rotationX < 0F)
			rotationX += 360F;
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
		if(getIdentifier().equals(expectedIdentity)) {
			pingbackTicks = TICKS_ALLOWED_WITHOUT_PINGBACK;
			Entity e = (Entity) burst;
			lastPingbackX = e.getX();
			lastPingbackY = e.getY();
			lastPingbackZ = e.getZ();
			setCanShoot(false);
		}
	}

	@Override
	public UUID getIdentifier() {
		if(identity == null)
			identity = UUID.randomUUID();
		return identity;
	}

	private UUID getIdentifierUnsafe() {
		return identity;
	}

}
