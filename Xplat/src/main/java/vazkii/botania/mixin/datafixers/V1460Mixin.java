package vazkii.botania.mixin.datafixers;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.fixes.WriteAndReadFix;
import net.minecraft.util.datafix.schemas.V1460;
import net.minecraft.util.datafix.schemas.V99;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * This mixin serves the same purpose as vanilla's {@link V99} class,
 * meaning that this is the "beginning", where we register all entities and block entities that exist in botania.
 * That way, DFU knows how to update for example item stacks in BE data. If a block entity changes format in a
 * significant way, we'd update it in the relevant schema, as well as registering a
 * {@link WriteAndReadFix} or similar.
 * <p>
 * V1460 is the schema after the flattening in 1.13, which re-registers a bunch of entity types and such. It seemed like
 * a fitting injection point, also used by other DFU-supporting mods.
 * <p>
 * The dream would of course be to register our own version format that gets stored with worlds, so we can run datafixes
 * completely independent of minecraft and other mods.
 */
@Mixin(V1460.class)
public abstract class V1460Mixin {

	@Shadow
	protected static void registerInventory(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	@Shadow
	protected static void registerMob(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	@Inject(method = "registerEntities", at = @At("TAIL"))
	private void registerEntities(Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> cir) {
		Map<String, Supplier<TypeTemplate>> map = cir.getReturnValue();
		schema.registerSimple(map, "botania:babylon_weapon");
		schema.registerSimple(map, "botania:corporea_spark");
		registerMob(schema, map, "botania:doppleganger");
		schema.registerSimple(map, "botania:ender_air");
		schema.registerSimple(map, "botania:ender_air_bottle");
		schema.registerSimple(map, "botania:falling_star");
		schema.registerSimple(map, "botania:flame_ring");
		schema.registerSimple(map, "botania:magic_landmine");
		schema.registerSimple(map, "botania:magic_missile");
		schema.register(map, "botania:mana_burst", name -> DSL.optionalFields("lensStack", References.ITEM_STACK.in(schema)));
		schema.registerSimple(map, "botania:mana_storm");
		registerMob(schema, map, "botania:pink_wither");
		registerMob(schema, map, "botania:pixie");
		schema.registerSimple(map, "botania:player_mover");
		schema.register(map, "botania:pool_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
		schema.registerSimple(map, "botania:spark");
		schema.register(map, "botania:thorn_chakram", name -> DSL.optionalFields("fly_stack", References.ITEM_STACK.in(schema)));
		schema.register(map, "botania:thrown_item", name -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
		schema.registerSimple(map, "botania:vine_ball");
	}

	@Inject(method = "registerBlockEntities", at = @At("TAIL"))
	private void registerBlockEntities(Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> cir) {
		Map<String, Supplier<TypeTemplate>> map = cir.getReturnValue();
		schema.register(map, "botania:corporea_crystal_cube",
				() -> DSL.optionalFields("requestTarget", References.ITEM_STACK.in(schema))
		);
		schema.register(map, "botania:cacophonium",
				() -> DSL.optionalFields("stack", References.ITEM_STACK.in(schema))
		);
		schema.register(map, "botania:enchanter",
				() -> DSL.optionalFields("item", References.ITEM_STACK.in(schema))
		);
		schema.register(map, "botania:platform",
				() -> DSL.optionalFields("camo", References.BLOCK_STATE.in(schema))
		);
		schema.register(map, "botania:gourmaryllis",
				() -> DSL.optionalFields("lastFoods", DSL.list(References.ITEM_STACK.in(schema)))
		);
		ImmutableSet.of(
				"botania:avatar",
				"botania:brewery",
				"botania:crafty_crate",
				"botania:hourglass",
				"botania:incense_plate",
				"botania:prism",
				"botania:mana_spreader",
				"botania:open_crate",
				"botania:altar",
				"botania:runic_altar",
				"botania:spark_changer",
				"botania:tiny_potato"
		).forEach(name -> registerInventory(schema, map, name));

		schema.register(map, "botania:alfheim_portal", () ->
		// Surely nobody has more than 101 stacks in their alfheim portal, right?
		// I'll actually be surprised if a single person has a single stack they remember + care about
		// Some automated setups might be mid-craft i guess.
		DSL.allWithRemainder(
				DSL.optional(DSL.field("portalStack0", References.ITEM_STACK.in(schema))),
				IntStream.range(0, 100).boxed().map(i -> DSL.optional(DSL.field("portalStack" + i, References.ITEM_STACK.in(schema)))
				).toArray(TypeTemplate[]::new)
		)
		);
	}
}
