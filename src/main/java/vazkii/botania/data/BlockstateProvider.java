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
import net.minecraft.world.level.block.state.properties.*;

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
import vazkii.botania.common.core.helper.ColorHelper;
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
				MultiVariantGenerator.multiVariant(alfPortal).with(
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
						.put(TextureSlot.SIDE, getBlockTexture(Blocks.NOTE_BLOCK))
						.put(TextureSlot.TOP, getBlockTexture(cacophonium, "_top")), this.modelOutput));
		remainingBlocks.remove(cacophonium);

		var crateTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/crate")), Optional.empty(),
				TextureSlot.BOTTOM, TextureSlot.SIDE);
		var craftCrateBottomTex = getBlockTexture(craftCrate, "_bottom");
		var crateDispatch = PropertyDispatch.property(BotaniaStateProps.CRATE_PATTERN);
		for (var pattern : CratePattern.values()) {
			String suffix = pattern == CratePattern.NONE ? "" : "_" + pattern.getSerializedName().substring("crafty_".length());
			var model = crateTemplate.create(getModelLocation(craftCrate, suffix),
					new TextureMapping().put(TextureSlot.BOTTOM, craftCrateBottomTex)
							.put(TextureSlot.SIDE, getBlockTexture(craftCrate, suffix)),
					this.modelOutput);
			crateDispatch = crateDispatch.select(pattern, Variant.variant().with(VariantProperties.MODEL, model));
		}
		this.blockstates.add(MultiVariantGenerator.multiVariant(craftCrate).with(crateDispatch));
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

		stairsBlock(remainingBlocks, corporeaStairs, corpBlock, corpBlock, corpBlock);

		wallBlock(remainingBlocks, corporeaBrickWall, getBlockTexture(corporeaBrick));

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
		ResourceLocation[] sideGlimmeringTexs = new ResourceLocation[6];
		ResourceLocation[] sideGlimmeringStrippedTexs = new ResourceLocation[6];
		ResourceLocation[] logModels = new ResourceLocation[6];
		ResourceLocation[] strippedLogModels = new ResourceLocation[6];

		for (int i = 0; i < 6; i++) {
			int index = i + 1;
			sideTexs[i] = getBlockTexture(dreamwoodLog, "/" + index);
			topTexs[i] = getBlockTexture(dreamwoodLog, "_top");
			sideStrippedTexs[i] = getBlockTexture(dreamwoodLogStripped, "/" + index);
			topStrippedTexs[i] = getBlockTexture(dreamwoodLogStripped, "_top");
			sideGlimmeringTexs[i] = getBlockTexture(dreamwoodLogGlimmering, "/" + index);
			sideGlimmeringStrippedTexs[i] = getBlockTexture(dreamwoodLogStrippedGlimmering, "/" + index);
			logModels[i] = getModelLocation(dreamwood, "_" + index);
			strippedLogModels[i] = getModelLocation(dreamwoodStripped, "_" + index);
		}

		logWithVariants(remainingBlocks, dreamwoodLog, topTexs, sideTexs);
		logWithVariants(remainingBlocks, dreamwood, sideTexs, sideTexs);
		logWithVariants(remainingBlocks, dreamwoodLogStripped, topStrippedTexs, sideStrippedTexs);
		logWithVariants(remainingBlocks, dreamwoodStripped, sideStrippedTexs, sideStrippedTexs);
		logWithVariants(remainingBlocks, dreamwoodLogGlimmering, topTexs, sideGlimmeringTexs);
		logWithVariants(remainingBlocks, dreamwoodGlimmering, sideGlimmeringTexs, sideGlimmeringTexs);
		logWithVariants(remainingBlocks, dreamwoodLogStrippedGlimmering, topStrippedTexs, sideGlimmeringStrippedTexs);
		logWithVariants(remainingBlocks, dreamwoodStrippedGlimmering, sideGlimmeringStrippedTexs, sideGlimmeringStrippedTexs);

		stairsBlockWithVariants(remainingBlocks, dreamwoodStairs, sideTexs, sideTexs, sideTexs);
		stairsBlockWithVariants(remainingBlocks, dreamwoodStrippedStairs, sideStrippedTexs, sideStrippedTexs, sideStrippedTexs);
		slabBlockWithVariants(remainingBlocks, dreamwoodSlab, logModels, sideTexs, sideTexs, sideTexs);
		slabBlockWithVariants(remainingBlocks, dreamwoodStrippedSlab, strippedLogModels, sideStrippedTexs, sideStrippedTexs, sideStrippedTexs);
		wallBlockWithVariants(remainingBlocks, dreamwoodWall, sideTexs);
		wallBlockWithVariants(remainingBlocks, dreamwoodStrippedWall, sideStrippedTexs);

		log(remainingBlocks, livingwoodLog, getBlockTexture(livingwoodLog, "_top"), getBlockTexture(livingwoodLog));
		log(remainingBlocks, livingwood, getBlockTexture(livingwoodLog), getBlockTexture(livingwoodLog));
		log(remainingBlocks, livingwoodLogStripped, getBlockTexture(livingwoodLogStripped, "_top"), getBlockTexture(livingwoodLogStripped));
		log(remainingBlocks, livingwoodStripped, getBlockTexture(livingwoodLogStripped), getBlockTexture(livingwoodLogStripped));
		log(remainingBlocks, livingwoodLogGlimmering, getBlockTexture(livingwoodLog, "_top"), getBlockTexture(livingwoodLogGlimmering));
		log(remainingBlocks, livingwoodGlimmering, getBlockTexture(livingwoodLogGlimmering), getBlockTexture(livingwoodLogGlimmering));
		log(remainingBlocks, livingwoodLogStrippedGlimmering, getBlockTexture(livingwoodLogStripped, "_top"), getBlockTexture(livingwoodLogStrippedGlimmering));
		log(remainingBlocks, livingwoodStrippedGlimmering, getBlockTexture(livingwoodLogStrippedGlimmering), getBlockTexture(livingwoodLogStrippedGlimmering));

		stairsBlock(remainingBlocks, livingwoodStairs, getBlockTexture(livingwoodLog), getBlockTexture(livingwoodLog), getBlockTexture(livingwoodLog));
		stairsBlock(remainingBlocks, livingwoodStrippedStairs, getBlockTexture(livingwoodLogStripped), getBlockTexture(livingwoodLogStripped), getBlockTexture(livingwoodLogStripped));
		slabBlock(remainingBlocks, livingwoodSlab, getModelLocation(livingwood), getBlockTexture(livingwoodLog), getBlockTexture(livingwoodLog), getBlockTexture(livingwoodLog));
		slabBlock(remainingBlocks, livingwoodStrippedSlab, getModelLocation(livingwoodStripped), getBlockTexture(livingwoodLogStripped), getBlockTexture(livingwoodLogStripped), getBlockTexture(livingwoodLogStripped));
		wallBlock(remainingBlocks, livingwoodWall, getBlockTexture(livingwoodLog));
		wallBlock(remainingBlocks, livingwoodStrippedWall, getBlockTexture(livingwoodLogStripped));

		wallBlock(remainingBlocks, ModFluffBlocks.livingrockWall, getBlockTexture(livingrock));
		wallBlock(remainingBlocks, ModFluffBlocks.livingrockBrickWall, getBlockTexture(livingrockBrick));
		wallBlock(remainingBlocks, ModFluffBlocks.livingrockBrickMossyWall, getBlockTexture(livingrockBrickMossy));

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
		particleOnly(remainingBlocks, avatar, getBlockTexture(livingwoodLog));
		particleOnly(remainingBlocks, bellows, getBlockTexture(livingwoodLog));
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

		var outsideSlot = AccessorTextureSlot.make("outside");
		var coreSlot = AccessorTextureSlot.make("core");
		var spreaderTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/spreader")), Optional.empty(),
				TextureSlot.SIDE, TextureSlot.BACK, TextureSlot.INSIDE, outsideSlot);
		var spreaderCoreTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/spreader_core")), Optional.of("_core"),
				coreSlot);
		var spreaderPaddingTemplate = new ModelTemplate(Optional.of(prefix("block/shapes/spreader_padding")),
				Optional.empty(), TextureSlot.ALL);
		takeAll(remainingBlocks, manaSpreader, redstoneSpreader, gaiaSpreader, elvenSpreader).forEach(b -> {
			ResourceLocation outside;
			if (b == redstoneSpreader || b == manaSpreader) {
				outside = getBlockTexture(livingwoodLog);
			} else if (b == elvenSpreader) {
				outside = getBlockTexture(dreamwoodLog, "/4");
			} else {
				outside = getBlockTexture(b, "_outside");
			}
			ResourceLocation inside;
			if (b == redstoneSpreader || b == manaSpreader) {
				inside = getBlockTexture(livingwoodLogStripped);
			} else if (b == elvenSpreader) {
				inside = getBlockTexture(dreamwoodLogStripped, "/4");
			} else {
				inside = getBlockTexture(b, "_inside");
			}
			singleVariantBlockState(b, spreaderTemplate.create(b, new TextureMapping()
					.put(TextureSlot.SIDE, getBlockTexture(b, "_side"))
					.put(TextureSlot.BACK, getBlockTexture(b, "_back"))
					.put(TextureSlot.INSIDE, inside)
					.put(outsideSlot, outside), this.modelOutput));
			spreaderCoreTemplate.create(b, new TextureMapping()
					.put(coreSlot, getBlockTexture(b, "_core")), this.modelOutput);
		});
		for (DyeColor color : DyeColor.values()) {
			Block wool = ColorHelper.WOOL_MAP.apply(color);
			spreaderPaddingTemplate.create(prefix("block/" + color.getName() + "_spreader_padding"),
					new TextureMapping().put(TextureSlot.ALL, getBlockTexture(wool)), this.modelOutput
			);
		}

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
			this.blockstates.add(MultiVariantGenerator.multiVariant(b).with(
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
					MultiVariantGenerator.multiVariant(b)
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
			wallBlock(remainingBlocks, cobbleWall, getBlockTexture(cobble));

			ResourceLocation brickId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
			Block brick = Registry.BLOCK.getOptional(brickId).get();
			cubeAll(brick);

			ResourceLocation brickWallId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.WALL_SUFFIX);
			Block brickWall = Registry.BLOCK.getOptional(brickWallId).get();
			wallBlock(remainingBlocks, brickWall, getBlockTexture(brick));

			ResourceLocation chiseledBricksId = prefix("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
			Block chiseledBricks = Registry.BLOCK.getOptional(chiseledBricksId).get();
			cubeAll(chiseledBricks);

			// stairs and slabs handled above already, walls get removed automatically
			remainingBlocks.removeAll(Arrays.asList(base, cobble, brick, chiseledBricks));
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
				stairsBlock(new HashSet<>(), b, side, bottom, top);
			} else {
				var tex = prefix("block/" + baseName);
				stairsBlock(new HashSet<>(), b, tex, tex, tex);
			}
		});

		takeAll(remainingBlocks, b -> b instanceof SlabBlock).forEach(slabBlock -> {
			String name = Registry.BLOCK.getKey(slabBlock).getPath();
			String baseName = name.substring(0, name.length() - LibBlockNames.SLAB_SUFFIX.length());
			Block base = Registry.BLOCK.getOptional(prefix(baseName)).get();
			boolean quartz = name.contains("quartz");
			if (quartz) {
				var side = getBlockTexture(base, "_side");
				var bottom = getBlockTexture(base, "_bottom");
				var top = getBlockTexture(base, "_top");
				var doubleModel = getModelLocation(base);
				slabBlock(new HashSet<>(), slabBlock, doubleModel, side, bottom, top);
			} else {
				var baseTex = getBlockTexture(base);
				var doubleModel = getModelLocation(base);
				slabBlock(new HashSet<>(), slabBlock, doubleModel, baseTex, baseTex, baseTex);
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

	protected void stairsBlock(Set<Block> blocks, Block block, ResourceLocation sideTex, ResourceLocation bottomTex, ResourceLocation topTex) {
		stairsBlockWithVariants(blocks, block, new ResourceLocation[] { sideTex }, new ResourceLocation[] { bottomTex }, new ResourceLocation[] { topTex });
	}

	protected void stairsBlockWithVariants(Set<Block> blocks, Block block, ResourceLocation[] sideTextures, ResourceLocation[] bottomTextures, ResourceLocation[] topTextures) {
		int length = sideTextures.length;
		if (length != topTextures.length || length != bottomTextures.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		ResourceLocation[] innerModels = new ResourceLocation[length];
		ResourceLocation[] straightModels = new ResourceLocation[length];
		ResourceLocation[] outerModels = new ResourceLocation[length];
		for (int i = 0; i < length; i++) {
			String suffix = length == 1 ? "" : "_" + (i + 1);
			var mapping = new TextureMapping()
					.put(TextureSlot.SIDE, sideTextures[i])
					.put(TextureSlot.BOTTOM, bottomTextures[i])
					.put(TextureSlot.TOP, topTextures[i]);
			ResourceLocation modelIdInner = getModelLocation(block, "_inner" + suffix);
			ResourceLocation modelIdStraight = getModelLocation(block, suffix);
			ResourceLocation modelIdOuter = getModelLocation(block, "_outer" + suffix);
			innerModels[i] = ModelTemplates.STAIRS_INNER.create(modelIdInner, mapping, this.modelOutput);
			straightModels[i] = ModelTemplates.STAIRS_STRAIGHT.create(modelIdStraight, mapping, this.modelOutput);
			outerModels[i] = ModelTemplates.STAIRS_OUTER.create(modelIdOuter, mapping, this.modelOutput);
		}
		stairsBlockWithModels(blocks, block, innerModels, straightModels, outerModels);
	}

	protected void stairsBlockWithModels(Set<Block> blocks, Block block, ResourceLocation[] innerModels, ResourceLocation[] straightModels, ResourceLocation[] outerModels) {
		var propertyDispatch = PropertyDispatch.properties(
				BlockStateProperties.HORIZONTAL_FACING,
				BlockStateProperties.HALF,
				BlockStateProperties.STAIRS_SHAPE
		);
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			for (Half half : Half.values()) {
				for (StairsShape stairsShape : StairsShape.values()) {
					// Stair blockstates are super weird. If it's left and bottom, you need to rotate it 90deg ccw compared to
					// usual, and if it's right and top, you need to rotate it 90deg cw. This is the cleanest way I could think
					// of to do that.
					boolean isLeft = stairsShape == StairsShape.INNER_LEFT || stairsShape == StairsShape.OUTER_LEFT;
					boolean isRight = stairsShape == StairsShape.INNER_RIGHT || stairsShape == StairsShape.OUTER_RIGHT;
					int rotationOffset = isLeft && half == Half.BOTTOM ? -1 : isRight && half == Half.TOP ? 1 : 0;

					VariantProperties.Rotation[] rotations = VariantProperties.Rotation.values();
					VariantProperties.Rotation yRot = switch (direction) {
						case EAST -> rotations[(4 + rotationOffset) % 4];
						case WEST -> rotations[(2 + rotationOffset) % 4];
						case SOUTH -> rotations[(1 + rotationOffset) % 4];
						case NORTH -> rotations[(3 + rotationOffset) % 4];
						default -> throw new IllegalStateException();
					};
					VariantProperties.Rotation xRot = switch (half) {
						case BOTTOM -> VariantProperties.Rotation.R0;
						case TOP -> VariantProperties.Rotation.R180;
					};
					ResourceLocation[] models = switch (stairsShape) {
						case STRAIGHT -> straightModels;
						case OUTER_RIGHT, OUTER_LEFT -> outerModels;
						case INNER_RIGHT, INNER_LEFT -> innerModels;
					};
					propertyDispatch.select(direction, half, stairsShape, Stream.of(models).map(rl -> Variant.variant()
							.with(VariantProperties.MODEL, rl)
							.with(VariantProperties.X_ROT, xRot)
							.with(VariantProperties.Y_ROT, yRot)
							.with(VariantProperties.UV_LOCK, true)).toList());
				}
			}
		}
		this.blockstates.add(MultiVariantGenerator.multiVariant(block).with(propertyDispatch));
		blocks.remove(block);
	}

	protected void slabBlock(Set<Block> blocks, Block block, ResourceLocation doubleModel, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
		slabBlockWithVariants(blocks, block, new ResourceLocation[] { doubleModel }, new ResourceLocation[] { side }, new ResourceLocation[] { bottom }, new ResourceLocation[] { top });
	}

	protected void slabBlockWithVariants(Set<Block> blocks, Block block, ResourceLocation[] doubleModels, ResourceLocation[] sideTextures, ResourceLocation[] bottomTextures, ResourceLocation[] topTextures) {
		int length = sideTextures.length;
		if (length != topTextures.length || length != bottomTextures.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		ResourceLocation[] bottomModels = new ResourceLocation[length];
		ResourceLocation[] topModels = new ResourceLocation[length];
		for (int i = 0; i < length; i++) {
			String suffix = length == 1 ? "" : "_" + (i + 1);
			var mapping = new TextureMapping()
					.put(TextureSlot.SIDE, sideTextures[i])
					.put(TextureSlot.BOTTOM, bottomTextures[i])
					.put(TextureSlot.TOP, topTextures[i]);
			ResourceLocation modelIdBottom = getModelLocation(block, suffix);
			ResourceLocation modelIdTop = getModelLocation(block, "_top" + suffix);
			bottomModels[i] = ModelTemplates.SLAB_BOTTOM.create(modelIdBottom, mapping, this.modelOutput);
			topModels[i] = ModelTemplates.SLAB_TOP.create(modelIdTop, mapping, this.modelOutput);
		}
		slabBlockWithModels(blocks, block, bottomModels, topModels, doubleModels);
	}

	protected void slabBlockWithModels(Set<Block> blocks, Block block, ResourceLocation[] bottomModels, ResourceLocation[] topModels, ResourceLocation[] doubleModels) {
		this.blockstates.add(MultiVariantGenerator.multiVariant(block).with(
				PropertyDispatch.property(BlockStateProperties.SLAB_TYPE)
						.select(SlabType.BOTTOM, Stream.of(bottomModels).map(rl -> Variant.variant().with(VariantProperties.MODEL, rl)).toList())
						.select(SlabType.TOP, Stream.of(topModels).map(rl -> Variant.variant().with(VariantProperties.MODEL, rl)).toList())
						.select(SlabType.DOUBLE, Stream.of(doubleModels).map(rl -> Variant.variant().with(VariantProperties.MODEL, rl)).toList())
		));
		blocks.remove(block);
	}

	protected void wallBlock(Set<Block> blocks, Block block, ResourceLocation texture) {
		wallBlockWithVariants(blocks, block, new ResourceLocation[] { texture });
	}

	protected void wallBlockWithVariants(Set<Block> blocks, Block block, ResourceLocation[] sideTextures) {
		int length = sideTextures.length;
		ResourceLocation[] postModels = new ResourceLocation[length];
		ResourceLocation[] lowModels = new ResourceLocation[length];
		ResourceLocation[] tallModels = new ResourceLocation[length];
		for (int i = 0; i < length; i++) {
			String suffix = length == 1 ? "" : "_" + (i + 1);
			var mapping = new TextureMapping()
					.put(TextureSlot.WALL, sideTextures[i]);
			ResourceLocation modelIdPost = getModelLocation(block, "_post" + suffix);
			ResourceLocation modelIdLow = getModelLocation(block, "_side" + suffix);
			ResourceLocation modelIdTall = getModelLocation(block, "_side_tall" + suffix);
			postModels[i] = ModelTemplates.WALL_POST.create(modelIdPost, mapping, this.modelOutput);
			lowModels[i] = ModelTemplates.WALL_LOW_SIDE.create(modelIdLow, mapping, this.modelOutput);
			tallModels[i] = ModelTemplates.WALL_TALL_SIDE.create(modelIdTall, mapping, this.modelOutput);
		}
		wallBlockWithModels(blocks, block, postModels, lowModels, tallModels);
	}

	protected void wallBlockWithModels(Set<Block> blocks, Block block, ResourceLocation[] postModels, ResourceLocation[] lowModels, ResourceLocation[] tallModels) {
		var multiPartGenerator = MultiPartGenerator.multiPart(block);
		multiPartGenerator.with(Condition.condition().term(BlockStateProperties.UP, true),
				Stream.of(postModels).map(rl -> Variant.variant().with(VariantProperties.MODEL, rl)).toList());
		var wallSides = List.of(BlockStateProperties.EAST_WALL, BlockStateProperties.WEST_WALL, BlockStateProperties.SOUTH_WALL, BlockStateProperties.NORTH_WALL);
		for (EnumProperty<WallSide> wallSide : wallSides) {
			VariantProperties.Rotation yRot =
					wallSide == BlockStateProperties.EAST_WALL ? VariantProperties.Rotation.R90
							: wallSide == BlockStateProperties.WEST_WALL ? VariantProperties.Rotation.R270
							: wallSide == BlockStateProperties.SOUTH_WALL ? VariantProperties.Rotation.R180
							: VariantProperties.Rotation.R0;
			multiPartGenerator
					.with(Condition.condition().term(wallSide, WallSide.LOW), Stream.of(lowModels).map(rl -> Variant.variant()
							.with(VariantProperties.MODEL, rl)
							.with(VariantProperties.Y_ROT, yRot)
							.with(VariantProperties.UV_LOCK, true)).toList())
					.with(Condition.condition().term(wallSide, WallSide.TALL), Stream.of(tallModels).map(rl -> Variant.variant()
							.with(VariantProperties.MODEL, rl)
							.with(VariantProperties.Y_ROT, yRot)
							.with(VariantProperties.UV_LOCK, true)).toList());
		}
		this.blockstates.add(multiPartGenerator);
		blocks.remove(block);
	}

	protected void fenceBlock(Block block, ResourceLocation tex) {
		var mapping = TextureMapping.defaultTexture(tex);
		var postModel = ModelTemplates.FENCE_POST.create(block, mapping, this.modelOutput);
		var sideModel = ModelTemplates.FENCE_SIDE.create(block, mapping, this.modelOutput);
		this.blockstates.add(AccessorBlockModelGenerators.makeFenceState(block, postModel, sideModel));
	}

	protected void fenceGateBlock(Block block, ResourceLocation tex) {
		var mapping = TextureMapping.defaultTexture(tex);
		var openModel = ModelTemplates.FENCE_GATE_OPEN.create(block, mapping, this.modelOutput);
		var closedModel = ModelTemplates.FENCE_GATE_CLOSED.create(block, mapping, this.modelOutput);
		var openWallModel = ModelTemplates.FENCE_GATE_WALL_OPEN.create(block, mapping, this.modelOutput);
		var closedWallModel = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(block, mapping, this.modelOutput);
		this.blockstates.add(AccessorBlockModelGenerators.makeFenceGateState(block, openModel, closedModel, openWallModel, closedWallModel));
	}

	protected void cubeAll(Block b) {
		var model = ModelTemplates.CUBE_ALL.create(b, TextureMapping.cube(b), this.modelOutput);
		singleVariantBlockState(b, model);
	}

	protected void singleVariantBlockState(Block b, ResourceLocation model) {
		this.blockstates.add(MultiVariantGenerator.multiVariant(b, Variant.variant().with(VariantProperties.MODEL, model)));
	}

	protected void log(Set<Block> blocks, Block block, ResourceLocation top, ResourceLocation side) {
		logWithVariants(blocks, block, new ResourceLocation[] { top }, new ResourceLocation[] { side });
	}

	protected void logWithVariants(Set<Block> blocks, Block block, ResourceLocation[] topTextures, ResourceLocation[] sideTextures) {
		int length = topTextures.length;
		if (length != sideTextures.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		ResourceLocation[] topModels = new ResourceLocation[length];
		ResourceLocation[] sideModels = new ResourceLocation[length];
		for (int i = 0; i < length; i++) {
			String suffix = length == 1 ? "" : "_" + (i + 1);
			ResourceLocation modelIdTop = getModelLocation(block, suffix);
			ResourceLocation modelIdSide = getModelLocation(block, "_horizontal" + suffix);
			topModels[i] = ModelTemplates.CUBE_COLUMN.create(modelIdTop, TextureMapping.column(sideTextures[i], topTextures[i]), this.modelOutput);
			sideModels[i] = ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(modelIdSide, TextureMapping.column(sideTextures[i], topTextures[i]), this.modelOutput);
		}
		logWithModels(blocks, block, topModels, sideModels);
	}

	protected void logWithModels(Set<Block> blocks, Block block, ResourceLocation[] topModels, ResourceLocation[] sideModels) {
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
