package vazkii.botania.common.internal_caps;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.internal.ItemSource;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public final class BotaniaDataAttachments {

	/**
	 * Automatically attached to all item entities. Denotes the entity's lifetime in ticks, limited to
	 * {@link Short#MAX_VALUE}.
	 */
	public static final ResourceLocation ITEM_LIFETIME = botaniaRL("item_lifetime");
	/**
	 * When present on an item entity, denotes a particular {@link ItemSource} for the item entity, which may affect how
	 * certain flowers interact with it.
	 */
	public static final ResourceLocation ITEM_SOURCE = botaniaRL("item_source");
	/**
	 * Attached to a player on death. Denotes items kept when respawning.
	 */
	public static final ResourceLocation KEPT_ITEMS = botaniaRL("kept_items");
	/**
	 * Added to mobs when spawned by Loonium. Overrides the mob's loot table with a single (potentially empty) drop.
	 */
	public static final ResourceLocation LOONIUM_DROP = botaniaRL("loonium_drop");
	/**
	 * Added to slimes that spawned naturally in a slime chunk.
	 */
	public static final ResourceLocation SLIME_CHUNK_SPAWNED = botaniaRL("slime_chunk_spawned");
	/**
	 * When present on a mob, that mob will not despawn instantly when far away from all players.
	 * (Random idle despawning still applies.)
	 */
	public static final ResourceLocation SLOW_DESPAWN = botaniaRL("slow_despawn");
	/**
	 * Temporarily attached to minecarts while affected by a spectral rail.
	 */
	public static final ResourceLocation SPECTRAL_FLOAT_TICKS = botaniaRL("spectral_float_ticks");
	/**
	 * Temporarily added to creepers to prevent them from exploding.
	 */
	public static final ResourceLocation TIGERSEYE_PACIFIED = botaniaRL("tigerseye_pacified");
	/**
	 * Added to primed TNT entities when they spawn through TNT duplication.
	 */
	public static final ResourceLocation UNETHICAL_TNT = botaniaRL("unethical_tnt");

	private BotaniaDataAttachments() {}
}
