package vazkii.botania.mixin.datafixers;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;

import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.SpawnData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.datafix.BotaniaDataFixers;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Mixin(ItemStackComponentizationFix.class)
public abstract class ItemStackComponentizationFixMixin {
	@Unique
	private static final Set<String> BOTANIA_RELICS = Set.of(
			"botania:dice",
			"botania:infinite_fruit",
			"botania:king_key",
			"botania:flugel_eye",
			"botania:thor_ring",
			"botania:odin_ring",
			"botania:loki_ring"
	);

	@Unique
	private static void botania_moveBooleanTagToUnitComponent(ItemStackComponentizationFix.ItemStackData stack, String key, String component, Dynamic<?> tag) {
		if (stack.removeTag(key).asBoolean(false)) {
			stack.setComponent(component, tag.emptyMap());
		}
	}

	@Inject(method = "fixItemStack", at = @At("TAIL"))
	private static void fixItemStack(ItemStackComponentizationFix.ItemStackData stack, Dynamic<?> tag, CallbackInfo ci) {
		botania_moveBooleanTagToUnitComponent(stack, "phantomInk", "botania_phantom_inked", tag);
		botania_moveBooleanTagToUnitComponent(stack, "Botania_keepIvy", "resolute_ivy", tag);

		if (stack.is(Set.of("botania:bauble_box", "botania:flower_bag"))) {
			List<Dynamic<?>> contents = new ArrayList<>();
			List<Dynamic<?>> oldContents = tag.get("Items").asList(dynamic -> dynamic);
			List<DyeColor> newColorOrder = ColorHelper.supportedColors().toList();
			for (int i = 0; i < oldContents.size(); i++) {
				int slot = stack.is("botania:flower_bag")
						? (i - (i % 16)) + newColorOrder.indexOf(DyeColor.byId(i % 16))
						: i;
				contents.add(tag.emptyMap()
						.set("item", oldContents.get(i))
						.set("slot", tag.createInt(slot)));
			}
			stack.setComponent("minecraft:container", tag.createList(contents.stream()));
			stack.removeTag("Items");
		}

		if (stack.is(BOTANIA_RELICS)) {
			stack.removeTag("soulbindUUID").asString().ifSuccess(uuid -> {
				stack.setComponent(
						"botania:soulbound",
						new Dynamic<>(NbtOps.INSTANCE, NbtUtils.createUUID(UUID.fromString(uuid)))
				);
			});
		}

		if (stack.is("botania:lexicon")) {
			botania_moveBooleanTagToUnitComponent(stack, "botania:elven_unlock", "botania:elven_unlock", tag);
		}

		if (stack.is(Set.of("botania:twig_wand", "botania:dreamwood_wand"))) {
			stack.setComponent("botania:wand_color1",
					tag.createString(DyeColor.byId(stack.removeTag("color1").asInt(0)).getSerializedName()));
			stack.setComponent("botania:wand_color2",
					tag.createString(DyeColor.byId(stack.removeTag("color2").asInt(0)).getSerializedName()));
			botania_moveBooleanTagToUnitComponent(stack, "bindMode", "botania:wand_bind_mode", tag);
			stack.removeTag("boundTileX");
			stack.removeTag("boundTileY");
			stack.removeTag("boundTileZ");
		}

		if (stack.is("botania:tornado_rod")) {
			stack.removeTag("flying");
			stack.moveTagToComponent("flyCounter", "botania:remaining_ticks");
		}

		if (stack.is("botania:exchange_rod")) {
			for (String oldTag : Set.of("targetBlock", "swapping", "selectX", "selectY", "selectZ", "extraRange", "swapHitVec", "swapDirection", "temperanceStone")) {
				stack.removeTag(oldTag);
			}
			stack.moveTagToComponent("placedItem", "botania:placed_item");
		}

		if (stack.is("botania:gravity_rod")) {
			stack.moveTagToComponent("ticksTillExpire", "botania:remaining_ticks");
			stack.removeTag("ticksCooldown");
			stack.moveTagToComponent("target", "botania:target_entity");
			stack.removeTag("dist").get().ifSuccess(dyn -> stack.setComponent("botania:target_dist", tag.createFloat((float) dyn.asDouble(0.0)))
			);
		}

		if (stack.is("botania:terra_pick")) {
			botania_moveBooleanTagToUnitComponent(stack, "enabled", "botania:active", tag);
			botania_moveBooleanTagToUnitComponent(stack, "tipped", "botania:elementium_tipped", tag);
			stack.moveTagToComponent("mana", "botania:mana");
		}

		if (stack.is("botania:glass_pickaxe")) {
			// If you managed to save your 1.20- world the same tick as mining a block with the vitreous pickaxe,
			// and join it in 1.21+, congrats on your free silk touch enchant.
			// I'm not gonna bother going all the way into the enchant tag to remove it.
			stack.removeTag("botania:silk_hack");
		}

		if (stack.is("botania:mana_tablet")) {
			stack.moveTagToComponent("mana", "botania:mana");
			botania_moveBooleanTagToUnitComponent(stack, "creative", "botania:creative_mana", tag);
			if (stack.removeTag("oneUse").asBoolean(false) || stack.hasComponent("botania:creative_mana")) {
				stack.setComponent("!botania:can_receive_mana_from_item", tag.emptyMap());
				stack.setComponent("!botania:can_receive_mana_from_pool", tag.emptyMap());
			}
		}

		if (stack.is("botania:mana_mirror")) {
			stack.moveTagToComponent("mana", "botania:mana");
			stack.moveTagToComponent("maxMana", "botania:max_mana");
			stack.moveTagToComponent("manaBacklog", "botania:mana_backlog");
			stack.moveTagToComponent("pos", "botania:mana_pool_pos");
		}

		if (stack.is("botania:mana_gun")) {
			Dynamic<?> lens = stack.removeTag("lens").orElseEmptyMap();
			if (!lens.equals(tag.emptyMap())) {
				stack.setComponent("botania:attached_lens", lens);
			} else {
				stack.setComponent("botania:attached_lenses", tag.createList(
						IntStream.range(0, 6)
								.mapToObj(i -> {
									OptionalDynamic<?> item = stack.removeTag("lens" + i);
									if (item.get("id").asString("minecraft:air").equals("minecraft:air")) {
										return tag.emptyMap();
									}
									return item.orElseEmptyMap();
								})
				));
			}
			botania_moveBooleanTagToUnitComponent(stack, "clip", "botania:lens_clip", tag);
			stack.moveTagToComponent("clipPos", "botania:lens_clip_pos");
			int cooldown = stack.removeTag("cooldown").asInt(0);
			if (cooldown > 0) {
				stack.setComponent("botania:cooldown", tag.createInt(cooldown));
			}
		}

		if (stack.is("botania:spawner_mover")) {
			OptionalDynamic<?> beData = stack.removeTag("spawner");
			stack.setComponent("minecraft:block_entity_data", beData);
			stack.setComponent("botania:block_type", tag.createString("minecraft:spawner"));
			beData.get("SpawnData").get()
					.flatMap(SpawnData.CODEC::parse)
					.map(SpawnData::entityToSpawn)
					.flatMap(NbtOps.INSTANCE::getMap)
					.map(map -> map.get("id"))
					.flatMap(NbtOps.INSTANCE::getStringValue)
					.ifSuccess(entity -> {
						stack.setComponent("botania:mob_type", tag.createString(entity));
					});
		}

		if (stack.is("botania:crafting_halo") || stack.is("botania:auto_crafting_halo")) {
			stack.removeTag("equipped");
			stack.removeTag("rotationBase");
			stack.moveTagToComponent("lastCrafting", "botania:last_recipe_id");
			stack.setComponent("botania:stored_recipes", tag.createList(IntStream.range(1, 12).mapToObj(i -> stack.removeTag("storedRecipe" + i).map(recipe -> tag.emptyMap()
					.set("index", tag.createInt(i))
					.set("value", recipe)
			)
			).mapMulti(DataResult::ifSuccess)));

			if (stack.is("botania:auto_crafting_halo")) {
				if (!stack.removeTag("active").asBoolean(true)) {
					stack.setComponent("!botania:active", tag.emptyMap());
				}
			}
		}

		if (stack.is("botania:black_hole_talisman")) {
			botania_moveBooleanTagToUnitComponent(stack, "active", "botania:active", tag);
			stack.moveTagToComponent("blockName", "botania:block_type");
			stack.moveTagToComponent("blockCount", "botania:block_count");
		}

		if (stack.is("botania:temperance_stone")) {
			botania_moveBooleanTagToUnitComponent(stack, "active", "botania:active", tag);
		}

		if (stack.is("botania:cacophonium")) {
			stack.moveTagToComponent("sound", "minecraft:note_block_sound");
			stack.removeTag("soundName").get().ifSuccess(sound -> {
				String[] segments = sound.asString("").split("\\.");
				if (segments.length == 3) {
					stack.setComponent("botania:mob_type", tag.createString(segments[1] + ":" + segments[2]));
				}
			});
		}

		if (stack.is("botania:sextant")) {
			stack.moveTagToComponent("mode", "botania:sextant_mode");
			stack.removeTag("sourceX");
			stack.removeTag("sourceY");
			stack.removeTag("sourceZ");
		}

		if (stack.is("botania:astrolabe")) {
			stack.removeTag("blockstate").get("Name").get().ifSuccess(block -> stack.setComponent("botania:block_type", block)
			);
			stack.moveTagToComponent("size", "botania:size");
		}

		if (stack.is("botania:mana_ring") || stack.is("botania:mana_ring_greater")) {
			stack.moveTagToComponent("mana", "botania:mana");
		}

		if (stack.is("botania:magnet_ring") || stack.is("botania:magnet_ring_greater")) {
			stack.removeTag("cooldown");
		}

		if (stack.is("botania:dodge_ring")) {
			stack.removeTag("dodgeCooldown");
		}

		if (stack.is("botania:speed_up_belt")) {
			stack.removeTag("speed");
			stack.removeTag("oldX");
			stack.removeTag("oldY");
			stack.removeTag("oldZ");
		}

		if (stack.is("botania:itemfinder")) {
			stack.removeTag("highlightPositionsEnt");
			stack.removeTag("highlightPositionsBlock");
		}

		if (stack.is("botania:flight_tiara")) {
			stack.removeTag("flying");
			stack.removeTag("gliding");
			stack.removeTag("boostPending");
			stack.removeTag("isSprinting");
			stack.removeTag("dashCooldown");
			stack.moveTagToComponent("variant", "botania:tiara_variant");
			stack.moveTagToComponent("timeLeft", "botania:remaining_ticks");
			botania_moveBooleanTagToUnitComponent(stack, "infiniteFlight", "botania:creative_flight", tag);
		}

		if (stack.is("botania:mana_bottle")) {
			stack.moveTagToComponent("swigsLeft", "botania:remaining_uses");
			stack.moveTagToComponent("seed", "botania:random_seed");
		}

		if (stack.is("botania:laputa_shard")) {
			stack.moveTagToComponent("level", "botania:shard_level");
			stack.moveTagToComponent("_pointy", "botania:shard_pointy");
			stack.moveTagToComponent("_heightscale", "botania:shard_height_scale");
			Map<Dynamic<?>, Dynamic<?>> laputaState = new HashMap<>();
			stack.removeTag("_state").get().ifSuccess(state -> laputaState.put(tag.createString("block_state"), state));
			stack.removeTag("_tile").get().ifSuccess(tile -> laputaState.put(tag.createString("block_entity_data"), tile));
			stack.removeTag("_yStart").get().ifSuccess(yStart -> laputaState.put(tag.createString("y_start"), yStart));
			stack.removeTag("iterationI").get().ifSuccess(iterationI -> laputaState.put(tag.createString("iteration_i"), iterationI));
			stack.removeTag("iterationJ").get().ifSuccess(iterationJ -> laputaState.put(tag.createString("iteration_j"), iterationJ));
			stack.removeTag("iterationK").get().ifSuccess(iterationK -> laputaState.put(tag.createString("iteration_k"), iterationK));
			List<Dynamic<?>> nextPos = new ArrayList<>();
			stack.removeTag("_x").get().ifSuccess(nextPos::add);
			stack.removeTag("_y").get().ifSuccess(nextPos::add);
			stack.removeTag("_z").get().ifSuccess(nextPos::add);
			if (!nextPos.equals(tag.emptyMap())) {
				laputaState.put(tag.createString("next_pos"), tag.createList(nextPos.stream()));
			}
			if (!laputaState.equals(tag.emptyMap())) {
				stack.setComponent("botania:laputa_state", tag.createMap(laputaState));
			}
		}

		if (stack.is("botania:king_key")) {
			stack.removeTag("weaponsSpawned");
			stack.removeTag("charging");
		}

		if (stack.is("botania:flugel_eye")) {
			Dynamic<?> boundPositions = tag.emptyMap();
			Map<String, Dynamic<?>> tags = stack.tag
					.asMap(k -> k.asString(""), v -> v);
			for (Map.Entry<String, Dynamic<?>> entry : tags.entrySet()) {
				String key = entry.getKey();
				if (key.startsWith("target_")) {
					stack.removeTag(key);
					boundPositions.set(key.substring("target_".length()), entry.getValue());
				}
			}
			stack.setComponent("botania:bound_positions", boundPositions);
		}

		if (stack.is("botania:loki_ring")) {
			stack.removeTag("xOrigin");
			stack.removeTag("yOrigin");
			stack.removeTag("zOrigin");
			Dynamic<?> cursorList = stack.removeTag("cursorList").orElseEmptyList();
			Dynamic<?> lokiOffsetList = tag.createList(
					IntStream.range(0, cursorList.get("cursorCount").asInt(0)).mapToObj(n -> {
						OptionalDynamic<?> pos = cursorList.get("cursor" + n);
						return pos.get("xOffset").get().apply3(
								(x, y, z) -> tag.createList(Stream.of(x, y, z)),
								pos.get("yOffset").get(),
								pos.get("zOffset").get()
						).getOrThrow();
					})
			);
			stack.setComponent("botania:ring_of_loki_offset_list", lokiOffsetList);
		}

		if (stack.is(Set.of("botania:brew_vial", "botania:brew_flask"))) {
			stack.moveTagToComponent("swigsLeft", "botania:remaining_uses");
		}

		if (stack.is(Set.of("botania:brew_vial", "botania:brew_flask", "botania:incense_stick", "botania:blood_pendant"))) {
			stack.removeTag("brewKey").get()
					.flatMap(Dynamic::asString)
					.map(brew -> {
						String newName = BotaniaDataFixers.RENAMED_BREWS.get(brew);
						return newName == null ? brew : newName;
					})
					.ifSuccess(name -> stack.setComponent("botania:brew", tag.createString(name)));
		}
	}
}
