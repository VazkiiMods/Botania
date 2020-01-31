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

import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class SubTileVinculotus extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":vinculotus")
	public static TileEntityType<SubTileVinculotus> TYPE;

	public static final Set<SubTileVinculotus> existingFlowers = Collections.newSetFromMap(new WeakHashMap<>());
	private static final int RANGE = 64;

	public SubTileVinculotus() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(!getWorld().isRemote) {
			existingFlowers.add(this);
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Circle(getEffectivePos(), RANGE);
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

	@SubscribeEvent
	public static void onEndermanTeleport(EnderTeleportEvent event) {
		int cost = 50;

		if(event.getEntityLiving() instanceof EndermanEntity) {
			List<SubTileVinculotus> possibleFlowers = new ArrayList<>();
			for(SubTileVinculotus flower : existingFlowers) {
				BlockPos activePos = flower.getEffectivePos();

				if(flower.redstoneSignal > 0 || flower.getMana() <= cost
						|| flower.getWorld() != event.getEntityLiving().world
						|| flower.getWorld().getTileEntity(flower.getPos()) != flower)
					continue;

				double x = activePos.getX() + 0.5;
				double y = activePos.getY() + 1.5;
				double z = activePos.getZ() + 0.5;

				if(MathHelper.pointDistanceSpace(x, y, z, event.getTargetX(), event.getTargetY(), event.getTargetZ()) < RANGE)
					possibleFlowers.add(flower);
			}

			if(!possibleFlowers.isEmpty()) {
				SubTileVinculotus  flower = possibleFlowers.get(event.getEntityLiving().world.rand.nextInt(possibleFlowers.size()));
				BlockPos activePos = flower.getEffectivePos();

				double x = activePos.getX() + 0.5;
				double y = activePos.getY() + 1.5;
				double z = activePos.getZ() + 0.5;

				event.setTargetX(x + Math.random() * 3 - 1);
				event.setTargetY(y);
				event.setTargetZ(z + Math.random() * 3 - 1);
				flower.addMana(-cost);
				flower.sync();
			}
		}
	}
}
