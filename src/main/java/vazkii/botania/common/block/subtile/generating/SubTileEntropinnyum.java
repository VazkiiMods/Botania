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

import java.util.List;

import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileEntropinnyum extends SubTileGenerating {

	private static final int RANGE = 12;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(mana == 0) {
			List<EntityTNTPrimed> tnts = supertile.getWorld().getEntitiesWithinAABB(EntityTNTPrimed.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for(EntityTNTPrimed tnt : tnts) {
				if(tnt.fuse == 1 && !tnt.isDead && !supertile.getWorld().getBlockState(new BlockPos(tnt)).getBlock().getMaterial().isLiquid()) {
					if(!supertile.getWorld().isRemote) {
						tnt.setDead();
						mana += getMaxMana();
						supertile.getWorld().playSoundEffect(tnt.posX, tnt.posY, tnt.posZ, "random.explode", 0.2F, (1F + (supertile.getWorld().rand.nextFloat() - supertile.getWorld().rand.nextFloat()) * 0.2F) * 0.7F);
						sync();
					}

					for(int i = 0; i < 50; i++)
						Botania.proxy.sparkleFX(tnt.worldObj, tnt.posX + Math.random() * 4 - 2, tnt.posY + Math.random() * 4 - 2, tnt.posZ + Math.random() * 4 - 2, 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, (float) (Math.random() * 0.65F + 1.25F), 12);

					supertile.getWorld().spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, tnt.posX, tnt.posY, tnt.posZ, 1D, 0D, 0D);
					return;
				}
			}
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
	};

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.entropinnyum;
	}

}
