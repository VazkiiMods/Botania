/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

// Same as RenderSnowball, but ItemStack sensitive
public class RenderSnowballStack<T extends Entity> extends RenderSnowball<T> {

    private final ItemStack stack;

    public RenderSnowballStack(RenderManager renderManagerIn, ItemStack stack, RenderItem render) {
        super(renderManagerIn, stack.getItem(), render);
        this.stack = stack;
    }

    @Override
    public ItemStack func_177082_d(T entity) {
        return stack;
    }

}
