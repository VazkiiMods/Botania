package vazkii.botania.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import vazkii.botania.common.Botania;

import java.util.function.BooleanSupplier;

public class GardenOfGlassConditionFactory implements IConditionFactory {
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		boolean value = JsonUtils.getBoolean(json , "value", true);
		return () -> Botania.gardenOfGlassLoaded == value;
	}
}
