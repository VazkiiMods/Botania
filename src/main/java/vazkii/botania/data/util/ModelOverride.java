/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.util;

import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;

import java.util.Map;

public class ModelOverride {
	private final Map<Identifier, Double> predicates;
	private final Identifier model;

	public ModelOverride(Map<Identifier, Double> predicates, Identifier model) {
		this.predicates = predicates;
		this.model = model;
	}

	public JsonObject toJson() {
		JsonObject ret = new JsonObject();
		JsonObject preds = new JsonObject();
		predicates.forEach((p, v) -> preds.addProperty(p.toString(), v));
		ret.add("predicate", preds);
		ret.addProperty("model", model.toString());
		return ret;
	}
}
