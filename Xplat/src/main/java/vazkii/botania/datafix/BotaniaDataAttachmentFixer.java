package vazkii.botania.datafix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;

import net.minecraft.util.datafix.fixes.References;

import vazkii.botania.xplat.XplatAbstractions;

import java.util.Map;

public class BotaniaDataAttachmentFixer extends DataFix {
	public BotaniaDataAttachmentFixer(Schema outputSchema) {
		super(outputSchema, false);
	}

	@Override
	protected TypeRewriteRule makeRule() {
		Type<?> playerType = getInputSchema().getType(References.PLAYER);
		Type<?> entityType = getInputSchema().getType(References.ENTITY);
		return TypeRewriteRule.seq(
				fixTypeEverywhereTyped("BotaniaDataAttachmentFixer: Player", playerType, this::fixTyped),
				fixTypeEverywhereTyped("BotaniaDataAttachmentFixer: Entities", entityType, this::fixTyped)
		);
	}

	private Typed<?> fixTyped(Typed<?> typed) {
		return typed.update(DSL.remainderFinder(), this::fixAttachments);
	}

	private Dynamic<?> fixAttachments(Dynamic<?> dynamic) {
		String oldLocation = XplatAbstractions.instance().isFabric() ? "cardinal_components" : "ForgeCaps";
		String newLocation = XplatAbstractions.instance().isFabric() ? "fabric:attachments" : "neoforge:attachments";
		Dynamic<?> components = dynamic.get(oldLocation).orElseEmptyMap();
		Map<String, Dynamic<?>> componentsMap = components.asMap(
				key -> key.asString(""),
				value -> value
		);
		Dynamic<?> newAttachments = dynamic.get(newLocation).orElseEmptyMap();
		for (Map.Entry<String, Dynamic<?>> entry : componentsMap.entrySet()) {
			if (entry.getKey().startsWith("botania:")) {
				components = components.remove(entry.getKey());
				switch (entry.getKey()) {
					case "botania:kept_items":
						newAttachments = newAttachments.set(entry.getKey(), entry.getValue().get("stacks").orElseEmptyList());
						break;
					case "botania:iitem":
						Dynamic<?> tags = entry.getValue();
						if (tags.get("ElvenPortalSpawned").asBoolean(false) ||
								tags.get("_elvenPortal").asBoolean(false)) {
							newAttachments = newAttachments.set("botania:item_source", tags.createString("botania:alfheim_portal"));
						} else if (tags.get("ApothecarySpawned").asBoolean(false)) {
							newAttachments = newAttachments.set("botania:item_source", tags.createString("botania:petal_apothecary"));
						} else if (tags.get("ManaInfusionSpawned").asBoolean(false) ||
								tags.get("manaInfusionCooldown").asInt(0) > 0) {
							newAttachments = newAttachments.set("botania:item_source", tags.createString("botania:mana_infusion"));
						} else if (tags.get("RunicAltarSpawned").asBoolean(false) ||
								tags.get("runicAltarCooldown").asInt(0) > 0) {
							newAttachments = newAttachments.set("botania:item_source", tags.createString("botania:runic_altar"));
						}
						DataResult<? extends Dynamic<?>> timeCounter = tags.get("timeCounter").get();
						if (timeCounter.isSuccess()) {
							newAttachments = newAttachments.set("botania:item_lifetime", timeCounter.getOrThrow());
						}
						break;
					case "botania:loonium_drop":
						if (entry.getValue().get("overrideDrop").asBoolean(false)) {
							if (entry.getValue().get("toDrop").get().isSuccess()) {
								newAttachments = newAttachments.set("botania:loonium_drop", entry.getValue().get("toDrop").get().getOrThrow());
							}
						}
						if (entry.getValue().get("slowDespawn").asBoolean(false)) {
							newAttachments = newAttachments.set("botania:slow_despawn", dynamic.emptyMap());
						}
						break;
					case "botania:ghost_rail":
						DataResult<? extends Dynamic<?>> floatTicks = entry.getValue().get("botania:float_ticks").get();
						if (floatTicks.isSuccess() && floatTicks.getOrThrow().asInt(0) > 0) {
							newAttachments = newAttachments.set("botania:spectral_float_ticks", floatTicks.getOrThrow());
						}
						break;
					case "botania:narslimmus":
						if (entry.getValue().get("botania:world_spawned").asBoolean(false)) {
							newAttachments = newAttachments.set("botania:slime_chunk_spawned", dynamic.emptyMap());
						}
						break;
					case "botania:tnt_ethical":
						if (entry.getValue().get("botania:unethical").asBoolean(true)) {
							newAttachments = newAttachments.set("botania:unethical_tnt", dynamic.emptyMap());
						}
						break;
					case "botania:tigerseye_pacified":
						if (entry.getValue().get("botania:tigerseye_pacified").asBoolean(true)) {
							newAttachments = newAttachments.set("botania:tigerseye_pacified", dynamic.emptyMap());
						}
						break;
				}
			}
		}

		return dynamic
				.set(oldLocation, components)
				.set(newLocation, newAttachments);
	}
}
