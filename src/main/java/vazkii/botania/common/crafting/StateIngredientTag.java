package vazkii.botania.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.recipe.StateIngredient;

import java.util.List;
import java.util.stream.Collectors;

public class StateIngredientTag implements StateIngredient {
	private final Tag<Block> tag;

	public StateIngredientTag(Tag<Block> tag) {
		this.tag = tag;
	}

	public StateIngredientTag(ResourceLocation id) {
		this(new BlockTags.Wrapper(id));
	}

	@Override
	public boolean test(BlockState state) {
		return tag.contains(state.getBlock());
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "tag");
		object.addProperty("tag", tag.getId().toString());
		return object;
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeVarInt(0);
		buffer.writeResourceLocation(tag.getId());
	}

	@Override
	public List<BlockState> getDisplayed() {
		return tag.getAllElements().stream().map(Block::getDefaultState).collect(Collectors.toList());
	}

}
