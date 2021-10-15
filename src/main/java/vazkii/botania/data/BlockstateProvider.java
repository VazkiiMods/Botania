/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.*;
import vazkii.botania.common.block.decor.BlockBuriedPetals;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.block.decor.BlockMotifFlower;
import vazkii.botania.common.block.decor.BlockPetalBlock;
import vazkii.botania.common.block.decor.panes.BlockModPane;
import vazkii.botania.common.block.string.BlockRedString;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.mixin.AccessorBlockModelGenerators;
import vazkii.botania.mixin.AccessorTextureSlot;

import javax.annotation.Nonnull;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static net.minecraft.data.models.model.ModelLocationUtils.getModelLocation;
import static net.minecraft.data.models.model.TextureMapping.getBlockTexture;
import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.block.ModFluffBlocks.*;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BlockstateProvider implements DataProvider {
	private final DataGenerator generator;

	private final List<BlockStateGenerator> blockstates = new ArrayList<>();

	private final Map<ResourceLocation, Supplier<JsonElement>> models = new HashMap<>();
	private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput = models::put;

	public BlockstateProvider(DataGenerator generator) {
		this.generator = generator;
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania Blockstates and Models";
	}

	@Override
	public void run(HashCache hashCache) throws IOException {
		try {
			registerStatesAndModels();
		} catch (Exception e) {
			Botania.LOGGER.error("Error registering states and models", e);
		}

		var root = generator.getOutputFolder();
		var gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

		for (var state : blockstates) {
			ResourceLocation id = Registry.BLOCK.getKey(state.getBlock());
			var path = root.resolve("assets/" + id.getNamespace() + "/blockstates/" + id.getPath() + ".json");
			try {
				DataProvider.save(gson, hashCache, state.get(), path);
			} catch (IOException ex) {
				Botania.LOGGER.error("Error generating blockstate file for {}", id, ex);
			}
		}

		for (var e : models.entrySet()) {
			var modelId = e.getKey();
			var path = root.resolve("assets/" + modelId.getNamespace() + "/models/" + modelId.getPath() + ".json");
			try {
				DataProvider.save(gson, hashCache, e.getValue().get(), path);
			} catch (IOException ex) {
				Botania.LOGGER.error("Error generating model file {}", modelId, ex);
			}
		}
	}

	private void registerStatesAndModels() {
		Set<Block> remainingBlocks = Registry.BLOCK.stream()
				.filter(b -> LibMisc.MOD_ID.equals(Registry.BLOCK.getKey(b).getNamespace()))
				.collect(Collectors.toSet());

		// Manually written blockstate + models
		remainingBlocks.remove(ghostRail);
		remainingBlocks.remove(solidVines);

		// Manually written model, generated blockstate
		manualModel(remainingBlocks, cocoon);
		manualModel(remainingBlocks, corporeaCrystalCube);
		manualModel(remainingBlocks, distributor);
		manualModel(remainingBlocks, prism);
		manualModel(remainingBlocks, runeAltar);
		manualModel(remainingBlocks, spawnerClaw);

		// Single blocks
		var alfPortalModel = ModelTemplates.CUBE_ALL.create(getModelLocation(alfPortal), TextureMapping.cube(alfPortal), this.modelOutput);
		var alfPortalActivatedModel = ModelTemplates.CUBE_ALL.create(getModelLocation(alfPortal, "_activated"), TextureMapping.cube(getModelLocation(alfPortal, "_activated")), this.modelOutput);
		this.blockstates.add(
				MultiVariantGenerator.multiVariant(alfPortal, Variant.variant()).with(
						PropertyDispatch.property(BotaniaStateProps.ALFPORTAL_STATE)
								.select(AlfPortalState.OFF, Variant.variant().with(VariantProperties.MODEL, alfPortalModel))
								.select(AlfPortalState.ON_X, Variant.variant().with(VariantProperties.MODEL, alfPortalActivatedModel))
								.select(AlfPortalState.ON_Z, Variant.variant().with(VariantProperties.MODEL, alfPortalActivatedModel))
				));
		remainingBlocks.remove(alfPortal);

		singleVariantBlockState(bifrostPerm,
				ModelTemplates.CUBE_ALL.create(
						getModelLocation(bifrostPerm),
						TextureMapping.cube(bifrost), this.modelOutput));
		remainingBlocks.remove(bifrostPerm);

		singleVariantBlockState(cacophonium,
				ModelTemplates.CUBE_TOP.create(cacophonium, (new TextureMapping())
						.put(TextureSlot.SIDE, getBlockTexture(cacophonium))
						.put(TextureSlot.TOP, getBlockTexture(cacophonium, "_top")), this.modelOutput));
		remainingBlocks.remove(cacophonium);

		var crateTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/crate")), Optional.empty(),
				TextureSlot.BOTTOM, TextureSlot.SIDE);
		var craftCrateBottomTex = getBlockTexture(craftCrate, "_bottom");
		this.blockstates.add(MultiVariantGenerator.multiVariant(craftCrate, Arrays.stream(CratePattern.values()).map(pat -> {
			String suffix = pat == CratePattern.NONE ? "" : "_" + pat.getSerializedName().substring("crafty_".length());
			var model = crateTemplate.create(getModelLocation(craftCrate, suffix),
					new TextureMapping().put(TextureSlot.BOTTOM, craftCrateBottomTex)
							.put(TextureSlot.SIDE, getBlockTexture(craftCrate, suffix)),
					this.modelOutput);
			return Variant.variant().with(VariantProperties.MODEL, model);
		}).toArray(Variant[]::new)));
		remainingBlocks.remove(craftCrate);

		ResourceLocation corpSlabSide = prefix("block/corporea_slab_side");
		ResourceLocation corpBlock = getBlockTexture(corporeaBlock);
		var corpSlabBottomModel = ModelTemplates.SLAB_BOTTOM.create(corporeaSlab,
				new TextureMapping()
						.put(TextureSlot.BOTTOM, corpBlock).put(TextureSlot.TOP, corpBlock).put(TextureSlot.SIDE, corpBlock),
				this.modelOutput);
		var corpSlabTopModel = ModelTemplates.SLAB_TOP.create(getModelLocation(corporeaSlab, "_top"),
				new TextureMapping()
						.put(TextureSlot.BOTTOM, corpBlock).put(TextureSlot.TOP, corpBlock).put(TextureSlot.SIDE, corpBlock),
				this.modelOutput);
		var corpSlabDoubleModel = ModelTemplates.CUBE_BOTTOM_TOP.create(prefix("block/corporea_double_slab"),
				new TextureMapping()
						.put(TextureSlot.SIDE, corpSlabSide).put(TextureSlot.BOTTOM, corpBlock).put(TextureSlot.TOP, corpBlock),
				this.modelOutput);
		blockstates.add(AccessorBlockModelGenerators.makeSlabState(corporeaSlab, corpSlabBottomModel, corpSlabTopModel, corpSlabDoubleModel));
		remainingBlocks.remove(corporeaSlab);

		stairsBlock(corporeaStairs, corpBlock, corpBlock, corpBlock);
		remainingBlocks.remove(corporeaStairs);

		wallBlock(corporeaBrickWall, getBlockTexture(corporeaBrick));
		remainingBlocks.remove(corporeaBrickWall);

		this.blockstates.add(MultiVariantGenerator.multiVariant(elfGlass, IntStream.rangeClosed(0, 3)
				.mapToObj(i -> {
					var model = ModelTemplates.CUBE_ALL.create(
							getModelLocation(elfGlass, "_" + i),
							TextureMapping.cube(getBlockTexture(elfGlass, "_" + i)),
							this.modelOutput);
					return Variant.variant().with(VariantProperties.MODEL, model);
				})
				.toArray(Variant[]::new)));
		remainingBlocks.remove(elfGlass);

		singleVariantBlockState(enchantedSoil, ModelTemplates.CUBE_BOTTOM_TOP.create(
				enchantedSoil,
				TextureMapping.cubeBottomTop(enchantedSoil).put(TextureSlot.BOTTOM, getBlockTexture(Blocks.DIRT)),
				this.modelOutput
		));
		remainingBlocks.remove(enchantedSoil);

		var pumpkinModel = ModelTemplates.CUBE_ORIENTABLE.create(felPumpkin, new TextureMapping()
				.put(TextureSlot.SIDE, getBlockTexture(Blocks.PUMPKIN, "_side"))
				.put(TextureSlot.FRONT, getBlockTexture(felPumpkin))
				.put(TextureSlot.TOP, getBlockTexture(Blocks.PUMPKIN, "_top")),
				this.modelOutput
		);
		this.blockstates.add(MultiVariantGenerator.multiVariant(felPumpkin, Variant.variant().with(VariantProperties.MODEL, pumpkinModel))
				.with(AccessorBlockModelGenerators.horizontalDispatch()));
		remainingBlocks.remove(felPumpkin);

		ModelTemplate eightByEightTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/eightbyeight")),
				Optional.empty(),
				TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.WEST, TextureSlot.EAST);
		singleVariantBlockState(forestEye, eightByEightTemplate.create(forestEye,
				new TextureMapping()
						.put(TextureSlot.BOTTOM, getBlockTexture(forestEye, "_bottom"))
						.put(TextureSlot.TOP, getBlockTexture(forestEye, "_top"))
						.put(TextureSlot.NORTH, getBlockTexture(forestEye, "_north"))
						.put(TextureSlot.SOUTH, getBlockTexture(forestEye, "_south"))
						.put(TextureSlot.WEST, getBlockTexture(forestEye, "_west"))
						.put(TextureSlot.EAST, getBlockTexture(forestEye, "_east")),
				this.modelOutput));
		remainingBlocks.remove(forestEye);

		var plateFile = getModelLocation(incensePlate);
		this.blockstates.add(MultiVariantGenerator.multiVariant(incensePlate, Variant.variant().with(VariantProperties.MODEL, plateFile))
				.with(AccessorBlockModelGenerators.horizontalDispatch()));
		remainingBlocks.remove(incensePlate);

		var fourHighBottomTopTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/four_high_bottom_top")),
				Optional.empty(),
				TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
		singleVariantBlockState(lightLauncher, fourHighBottomTopTemplate.create(lightLauncher,
				new TextureMapping()
						.put(TextureSlot.BOTTOM, getBlockTexture(lightLauncher, "_end"))
						.put(TextureSlot.TOP, getBlockTexture(lightLauncher, "_end"))
						.put(TextureSlot.SIDE, getBlockTexture(lightLauncher, "_side")),
				this.modelOutput
		));
		remainingBlocks.remove(lightLauncher);

		singleVariantBlockState(openCrate,
				crateTemplate.create(openCrate, new TextureMapping()
						.put(TextureSlot.SIDE, getBlockTexture(openCrate))
						.put(TextureSlot.BOTTOM, getBlockTexture(openCrate, "_bottom")),
						this.modelOutput
				));
		remainingBlocks.remove(openCrate);

		var threeHighBottomTopTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/three_high_bottom_top")),
				Optional.empty(),
				TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
		singleVariantBlockState(sparkChanger, threeHighBottomTopTemplate.create(sparkChanger,
				TextureMapping.cubeBottomTop(sparkChanger),
				this.modelOutput));
		remainingBlocks.remove(sparkChanger);

		singleVariantBlockState(starfield, fourHighBottomTopTemplate.create(starfield,
				TextureMapping.cubeBottomTop(starfield), this.modelOutput));
		remainingBlocks.remove(starfield);

		singleVariantBlockState(terraPlate, threeHighBottomTopTemplate.create(terraPlate,
				TextureMapping.cubeBottomTop(terraPlate), this.modelOutput));
		remainingBlocks.remove(terraPlate);

		var tenByTenAllTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/tenbyten_all")),
				Optional.empty(),
				TextureSlot.ALL);
		singleVariantBlockState(tinyPlanet, tenByTenAllTemplate.create(tinyPlanet,
				TextureMapping.cube(tinyPlanet), this.modelOutput));
		remainingBlocks.remove(tinyPlanet);

		singleVariantBlockState(turntable, ModelTemplates.CUBE_BOTTOM_TOP.create(turntable,
				TextureMapping.cubeBottomTop(turntable),
				this.modelOutput
		));
		remainingBlocks.remove(turntable);

		ResourceLocation[] topTexs = new ResourceLocation[6];
		ResourceLocation[] sideTexs = new ResourceLocation[6];
		ResourceLocation[] topStrippedTexs = new ResourceLocation[6];
		ResourceLocation[] sideStrippedTexs = new ResourceLocation[6];

		for (int i = 0; i < 6; i++) {
			int index = i + 1;
			sideTexs[i] = getBlockTexture(dreamwoodLog, "/" + index);
			topTexs[i] = getBlockTexture(dreamwoodLog, "_top");
			sideStrippedTexs[i] = getBlockTexture(dreamwoodLogStripped, "/" + index);
			topStrippedTexs[i] = getBlockTexture(dreamwoodLogStripped, "_top");
		}

		logWithVariants(remainingBlocks, dreamwoodLog, topTexs, sideTexs);
		logWithVariants(remainingBlocks, dreamwood, sideTexs, sideTexs);
		logWithVariants(remainingBlocks, dreamwoodLogStripped, topStrippedTexs, sideStrippedTexs);
		logWithVariants(remainingBlocks, dreamwoodStripped, sideStrippedTexs, sideStrippedTexs);

		log(remainingBlocks, livingwoodLog, getBlockTexture(livingwoodLog, "_top"), getBlockTexture(livingwoodLog));
		log(remainingBlocks, livingwood, getBlockTexture(livingwoodLog), getBlockTexture(livingwoodLog));
		log(remainingBlocks, livingwoodLogStripped, getBlockTexture(livingwoodLogStripped, "_top"), getBlockTexture(livingwoodLogStripped));
		log(remainingBlocks, livingwoodStripped, getBlockTexture(livingwoodLogStripped), getBlockTexture(livingwoodLogStripped));

		//dreamwood_log/1.png
		//dreamwood_log/2.png
		//dreamwood_log/3.png
		//dreamwood_log/4.png
		//dreamwood_log/5.png
		//dreamwood_log/6.png
		//stripped_dreamwood_log/1.png
		//stripped_dreamwood_log/2.png
		//stripped_dreamwood_log/3.png
		//stripped_dreamwood_log/4.png
		//stripped_dreamwood_log/5.png
		//stripped_dreamwood_log/6.png
		//
		//dreamwood_log_top.png
		//dreamwood_planks.png
		//livingwood_log.png
		//livingwood_log_top.png
		//livingwood_planks.png
		//stripped_dreamwood_log_top.png
		//stripped_livingwood_log.png
		//stripped_livingwood_log_top.png

		wallBlock(ModFluffBlocks.dreamwoodWall, getBlockTexture(dreamwood));
		wallBlock(ModFluffBlocks.livingrockWall, getBlockTexture(livingrock));
		wallBlock(ModFluffBlocks.livingrockBrickWall, getBlockTexture(livingrockBrick));
		wallBlock(ModFluffBlocks.livingrockBrickMossyWall, getBlockTexture(livingrockBrickMossy));
		wallBlock(ModFluffBlocks.livingwoodWall, getBlockTexture(livingwood));
		remainingBlocks.remove(ModFluffBlocks.dreamwoodWall);
		remainingBlocks.remove(ModFluffBlocks.livingrockWall);
		remainingBlocks.remove(ModFluffBlocks.livingrockBrickWall);
		remainingBlocks.remove(ModFluffBlocks.livingrockBrickMossyWall);
		remainingBlocks.remove(ModFluffBlocks.livingwoodWall);

		fenceBlock(dreamwoodFence, getBlockTexture(dreamwoodPlanks));
		fenceGateBlock(dreamwoodFenceGate, getBlockTexture(dreamwoodPlanks));
		fenceBlock(livingwoodFence, getBlockTexture(livingwoodPlanks));
		fenceGateBlock(livingwoodFenceGate, getBlockTexture(livingwoodPlanks));
		remainingBlocks.remove(dreamwoodFence);
		remainingBlocks.remove(dreamwoodFenceGate);
		remainingBlocks.remove(livingwoodFence);
		remainingBlocks.remove(livingwoodFenceGate);

		// block entities with only particles
		particleOnly(remainingBlocks, animatedTorch, getBlockTexture(Blocks.REDSTONE_TORCH));
		particleOnly(remainingBlocks, avatar, getBlockTexture(livingwood));
		particleOnly(remainingBlocks, bellows, getBlockTexture(livingwood));
		particleOnly(remainingBlocks, brewery, getBlockTexture(livingrock));
		particleOnly(remainingBlocks, corporeaIndex, getBlockTexture(corporeaBlock));
		particleOnly(remainingBlocks, lightRelayDetector, getBlockTexture(lightRelayDetector));
		singleVariantBlockState(fakeAir, new ModelTemplate(Optional.empty(), Optional.empty()).create(fakeAir, new TextureMapping(), this.modelOutput));
		remainingBlocks.remove(fakeAir);
		particleOnly(remainingBlocks, lightRelayFork, getBlockTexture(lightRelayFork));
		particleOnly(remainingBlocks, gaiaHead, getBlockTexture(Blocks.SOUL_SAND));
		particleOnly(remainingBlocks, gaiaHeadWall, getBlockTexture(Blocks.SOUL_SAND));
		particleOnly(remainingBlocks, gaiaPylon, getBlockTexture(elementiumBlock));
		particleOnly(remainingBlocks, hourglass, getBlockTexture(manaGlass));
		particleOnly(remainingBlocks, lightRelayDefault, getBlockTexture(lightRelayDefault));
		particleOnly(remainingBlocks, manaFlame, new ResourceLocation("block/fire_0"));
		particleOnly(remainingBlocks, manaPylon, getBlockTexture(manasteelBlock));
		particleOnly(remainingBlocks, naturaPylon, getBlockTexture(terrasteelBlock));
		particleOnly(remainingBlocks, teruTeruBozu, getBlockTexture(Blocks.WHITE_WOOL));
		particleOnly(remainingBlocks, lightRelayToggle, getBlockTexture(lightRelayToggle));

		// Block groups
		Predicate<Block> flowers = b -> b instanceof BlockSpecialFlower
				|| b instanceof BlockModMushroom
				|| b instanceof BlockModFlower;
		ModelTemplate crossTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/cross")), Optional.empty(), TextureSlot.CROSS);
		takeAll(remainingBlocks, flowers).forEach(b -> singleVariantBlockState(b, crossTemplate.create(b, TextureMapping.cross(b), this.modelOutput))
		);

		takeAll(remainingBlocks, b -> b instanceof BlockMotifFlower).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath().replace("_motif", "");
			singleVariantBlockState(b, crossTemplate.create(b, new TextureMapping()
					.put(TextureSlot.CROSS, prefix("block/" + name)),
					this.modelOutput));
		});

		takeAll(remainingBlocks, corporeaFunnel, corporeaInterceptor, corporeaRetainer).forEach(b -> {
			singleVariantBlockState(b, ModelTemplates.CUBE_COLUMN.create(b,
					TextureMapping.column(getBlockTexture(b, "_side"), getBlockTexture(b, "_end")),
					this.modelOutput));
		});

		var drumModelTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/drum")), Optional.empty(), TextureSlot.TOP, TextureSlot.SIDE);
		takeAll(remainingBlocks, gatheringDrum, canopyDrum, wildDrum).forEach(b -> {
			singleVariantBlockState(b, drumModelTemplate.create(b,
					new TextureMapping()
							.put(TextureSlot.TOP, prefix("block/drum_top"))
							.put(TextureSlot.SIDE, getBlockTexture(b)),
					this.modelOutput));
		});

		var materialSlot = AccessorTextureSlot.make("material");
		var insideSlot = AccessorTextureSlot.make("inside");
		var spreaderTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/spreader")), Optional.empty(),
				TextureSlot.SIDE, materialSlot);
		var spreaderInsideTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/spreader_inside")), Optional.of("_inside"),
				insideSlot);
		takeAll(remainingBlocks, manaSpreader, redstoneSpreader, gaiaSpreader, elvenSpreader).forEach(b -> {
			ResourceLocation material;
			if (b == elvenSpreader) {
				material = getBlockTexture(dreamwood);
			} else if (b == gaiaSpreader) {
				material = getBlockTexture(b, "_material");
			} else {
				material = getBlockTexture(livingwood);
			}
			singleVariantBlockState(b, spreaderTemplate.create(b, new TextureMapping()
					.put(TextureSlot.SIDE, getBlockTexture(b, "_side"))
					.put(materialSlot, material), this.modelOutput));
			spreaderInsideTemplate.create(b, new TextureMapping()
					.put(insideSlot, getBlockTexture(b, "_inside")), this.modelOutput);
		});

		var liquidSlot = AccessorTextureSlot.make("liquid");
		var poolTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/pool")), Optional.empty(), TextureSlot.ALL);
		var poolFullTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/pool_full")), Optional.of("_full"), TextureSlot.ALL, liquidSlot);
		takeAll(remainingBlocks, manaPool, dilutedPool, fabulousPool, creativePool).forEach(b -> {
			ResourceLocation tex = b == manaPool || b == fabulousPool
					? getBlockTexture(livingrock)
					: getBlockTexture(b);
			poolFullTemplate.create(b, TextureMapping.cube(tex).put(liquidSlot, prefix("block/mana_water")), this.modelOutput);
			singleVariantBlockState(b, poolTemplate.create(b, TextureMapping.cube(tex), this.modelOutput));
		});

		takeAll(remainingBlocks, pump, tinyPotato).forEach(b -> this.blockstates.add(MultiVariantGenerator.multiVariant(b, Variant.variant().with(VariantProperties.MODEL, getModelLocation(b)))
				.with(AccessorBlockModelGenerators.horizontalDispatch()))
		);

		takeAll(remainingBlocks, enderEye, manaDetector).forEach(b -> {
			var offModel = ModelTemplates.CUBE_ALL.create(b, TextureMapping.cube(b), this.modelOutput);
			var onModel = ModelTemplates.CUBE_ALL.create(getModelLocation(b, "_powered"),
					TextureMapping.cube(getBlockTexture(b, "_powered")), this.modelOutput);
			this.blockstates.add(MultiVariantGenerator.multiVariant(b, Variant.variant()).with(
					PropertyDispatch.property(BlockStateProperties.POWERED)
							.select(false, Variant.variant().with(VariantProperties.MODEL, offModel))
							.select(true, Variant.variant().with(VariantProperties.MODEL, onModel))
			));
		});

		var petalBlockModel = new ModelTemplate(Optional.of(prefix("block/shapes/cube_all_tinted")), Optional.empty(), TextureSlot.ALL)
				.create(prefix("block/petal_block"), new TextureMapping().put(TextureSlot.ALL, prefix("block/petal_block")), this.modelOutput);
		takeAll(remainingBlocks, b -> b instanceof BlockPetalBlock).forEach(b -> singleVariantBlockState(b, petalBlockModel));

		takeAll(remainingBlocks, b -> b instanceof BlockAltGrass).forEach(b -> {
			var model = ModelTemplates.CUBE_BOTTOM_TOP.create(b, new TextureMapping()
					.put(TextureSlot.SIDE, getBlockTexture(b, "_side"))
					.put(TextureSlot.BOTTOM, getBlockTexture(Blocks.DIRT))
					.put(TextureSlot.TOP, getBlockTexture(b, "_top")),
					this.modelOutput
			);
			this.blockstates.add(MultiVariantGenerator.multiVariant(b, AccessorBlockModelGenerators.createRotatedVariants(model)));
		});

		takeAll(remainingBlocks, b -> b instanceof BlockRedString).forEach(this::redStringBlock);

		takeAll(remainingBlocks, b -> b instanceof BlockModDoubleFlower).forEach(b -> {
			var bottom = ModelTemplates.CROSS.create(b, TextureMapping.cross(b), this.modelOutput);
			var top = ModelTemplates.CROSS.create(getModelLocation(b, "_top"), TextureMapping.cross(getBlockTexture(b, "_top")), this.modelOutput);
			this.blockstates.add(
					MultiVariantGenerator.multiVariant(b, Variant.variant())
							.with(PropertyDispatch.property(TallFlowerBlock.HALF)
									.select(DoubleBlockHalf.LOWER, Variant.variant().with(VariantProperties.MODEL, bottom))
									.select(DoubleBlockHalf.UPPER, Variant.variant().with(VariantProperties.MODEL, top))
							)
			);
		});

		for (String variant : new String[] { "desert", "forest", "fungal", "mesa", "mountain",
				"plains", "swamp", "taiga" }) {

			ResourceLocation baseId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone");
			Block base = Registry.BLOCK.getOptional(baseId).get();
			cubeAll(base);

			ResourceLocation cobbleId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone");
			Block cobble = Registry.BLOCK.getOptional(cobbleId).get();
			cubeAll(cobble);

			ResourceLocation cobbleWallId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.WALL_SUFFIX);
			Block cobbleWall = Registry.BLOCK.getOptional(cobbleWallId).get();
			wallBlock(cobbleWall, getBlockTexture(cobble));

			ResourceLocation brickId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
			Block brick = Registry.BLOCK.getOptional(brickId).get();
			cubeAll(brick);

			ResourceLocation brickWallId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.WALL_SUFFIX);
			Block brickWall = Registry.BLOCK.getOptional(brickWallId).get();
			wallBlock(brickWall, getBlockTexture(brick));

			ResourceLocation chiseledBricksId = prefix("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
			Block chiseledBricks = Registry.BLOCK.getOptional(chiseledBricksId).get();
			cubeAll(chiseledBricks);

			// stairs and slabs handled above already
			remainingBlocks.removeAll(Arrays.asList(base, cobble, cobbleWall, brick, brickWall, chiseledBricks));
		}

		for (String variant : new String[] { "dark", "mana", "blaze", "lavender", "red", "elf", "sunny" }) {
			ResourceLocation quartzId = prefix(variant + "_quartz");
			Block quartz = Registry.BLOCK.getOptional(quartzId).get();
			singleVariantBlockState(quartz,
					ModelTemplates.CUBE_BOTTOM_TOP.create(quartz, TextureMapping.cubeBottomTop(quartz), this.modelOutput));

			ResourceLocation pillarId = prefix(variant + "_quartz_pillar");
			Block pillar = Registry.BLOCK.getOptional(pillarId).get();
			var pillarModel = ModelTemplates.CUBE_COLUMN.create(pillar,
					TextureMapping.column(getBlockTexture(pillar, "_side"), getBlockTexture(pillar, "_end")),
					this.modelOutput);
			this.blockstates.add(AccessorBlockModelGenerators.createAxisAlignedPillarBlock(pillar, pillarModel));

			ResourceLocation chiseledId = prefix("chiseled_" + variant + "_quartz");
			Block chiseled = Registry.BLOCK.getOptional(chiseledId).get();
			singleVariantBlockState(chiseled,
					ModelTemplates.CUBE_COLUMN.create(chiseled, new TextureMapping()
							.put(TextureSlot.SIDE, getBlockTexture(chiseled, "_side"))
							.put(TextureSlot.END, getBlockTexture(chiseled, "_end")), this.modelOutput));

			remainingBlocks.remove(quartz);
			remainingBlocks.remove(pillar);
			remainingBlocks.remove(chiseled);
		}

		takeAll(remainingBlocks, b -> b instanceof BlockBuriedPetals).forEach(b -> {
			DyeColor color = ((BlockBuriedPetals) b).color;
			ResourceLocation wool = new ResourceLocation("block/" + color.getSerializedName() + "_wool");
			particleOnly(remainingBlocks, b, wool);
		});

		var gobletSlot = AccessorTextureSlot.make("goblet");
		var topBottomSlot = AccessorTextureSlot.make("top_bottom");
		var apothecaryTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/petal_apothecary")), Optional.empty(),
				TextureSlot.SIDE, gobletSlot, topBottomSlot);
		takeAll(remainingBlocks, b -> b instanceof BlockAltar).forEach(b -> {
			singleVariantBlockState(b,
					apothecaryTemplate.create(b, new TextureMapping()
							.put(TextureSlot.SIDE, getBlockTexture(b, "_side"))
							.put(gobletSlot, getBlockTexture(b, "_goblet"))
							.put(topBottomSlot, getBlockTexture(b, "_top_bottom")), this.modelOutput));
		});

		takeAll(remainingBlocks, b -> b instanceof BlockFloatingFlower).forEach(b -> {
			// Models generated by FloatingFlowerModelProvider
			singleVariantBlockState(b, getModelLocation(b));
		});

		takeAll(remainingBlocks, b -> b instanceof BlockModPane).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			var mapping = new TextureMapping()
					.put(TextureSlot.EDGE, getBlockTexture(b))
					.put(TextureSlot.PANE, prefix("block/" + name.substring(0, name.length() - "_pane".length())));
			ResourceLocation postModel = ModelTemplates.STAINED_GLASS_PANE_POST.create(b, mapping, this.modelOutput);
			ResourceLocation sideModel = ModelTemplates.STAINED_GLASS_PANE_SIDE.create(b, mapping, this.modelOutput);
			ResourceLocation sideAltModel = ModelTemplates.STAINED_GLASS_PANE_SIDE_ALT.create(b, mapping, this.modelOutput);
			ResourceLocation noSideModel = ModelTemplates.STAINED_GLASS_PANE_NOSIDE.create(b, mapping, this.modelOutput);
			ResourceLocation noSideAltModel = ModelTemplates.STAINED_GLASS_PANE_NOSIDE_ALT.create(b, mapping, this.modelOutput);

			// [VanillaCopy] BlockModelGenerator glass panes
			this.blockstates.add(MultiPartGenerator.multiPart(b)
					.with(Variant.variant().with(VariantProperties.MODEL, postModel))
					.with(Condition.condition().term(BlockStateProperties.NORTH, true), Variant.variant().with(VariantProperties.MODEL, sideModel))
					.with(Condition.condition().term(BlockStateProperties.EAST, true), Variant.variant().with(VariantProperties.MODEL, sideModel).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.SOUTH, true), Variant.variant().with(VariantProperties.MODEL, sideAltModel))
					.with(Condition.condition().term(BlockStateProperties.WEST, true), Variant.variant().with(VariantProperties.MODEL, sideAltModel).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.NORTH, false), Variant.variant().with(VariantProperties.MODEL, noSideModel))
					.with(Condition.condition().term(BlockStateProperties.EAST, false), Variant.variant().with(VariantProperties.MODEL, noSideAltModel))
					.with(Condition.condition().term(BlockStateProperties.SOUTH, false), Variant.variant().with(VariantProperties.MODEL, noSideAltModel).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.WEST, false), Variant.variant().with(VariantProperties.MODEL, noSideModel).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
		});

		takeAll(remainingBlocks, b -> b instanceof StairBlock).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			String baseName = name.substring(0, name.length() - LibBlockNames.STAIR_SUFFIX.length());
			boolean quartz = name.contains("quartz");
			if (quartz) {
				ResourceLocation side = prefix("block/" + baseName + "_side");
				ResourceLocation bottom = prefix("block/" + baseName + "_bottom");
				ResourceLocation top = prefix("block/" + baseName + "_top");
				stairsBlock(b, side, bottom, top);
			} else {
				var tex = prefix("block/" + baseName);
				stairsBlock(b, tex, tex, tex);
			}
		});

		takeAll(remainingBlocks, b -> b instanceof SlabBlock).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			String baseName = name.substring(0, name.length() - LibBlockNames.SLAB_SUFFIX.length());
			Block base = Registry.BLOCK.getOptional(prefix(baseName)).get();
			boolean quartz = name.contains("quartz");
			if (quartz) {
				var mapping = new TextureMapping()
						.put(TextureSlot.SIDE, getBlockTexture(base, "_side"))
						.put(TextureSlot.BOTTOM, getBlockTexture(base, "_bottom"))
						.put(TextureSlot.TOP, getBlockTexture(base, "_top"));
				var bottomModel = ModelTemplates.SLAB_BOTTOM.create(b, mapping, this.modelOutput);
				var topModel = ModelTemplates.SLAB_TOP.create(b, mapping, this.modelOutput);
				var doubleModel = getModelLocation(base);
				this.blockstates.add(AccessorBlockModelGenerators.makeSlabState(b, bottomModel, topModel, doubleModel));
			} else {
				var baseTex = getBlockTexture(base);
				var mapping = new TextureMapping()
						.put(TextureSlot.SIDE, baseTex)
						.put(TextureSlot.BOTTOM, baseTex)
						.put(TextureSlot.TOP, baseTex);
				var bottomModel = ModelTemplates.SLAB_BOTTOM.create(b, mapping, this.modelOutput);
				var topModel = ModelTemplates.SLAB_TOP.create(b, mapping, this.modelOutput);
				var doubleModel = getModelLocation(base);
				this.blockstates.add(AccessorBlockModelGenerators.makeSlabState(b, bottomModel, topModel, doubleModel));
			}
		});

		remainingBlocks.forEach(this::cubeAll);
	}

	protected void particleOnly(Set<Block> blocks, Block b, ResourceLocation particle) {
		singleVariantBlockState(b, ModelTemplates.PARTICLE_ONLY.create(b, TextureMapping.particle(particle), this.modelOutput));
		blocks.remove(b);
	}

	protected void manualModel(Set<Block> blocks, Block b) {
		singleVariantBlockState(b, getModelLocation(b));
		blocks.remove(b);
	}

	private void stairsBlock(Block block, ResourceLocation sideTex, ResourceLocation bottomTex, ResourceLocation topTex) {
		var mapping = new TextureMapping()
				.put(TextureSlot.SIDE, sideTex)
				.put(TextureSlot.BOTTOM, bottomTex)
				.put(TextureSlot.TOP, topTex);
		var insideModel = ModelTemplates.STAIRS_INNER.create(block, mapping, this.modelOutput);
		var straightModel = ModelTemplates.STAIRS_STRAIGHT.create(block, mapping, this.modelOutput);
		var outsideModel = ModelTemplates.STAIRS_OUTER.create(block, mapping, this.modelOutput);
		this.blockstates.add(AccessorBlockModelGenerators.makeStairState(block, insideModel, straightModel, outsideModel));
	}

	private void fenceBlock(Block block, ResourceLocation tex) {
		var mapping = TextureMapping.defaultTexture(tex);
		var postModel = ModelTemplates.FENCE_POST.create(block, mapping, this.modelOutput);
		var sideModel = ModelTemplates.FENCE_SIDE.create(block, mapping, this.modelOutput);
		this.blockstates.add(AccessorBlockModelGenerators.makeFenceState(block, postModel, sideModel));
	}

	private void fenceGateBlock(Block block, ResourceLocation tex) {
		var mapping = TextureMapping.defaultTexture(tex);
		var openModel = ModelTemplates.FENCE_GATE_OPEN.create(block, mapping, this.modelOutput);
		var closedModel = ModelTemplates.FENCE_GATE_CLOSED.create(block, mapping, this.modelOutput);
		var openWallModel = ModelTemplates.FENCE_GATE_WALL_OPEN.create(block, mapping, this.modelOutput);
		var closedWallModel = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(block, mapping, this.modelOutput);
		this.blockstates.add(AccessorBlockModelGenerators.makeFenceGateState(block, openModel, closedModel, openWallModel, closedWallModel));
	}

	private void cubeAll(Block b) {
		var model = ModelTemplates.CUBE_ALL.create(b, TextureMapping.cube(b), this.modelOutput);
		singleVariantBlockState(b, model);
	}

	private void singleVariantBlockState(Block b, ResourceLocation model) {
		this.blockstates.add(MultiVariantGenerator.multiVariant(b, Variant.variant().with(VariantProperties.MODEL, model)));
	}

	protected void log(Set<Block> blocks, Block block, ResourceLocation top, ResourceLocation side) {
		ResourceLocation modelId = getModelLocation(block);
		ResourceLocation topModel = ModelTemplates.CUBE_COLUMN.create(modelId, TextureMapping.column(side, top), this.modelOutput);
		ResourceLocation sideModel = ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(modelId, TextureMapping.column(side, top), this.modelOutput);
		logWithModels(blocks, block, new ResourceLocation[] { topModel }, new ResourceLocation[] { sideModel });
	}

	protected void logWithVariants(Set<Block> blocks, Block block, ResourceLocation[] topTextures, ResourceLocation[] sideTextures) {
		int length = topTextures.length;
		if (length != sideTextures.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		ResourceLocation[] topModels = new ResourceLocation[length];
		ResourceLocation[] sideModels = new ResourceLocation[length];
		for (int i = 0; i < length; i++) {
			ResourceLocation modelId = getModelLocation(block, "_" + (i + 1));
			topModels[i] = ModelTemplates.CUBE_COLUMN.create(modelId, TextureMapping.column(sideTextures[i], topTextures[i]), this.modelOutput);
			sideModels[i] = ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(modelId, TextureMapping.column(sideTextures[i], topTextures[i]), this.modelOutput);
		}
		logWithModels(blocks, block, topModels, sideModels);
	}

	private void logWithModels(Set<Block> blocks, Block block, ResourceLocation[] topModels, ResourceLocation[] sideModels) {
		this.blockstates.add(MultiVariantGenerator.multiVariant(block).with(
				PropertyDispatch.property(BlockStateProperties.AXIS)
						.select(Direction.Axis.Y, Stream.of(topModels).map(rl -> Variant.variant().with(VariantProperties.MODEL, rl)).toList())
						.select(Direction.Axis.Z, Stream.of(sideModels).map(rl -> Variant.variant()
								.with(VariantProperties.MODEL, rl)
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)).toList())
						.select(Direction.Axis.X, Stream.of(sideModels).map(rl -> Variant.variant()
								.with(VariantProperties.MODEL, rl)
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
								.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).toList()
						)));
		blocks.remove(block);
	}

	// ? extends T technically not correct, but is more convenient in ItemModelProvider
	@SafeVarargs
	@SuppressWarnings("varargs")
	public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
		List<T> ret = Arrays.asList(items);
		for (T item : items) {
			if (!src.contains(item)) {
				Botania.LOGGER.warn("Item {} not found in set", item);
			}
		}
		if (!src.removeAll(ret)) {
			Botania.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
		}
		return ret;
	}

	public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
		List<T> ret = new ArrayList<>();

		Iterator<T> iter = src.iterator();
		while (iter.hasNext()) {
			T item = iter.next();
			if (pred.test(item)) {
				iter.remove();
				ret.add(item);
			}
		}

		if (ret.isEmpty()) {
			Botania.LOGGER.warn("takeAll predicate yielded nothing", new Throwable());
		}
		return ret;
	}

	protected void wallBlock(Block block, ResourceLocation tex) {
		var mapping = new TextureMapping().put(TextureSlot.WALL, tex);
		var postModel = ModelTemplates.WALL_POST.create(block, mapping, this.modelOutput);
		var lowModel = ModelTemplates.WALL_LOW_SIDE.create(block, mapping, this.modelOutput);
		var tallModel = ModelTemplates.WALL_TALL_SIDE.create(block, mapping, this.modelOutput);
		this.blockstates.add(AccessorBlockModelGenerators.makeWallState(block, postModel, lowModel, tallModel));
	}

	protected void redStringBlock(Block b) {
		ResourceLocation selfName = getBlockTexture(b);
		ResourceLocation front = prefix("block/red_string_sender");
		var model = ModelTemplates.CUBE_ORIENTABLE.create(b, new TextureMapping()
				.put(TextureSlot.TOP, selfName)
				.put(TextureSlot.FRONT, front)
				.put(TextureSlot.SIDE, selfName), this.modelOutput);
		this.blockstates.add(MultiVariantGenerator.multiVariant(b, Variant.variant().with(VariantProperties.MODEL, model))
				.with(AccessorBlockModelGenerators.facingDispatch()));
	}
}
