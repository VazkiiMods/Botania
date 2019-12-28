package vazkii.botania.common.block.subtile.functional;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.lib.LibMisc;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class SubTileBergamute extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":bergamute")
	public static TileEntityType<SubTileBergamute> TYPE;

	private static final int RANGE = 4;
	private static final Set<SubTileBergamute> existingFlowers = Collections.newSetFromMap(new WeakHashMap<>());

	public SubTileBergamute() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote) {
			if(!existingFlowers.contains(this)) {
				existingFlowers.add(this);
			}
		}
	}

	// todo seems expensive when we have lots of sounds cache maybe?
	protected static SubTileBergamute getBergamuteNearby(float x, float y, float z) {
		return existingFlowers.stream()
				.filter(f -> f.redstoneSignal == 0)
				.filter(f -> f.getEffectivePos().distanceSq(x, y, z, false) <= RANGE * RANGE)
				.findAny().orElse(null);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getMaxMana() {
		return 1;
	}

	@Override
	public int getColor() {
		return 0xF46C6C;
	}

	@Override
	public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Circle(getEffectivePos(), RANGE);
	}

}
