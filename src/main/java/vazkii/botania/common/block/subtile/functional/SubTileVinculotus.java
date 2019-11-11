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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class SubTileVinculotus extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":vinculotus")
	public static TileEntityType<SubTileVinculotus> TYPE;

	// Must store position since red string spoofers are only active during tick
	// But our main logic runs outside, in the event handler
	public static final Map<SubTileVinculotus, BlockPos> existingFlowers = new WeakHashMap<>();
	private static final int RANGE = 64;

	public SubTileVinculotus() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(!getWorld().isRemote) {
		    BlockPos pos = existingFlowers.get(this);
		    if (pos == null || !pos.equals(getPos()))
			    existingFlowers.put(this, getPos());
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Circle(getPos(), RANGE);
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
			List<Pair<SubTileVinculotus, BlockPos>> possibleFlowers = new ArrayList<>();
			for(Map.Entry<SubTileVinculotus, BlockPos> e : existingFlowers.entrySet()) {
				SubTileVinculotus flower = e.getKey();
				BlockPos activePos = e.getValue();

				if(flower == null || flower.redstoneSignal > 0 || flower.getMana() <= cost || flower.getWorld() != event.getEntityLiving().world || flower.getWorld().getTileEntity(flower.getPos()) != flower)
					continue;

				double x = activePos.getX() + 0.5;
				double y = activePos.getY() + 1.5;
				double z = activePos.getZ() + 0.5;

				if(MathHelper.pointDistanceSpace(x, y, z, event.getTargetX(), event.getTargetY(), event.getTargetZ()) < RANGE)
					possibleFlowers.add(ImmutablePair.of(flower, activePos));
			}

			if(!possibleFlowers.isEmpty()) {
				Pair<SubTileVinculotus, BlockPos> p = possibleFlowers.get(event.getEntityLiving().world.rand.nextInt(possibleFlowers.size()));
				SubTileVinculotus flower = p.getLeft();
				BlockPos activePos = p.getRight();

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
