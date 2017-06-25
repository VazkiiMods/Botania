package vazkii.botania.common.block.subtile.functional;

import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class SubTileBergamute extends SubTileFunctional {
	private static final int RANGE = 4;
	private static final Set<SubTileBergamute> existingFlowers = Collections.newSetFromMap(new WeakHashMap<>());

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (supertile.getWorld().isRemote) {
			if(!existingFlowers.contains(this)) {
				existingFlowers.add(this);
			}
		}
	}

	// todo seems expensive when we have lots of sounds cache maybe?
	protected static SubTileBergamute getBergamuteNearby(float x, float y, float z) {
		return existingFlowers.stream()
				.filter(f -> f.redstoneSignal == 0)
				.filter(f -> f.supertile.getWorld().getTileEntity(f.supertile.getPos()) == f.supertile)
				.filter(f -> f.supertile.getDistanceSq(x, y, z) <= RANGE * RANGE)
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
		return new RadiusDescriptor.Circle(toBlockPos(), RANGE);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.bergamute;
	}

}
