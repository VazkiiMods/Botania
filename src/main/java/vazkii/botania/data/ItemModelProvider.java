/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
	public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, LibMisc.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		Registry.ITEM.stream().filter(i -> LibMisc.MOD_ID.equals(Registry.ITEM.getKey(i).getNamespace()))
				.forEach(i -> {
					// todo 1.15 expand to all item models that simply reference their parent
					if (i instanceof BlockItem) {
						Block b = ((BlockItem) i).getBlock();
						String name = Registry.ITEM.getKey(i).getPath();
						if (b instanceof BlockSpecialFlower) {
							withExistingParent(name, "item/generated").texture("layer0", prefix("block/" + name));
						} else if (b instanceof BlockFloatingFlower) {
							withExistingParent(name, prefix("block/" + name));
						}
					}
				});
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania item models";
	}
}
