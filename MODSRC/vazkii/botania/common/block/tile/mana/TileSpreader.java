/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 25, 2014, 9:40:57 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.entity.EntityManaBurst.PositionProperties;
import vazkii.botania.common.lib.LibBlockNames;

public class TileSpreader extends TileSimpleInventory implements IManaCollector, ITileBound {

	private static final int MAX_MANA = 1000;
	private static final String TAG_MANA = "mana";
	private static final String TAG_KNOWN_MANA = "knownMana";
	private static final String TAG_REQUEST_UPDATE = "requestUpdate";
	private static final String TAG_ROTATION_X = "rotationX";
	private static final String TAG_ROTATION_Y = "rotationY";

	private static final String TAG_FORCE_CLIENT_BINDING_X = "forceClientBindingX";
	private static final String TAG_FORCE_CLIENT_BINDING_Y = "forceClientBindingY";
	private static final String TAG_FORCE_CLIENT_BINDING_Z = "forceClientBindingZ";

	public static boolean staticRedstone = false;

	int mana;
	int knownMana = -1;
	public float rotationX, rotationY;
	boolean added = false;

	boolean requestsClientUpdate = false;
	boolean hasReceivedInitialPacket = false;

	IManaReceiver receiver = null;
	IManaReceiver receiverLastTick = null;

	boolean redstoneLastTick = true;
	public boolean canShootBurst = true;
	public int lastBurstDeathTick = -1;
	public int burstParticleTick = 0;

	List<PositionProperties> lastTentativeBurst;

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(this.mana + mana, MAX_MANA);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		ManaNetworkEvent.removeCollector(this);
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		ManaNetworkEvent.removeCollector(this);
	}

	@Override
	public void updateEntity() {
		if(!worldObj.isRemote && !added) {
			ManaNetworkEvent.addCollector(this);
			added = true;
		}

		boolean redstone = false;

		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tileAt = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			if(tileAt instanceof IManaPool) {
				IManaPool pool = (IManaPool) tileAt;
				if(pool != receiver) {
					int manaInPool = pool.getCurrentMana();
					if(manaInPool > 0 && !isFull()) {
						int manaMissing = MAX_MANA - mana;
						int manaToRemove = Math.min(manaInPool, manaMissing);
						pool.recieveMana(-manaToRemove);
						recieveMana(manaToRemove);
					}
				}
			}

			int redstoneSide = worldObj.getIndirectPowerLevelTo(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir.ordinal());
			if(redstoneSide > 0)
				redstone = true;
		}

		if(needsNewBurstSimulation())
			checkForReceiver();

		boolean shouldShoot = !redstone;

		if(isRedstone())
			shouldShoot = redstone && !redstoneLastTick;

		if(shouldShoot)
			tryShootBurst();


		if(receiverLastTick != receiver && !worldObj.isRemote) {
			requestsClientUpdate = true;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

		redstoneLastTick = redstone;
		receiverLastTick = receiver;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		cmp.setInteger(TAG_MANA, mana);
		cmp.setFloat(TAG_ROTATION_X, rotationX);
		cmp.setFloat(TAG_ROTATION_Y, rotationY);
		cmp.setBoolean(TAG_REQUEST_UPDATE, requestsClientUpdate);
		cmp.setInteger(TAG_FORCE_CLIENT_BINDING_X, receiver == null ? 0 : ((TileEntity) receiver).xCoord);
		cmp.setInteger(TAG_FORCE_CLIENT_BINDING_Y, receiver == null ? -1 : ((TileEntity) receiver).yCoord);
		cmp.setInteger(TAG_FORCE_CLIENT_BINDING_Z, receiver == null ? 0 : ((TileEntity) receiver).zCoord);

		requestsClientUpdate = false;
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		mana = cmp.getInteger(TAG_MANA);
		rotationX = cmp.getFloat(TAG_ROTATION_X);
		rotationY = cmp.getFloat(TAG_ROTATION_Y);
		requestsClientUpdate = cmp.getBoolean(TAG_REQUEST_UPDATE);

		if(cmp.hasKey(TAG_KNOWN_MANA))
			knownMana = cmp.getInteger(TAG_KNOWN_MANA);

		if(requestsClientUpdate && worldObj != null) {
			int x = cmp.getInteger(TAG_FORCE_CLIENT_BINDING_X);
			int y = cmp.getInteger(TAG_FORCE_CLIENT_BINDING_Y);
			int z = cmp.getInteger(TAG_FORCE_CLIENT_BINDING_Z);
			if(y != -1) {
				TileEntity tile = worldObj.getTileEntity(x, y, z);
				if(tile instanceof IManaReceiver)
					receiver = (IManaReceiver) tile;
				else receiver = null;
			} else receiver = null;
		}

		if(worldObj != null && worldObj.isRemote)
			hasReceivedInitialPacket = true;
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return !isFull();
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return;

		if(!player.isSneaking()) {
			if(!worldObj.isRemote) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				writeCustomNBT(nbttagcompound);
				nbttagcompound.setInteger(TAG_KNOWN_MANA, mana);
				if(player instanceof EntityPlayerMP)
					((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound));
			}
			worldObj.playSoundAtEntity(player, "random.orb", 0.1F, 1F);
		} else {
			MovingObjectPosition pos = raytraceFromEntity(worldObj, player, true, 5);
			if(pos != null && pos.hitVec != null && !worldObj.isRemote) {
				double x = pos.hitVec.xCoord - xCoord - 0.5;
				double y = pos.hitVec.yCoord - yCoord - 0.5;
				double z = pos.hitVec.zCoord - zCoord - 0.5;

				if(pos.sideHit != 0 && pos.sideHit != 1) {
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
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
	}

	private boolean needsNewBurstSimulation() {
		if(worldObj.isRemote && !hasReceivedInitialPacket)
			return false;

		if(lastTentativeBurst == null)
			return true;

		for(PositionProperties props : lastTentativeBurst)
			if(!props.contentsEqual(worldObj))
				return true;

		return false;
	}

	public void tryShootBurst() {
		if(receiver != null || isRedstone()) {
			if(canShootBurst && (isRedstone() || receiver.canRecieveManaFromBursts() && !receiver.isFull())) {
				EntityManaBurst burst = getBurst(false);
				if(burst != null) {
					if(!worldObj.isRemote) {
						mana -= burst.getStartingMana();
						worldObj.spawnEntityInWorld(burst);
					}

					canShootBurst = false;
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
		}
	}

	public boolean isRedstone() {
		return worldObj == null ? staticRedstone : (getBlockMetadata() & 1) == 1;
	}

	public void checkForReceiver() {
		EntityManaBurst fakeBurst = getBurst(true);
		fakeBurst.setScanBeam();
		TileEntity receiver = fakeBurst.getCollidedTile(true);

		if(receiver != null && receiver instanceof IManaReceiver)
			this.receiver = (IManaReceiver) receiver;
		else this.receiver = null;
		lastTentativeBurst = fakeBurst.propsList;
	}

	public EntityManaBurst getBurst(boolean fake) {
		EntityManaBurst burst = new EntityManaBurst(this, fake);

		int maxMana = 160;
		int color = isRedstone() ? 0xFF2020 : 0x20FF20;
		int ticksBeforeManaLoss = 60;
		float manaLossPerTick = 4F;
		float motionModifier = 1F;
		float gravity = 0F;
		BurstProperties props = new BurstProperties(maxMana, ticksBeforeManaLoss, manaLossPerTick, gravity, motionModifier, color);

		ItemStack lens = getStackInSlot(0);
		if(lens != null && lens.getItem() instanceof ILensEffect)
			((ILensEffect) lens.getItem()).apply(lens, props);

		burst.setSourceLens(lens);
		if(getCurrentMana() >= props.maxMana || fake) {
			burst.setColor(props.color);
			burst.setMana(props.maxMana);
			burst.setStartingMana(props.maxMana);
			burst.setMinManaLoss(props.ticksBeforeManaLoss);
			burst.setManaLossPerTick(props.manaLossPerTick);
			burst.setGravity(props.gravity);
			burst.setMotion(burst.motionX * props.motionModifier, burst.motionY * props.motionModifier, burst.motionZ * props.motionModifier);

			return burst;
		}
		return null;
	}

	public static MovingObjectPosition raytraceFromEntity(World world, Entity player, boolean par3, double range) {
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;
		if (!world.isRemote && player instanceof EntityPlayer)
			d1 += 1.62D;
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
		Vec3 vec3 = world.getWorldVec3Pool().getVecFromPool(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = range;
		if (player instanceof EntityPlayerMP)
			d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
		Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		return world.func_147447_a(vec3, vec31, par3, !par3, par3);
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal(new ItemStack(ModBlocks.spreader, 1, getBlockMetadata()).getUnlocalizedName().replaceAll("tile.", "tile." + LibResources.PREFIX_MOD) + ".name");
		int color = isRedstone() ? 0x66FF0000 : 0x6600FF00;
		HUDHandler.drawSimpleManaHUD(color, knownMana, MAX_MANA, name, res);

		ItemStack lens = getStackInSlot(0);
		if(lens != null) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			String lensName = lens.getDisplayName();
			int width = 16 + mc.fontRenderer.getStringWidth(lensName) / 2;
			int x = res.getScaledWidth() / 2 - width;
			int y = res.getScaledHeight() / 2 + 50;

			mc.fontRenderer.drawStringWithShadow(lensName, x + 20, y + 5, color);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			new RenderItem().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, lens, x, y);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
		}

		if(receiver != null) {
			TileEntity receiverTile = (TileEntity) receiver;
			ItemStack recieverStack = new ItemStack(worldObj.getBlock(receiverTile.xCoord, receiverTile.yCoord, receiverTile.zCoord), 1, receiverTile.getBlockMetadata());
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			if(recieverStack != null && recieverStack.getItem() != null) {
				String stackName = recieverStack.getDisplayName();
				int width = 16 + mc.fontRenderer.getStringWidth(stackName) / 2;
				int x = res.getScaledWidth() / 2 - width;
				int y = res.getScaledHeight() / 2 + 30;

				mc.fontRenderer.drawStringWithShadow(stackName, x + 20, y + 5, color);
				new RenderItem().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, recieverStack, x, y);
			}

			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
		}

		GL11.glColor4f(1F, 1F, 1F, 1F);
	}

	@Override
	public void onClientDisplayTick() {
		if(worldObj != null) {
			EntityManaBurst burst = getBurst(true);
			burst.getCollidedTile(false);
		}
	}

	@Override
	public float getManaYieldMultiplier(IManaBurst burst) {
		return burst.getMana() < 16 ? 0F : 0.95F;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getInventoryName() {
		return LibBlockNames.SPREADER;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return itemstack.getItem() instanceof ILens;
	}

	@Override
	public void markDirty() {
		checkForReceiver();
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public ChunkCoordinates getBinding() {
		if(receiver == null)
			return null;

		TileEntity tile = (TileEntity) receiver;
		return new ChunkCoordinates(tile.xCoord, tile.yCoord, tile.zCoord);
	}

	@Override
	public int getMaxMana() {
		return MAX_MANA;
	}
}
