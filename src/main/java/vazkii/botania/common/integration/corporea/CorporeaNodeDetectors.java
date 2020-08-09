/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.corporea;

import net.minecraft.world.World;

import vazkii.botania.api.corporea.ICorporeaNode;
import vazkii.botania.api.corporea.ICorporeaNodeDetector;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.impl.corporea.DummyCorporeaNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Responsible for taking a world position and trying to produce an
 * {@link vazkii.botania.api.corporea.ICorporeaNode} from it.
 */
public class CorporeaNodeDetectors {
	private static final List<ICorporeaNodeDetector> API_DETECTORS = Collections.synchronizedList(new ArrayList<>());

	// List of detectors, which will be called from first to last. The first nonnull result will be taken as the overall result.
	private static final List<ICorporeaNodeDetector> DETECTORS = new ArrayList<>();

	public static void register(ICorporeaNodeDetector detector) {
		API_DETECTORS.add(detector);
	}

	public static void init() {
		DETECTORS.clear();
		DETECTORS.addAll(API_DETECTORS);
		DETECTORS.add(new VanillaNodeDetector());
	}

	public static ICorporeaNode findNode(World world, ICorporeaSpark spark) {
		for (ICorporeaNodeDetector detector : DETECTORS) {
			ICorporeaNode node = detector.getNode(world, spark);
			if (node != null) {
				return node;
			}
		}
		return new DummyCorporeaNode(world, spark.getAttachPos(), spark);
	}
}
