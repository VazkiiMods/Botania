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
package vazkii.botania.common.block.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.internal.ManaNetworkEvent;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.client.core.helper.Vector3;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.entity.EntityManaBurst;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TileSpreader extends TileMod implements IManaCollector {

	private static final int MAX_MANA = 1000;
	private static final String TAG_MANA = "mana";
	private static final String TAG_KNOWN_MANA = "knownMana";
	private static final String TAG_ROTATION_X = "rotationX";
	private static final String TAG_ROTATION_Y = "rotationY";

	int mana;
	int knownMana = -1;
	public float rotationX, rotationY;
	boolean added = false;
	
	IManaReceiver receiver = null;

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
		if(!added) {
			ManaNetworkEvent.addCollector(this);
			added = true;
		}
		tryShootBurst();
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
		cmp.setFloat(TAG_ROTATION_X, rotationX);
		cmp.setFloat(TAG_ROTATION_Y, rotationY);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
		rotationX = cmp.getFloat(TAG_ROTATION_X);
		rotationY = cmp.getFloat(TAG_ROTATION_Y);

		if(cmp.hasKey(TAG_KNOWN_MANA))
			knownMana = cmp.getInteger(TAG_KNOWN_MANA);
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
		if(!player.isSneaking()) {
			if(!worldObj.isRemote) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				writeCustomNBT(nbttagcompound);
				nbttagcompound.setInteger(TAG_KNOWN_MANA, mana);
				PacketDispatcher.sendPacketToPlayer(new Packet132TileEntityData(xCoord, yCoord, zCoord, -999, nbttagcompound), (Player) player);
			}
			worldObj.playSoundAtEntity(player, "random.orb", 1F, 1F);
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
									
					rotationX = (float) angle;
					if(clickVector.z < 0)
						rotationX = 360 - rotationX;
				}

				double angle = y * 180;			
				rotationY = (float) angle;
				
				PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
			}
		}
	}
	
	public boolean canShootBurst = true;
	
	public void tryShootBurst() {
		if(receiver == null) {
			EntityManaBurst fakeBurst = getBurst(true);
			TileEntity receiver = fakeBurst.getCollidedTile(true);
			if(receiver != null && receiver instanceof IManaReceiver)
				this.receiver = (IManaReceiver) receiver;
		}
		
		if(receiver != null) {
			TileEntity tile = (TileEntity) receiver;
			TileEntity tileAt = worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
			if(tileAt instanceof IManaReceiver)
				receiver = (IManaReceiver) tileAt;
			else receiver = null;
			
			if(receiver != null) {
				if(canShootBurst && receiver.canRecieveManaFromBursts() && !receiver.isFull()) {
					EntityManaBurst burst = getBurst(false);
					if(burst != null) {
						mana -= burst.getMana();
						worldObj.spawnEntityInWorld(burst);
						canShootBurst = false;
					}
				}
			}
		}
	}
	
	public EntityManaBurst getBurst(boolean fake) {
		int color = 0x00FF00;
		// Apply color changes here.
		
		EntityManaBurst burst = new EntityManaBurst(worldObj, this, fake, color);
		
		int maxMana = 160;
		// Apply max mana changes here.
		
		int ticksBeforeManaLoss = 100;
		// Apply ticks before mana loss changes here
		
		float manaLossPerTick = 4F;
		// Apply mana loss per tick changes here
		
		if(getCurrentMana() >= maxMana || fake) {
			burst.setMana(maxMana);
			burst.setStartingMana(maxMana);
			burst.setMinManaLoss(ticksBeforeManaLoss);
			burst.setManaLossPerTick(manaLossPerTick);
			
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
		return world.rayTraceBlocks_do_do(vec3, vec31, par3, !par3);
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = ModBlocks.spreader.getLocalizedName();
		int type = 0;
		if(knownMana >= 0) {
			type = 1;
			double percentage = (double) knownMana / (double) MAX_MANA * 100;
			if(percentage == 100)
				type = 5;
			else if(percentage >= 75)
				type = 4;
			else if(percentage >= 50)
				type = 3;
			else if(percentage > 0)
				type = 2;
		}
		String filling = StatCollector.translateToLocal("botaniamisc.status" + type);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int color = 0x66FFFFFF;
		mc.fontRenderer.drawStringWithShadow(name, res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(name) / 2, res.getScaledHeight() / 2 + 10, color);
		mc.fontRenderer.drawStringWithShadow(filling, res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(filling) / 2, res.getScaledHeight() / 2 + 20, color);
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void onClientDisplayTick() {
	}

}
