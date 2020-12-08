///*
// * This class is distributed as part of the Botania Mod.
// * Get the Source Code in github:
// * https://github.com/Vazkii/Botania
// *
// * Botania is Open Source and distributed under the
// * Botania License: http://botaniamod.net/license.php
// */
//package vazkii.botania.data;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.PaneBlock;
//import net.minecraft.block.WallBlock;
//import net.minecraft.data.DataGenerator;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.Item;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.registry.Registry;
//import net.minecraftforge.client.model.generators.ItemModelBuilder;
//import net.minecraftforge.client.model.generators.ModelBuilder;
//import net.minecraftforge.client.model.generators.ModelFile;
//import net.minecraftforge.common.data.ExistingFileHelper;
//import vazkii.botania.common.block.*;
//import vazkii.botania.common.block.decor.BlockModMushroom;
//import vazkii.botania.common.block.decor.BlockMotifFlower;
//import vazkii.botania.common.block.decor.BlockPetalBlock;
//import vazkii.botania.common.block.mana.BlockPool;
//import vazkii.botania.common.block.mana.BlockSpreader;
//import vazkii.botania.common.item.lens.ItemLens;
//import vazkii.botania.common.item.material.ItemPetal;
//import vazkii.botania.common.lib.LibMisc;
//
//import java.util.Set;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//
//import static vazkii.botania.common.item.ModItems.*;
//import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;
//import static vazkii.botania.data.BlockstateProvider.takeAll;
//
//public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
//	public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
//		super(generator, LibMisc.MOD_ID, existingFileHelper);
//	}
//
//	@Override
//	protected void registerModels() {
//		Set<Item> items = Registry.ITEM.stream().filter(i -> LibMisc.MOD_ID.equals(Registry.ITEM.getId(i).getNamespace()))
//				.collect(Collectors.toSet());
//		registerItemBlocks(takeAll(items, i -> i instanceof BlockItem).stream().map(i -> (BlockItem) i).collect(Collectors.toSet()));
//		registerItemOverrides(items);
//		registerItems(items);
//	}
//
//	private static String name(Item i) {
//		return Registry.ITEM.getId(i).getPath();
//	}
//
//	private static final Identifier GENERATED = new Identifier("item/generated");
//	private static final Identifier HANDHELD = new Identifier("item/handheld");
//
//	private ItemModelBuilder handheldItem(String name) {
//		return withExistingParent(name, HANDHELD.toString())
//				.texture("layer0", prefix("item/" + name).toString());
//	}
//
//	private ItemModelBuilder handheldItem(Item i) {
//		return handheldItem(name(i));
//	}
//
//	private ItemModelBuilder generatedItem(String name) {
//		return withExistingParent(name, GENERATED.toString())
//				.texture("layer0", prefix("item/" + name).toString());
//	}
//
//	private ItemModelBuilder generatedItem(Item i) {
//		return generatedItem(name(i));
//	}
//
//	private void registerItems(Set<Item> items) {
//		// Written manually
//		items.remove(manaGun);
//		items.remove(waterBowl);
//
//		takeAll(items, i -> i instanceof ItemLens).forEach(i -> withExistingParent(name(i), GENERATED.toString())
//				.texture("layer0", prefix("item/lens").toString())
//				.texture("layer1", prefix("item/" + name(i)).toString()));
//
//		generatedItem(bloodPendant)
//				.texture("layer1", prefix("item/" + name(bloodPendant) + "_overlay").toString());
//		items.remove(bloodPendant);
//
//		handheldItem(enderDagger)
//				.texture("layer1", prefix("item/" + name(enderDagger) + "_overlay").toString());
//		items.remove(enderDagger);
//
//		generatedItem(incenseStick)
//				.texture("layer1", prefix("item/" + name(incenseStick) + "_overlay").toString());
//		items.remove(incenseStick);
//
//		generatedItem(manaMirror)
//				.texture("layer1", prefix("item/" + name(manaMirror) + "_overlay").toString());
//		items.remove(manaMirror);
//
//		generatedItem(manaTablet)
//				.texture("layer1", prefix("item/" + name(manaTablet) + "_overlay").toString());
//		items.remove(manaTablet);
//
//		withExistingParent(name(thirdEye), GENERATED.toString())
//				.texture("layer0", prefix("item/" + name(thirdEye) + "_0").toString())
//				.texture("layer1", prefix("item/" + name(thirdEye) + "_1").toString())
//				.texture("layer2", prefix("item/" + name(thirdEye) + "_2").toString());
//		items.remove(thirdEye);
//
//		takeAll(items, cobbleRod, dirtRod, diviningRod, elementiumAxe, elementiumPick, elementiumShovel, elementiumSword,
//				exchangeRod, fireRod, glassPick, gravityRod, manasteelAxe, manasteelPick, manasteelShears, manasteelShovel,
//				missileRod, obedienceStick, rainbowRod, smeltRod, starSword, terraSword, terraformRod, thunderSword, waterRod,
//				kingKey, skyDirtRod).forEach(this::handheldItem);
//
//		takeAll(items, i -> true).forEach(this::generatedItem);
//	}
//
//	private void registerItemOverrides(Set<Item> items) {
//		// Written manually
//		items.remove(livingwoodBow);
//		items.remove(crystalBow);
//
//		generatedItem(blackHoleTalisman).override()
//				.predicate(new ResourceLocation(prefix("active").toString()), 1)
//				.model(generatedItem(name(blackHoleTalisman) + "_active")).end();
//		items.remove(blackHoleTalisman);
//
//		ItemModelBuilder flaskBuilder = withExistingParent(name(brewFlask), GENERATED.toString())
//				.texture("layer0", prefix("item/" + name(flask)).toString())
//				.texture("layer1", prefix("item/" + name(brewFlask) + "_0").toString());
//		for (int i = 1; i <= 5; i++) {
//			String overrideName = name(brewFlask) + "_" + i;
//			ModelFile overrideModel = withExistingParent(overrideName, GENERATED.toString())
//					.texture("layer0", prefix("item/" + name(flask)).toString())
//					.texture("layer1", prefix("item/" + overrideName).toString());
//			flaskBuilder.override()
//					.predicate(new ResourceLocation(prefix("swigs_taken").toString()), i)
//					.model(overrideModel).end();
//		}
//		items.remove(brewFlask);
//
//		ItemModelBuilder vialBuilder = withExistingParent(name(brewVial), GENERATED.toString())
//				.texture("layer0", prefix("item/" + name(vial)).toString())
//				.texture("layer1", prefix("item/" + name(brewVial) + "_0").toString());
//		for (int i = 1; i <= 3; i++) {
//			String overrideName = name(brewVial) + "_" + i;
//			ModelFile overrideModel = withExistingParent(overrideName, GENERATED.toString())
//					.texture("layer0", prefix("item/" + name(vial)).toString())
//					.texture("layer1", prefix("item/" + overrideName).toString());
//			vialBuilder.override()
//					.predicate(new ResourceLocation(prefix("swigs_taken").toString()), i)
//					.model(overrideModel).end();
//		}
//		items.remove(brewVial);
//
//		handheldItem(elementiumShears).override()
//				.predicate(new ResourceLocation(prefix("reddit").toString()), 1)
//				.model(handheldItem("dammitreddit")).end();
//		items.remove(elementiumShears);
//
//		ModelFile vuvuzela = handheldItem("vuvuzela");
//		generatedItem(grassHorn).override().predicate(new ResourceLocation(prefix("vuvuzela").toString()), 1).model(vuvuzela).end();
//		generatedItem(leavesHorn).override().predicate(new ResourceLocation(prefix("vuvuzela").toString()), 1).model(vuvuzela).end();
//		generatedItem(snowHorn).override().predicate(new ResourceLocation(prefix("vuvuzela").toString()), 1).model(vuvuzela).end();
//		items.remove(grassHorn);
//		items.remove(leavesHorn);
//		items.remove(snowHorn);
//
//		generatedItem(infiniteFruit).override()
//				.predicate(new ResourceLocation(prefix("boot").toString()), 1)
//				.model(generatedItem("dasboot")).end();
//		items.remove(infiniteFruit);
//
//		generatedItem(lexicon).override()
//				.predicate(new ResourceLocation(prefix("elven").toString()), 1)
//				.model(generatedItem(name(lexicon) + "_elven")).end();
//		items.remove(lexicon);
//
//		generatedItem(magnetRing).override()
//				.predicate(new ResourceLocation(prefix("active").toString()), 1)
//				.model(generatedItem(name(magnetRing) + "_active")).end();
//		items.remove(magnetRing);
//
//		generatedItem(magnetRingGreater).override()
//				.predicate(new ResourceLocation(prefix("active").toString()), 1)
//				.model(generatedItem(name(magnetRingGreater) + "_active")).end();
//		items.remove(magnetRingGreater);
//
//		ItemModelBuilder bottle = generatedItem(manaBottle);
//		for (int i = 1; i <= 5; i++) {
//			ModelFile overrideModel = generatedItem(name(manaBottle) + "_" + i);
//			bottle.override()
//					.predicate(new ResourceLocation(prefix("swigs_taken").toString()), i)
//					.model(overrideModel).end();
//		}
//		items.remove(manaBottle);
//
//		generatedItem(manaCookie).override()
//				.predicate(new ResourceLocation(prefix("totalbiscuit").toString()), 1)
//				.model(generatedItem("totalbiscuit")).end();
//		items.remove(manaCookie);
//
//		handheldItem(manasteelSword).override()
//				.predicate(new ResourceLocation(prefix("elucidator").toString()), 1)
//				.model(handheldItem("elucidator")).end();
//		items.remove(manasteelSword);
//
//		generatedItem(manaweaveHelm).override()
//				.predicate(new ResourceLocation(prefix("holiday").toString()), 1)
//				.model(generatedItem(name(manaweaveHelm) + "_holiday")).end();
//		items.remove(manaweaveHelm);
//
//		generatedItem(manaweaveChest).override()
//				.predicate(new ResourceLocation(prefix("holiday").toString()), 1)
//				.model(generatedItem(name(manaweaveChest) + "_holiday")).end();
//		items.remove(manaweaveChest);
//
//		generatedItem(manaweaveLegs).override()
//				.predicate(new ResourceLocation(prefix("holiday").toString()), 1)
//				.model(generatedItem(name(manaweaveLegs) + "_holiday")).end();
//		items.remove(manaweaveLegs);
//
//		generatedItem(manaweaveBoots).override()
//				.predicate(new ResourceLocation(prefix("holiday").toString()), 1)
//				.model(generatedItem(name(manaweaveBoots) + "_holiday")).end();
//		items.remove(manaweaveBoots);
//
//		generatedItem(slimeBottle).override()
//				.predicate(new ResourceLocation(prefix("active").toString()), 1)
//				.model(generatedItem(name(slimeBottle) + "_active")).end();
//		items.remove(slimeBottle);
//
//		generatedItem(spawnerMover).override()
//				.predicate(new ResourceLocation(prefix("full").toString()), 1)
//				.model(generatedItem(name(spawnerMover) + "_full")).end();
//		items.remove(spawnerMover);
//
//		generatedItem(temperanceStone).override()
//				.predicate(new ResourceLocation(prefix("active").toString()), 1)
//				.model(generatedItem(name(temperanceStone) + "_active")).end();
//		items.remove(temperanceStone);
//
//		handheldItem(terraAxe).override()
//				.predicate(new ResourceLocation(prefix("active").toString()), 1)
//				.model(handheldItem(name(terraAxe) + "_active")).end();
//		items.remove(terraAxe);
//
//		ModelFile enabledModel = withExistingParent(name(terraPick) + "_active", GENERATED.toString())
//				.texture("layer0", prefix("item/" + name(terraPick)).toString())
//				.texture("layer1", prefix("item/" + name(terraPick) + "_active").toString());
//		ModelFile tippedEnabledModel = withExistingParent(name(terraPick) + "_tipped_active", GENERATED.toString())
//				.texture("layer0", prefix("item/" + name(terraPick) + "_tipped").toString())
//				.texture("layer1", prefix("item/" + name(terraPick) + "_active").toString());
//
//		handheldItem(terraPick).override()
//				.predicate(new ResourceLocation(prefix("active").toString()), 1)
//				.model(enabledModel).end()
//				.override()
//				.predicate(new ResourceLocation(prefix("tipped").toString()), 1)
//				.model(generatedItem(name(terraPick) + "_tipped")).end()
//				.override()
//				.predicate(new ResourceLocation(prefix("tipped").toString()), 1).predicate(new ResourceLocation(prefix("active").toString()), 1)
//				.model(tippedEnabledModel).end();
//		items.remove(terraPick);
//
//		handheldItem(tornadoRod).override()
//				.predicate(new ResourceLocation(prefix("active").toString()), 1)
//				.model(handheldItem(name(tornadoRod) + "_active")).end();
//		items.remove(tornadoRod);
//
//		ModelFile twigwandBind = withExistingParent(name(twigWand) + "_bind", HANDHELD.toString())
//				.texture("layer0", prefix("item/" + name(twigWand)).toString())
//				.texture("layer1", prefix("item/" + name(twigWand) + "_top").toString())
//				.texture("layer2", prefix("item/" + name(twigWand) + "_bottom").toString())
//				.texture("layer3", prefix("item/" + name(twigWand) + "_bind").toString());
//		handheldItem(twigWand)
//				.texture("layer1", prefix("item/" + name(twigWand) + "_top").toString())
//				.texture("layer2", prefix("item/" + name(twigWand) + "_bottom").toString())
//				.override()
//				.predicate(new ResourceLocation(prefix("bindmode").toString()), 1)
//				.model(twigwandBind).end();
//		items.remove(twigWand);
//	}
//
//	private void registerItemBlocks(Set<BlockItem> itemBlocks) {
//		// Manually written
//		itemBlocks.remove(ModBlocks.corporeaCrystalCube.asItem());
//
//		String animatedTorchName = Registry.ITEM.getId(ModBlocks.animatedTorch.asItem()).getPath();
//		withExistingParent(animatedTorchName, "item/generated")
//				.texture("layer0", new Identifier("block/redstone_torch").toString())
//				.texture("layer1", prefix("block/animated_torch_glimmer").toString());
//		itemBlocks.remove(ModBlocks.animatedTorch.asItem());
//
//		String gaiaHeadName = Registry.ITEM.getId(ModBlocks.gaiaHead.asItem()).getPath();
//		withExistingParent(gaiaHeadName, "item/template_skull");
//		itemBlocks.remove(ModBlocks.gaiaHead.asItem());
//
//		takeAll(itemBlocks, i -> i.getBlock() instanceof BlockModDoubleFlower).forEach(i -> {
//			String name = Registry.ITEM.getId(i).getPath();
//			withExistingParent(name, "item/generated").texture("layer0", prefix("block/" + name + "_top").toString());
//		});
//
//		takeAll(itemBlocks, i -> i.getBlock() instanceof BlockPetalBlock).forEach(i -> {
//			String name = Registry.ITEM.getId(i).getPath();
//			withExistingParent(name, prefix("block/petal_block").toString());
//		});
//
//		takeAll(itemBlocks, i -> i.getBlock() instanceof PaneBlock).forEach(i -> {
//			String name = Registry.ITEM.getId(i).getPath();
//			String baseName = name.substring(0, name.length() - "_pane".length());
//			withExistingParent(name, "item/generated")
//					.texture("layer0", prefix("block/" + baseName).toString());
//		});
//
//		Predicate<BlockItem> defaultGenerated = i -> {
//			Block b = i.getBlock();
//			return b instanceof BlockSpecialFlower || b instanceof BlockModMushroom
//					|| b instanceof BlockLightRelay
//					|| b instanceof BlockModFlower
//					|| b == ModBlocks.ghostRail;
//		};
//		takeAll(itemBlocks, defaultGenerated).forEach(i -> {
//			String name = Registry.ITEM.getId(i).getPath();
//			withExistingParent(name, "item/generated").texture("layer0", prefix("block/" + name).toString());
//		});
//
//		takeAll(itemBlocks, b -> b.getBlock() instanceof BlockMotifFlower).forEach(i -> {
//			String name = Registry.ITEM.getId(i).getPath();
//			withExistingParent(name, "item/generated").texture("layer0", prefix("block/" + name.replace("_motif", "")).toString());
//		});
//
//		takeAll(itemBlocks, i -> i.getBlock() instanceof BlockPool).forEach(i -> {
//			String name = Registry.ITEM.getId(i).getPath();
//			withExistingParent(name, prefix("block/" + name).toString())
//					.override().predicate(new ResourceLocation(prefix("full").toString()), 1).model(getExistingFile(new ResourceLocation(prefix("block/" + name + "_full").toString()))).end();
//		});
//
//		takeAll(itemBlocks, i -> i.getBlock() instanceof WallBlock).forEach(i -> {
//			String name = Registry.ITEM.getId(i).getPath();
//			String baseName = name.substring(0, name.length() - "_wall".length());
//			withExistingParent(name, new Identifier("block/wall_inventory").toString())
//					.texture("wall", prefix("block/" + baseName).toString());
//		});
//
//		takeAll(itemBlocks, i -> i.getBlock() instanceof BlockSpreader).forEach(i -> {
//			String name = Registry.ITEM.getId(i).getPath();
//			String material;
//			if (i.getBlock() == ModBlocks.elvenSpreader) {
//				material = "dreamwood";
//			} else if (i.getBlock() == ModBlocks.gaiaSpreader) {
//				material = name + "_material";
//			} else {
//				material = "livingwood";
//			}
//			withExistingParent(name, prefix("block/shapes/spreader_item").toString())
//					.texture("side", prefix("block/" + name + "_side").toString())
//					.texture("material", prefix("block/" + material).toString())
//					.texture("inside", prefix("block/" + name + "_inside").toString());
//		});
//
//		takeAll(itemBlocks, ModBlocks.avatar.asItem(), ModBlocks.bellows.asItem(),
//				ModBlocks.brewery.asItem(), ModBlocks.corporeaIndex.asItem(), ModBlocks.gaiaPylon.asItem(),
//				ModBlocks.hourglass.asItem(), ModBlocks.manaPylon.asItem(), ModBlocks.naturaPylon.asItem(), ModBlocks.teruTeruBozu.asItem())
//						.forEach(this::builtinEntity);
//
//		takeAll(itemBlocks, i -> i instanceof ItemPetal).forEach(i -> {
//			String name = Registry.ITEM.getId(i).getPath();
//			withExistingParent(name, "item/generated").texture("layer0", prefix("item/petal").toString());
//		});
//
//		String dreamwoodFenceName = Registry.ITEM.getId(ModFluffBlocks.dreamwoodFence.asItem()).getPath();
//		withExistingParent(dreamwoodFenceName, "block/fence_inventory")
//				.texture("texture", prefix("block/dreamwood_planks").toString());
//		itemBlocks.remove(ModFluffBlocks.dreamwoodFence.asItem());
//
//		String livingwoodFenceName = Registry.ITEM.getId(ModFluffBlocks.livingwoodFence.asItem()).getPath();
//		withExistingParent(livingwoodFenceName, "block/fence_inventory")
//				.texture("texture", prefix("block/livingwood_planks").toString());
//		itemBlocks.remove(ModFluffBlocks.livingwoodFence.asItem());
//
//		String elfGlassName = Registry.ITEM.getId(ModBlocks.elfGlass.asItem()).getPath();
//		withExistingParent(elfGlassName, prefix("block/elf_glass_0").toString());
//		itemBlocks.remove(ModBlocks.elfGlass.asItem());
//
//		itemBlocks.forEach(i -> {
//			String name = Registry.ITEM.getId(i).getPath();
//			withExistingParent(name, prefix("block/" + name).toString());
//		});
//	}
//
//	private void builtinEntity(Item i) {
//		// [VanillaCopy] from item/chest.json
//		String name = Registry.ITEM.getId(i).getPath();
//		getBuilder(name).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
//				.transforms()
//				.transform(ModelBuilder.Perspective.GUI)
//				.rotation(30, 45, 0)
//				.scale(0.625F)
//				.end()
//				.transform(ModelBuilder.Perspective.GROUND)
//				.translation(0, 3, 0)
//				.scale(0.25F)
//				.end()
//				.transform(ModelBuilder.Perspective.HEAD)
//				.rotation(0, 180, 0)
//				.end()
//				.transform(ModelBuilder.Perspective.FIXED)
//				.rotation(0, 180, 0)
//				.scale(0.5F)
//				.end()
//				.transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
//				.rotation(75, 315, 0)
//				.translation(0, 2.5F, 0)
//				.scale(0.375F)
//				.end()
//				.transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
//				.rotation(0, 315, 0)
//				.scale(0.4F)
//				.end()
//				.end();
//	}
//
//	/*
//	@Nonnull
//	@Override
//	public String getName() {
//		return "Botania item models";
//	}
//	*/
//}
