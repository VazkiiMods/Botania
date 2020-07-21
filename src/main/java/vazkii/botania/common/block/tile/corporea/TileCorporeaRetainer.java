/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.corporea;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import vazkii.botania.api.corporea.*;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;

import javax.annotation.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class TileCorporeaRetainer extends TileMod {
	private static final String TAG_REQUEST_X = "requestX";
	private static final String TAG_REQUEST_Y = "requestY";
	private static final String TAG_REQUEST_Z = "requestZ";
	private static final String TAG_REQUEST_TYPE = "requestType";
	private static final String TAG_REQUEST_COUNT = "requestCount";

	private static final Map<Identifier, Function<CompoundTag, ? extends ICorporeaRequestMatcher>> corporeaMatcherDeserializers = new ConcurrentHashMap<>();
	private static final Map<Class<? extends ICorporeaRequestMatcher>, Identifier> corporeaMatcherSerializers = new ConcurrentHashMap<>();

	private BlockPos requestPos = BlockPos.ORIGIN;
	@Nullable
	private ICorporeaRequestMatcher request;
	private int requestCount;
	private int compValue;

	public TileCorporeaRetainer() {
		super(ModTiles.CORPOREA_RETAINER);
	}

	public void setPendingRequest(BlockPos pos, ICorporeaRequestMatcher request, int requestCount) {
		if (hasPendingRequest()) {
			return;
		}

		this.requestPos = pos;
		this.request = request;
		this.requestCount = requestCount;

		compValue = CorporeaHelper.instance().signalStrengthForRequestSize(requestCount);
		world.updateComparators(getPos(), getCachedState().getBlock());
	}

	public int getComparatorValue() {
		return compValue;
	}

	public boolean hasPendingRequest() {
		return request != null;
	}

	public void fulfilRequest() {
		if (!hasPendingRequest()) {
			return;
		}

		ICorporeaSpark spark = CorporeaHelper.instance().getSparkForBlock(world, requestPos);
		if (spark != null) {
			InvWithLocation inv = spark.getSparkInventory();
			if (inv != null && inv.getWorld().getBlockEntity(inv.getPos()) instanceof ICorporeaRequestor) {
				ICorporeaRequestor requestor = (ICorporeaRequestor) inv.getWorld().getBlockEntity(inv.getPos());
				requestor.doCorporeaRequest(request, requestCount, spark);

				request = null;
				requestCount = 0;
				compValue = 0;
				world.updateComparators(getPos(), getCachedState().getBlock());
			}
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		super.writePacketNBT(cmp);

		cmp.putInt(TAG_REQUEST_X, requestPos.getX());
		cmp.putInt(TAG_REQUEST_Y, requestPos.getY());
		cmp.putInt(TAG_REQUEST_Z, requestPos.getZ());

		Identifier reqType = request != null ? corporeaMatcherSerializers.get(request.getClass()) : null;

		if (reqType != null) {
			cmp.putString(TAG_REQUEST_TYPE, reqType.toString());
			request.writeToNBT(cmp);
			cmp.putInt(TAG_REQUEST_COUNT, requestCount);
		}
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		super.readPacketNBT(cmp);

		int x = cmp.getInt(TAG_REQUEST_X);
		int y = cmp.getInt(TAG_REQUEST_Y);
		int z = cmp.getInt(TAG_REQUEST_Z);
		requestPos = new BlockPos(x, y, z);

		Identifier reqType = Identifier.tryParse(cmp.getString(TAG_REQUEST_TYPE));
		if (reqType != null && corporeaMatcherDeserializers.containsKey(reqType)) {
			request = corporeaMatcherDeserializers.get(reqType).apply(cmp);
		} else {
			request = null;
		}
		requestCount = cmp.getInt(TAG_REQUEST_COUNT);
	}

	public static <T extends ICorporeaRequestMatcher> void addCorporeaRequestMatcher(Identifier id, Class<T> clazz, Function<CompoundTag, T> deserializer) {
		corporeaMatcherSerializers.put(clazz, id);
		corporeaMatcherDeserializers.put(id, deserializer);
	}

}
