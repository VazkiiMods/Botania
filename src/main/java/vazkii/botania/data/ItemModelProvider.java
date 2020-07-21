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
import net.minecraft.block.PaneBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import vazkii.botania.common.block.*;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.block.decor.BlockPetalBlock;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.block.mana.BlockSpreader;
import vazkii.botania.common.item.lens.ItemLens;
import vazkii.botania.common.item.material.ItemPetal;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static vazkii.botania.common.item.ModItems.*;
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
		registerItemOverrides(items);
		registerItems(items);
	}

	private static String name(Item i) {
		return Registry.ITEM.getKey(i).getPath();
	}

	private static final ResourceLocation GENERATED = new ResourceLocation("item/generated");

	private ItemModelBuilder generatedItem(String name) {
		return withExistingParent(name, GENERATED)
				.texture("layer0", prefix("item/" + name));
	}

	private ItemModelBuilder generatedItem(Item i) {
		return generatedItem(name(i));
	}

	private void registerItems(Set<Item> items) {
		// Written manually
		items.remove(manaGun);
		items.remove(waterBowl);

		takeAll(items, i -> i instanceof ItemLens).forEach(i -> withExistingParent(name(i), GENERATED)
				.texture("layer0", prefix("item/lens"))
				.texture("layer1", prefix("item/" + name(i))));

		generatedItem(bloodPendant)
				.texture("layer1", prefix("item/" + name(bloodPendant) + "_overlay"));
		items.remove(bloodPendant);

		generatedItem(enderDagger)
				.texture("layer1", prefix("item/" + name(enderDagger) + "_overlay"));
		items.remove(enderDagger);

		generatedItem(incenseStick)
				.texture("layer1", prefix("item/" + name(incenseStick) + "_overlay"));
		items.remove(incenseStick);

		generatedItem(manaMirror)
				.texture("layer1", prefix("item/" + name(manaMirror) + "_overlay"));
		items.remove(manaMirror);

		generatedItem(manaTablet)
				.texture("layer1", prefix("item/" + name(manaTablet) + "_overlay"));
		items.remove(manaTablet);

		withExistingParent(name(thirdEye), GENERATED)
				.texture("layer0", prefix("item/" + name(thirdEye) + "_0"))
				.texture("layer1", prefix("item/" + name(thirdEye) + "_1"))
				.texture("layer2", prefix("item/" + name(thirdEye) + "_2"));
		items.remove(thirdEye);

		takeAll(items, i -> true).forEach(this::generatedItem);
	}

	private void registerItemOverrides(Set<Item> items) {
		// Written manually
		items.remove(livingwoodBow);
		items.remove(crystalBow);

		generatedItem(blackHoleTalisman).override()
				.predicate(prefix("active"), 1)
				.model(generatedItem(name(blackHoleTalisman) + "_active")).end();
		items.remove(blackHoleTalisman);

		ItemModelBuilder flaskBuilder = withExistingParent(name(brewFlask), GENERATED)
				.texture("layer0", prefix("item/" + name(flask)))
				.texture("layer1", prefix("item/" + name(brewFlask) + "_0"));
		for (int i = 1; i <= 5; i++) {
			String overrideName = name(brewFlask) + "_" + i;
			ModelFile overrideModel = withExistingParent(overrideName, GENERATED)
					.texture("layer0", prefix("item/" + name(flask)))
					.texture("layer1", prefix("item/" + overrideName));
			flaskBuilder.override()
					.predicate(prefix("swigs_taken"), i)
					.model(overrideModel).end();
		}
		items.remove(brewFlask);

		ItemModelBuilder vialBuilder = withExistingParent(name(brewVial), GENERATED)
				.texture("layer0", prefix("item/" + name(vial)))
				.texture("layer1", prefix("item/" + name(brewVial) + "_0"));
		for (int i = 1; i <= 3; i++) {
			String overrideName = name(brewVial) + "_" + i;
			ModelFile overrideModel = withExistingParent(overrideName, GENERATED)
					.texture("layer0", prefix("item/" + name(vial)))
					.texture("layer1", prefix("item/" + overrideName));
			vialBuilder.override()
					.predicate(prefix("swigs_taken"), i)
					.model(overrideModel).end();
		}
		items.remove(brewVial);

		generatedItem(elementiumShears).override()
				.predicate(prefix("reddit"), 1)
				.model(generatedItem("dammitreddit")).end();
		items.remove(elementiumShears);

		ModelFile vuvuzela = generatedItem("vuvuzela");
		generatedItem(grassHorn).override().predicate(prefix("vuvuzela"), 1).model(vuvuzela).end();
		generatedItem(leavesHorn).override().predicate(prefix("vuvuzela"), 1).model(vuvuzela).end();
		generatedItem(snowHorn).override().predicate(prefix("vuvuzela"), 1).model(vuvuzela).end();
		items.remove(grassHorn);
		items.remove(leavesHorn);
		items.remove(snowHorn);

		generatedItem(infiniteFruit).override()
				.predicate(prefix("boot"), 1)
				.model(generatedItem("dasboot")).end();
		items.remove(infiniteFruit);

		generatedItem(lexicon).override()
				.predicate(prefix("elven"), 1)
				.model(generatedItem(name(lexicon) + "_elven")).end();
		items.remove(lexicon);

		generatedItem(magnetRing).override()
				.predicate(prefix("active"), 1)
				.model(generatedItem(name(magnetRing) + "_active")).end();
		items.remove(magnetRing);

		generatedItem(magnetRingGreater).override()
				.predicate(prefix("active"), 1)
				.model(generatedItem(name(magnetRingGreater) + "_active")).end();
		items.remove(magnetRingGreater);

		ItemModelBuilder bottle = generatedItem(manaBottle);
		for (int i = 1; i <= 5; i++) {
			ModelFile overrideModel = generatedItem(name(manaBottle) + "_" + i);
			bottle.override()
					.predicate(prefix("swigs_taken"), i)
					.model(overrideModel).end();
		}
		items.remove(manaBottle);

		generatedItem(manaCookie).override()
				.predicate(prefix("totalbiscuit"), 1)
				.model(generatedItem("totalbiscuit")).end();
		items.remove(manaCookie);

		generatedItem(manasteelSword).override()
				.predicate(prefix("elucidator"), 1)
				.model(generatedItem("elucidator")).end();
		items.remove(manasteelSword);

		generatedItem(manaweaveHelm).override()
				.predicate(prefix("holiday"), 1)
				.model(generatedItem(name(manaweaveHelm) + "_holiday")).end();
		items.remove(manaweaveHelm);

		generatedItem(manaweaveChest).override()
				.predicate(prefix("holiday"), 1)
				.model(generatedItem(name(manaweaveChest) + "_holiday")).end();
		items.remove(manaweaveChest);

		generatedItem(manaweaveLegs).override()
				.predicate(prefix("holiday"), 1)
				.model(generatedItem(name(manaweaveLegs) + "_holiday")).end();
		items.remove(manaweaveLegs);

		generatedItem(manaweaveBoots).override()
				.predicate(prefix("holiday"), 1)
				.model(generatedItem(name(manaweaveBoots) + "_holiday")).end();
		items.remove(manaweaveBoots);

		generatedItem(slimeBottle).override()
				.predicate(prefix("active"), 1)
				.model(generatedItem(name(slimeBottle) + "_active")).end();
		items.remove(slimeBottle);

		generatedItem(spawnerMover).override()
				.predicate(prefix("full"), 1)
				.model(generatedItem(name(spawnerMover) + "_full")).end();
		items.remove(spawnerMover);

		generatedItem(temperanceStone).override()
				.predicate(prefix("active"), 1)
				.model(generatedItem(name(temperanceStone) + "_active")).end();
		items.remove(temperanceStone);

		generatedItem(terraAxe).override()
				.predicate(prefix("active"), 1)
				.model(generatedItem(name(terraAxe) + "_active")).end();
		items.remove(terraAxe);

		ModelFile enabledModel = withExistingParent(name(terraPick) + "_active", GENERATED)
				.texture("layer0", prefix("item/" + name(terraPick)))
				.texture("layer1", prefix("item/" + name(terraPick) + "_active"));
		ModelFile tippedEnabledModel = withExistingParent(name(terraPick) + "_tipped_active", GENERATED)
				.texture("layer0", prefix("item/" + name(terraPick) + "_tipped"))
				.texture("layer1", prefix("item/" + name(terraPick) + "_active"));

		generatedItem(terraPick).override()
				.predicate(prefix("active"), 1)
				.model(enabledModel).end()
				.override()
				.predicate(prefix("tipped"), 1)
				.model(generatedItem(name(terraPick) + "_tipped")).end()
				.override()
				.predicate(prefix("tipped"), 1).predicate(prefix("active"), 1)
				.model(tippedEnabledModel).end();
		items.remove(terraPick);

		generatedItem(tornadoRod).override()
				.predicate(prefix("active"), 1)
				.model(generatedItem(name(tornadoRod) + "_active")).end();
		items.remove(tornadoRod);

		ModelFile twigwandBind = withExistingParent(name(twigWand) + "_bind", GENERATED)
				.texture("layer0", prefix("item/" + name(twigWand)))
				.texture("layer1", prefix("item/" + name(twigWand) + "_top"))
				.texture("layer2", prefix("item/" + name(twigWand) + "_bottom"))
				.texture("layer3", prefix("item/" + name(twigWand) + "_bind"));
		generatedItem(twigWand)
				.texture("layer1", prefix("item/" + name(twigWand) + "_top"))
				.texture("layer2", prefix("item/" + name(twigWand) + "_bottom"))
				.override()
				.predicate(prefix("bindmode"), 1)
				.model(twigwandBind).end();
		items.remove(twigWand);
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
