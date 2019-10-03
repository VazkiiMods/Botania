/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 20 2019, 2:59 PM (GMT)]
 */
package vazkii.botania.client.patchouli.component;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.crafting.Ingredient;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;

import java.util.List;

/**
 * Base custom Patchouli component that draws a rotating circle of items.
 * Size is 80x80. For a centered one, set X to -1.
 * For usage with a specified list of items, see {@link RotatingItemListComponent}.
 * For usage with a runic altar or petal apothecary recipes, see {@link RotatingRecipeComponent}.
 */
abstract class RotatingItemListComponentBase implements ICustomComponent {
	protected transient List<Ingredient> ingredients;
	protected transient int x, y;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX != -1 ? componentX : 17;
		this.y = componentY;
		this.ingredients = makeIngredients();
	}

	protected abstract List<Ingredient> makeIngredients();

	@Override
	public void render(IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		int degreePerInput = (int) (360F / ingredients.size());
		int ticksElapsed = context.getTicksInBook();

		float currentDegree = ConfigHandler.CLIENT.lexiconRotatingItems.get()
				? Screen.hasShiftDown()
				? ticksElapsed
				: ticksElapsed + pticks
				: 0;

		for(Ingredient input : ingredients) {
			renderIngredientAtAngle(context, currentDegree, input, mouseX, mouseY);

			currentDegree += degreePerInput;
		}
	}

	private void renderIngredientAtAngle(IComponentRenderContext context, float angle, Ingredient ingredient, int mouseX, int mouseY) {
		if(ingredient.hasNoMatchingItems())
			return;

		angle -= 90;
		int radius = 32;
		double xPos = x + Math.cos(angle * Math.PI / 180D) * radius + 32;
		double yPos = y + Math.sin(angle * Math.PI / 180D) * radius + 32;

		GlStateManager.pushMatrix(); // This translation makes it not stuttery. It does not affect the tooltip as that is drawn separately later.
		GlStateManager.translated(xPos - (int) xPos, yPos - (int) yPos, 0);
		context.renderIngredient((int) xPos, (int) yPos, mouseX, mouseY, ingredient);
		GlStateManager.popMatrix();
	}

}
