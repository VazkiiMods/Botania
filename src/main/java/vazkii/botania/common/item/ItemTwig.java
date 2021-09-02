package vazkii.botania.common.item;

import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemTwig extends LoomPatternItem {
	public ItemTwig(LoomPattern pattern, Properties settings) {
		super(pattern, settings);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> lines, TooltipFlag ctx) {
		//no-op
	}
	
}
