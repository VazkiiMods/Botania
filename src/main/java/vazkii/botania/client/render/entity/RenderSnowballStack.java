/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Function;

// Same as RenderSnowball, but ItemStack sensitive
public class RenderSnowballStack<T extends Entity> extends SpriteRenderer<T> {

	private final Function<T, ItemStack> stackGetter;

	public RenderSnowballStack(EntityRendererManager renderManagerIn, Item item, ItemRenderer render, Function<T, ItemStack> stackGetter) {
		super(renderManagerIn, item, render);
		this.stackGetter = stackGetter;
	}

	@Nonnull
	@Override
	public ItemStack getStackToRender(T entity) {
		return stackGetter.apply(entity);
	}

}
