package vazkii.botania.mixin.datafixers;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Supplier;

// Things broke when trying to mixin to 3938. 3825 is nice in the sense that it just adds a new entity, which is what
// we're doing too.
@Mixin(V3825.class)
public abstract class V3825Mixin {

	@Inject(method = "registerEntities", at = @At("TAIL"))
	private void registerEntities(Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> cir) {
		Map<String, Supplier<TypeTemplate>> map = cir.getReturnValue();

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
	}
}
