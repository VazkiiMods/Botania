/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.lib.BotaniaTags;

public class EntityTagProvider extends EntityTypeTagsProvider {
	public EntityTagProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void addTags() {
		tag(BotaniaTags.Entities.COCOON_COMMON).add(
				EntityType.PIG, EntityType.COW, EntityType.CHICKEN, EntityType.RABBIT, EntityType.SHEEP
		);
		tag(BotaniaTags.Entities.COCOON_RARE)
				.add(EntityType.HORSE, EntityType.DONKEY, EntityType.WOLF, EntityType.OCELOT,
						EntityType.CAT, EntityType.PARROT, EntityType.LLAMA, EntityType.FOX,
						EntityType.PANDA, EntityType.TURTLE, EntityType.GOAT)
				.addOptional(new ResourceLocation("quark", "frog"));

		tag(BotaniaTags.Entities.COCOON_COMMON_AQUATIC)
				.add(EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.SQUID, EntityType.FROG)
				.addOptional(new ResourceLocation("quark", "crab"));
		tag(BotaniaTags.Entities.COCOON_RARE_AQUATIC).add(EntityType.DOLPHIN, EntityType.GLOW_SQUID, EntityType.AXOLOTL);

		tag(BotaniaTags.Entities.SHADED_MESA_BLACKLIST).add(EntityType.ENDER_DRAGON, EntityType.WITHER,
				EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME, EntityType.END_CRYSTAL, EntityType.PAINTING,
				EntityType.COMMAND_BLOCK_MINECART, EntityType.MARKER, EntityType.AREA_EFFECT_CLOUD,
				EntityType.EVOKER_FANGS, EntityType.LEASH_KNOT,
				BotaniaEntities.CORPOREA_SPARK, BotaniaEntities.DOPPLEGANGER, BotaniaEntities.FLAME_RING, BotaniaEntities.MAGIC_LANDMINE,
				BotaniaEntities.MAGIC_MISSILE, BotaniaEntities.MANA_BURST, BotaniaEntities.PINK_WITHER, BotaniaEntities.SPARK, BotaniaEntities.PLAYER_MOVER);

		var bosses = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("c", "bosses"));
		tag(bosses).add(BotaniaEntities.DOPPLEGANGER);
	}
}
