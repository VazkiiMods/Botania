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
import vazkii.patchouli.api.PatchouliAPI;

import java.util.function.Function;

/**
 * Patchouli custom component that draws provided stacks arranged like the Terrestial Agglomeration Plate multiblock.
 * Size is 43 x 31.
 * Parameters: corner, center, edge, plate can be provided to override default blocks.
 */
public class TerraPlateComponent implements ICustomComponent {
	public String corner = "botania:livingrock";
	public String center = "botania:livingrock";
	public String edge = "minecraft:lapis_block";
	public String plate = "botania:terra_plate";

	private transient int x, y;
	private transient ItemStack cornerBlock, centerBlock, middleBlock, plateBlock;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
		this.cornerBlock = PatchouliAPI.instance.deserializeItemStack(corner);
		this.centerBlock = PatchouliAPI.instance.deserializeItemStack(center);
		this.middleBlock = PatchouliAPI.instance.deserializeItemStack(edge);
		this.plateBlock = PatchouliAPI.instance.deserializeItemStack(plate);
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
		ms.translate(0F, 0F, -10.0f);

		ms.pop();
	}

	@Override
	public void onVariablesAvailable(Function<String, String> lookup) {
		corner = lookup.apply(corner);
		center = lookup.apply(center);
		edge = lookup.apply(edge);
		plate = lookup.apply(plate);
	}
}
