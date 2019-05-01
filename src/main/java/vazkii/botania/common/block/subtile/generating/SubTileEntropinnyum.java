/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 7, 2014, 10:59:44 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class SubTileEntropinnyum extends TileEntityGeneratingFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":entropinnyum")
	public static TileEntityType<SubTileEntropinnyum> TYPE;

	private static final int RANGE = 12;
	private static final int EXPLODE_EFFECT_EVENT = 0;

	public SubTileEntropinnyum() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(!getWorld().isRemote && mana == 0) {
			List<EntityTNTPrimed> tnts = getWorld().getEntitiesWithinAABB(EntityTNTPrimed.class, new AxisAlignedBB(getPos().add(-RANGE, -RANGE, -RANGE), getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for(EntityTNTPrimed tnt : tnts) {
				if(tnt.getFuse() == 1 && !tnt.removed && !getWorld().getBlockState(new BlockPos(tnt)).getMaterial().isLiquid()) {
					tnt.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.2F, (1F + (getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.2F) * 0.7F);
					tnt.remove();
					mana += getMaxMana();
					sync();

					getWorld().addBlockEvent(getPos(), getBlockState().getBlock(), EXPLODE_EFFECT_EVENT, tnt.getEntityId());
					break;
				}
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int event, int param) {
		if(event == EXPLODE_EFFECT_EVENT) {
			if(getWorld().isRemote && getWorld().getEntityByID(param) instanceof EntityTNTPrimed) {
				Entity e = getWorld().getEntityByID(param);

				for(int i = 0; i < 50; i++)
					Botania.proxy.sparkleFX(e.posX + Math.random() * 4 - 2, e.posY + Math.random() * 4 - 2, e.posZ + Math.random() * 4 - 2, 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, (float) (Math.random() * 0.65F + 1.25F), 12);

				getWorld().addParticle(Particles.EXPLOSION_EMITTER, e.posX, e.posY, e.posZ, 1D, 0D, 0D);
			}
			return true;
		} else {
			return super.receiveClientEvent(event, param);
		}
	}

	@Override
	public int getColor() {
		return 0xcb0000;
	}

	@Override
	public int getMaxMana() {
		return 6500;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.entropinnyum;
	}

}
