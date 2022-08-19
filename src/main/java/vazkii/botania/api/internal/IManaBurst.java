/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 31, 2014, 4:36:13 PM (GMT)]
 */
package vazkii.botania.api.internal;

import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;

/**
 * Interface for the Mana Burst entity. This can safely be casted to EntityThrowable.
 */
public interface IManaBurst {

	public boolean isFake();

	public void setMotion(double x, double y, double z);

	public int getColor();

	public void setColor(int color);

	public int getMana();

	public void setMana(int mana);

	public int getStartingMana();

	public void setStartingMana(int mana);

	public int getMinManaLoss();

	public void setMinManaLoss(int minManaLoss);

	public float getManaLossPerTick();

	public void setManaLossPerTick(float mana);

	public float getGravity();

	public void setGravity(float gravity);

	public ChunkCoordinates getBurstSourceChunkCoordinates();

	public void setBurstSourceCoords(int x, int y, int z);

	public ItemStack getSourceLens();

	public void setSourceLens(ItemStack lens);

	public boolean hasAlreadyCollidedAt(int x, int y, int z);

	public void setCollidedAt(int x, int y, int z);

	public int getTicksExisted();

	public void setFake(boolean fake);

	public void setShooterUUID(UUID uuid);

	public UUID getShooterUIID();

	public void ping();

}
