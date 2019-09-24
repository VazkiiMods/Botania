/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 24 2019, 5:18 PM (GMT)]
 */
package vazkii.botania.client.patchouli.component;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.botania.common.Botania;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.api.VariableHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

// TODO: Remove when we don't need a bypass like that
/**
 * A custom Patchouli component that is similiar to the default {@code item} type, but accepts 
 * air as a valid option in presence of other items.
 * It does not support the {@code link_recipe} or the {@code framed} parameters.
 * @deprecated For removal when Patchouli does not throw air away.
 */
@Deprecated
public class ItemStackListComponent implements ICustomComponent {
	@VariableHolder
	public String item;

	private transient int x, y;
	private transient ItemStack[] stacks;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
		this.stacks = Arrays.stream(splitSerializedStacks(item))
				.map(PatchouliAPI.instance::deserializeItemStack).toArray(ItemStack[]::new);
	}

	@Override
	public void render(IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		int index = (PatchouliUtils.getBookTicksElapsed(context) / 20) % stacks.length;
		context.renderItemStack(x, y, mouseX, mouseY, stacks[index]);
	}

	private static final Method splitStacksFromSerializedIngredient = ObfuscationReflectionHelper.findMethod(
			vazkii.patchouli.common.util.ItemStackUtil.class, "splitStacksFromSerializedIngredient", String.class);

	//TODO stop reflecting into Patchouli
	private static String[] splitSerializedStacks(String ingredient) {
		String[] strings;
		try {
			strings = (String[]) splitStacksFromSerializedIngredient.invoke(null, ingredient);
		} catch (IllegalAccessException | InvocationTargetException e) {
			Botania.LOGGER.error("Failed to access Patchouli internals", e);
			return new String[]{"minecraft:air"};
		}
		return strings;
	}
}
