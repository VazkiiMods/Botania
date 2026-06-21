/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import com.google.gson.JsonElement;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;

import org.apache.commons.lang3.function.TriConsumer;
import org.slf4j.Logger;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.api.state.enums.CraftyCratePattern;
import vazkii.botania.common.block.*;
import vazkii.botania.common.block.flower.*;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.block.mana.ManaSpreaderBlock;
import vazkii.botania.common.block.red_string.RedStringBlock;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.data.util.BotaniaModelTemplates;
import vazkii.botania.mixin.BlockModelGeneratorsAccessor;
import vazkii.botania.mixin.TextureSlotAccessor;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static net.minecraft.data.models.model.ModelLocationUtils.getModelLocation;
import static net.minecraft.data.models.model.TextureMapping.getBlockTexture;
import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BlockstateProvider implements DataProvider {
	protected final PackOutput packOutput;

	protected final List<BlockStateGenerator> blockStateGenerators = new ArrayList<>();

	protected final Map<ResourceLocation, Supplier<JsonElement>> models = new HashMap<>();
	protected final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput = models::put;

	public BlockstateProvider(PackOutput packOutput) {
		this.packOutput = packOutput;
	}

	protected Logger getLogger() {
		return BotaniaAPI.LOGGER;
	}

	@Override
	public String getName() {
		return "Botania Blockstates and Models";
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		try {
			registerStatesAndModels();
		} catch (Exception e) {
			getLogger().error("Error registering states and models", e);
		}

		PackOutput.PathProvider blockstatePathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
		PackOutput.PathProvider modelPathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
		List<CompletableFuture<?>> outputList = new ArrayList<>();

		for (BlockStateGenerator state : blockStateGenerators) {
			ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
			Path path = blockstatePathProvider.json(id);
			outputList.add(DataProvider.saveStable(output, state.get(), path));
		}

		for (Map.Entry<ResourceLocation, Supplier<JsonElement>> e : models.entrySet()) {
			ResourceLocation modelId = e.getKey();
			Path path = modelPathProvider.json(modelId);
			outputList.add(DataProvider.saveStable(output, e.getValue().get(), path));
		}
		return CompletableFuture.allOf(outputList.toArray(CompletableFuture[]::new));
	}

	protected void registerStatesAndModels() {
		Set<Block> remainingBlocks = BuiltInRegistries.BLOCK.stream()
				.filter(block -> BotaniaAPI.MODID.equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace()))
				.collect(Collectors.toSet());

		// Manually written blockstate + models
		remainingBlocks.remove(BotaniaBlocks.SPECTRAL_RAIL);
		remainingBlocks.remove(BotaniaBlocks.SOLID_VINE);

		// Manually written model, generated blockstate
		manualModel(remainingBlocks, BotaniaBlocks.COCOON_OF_CAPRICE);
		manualModel(remainingBlocks, BotaniaBlocks.CORPOREA_CRYSTAL_CUBE);
		manualModel(remainingBlocks, BotaniaBlocks.MANA_SPLITTER);
		manualModel(remainingBlocks, BotaniaBlocks.MANA_PRISM);
		manualModel(remainingBlocks, BotaniaBlocks.RUNIC_ALTAR);
		manualModel(remainingBlocks, BotaniaBlocks.LIFE_IMBUER);

		// Single blocks
		var alfPortalModel = ModelTemplates.CUBE_ALL.create(
				getModelLocation(BotaniaBlocks.ELVEN_GATEWAY_CORE),
				TextureMapping.cube(BotaniaBlocks.ELVEN_GATEWAY_CORE),
				this.modelOutput);
		var alfPortalActivatedModel = ModelTemplates.CUBE_ALL.create(
				getModelLocation(BotaniaBlocks.ELVEN_GATEWAY_CORE, "_activated"),
				TextureMapping.cube(getModelLocation(BotaniaBlocks.ELVEN_GATEWAY_CORE, "_activated")),
				this.modelOutput);
		this.blockStateGenerators.add(
				MultiVariantGenerator.multiVariant(BotaniaBlocks.ELVEN_GATEWAY_CORE).with(
						PropertyDispatch.property(BotaniaStateProperties.ALFPORTAL_STATE)
								.select(AlfheimPortalState.OFF,
										Variant.variant().with(VariantProperties.MODEL, alfPortalModel))
								.select(AlfheimPortalState.ON_X,
										Variant.variant().with(VariantProperties.MODEL, alfPortalActivatedModel))
								.select(AlfheimPortalState.ON_Z,
										Variant.variant().with(VariantProperties.MODEL, alfPortalActivatedModel))
				));
		remainingBlocks.remove(BotaniaBlocks.ELVEN_GATEWAY_CORE);

		singleVariantBlockState(
				BotaniaBlocks.BIFROST_BRIDGE,
				ModelTemplates.CUBE_ALL.create(
						getModelLocation(BotaniaBlocks.BIFROST_BRIDGE),
						TextureMapping.cube(BotaniaBlocks.BIFROST), this.modelOutput));
		remainingBlocks.remove(BotaniaBlocks.BIFROST_BRIDGE);

		singleVariantBlockState(
				BotaniaBlocks.CACOPHONIUM_BLOCK,
				ModelTemplates.CUBE_TOP.create(
						BotaniaBlocks.CACOPHONIUM_BLOCK, (new TextureMapping())
								.put(TextureSlot.SIDE, getBlockTexture(Blocks.NOTE_BLOCK))
								.put(TextureSlot.TOP, getBlockTexture(BotaniaBlocks.CACOPHONIUM_BLOCK, "_top")),
						this.modelOutput));
		remainingBlocks.remove(BotaniaBlocks.CACOPHONIUM_BLOCK);

		var crateTemplate = new ModelTemplate(Optional.of(botaniaRL("block/shapes/crate")), Optional.empty(),
				TextureSlot.BOTTOM, TextureSlot.SIDE);
		var craftCrateBottomTex = getBlockTexture(BotaniaBlocks.CRAFTY_CRATE, "_bottom");
		var crateDispatch = PropertyDispatch.property(BotaniaStateProperties.CRATE_PATTERN);
		for (var pattern : CraftyCratePattern.values()) {
			String suffix = pattern == CraftyCratePattern.NONE
					? ""
					: "_" + pattern.getSerializedName().substring("crafty_".length());
			var model = crateTemplate.create(getModelLocation(BotaniaBlocks.CRAFTY_CRATE, suffix),
					new TextureMapping().put(TextureSlot.BOTTOM, craftCrateBottomTex)
							.put(TextureSlot.SIDE, getBlockTexture(BotaniaBlocks.CRAFTY_CRATE, suffix)),
					this.modelOutput);
			crateDispatch = crateDispatch.select(pattern, Variant.variant().with(VariantProperties.MODEL, model));
		}
		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(BotaniaBlocks.CRAFTY_CRATE).with(crateDispatch));
		remainingBlocks.remove(BotaniaBlocks.CRAFTY_CRATE);

		ResourceLocation corpSlabSide = botaniaRL("block/corporea_slab_side");
		ResourceLocation corpBlock = getBlockTexture(BotaniaBlocks.CORPOREA_BLOCK);
		var corpSlabBottomModel = ModelTemplates.SLAB_BOTTOM.create(
				BotaniaBlocks.CORPOREA_SLAB,
				new TextureMapping()
						.put(TextureSlot.BOTTOM, corpBlock)
						.put(TextureSlot.TOP, corpBlock)
						.put(TextureSlot.SIDE, corpBlock),
				this.modelOutput);
		var corpSlabTopModel = ModelTemplates.SLAB_TOP.create(
				getModelLocation(BotaniaBlocks.CORPOREA_SLAB, "_top"),
				new TextureMapping()
						.put(TextureSlot.BOTTOM, corpBlock)
						.put(TextureSlot.TOP, corpBlock)
						.put(TextureSlot.SIDE, corpBlock),
				this.modelOutput);
		var corpSlabDoubleModel = ModelTemplates.CUBE_BOTTOM_TOP.create(
				botaniaRL("block/corporea_double_slab"),
				new TextureMapping()
						.put(TextureSlot.SIDE, corpSlabSide)
						.put(TextureSlot.BOTTOM, corpBlock)
						.put(TextureSlot.TOP, corpBlock),
				this.modelOutput);
		blockStateGenerators.add(BlockModelGeneratorsAccessor.botania_createSlab(BotaniaBlocks.CORPOREA_SLAB,
				corpSlabBottomModel, corpSlabTopModel, corpSlabDoubleModel));
		remainingBlocks.remove(BotaniaBlocks.CORPOREA_SLAB);

		stairsBlock(remainingBlocks, BotaniaBlocks.CORPOREA_STAIRS, corpBlock, corpBlock, corpBlock);

		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(
				BotaniaBlocks.ALFGLASS, IntStream.rangeClosed(0, 3)
						.mapToObj(i -> {
							var model = ModelTemplates.CUBE_ALL.create(
									getModelLocation(BotaniaBlocks.ALFGLASS, "_" + i),
									TextureMapping.cube(getBlockTexture(BotaniaBlocks.ALFGLASS, "_" + i)),
									this.modelOutput);
							return Variant.variant().with(VariantProperties.MODEL, model);
						})
						.toArray(Variant[]::new)));
		remainingBlocks.remove(BotaniaBlocks.ALFGLASS);

		var pumpkinModel = ModelTemplates.CUBE_ORIENTABLE.create(
				BotaniaBlocks.FEL_PUMPKIN, new TextureMapping()
						.put(TextureSlot.SIDE, getBlockTexture(Blocks.PUMPKIN, "_side"))
						.put(TextureSlot.FRONT, getBlockTexture(BotaniaBlocks.FEL_PUMPKIN))
						.put(TextureSlot.TOP, getBlockTexture(Blocks.PUMPKIN, "_top")),
				this.modelOutput
		);
		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(
				BotaniaBlocks.FEL_PUMPKIN, Variant.variant().with(VariantProperties.MODEL, pumpkinModel))
				.with(BlockModelGeneratorsAccessor.botania_createHorizontalFacingDispatch()));
		remainingBlocks.remove(BotaniaBlocks.FEL_PUMPKIN);

		ModelTemplate eightByEightTemplate = new ModelTemplate(Optional.of(botaniaRL("block/shapes/eightbyeight")),
				Optional.empty(),
				TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.WEST, TextureSlot.EAST);
		singleVariantBlockState(
				BotaniaBlocks.EYE_OF_THE_ANCIENTS, eightByEightTemplate.create(
						BotaniaBlocks.EYE_OF_THE_ANCIENTS,
						new TextureMapping()
								.put(TextureSlot.BOTTOM, getBlockTexture(BotaniaBlocks.EYE_OF_THE_ANCIENTS, "_bottom"))
								.put(TextureSlot.TOP, getBlockTexture(BotaniaBlocks.EYE_OF_THE_ANCIENTS, "_top"))
								.put(TextureSlot.NORTH, getBlockTexture(BotaniaBlocks.EYE_OF_THE_ANCIENTS, "_north"))
								.put(TextureSlot.SOUTH, getBlockTexture(BotaniaBlocks.EYE_OF_THE_ANCIENTS, "_south"))
								.put(TextureSlot.WEST, getBlockTexture(BotaniaBlocks.EYE_OF_THE_ANCIENTS, "_west"))
								.put(TextureSlot.EAST, getBlockTexture(BotaniaBlocks.EYE_OF_THE_ANCIENTS, "_east")),
						this.modelOutput));
		remainingBlocks.remove(BotaniaBlocks.EYE_OF_THE_ANCIENTS);

		var plateFile = getModelLocation(BotaniaBlocks.INCENSE_PLATE);
		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(
				BotaniaBlocks.INCENSE_PLATE, Variant.variant().with(VariantProperties.MODEL, plateFile))
				.with(BlockModelGeneratorsAccessor.botania_createHorizontalFacingDispatch()));
		remainingBlocks.remove(BotaniaBlocks.INCENSE_PLATE);

		var fourHighBottomTopTemplate = new ModelTemplate(Optional.of(botaniaRL("block/shapes/four_high_bottom_top")),
				Optional.empty(),
				TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
		singleVariantBlockState(
				BotaniaBlocks.LUMINIZER_LAUNCHER, fourHighBottomTopTemplate.create(
						BotaniaBlocks.LUMINIZER_LAUNCHER,
						new TextureMapping()
								.put(TextureSlot.BOTTOM, getBlockTexture(BotaniaBlocks.LUMINIZER_LAUNCHER, "_end"))
								.put(TextureSlot.TOP, getBlockTexture(BotaniaBlocks.LUMINIZER_LAUNCHER, "_end"))
								.put(TextureSlot.SIDE, getBlockTexture(BotaniaBlocks.LUMINIZER_LAUNCHER, "_side")),
						this.modelOutput
				));
		remainingBlocks.remove(BotaniaBlocks.LUMINIZER_LAUNCHER);

		singleVariantBlockState(
				BotaniaBlocks.OPEN_CRATE,
				crateTemplate.create(
						BotaniaBlocks.OPEN_CRATE, new TextureMapping()
								.put(TextureSlot.SIDE, getBlockTexture(BotaniaBlocks.OPEN_CRATE))
								.put(TextureSlot.BOTTOM, getBlockTexture(BotaniaBlocks.OPEN_CRATE, "_bottom")),
						this.modelOutput
				));
		remainingBlocks.remove(BotaniaBlocks.OPEN_CRATE);

		var threeHighBottomTopTemplate = new ModelTemplate(Optional.of(botaniaRL("block/shapes/three_high_bottom_top")),
				Optional.empty(),
				TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
		singleVariantBlockState(
				BotaniaBlocks.SPARK_TINKERER, threeHighBottomTopTemplate.create(
						BotaniaBlocks.SPARK_TINKERER,
						TextureMapping.cubeBottomTop(BotaniaBlocks.SPARK_TINKERER),
						this.modelOutput));
		remainingBlocks.remove(BotaniaBlocks.SPARK_TINKERER);

		singleVariantBlockState(
				BotaniaBlocks.STARFIELD_CREATOR, fourHighBottomTopTemplate.create(
						BotaniaBlocks.STARFIELD_CREATOR,
						TextureMapping.cubeBottomTop(BotaniaBlocks.STARFIELD_CREATOR), this.modelOutput));
		remainingBlocks.remove(BotaniaBlocks.STARFIELD_CREATOR);

		singleVariantBlockState(
				BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE, threeHighBottomTopTemplate.create(
						BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE,
						TextureMapping.cubeBottomTop(BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE), this.modelOutput));
		remainingBlocks.remove(BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE);

		var tenByTenAllTemplate = new ModelTemplate(Optional.of(botaniaRL("block/shapes/tenbyten_all")),
				Optional.empty(),
				TextureSlot.ALL);
		singleVariantBlockState(
				BotaniaBlocks.TINY_PLANET, tenByTenAllTemplate.create(
						BotaniaBlocks.TINY_PLANET,
						TextureMapping.cube(BotaniaBlocks.TINY_PLANET), this.modelOutput));
		remainingBlocks.remove(BotaniaBlocks.TINY_PLANET);

		singleVariantBlockState(
				BotaniaBlocks.SPREADER_TURNTABLE, ModelTemplates.CUBE_BOTTOM_TOP.create(
						BotaniaBlocks.SPREADER_TURNTABLE,
						TextureMapping.cubeBottomTop(BotaniaBlocks.SPREADER_TURNTABLE),
						this.modelOutput
				));
		remainingBlocks.remove(BotaniaBlocks.SPREADER_TURNTABLE);

		ResourceLocation[] topTexs = new ResourceLocation[6];
		ResourceLocation[] sideTexs = new ResourceLocation[6];
		ResourceLocation[] topStrippedTexs = new ResourceLocation[6];
		ResourceLocation[] sideStrippedTexs = new ResourceLocation[6];
		ResourceLocation[] sideGlimmeringTexs = new ResourceLocation[6];
		ResourceLocation[] sideGlimmeringStrippedTexs = new ResourceLocation[6];
		ResourceLocation[] logModels = new ResourceLocation[6];
		ResourceLocation[] strippedLogModels = new ResourceLocation[6];

		for (int i = 0; i < 6; i++) {
			String suffix = i == 0 ? "" : "_" + i;
			sideTexs[i] = getBlockTexture(BotaniaBlocks.DREAMWOOD_LOG, suffix);
			topTexs[i] = getBlockTexture(BotaniaBlocks.DREAMWOOD_LOG, "_top");
			sideStrippedTexs[i] = getBlockTexture(BotaniaBlocks.STRIPPED_DREAMWOOD_LOG, suffix);
			topStrippedTexs[i] = getBlockTexture(BotaniaBlocks.STRIPPED_DREAMWOOD_LOG, "_top");
			sideGlimmeringTexs[i] = getBlockTexture(BotaniaBlocks.GLIMMERING_DREAMWOOD_LOG, suffix);
			sideGlimmeringStrippedTexs[i] = getBlockTexture(BotaniaBlocks.STRIPPED_GLIMMERING_DREAMWOOD_LOG, suffix);
			logModels[i] = getModelLocation(BotaniaBlocks.DREAMWOOD, suffix);
			strippedLogModels[i] = getModelLocation(BotaniaBlocks.STRIPPED_DREAMWOOD, suffix);
		}

		pillarWithVariants(remainingBlocks, BotaniaBlocks.DREAMWOOD_LOG, topTexs, sideTexs);
		pillarWithVariants(remainingBlocks, BotaniaBlocks.DREAMWOOD, sideTexs, sideTexs);
		pillarWithVariants(remainingBlocks, BotaniaBlocks.STRIPPED_DREAMWOOD_LOG, topStrippedTexs, sideStrippedTexs);
		pillarWithVariants(remainingBlocks, BotaniaBlocks.STRIPPED_DREAMWOOD, sideStrippedTexs, sideStrippedTexs);
		pillarWithVariants(remainingBlocks, BotaniaBlocks.GLIMMERING_DREAMWOOD_LOG, topTexs, sideGlimmeringTexs);
		pillarWithVariants(remainingBlocks, BotaniaBlocks.GLIMMERING_DREAMWOOD, sideGlimmeringTexs, sideGlimmeringTexs);
		pillarWithVariants(remainingBlocks, BotaniaBlocks.STRIPPED_GLIMMERING_DREAMWOOD_LOG,
				topStrippedTexs, sideGlimmeringStrippedTexs);
		pillarWithVariants(remainingBlocks, BotaniaBlocks.STRIPPED_GLIMMERING_DREAMWOOD,
				sideGlimmeringStrippedTexs, sideGlimmeringStrippedTexs);

		stairsBlockWithVariants(remainingBlocks, BotaniaBlocks.DREAMWOOD_STAIRS, sideTexs, sideTexs, sideTexs);
		stairsBlockWithVariants(remainingBlocks, BotaniaBlocks.STRIPPED_DREAMWOOD_STAIRS,
				sideStrippedTexs, sideStrippedTexs, sideStrippedTexs);
		slabBlockWithVariants(remainingBlocks, BotaniaBlocks.DREAMWOOD_SLAB, logModels, sideTexs, sideTexs, sideTexs);
		slabBlockWithVariants(remainingBlocks, BotaniaBlocks.STRIPPED_DREAMWOOD_SLAB,
				strippedLogModels, sideStrippedTexs, sideStrippedTexs, sideStrippedTexs);
		wallBlockWithVariants(remainingBlocks, BotaniaBlocks.DREAMWOOD_WALL, sideTexs);
		wallBlockWithVariants(remainingBlocks, BotaniaBlocks.STRIPPED_DREAMWOOD_WALL, sideStrippedTexs);

		pillar(remainingBlocks, BotaniaBlocks.LIVINGWOOD_LOG,
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG, "_top"),
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG));
		pillar(remainingBlocks, BotaniaBlocks.LIVINGWOOD,
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG),
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG));
		pillar(remainingBlocks, BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG,
				getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG, "_top"),
				getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG));
		pillar(remainingBlocks, BotaniaBlocks.STRIPPED_LIVINGWOOD,
				getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG),
				getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG));
		pillar(remainingBlocks, BotaniaBlocks.GLIMMERING_LIVINGWOOD_LOG,
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG, "_top"),
				getBlockTexture(BotaniaBlocks.GLIMMERING_LIVINGWOOD_LOG));
		pillar(remainingBlocks, BotaniaBlocks.GLIMMERING_LIVINGWOOD,
				getBlockTexture(BotaniaBlocks.GLIMMERING_LIVINGWOOD_LOG),
				getBlockTexture(BotaniaBlocks.GLIMMERING_LIVINGWOOD_LOG));
		pillar(remainingBlocks, BotaniaBlocks.STRIPPED_GLIMMERING_LIVINGWOOD_LOG,
				getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG, "_top"),
				getBlockTexture(BotaniaBlocks.STRIPPED_GLIMMERING_LIVINGWOOD_LOG));
		pillar(remainingBlocks, BotaniaBlocks.STRIPPED_GLIMMERING_LIVINGWOOD,
				getBlockTexture(BotaniaBlocks.STRIPPED_GLIMMERING_LIVINGWOOD_LOG),
				getBlockTexture(BotaniaBlocks.STRIPPED_GLIMMERING_LIVINGWOOD_LOG));

		pillarAlt(remainingBlocks, BotaniaBlocks.FRAMED_LIVINGWOOD,
				getBlockTexture(BotaniaBlocks.PATTERN_FRAMED_LIVINGWOOD),
				getBlockTexture(BotaniaBlocks.FRAMED_LIVINGWOOD));
		pillarAlt(remainingBlocks, BotaniaBlocks.FRAMED_DREAMWOOD,
				getBlockTexture(BotaniaBlocks.PATTERN_FRAMED_DREAMWOOD),
				getBlockTexture(BotaniaBlocks.FRAMED_DREAMWOOD));

		stairsBlock(remainingBlocks, BotaniaBlocks.LIVINGWOOD_STAIRS,
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG),
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG),
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG));
		stairsBlock(remainingBlocks, BotaniaBlocks.STRIPPED_LIVINGWOOD_STAIRS,
				getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG),
				getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG),
				getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG));
		slabBlock(remainingBlocks, BotaniaBlocks.LIVINGWOOD_SLAB,
				getModelLocation(BotaniaBlocks.LIVINGWOOD),
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG),
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG),
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG));
		slabBlock(remainingBlocks, BotaniaBlocks.STRIPPED_LIVINGWOOD_SLAB,
				getModelLocation(BotaniaBlocks.STRIPPED_LIVINGWOOD),
				getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG),
				getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG),
				getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG));
		wallBlock(remainingBlocks, BotaniaBlocks.LIVINGWOOD_WALL,
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG));
		wallBlock(remainingBlocks, BotaniaBlocks.STRIPPED_LIVINGWOOD_WALL,
				getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG));

		fenceBlock(remainingBlocks, BotaniaBlocks.DREAMWOOD_FENCE, getBlockTexture(BotaniaBlocks.DREAMWOOD_PLANKS));
		fenceGateBlock(remainingBlocks, BotaniaBlocks.DREAMWOOD_FENCE_GATE,
				getBlockTexture(BotaniaBlocks.DREAMWOOD_PLANKS));
		specialDoorBlock(remainingBlocks, BotaniaBlocks.DREAMWOOD_DOOR);
		specialTrapdoorBlock(remainingBlocks, BotaniaBlocks.DREAMWOOD_TRAPDOOR);
		buttonBlock(remainingBlocks, BotaniaBlocks.DREAMWOOD_BUTTON, getBlockTexture(BotaniaBlocks.DREAMWOOD_PLANKS));
		pressurePlateBlock(remainingBlocks, BotaniaBlocks.DREAMWOOD_PRESSURE_PLATE,
				getBlockTexture(BotaniaBlocks.DREAMWOOD_PLANKS));
		sign(remainingBlocks, BotaniaBlocks.DREAMWOOD_PLANKS,
				BotaniaBlocks.DREAMWOOD_SIGN,
				BotaniaBlocks.DREAMWOOD_WALL_SIGN);
		hangingSign(remainingBlocks, BotaniaBlocks.STRIPPED_DREAMWOOD_LOG,
				BotaniaBlocks.DREAMWOOD_HANGING_SIGN,
				BotaniaBlocks.DREAMWOOD_WALL_HANGING_SIGN);

		fenceBlock(remainingBlocks, BotaniaBlocks.LIVINGWOOD_FENCE, getBlockTexture(BotaniaBlocks.LIVINGWOOD_PLANKS));
		fenceGateBlock(remainingBlocks, BotaniaBlocks.LIVINGWOOD_FENCE_GATE,
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_PLANKS));
		specialDoorBlock(remainingBlocks, BotaniaBlocks.LIVINGWOOD_DOOR);
		specialTrapdoorBlock(remainingBlocks, BotaniaBlocks.LIVINGWOOD_TRAPDOOR);
		buttonBlock(remainingBlocks, BotaniaBlocks.LIVINGWOOD_BUTTON, getBlockTexture(BotaniaBlocks.LIVINGWOOD_PLANKS));
		pressurePlateBlock(remainingBlocks, BotaniaBlocks.LIVINGWOOD_PRESSURE_PLATE,
				getBlockTexture(BotaniaBlocks.LIVINGWOOD_PLANKS));
		sign(remainingBlocks, BotaniaBlocks.LIVINGWOOD_PLANKS,
				BotaniaBlocks.LIVINGWOOD_SIGN,
				BotaniaBlocks.LIVINGWOOD_WALL_SIGN);
		hangingSign(remainingBlocks, BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG,
				BotaniaBlocks.LIVINGWOOD_HANGING_SIGN,
				BotaniaBlocks.LIVINGWOOD_WALL_HANGING_SIGN);

		fenceBlock(remainingBlocks, BotaniaBlocks.SHIMMERWOOD_FENCE, getBlockTexture(BotaniaBlocks.SHIMMERWOOD_PLANKS));
		fenceGateBlock(remainingBlocks, BotaniaBlocks.SHIMMERWOOD_FENCE_GATE,
				getBlockTexture(BotaniaBlocks.SHIMMERWOOD_PLANKS));
		buttonBlock(remainingBlocks, BotaniaBlocks.SHIMMERWOOD_BUTTON,
				getBlockTexture(BotaniaBlocks.SHIMMERWOOD_PLANKS));
		pressurePlateBlock(remainingBlocks, BotaniaBlocks.SHIMMERWOOD_PRESSURE_PLATE,
				getBlockTexture(BotaniaBlocks.SHIMMERWOOD_PLANKS));

		wallBlock(remainingBlocks, BotaniaBlocks.CORPOREA_WALL, getBlockTexture(BotaniaBlocks.CORPOREA_BLOCK));
		buttonBlock(remainingBlocks, BotaniaBlocks.CORPOREA_BUTTON, getBlockTexture(BotaniaBlocks.CORPOREA_BLOCK));
		pressurePlateBlock(remainingBlocks, BotaniaBlocks.CORPOREA_PRESSURE_PLATE,
				getBlockTexture(BotaniaBlocks.CORPOREA_BLOCK));

		rotatedMirrored(remainingBlocks, BotaniaBlocks.LIVINGROCK, getBlockTexture(BotaniaBlocks.LIVINGROCK));

		ResourceLocation polishedLivingrockTexture = getBlockTexture(BotaniaBlocks.POLISHED_LIVINGROCK);
		ResourceLocation polishedLivingrockSlabSideTexture = getBlockTexture(BotaniaBlocks.POLISHED_LIVINGROCK_SLAB);
		ResourceLocation polishedLivingrockSlabDoubleModel = ModelTemplates.CUBE_COLUMN.create(
				getModelLocation(BotaniaBlocks.POLISHED_LIVINGROCK_SLAB, "_double"),
				new TextureMapping()
						.put(TextureSlot.SIDE, polishedLivingrockSlabSideTexture)
						.put(TextureSlot.END, polishedLivingrockTexture),
				this.modelOutput
		);
		slabBlock(remainingBlocks, BotaniaBlocks.POLISHED_LIVINGROCK_SLAB,
				polishedLivingrockSlabDoubleModel, polishedLivingrockSlabSideTexture,
				polishedLivingrockTexture, polishedLivingrockTexture);

		var conjurationTexture = getBlockTexture(BotaniaBlocks.CONJURATION_CATALYST);
		var conjurationMirrored = getBlockTexture(BotaniaBlocks.CONJURATION_CATALYST, "_mirrored");
		checkeredBlockWithBlockstate(remainingBlocks, BotaniaBlocks.CONJURATION_CATALYST,
				conjurationTexture, conjurationMirrored);

		// block entities with only particles
		particleOnly(remainingBlocks, BotaniaBlocks.ANIMATED_TORCH, getBlockTexture(Blocks.REDSTONE_TORCH));
		particleOnly(remainingBlocks, BotaniaBlocks.LIVINGWOOD_AVATAR, getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG));
		particleOnly(remainingBlocks, BotaniaBlocks.MANATIDE_BELLOWS, getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG));
		particleOnly(remainingBlocks, BotaniaBlocks.BOTANICAL_BREWERY, getBlockTexture(BotaniaBlocks.LIVINGROCK));
		particleOnly(remainingBlocks, BotaniaBlocks.CORPOREA_INDEX, getBlockTexture(BotaniaBlocks.CORPOREA_BLOCK));
		particleOnly(remainingBlocks, BotaniaBlocks.DETECTOR_LUMINIZER, getBlockTexture(BotaniaBlocks.DETECTOR_LUMINIZER));
		singleVariantBlockState(BotaniaBlocks.FAKE_AIR, new ModelTemplate(Optional.empty(), Optional.empty())
				.create(BotaniaBlocks.FAKE_AIR, new TextureMapping(), this.modelOutput));
		remainingBlocks.remove(BotaniaBlocks.FAKE_AIR);
		particleOnly(remainingBlocks, BotaniaBlocks.FORK_LUMINIZER, getBlockTexture(BotaniaBlocks.FORK_LUMINIZER));
		particleOnly(remainingBlocks, BotaniaBlocks.GAIA_HEAD, getBlockTexture(Blocks.SOUL_SAND));
		particleOnly(remainingBlocks, BotaniaBlocks.GAIA_WALL_HEAD_BLOCK, getBlockTexture(Blocks.SOUL_SAND));
		particleOnly(remainingBlocks, BotaniaBlocks.GAIA_PYLON, getBlockTexture(BotaniaBlocks.ELEMENTIUM_BLOCK));
		particleOnly(remainingBlocks, BotaniaBlocks.HOVERING_HOURGLASS, getBlockTexture(BotaniaBlocks.MANAGLASS));
		particleOnly(remainingBlocks, BotaniaBlocks.LUMINIZER, getBlockTexture(BotaniaBlocks.LUMINIZER));
		particleOnly(remainingBlocks, BotaniaBlocks.MANA_FLAME, ResourceLocation.withDefaultNamespace("block/fire_0"));
		particleOnly(remainingBlocks, BotaniaBlocks.MANA_PYLON, getBlockTexture(BotaniaBlocks.MANASTEEL_BLOCK));
		particleOnly(remainingBlocks, BotaniaBlocks.NATURA_PYLON, getBlockTexture(BotaniaBlocks.TERRASTEEL_BLOCK));
		particleOnly(remainingBlocks, BotaniaBlocks.TERU_TERU_BOZU, getBlockTexture(Blocks.WHITE_WOOL));
		particleOnly(remainingBlocks, BotaniaBlocks.TOGGLE_LUMINIZER, getBlockTexture(BotaniaBlocks.TOGGLE_LUMINIZER));

		// Block groups
		Predicate<Block> flowers = block -> block instanceof SpecialFlowerBlock
				|| block instanceof ShimmeringMushroomBlock
				|| block instanceof BotaniaFlowerBlock;
		ModelTemplate crossTemplate = new ModelTemplate(Optional.of(botaniaRL("block/shapes/cross")), Optional.empty(), TextureSlot.CROSS);
		takeAll(remainingBlocks, flowers).forEach(block -> singleVariantBlockState(block,
				crossTemplate.create(block, TextureMapping.cross(block), this.modelOutput)));

		takeAll(remainingBlocks, block -> block instanceof FlowerMotifBlock).forEach(block -> {
			String name = BuiltInRegistries.BLOCK.getKey(block).getPath().replace("_motif", "");
			singleVariantBlockState(block, crossTemplate.create(block, new TextureMapping()
					.put(TextureSlot.CROSS, botaniaRL("block/" + name)),
					this.modelOutput));
		});

		takeAll(remainingBlocks,
				BotaniaBlocks.CORPOREA_FUNNEL, BotaniaBlocks.CORPOREA_INTERCEPTOR, BotaniaBlocks.CORPOREA_RETAINER)
				.forEach(block -> singleVariantBlockState(block, ModelTemplates.CUBE_COLUMN.create(block,
						TextureMapping.column(getBlockTexture(block, "_side"), getBlockTexture(block, "_end")),
						this.modelOutput)));

		var drumModelTemplate = new ModelTemplate(Optional.of(botaniaRL("block/shapes/drum")), Optional.empty(),
				TextureSlot.TOP, TextureSlot.SIDE);
		takeAll(remainingBlocks,
				BotaniaBlocks.DRUM_OF_THE_GATHERING, BotaniaBlocks.DRUM_OF_THE_CANOPY, BotaniaBlocks.DRUM_OF_THE_WILD)
				.forEach(block -> singleVariantBlockState(block, drumModelTemplate.create(block,
						new TextureMapping()
								.put(TextureSlot.TOP, botaniaRL("block/drum_top"))
								.put(TextureSlot.SIDE, getBlockTexture(block)),
						this.modelOutput)));

		var outsideSlot = TextureSlotAccessor.botania_create("outside");
		var coreSlot = TextureSlotAccessor.botania_create("core");
		var spreaderTemplate = new ModelTemplate(
				Optional.of(botaniaRL("block/shapes/spreader")), Optional.empty(),
				TextureSlot.SIDE, TextureSlot.BACK, TextureSlot.INSIDE, outsideSlot);
		var coveredSpreaderTemplate = new ModelTemplate(
				Optional.of(botaniaRL("block/shapes/spreader_covered")), Optional.empty(),
				TextureSlot.INSIDE, outsideSlot, TextureSlot.WOOL);
		var spreaderCoreTemplate = new ModelTemplate(
				Optional.of(botaniaRL("block/shapes/spreader_core")), Optional.of("_core"),
				coreSlot);
		var spreaderPaddingTemplate = new ModelTemplate(
				Optional.of(botaniaRL("block/shapes/spreader_padding")), Optional.empty(),
				TextureSlot.FRONT, TextureSlot.BACK, TextureSlot.SIDE);
		var spreaderScaffoldingTemplate = new ModelTemplate(
				Optional.of(botaniaRL("block/shapes/spreader_scaffolding")), Optional.of("_scaffolding"),
				TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);
		takeAll(remainingBlocks, ManaSpreaderBlock.class::isInstance).forEach(block -> {
			ManaSpreaderBlock spreaderBlock = (ManaSpreaderBlock) block;
			ManaSpreaderBlock baseBlock = ManaSpreaderBlock.getBaseBlock(spreaderBlock);
			ResourceLocation outside;
			if (baseBlock == BotaniaBlocks.PULSE_MANA_SPREADER || baseBlock == BotaniaBlocks.MANA_SPREADER) {
				outside = getBlockTexture(BotaniaBlocks.LIVINGWOOD_LOG);
			} else if (baseBlock == BotaniaBlocks.ELVEN_MANA_SPREADER) {
				outside = getBlockTexture(BotaniaBlocks.DREAMWOOD_LOG, "_3");
			} else {
				outside = getBlockTexture(baseBlock, "_outside");
			}
			ResourceLocation inside;
			if (baseBlock == BotaniaBlocks.PULSE_MANA_SPREADER || baseBlock == BotaniaBlocks.MANA_SPREADER) {
				inside = getBlockTexture(BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG);
			} else if (baseBlock == BotaniaBlocks.ELVEN_MANA_SPREADER) {
				inside = getBlockTexture(BotaniaBlocks.STRIPPED_DREAMWOOD_LOG, "_3");
			} else {
				inside = getBlockTexture(baseBlock, "_inside");
			}
			DyeColor coverColor = spreaderBlock.getCoverColor();
			if (coverColor != null) {
				if (spreaderBlock.isRainbowRendered()) {
					// Gaia spreader needs to be rendered in two parts to not apply the color effect to the cover
					singleVariantBlockState(block, ModelLocationUtils.getModelLocation(baseBlock));
				} else {
					singleVariantBlockState(block, coveredSpreaderTemplate.create(block, new TextureMapping()
							.put(TextureSlot.INSIDE, inside)
							.put(outsideSlot, outside)
							.put(TextureSlot.WOOL,
									TextureMapping.getBlockTexture(ColorHelper.WOOL_MAP.apply(coverColor))),
							this.modelOutput));
				}
			} else {
				singleVariantBlockState(block, spreaderTemplate.create(block, new TextureMapping()
						.put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
						.put(TextureSlot.BACK, getBlockTexture(block, "_back"))
						.put(TextureSlot.INSIDE, inside)
						.put(outsideSlot, outside), this.modelOutput));
				spreaderCoreTemplate.create(block, new TextureMapping()
						.put(coreSlot, getBlockTexture(block, "_core")), this.modelOutput);
				Block sb = block == BotaniaBlocks.PULSE_MANA_SPREADER ? BotaniaBlocks.MANA_SPREADER : block;
				spreaderScaffoldingTemplate.create(block, new TextureMapping()
						.put(TextureSlot.TOP, getBlockTexture(sb, "_scaffolding_top"))
						.put(TextureSlot.SIDE, getBlockTexture(sb, "_scaffolding_side"))
						.put(TextureSlot.BOTTOM, getBlockTexture(sb, "_scaffolding_bottom")), this.modelOutput);
			}
		});
		ColorHelper.supportedColors().forEach(color -> {
			Block wool = ColorHelper.WOOL_MAP.apply(color);
			spreaderPaddingTemplate.create(botaniaRL("block/" + color.getName() + "_spreader_padding"),
					new TextureMapping()
							.put(TextureSlot.FRONT, getBlockTexture(wool))
							.put(TextureSlot.BACK, getBlockTexture(wool))
							.put(TextureSlot.SIDE, getBlockTexture(wool)),
					this.modelOutput);
		});

		TextureSlot[] manaPoolSlots = new TextureSlot[] {
				TextureSlot.SIDE, TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.INSIDE
		};
		TextureSlot[] manaPoolFullSlots = new TextureSlot[] {
				TextureSlot.SIDE, TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.INSIDE, TextureSlot.CONTENT
		};
		takeAll(remainingBlocks, block -> block instanceof ManaPoolBlock poolBlock && poolBlock.color == null)
				.forEach(block -> {
					Block blockForTexture = block == BotaniaBlocks.FABULOUS_MANA_POOL ? BotaniaBlocks.MANA_POOL : block;
					ResourceLocation side = getBlockTexture(blockForTexture, "_side");
					ResourceLocation top = getBlockTexture(blockForTexture, "_top");
					ResourceLocation bottom = getBlockTexture(blockForTexture, "_bottom");
					ResourceLocation inside = getBlockTexture(blockForTexture, "_inside");
					ResourceLocation blockModelTemplateKey = BuiltInRegistries.BLOCK.getKey(blockForTexture)
							.withPrefix("block/shapes/");
					ModelTemplate template = new ModelTemplate(Optional.of(blockModelTemplateKey), Optional.empty(),
							manaPoolSlots);
					TextureMapping mapping = new TextureMapping()
							.put(TextureSlot.SIDE, side)
							.put(TextureSlot.TOP, top)
							.put(TextureSlot.BOTTOM, bottom)
							.put(TextureSlot.INSIDE, inside);

					singleVariantBlockState(block, template.create(block, mapping, this.modelOutput));

					ResourceLocation blockModelFullTemplateKey = blockModelTemplateKey.withSuffix("_full");
					ModelTemplate fullTemplate = new ModelTemplate(Optional.of(blockModelFullTemplateKey),
							Optional.of("_full"), manaPoolFullSlots);
					fullTemplate.create(block, mapping.put(TextureSlot.CONTENT, botaniaRL("block/mana_water")),
							this.modelOutput);
				});
		takeAll(remainingBlocks, block -> block instanceof ManaPoolBlock poolBlock && poolBlock.color != null)
				.forEach(block -> {
					Block baseBlock = ManaPoolBlock.getUndyedBlock((ManaPoolBlock) block);
					ResourceLocation blockModelTemplateKey = BuiltInRegistries.BLOCK.getKey(baseBlock)
							.withPrefix("block/");
					singleVariantBlockState(block, blockModelTemplateKey);
				});

		takeAll(remainingBlocks, BotaniaBlocks.MANA_PUMP, BotaniaBlocks.TINY_POTATO)
				.forEach(block -> this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(block,
						Variant.variant().with(VariantProperties.MODEL, getModelLocation(block)))
						.with(BlockModelGeneratorsAccessor.botania_createHorizontalFacingDispatch()))
				);

		takeAll(remainingBlocks, BotaniaBlocks.ENDER_OVERSEER, BotaniaBlocks.MANA_DETECTOR).forEach(block -> {
			var offModel = ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(block), this.modelOutput);
			var onModel = ModelTemplates.CUBE_ALL.create(getModelLocation(block, "_powered"),
					TextureMapping.cube(getBlockTexture(block, "_powered")), this.modelOutput);
			this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(block).with(
					PropertyDispatch.property(BlockStateProperties.POWERED)
							.select(false, Variant.variant().with(VariantProperties.MODEL, offModel))
							.select(true, Variant.variant().with(VariantProperties.MODEL, onModel))
			));
		});

		takeAll(remainingBlocks, block -> block instanceof BotaniaGrassBlock).forEach(block -> {
			var model = ModelTemplates.CUBE_BOTTOM_TOP.create(block, new TextureMapping()
					.put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
					.put(TextureSlot.BOTTOM, getBlockTexture(Blocks.DIRT))
					.put(TextureSlot.TOP, getBlockTexture(block, "_top")),
					this.modelOutput
			);
			this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(block, BlockModelGeneratorsAccessor.botania_createRotatedVariants(model)));
		});

		takeAll(remainingBlocks, block -> block instanceof RedStringBlock).forEach(this::redStringBlock);

		takeAll(remainingBlocks, block -> block instanceof TallMysticalFlowerBlock).forEach(block -> {
			var bottom = ModelTemplates.CROSS.create(block, TextureMapping.cross(block), this.modelOutput);
			var top = ModelTemplates.CROSS.create(getModelLocation(block, "_top"),
					TextureMapping.cross(getBlockTexture(block, "_top")), this.modelOutput);
			this.blockStateGenerators.add(
					MultiVariantGenerator.multiVariant(block)
							.with(PropertyDispatch.property(TallFlowerBlock.HALF)
									.select(DoubleBlockHalf.LOWER, Variant.variant().with(VariantProperties.MODEL, bottom))
									.select(DoubleBlockHalf.UPPER, Variant.variant().with(VariantProperties.MODEL, top))
							)
			);
		});

		var mountainTextures = new ResourceLocation[] { getBlockTexture(BotaniaBlocks.GNEISS), getBlockTexture(
				BotaniaBlocks.GNEISS, "_1") };
		var mountainModels = new ResourceLocation[] { getModelLocation(BotaniaBlocks.GNEISS), getModelLocation(
				BotaniaBlocks.GNEISS, "_1") };
		var mountainWeights = new Integer[] { 5, 1 };
		rotatedMirroredWithVariants(remainingBlocks, BotaniaBlocks.GNEISS, mountainTextures, mountainWeights);
		stairsBlockWithVariants(remainingBlocks, BotaniaBlocks.GNEISS_STAIRS, mountainTextures, mountainTextures, mountainTextures, mountainWeights);
		slabBlockWithVariants(remainingBlocks, BotaniaBlocks.GNEISS_SLAB, mountainModels, mountainTextures, mountainTextures, mountainTextures, mountainWeights);
		wallBlockWithVariants(remainingBlocks, BotaniaBlocks.GNEISS_WALL, mountainTextures, mountainWeights);

		var mountainBrickTextures = new ResourceLocation[] {
				getBlockTexture(BotaniaBlocks.GNEISS_BRICKS),
				getBlockTexture(BotaniaBlocks.GNEISS_BRICKS, "_1"),
				getBlockTexture(BotaniaBlocks.GNEISS_BRICKS, "_2"),
				getBlockTexture(BotaniaBlocks.GNEISS_BRICKS, "_3"),
				getBlockTexture(BotaniaBlocks.GNEISS_BRICKS, "_4"),
				getBlockTexture(BotaniaBlocks.GNEISS_BRICKS, "_5")
		};
		var mountainBrickModels = new ResourceLocation[] {
				getModelLocation(BotaniaBlocks.GNEISS_BRICKS),
				getModelLocation(BotaniaBlocks.GNEISS_BRICKS, "_1"),
				getModelLocation(BotaniaBlocks.GNEISS_BRICKS, "_2"),
				getModelLocation(BotaniaBlocks.GNEISS_BRICKS, "_3"),
				getModelLocation(BotaniaBlocks.GNEISS_BRICKS, "_4"),
				getModelLocation(BotaniaBlocks.GNEISS_BRICKS, "_5"),
		};
		cubeAllWithVariants(remainingBlocks, BotaniaBlocks.GNEISS_BRICKS, mountainBrickTextures);
		stairsBlockWithVariants(remainingBlocks, BotaniaBlocks.GNEISS_BRICK_STAIRS,
				mountainBrickTextures, mountainBrickTextures, mountainBrickTextures);
		slabBlockWithVariants(remainingBlocks, BotaniaBlocks.GNEISS_BRICK_SLAB,
				mountainBrickModels, mountainBrickTextures, mountainBrickTextures, mountainBrickTextures);
		wallBlockWithVariants(remainingBlocks, BotaniaBlocks.GNEISS_BRICK_WALL, mountainBrickTextures);

		var taigaTextures = new ResourceLocation[] { getBlockTexture(BotaniaBlocks.LUNITE), getBlockTexture(
				BotaniaBlocks.LUNITE, "_1") };
		var taigaModels = new ResourceLocation[] { getModelLocation(BotaniaBlocks.LUNITE), getModelLocation(
				BotaniaBlocks.LUNITE, "_1") };
		rotatedMirroredWithVariants(remainingBlocks, BotaniaBlocks.LUNITE, taigaTextures);
		stairsBlockWithVariants(remainingBlocks, BotaniaBlocks.LUNITE_STAIRS,
				taigaTextures, taigaTextures, taigaTextures);
		slabBlockWithVariants(remainingBlocks, BotaniaBlocks.LUNITE_SLAB,
				taigaModels, taigaTextures, taigaTextures, taigaTextures);
		wallBlockWithVariants(remainingBlocks, BotaniaBlocks.LUNITE_WALL, taigaTextures);

		var plainsBrickSide = getBlockTexture(BotaniaBlocks.TALC_BRICKS);
		var plainsBrickTop = getBlockTexture(BotaniaBlocks.TALC_BRICKS, "_top");
		pillarAlt(remainingBlocks, BotaniaBlocks.TALC_BRICKS, plainsBrickTop, plainsBrickSide);
		stairsBlock(remainingBlocks, BotaniaBlocks.TALC_BRICK_STAIRS, plainsBrickSide, plainsBrickTop, plainsBrickTop);
		slabBlock(remainingBlocks, BotaniaBlocks.TALC_BRICK_SLAB, getModelLocation(BotaniaBlocks.TALC_BRICKS),
				plainsBrickSide, plainsBrickTop, plainsBrickTop);
		wallBlock(remainingBlocks, BotaniaBlocks.TALC_BRICK_WALL, plainsBrickSide, plainsBrickTop, plainsBrickTop);

		var forestBrickTextures = new ResourceLocation[] { getBlockTexture(BotaniaBlocks.FUCHSITE_BRICKS),
				getBlockTexture(BotaniaBlocks.FUCHSITE_BRICKS, "_1") };
		var forestBrickModels = new ResourceLocation[] { getModelLocation(BotaniaBlocks.FUCHSITE_BRICKS),
				getModelLocation(BotaniaBlocks.FUCHSITE_BRICKS, "_1") };
		var forestBrickWeights = new Integer[] { 2, 1 };
		cubeAllWithVariants(remainingBlocks, BotaniaBlocks.FUCHSITE_BRICKS, forestBrickTextures, forestBrickWeights);
		stairsBlockWithVariants(remainingBlocks, BotaniaBlocks.FUCHSITE_BRICK_STAIRS,
				forestBrickTextures, forestBrickTextures, forestBrickTextures, forestBrickWeights);
		slabBlockWithVariants(remainingBlocks, BotaniaBlocks.FUCHSITE_BRICK_SLAB, forestBrickModels,
				forestBrickTextures, forestBrickTextures, forestBrickTextures, forestBrickWeights);
		wallBlockWithVariants(remainingBlocks, BotaniaBlocks.FUCHSITE_BRICK_WALL, forestBrickTextures, forestBrickWeights);

		var fungalBrickTextures = new ResourceLocation[] { getBlockTexture(BotaniaBlocks.MYCELITE_BRICKS),
				getBlockTexture(BotaniaBlocks.MYCELITE_BRICKS, "_1") };
		var fungalBrickModels = new ResourceLocation[] { getModelLocation(BotaniaBlocks.MYCELITE_BRICKS),
				getModelLocation(BotaniaBlocks.MYCELITE_BRICKS, "_1") };
		cubeAllWithVariants(remainingBlocks, BotaniaBlocks.MYCELITE_BRICKS, fungalBrickTextures);
		stairsBlockWithVariants(remainingBlocks, BotaniaBlocks.MYCELITE_BRICK_STAIRS,
				fungalBrickTextures, fungalBrickTextures, fungalBrickTextures);
		slabBlockWithVariants(remainingBlocks, BotaniaBlocks.MYCELITE_BRICK_SLAB,
				fungalBrickModels, fungalBrickTextures, fungalBrickTextures, fungalBrickTextures);
		wallBlockWithVariants(remainingBlocks, BotaniaBlocks.MYCELITE_BRICK_WALL, fungalBrickTextures);

		var swampBrickTopTextures = new ResourceLocation[] {
				getBlockTexture(BotaniaBlocks.CATACLASITE_BRICKS, "_top"),
				getBlockTexture(BotaniaBlocks.CATACLASITE_BRICKS, "_top_1")
		};
		var swampBrickBottomTextures = new ResourceLocation[] {
				getBlockTexture(BotaniaBlocks.CATACLASITE_BRICKS, "_bottom"),
				getBlockTexture(BotaniaBlocks.CATACLASITE_BRICKS, "_bottom")
		};
		var swampBrickSideTextures = new ResourceLocation[] {
				getBlockTexture(BotaniaBlocks.CATACLASITE_BRICKS),
				getBlockTexture(BotaniaBlocks.CATACLASITE_BRICKS)
		};
		var swampBrickModels = new ResourceLocation[] {
				getModelLocation(BotaniaBlocks.CATACLASITE_BRICKS),
				getModelLocation(BotaniaBlocks.CATACLASITE_BRICKS, "_1")
		};
		directionalPillarWithVariants(remainingBlocks, BotaniaBlocks.CATACLASITE_BRICKS,
				swampBrickTopTextures, swampBrickBottomTextures, swampBrickSideTextures);
		stairsBlockWithVariants(remainingBlocks, BotaniaBlocks.CATACLASITE_BRICK_STAIRS,
				swampBrickSideTextures, swampBrickBottomTextures, swampBrickTopTextures);
		slabBlockWithVariants(remainingBlocks, BotaniaBlocks.CATACLASITE_BRICK_SLAB,
				swampBrickModels, swampBrickSideTextures, swampBrickBottomTextures, swampBrickTopTextures);
		wallBlockWithVariants(remainingBlocks, BotaniaBlocks.CATACLASITE_BRICK_WALL,
				swampBrickSideTextures, swampBrickBottomTextures, swampBrickTopTextures);

		var swampChiseledBrickTopTextures = new ResourceLocation[] {
				getBlockTexture(BotaniaBlocks.CHISELED_CATACLASITE_BRICKS, "_top"),
				getBlockTexture(BotaniaBlocks.CHISELED_CATACLASITE_BRICKS, "_top_1")
		};
		var swampChiseledBrickBottomTextures = new ResourceLocation[] {
				getBlockTexture(BotaniaBlocks.CHISELED_CATACLASITE_BRICKS, "_bottom"),
				getBlockTexture(BotaniaBlocks.CHISELED_CATACLASITE_BRICKS, "_bottom")
		};
		var swampChiseledBrickSideTextures = new ResourceLocation[] {
				getBlockTexture(BotaniaBlocks.CHISELED_CATACLASITE_BRICKS),
				getBlockTexture(BotaniaBlocks.CHISELED_CATACLASITE_BRICKS)
		};
		directionalPillarWithVariants(remainingBlocks, BotaniaBlocks.CHISELED_CATACLASITE_BRICKS,
				swampChiseledBrickTopTextures, swampChiseledBrickBottomTextures, swampChiseledBrickSideTextures);

		var swampCobblestoneTextures = new ResourceLocation[] { getBlockTexture(BotaniaBlocks.COBBLED_CATACLASITE),
				getBlockTexture(BotaniaBlocks.COBBLED_CATACLASITE, "_1") };
		var swampCobblestoneModels = new ResourceLocation[] { getModelLocation(BotaniaBlocks.COBBLED_CATACLASITE),
				getModelLocation(BotaniaBlocks.COBBLED_CATACLASITE, "_1") };
		cubeAllWithVariants(remainingBlocks, BotaniaBlocks.COBBLED_CATACLASITE, swampCobblestoneTextures);
		stairsBlockWithVariants(remainingBlocks, BotaniaBlocks.COBBLED_CATACLASITE_STAIRS,
				swampCobblestoneTextures, swampCobblestoneTextures, swampCobblestoneTextures);
		slabBlockWithVariants(remainingBlocks, BotaniaBlocks.COBBLED_CATACLASITE_SLAB,
				swampCobblestoneModels, swampCobblestoneTextures, swampCobblestoneTextures, swampCobblestoneTextures);
		wallBlockWithVariants(remainingBlocks, BotaniaBlocks.COBBLED_CATACLASITE_WALL, swampCobblestoneTextures);

		var mesaBrick = getBlockTexture(BotaniaBlocks.ROSY_TALC_BRICKS);
		var mesaBrickMirrored = getBlockTexture(BotaniaBlocks.ROSY_TALC_BRICKS, "_mirrored");
		var mesaBrickModel = checkeredBlockWithBlockstate(remainingBlocks, BotaniaBlocks.ROSY_TALC_BRICKS,
				mesaBrick, mesaBrickMirrored);
		checkeredSlabBlock(remainingBlocks, BotaniaBlocks.ROSY_TALC_BRICK_SLAB, mesaBrickModel,
				mesaBrick, mesaBrickMirrored);
		checkeredStairsBlock(remainingBlocks, BotaniaBlocks.ROSY_TALC_BRICK_STAIRS, mesaBrick, mesaBrickMirrored);
		checkeredWallBlock(remainingBlocks, BotaniaBlocks.ROSY_TALC_BRICK_WALL, mesaBrick, mesaBrickMirrored);

		var mesaChiseledBrickSide = getBlockTexture(BotaniaBlocks.CHISELED_ROSY_TALC_BRICKS);
		var mesaChiseledBrickTop = getBlockTexture(BotaniaBlocks.CHISELED_ROSY_TALC_BRICKS, "_top");
		pillarAlt(remainingBlocks, BotaniaBlocks.CHISELED_ROSY_TALC_BRICKS, mesaChiseledBrickTop, mesaChiseledBrickSide);

		// Remaining slabs, stairs, walls are handled automatically.
		for (Block stone : new Block[] {
				BotaniaBlocks.SOLITE, BotaniaBlocks.FUCHSITE, BotaniaBlocks.MYCELITE,
				BotaniaBlocks.ROSY_TALC, BotaniaBlocks.TALC, BotaniaBlocks.CATACLASITE
		}) {
			rotatedMirrored(remainingBlocks, stone, getBlockTexture(stone));
		}

		for (String variant : LibBlockNames.QUARTZ_VARIANTS) {
			ResourceLocation quartzId = botaniaRL(variant);
			Block quartz = BuiltInRegistries.BLOCK.get(quartzId.withSuffix(LibBlockNames.BLOCK_SUFFIX));
			singleVariantBlockState(quartz, ModelTemplates.CUBE_TOP.create(quartz,
					TextureMapping.cubeTop(quartz),
					this.modelOutput));

			ResourceLocation pillarId = quartzId.withSuffix(LibBlockNames.PILLAR_SUFFIX);
			Block pillar = BuiltInRegistries.BLOCK.get(pillarId);
			var pillarModel = ModelTemplates.CUBE_COLUMN.create(pillar,
					TextureMapping.column(getBlockTexture(pillar), getBlockTexture(pillar, "_top")),
					this.modelOutput);
			this.blockStateGenerators.add(BlockModelGeneratorsAccessor.botania_createAxisAlignedPillarBlock(pillar, pillarModel));

			ResourceLocation bricksId = quartzId.withSuffix(LibBlockNames.BRICKS_SUFFIX);
			Block bricks = BuiltInRegistries.BLOCK.get(bricksId);
			singleVariantBlockState(bricks, ModelTemplates.CUBE_ALL.create(bricks, TextureMapping.cube(bricks), this.modelOutput));

			ResourceLocation chiseledId = quartzId.withPrefix(LibBlockNames.CHISELED_PREFIX).withSuffix(LibBlockNames.BLOCK_SUFFIX);
			Block chiseled = BuiltInRegistries.BLOCK.get(chiseledId);
			singleVariantBlockState(chiseled,
					ModelTemplates.CUBE_COLUMN.create(chiseled, new TextureMapping()
							.put(TextureSlot.SIDE, getBlockTexture(chiseled))
							.put(TextureSlot.END, getBlockTexture(chiseled, "_top")), this.modelOutput));

			ResourceLocation smoothId = quartzId.withPrefix(LibBlockNames.SMOOTH_PREFIX);
			Block smooth = BuiltInRegistries.BLOCK.get(smoothId.withSuffix(LibBlockNames.BLOCK_SUFFIX));
			singleVariantBlockState(smooth, ModelTemplates.CUBE_ALL.create(smooth,
					// it's a weird vanilla thing – regular block made of "top" and "side", smooth made of "bottom"
					TextureMapping.cube(getBlockTexture(quartz, "_bottom")),
					this.modelOutput));

			remainingBlocks.remove(quartz);
			remainingBlocks.remove(pillar);
			remainingBlocks.remove(bricks);
			remainingBlocks.remove(chiseled);
			remainingBlocks.remove(smooth);
		}

		takeAll(remainingBlocks, block -> block instanceof BuriedPetalBlock).forEach(block -> {
			DyeColor color = ((BuriedPetalBlock) block).color;
			ResourceLocation wool = ResourceLocation.withDefaultNamespace("block/" + color.getSerializedName() + "_wool");
			particleOnly(remainingBlocks, block, wool);
		});

		var apothecaryTemplate = new ModelTemplate(Optional.of(botaniaRL("block/shapes/petal_apothecary")),
				Optional.empty(), TextureSlot.SIDE, TextureSlot.TOP, TextureSlot.BOTTOM);
		takeAll(remainingBlocks, block -> block instanceof PetalApothecaryBlock)
				.forEach(block -> singleVariantBlockState(block,
						apothecaryTemplate.create(block, new TextureMapping()
								.put(TextureSlot.SIDE, getBlockTexture(block, "_side"))
								.put(TextureSlot.TOP, getBlockTexture(block, "_top"))
								.put(TextureSlot.BOTTOM, getBlockTexture(block, "_bottom")), this.modelOutput))
				);

		takeAll(remainingBlocks,
				block -> block instanceof FloatingFlowerBaseBlock
						|| block instanceof FlowerPotBlock)
				// Models generated by FloatingFlowerModelProvider or PottedPlantModelProvider
				.forEach(block -> singleVariantBlockState(block, getModelLocation(block)));

		takeAll(remainingBlocks, block -> block instanceof IronBarsBlock).forEach(block -> {
			String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
			var mapping = new TextureMapping()
					.put(TextureSlot.EDGE, getBlockTexture(block))
					.put(TextureSlot.PANE, botaniaRL("block/" + name.substring(0, name.length() - "_pane".length())));
			ResourceLocation postModel = ModelTemplates.STAINED_GLASS_PANE_POST.create(block, mapping, this.modelOutput);
			ResourceLocation sideModel = ModelTemplates.STAINED_GLASS_PANE_SIDE.create(block, mapping, this.modelOutput);
			ResourceLocation sideAltModel = ModelTemplates.STAINED_GLASS_PANE_SIDE_ALT.create(block, mapping, this.modelOutput);
			ResourceLocation noSideModel = ModelTemplates.STAINED_GLASS_PANE_NOSIDE.create(block, mapping, this.modelOutput);
			ResourceLocation noSideAltModel = ModelTemplates.STAINED_GLASS_PANE_NOSIDE_ALT.create(block, mapping, this.modelOutput);

			// [VanillaCopy] BlockModelGenerator glass panes
			this.blockStateGenerators.add(MultiPartGenerator.multiPart(block)
					.with(Variant.variant().with(VariantProperties.MODEL, postModel))
					.with(Condition.condition().term(BlockStateProperties.NORTH, true),
							Variant.variant().with(VariantProperties.MODEL, sideModel))
					.with(Condition.condition().term(BlockStateProperties.EAST, true),
							Variant.variant().with(VariantProperties.MODEL, sideModel)
									.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.SOUTH, true),
							Variant.variant().with(VariantProperties.MODEL, sideAltModel))
					.with(Condition.condition().term(BlockStateProperties.WEST, true),
							Variant.variant().with(VariantProperties.MODEL, sideAltModel)
									.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.NORTH, false),
							Variant.variant().with(VariantProperties.MODEL, noSideModel))
					.with(Condition.condition().term(BlockStateProperties.EAST, false),
							Variant.variant().with(VariantProperties.MODEL, noSideAltModel))
					.with(Condition.condition().term(BlockStateProperties.SOUTH, false),
							Variant.variant().with(VariantProperties.MODEL, noSideAltModel)
									.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
					.with(Condition.condition().term(BlockStateProperties.WEST, false),
							Variant.variant().with(VariantProperties.MODEL, noSideModel)
									.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
		});

		takeAll(remainingBlocks, block -> block instanceof StairBlock).forEach(block -> {
			String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
			boolean quartz = name.contains("quartz");
			String tentativeBaseName = name.substring(0, name.length() - LibBlockNames.STAIRS_SUFFIX.length());
			String baseName = tentativeBaseName.endsWith("brick") || tentativeBaseName.endsWith("plank")
					? tentativeBaseName + "s"
					: quartz ? tentativeBaseName + LibBlockNames.BLOCK_SUFFIX : tentativeBaseName;
			boolean smooth = name.contains("smooth");
			if (quartz) {
				if (smooth) {
					var tex = botaniaRL("block/" + baseName.substring("smooth_".length()) + "_bottom");
					stairsBlock(new HashSet<>(), block, tex, tex, tex);
				} else {
					ResourceLocation side = botaniaRL("block/" + baseName + "_side");
					ResourceLocation bottom = botaniaRL("block/" + baseName + "_top");
					ResourceLocation top = botaniaRL("block/" + baseName + "_top");
					stairsBlock(new HashSet<>(), block, side, bottom, top);
				}
			} else {
				var tex = botaniaRL("block/" + baseName);
				stairsBlock(new HashSet<>(), block, tex, tex, tex);
			}
		});

		takeAll(remainingBlocks, block -> block instanceof SlabBlock).forEach(slabBlock -> {
			String name = BuiltInRegistries.BLOCK.getKey(slabBlock).getPath();
			boolean quartz = name.contains("quartz");
			String tentativeBaseName = name.substring(0, name.length() - LibBlockNames.SLAB_SUFFIX.length());
			String baseName = tentativeBaseName.endsWith("brick") || tentativeBaseName.endsWith("plank")
					? tentativeBaseName + "s"
					: quartz ? tentativeBaseName + LibBlockNames.BLOCK_SUFFIX : tentativeBaseName;
			Block base = BuiltInRegistries.BLOCK.get(botaniaRL(baseName));
			boolean smooth = name.contains("smooth");
			if (quartz) {
				if (smooth) {
					var baseTex = botaniaRL("block/" + baseName.substring("smooth_".length()) + "_bottom");
					var doubleModel = getModelLocation(base);
					slabBlock(new HashSet<>(), slabBlock, doubleModel, baseTex, baseTex, baseTex);
				} else {
					var side = getBlockTexture(base, "_side");
					var bottom = getBlockTexture(base, "_top");
					var top = getBlockTexture(base, "_top");
					var doubleModel = getModelLocation(base);
					slabBlock(new HashSet<>(), slabBlock, doubleModel, side, bottom, top);
				}
			} else {
				var baseTex = getBlockTexture(base);
				var doubleModel = getModelLocation(base);
				slabBlock(new HashSet<>(), slabBlock, doubleModel, baseTex, baseTex, baseTex);
			}
		});

		handleStandardBlockModel(remainingBlocks, WallBlock.class, LibBlockNames.WALL_SUFFIX, this::wallBlock);
		handleStandardBlockModel(remainingBlocks, ButtonBlock.class, LibBlockNames.BUTTON_SUFFIX, this::buttonBlock);
		handleStandardBlockModel(remainingBlocks, PressurePlateBlock.class, LibBlockNames.PRESSURE_PLATE_SUFFIX, this::pressurePlateBlock);

		remainingBlocks.forEach(this::cubeAllNoRemove);
	}

	private <T extends Block> void handleStandardBlockModel(Set<Block> remainingBlocks, Class<T> blockClass,
			String suffix, TriConsumer<Set<Block>, Block, ResourceLocation> modelBuilder) {
		takeAll(remainingBlocks, blockClass::isInstance).forEach(block -> {
			String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
			String tentativeBaseName = name.substring(0, name.length() - suffix.length());
			String baseName = tentativeBaseName.endsWith("brick") || tentativeBaseName.endsWith("plank") ? tentativeBaseName + "s" : tentativeBaseName;
			Block base = BuiltInRegistries.BLOCK.get(botaniaRL(baseName));
			if (base == Blocks.AIR) {
				BotaniaAPI.LOGGER.error("Invalid base block name {} for {}", baseName, name);
			}
			var baseTexture = getBlockTexture(base);
			modelBuilder.accept(new HashSet<>(), block, baseTexture);
		});
	}

	protected void particleOnly(Set<Block> blocks, Block block, ResourceLocation particle) {
		singleVariantBlockState(block, ModelTemplates.PARTICLE_ONLY.create(block, TextureMapping.particle(particle), this.modelOutput));
		blocks.remove(block);
	}

	protected void manualModel(Set<Block> blocks, Block block) {
		singleVariantBlockState(block, getModelLocation(block));
		blocks.remove(block);
	}

	protected void stairsBlock(Set<Block> blocks, Block block, ResourceLocation sideTex, ResourceLocation bottomTex,
			ResourceLocation topTex) {
		stairsBlockWithVariants(blocks, block,
				new ResourceLocation[] { sideTex },
				new ResourceLocation[] { bottomTex },
				new ResourceLocation[] { topTex });
	}

	protected void checkeredStairsBlock(Set<Block> blocks, Block block, ResourceLocation texture,
			ResourceLocation mirroredTexture) {
		BiFunction<String, Optional<String>, ModelTemplate> checkeredTemplate =
				(model, suffix) -> new ModelTemplate(
						Optional.of(botaniaRL("block/shapes/" + model)), suffix,
						TextureSlot.SIDE, TextureSlot.NORTH);
		TextureMapping checkeredMapping = new TextureMapping().put(TextureSlot.SIDE, texture).put(TextureSlot.NORTH, mirroredTexture);

		var checkeredStairsModel = checkeredTemplate.apply("stairs_checkered",
				Optional.empty()).create(BotaniaBlocks.ROSY_TALC_BRICK_STAIRS, checkeredMapping, this.modelOutput);
		var checkeredStairsModelRot90 = checkeredTemplate.apply("stairs_checkered_90deg",
				Optional.of("_90deg")).create(BotaniaBlocks.ROSY_TALC_BRICK_STAIRS, checkeredMapping, this.modelOutput);
		var checkeredStairsOuterModel = checkeredTemplate.apply("stairs_outer_checkered",
				Optional.of("_outer")).create(BotaniaBlocks.ROSY_TALC_BRICK_STAIRS, checkeredMapping, this.modelOutput);
		var checkeredStairsOuterModelRot90 = checkeredTemplate.apply("stairs_outer_checkered_90deg",
				Optional.of("_outer_90deg")).create(BotaniaBlocks.ROSY_TALC_BRICK_STAIRS, checkeredMapping, this.modelOutput);
		var checkeredStairsInnerModel = checkeredTemplate.apply("stairs_inner_checkered",
				Optional.of("_inner")).create(BotaniaBlocks.ROSY_TALC_BRICK_STAIRS, checkeredMapping, this.modelOutput);
		var checkeredStairsInnerModelRot90 = checkeredTemplate.apply("stairs_inner_checkered_90deg",
				Optional.of("_inner_90deg")).create(BotaniaBlocks.ROSY_TALC_BRICK_STAIRS, checkeredMapping, this.modelOutput);

		stairsBlockWithModels(blocks, block, checkeredStairsInnerModel, checkeredStairsInnerModelRot90,
				checkeredStairsModel, checkeredStairsModelRot90, checkeredStairsOuterModel, checkeredStairsOuterModelRot90);
	}

	protected void stairsBlockWithVariants(Set<Block> blocks, Block block, ResourceLocation[] sideTextures,
			ResourceLocation[] bottomTextures, ResourceLocation[] topTextures) {
		var weights = new Integer[sideTextures.length];
		Arrays.fill(weights, 1);
		stairsBlockWithVariants(blocks, block, sideTextures, bottomTextures, topTextures, weights);
	}

	protected void stairsBlockWithVariants(Set<Block> blocks, Block block, ResourceLocation[] sideTextures,
			ResourceLocation[] bottomTextures, ResourceLocation[] topTextures, Integer[] weights) {
		int length = sideTextures.length;
		if (length != topTextures.length || length != bottomTextures.length || length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		ResourceLocation[] innerModels = new ResourceLocation[length];
		ResourceLocation[] straightModels = new ResourceLocation[length];
		ResourceLocation[] outerModels = new ResourceLocation[length];
		for (int i = 0; i < length; i++) {
			String suffix = i == 0 ? "" : "_" + i;
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
		stairsBlockWithModels(blocks, block, innerModels, straightModels, outerModels, weights);
	}

	protected void stairsBlockWithModels(Set<Block> blocks, Block block, ResourceLocation[] innerModels,
			ResourceLocation[] straightModels, ResourceLocation[] outerModels, Integer[] weights) {
		stairsBlockWithModels(blocks, block, innerModels, straightModels, outerModels, weights, true);
	}

	protected void stairsBlockWithModels(Set<Block> blocks, Block block, ResourceLocation innerModel,
			ResourceLocation innerModelRot90, ResourceLocation straightModel, ResourceLocation straightModelRot90,
			ResourceLocation outerModel, ResourceLocation outerModelRot90) {
		stairsBlockWithModels(blocks, block,
				new ResourceLocation[] { innerModel },
				new ResourceLocation[] { innerModelRot90 },
				new ResourceLocation[] { straightModel },
				new ResourceLocation[] { straightModelRot90 },
				new ResourceLocation[] { outerModel },
				new ResourceLocation[] { outerModelRot90 },
				new Integer[] { 1 },
				true);
	}

	protected void stairsBlockWithModels(Set<Block> blocks, Block block, ResourceLocation[] innerModels,
			ResourceLocation[] straightModels, ResourceLocation[] outerModels, Integer[] weights, Boolean uvlock) {
		stairsBlockWithModels(blocks, block,
				innerModels, innerModels,
				straightModels, straightModels,
				outerModels, outerModels,
				weights, uvlock);
	}

	protected void stairsBlockWithModels(Set<Block> blocks, Block block, ResourceLocation[] innerModels,
			ResourceLocation[] innerModelsRot90, ResourceLocation[] straightModels,
			ResourceLocation[] straightModelsRot90, ResourceLocation[] outerModels, ResourceLocation[] outerModelsRot90,
			Integer[] weights, Boolean uvlock) {
		int length = innerModels.length;
		if (length != straightModels.length || length != outerModels.length || length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
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

					// Additionally, we're using an alternate model for everything that's rotated 90/270 degrees to be able to
					// have a consistent "checkered" texture for some stairs. Normal stairs just provide the same model twice
					// which makes no impact in the generated blockstate.
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
					boolean rotatedModel = yRot == VariantProperties.Rotation.R90 || yRot == VariantProperties.Rotation.R270;
					ResourceLocation[] models = switch (stairsShape) {
						case STRAIGHT -> rotatedModel ? straightModelsRot90 : straightModels;
						case OUTER_RIGHT, OUTER_LEFT -> rotatedModel ? outerModelsRot90 : outerModels;
						case INNER_RIGHT, INNER_LEFT -> rotatedModel ? innerModelsRot90 : innerModels;
					};
					var indices = IntStream.range(0, length).boxed();
					propertyDispatch.select(direction, half, stairsShape,
							indices.map(i -> maybeUVLock(uvlock, maybeWeight(weights[i], maybeYRot(yRot,
									maybeXRot(xRot, Variant.variant().with(VariantProperties.MODEL, models[i]))
							)))).toList());
				}
			}
		}
		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(block).with(propertyDispatch));
		blocks.remove(block);
	}

	protected void slabBlock(Set<Block> blocks, Block block, ResourceLocation doubleModel, ResourceLocation side,
			ResourceLocation bottom, ResourceLocation top) {
		slabBlockWithVariants(blocks, block,
				new ResourceLocation[] { doubleModel },
				new ResourceLocation[] { side },
				new ResourceLocation[] { bottom },
				new ResourceLocation[] { top });
	}

	protected void checkeredSlabBlock(Set<Block> blocks, Block block, ResourceLocation doubleModel,
			ResourceLocation texture, ResourceLocation mirroredTexture) {
		BiFunction<String, Optional<String>, ModelTemplate> checkeredTemplate =
				(model, suffix) -> new ModelTemplate(
						Optional.of(botaniaRL("block/shapes/" + model)), suffix, TextureSlot.SIDE, TextureSlot.NORTH);
		TextureMapping checkeredMapping = new TextureMapping().put(TextureSlot.SIDE, texture).put(TextureSlot.NORTH, mirroredTexture);

		var slabModel = checkeredTemplate.apply("slab_checkered",
				Optional.empty()).create(BotaniaBlocks.ROSY_TALC_BRICK_SLAB, checkeredMapping, this.modelOutput);
		var slabTopModel = checkeredTemplate.apply("slab_top_checkered",
				Optional.of("_top")).create(BotaniaBlocks.ROSY_TALC_BRICK_SLAB, checkeredMapping, this.modelOutput);
		slabBlockWithModels(blocks, block, slabModel, slabTopModel, doubleModel);
	}

	protected void slabBlockWithVariants(Set<Block> blocks, Block block, ResourceLocation[] doubleModels,
			ResourceLocation[] sideTextures, ResourceLocation[] bottomTextures, ResourceLocation[] topTextures) {
		var weights = new Integer[sideTextures.length];
		Arrays.fill(weights, 1);
		slabBlockWithVariants(blocks, block, doubleModels, sideTextures, bottomTextures, topTextures, weights);
	}

	protected void slabBlockWithVariants(Set<Block> blocks, Block block, ResourceLocation[] doubleModels,
			ResourceLocation[] sideTextures, ResourceLocation[] bottomTextures, ResourceLocation[] topTextures,
			Integer[] weights) {
		int length = sideTextures.length;
		if (length != topTextures.length || length != bottomTextures.length || length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		ResourceLocation[] bottomModels = new ResourceLocation[length];
		ResourceLocation[] topModels = new ResourceLocation[length];
		for (int i = 0; i < length; i++) {
			String suffix = i == 0 ? "" : "_" + i;
			var mapping = new TextureMapping()
					.put(TextureSlot.SIDE, sideTextures[i])
					.put(TextureSlot.BOTTOM, bottomTextures[i])
					.put(TextureSlot.TOP, topTextures[i]);
			ResourceLocation modelIdBottom = getModelLocation(block, suffix);
			ResourceLocation modelIdTop = getModelLocation(block, "_top" + suffix);
			bottomModels[i] = ModelTemplates.SLAB_BOTTOM.create(modelIdBottom, mapping, this.modelOutput);
			topModels[i] = ModelTemplates.SLAB_TOP.create(modelIdTop, mapping, this.modelOutput);
		}
		slabBlockWithModels(blocks, block, bottomModels, topModels, doubleModels, weights);
	}

	protected void slabBlockWithModels(Set<Block> blocks, Block block, ResourceLocation bottomModel,
			ResourceLocation topModel, ResourceLocation doubleModel) {
		slabBlockWithModels(blocks, block,
				new ResourceLocation[] { bottomModel },
				new ResourceLocation[] { topModel },
				new ResourceLocation[] { doubleModel },
				new Integer[] { 1 });
	}

	protected void slabBlockWithModels(Set<Block> blocks, Block block, ResourceLocation[] bottomModels,
			ResourceLocation[] topModels, ResourceLocation[] doubleModels, Integer[] weights) {
		int length = doubleModels.length;
		if (length != topModels.length || length != bottomModels.length || length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		var indicesBottom = IntStream.range(0, length).boxed();
		var indicesTop = IntStream.range(0, length).boxed();
		var indicesDouble = IntStream.range(0, length).boxed();
		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(block).with(
				PropertyDispatch.property(BlockStateProperties.SLAB_TYPE)
						.select(SlabType.BOTTOM, indicesBottom.map(i -> maybeWeight(weights[i],
								Variant.variant().with(VariantProperties.MODEL, bottomModels[i]))).toList())
						.select(SlabType.TOP, indicesTop.map(i -> maybeWeight(weights[i],
								Variant.variant().with(VariantProperties.MODEL, topModels[i]))).toList())
						.select(SlabType.DOUBLE, indicesDouble.map(i -> maybeWeight(weights[i],
								Variant.variant().with(VariantProperties.MODEL, doubleModels[i]))).toList())
		));
		blocks.remove(block);
	}

	protected void wallBlock(Set<Block> blocks, Block block, ResourceLocation texture) {
		wallBlock(blocks, block, texture, texture, texture);
	}

	protected void wallBlock(Set<Block> blocks, Block block, ResourceLocation sideTexture,
			ResourceLocation bottomTexture, ResourceLocation topTexture) {
		wallBlockWithVariants(blocks, block,
				new ResourceLocation[] { sideTexture },
				new ResourceLocation[] { bottomTexture },
				new ResourceLocation[] { topTexture });
	}

	protected void checkeredWallBlock(Set<Block> blocks, Block block, ResourceLocation texture,
			ResourceLocation mirroredTexture) {
		BiFunction<String, Optional<String>, ModelTemplate> checkeredTemplate =
				(model, suffix) -> new ModelTemplate(
						Optional.of(botaniaRL("block/shapes/" + model)), suffix, TextureSlot.SIDE, TextureSlot.NORTH);
		TextureMapping checkeredMapping = new TextureMapping().put(TextureSlot.SIDE, texture).put(TextureSlot.NORTH, mirroredTexture);

		var checkeredWallPostModel = checkeredTemplate.apply("wall_post_checkered",
				Optional.of("_post")).create(BotaniaBlocks.ROSY_TALC_BRICK_WALL, checkeredMapping, this.modelOutput);
		var checkeredWallSideModel = checkeredTemplate.apply("wall_side_checkered",
				Optional.of("_side")).create(BotaniaBlocks.ROSY_TALC_BRICK_WALL, checkeredMapping, this.modelOutput);
		var checkeredWallSideModelRot90 = checkeredTemplate.apply("wall_side_checkered_90deg",
				Optional.of("_side_90deg")).create(BotaniaBlocks.ROSY_TALC_BRICK_WALL, checkeredMapping, this.modelOutput);
		var checkeredWallSideTallModel = checkeredTemplate.apply("wall_side_tall_checkered",
				Optional.of("_side_tall")).create(BotaniaBlocks.ROSY_TALC_BRICK_WALL, checkeredMapping, this.modelOutput);
		var checkeredWallSideTallModelRot90 = checkeredTemplate.apply("wall_side_tall_checkered_90deg",
				Optional.of("_side_tall_90deg")).create(BotaniaBlocks.ROSY_TALC_BRICK_WALL, checkeredMapping, this.modelOutput);

		wallBlockWithModels(blocks, block, checkeredWallPostModel, checkeredWallSideModel, checkeredWallSideModelRot90,
				checkeredWallSideTallModel, checkeredWallSideTallModelRot90);
	}

	protected void wallBlockWithVariants(Set<Block> blocks, Block block, ResourceLocation[] textures) {
		wallBlockWithVariants(blocks, block, textures, textures, textures);
	}

	protected void wallBlockWithVariants(Set<Block> blocks, Block block, ResourceLocation[] textures, Integer[] weights) {
		wallBlockWithVariants(blocks, block, textures, textures, textures, weights);
	}

	protected void wallBlockWithVariants(Set<Block> blocks, Block block, ResourceLocation[] sideTextures,
			ResourceLocation[] bottomTextures, ResourceLocation[] topTextures) {
		var weights = new Integer[sideTextures.length];
		Arrays.fill(weights, 1);
		wallBlockWithVariants(blocks, block, sideTextures, bottomTextures, topTextures, weights);
	}

	protected void wallBlockWithVariants(Set<Block> blocks, Block block, ResourceLocation[] sideTextures,
			ResourceLocation[] bottomTextures, ResourceLocation[] topTextures, Integer[] weights) {
		int length = sideTextures.length;
		if (length != bottomTextures.length && length != topTextures.length && length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		ResourceLocation[] postModels = new ResourceLocation[length];
		ResourceLocation[] lowModels = new ResourceLocation[length];
		ResourceLocation[] tallModels = new ResourceLocation[length];
		for (int i = 0; i < length; i++) {
			String suffix = i == 0 ? "" : "_" + i;
			var mapping = new TextureMapping()
					.put(TextureSlot.WALL, sideTextures[i])
					.put(TextureSlot.BOTTOM, bottomTextures[i])
					.put(TextureSlot.TOP, topTextures[i]);
			ResourceLocation modelIdPost = getModelLocation(block, "_post" + suffix);
			ResourceLocation modelIdLow = getModelLocation(block, "_side" + suffix);
			ResourceLocation modelIdTall = getModelLocation(block, "_side_tall" + suffix);
			var postTemplate = new ModelTemplate(Optional.of(botaniaRL("block/shapes/wall_post")),
					Optional.of("_post"), TextureSlot.WALL, TextureSlot.BOTTOM, TextureSlot.TOP);
			var sideTemplate = new ModelTemplate(Optional.of(botaniaRL("block/shapes/wall_side")),
					Optional.of("_side"), TextureSlot.WALL, TextureSlot.BOTTOM, TextureSlot.TOP);
			var sideTallTemplate = new ModelTemplate(Optional.of(botaniaRL("block/shapes/wall_side_tall")),
					Optional.of("_side_tall"), TextureSlot.WALL, TextureSlot.BOTTOM, TextureSlot.TOP);
			postModels[i] = postTemplate.create(modelIdPost, mapping, this.modelOutput);
			lowModels[i] = sideTemplate.create(modelIdLow, mapping, this.modelOutput);
			tallModels[i] = sideTallTemplate.create(modelIdTall, mapping, this.modelOutput);
		}
		wallBlockWithModels(blocks, block, postModels, lowModels, tallModels, weights);
	}

	protected void wallBlockWithModels(Set<Block> blocks, Block block, ResourceLocation[] postModels,
			ResourceLocation[] lowModels, ResourceLocation[] tallModels, Integer[] weights) {
		wallBlockWithModels(blocks, block, postModels, lowModels, tallModels, weights, true);
	}

	protected void wallBlockWithModels(Set<Block> blocks, Block block, ResourceLocation postModel,
			ResourceLocation lowModel, ResourceLocation lowModelRot90, ResourceLocation tallModel,
			ResourceLocation tallodelRot90) {
		wallBlockWithModels(blocks, block,
				new ResourceLocation[] { postModel },
				new ResourceLocation[] { lowModel },
				new ResourceLocation[] { lowModelRot90 },
				new ResourceLocation[] { tallModel },
				new ResourceLocation[] { tallodelRot90 },
				new Integer[] { 1 },
				true);
	}

	protected void wallBlockWithModels(Set<Block> blocks, Block block, ResourceLocation[] postModels,
			ResourceLocation[] lowModels, ResourceLocation[] tallModels, Integer[] weights, Boolean uvlock) {
		wallBlockWithModels(blocks, block, postModels, lowModels, lowModels, tallModels, tallModels, weights, uvlock);
	}

	protected void wallBlockWithModels(Set<Block> blocks, Block block, ResourceLocation[] postModels,
			ResourceLocation[] lowModels, ResourceLocation[] lowModelsRot90, ResourceLocation[] tallModels,
			ResourceLocation[] tallModelsRot90, Integer[] weights, Boolean uvlock) {
		int length = postModels.length;
		if (length != lowModels.length || length != tallModels.length || length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		var multiPartGenerator = MultiPartGenerator.multiPart(block);
		var indicesPost = IntStream.range(0, length).boxed();
		multiPartGenerator.with(Condition.condition().term(BlockStateProperties.UP, true),
				indicesPost.map(i -> maybeWeight(weights[i],
						Variant.variant().with(VariantProperties.MODEL, postModels[i]))).toArray(Variant[]::new));
		var wallSides = List.of(BlockStateProperties.EAST_WALL, BlockStateProperties.WEST_WALL,
				BlockStateProperties.SOUTH_WALL, BlockStateProperties.NORTH_WALL);

		// We're using an alternate model for everything that's rotated 90/270 degrees to be able to
		// have a consistent "checkered" texture for some walls. Normal walls just provide the same model twice
		// which makes no impact in the generated blockstate.
		for (EnumProperty<WallSide> wallSide : wallSides) {
			VariantProperties.Rotation yRot =
					wallSide == BlockStateProperties.EAST_WALL ? VariantProperties.Rotation.R90
							: wallSide == BlockStateProperties.WEST_WALL ? VariantProperties.Rotation.R270
							: wallSide == BlockStateProperties.SOUTH_WALL ? VariantProperties.Rotation.R180
							: VariantProperties.Rotation.R0;
			boolean rotatedModel = yRot == VariantProperties.Rotation.R90 || yRot == VariantProperties.Rotation.R270;
			var indicesLow = IntStream.range(0, length).boxed();
			var indicesTall = IntStream.range(0, length).boxed();
			multiPartGenerator
					.with(Condition.condition().term(wallSide, WallSide.LOW), indicesLow.map(
							i -> maybeUVLock(uvlock, maybeWeight(weights[i], maybeYRot(yRot,
									Variant.variant().with(VariantProperties.MODEL,
											rotatedModel ? lowModelsRot90[i] : lowModels[i]))))
					).toArray(Variant[]::new))
					.with(Condition.condition().term(wallSide, WallSide.TALL), indicesTall.map(
							i -> maybeUVLock(uvlock, maybeWeight(weights[i], maybeYRot(yRot,
									Variant.variant().with(VariantProperties.MODEL,
											rotatedModel ? tallModelsRot90[i] : tallModels[i]))))
					).toArray(Variant[]::new));
		}
		this.blockStateGenerators.add(multiPartGenerator);
		blocks.remove(block);
	}

	protected void fenceBlock(Set<Block> blocks, Block block, ResourceLocation tex) {
		var mapping = TextureMapping.defaultTexture(tex);
		var postModel = ModelTemplates.FENCE_POST.create(block, mapping, this.modelOutput);
		var sideModel = ModelTemplates.FENCE_SIDE.create(block, mapping, this.modelOutput);
		this.blockStateGenerators.add(BlockModelGeneratorsAccessor.botania_createFence(block, postModel, sideModel));
		blocks.remove(block);
	}

	protected void fenceGateBlock(Set<Block> blocks, Block block, ResourceLocation tex) {
		var mapping = TextureMapping.defaultTexture(tex);
		var openModel = ModelTemplates.FENCE_GATE_OPEN.create(block, mapping, this.modelOutput);
		var closedModel = ModelTemplates.FENCE_GATE_CLOSED.create(block, mapping, this.modelOutput);
		var openWallModel = ModelTemplates.FENCE_GATE_WALL_OPEN.create(block, mapping, this.modelOutput);
		var closedWallModel = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(block, mapping, this.modelOutput);
		this.blockStateGenerators.add(BlockModelGeneratorsAccessor.botania_createFenceGate(block, openModel, closedModel, openWallModel, closedWallModel, false));
		blocks.remove(block);
	}

	protected void specialDoorBlock(Set<Block> blocks, Block doorBlock) {
		TextureMapping texturemapping = BotaniaModelTemplates.doorMapping(doorBlock);
		ResourceLocation bottomLeftModel = BotaniaModelTemplates.DOOR_BOTTOM_LEFT.create(doorBlock, texturemapping, this.modelOutput);
		ResourceLocation bottomLeftOpenModel = BotaniaModelTemplates.DOOR_BOTTOM_LEFT_OPEN.create(doorBlock, texturemapping, this.modelOutput);
		ResourceLocation bottomRightModel = BotaniaModelTemplates.DOOR_BOTTOM_RIGHT.create(doorBlock, texturemapping, this.modelOutput);
		ResourceLocation bottomRightOpenModel = BotaniaModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.create(doorBlock, texturemapping, this.modelOutput);
		ResourceLocation topLeftModel = BotaniaModelTemplates.DOOR_TOP_LEFT.create(doorBlock, texturemapping, this.modelOutput);
		ResourceLocation topLeftOpenModel = BotaniaModelTemplates.DOOR_TOP_LEFT_OPEN.create(doorBlock, texturemapping, this.modelOutput);
		ResourceLocation topRightModel = BotaniaModelTemplates.DOOR_TOP_RIGHT.create(doorBlock, texturemapping, this.modelOutput);
		ResourceLocation topRightOpenModel = BotaniaModelTemplates.DOOR_TOP_RIGHT_OPEN.create(doorBlock, texturemapping, this.modelOutput);
		this.blockStateGenerators.add(BlockModelGeneratorsAccessor.botania_createDoor(doorBlock,
				bottomLeftModel, bottomLeftOpenModel,
				bottomRightModel, bottomRightOpenModel,
				topLeftModel, topLeftOpenModel,
				topRightModel, topRightOpenModel));
		blocks.remove(doorBlock);
	}

	protected void specialTrapdoorBlock(Set<Block> blocks, Block trapdoorBlock) {
		TextureMapping texturemapping = BotaniaModelTemplates.trapdoorMapping(trapdoorBlock);
		ResourceLocation topModel = BotaniaModelTemplates.TRAPDOOR_TOP.create(trapdoorBlock, texturemapping, this.modelOutput);
		ResourceLocation bottomModel = BotaniaModelTemplates.TRAPDOOR_BOTTOM.create(trapdoorBlock, texturemapping, this.modelOutput);
		ResourceLocation openModel = BotaniaModelTemplates.TRAPDOOR_OPEN.create(trapdoorBlock, texturemapping, this.modelOutput);
		this.blockStateGenerators.add(BlockModelGeneratorsAccessor.botania_createOrientableTrapdoor(trapdoorBlock, topModel, bottomModel, openModel));
		blocks.remove(trapdoorBlock);
	}

	protected void sign(Set<Block> blocks, Block particleBlock, Block signBlock, Block wallSignBlock) {
		TextureMapping texturemapping = TextureMapping.particle(particleBlock);
		ResourceLocation resourcelocation = ModelTemplates.PARTICLE_ONLY.create(signBlock, texturemapping, this.modelOutput);
		singleVariantBlockState(signBlock, resourcelocation);
		singleVariantBlockState(wallSignBlock, resourcelocation);
		blocks.remove(signBlock);
		blocks.remove(wallSignBlock);
	}

	protected void hangingSign(Set<Block> blocks, Block particleBlock, Block hangingSignBlock, Block wallHangingSignBlock) {
		TextureMapping texturemapping = TextureMapping.particle(particleBlock);
		ResourceLocation resourcelocation = ModelTemplates.PARTICLE_ONLY.create(hangingSignBlock, texturemapping, this.modelOutput);
		singleVariantBlockState(hangingSignBlock, resourcelocation);
		singleVariantBlockState(wallHangingSignBlock, resourcelocation);
		blocks.remove(hangingSignBlock);
		blocks.remove(wallHangingSignBlock);
	}

	protected void buttonBlock(Set<Block> blocks, Block buttonBlock, ResourceLocation blockTexture) {
		TextureMapping texturemapping = TextureMapping.defaultTexture(blockTexture);
		ResourceLocation unpoweredModel = ModelTemplates.BUTTON.create(buttonBlock, texturemapping, this.modelOutput);
		ResourceLocation poweredModel = ModelTemplates.BUTTON_PRESSED.create(buttonBlock, texturemapping, this.modelOutput);
		this.blockStateGenerators.add(BlockModelGeneratorsAccessor.botania_createButton(buttonBlock, unpoweredModel, poweredModel));
		ModelTemplates.BUTTON_INVENTORY.create(buttonBlock, texturemapping, this.modelOutput);
		blocks.remove(buttonBlock);
	}

	protected void pressurePlateBlock(Set<Block> blocks, Block pressurePlateBlock, ResourceLocation blockTexture) {
		TextureMapping texturemapping = TextureMapping.defaultTexture(blockTexture);
		ResourceLocation unpoweredModel = ModelTemplates.PRESSURE_PLATE_UP.create(pressurePlateBlock, texturemapping, this.modelOutput);
		ResourceLocation poweredModel = ModelTemplates.PRESSURE_PLATE_DOWN.create(pressurePlateBlock, texturemapping, this.modelOutput);
		this.blockStateGenerators.add(BlockModelGeneratorsAccessor.botania_createPreasurePlate(pressurePlateBlock, unpoweredModel, poweredModel));
		blocks.remove(pressurePlateBlock);
	}

	protected void cubeAllNoRemove(Block block) {
		cubeAll(new HashSet<>(), block);
	}

	protected void cubeAll(Set<Block> blocks, Block block) {
		ResourceLocation texture = getBlockTexture(block);
		cubeAllWithVariants(blocks, block, new ResourceLocation[] { texture });
	}

	protected ResourceLocation checkeredBlockWithBlockstate(Set<Block> blocks, Block block, ResourceLocation texture,
			ResourceLocation mirroredTexture) {
		BiFunction<String, Optional<String>, ModelTemplate> checkeredTemplate =
				(model, suffix) -> new ModelTemplate(
						Optional.of(botaniaRL("block/shapes/" + model)), suffix, TextureSlot.SIDE, TextureSlot.NORTH);
		TextureMapping checkeredMapping = new TextureMapping().put(TextureSlot.SIDE, texture).put(TextureSlot.NORTH, mirroredTexture);

		var blockModel = checkeredTemplate.apply("cube_checkered",
				Optional.empty()).create(block, checkeredMapping, this.modelOutput);
		cubeAllWithModels(blocks, block, new ResourceLocation[] { blockModel }, new Integer[] { 1 });
		return blockModel;
	}

	protected void cubeAllWithVariants(Set<Block> blocks, Block block, ResourceLocation[] textures) {
		var weights = new Integer[textures.length];
		Arrays.fill(weights, 1);
		cubeAllWithVariants(blocks, block, textures, weights);
	}

	protected void cubeAllWithVariants(Set<Block> blocks, Block block, ResourceLocation[] textures, Integer[] weights) {
		int length = textures.length;
		if (length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		ResourceLocation[] models = new ResourceLocation[length];
		for (int i = 0; i < length; i++) {
			String suffix = i == 0 ? "" : "_" + i;
			ResourceLocation modelId = getModelLocation(block, suffix);
			models[i] = ModelTemplates.CUBE_ALL.create(modelId, TextureMapping.cube(textures[i]), this.modelOutput);
		}
		cubeAllWithModels(blocks, block, models, weights);
	}

	protected void cubeAllWithModels(Set<Block> blocks, Block block, ResourceLocation[] models, Integer[] weights) {
		int length = models.length;
		if (length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		var indices = IntStream.range(0, length).boxed();
		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(block, indices.map(i -> maybeWeight(weights[i],
				Variant.variant().with(VariantProperties.MODEL, models[i]))).toArray(Variant[]::new)));
		blocks.remove(block);
	}

	protected void singleVariantBlockState(Block block, ResourceLocation model) {
		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, model)));
	}

	protected void rotatedMirrored(Set<Block> blocks, Block block, ResourceLocation texture) {
		rotatedMirroredWithVariants(blocks, block, new ResourceLocation[] { texture });
	}

	protected void rotatedMirroredWithVariants(Set<Block> blocks, Block block, ResourceLocation[] textures) {
		var weights = new Integer[textures.length];
		Arrays.fill(weights, 1);
		rotatedMirroredWithVariants(blocks, block, textures, weights);
	}

	protected void rotatedMirroredWithVariants(Set<Block> blocks, Block block, ResourceLocation[] textures, Integer[] weights) {
		int length = textures.length;
		if (length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		ResourceLocation[] models = new ResourceLocation[length];
		ResourceLocation[] mirroredModels = new ResourceLocation[length];
		for (int i = 0; i < length; i++) {
			String suffix = i == 0 ? "" : "_" + i;
			ResourceLocation modelId = getModelLocation(block, suffix);
			ResourceLocation mirriredModelId = getModelLocation(block, "_mirrored" + suffix);
			models[i] = ModelTemplates.CUBE_ALL.create(modelId, TextureMapping.cube(textures[i]), this.modelOutput);
			mirroredModels[i] = ModelTemplates.CUBE_MIRRORED_ALL.create(mirriredModelId, TextureMapping.cube(textures[i]), this.modelOutput);
		}
		rotatedMirroredWithModels(blocks, block, models, mirroredModels, weights);
	}

	protected void rotatedMirroredWithModels(Set<Block> blocks, Block block, ResourceLocation[] models,
			ResourceLocation[] mirroredModels, Integer[] weights) {
		int length = models.length;
		if (length != mirroredModels.length || length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		var indices = IntStream.range(0, length).boxed();
		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(block, indices.flatMap(i -> Stream.of(
				maybeWeight(weights[i], Variant.variant().with(VariantProperties.MODEL, models[i])),
				maybeWeight(weights[i], Variant.variant().with(VariantProperties.MODEL, mirroredModels[i])),
				maybeWeight(weights[i], Variant.variant()
						.with(VariantProperties.MODEL, models[i])
						.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)),
				maybeWeight(weights[i], Variant.variant()
						.with(VariantProperties.MODEL, mirroredModels[i])
						.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
		)).toArray(Variant[]::new)));
		blocks.remove(block);
	}

	protected void pillar(Set<Block> blocks, Block block, ResourceLocation top, ResourceLocation side) {
		pillarWithVariants(blocks, block, new ResourceLocation[] { top }, new ResourceLocation[] { side });
	}

	protected void pillarWithVariants(Set<Block> blocks, Block block, ResourceLocation[] topTextures,
			ResourceLocation[] sideTextures) {
		var weights = new Integer[topTextures.length];
		Arrays.fill(weights, 1);
		pillarWithVariants(blocks, block, topTextures, sideTextures, weights);
	}

	protected void pillarWithVariants(Set<Block> blocks, Block block, ResourceLocation[] topTextures,
			ResourceLocation[] sideTextures, Integer[] weights) {
		int length = topTextures.length;
		if (length != sideTextures.length || length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		ResourceLocation[] topModels = new ResourceLocation[length];
		ResourceLocation[] horizontalModels = new ResourceLocation[length];
		for (int i = 0; i < length; i++) {
			String suffix = i == 0 ? "" : "_" + i;
			ResourceLocation modelIdTop = getModelLocation(block, suffix);
			ResourceLocation modelIdHorizontal = getModelLocation(block, "_horizontal" + suffix);
			topModels[i] = ModelTemplates.CUBE_COLUMN.create(modelIdTop, TextureMapping.column(sideTextures[i], topTextures[i]), this.modelOutput);
			horizontalModels[i] = ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(modelIdHorizontal, TextureMapping.column(sideTextures[i], topTextures[i]), this.modelOutput);
		}
		pillarWithModels(blocks, block, topModels, horizontalModels, weights);
	}

	protected void pillarWithModels(Set<Block> blocks, Block block, ResourceLocation[] topModels,
			ResourceLocation[] horizontalModels, Integer[] weights) {
		int length = topModels.length;
		if (length != horizontalModels.length || length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		var indicesX = IntStream.range(0, length).boxed();
		var indicesY = IntStream.range(0, length).boxed();
		var indicesZ = IntStream.range(0, length).boxed();
		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(block).with(
				PropertyDispatch.property(BlockStateProperties.AXIS)
						.select(Direction.Axis.Y, indicesX.map(i -> maybeWeight(weights[i], Variant.variant()
								.with(VariantProperties.MODEL, topModels[i]))).toList())
						.select(Direction.Axis.Z, indicesY.map(i -> maybeWeight(weights[i], Variant.variant()
								.with(VariantProperties.MODEL, horizontalModels[i])
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))).toList())
						.select(Direction.Axis.X, indicesZ.map(i -> maybeWeight(weights[i], Variant.variant()
								.with(VariantProperties.MODEL, horizontalModels[i])
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
								.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))).toList())
		));
		blocks.remove(block);
	}

	// Alternative pillar model that rotates and mirrors some additional faces
	protected void pillarAlt(Set<Block> blocks, Block block, ResourceLocation top, ResourceLocation side) {
		pillarAltWithVariants(blocks, block, new ResourceLocation[] { top }, new ResourceLocation[] { side });
	}

	protected void pillarAltWithVariants(Set<Block> blocks, Block block, ResourceLocation[] topTextures,
			ResourceLocation[] sideTextures) {
		int length = topTextures.length;
		if (length != sideTextures.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		ResourceLocation[] topModels = new ResourceLocation[length];
		ResourceLocation[] horizontalXModels = new ResourceLocation[length];
		ResourceLocation[] horizontalZModels = new ResourceLocation[length];
		ModelTemplate horizontalXTemplate = new ModelTemplate(
				Optional.of(botaniaRL("block/shapes/cube_column_horizontal_x")),
				Optional.of("_horizontal_x"), TextureSlot.END, TextureSlot.SIDE);
		ModelTemplate horizontalZTemplate = new ModelTemplate(
				Optional.of(botaniaRL("block/shapes/cube_column_horizontal_z")),
				Optional.of("_horizontal_z"), TextureSlot.END, TextureSlot.SIDE);
		for (int i = 0; i < length; i++) {
			String suffix = i == 0 ? "" : "_" + i;
			ResourceLocation modelIdTop = getModelLocation(block, suffix);
			ResourceLocation modelIdHorizontalX = getModelLocation(block, "_horizontal_x" + suffix);
			ResourceLocation modelIdHorizontalZ = getModelLocation(block, "_horizontal_z" + suffix);
			topModels[i] = ModelTemplates.CUBE_COLUMN.create(modelIdTop,
					TextureMapping.column(sideTextures[i], topTextures[i]), this.modelOutput);
			horizontalXModels[i] = horizontalXTemplate.create(modelIdHorizontalX,
					TextureMapping.column(sideTextures[i], topTextures[i]), this.modelOutput);
			horizontalZModels[i] = horizontalZTemplate.create(modelIdHorizontalZ,
					TextureMapping.column(sideTextures[i], topTextures[i]), this.modelOutput);
		}
		pillarAltWithModels(blocks, block, topModels, horizontalXModels, horizontalZModels);
	}

	protected void pillarAltWithModels(Set<Block> blocks, Block block, ResourceLocation[] yModels,
			ResourceLocation[] xModels, ResourceLocation[] zModels) {
		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(block).with(
				PropertyDispatch.property(BlockStateProperties.AXIS)
						.select(Direction.Axis.Y, Stream.of(yModels)
								.map(rl -> Variant.variant().with(VariantProperties.MODEL, rl)).toList())
						.select(Direction.Axis.X, Stream.of(xModels)
								.map(rl -> Variant.variant().with(VariantProperties.MODEL, rl)).toList())
						.select(Direction.Axis.Z, Stream.of(zModels)
								.map(rl -> Variant.variant().with(VariantProperties.MODEL, rl)).toList())
		));
		blocks.remove(block);
	}

	protected void directionalPillar(Set<Block> blocks, Block block, ResourceLocation top,
			ResourceLocation bottom, ResourceLocation side) {
		directionalPillarWithVariants(blocks, block,
				new ResourceLocation[] { top },
				new ResourceLocation[] { top },
				new ResourceLocation[] { side });
	}

	protected void directionalPillarWithVariants(Set<Block> blocks, Block block, ResourceLocation[] topTextures,
			ResourceLocation[] bottomTextures, ResourceLocation[] sideTextures) {
		var weights = new Integer[topTextures.length];
		Arrays.fill(weights, 1);
		directionalPillarWithVariants(blocks, block, topTextures, bottomTextures, sideTextures, weights);
	}

	protected void directionalPillarWithVariants(Set<Block> blocks, Block block, ResourceLocation[] topTextures,
			ResourceLocation[] bottomTextures, ResourceLocation[] sideTextures, Integer[] weights) {
		int length = topTextures.length;
		if (length != bottomTextures.length || length != sideTextures.length || length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		ResourceLocation[] topModels = new ResourceLocation[length];
		ResourceLocation[] horizontalModels = new ResourceLocation[length];
		ModelTemplate topTemplate = new ModelTemplate(
				Optional.of(botaniaRL("block/shapes/cube_column_directional")), Optional.empty(),
				TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
		ModelTemplate horizontalTemplate = new ModelTemplate(
				Optional.of(botaniaRL("block/shapes/cube_column_directional_horizontal")), Optional.of("_horizontal"),
				TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
		for (int i = 0; i < length; i++) {
			TextureMapping mapping = new TextureMapping()
					.put(TextureSlot.SIDE, sideTextures[i])
					.put(TextureSlot.TOP, topTextures[i])
					.put(TextureSlot.BOTTOM, bottomTextures[i]);
			String suffix = i == 0 ? "" : "_" + i;
			ResourceLocation modelIdTop = getModelLocation(block, suffix);
			ResourceLocation modelIdHorizontal = getModelLocation(block, "_horizontal" + suffix);
			topModels[i] = topTemplate.create(modelIdTop, mapping, this.modelOutput);
			horizontalModels[i] = horizontalTemplate.create(modelIdHorizontal, mapping, this.modelOutput);
		}
		directionalPillarWithModels(blocks, block, topModels, horizontalModels, weights);
	}

	protected void directionalPillarWithModels(Set<Block> blocks, Block block, ResourceLocation[] topModels,
			ResourceLocation[] horizontalModels, Integer[] weights) {
		int length = topModels.length;
		if (length != horizontalModels.length || length != weights.length) {
			throw new IllegalArgumentException("Arrays must have equal length");
		}
		var indicesUp = IntStream.range(0, length).boxed();
		var indicesDown = IntStream.range(0, length).boxed();
		var indicesNorth = IntStream.range(0, length).boxed();
		var indicesSouth = IntStream.range(0, length).boxed();
		var indicesEast = IntStream.range(0, length).boxed();
		var indicesWest = IntStream.range(0, length).boxed();
		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(block).with(
				PropertyDispatch.property(BlockStateProperties.FACING)
						.select(Direction.UP, indicesUp.map(i -> maybeWeight(weights[i], Variant.variant()
								.with(VariantProperties.MODEL, topModels[i]))).toList())
						.select(Direction.DOWN, indicesDown.map(i -> maybeWeight(weights[i], Variant.variant()
								.with(VariantProperties.MODEL, topModels[i])
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))).toList())
						.select(Direction.NORTH, indicesNorth.map(i -> maybeWeight(weights[i], Variant.variant()
								.with(VariantProperties.MODEL, horizontalModels[i]))).toList())
						.select(Direction.SOUTH, indicesSouth.map(i -> maybeWeight(weights[i], Variant.variant()
								.with(VariantProperties.MODEL, horizontalModels[i])
								.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))).toList())
						.select(Direction.EAST, indicesEast.map(i -> maybeWeight(weights[i], Variant.variant()
								.with(VariantProperties.MODEL, horizontalModels[i])
								.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))).toList())
						.select(Direction.WEST, indicesWest.map(i -> maybeWeight(weights[i], Variant.variant()
								.with(VariantProperties.MODEL, horizontalModels[i])
								.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))).toList())
		));
		blocks.remove(block);
	}

	protected <T> Variant withMaybe(VariantProperty<T> property, T value, boolean shouldAdd, Variant variant) {
		if (shouldAdd) {
			variant.with(property, value);
		}
		return variant;
	}

	protected Variant maybeUVLock(Boolean uvlock, Variant variant) {
		return withMaybe(VariantProperties.UV_LOCK, uvlock, uvlock, variant);
	}

	protected Variant maybeWeight(int weight, Variant variant) {
		return withMaybe(VariantProperties.WEIGHT, weight, weight != 1, variant);
	}

	protected Variant maybeXRot(VariantProperties.Rotation rotation, Variant variant) {
		return withMaybe(VariantProperties.X_ROT, rotation, rotation != VariantProperties.Rotation.R0, variant);
	}

	protected Variant maybeYRot(VariantProperties.Rotation rotation, Variant variant) {
		return withMaybe(VariantProperties.Y_ROT, rotation, rotation != VariantProperties.Rotation.R0, variant);
	}

	@SafeVarargs
	@SuppressWarnings("varargs")
	public final <T> Collection<T> takeAll(Set<T> src, T... items) {
		List<T> ret = Arrays.asList(items);
		for (T item : items) {
			if (!src.contains(item)) {
				getLogger().warn("Item {} not found in set", item);
			}
		}
		if (!src.removeAll(ret)) {
			getLogger().warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
		}
		return ret;
	}

	public final <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
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
			getLogger().warn("takeAll predicate yielded nothing", new Throwable());
		}
		return ret;
	}

	protected void redStringBlock(Block block) {
		ResourceLocation selfName = getBlockTexture(block);
		ResourceLocation front = botaniaRL("block/red_stringed_sender");
		var model = ModelTemplates.CUBE_ORIENTABLE.create(block, new TextureMapping()
				.put(TextureSlot.TOP, selfName)
				.put(TextureSlot.FRONT, front)
				.put(TextureSlot.SIDE, selfName), this.modelOutput);
		this.blockStateGenerators.add(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, model))
				.with(BlockModelGeneratorsAccessor.botania_createFacingDispatch()));
	}
}
