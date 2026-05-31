package vazkii.botania.common.internal_caps;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.attachment.DataHolderId;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Temporarily attached to minecarts while affected by a spectral rail.
 * 
 * @see vazkii.botania.common.block.SpectralRailBlock
 */
public final class SpectralFloatTicks {

	public static final ResourceLocation ID = botaniaRL("spectral_float_ticks");
	public static final DataHolderId<Integer> HOLDER = new DataHolderId<>(ID, Codec.INT);

	private SpectralFloatTicks() {}
}
