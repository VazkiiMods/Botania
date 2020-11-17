/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.EntityTypeTagsProvider;
import net.minecraft.entity.EntityType;

import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.lib.ModTags;

public class EntityTagProvider extends EntityTypeTagsProvider {
	public EntityTagProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void configure() {
		getOrCreateTagBuilder(ModTags.Entities.COCOON_COMMON).add(
				EntityType.PIG, EntityType.COW, EntityType.CHICKEN, EntityType.RABBIT, EntityType.SHEEP
		);
		getOrCreateTagBuilder(ModTags.Entities.COCOON_RARE)
				.add(EntityType.HORSE, EntityType.DONKEY, EntityType.WOLF, EntityType.OCELOT,
						EntityType.CAT, EntityType.PARROT, EntityType.LLAMA, EntityType.FOX,
						EntityType.PANDA, EntityType.TURTLE)
				.add(quark("frog"));

		getOrCreateTagBuilder(ModTags.Entities.COCOON_COMMON_AQUATIC)
				.add(EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.SQUID)
				.add(quark("crab"));
		getOrCreateTagBuilder(ModTags.Entities.COCOON_RARE_AQUATIC).add(EntityType.DOLPHIN);

		getOrCreateTagBuilder(ModTags.Entities.SHADED_MESA_BLACKLIST).add(EntityType.ENDER_DRAGON, EntityType.WITHER,
				EntityType.ITEM_FRAME, EntityType.END_CRYSTAL, EntityType.PAINTING, ModEntities.CORPOREA_SPARK,
				ModEntities.DOPPLEGANGER, ModEntities.FLAME_RING, ModEntities.MAGIC_LANDMINE, ModEntities.MAGIC_MISSILE,
				ModEntities.MANA_BURST, ModEntities.PINK_WITHER, ModEntities.SPARK, ModEntities.PLAYER_MOVER);
	}

	private static Tag.OptionalObjectEntry quark(String path) {
		return new Tag.OptionalObjectEntry(new Identifier("quark", path));
	}
}
