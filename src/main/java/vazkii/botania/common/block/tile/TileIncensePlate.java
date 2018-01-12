/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 15, 2015, 4:09:07 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.brew.ItemIncenseStick;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.List;

public class TileIncensePlate extends TileSimpleInventory implements ITickable {

	private static final String TAG_TIME_LEFT = "timeLeft";
	private static final String TAG_BURNING = "burning";
	private static final int RANGE = 32;

	public int timeLeft = 0;
	public boolean burning = false;

	public int comparatorOutput = 0;

	@Override
	public void update() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		if(!stack.isEmpty() && burning) {
			Brew brew = ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack);
			PotionEffect effect = brew.getPotionEffects(stack).get(0);
			if(timeLeft > 0) {
				timeLeft--;
				if(!world.isRemote) {
					List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.getX() + 0.5 - RANGE, pos.getY() + 0.5 - RANGE, pos.getZ() + 0.5 - RANGE, pos.getX() + 0.5 + RANGE, pos.getY() + 0.5 + RANGE, pos.getZ() + 0.5 + RANGE));
					for(EntityPlayer player : players) {
						PotionEffect currentEffect = player.getActivePotionEffect(effect.getPotion());
						boolean nightVision = effect.getPotion() == MobEffects.NIGHT_VISION;
						if(currentEffect == null || currentEffect.getDuration() < (nightVision ? 205 : 3)) {
							PotionEffect applyEffect = new PotionEffect(effect.getPotion(), nightVision ? 285 : 80, effect.getAmplifier(), true, true);
							player.addPotionEffect(applyEffect);
						}
					}

					if(world.rand.nextInt(20) == 0)
						world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.1F, 1);
				} else {
					double x = pos.getX() + 0.5;
					double y = pos.getY() + 0.5;
					double z = pos.getZ() + 0.5;
					Color color = new Color(brew.getColor(stack));
					float r = color.getRed() / 255F;
					float g = color.getGreen() / 255F;
					float b = color.getBlue() / 255F;
					Botania.proxy.wispFX(x - (Math.random() - 0.5) * 0.2, y - (Math.random() - 0.5) * 0.2, z - (Math.random() - 0.5) * 0.2, r, g, b, 0.05F + (float) Math.random() * 0.02F, 0.005F - (float) Math.random() * 0.01F, 0.01F + (float) Math.random() * 0.005F, 0.005F - (float) Math.random() * 0.01F);
					Botania.proxy.wispFX(x - (Math.random() - 0.5) * 0.2, y - (Math.random() - 0.5) * 0.2, z - (Math.random() - 0.5) * 0.2, 0.2F, 0.2F, 0.2F, 0.05F + (float) Math.random() * 0.02F, 0.005F - (float) Math.random() * 0.01F, 0.01F + (float) Math.random() * 0.001F, 0.005F - (float) Math.random() * 0.01F);
				}
			} else {
				itemHandler.setStackInSlot(0, ItemStack.EMPTY);
				burning = false;
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		} else timeLeft = 0;

		int newComparator = 0;
		if(!stack.isEmpty())
			newComparator = 1;
		if(burning)
			newComparator = 2;
		if(comparatorOutput != newComparator) {
			comparatorOutput = newComparator;
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
		}
	}

	public void ignite() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		if(stack.isEmpty() || burning)
			return;

		burning = true;
		Brew brew = ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack);
		timeLeft = brew.getPotionEffects(stack).get(0).getDuration() * ItemIncenseStick.TIME_MULTIPLIER;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public void writePacketNBT(NBTTagCompound par1nbtTagCompound) {
		super.writePacketNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_TIME_LEFT, timeLeft);
		par1nbtTagCompound.setBoolean(TAG_BURNING, burning);
	}

	@Override
	public void readPacketNBT(NBTTagCompound par1nbtTagCompound) {
		super.readPacketNBT(par1nbtTagCompound);
		timeLeft = par1nbtTagCompound.getInteger(TAG_TIME_LEFT);
		burning = par1nbtTagCompound.getBoolean(TAG_BURNING);
	}

	public boolean acceptsItem(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() == ModItems.incenseStick && ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack) != BotaniaAPI.fallbackBrew;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, true) {
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				if(acceptsItem(stack))
					return super.insertItem(slot, stack, simulate);
				else return stack;
			}

			@Nonnull
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				return ItemStack.EMPTY;
			}
		};
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if(!world.isRemote)
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

}
