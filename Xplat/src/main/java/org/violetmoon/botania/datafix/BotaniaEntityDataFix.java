package vazkii.botania.datafix;

import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;

import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.EntityRenameFix;
import net.minecraft.util.datafix.fixes.References;

public class BotaniaEntityDataFix extends EntityRenameFix {

	public BotaniaEntityDataFix(Schema outputSchema) {
		super("BotaniaEntityDataFix", outputSchema, true);
	}

	@Override
	protected Pair<String, Typed<?>> fix(String entityName, Typed<?> typed) {
		return switch (entityName) {
			case "botania:doppleganger" -> Pair.of("botania:gaia_guardian", Util.writeAndReadTypedOrThrow(
					typed,
					getOutputSchema().findChoiceType(References.ENTITY).types().get("botania:gaia_guardian"),
					dynamic -> dynamic.renameField("sourcesZ", "sourceZ")
			));
			case "botania:magic_landmine" -> Pair.of("botania:gaia_trap", typed);
			case "botania:spark" -> Pair.of("botania:mana_spark", Util.writeAndReadTypedOrThrow(
					typed,
					getOutputSchema().findChoiceType(References.ENTITY).types().get("botania:mana_spark"),
					dynamic -> {
						int oldValue = dynamic.get("upgrade").asInt(0);
						if (1 <= oldValue && oldValue <= 4) {
							return dynamic.set("upgrade", dynamic.emptyMap().set("id", dynamic.createString(switch (oldValue) {
								case 1 -> "botania:spark_augment_dispersive";
								case 2 -> "botania:spark_augment_dominant";
								case 3 -> "botania:spark_augment_recessive";
								case 4 -> "botania:spark_augment_isolated";
								default -> "";
							})));
						} else {
							return dynamic.remove("upgrade");
						}
					}
			));
			case "botania:ender_air_bottle" -> Pair.of("botania:ender_essence_flask", typed);
			case "botania:player_mover" -> Pair.of("botania:luminizer_beam", typed);
			case "botania:mana_storm" -> Pair.of("botania:manastorm_epicenter", typed);
			case "botania:babylon_weapon" -> Pair.of("botania:treasury_weapon", typed);
			case "botania:ender_air" -> Pair.of("botania:pure_ender_essence_cloud", typed);
			default -> Pair.of(entityName, typed);
		};
	}
}
