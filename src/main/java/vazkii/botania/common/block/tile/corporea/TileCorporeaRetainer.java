/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.corporea;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

	private static final Map<ResourceLocation, Function<CompoundNBT, ? extends ICorporeaRequestMatcher>> corporeaMatcherDeserializers = new ConcurrentHashMap<>();
	private static final Map<Class<? extends ICorporeaRequestMatcher>, ResourceLocation> corporeaMatcherSerializers = new ConcurrentHashMap<>();

	private BlockPos requestPos = BlockPos.ZERO;

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
			TileEntity te = spark.getSparkNode().getWorld().getTileEntity(spark.getSparkNode().getPos());
			if (te instanceof ICorporeaRequestor) {
				ICorporeaRequestor requestor = (ICorporeaRequestor) te;
				requestor.doCorporeaRequest(request, requestCount, spark);

				request = null;
				requestCount = 0;
				compValue = 0;
				world.updateComparatorOutputLevel(getPos(), getBlockState().getBlock());
			}
		}
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		super.writePacketNBT(cmp);

		cmp.putInt(TAG_REQUEST_X, requestPos.getX());
		cmp.putInt(TAG_REQUEST_Y, requestPos.getY());
		cmp.putInt(TAG_REQUEST_Z, requestPos.getZ());

		ResourceLocation reqType = request != null ? corporeaMatcherSerializers.get(request.getClass()) : null;

		if (reqType != null) {
			cmp.putString(TAG_REQUEST_TYPE, reqType.toString());
			request.writeToNBT(cmp);
			cmp.putInt(TAG_REQUEST_COUNT, requestCount);
		}
		cmp.putBoolean(TAG_RETAIN_MISSING, retainMissing);
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		super.readPacketNBT(cmp);

		int x = cmp.getInt(TAG_REQUEST_X);
		int y = cmp.getInt(TAG_REQUEST_Y);
		int z = cmp.getInt(TAG_REQUEST_Z);
		requestPos = new BlockPos(x, y, z);

		ResourceLocation reqType = ResourceLocation.tryCreate(cmp.getString(TAG_REQUEST_TYPE));
		if (reqType != null && corporeaMatcherDeserializers.containsKey(reqType)) {
			request = corporeaMatcherDeserializers.get(reqType).apply(cmp);
		} else {
			request = null;
		}
		requestCount = cmp.getInt(TAG_REQUEST_COUNT);
		retainMissing = cmp.getBoolean(TAG_RETAIN_MISSING);
	}

	public static <T extends ICorporeaRequestMatcher> void addCorporeaRequestMatcher(ResourceLocation id, Class<T> clazz, Function<CompoundNBT, T> deserializer) {
		corporeaMatcherSerializers.put(clazz, id);
		corporeaMatcherDeserializers.put(id, deserializer);
	}

	@OnlyIn(Dist.CLIENT)
	public void renderHUD(MatrixStack ms, Minecraft mc) {
		String mode = I18n.format("botaniamisc.retainer." + (retainMissing ? "retain_missing" : "retain_all"));
		int x = mc.getMainWindow().getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(mode) / 2;
		int y = mc.getMainWindow().getScaledHeight() / 2 + 10;

		mc.fontRenderer.drawStringWithShadow(ms, mode, x, y, TextFormatting.GRAY.getColor());
	}

	public boolean onUsedByWand() {
		if (!world.isRemote) {
			retainMissing = !retainMissing;
			markDirty();
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
		return true;
	}
}
