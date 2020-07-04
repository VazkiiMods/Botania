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
import net.minecraft.block.FenceBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import vazkii.botania.common.block.*;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.block.decor.BlockPetalBlock;
import vazkii.botania.common.block.decor.BlockShinyFlower;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.block.mana.BlockSpreader;
import vazkii.botania.common.block.string.BlockRedString;
import vazkii.botania.common.item.material.ItemPetal;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;
import static vazkii.botania.data.BlockstateProvider.takeAll;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
	public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, LibMisc.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		Set<Item> items = Registry.ITEM.stream().filter(i -> LibMisc.MOD_ID.equals(Registry.ITEM.getKey(i).getNamespace()))
				.collect(Collectors.toSet());
		registerItemBlocks(takeAll(items, i -> i instanceof BlockItem).stream().map(i -> (BlockItem) i).collect(Collectors.toSet()));
	}

	private void registerItemBlocks(Set<BlockItem> itemBlocks) {
		// Manually written
		itemBlocks.remove(ModBlocks.corporeaCrystalCube.asItem());

		String animatedTorchName = Registry.ITEM.getKey(ModBlocks.animatedTorch.asItem()).getPath();
		withExistingParent(animatedTorchName, "item/generated")
				.texture("layer0", new ResourceLocation("block/redstone_torch"))
				.texture("layer1", prefix("block/animated_torch_glimmer"));
		itemBlocks.remove(ModBlocks.animatedTorch.asItem());

		String gaiaHeadName = Registry.ITEM.getKey(ModBlocks.gaiaHead.asItem()).getPath();
		withExistingParent(gaiaHeadName, "item/template_skull");
		itemBlocks.remove(ModBlocks.gaiaHead.asItem());

		takeAll(itemBlocks, i -> i.getBlock() instanceof BlockModDoubleFlower).forEach(i -> {
			String name = Registry.ITEM.getKey(i).getPath();
			withExistingParent(name, "item/generated").texture("layer0", prefix("block/" + name + "_top"));
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof BlockPetalBlock).forEach(i -> {
			String name = Registry.ITEM.getKey(i).getPath();
			withExistingParent(name, prefix("block/petal_block"));
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof PaneBlock).forEach(i -> {
			String name = Registry.ITEM.getKey(i).getPath();
			String baseName = name.substring(0, name.length() - "_pane".length());
			withExistingParent(name, "item/generated")
					.texture("layer0", prefix("block/" + baseName));
		});

		Predicate<BlockItem> defaultGenerated = i -> {
			Block b = i.getBlock();
			return b instanceof BlockSpecialFlower || b instanceof BlockModMushroom
					|| b instanceof BlockLightRelay
					|| b instanceof BlockModFlower
					|| b == ModBlocks.ghostRail;
		};
		takeAll(itemBlocks, defaultGenerated).forEach(i -> {
			String name = Registry.ITEM.getKey(i).getPath();
			withExistingParent(name, "item/generated").texture("layer0", prefix("block/" + name));
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof BlockPool).forEach(i -> {
			String name = Registry.ITEM.getKey(i).getPath();
			withExistingParent(name, prefix("block/" + name))
					.override().predicate(prefix("full"), 1).model(getExistingFile(prefix("block/" + name + "_full"))).end();
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof WallBlock).forEach(i -> {
			String name = Registry.ITEM.getKey(i).getPath();
			String baseName = name.substring(0, name.length() - "_wall".length());
			withExistingParent(name, new ResourceLocation("block/wall_inventory"))
					.texture("wall", prefix("block/" + baseName));
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof BlockSpreader).forEach(i -> {
			String name = Registry.ITEM.getKey(i).getPath();
			String material;
			if (i.getBlock() == ModBlocks.elvenSpreader) {
				material = "dreamwood";
			} else if (i.getBlock() == ModBlocks.gaiaSpreader) {
				material = name + "_material";
			} else {
				material = "livingwood";
			}
			withExistingParent(name, prefix("block/shapes/spreader_item"))
					.texture("side", prefix("block/" + name + "_side"))
					.texture("material", prefix("block/" + material))
					.texture("inside", prefix("block/" + name + "_inside"));
		});

		takeAll(itemBlocks, ModBlocks.avatar.asItem(), ModBlocks.bellows.asItem(),
				ModBlocks.brewery.asItem(), ModBlocks.corporeaIndex.asItem(), ModBlocks.gaiaPylon.asItem(),
				ModBlocks.hourglass.asItem(), ModBlocks.manaPylon.asItem(), ModBlocks.naturaPylon.asItem(), ModBlocks.teruTeruBozu.asItem())
						.forEach(this::builtinEntity);

		takeAll(itemBlocks, i -> i instanceof ItemPetal).forEach(i -> {
			String name = Registry.ITEM.getKey(i).getPath();
			withExistingParent(name, "item/generated").texture("layer0", prefix("item/petal"));
		});

		String dreamwoodFenceName = Registry.ITEM.getKey(ModFluffBlocks.dreamwoodFence.asItem()).getPath();
		withExistingParent(dreamwoodFenceName, "block/fence_inventory")
				.texture("texture", prefix("block/dreamwood_planks"));
		itemBlocks.remove(ModFluffBlocks.dreamwoodFence.asItem());

		String livingwoodFenceName = Registry.ITEM.getKey(ModFluffBlocks.livingwoodFence.asItem()).getPath();
		withExistingParent(livingwoodFenceName, "block/fence_inventory")
				.texture("texture", prefix("block/livingwood_planks"));
		itemBlocks.remove(ModFluffBlocks.livingwoodFence.asItem());

		String elfGlassName = Registry.ITEM.getKey(ModBlocks.elfGlass.asItem()).getPath();
		withExistingParent(elfGlassName, prefix("block/elf_glass_0"));
		itemBlocks.remove(ModBlocks.elfGlass.asItem());

		itemBlocks.forEach(i -> {
			String name = Registry.ITEM.getKey(i).getPath();
			withExistingParent(name, prefix("block/" + name));
		});
	}

	private void builtinEntity(Item i) {
		// [VanillaCopy] from item/chest.json
		String name = Registry.ITEM.getKey(i).getPath();
		getBuilder(name).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
				.transforms()
				.transform(ModelBuilder.Perspective.GUI)
				.rotation(30, 45, 0)
				.scale(0.625F)
				.end()
				.transform(ModelBuilder.Perspective.GROUND)
				.translation(0, 3, 0)
				.scale(0.25F)
				.end()
				.transform(ModelBuilder.Perspective.HEAD)
				.rotation(0, 180, 0)
				.end()
				.transform(ModelBuilder.Perspective.FIXED)
				.rotation(0, 180, 0)
				.scale(0.5F)
				.end()
				.transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
				.rotation(75, 315, 0)
				.translation(0, 2.5F, 0)
				.scale(0.375F)
				.end()
				.transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
				.rotation(0, 315, 0)
				.scale(0.4F)
				.end()
				.end();
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania item models";
	}
}
