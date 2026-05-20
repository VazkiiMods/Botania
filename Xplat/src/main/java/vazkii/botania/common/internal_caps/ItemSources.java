package vazkii.botania.common.internal_caps;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.api.internal.ItemSource;
import vazkii.botania.common.helper.RegistryHelper;

import java.util.function.BiConsumer;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ItemSources {
	public static final Codec<ItemSource> CODEC = Codec.lazyInitialized(
			() -> RegistryHelper.getDefaultedRegistry(BotaniaRegistries.ITEM_SOURCE).byNameCodec());

	public static final ItemSource PETAL_APOTHECARY = new ItemSource(true);
	public static final ItemSource MANA_INFUSION = new ItemSource(true);
	public static final ItemSource RUNIC_ALTAR = new ItemSource(true);
	public static final ItemSource ALFHEIM_PORTAL = new ItemSource(true);
	public static final ItemSource TERRA_PLATE = new ItemSource(false);

	public static void submitRegistrations(BiConsumer<ItemSource, ResourceLocation> r) {
		r.accept(PETAL_APOTHECARY, botaniaRL("petal_apothecary"));
		r.accept(MANA_INFUSION, botaniaRL("mana_infusion"));
		r.accept(RUNIC_ALTAR, botaniaRL("runic_altar"));
		r.accept(ALFHEIM_PORTAL, botaniaRL("alfheim_portal"));
		r.accept(TERRA_PLATE, botaniaRL("terra_plate"));
	}
}
