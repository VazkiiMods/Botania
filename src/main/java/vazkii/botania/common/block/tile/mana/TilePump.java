/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 18, 2015, 3:16:57 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.animation.TimeValues;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.TileMod;

import javax.annotation.Nonnull;

public class TilePump extends TileMod implements ITickable {

	private static final String TAG_ACTIVE = "active";

	public float innerRingPos;
	public boolean active = false;
	public boolean hasCart = false;
	public boolean hasCartOnTop = false;
	public float moving = 0F;

	public int comparator;
	public boolean hasRedstone = false;
	private int lastComparator = 0;

	private final TimeValues.VariableValue move;
	private final IAnimationStateMachine asm;

	public TilePump() {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			move = new TimeValues.VariableValue(0);
			asm = ModelLoaderRegistry.loadASM(new ResourceLocation("botania", "asms/block/pump.json"), ImmutableMap.of("move", move));
		} else {
			move = null;
			asm = null;
		}
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityAnimation.ANIMATION_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityAnimation.ANIMATION_CAPABILITY) {
			return CapabilityAnimation.ANIMATION_CAPABILITY.cast(asm);
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void update() {
		hasRedstone = false;
		for(EnumFacing dir : EnumFacing.VALUES) {
			int redstoneSide = world.getRedstonePower(pos.offset(dir), dir);
			if(redstoneSide > 0) {
				hasRedstone = true;
				break;
			}
		}

		float max = 8F;
		float min = 0F;

		float incr = max / 10F;

		if(innerRingPos < max && active && moving >= 0F) {
			innerRingPos += incr;
			moving = incr;
			if(innerRingPos >= max) {
				innerRingPos = Math.min(max, innerRingPos);
				moving = 0F;
				for(int x = 0; x < 2; x++)
					world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, getPos().getX() + Math.random(), getPos().getY() + Math.random(), getPos().getZ() + Math.random(), 0, 0, 0);
			}
		} else if(innerRingPos > min) {
			innerRingPos -= incr * 2;
			moving = -incr * 2;
			if(innerRingPos <= min) {
				innerRingPos = Math.max(min, innerRingPos);
				moving = 0F;
			}
		}

		if(world.isRemote)
			move.setValue(innerRingPos / 8 * 0.5F); // rescale to 0 - 0.5 for json animation

		if(!hasCartOnTop)
			comparator = 0;
		if(!hasCart && active)
			setActive(false);
		if(active && hasRedstone)
			setActive(false);

		hasCart = false;
		hasCartOnTop = false;

		if(comparator != lastComparator)
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
		lastComparator = comparator;
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.setBoolean(TAG_ACTIVE, active);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		boolean prevActive = active;
		active = cmp.getBoolean(TAG_ACTIVE);
		if(world != null && world.isRemote)
			if(prevActive != active)
				asm.transition(active ? "moving" : "default");
	}

	public void setActive(boolean active) {
		if(!world.isRemote) {
			boolean diff = this.active != active;
			this.active = active;
			if(diff)
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
		}
	}
}
