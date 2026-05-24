package vazkii.botania.datafix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import net.minecraft.util.datafix.schemas.V100;

import java.util.Map;
import java.util.function.Supplier;

// Let's hope no other mod chooses this one specifically; We need a unique schema to register new entities since we're
// also datafixing old ones to match the new types.
public class V3807_5 extends NamespacedSchema {

	public V3807_5(int versionKey, Schema parent) {
		super(versionKey, parent);
	}

	@Override
	public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
		Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);

		map.remove("botania:doppleganger");
		schema.register(map, "botania:gaia_guardian", () -> V100.equipment(schema));
		map.remove("botania:magic_landmine");
		schema.registerSimple(map, "botania:gaia_trap");

		map.remove("botania:ender_air_bottle");
		schema.registerSimple(map, "botania:ender_essence_flask");
		map.remove("botania:ender_air");
		schema.registerSimple(map, "botania:pure_ender_essence_cloud");
		schema.registerSimple(map, "botania:diluted_ender_essence_cloud");

		map.remove("botania:pool_minecart");
		schema.register(map, "botania:mana_pool_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));

		map.remove("botania:player_mover");
		schema.registerSimple(map, "botania:luminizer_beam");
		map.remove("botania:mana_storm");
		schema.registerSimple(map, "botania:manastorm_epicenter");
		map.remove("botania:babylon_weapon");
		schema.registerSimple(map, "botania:treasury_weapon");
		map.remove("botania:spark");
		schema.register(map, "botania:mana_spark", name -> DSL.optionalFields("upgrade", References.ITEM_STACK.in(schema)));

		return map;
	}
}
