package vazkii.botania.client.integration.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.recipe.ManaInfusionRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ManaInfusionEmiRecipe extends BotaniaEmiRecipe {
	private static final ResourceLocation TEXTURE = prefix("textures/gui/pure_daisy_overlay.png");
	private static final EmiStack POOL;
	private final int mana;

	static {
		ItemStack stack = new ItemStack(BotaniaBlocks.manaPool);
		ItemNBTHelper.setBoolean(stack, "RenderFull", true);
		POOL = EmiStack.of(stack);
	}

	public ManaInfusionEmiRecipe(ManaInfusionRecipe recipe) {
		super(BotaniaEmiPlugin.MANA_INFUSION, recipe);
		this.input = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
		if (recipe.getRecipeCatalyst() != null) {
			this.catalysts = List.of(EmiIngredient.of(recipe.getRecipeCatalyst().getDisplayed().stream()
					.map(s -> EmiStack.of(s.getBlock())).toList()));
		}
		// TODO 1.19.4 figure out the proper way to get a registry access
		this.output = List.of(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
		mana = recipe.getManaToConsume();
	}

	@Override
	public int getDisplayHeight() {
		return 65;
	}

	@Override
	public int getDisplayWidth() {
		return 116;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.add(new BlendTextureWidget(TEXTURE, 28, 0, 65, 44, 0, 0));
		widgets.add(new ManaWidget(7, 50, mana, ManaPoolBlockEntity.MAX_MANA / 10));
		widgets.addSlot(input.get(0), 21, 13).drawBack(false);
		widgets.addSlot(POOL, 50, 13).catalyst(true).drawBack(false);
		if (catalysts.size() > 0) {
			widgets.addSlot(catalysts.get(0), 0, 13).catalyst(true).drawBack(false);
		}
		widgets.addSlot(output.get(0), 79, 13).drawBack(false).recipeContext(this);
	}

}
