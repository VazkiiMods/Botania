/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 15, 2014, 4:30:08 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class SubTileVinculotus extends SubTileFunctional {

	public static final Set<SubTileVinculotus> existingFlowers = Collections.newSetFromMap(new WeakHashMap<>());
	private static boolean registered = false;
	private static final int RANGE = 64;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(!supertile.getWorld().isRemote && !existingFlowers.contains(this)) {
			existingFlowers.add(this);
			if(!registered) {
				MinecraftForge.EVENT_BUS.register(EndermanIntercepter.class);
				registered = true;
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(toBlockPos(), RANGE);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getColor() {
		return 0x0A6051;
	}

	@Override
	public int getMaxMana() {
		return 500;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.vinculotus;
	}

	public static class EndermanIntercepter {

		@SubscribeEvent
		public static void onEndermanTeleport(EnderTeleportEvent event) {
			int cost = 50;

			if(event.getEntityLiving() instanceof EntityEnderman) {
				List<SubTileVinculotus> possibleFlowers = new ArrayList<>();
				for(SubTileVinculotus flower : existingFlowers) {
					if(flower.redstoneSignal > 0 || flower.mana <= cost || flower.supertile.getWorld() != event.getEntityLiving().worldObj || flower.supertile.getWorld().getTileEntity(flower.supertile.getPos()) != flower.supertile)
						continue;

					double x = flower.supertile.getPos().getX() + 0.5;
					double y = flower.supertile.getPos().getY() + 1.5;
					double z = flower.supertile.getPos().getZ() + 0.5;

					if(MathHelper.pointDistanceSpace(x, y, z, event.getTargetX(), event.getTargetY(), event.getTargetZ()) < RANGE)
						possibleFlowers.add(flower);
				}

				if(!possibleFlowers.isEmpty()) {
					SubTileVinculotus flower = possibleFlowers.get(event.getEntityLiving().worldObj.rand.nextInt(possibleFlowers.size()));

					double x = flower.supertile.getPos().getX() + 0.5;
					double y = flower.supertile.getPos().getY() + 1.5;
					double z = flower.supertile.getPos().getZ() + 0.5;

					event.setTargetX(x + Math.random() * 3 - 1);
					event.setTargetY(y);
					event.setTargetZ(z + Math.random() * 3 - 1);
					flower.mana -= cost;
					flower.sync();
				}
			}
		}

	}

}
