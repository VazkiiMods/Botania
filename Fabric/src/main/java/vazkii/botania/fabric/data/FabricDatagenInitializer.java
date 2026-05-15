/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.impl.resource.conditions.conditions.AllModsLoadedResourceCondition;
import net.fabricmc.fabric.impl.resource.conditions.conditions.NotResourceCondition;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBannerPatterns;
import vazkii.botania.data.*;
import vazkii.botania.data.loot.BotaniaBlockLoot;
import vazkii.botania.data.loot.BotaniaChestLoot;
import vazkii.botania.data.loot.BotaniaEntityLoot;
import vazkii.botania.data.loot.BotaniaEquipmentLoot;
import vazkii.botania.data.loot.BotaniaGenericLoot;
import vazkii.botania.data.loot.BotaniaGiftLoot;
import vazkii.botania.fabric.data.fabric.FabricBlockTagProvider;
import vazkii.botania.fabric.data.fabric.FabricItemTagProvider;
import vazkii.botania.fabric.data.xplat.BrewProvider;
import vazkii.botania.fabric.data.xplat.ConventionalBiomeTagProvider;
import vazkii.botania.fabric.data.xplat.ConventionalBlockTagProvider;
import vazkii.botania.fabric.data.xplat.ConventionalEntityTypeTagProvider;
import vazkii.botania.fabric.data.xplat.ConventionalFluidTagProvider;
import vazkii.botania.fabric.data.xplat.ConventionalItemTagProvider;
import vazkii.botania.fabric.data.xplat.CraftingRecipeProvider;
import vazkii.botania.fabric.data.xplat.ElvenTradeProvider;
import vazkii.botania.fabric.data.xplat.ManaInfusionProvider;
import vazkii.botania.fabric.data.xplat.OrechidProvider;
import vazkii.botania.fabric.data.xplat.PetalApothecaryProvider;
import vazkii.botania.fabric.data.xplat.PureDaisyProvider;
import vazkii.botania.fabric.data.xplat.RunicAltarProvider;
import vazkii.botania.fabric.data.xplat.SmeltingProvider;
import vazkii.botania.fabric.data.xplat.StonecuttingProvider;
import vazkii.botania.fabric.data.xplat.TerrestrialAgglomerationProvider;

import java.util.List;

import static vazkii.botania.common.BotaniaDamageTypes.*;

public class FabricDatagenInitializer implements DataGeneratorEntrypoint {
	@SuppressWarnings("UnstableApiUsage")
	public static final NotResourceCondition GOG_NOT_LOADED_CONDITION =
			new NotResourceCondition(new AllModsLoadedResourceCondition(List.of(BotaniaAPI.GOG_MODID)));

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		if (System.getProperty("botania.xplat_datagen") != null) {
			configureXplatDatagen(generator.createPack());
		} else {
			configureFabricDatagen(generator.createPack());
		}
	}

	private static void configureFabricDatagen(FabricDataGenerator.Pack pack) {
		BlockTagProvider blockTagProvider = pack.addProvider(FabricBlockTagProvider::new);
		pack.addProvider((output,
				registriesFuture) -> new FabricItemTagProvider(output, registriesFuture, blockTagProvider.contentsGetter()));
	}

	private static void configureXplatDatagen(FabricDataGenerator.Pack pack) {
		pack.addProvider(BotaniaBlockLootWrapper.wrap(BotaniaBlockLoot::new));
		pack.addProvider(LooniumStructureConfigurationProvider::new);
		pack.addProvider(GaiaFightConfigurationProvider::new);
		pack.addProvider(BotaniaSimpleLootWrapper.wrap(BotaniaGenericLoot::new, LootContextParamSets.ALL_PARAMS));
		pack.addProvider(BotaniaSimpleLootWrapper.wrap(BotaniaEquipmentLoot::new, LootContextParamSets.EQUIPMENT));
		pack.addProvider(BotaniaSimpleLootWrapper.wrap(BotaniaEntityLoot::new, LootContextParamSets.ENTITY));
		pack.addProvider(BotaniaSimpleLootWrapper.wrap(BotaniaGiftLoot::new, LootContextParamSets.GIFT));
		pack.addProvider(BotaniaSimpleLootWrapper.wrap(BotaniaChestLoot::new, LootContextParamSets.CHEST));
		BlockTagProvider blockTagProvider = pack.addProvider(BlockTagProvider::new);
		pack.addProvider((output, registriesFuture) -> new ItemTagProvider(
				output, registriesFuture, blockTagProvider.contentsGetter()));
		var conventionalBlockTagProvider = pack.addProvider(ConventionalBlockTagProvider::new);
		pack.addProvider((output, registriesFuture) -> new ConventionalItemTagProvider(
				output, registriesFuture, conventionalBlockTagProvider.contentsGetter()));
		pack.addProvider(ConventionalFluidTagProvider::new);
		pack.addProvider(EntityTagProvider::new);
		pack.addProvider(ConventionalEntityTypeTagProvider::new);
		pack.addProvider(BannerPatternProvider::new);
		pack.addProvider(BannerPatternTagsProvider::new);
		pack.addProvider(ConventionalBiomeTagProvider::new);
		pack.addProvider(BotaniaDynamicRegistryProvider::new);
		pack.addProvider(DamageTypeTagProvider::new);
		pack.addProvider(DataComponentTypeTagProvider::new);
		pack.addProvider(StonecuttingProvider::new);
		pack.addProvider(CraftingRecipeProvider::new);
		pack.addProvider(SmeltingProvider::new);
		pack.addProvider(ElvenTradeProvider::new);
		pack.addProvider(ManaInfusionProvider::new);
		pack.addProvider(PureDaisyProvider::new);
		pack.addProvider(BrewProvider::new);
		pack.addProvider(PetalApothecaryProvider::new);
		pack.addProvider(RunicAltarProvider::new);
		pack.addProvider(TerrestrialAgglomerationProvider::new);
		pack.addProvider(OrechidProvider::new);
		pack.addProvider((PackOutput output) -> new BlockstateProvider(output));
		pack.addProvider((PackOutput output) -> new FloatingFlowerModelProvider(output));
		pack.addProvider((PackOutput output) -> new ItemModelProvider(output));
		pack.addProvider((PackOutput output) -> new PottedPlantModelProvider(output));
		pack.addProvider(AdvancementProvider::create);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder builder) {
		builder.add(Registries.DAMAGE_TYPE, FabricDatagenInitializer::damageTypeBC);
		builder.add(Registries.BANNER_PATTERN, bootstrapContext -> BotaniaBannerPatterns.provideData(bootstrapContext::register));
	}

	protected static void damageTypeBC(BootstrapContext<DamageType> context) {
		context.register(RELIC_DAMAGE, RELIC);
		context.register(PLAYER_ATTACK_ARMOR_PIERCING, PLAYER_AP);
		context.register(KEY_EXPLOSION, KEY);
		context.register(PORTAL_BREAD_EXPLOSION, PORTAL);
	}
}
