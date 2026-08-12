/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class EntityTagProvider extends IntrinsicHolderTagsProvider<EntityType<?>> {
	public EntityTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Registries.ENTITY_TYPE, lookupProvider,
				entityType -> BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).orElseThrow());
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(BotaniaTags.Entities.COCOON_COMMON).add(
				EntityType.PIG, EntityType.COW, EntityType.CHICKEN, EntityType.RABBIT, EntityType.SHEEP
		);
		tag(BotaniaTags.Entities.COCOON_RARE)
				.add(EntityType.HORSE, EntityType.DONKEY, EntityType.WOLF, EntityType.OCELOT,
						EntityType.CAT, EntityType.PARROT, EntityType.LLAMA, EntityType.FOX,
						EntityType.PANDA, EntityType.TURTLE, EntityType.GOAT, EntityType.CAMEL,
						EntityType.ARMADILLO);

		tag(BotaniaTags.Entities.COCOON_COMMON_AQUATIC)
				.add(EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.SQUID, EntityType.FROG)
				.addOptional(ResourceLocation.fromNamespaceAndPath("quark", "crab"));
		tag(BotaniaTags.Entities.COCOON_RARE_AQUATIC).add(EntityType.DOLPHIN, EntityType.GLOW_SQUID, EntityType.AXOLOTL);

		tag(BotaniaTags.Entities.DRUM_MILKABLE).add(EntityType.COW, EntityType.MOOSHROOM, EntityType.GOAT);
		tag(BotaniaTags.Entities.DRUM_NO_SHEARING).add(EntityType.MOOSHROOM);

		tag(BotaniaTags.Entities.SHADED_MESA_NO_PICKUP).add(EntityType.ENDER_DRAGON, EntityType.WITHER,
				EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME, EntityType.END_CRYSTAL, EntityType.PAINTING,
				EntityType.COMMAND_BLOCK_MINECART, EntityType.MARKER, EntityType.AREA_EFFECT_CLOUD,
				EntityType.EVOKER_FANGS, EntityType.LEASH_KNOT, EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY,
				EntityType.TEXT_DISPLAY, EntityType.INTERACTION, EntityType.MARKER,
				BotaniaEntities.CORPOREA_SPARK, BotaniaEntities.GAIA_GUARDIAN, BotaniaEntities.FLAME_RING, BotaniaEntities.GAIA_TRAP,
				BotaniaEntities.MAGIC_MISSILE, BotaniaEntities.MANA_BURST, BotaniaEntities.PINK_WITHER, BotaniaEntities.SPARK, BotaniaEntities.LUMINIZER_BEAM
		);

		tag(BotaniaTags.Entities.KEY_IMMUNE).add(EntityType.ITEM, EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME,
				EntityType.PAINTING, EntityType.EXPERIENCE_ORB);
		tag(BotaniaTags.Entities.PORTAL_BREAD_IMMUNE).add(EntityType.ITEM, EntityType.EXPERIENCE_ORB);

		tag(BotaniaTags.Entities.ENDER_ESSENCE_CLOUDS)
				.add(BotaniaEntities.DILUTED_ENDER_ESSENCE_CLOUD, BotaniaEntities.PURE_ENDER_ESSENCE_CLOUD);
	}
}
