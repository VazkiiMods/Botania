/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.brew.ItemIncenseStick;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.List;

public class TileIncensePlate extends TileSimpleInventory implements ITickableTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.INCENSE_PLATE) public static TileEntityType<TileIncensePlate> TYPE;
	private static final String TAG_TIME_LEFT = "timeLeft";
	private static final String TAG_BURNING = "burning";
	private static final int RANGE = 32;

	private int timeLeft = 0;
	public boolean burning = false;
	public int comparatorOutput = 0;

	public TileIncensePlate() {
		super(TYPE);
	}

	@Override
	public void tick() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		if (!stack.isEmpty() && burning) {
			Brew brew = ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack);
			EffectInstance effect = brew.getPotionEffects(stack).get(0);
			if (timeLeft > 0) {
				timeLeft--;
				if (!world.isRemote) {
					List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos.getX() + 0.5 - RANGE, pos.getY() + 0.5 - RANGE, pos.getZ() + 0.5 - RANGE, pos.getX() + 0.5 + RANGE, pos.getY() + 0.5 + RANGE, pos.getZ() + 0.5 + RANGE));
					for (PlayerEntity player : players) {
						EffectInstance currentEffect = player.getActivePotionEffect(effect.getPotion());
						boolean nightVision = effect.getPotion() == Effects.NIGHT_VISION;
						if (currentEffect == null || currentEffect.getDuration() < (nightVision ? 205 : 3)) {
							EffectInstance applyEffect = new EffectInstance(effect.getPotion(), nightVision ? 285 : 80, effect.getAmplifier(), true, true);
							player.addPotionEffect(applyEffect);
						}
					}

					if (world.rand.nextInt(20) == 0) {
						world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.1F, 1);
					}
				} else {
					double x = pos.getX() + 0.5;
					double y = pos.getY() + 0.5;
					double z = pos.getZ() + 0.5;

					int color = brew.getColor(stack);
					float r = (color >> 16 & 0xFF) / 255F;
					float g = (color >> 8 & 0xFF) / 255F;
					float b = (color & 0xFF) / 255F;

					WispParticleData data1 = WispParticleData.wisp(0.05F + (float) Math.random() * 0.02F, r, g, b);
					world.addParticle(data1, x - (Math.random() - 0.5) * 0.2, y - (Math.random() - 0.5) * 0.2, z - (Math.random() - 0.5) * 0.2, 0.005F - (float) Math.random() * 0.01F, 0.01F + (float) Math.random() * 0.005F, 0.005F - (float) Math.random() * 0.01F);
					WispParticleData data = WispParticleData.wisp(0.05F + (float) Math.random() * 0.02F, 0.2F, 0.2F, 0.2F);
					world.addParticle(data, x - (Math.random() - 0.5) * 0.2, y - (Math.random() - 0.5) * 0.2, z - (Math.random() - 0.5) * 0.2, 0.005F - (float) Math.random() * 0.01F, 0.01F + (float) Math.random() * 0.001F, 0.005F - (float) Math.random() * 0.01F);
				}
			} else {
				itemHandler.setStackInSlot(0, ItemStack.EMPTY);
				burning = false;
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		} else {
			timeLeft = 0;
		}

		int newComparator = 0;
		if (!stack.isEmpty()) {
			newComparator = 1;
		}
		if (burning) {
			newComparator = 2;
		}
		if (comparatorOutput != newComparator) {
			comparatorOutput = newComparator;
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
		}
	}

	public void ignite() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		if (stack.isEmpty() || burning) {
			return;
		}

		burning = true;
		Brew brew = ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack);
		timeLeft = brew.getPotionEffects(stack).get(0).getDuration() * ItemIncenseStick.TIME_MULTIPLIER;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public void writePacketNBT(CompoundNBT tag) {
		super.writePacketNBT(tag);
		tag.putInt(TAG_TIME_LEFT, timeLeft);
		tag.putBoolean(TAG_BURNING, burning);
	}

	@Override
	public void readPacketNBT(CompoundNBT tag) {
		super.readPacketNBT(tag);
		timeLeft = tag.getInt(TAG_TIME_LEFT);
		burning = tag.getBoolean(TAG_BURNING);
	}

	public boolean acceptsItem(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() == ModItems.incenseStick && ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack) != ModBrews.fallbackBrew;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, true) {
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				if (acceptsItem(stack)) {
					return super.insertItem(slot, stack, simulate);
				} else {
					return stack;
				}
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
		if (!world.isRemote) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

}
