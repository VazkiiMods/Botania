package vazkii.botania.common.block.mana;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.mana.BurstProperties;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class GaiaManaSpreaderBlock extends ManaSpreaderBlock {

	private static final ResourceLocation GAIA_SPREADER_MODEL_ID = botaniaRL("block/gaia_spreader");
	private static final ResourceLocation GAIA_CORE_MODEL_ID = botaniaRL("block/gaia_spreader_core");
	private static final ResourceLocation GAIA_SCAFFOLDING_MODEL_ID = botaniaRL("block/gaia_spreader_scaffolding");

	public GaiaManaSpreaderBlock(Properties builder) {
		super(builder);
	}

	@Override
	public boolean isRainbowRendered() {
		return true;
	}

	public BurstProperties getDefaultBurstProperties() {
		return new BurstProperties(640, 120, 20f, 0f, 2f, 0x20FF20);
	}

	@Override
	public int getManaCapacity() {
		return 6400;
	}

	@Override
	public ResourceLocation getSpreaderModelId() {
		return GAIA_SPREADER_MODEL_ID;
	}

	@Override
	public ResourceLocation getCoreModelId() {
		return GAIA_CORE_MODEL_ID;
	}

	@Override
	public ResourceLocation getScaffoldingModelId() {
		return GAIA_SCAFFOLDING_MODEL_ID;
	}
}
