/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 28, 2015, 11:55:56 AM (GMT)]
 */
package vazkii.botania.common.block.tile.corporea;

import com.google.common.collect.HashBiMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaRequestMatcher;
import vazkii.botania.api.corporea.ICorporeaRequestor;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static vazkii.botania.api.corporea.CorporeaRequestDefaultMatchers.*;

public class TileCorporeaRetainer extends TileMod {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.CORPOREA_RETAINER)
	public static TileEntityType<TileCorporeaRetainer> TYPE;
	private static final String TAG_REQUEST_X = "requestX";
	private static final String TAG_REQUEST_Y = "requestY";
	private static final String TAG_REQUEST_Z = "requestZ";
	private static final String TAG_REQUEST_TYPE = "requestType";
	private static final String TAG_REQUEST_COUNT = "requestCount";

	public static final Map<String, Function<CompoundNBT, ICorporeaRequestMatcher>> corporeaMatcherDeserializers = new HashMap<>();
	public static final Map<Class<?>, String> corporeaMatcherSerializers = new HashMap<>();

	private BlockPos requestPos = BlockPos.ZERO;
	@Nullable
	private ICorporeaRequestMatcher request;
	private int requestCount;
	private int compValue;

	static {
		addCorporeaRequestMatcher("string", CorporeaStringMatcher.class, CorporeaStringMatcher::createFromNBT);
		addCorporeaRequestMatcher("item_stack", CorporeaItemStackMatcher.class, CorporeaItemStackMatcher::createFromNBT);
	}

	public TileCorporeaRetainer() {
		super(TYPE);
	}

	public void setPendingRequest(BlockPos pos, ICorporeaRequestMatcher request, int requestCount) {
		if(hasPendingRequest())
			return;

		this.requestPos = pos;
		this.request = request;
		this.requestCount = requestCount;

		compValue = CorporeaHelper.signalStrengthForRequestSize(requestCount);
		world.updateComparatorOutputLevel(getPos(), world.getBlockState(getPos()).getBlock());
	}
	
	public int getComparatorValue() {
		return compValue;
	}
	
	public boolean hasPendingRequest() {
		return request != null;
	}

	public void fulfilRequest() {
		if(!hasPendingRequest())
			return;

		ICorporeaSpark spark = CorporeaHelper.getSparkForBlock(world, requestPos);
		if(spark != null) {
			InvWithLocation inv = spark.getSparkInventory();
			if(inv != null && inv.world.getTileEntity(inv.pos) instanceof ICorporeaRequestor) {
				ICorporeaRequestor requestor = (ICorporeaRequestor) inv.world.getTileEntity(inv.pos);
				requestor.doCorporeaRequest(request, requestCount, spark);

				request = null;
				requestCount = 0;
				compValue = 0;
				world.updateComparatorOutputLevel(getPos(), world.getBlockState(getPos()).getBlock());
			}
		}
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		super.writePacketNBT(cmp);

		cmp.putInt(TAG_REQUEST_X, requestPos.getX());
		cmp.putInt(TAG_REQUEST_Y, requestPos.getY());
		cmp.putInt(TAG_REQUEST_Z, requestPos.getZ());

		String reqType = request != null ? corporeaMatcherSerializers.getOrDefault(request.getClass(), "") : "";

		cmp.putString(TAG_REQUEST_TYPE, reqType);

		if(reqType.length() > 0)
			request.writeToNBT(cmp);
		cmp.putInt(TAG_REQUEST_COUNT, requestCount);
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		super.readPacketNBT(cmp);

		int x = cmp.getInt(TAG_REQUEST_X);
		int y = cmp.getInt(TAG_REQUEST_Y);
		int z = cmp.getInt(TAG_REQUEST_Z);
		requestPos = new BlockPos(x, y, z);

		String reqType = cmp.getString(TAG_REQUEST_TYPE);
		if(corporeaMatcherDeserializers.containsKey(reqType))
			request = corporeaMatcherDeserializers.get(reqType).apply(cmp);
		else
			request = null;
		requestCount = cmp.getInt(TAG_REQUEST_COUNT);
	}
	
	public static void addCorporeaRequestMatcher(String type, Class<?> clazz, Function<CompoundNBT, ICorporeaRequestMatcher> deserializer) {
		corporeaMatcherSerializers.put(clazz, type);
		corporeaMatcherDeserializers.put(type, deserializer);
	} 

}
