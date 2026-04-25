package vazkii.botania.common.item.block;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import vazkii.botania.common.component.BotaniaDataComponents;

public class DecayableSpecialFlowerBlockItem extends SpecialFlowerBlockItem {
	private final int decayTime;

	public DecayableSpecialFlowerBlockItem(Block block, int decayTime, Properties props) {
		super(block, props);
		this.decayTime = decayTime;
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return stack.has(BotaniaDataComponents.DECAY_TICKS);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return Math.round(13F * getDecayProgress(stack));
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return Mth.hsvToRgb(getDecayProgress(stack) / 3.0F, 1.0F, 1.0F);
	}

	private float getDecayProgress(ItemStack stack) {
		return 1 - stack.getOrDefault(BotaniaDataComponents.DECAY_TICKS, 0) / (float) decayTime;
	}
}
