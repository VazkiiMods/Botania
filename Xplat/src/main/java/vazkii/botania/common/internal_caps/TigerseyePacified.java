package vazkii.botania.common.internal_caps;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.attachment.DataMarkerId;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Added to creepers to prevent them from exploding.
 */
public final class TigerseyePacified {
	public static final ResourceLocation ID = botaniaRL("tigerseye_pacified");
	public static final DataMarkerId MARKER = new DataMarkerId(ID);

	private TigerseyePacified() {}
}
