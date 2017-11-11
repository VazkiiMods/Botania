/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.function.BooleanSupplier;

public class DarkQuartzConditionFactory implements IConditionFactory {
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		boolean value = JsonUtils.getBoolean(json , "value", true);
		return () -> ConfigHandler.darkQuartzEnabled == value;
	}
}
