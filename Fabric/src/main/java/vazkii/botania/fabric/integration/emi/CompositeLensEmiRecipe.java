package vazkii.botania.fabric.integration.emi;

import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import vazkii.botania.common.item.lens.LensItem;

import java.util.List;

public class CompositeLensEmiRecipe extends EmiPatternCraftingRecipe {
	private static final EmiStack SLIME = EmiStack.of(Items.SLIME_BALL);
	private final List<EmiStack> lenses;

	@SuppressWarnings("unchecked")
	public CompositeLensEmiRecipe(List<EmiStack> lenses) {
		super((List<EmiIngredient>) (List<?>) lenses, EmiStack.EMPTY, null);
		this.lenses = lenses;
	}

	@Override
	public List<EmiStack> getOutputs() {
		return lenses;
	}

	@Override
	public SlotWidget getInputWidget(int slot, int x, int y) {
		if (slot == 0) {
			return new GeneratedSlotWidget(r -> lenses.get(r.nextInt(lenses.size())), unique, x, y);
		} else if (slot == 1) {
			return new SlotWidget(SLIME, x, y);
		} else if (slot == 2) {
			return new GeneratedSlotWidget(r -> lenses.get(r.nextInt(1) + r.nextInt(lenses.size())), unique, x, y);
		}
		return new SlotWidget(EmiStack.EMPTY, x, y);
	}

	@Override
	public SlotWidget getOutputWidget(int x, int y) {
		return new GeneratedSlotWidget(r -> {
			ItemStack a = lenses.get(r.nextInt(lenses.size())).getItemStack();
			ItemStack b = lenses.get(r.nextInt(lenses.size())).getItemStack();
			((LensItem) a.getItem()).setCompositeLens(a.copy(), b);
			return EmiStack.of(a);
		}, unique, x, y);
	}
}
