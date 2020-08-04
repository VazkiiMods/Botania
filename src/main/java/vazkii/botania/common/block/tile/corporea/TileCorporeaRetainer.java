/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.corporea;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import vazkii.botania.api.corporea.*;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
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
	private static final String TAG_RETAIN_MISSING = "retainMissing";

	private static final Map<Identifier, Function<CompoundTag, ? extends ICorporeaRequestMatcher>> corporeaMatcherDeserializers = new ConcurrentHashMap<>();
	private static final Map<Class<? extends ICorporeaRequestMatcher>, Identifier> corporeaMatcherSerializers = new ConcurrentHashMap<>();

	private BlockPos requestPos = BlockPos.ORIGIN;

	@Nullable
	private ICorporeaRequestMatcher request;
	private int requestCount;
	private int compValue;
	private boolean retainMissing = false;

	public TileCorporeaRetainer() {
		super(ModTiles.CORPOREA_RETAINER);
	}

	public void remember(BlockPos pos, ICorporeaRequestMatcher request, int count, int missing) {
		if (hasPendingRequest()) {
			return;
		}

		this.requestPos = pos;
		this.request = request;
		this.requestCount = retainMissing ? missing : count;

		compValue = CorporeaHelper.instance().signalStrengthForRequestSize(requestCount);
		markDirty();
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
			BlockEntity te = spark.getSparkNode().getWorld().getBlockEntity(spark.getSparkNode().getPos());
			if (te instanceof ICorporeaRequestor) {
				ICorporeaRequestor requestor = (ICorporeaRequestor) te;
				requestor.doCorporeaRequest(request, requestCount, spark);

				request = null;
				requestCount = 0;
				compValue = 0;
				markDirty();
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
		cmp.putBoolean(TAG_RETAIN_MISSING, retainMissing);
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
		retainMissing = cmp.getBoolean(TAG_RETAIN_MISSING);
	}

	public static <T extends ICorporeaRequestMatcher> void addCorporeaRequestMatcher(Identifier id, Class<T> clazz, Function<CompoundTag, T> deserializer) {
		corporeaMatcherSerializers.put(clazz, id);
		corporeaMatcherDeserializers.put(id, deserializer);
	}

	@Environment(EnvType.CLIENT)
	public void renderHUD(MatrixStack ms, MinecraftClient mc) {
		String mode = I18n.translate("botaniamisc.retainer." + (retainMissing ? "retain_missing" : "retain_all"));
		int x = mc.getWindow().getScaledWidth() / 2 - mc.textRenderer.getWidth(mode) / 2;
		int y = mc.getWindow().getScaledHeight() / 2 + 10;

		mc.textRenderer.drawWithShadow(ms, mode, x, y, Formatting.GRAY.getColorValue());
	}

	public boolean onUsedByWand() {
		if (!world.isClient) {
			retainMissing = !retainMissing;
			markDirty();
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
		return true;
	}
}
