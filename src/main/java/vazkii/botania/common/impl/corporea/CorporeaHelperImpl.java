package vazkii.botania.common.impl.corporea;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.corporea.*;
import vazkii.botania.common.block.tile.corporea.TileCorporeaRetainer;

import java.util.*;
import java.util.function.Function;

public class CorporeaHelperImpl implements CorporeaHelper {
	private final WeakHashMap<List<ICorporeaSpark>, List<InvWithLocation>> cachedNetworks = new WeakHashMap<>();

	@Override
	public List<InvWithLocation> getInventoriesOnNetwork(ICorporeaSpark spark) {
		ICorporeaSpark master = spark.getMaster();
		if (master == null) {
			return Collections.emptyList();
		}
		List<ICorporeaSpark> network = master.getConnections();

		if (cachedNetworks.containsKey(network)) {
			List<InvWithLocation> cache = cachedNetworks.get(network);
			if (cache != null) {
				return cache;
			}
		}

		List<InvWithLocation> inventories = new ArrayList<>();
		if (network != null) {
			for (ICorporeaSpark otherSpark : network) {
				if (otherSpark != null) {
					InvWithLocation inv = otherSpark.getSparkInventory();
					if (inv != null) {
						inventories.add(inv);
					}
				}
			}
		}

		cachedNetworks.put(network, inventories);
		return inventories;
	}

	@Override
	public int getCountInNetwork(ICorporeaRequestMatcher matcher, ICorporeaSpark spark) {
		List<InvWithLocation> inventories = getInventoriesOnNetwork(spark);
		return getCountInNetwork(matcher, inventories);
	}

	@Override
	public int getCountInNetwork(ICorporeaRequestMatcher matcher, List<InvWithLocation> inventories) {
		Map<InvWithLocation, Integer> map = getInventoriesWithMatchInNetwork(matcher, inventories);
		return getCountInNetwork(matcher, map);
	}

	@Override
	public int getCountInNetwork(ICorporeaRequestMatcher matcher, Map<InvWithLocation, Integer> inventories) {
		int count = 0;

		for (int value : inventories.values()) {
			count += value;
		}

		return count;
	}

	@Override
	public Map<InvWithLocation, Integer> getInventoriesWithMatchInNetwork(ICorporeaRequestMatcher matcher, ICorporeaSpark spark) {
		List<InvWithLocation> inventories = getInventoriesOnNetwork(spark);
		return getInventoriesWithMatchInNetwork(matcher, inventories);
	}

	@Override
	public Map<InvWithLocation, Integer> getInventoriesWithMatchInNetwork(ICorporeaRequestMatcher matcher, List<InvWithLocation> inventories) {
		Map<InvWithLocation, Integer> countMap = new HashMap<>();
		List<IWrappedInventory> wrappedInventories = BotaniaAPI.internalHandler.wrapInventory(inventories);
		for (IWrappedInventory inv : wrappedInventories) {
			ICorporeaRequest request = new CorporeaRequest(matcher, -1);
			inv.countItems(request);
			if (request.getFound() > 0) {
				countMap.put(inv.getWrappedObject(), request.getFound());
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
		CorporeaRequestEvent event = new CorporeaRequestEvent(matcher, itemCount, spark, !doit);
		if (MinecraftForge.EVENT_BUS.post(event)) {
			return new CorporeaResult(stacks, 0, 0); // todo 1.15 what to do here
		}

		List<InvWithLocation> inventories = getInventoriesOnNetwork(spark);

		List<IWrappedInventory> inventoriesW = BotaniaAPI.internalHandler.wrapInventory(inventories);
		Map<ICorporeaInterceptor, ICorporeaSpark> interceptors = new HashMap<>();

		ICorporeaRequest request = new CorporeaRequest(matcher, itemCount);
		for (IWrappedInventory inv : inventoriesW) {
			ICorporeaSpark invSpark = inv.getSpark();

			InvWithLocation originalInventory = inv.getWrappedObject();
			if (originalInventory.getWorld().getTileEntity(originalInventory.getPos()) instanceof ICorporeaInterceptor) {
				ICorporeaInterceptor interceptor = (ICorporeaInterceptor) originalInventory.getWorld().getTileEntity(originalInventory.getPos());
				interceptor.interceptRequest(matcher, itemCount, invSpark, spark, stacks, inventories, doit);
				interceptors.put(interceptor, invSpark);
			}

			if (doit) {
				stacks.addAll(inv.extractItems(request));
			} else {
				stacks.addAll(inv.countItems(request));
			}
		}

		for (ICorporeaInterceptor interceptor : interceptors.keySet()) {
			interceptor.interceptRequestLast(matcher, itemCount, interceptors.get(interceptor), spark, stacks, inventories, doit);
		}

		return new CorporeaResult(stacks, request.getFound(), request.getExtracted());
	}

	@Override
	public ICorporeaSpark getSparkForInventory(InvWithLocation inv) {
		TileEntity tile = inv.getWorld().getTileEntity(inv.getPos());
		return getSparkForBlock(tile.getWorld(), tile.getPos());
	}

	@Override
	public ICorporeaSpark getSparkForBlock(World world, BlockPos pos) {
		List<Entity> sparks = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up(), pos.add(1, 2, 1)), Predicates.instanceOf(ICorporeaSpark.class));
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
	public <T extends ICorporeaRequestMatcher> void registerRequestMatcher(ResourceLocation id, Class<T> clazz, Function<CompoundNBT, T> deserializer) {
		TileCorporeaRetainer.addCorporeaRequestMatcher(id, clazz, deserializer);
	}

	public void clearCache() {
		cachedNetworks.clear();
	}
}
