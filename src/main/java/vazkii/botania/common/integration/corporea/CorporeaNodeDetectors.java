/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.corporea;

import net.minecraft.Util;
import net.minecraft.world.level.Level;

import vazkii.botania.api.corporea.ICorporeaNode;
import vazkii.botania.api.corporea.ICorporeaNodeDetector;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.common.impl.corporea.DummyCorporeaNode;

import java.util.*;

/**
 * Responsible for taking a world position and trying to produce an
 * {@link vazkii.botania.api.corporea.ICorporeaNode} from it.
 */
public class CorporeaNodeDetectors {
	// List of detectors, which will be called from left to right. The first nonnull result will be taken as the overall result.
	private static final Deque<ICorporeaNodeDetector> DETECTORS = Util.make(new ArrayDeque<>(), d -> {
		d.addLast(new VanillaNodeDetector());
	});

	public static synchronized void register(ICorporeaNodeDetector detector) {
		DETECTORS.addFirst(detector);
	}

	public static ICorporeaNode findNode(Level world, ICorporeaSpark spark) {
		for (ICorporeaNodeDetector detector : DETECTORS) {
			ICorporeaNode node = detector.getNode(world, spark);
			if (node != null) {
				return node;
			}
		}
		return new DummyCorporeaNode(world, spark.getAttachPos(), spark);
	}
}
