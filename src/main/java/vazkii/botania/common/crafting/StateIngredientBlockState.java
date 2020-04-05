package vazkii.botania.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import vazkii.botania.api.recipe.StateIngredient;

import java.util.Collections;
import java.util.List;

public class StateIngredientBlockState implements StateIngredient {
	private final BlockState state;

	public StateIngredientBlockState(BlockState state) {
		this.state = state;
	}

	@Override
	public boolean test(BlockState blockState) {
		return false;
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = StateIngredientHelper.serializeBlockState(state);
		object.addProperty("type", "state");
		return object;
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeVarInt(2);
		buffer.writeVarInt(Block.getStateId(state));
	}

	@Override
	public List<BlockState> getDisplayed() {
		return Collections.singletonList(state);
	}

}
