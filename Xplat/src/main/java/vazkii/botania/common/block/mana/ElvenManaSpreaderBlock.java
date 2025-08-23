package vazkii.botania.common.block.mana;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.mana.BurstProperties;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ElvenManaSpreaderBlock extends ManaSpreaderBlock {

	private static final ResourceLocation ELVEN_SPREADER_MODEL_ID = botaniaRL("block/elven_spreader");
	private static final ResourceLocation ELVEN_CORE_MODEL_ID = botaniaRL("block/elven_spreader_core");
	private static final ResourceLocation ELVEN_SCAFFOLDING_MODEL_ID = botaniaRL("block/elven_spreader_scaffolding");

	public ElvenManaSpreaderBlock(Properties builder) {
		super(builder);
	}

	public BurstProperties getDefaultBurstProperties() {
		return new BurstProperties(240, 80, 4f, 0f, 1.25f, 0xFF45C4);
	}

	public int getHudColor() {
		return 0xFF00AE;
	}

	@Override
	public ResourceLocation getSpreaderModelId() {
		return ELVEN_SPREADER_MODEL_ID;
	}

	@Override
	public ResourceLocation getCoreModelId() {
		return ELVEN_CORE_MODEL_ID;
	}

	@Override
	public ResourceLocation getScaffoldingModelId() {
		return ELVEN_SCAFFOLDING_MODEL_ID;
	}
}
