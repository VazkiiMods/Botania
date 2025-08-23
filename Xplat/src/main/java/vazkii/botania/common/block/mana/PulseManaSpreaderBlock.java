package vazkii.botania.common.block.mana;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.mana.BurstProperties;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class PulseManaSpreaderBlock extends ManaSpreaderBlock {

	private static final ResourceLocation PULSE_SPREADER_MODEL_ID = botaniaRL("block/redstone_spreader");
	private static final ResourceLocation PULSE_CORE_MODEL_ID = botaniaRL("block/redstone_spreader_core");
	private static final ResourceLocation PULSE_SCAFFOLDING_MODEL_ID = botaniaRL("block/redstone_spreader_scaffolding");

	public PulseManaSpreaderBlock(Properties builder) {
		super(builder);
	}

	@Override
	public boolean isRedstoneTriggered() {
		return true;
	}

	@Override
	public BurstProperties getDefaultBurstProperties() {
		BurstProperties defaultProps = super.getDefaultBurstProperties();
		defaultProps.color = 0xFF2020;
		return defaultProps;
	}

	@Override
	public int getHudColor() {
		return 0xFF0000;
	}

	@Override
	public ResourceLocation getSpreaderModelId() {
		return PULSE_SPREADER_MODEL_ID;
	}

	@Override
	public ResourceLocation getCoreModelId() {
		return PULSE_CORE_MODEL_ID;
	}

	@Override
	public ResourceLocation getScaffoldingModelId() {
		return PULSE_SCAFFOLDING_MODEL_ID;
	}
}
