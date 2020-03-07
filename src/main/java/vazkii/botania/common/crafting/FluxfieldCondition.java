/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibMisc;

public class FluxfieldCondition implements ICondition {
	public static final ResourceLocation KEY = new ResourceLocation(LibMisc.MOD_ID, "fluxfield_enabled");
	private final boolean value;

	public FluxfieldCondition(boolean value) {
		this.value = value;
	}

	@Override
	public ResourceLocation getID() {
		return KEY;
	}

	@Override
	public boolean test() {
		return ConfigHandler.COMMON.fluxfieldEnabled.get() == value;
	}

	public static final IConditionSerializer<FluxfieldCondition> SERIALIZER = new IConditionSerializer<FluxfieldCondition>() {
		@Override
		public void write(JsonObject json, FluxfieldCondition cond) {
			json.addProperty("value", cond.value);
		}

		@Override
		public FluxfieldCondition read(JsonObject json) {
			return new FluxfieldCondition(json.get("value").getAsBoolean());
		}

		@Override
		public ResourceLocation getID() {
			return KEY;
		}
	};
}
