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
import net.minecraftforge.common.crafting.IConditionSerializer;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;
import java.util.function.BooleanSupplier;

public class FluxfieldConditionFactory implements IConditionSerializer {
	@Nonnull
	@Override
	public BooleanSupplier parse(@Nonnull JsonObject json) {
		boolean value = JsonUtils.getBoolean(json , "value", true);
		return () -> ConfigHandler.fluxfieldEnabled == value;
	}
}
