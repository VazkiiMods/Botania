/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 26, 2014, 5:09:12 PM (GMT)]
 */
package vazkii.botania.common.entity;

import java.awt.Color;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileSpreader;

public class EntityManaBurst extends EntityThrowable {

	private static final String TAG_COLOR = "color";
	
	boolean fake = false;
	
	public EntityManaBurst(World world) {
		super(world);
		setSize(0F, 0F);
		for(int i = 0; i < 8; i++) {
			int j = 24 + i;
			if(j == 28)
				dataWatcher.addObject(j, 0F);
			else dataWatcher.addObject(j, 0);

			dataWatcher.setObjectWatched(j);
		}
	}
	
	public EntityManaBurst(World par1World, TileSpreader spreader, boolean fake, int color) {
		this(par1World);
		this.fake = fake;
		
		setBurstSourceCoords(spreader.xCoord, spreader.yCoord, spreader.zCoord);
		setLocationAndAngles(spreader.xCoord + 0.5, spreader.yCoord + 0.5, spreader.zCoord + 0.5, 0, 0);
		rotationYaw = -(spreader.rotationX + 90F);
		rotationPitch = spreader.rotationY;
		
		float f = 0.4F;
        motionX = (MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f) / 2D;
        motionZ = -(MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f) / 2D;
        motionY = (MathHelper.sin((rotationPitch + func_70183_g()) / 180.0F * (float) Math.PI) * f) / 2D;
	
//        posX += motionX * 5;
//        posY += motionY * 5;
//        posZ += motionZ * 5;
        setColor(color);
	}
	
	float accumulatedManaLoss = 0;
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		particles();
		
		int mana = getMana();
		if(ticksExisted >= getMinManaLoss()) {
			accumulatedManaLoss += getManaLossPerTick();
			int loss = (int) accumulatedManaLoss;
			setMana(mana - loss);
			accumulatedManaLoss -= loss;
			
			if(getMana() <= 0)
				setDead();
		}
	}
	
	TileEntity collidedTile = null;
	boolean noParticles = false;
	
	public TileEntity getCollidedTile(boolean noParticles) {
		this.noParticles = noParticles;
		
		while(!isDead) {
			++ticksExisted;
			onUpdate();
		}
		return collidedTile;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_COLOR, getColor());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readEntityFromNBT(par1nbtTagCompound);
		setColor(par1nbtTagCompound.getInteger(TAG_COLOR));
	}
	
	public void particles() {
		if(!worldObj.isRemote || isDead)
			return;
		
		Color color = new Color(getColor());
		float r = (float) color.getRed() / 255F;
		float g = (float) color.getGreen() / 255F;
		float b = (float) color.getBlue() / 255F;

		if(fake) {
			if(!noParticles)
				Botania.proxy.sparkleFX(worldObj, posX, posY, posZ, r, g, b, 1F, 0, true);
		} else {
			int mana = getMana();
			int maxMana = getStartingMana();
			float size = (float) mana / (float) maxMana;
			Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.25F * size, (float) -motionX * 0.01F, (float) -motionY * 0.01F, (float) -motionZ * 0.01F);
		}
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		if(movingobjectposition.entityHit == null) {
			TileEntity tile = worldObj.getBlockTileEntity(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
			collidedTile = tile;
			
			ChunkCoordinates coords = getBurstSourceChunkCoordinates();
			if(tile == null || tile.xCoord != coords.posX || tile.yCoord != coords.posY || tile.zCoord != coords.posZ) {
				if(!worldObj.isRemote && tile != null && tile instanceof IManaReceiver && ((IManaReceiver) tile).canRecieveManaFromBursts()) {
					((IManaReceiver) tile).recieveMana(getMana());
					PacketDispatcher.sendPacketToAllInDimension(tile.getDescriptionPacket(), worldObj.provider.dimensionId);
				}
				
				setDead();
			}
		}
	}
	
	@Override
	public void setDead() {
		super.setDead();
		
		ChunkCoordinates coords = getBurstSourceChunkCoordinates();
		TileEntity tile = worldObj.getBlockTileEntity(coords.posX, coords.posY, coords.posZ);
		if(tile != null && tile instanceof TileSpreader)
			((TileSpreader) tile).canShootBurst = true;
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	int getColor() {
		return dataWatcher.getWatchableObjectInt(24);
	}
	
	void setColor(int color) {
		dataWatcher.updateObject(24, color);
	}
	
	public int getMana() {
		return dataWatcher.getWatchableObjectInt(25);
	}
	
	public void setMana(int mana) {
		dataWatcher.updateObject(25, mana);
	}
	
	public int getStartingMana() {
		return dataWatcher.getWatchableObjectInt(26);
	}
	
	public void setStartingMana(int mana) {
		dataWatcher.updateObject(26, mana);
	}
	
	public int getMinManaLoss() {
		return dataWatcher.getWatchableObjectInt(27);
	}
	
	public void setMinManaLoss(int minManaLoss) {
		dataWatcher.updateObject(27, minManaLoss);
	}
	
	public float getManaLossPerTick() {
		return dataWatcher.getWatchableObjectFloat(28);
	}
	
	public void setManaLossPerTick(float mana) {
		dataWatcher.updateObject(28, mana);
	}
	
	public ChunkCoordinates getBurstSourceChunkCoordinates() {
		int x = dataWatcher.getWatchableObjectInt(29);
		int y = dataWatcher.getWatchableObjectInt(30);
		int z = dataWatcher.getWatchableObjectInt(31);

		return new ChunkCoordinates(x, y, z);
	}
	
	public void setBurstSourceCoords(int x, int y, int z) {
		dataWatcher.updateObject(29, x);
		dataWatcher.updateObject(30, y);
		dataWatcher.updateObject(31, z);
	}
}
