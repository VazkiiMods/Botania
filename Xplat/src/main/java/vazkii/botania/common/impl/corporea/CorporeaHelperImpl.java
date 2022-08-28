/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl.corporea;

import com.google.common.base.Predicates;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.corporea.*;
import vazkii.botania.common.block.tile.corporea.TileCorporeaRetainer;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.*;
import java.util.function.Function;

public class CorporeaHelperImpl implements CorporeaHelper {
	private final WeakHashMap<ICorporeaSpark, Set<ICorporeaNode>> cachedNetworks = new WeakHashMap<>();

	@Override
	public Set<ICorporeaNode> getNodesOnNetwork(ICorporeaSpark spark) {
		ICorporeaSpark master = spark.getMaster();
		if (master == null) {
			return Collections.emptySet();
		}
		Set<ICorporeaSpark> network = master.getConnections();

		var cache = cachedNetworks.get(master);
		if (cache != null) {
			return cache;
		}

		Set<ICorporeaNode> nodes = new LinkedHashSet<>();
		if (network != null) {
			for (ICorporeaSpark otherSpark : network) {
				if (otherSpark != null) {
					nodes.add(otherSpark.getSparkNode());
				}
			}
		}

		cachedNetworks.put(master, nodes);
		return nodes;
	}

	@Override
	public ICorporeaRequestMatcher createMatcher(ItemStack stack, boolean checkNBT) {
		return new CorporeaItemStackMatcher(stack, checkNBT);
	}

	@Override
	public ICorporeaRequestMatcher createMatcher(String name) {
		return new CorporeaStringMatcher(name);
	}

	@Override
	public ICorporeaResult requestItem(ICorporeaRequestMatcher matcher, int itemCount, ICorporeaSpark spark, boolean doit) {
		List<ItemStack> stacks = new ArrayList<>();
		if (IXplatAbstractions.INSTANCE.fireCorporeaRequestEvent(matcher, itemCount, spark, !doit)) {
			return new CorporeaResult(stacks, 0, 0, Object2IntMaps.emptyMap());
		}

		Object2IntMap<ICorporeaNode> matchCountByNode = new Object2IntOpenHashMap<>();
		Set<ICorporeaNode> nodes = getNodesOnNetwork(spark);
		Map<ICorporeaInterceptor, ICorporeaSpark> interceptors = new HashMap<>();

		ICorporeaRequest request = new CorporeaRequest(matcher, itemCount);
		for (ICorporeaNode node : nodes) {
			ICorporeaSpark invSpark = node.getSpark();

			BlockEntity te = node.getWorld().getBlockEntity(node.getPos());
			if (te instanceof ICorporeaInterceptor interceptor) {
				interceptor.interceptRequest(matcher, itemCount, invSpark, spark, stacks, nodes, doit);
				interceptors.put(interceptor, invSpark);
			}

			var nodeStacks = doit ? node.extractItems(request) : node.countItems(request);
			int sum = nodeStacks.stream().mapToInt(ItemStack::getCount).sum();
			matchCountByNode.mergeInt(node, sum, Integer::sum);
			stacks.addAll(nodeStacks);
		}

		for (ICorporeaInterceptor interceptor : interceptors.keySet()) {
			interceptor.interceptRequestLast(matcher, itemCount, interceptors.get(interceptor), spark, stacks, nodes, doit);
		}

		return new CorporeaResult(stacks, request.getFound(), request.getExtracted(), matchCountByNode);
	}

	@Override
	public ICorporeaSpark getSparkForBlock(Level world, BlockPos pos) {
		List<Entity> sparks = world.getEntitiesOfClass(Entity.class, new AABB(pos.above(), pos.offset(1, 2, 1)), Predicates.instanceOf(ICorporeaSpark.class));
		return sparks.isEmpty() ? null : (ICorporeaSpark) sparks.get(0);
	}

	@Override
	public int signalStrengthForRequestSize(int requestSize) {
		if (requestSize <= 0) {
			return 0;
		} else if (requestSize >= 16384) {
			return 15;
		} else {
			return Math.min(15, Mth.log2(requestSize) + 1);
		}
	}

	@Override
	public <T extends ICorporeaRequestMatcher> void registerRequestMatcher(ResourceLocation id, Class<T> clazz, Function<CompoundTag, T> deserializer) {
		TileCorporeaRetainer.addCorporeaRequestMatcher(id, clazz, deserializer);
	}

	public void clearCache() {
		cachedNetworks.clear();
	}
}
