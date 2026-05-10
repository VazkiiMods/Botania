package vazkii.botania.data.util;

import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.world.level.block.Block;

import vazkii.botania.mixin.TextureSlotAccessor;

import java.util.Optional;

import static net.minecraft.data.models.model.TextureMapping.getBlockTexture;
import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BotaniaModelTemplates {

	public static TextureSlot TOP_SIDE = TextureSlotAccessor.botania_create("top_side");
	public static TextureSlot BOTTOM_SIDE = TextureSlotAccessor.botania_create("bottom_side");

	public static ModelTemplate DOOR_BOTTOM_LEFT = create("door_bottom_left", "_bottom_left", TextureSlot.TOP, TextureSlot.BOTTOM, TOP_SIDE, BOTTOM_SIDE);
	public static ModelTemplate DOOR_BOTTOM_LEFT_OPEN = create("door_bottom_left_open", "_bottom_left_open", TextureSlot.TOP, TextureSlot.BOTTOM, TOP_SIDE, BOTTOM_SIDE);
	public static ModelTemplate DOOR_BOTTOM_RIGHT = create("door_bottom_right", "_bottom_right", TextureSlot.TOP, TextureSlot.BOTTOM, TOP_SIDE, BOTTOM_SIDE);
	public static ModelTemplate DOOR_BOTTOM_RIGHT_OPEN = create("door_bottom_right_open", "_bottom_right_open", TextureSlot.TOP, TextureSlot.BOTTOM, TOP_SIDE, BOTTOM_SIDE);
	public static ModelTemplate DOOR_TOP_LEFT = create("door_top_left", "_top_left", TextureSlot.TOP, TextureSlot.BOTTOM, TOP_SIDE, BOTTOM_SIDE);
	public static ModelTemplate DOOR_TOP_LEFT_OPEN = create("door_top_left_open", "_top_left_open", TextureSlot.TOP, TextureSlot.BOTTOM, TOP_SIDE, BOTTOM_SIDE);
	public static ModelTemplate DOOR_TOP_RIGHT = create("door_top_right", "_top_right", TextureSlot.TOP, TextureSlot.BOTTOM, TOP_SIDE, BOTTOM_SIDE);
	public static ModelTemplate DOOR_TOP_RIGHT_OPEN = create("door_top_right_open", "_top_right_open", TextureSlot.TOP, TextureSlot.BOTTOM, TOP_SIDE, BOTTOM_SIDE);

	public static ModelTemplate TRAPDOOR_TOP = create("trapdoor_top", "_top", TextureSlot.TEXTURE, TextureSlot.SIDE);
	public static ModelTemplate TRAPDOOR_BOTTOM = create("trapdoor_bottom", "_bottom", TextureSlot.TEXTURE, TextureSlot.SIDE);
	public static ModelTemplate TRAPDOOR_OPEN = create("trapdoor_open", "_open", TextureSlot.TEXTURE, TextureSlot.SIDE);

	public static TextureMapping doorMapping(Block doorBlock) {
		return new TextureMapping()
				.put(TextureSlot.TOP, getBlockTexture(doorBlock, "_top"))
				.put(TextureSlot.BOTTOM, getBlockTexture(doorBlock, "_bottom"))
				.put(TOP_SIDE, getBlockTexture(doorBlock, "_top_side"))
				.put(BOTTOM_SIDE, getBlockTexture(doorBlock, "_bottom_side"));
	}

	public static TextureMapping trapdoorMapping(Block trapdoorBlock) {
		return new TextureMapping()
				.put(TextureSlot.TEXTURE, getBlockTexture(trapdoorBlock))
				.put(TextureSlot.SIDE, getBlockTexture(trapdoorBlock, "_side"));
	}

	private static ModelTemplate create(String shapesModelLocation, String suffix, TextureSlot... requiredSlots) {
		return new ModelTemplate(Optional.of(botaniaRL("block/shapes/" + shapesModelLocation)), Optional.of(suffix), requiredSlots);
	}
}
