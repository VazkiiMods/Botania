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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import vazkii.botania.api.corporea.*;
import vazkii.botania.common.block.tile.corporea.TileCorporeaRetainer;

import java.util.*;
import java.util.function.Function;

public class CorporeaHelperImpl implements CorporeaHelper {
	private final WeakHashMap<List<ICorporeaSpark>, List<ICorporeaNode>> cachedNetworks = new WeakHashMap<>();

	@Override
	public List<ICorporeaNode> getNodesOnNetwork(ICorporeaSpark spark) {
		ICorporeaSpark master = spark.getMaster();
		if (master == null) {
			return Collections.emptyList();
		}
		List<ICorporeaSpark> network = master.getConnections();

		if (cachedNetworks.containsKey(network)) {
			List<ICorporeaNode> cache = cachedNetworks.get(network);
			if (cache != null) {
				return cache;
			}
		}

		List<ICorporeaNode> nodes = new ArrayList<>();
		if (network != null) {
			for (ICorporeaSpark otherSpark : network) {
				if (otherSpark != null) {
					nodes.add(otherSpark.getSparkNode());
				}
			}
		}

		cachedNetworks.put(network, nodes);
		return nodes;
	}

	@Override
	public int getCountInNetwork(ICorporeaRequestMatcher matcher, ICorporeaSpark spark) {
		return getCountInNetwork(matcher, getNodesOnNetwork(spark));
	}

	@Override
	public int getCountInNetwork(ICorporeaRequestMatcher matcher, List<ICorporeaNode> inventories) {
		Map<ICorporeaNode, Integer> map = getInventoriesWithMatchInNetwork(matcher, inventories);
		int count = 0;

		for (int value : map.values()) {
			count += value;
		}

		return count;
	}

	@Override
	public Map<ICorporeaNode, Integer> getInventoriesWithMatchInNetwork(ICorporeaRequestMatcher matcher, ICorporeaSpark spark) {
		List<ICorporeaNode> inventories = getNodesOnNetwork(spark);
		return getInventoriesWithMatchInNetwork(matcher, inventories);
	}

	@Override
	public Map<ICorporeaNode, Integer> getInventoriesWithMatchInNetwork(ICorporeaRequestMatcher matcher, List<ICorporeaNode> nodes) {
		Map<ICorporeaNode, Integer> countMap = new HashMap<>();
		for (ICorporeaNode node : nodes) {
			ICorporeaRequest request = new CorporeaRequest(matcher, -1);
			node.countItems(request);
			if (request.getFound() > 0) {
				countMap.put(node, request.getFound());
			}

		}

		return countMap;
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
		if (CorporeaRequestCallback.EVENT.invoker().onRequest(matcher, itemCount, spark, !doit)) {
			return new CorporeaResult(stacks, 0, 0);
		}

		List<ICorporeaNode> nodes = getNodesOnNetwork(spark);
		Map<ICorporeaInterceptor, ICorporeaSpark> interceptors = new HashMap<>();

		ICorporeaRequest request = new CorporeaRequest(matcher, itemCount);
		for (ICorporeaNode node : nodes) {
			ICorporeaSpark invSpark = node.getSpark();

			BlockEntity te = node.getWorld().getBlockEntity(node.getPos());
			if (te instanceof ICorporeaInterceptor) {
				ICorporeaInterceptor interceptor = (ICorporeaInterceptor) te;
				interceptor.interceptRequest(matcher, itemCount, invSpark, spark, stacks, nodes, doit);
				interceptors.put(interceptor, invSpark);
			}

			if (doit) {
				stacks.addAll(node.extractItems(request));
			} else {
				stacks.addAll(node.countItems(request));
			}
		}

		for (ICorporeaInterceptor interceptor : interceptors.keySet()) {
			interceptor.interceptRequestLast(matcher, itemCount, interceptors.get(interceptor), spark, stacks, nodes, doit);
		}

		return new CorporeaResult(stacks, request.getFound(), request.getExtracted());
	}

	@Override
	public ICorporeaSpark getSparkForBlock(World world, BlockPos pos) {
		List<Entity> sparks = world.getEntities(Entity.class, new Box(pos.up(), pos.add(1, 2, 1)), Predicates.instanceOf(ICorporeaSpark.class));
		return sparks.isEmpty() ? null : (ICorporeaSpark) sparks.get(0);
	}

	@Override
	public int signalStrengthForRequestSize(int requestSize) {
		if (requestSize <= 0) {
			return 0;
		} else if (requestSize >= 16384) {
			return 15;
		} else {
			return Math.min(15, MathHelper.log2(requestSize) + 1);
		}
	}

	@Override
	public <T extends ICorporeaRequestMatcher> void registerRequestMatcher(Identifier id, Class<T> clazz, Function<CompoundTag, T> deserializer) {
		TileCorporeaRetainer.addCorporeaRequestMatcher(id, clazz, deserializer);
	}

	public void clearCache() {
		cachedNetworks.clear();
	}
}
