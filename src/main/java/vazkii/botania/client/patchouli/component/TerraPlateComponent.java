/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.component;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.item.ItemStack;

import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;

/**
 * Patchouli custom component that draws provided stacks arranged like the Terrestial Agglomeration Plate multiblock.
 * Size is 43 x 31.
 * Parameters: corner, center, edge, plate can be provided to override default blocks.
 */
public class TerraPlateComponent implements ICustomComponent {
	public IVariable corner = IVariable.wrap("botania:livingrock");
	public IVariable center = IVariable.wrap("botania:livingrock");
	public IVariable edge = IVariable.wrap("minecraft:lapis_block");
	public IVariable plate = IVariable.wrap("botania:terra_plate");

	private transient int x, y;
	private transient ItemStack cornerBlock, centerBlock, middleBlock, plateBlock;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
	}

	@Override
	public void render(MatrixStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		ms.push();
		ms.translate(0, 0, -10);
		context.renderItemStack(ms, x + 13, y + 1, mouseX, mouseY, cornerBlock);

		ms.translate(0F, 0F, 5F);
		context.renderItemStack(ms, x + 20, y + 4, mouseX, mouseY, middleBlock);
		context.renderItemStack(ms, x + 7, y + 4, mouseX, mouseY, middleBlock);

		ms.translate(0F, 0F, 5F);
		context.renderItemStack(ms, x + 13, y + 8, mouseX, mouseY, cornerBlock);
		context.renderItemStack(ms, x + 27, y + 8, mouseX, mouseY, centerBlock);
		context.renderItemStack(ms, x, y + 8, mouseX, mouseY, cornerBlock);

		ms.translate(0F, 0F, 5F);
		context.renderItemStack(ms, x + 7, y + 12, mouseX, mouseY, middleBlock);
		context.renderItemStack(ms, x + 20, y + 12, mouseX, mouseY, middleBlock);

		ms.translate(0F, 0F, 5F);
		context.renderItemStack(ms, x + 14, y + 15, mouseX, mouseY, cornerBlock);

		ms.translate(0F, 0F, 5F);
		context.renderItemStack(ms, x + 13, y, mouseX, mouseY, plateBlock);
		ms.pop();
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		cornerBlock = lookup.apply(corner).as(ItemStack.class);
		centerBlock = lookup.apply(center).as(ItemStack.class);
		middleBlock = lookup.apply(edge).as(ItemStack.class);
		plateBlock = lookup.apply(plate).as(ItemStack.class);
	}
}
