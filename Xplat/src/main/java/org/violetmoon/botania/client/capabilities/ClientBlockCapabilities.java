/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.client.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import org.violetmoon.botania.api.block.MonocleHud;
import org.violetmoon.botania.api.block.WandHUD;
import org.violetmoon.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import org.violetmoon.botania.api.capability.registration.ApiIdRegistration;
import org.violetmoon.botania.api.capability.registration.ApiProviderRegistration;
import org.violetmoon.botania.api.mana.PoolOverlayProvider;
import org.violetmoon.botania.client.gui.monocle.ComparatorSettingHud;
import org.violetmoon.botania.client.gui.monocle.DaylightDetectorHud;
import org.violetmoon.botania.client.gui.monocle.RedstonePowerHud;
import org.violetmoon.botania.client.gui.monocle.RepeaterSettingHud;
import org.violetmoon.botania.client.gui.monocle.SculkSensorHud;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.block_entity.AnimatedTorchBlockEntity;
import org.violetmoon.botania.common.block.block_entity.BotaniaBlockEntities;
import org.violetmoon.botania.common.block.block_entity.BotanicalBreweryBlockEntity;
import org.violetmoon.botania.common.block.block_entity.CraftyCrateBlockEntity;
import org.violetmoon.botania.common.block.block_entity.EyeOfTheAncientsBlockEntity;
import org.violetmoon.botania.common.block.block_entity.HoveringHourglassBlockEntity;
import org.violetmoon.botania.common.block.block_entity.ManaEnchanterBlockEntity;
import org.violetmoon.botania.common.block.block_entity.corporea.CorporeaRetainerBlockEntity;
import org.violetmoon.botania.common.block.block_entity.flower.functional.HopperhockBlockEntity;
import org.violetmoon.botania.common.block.block_entity.flower.functional.LooniumBlockEntity;
import org.violetmoon.botania.common.block.block_entity.flower.functional.PollidisiacBlockEntity;
import org.violetmoon.botania.common.block.block_entity.flower.functional.RannuncarpusBlockEntity;
import org.violetmoon.botania.common.block.block_entity.flower.generating.SpectrolusBlockEntity;
import org.violetmoon.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import org.violetmoon.botania.common.block.block_entity.mana.ManaPrismBlockEntity;
import org.violetmoon.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import org.violetmoon.botania.common.block.block_entity.mana.SpreaderTurntableBlockEntity;

import java.util.List;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;
import static org.violetmoon.botania.common.capabilities.BlockCapabilities.*;

public final class ClientBlockCapabilities {
	private static final ResourceLocation ALCHEMY_CATALYST_OVERLAY = botaniaRL("block/alchemy_catalyst_overlay");
	private static final ResourceLocation CONJURATION_CATALYST_OVERLAY = botaniaRL("block/conjuration_catalyst_overlay");

	public static void registerCapabilityTypes(ApiIdRegistration registration) {
		registration.register(MonocleHud.BLOCK_LOOKUP);
		registration.register(WandHUD.BLOCK_LOOKUP);
		registration.register(PoolOverlayProvider.LOOKUP);
	}

	@SuppressWarnings("unchecked")
	public static void registerCapabilityProviders(ApiProviderRegistration registration) {
		registration.register(WandHUD.BLOCK_LOOKUP, List.of(
				blockEntityApi(AnimatedTorchBlockEntity.WandHud::new, BotaniaBlockEntities.ANIMATED_TORCH),
				blockEntityApi(BotanicalBreweryBlockEntity.WandHud::new, BotaniaBlockEntities.BOTANICAL_BREWERY),
				blockEntityApi(CorporeaRetainerBlockEntity.WandHud::new, BotaniaBlockEntities.CORPOREA_RETAINER),
				blockEntityApi(CraftyCrateBlockEntity.WandHud::new, BotaniaBlockEntities.CRAFTY_CRATE),
				blockEntityApi(EyeOfTheAncientsBlockEntity.WandHud::new, BotaniaBlockEntities.EYE_OF_THE_ANCIENTS),
				blockEntityApi(HoveringHourglassBlockEntity.WandHud::new, BotaniaBlockEntities.HOVERING_HOURGLASS),
				blockEntityApi(ManaEnchanterBlockEntity.WandHud::new, BotaniaBlockEntities.MANA_ENCHANTER),
				blockEntityApi(ManaPoolBlockEntity.WandHud::new, BotaniaBlockEntities.MANA_POOL),
				blockEntityApi(ManaPrismBlockEntity.WandHud::new, BotaniaBlockEntities.MANA_PRISM),
				blockEntityApi(ManaSpreaderBlockEntity.WandHud::new, BotaniaBlockEntities.MANA_SPREADER),
				blockEntityApi(SpreaderTurntableBlockEntity.WandHud::new, BotaniaBlockEntities.SPREADER_TURNTABLE),

				// flowers
				blockEntityApi(BindableSpecialFlowerBlockEntity.BindableFlowerWandHud::new,
						// generating flowers
						BotaniaBlockEntities.DANDELIFEON, BotaniaBlockEntities.ENDOFLAME,
						BotaniaBlockEntities.ENTROPINNYUM, BotaniaBlockEntities.GOURMARYLLIS,
						BotaniaBlockEntities.HYDROANGEAS, BotaniaBlockEntities.KEKIMURUS,
						BotaniaBlockEntities.MUNCHDEW, BotaniaBlockEntities.NARSLIMMUS,
						BotaniaBlockEntities.RAFFLOWSIA, BotaniaBlockEntities.ROSA_ARCANA,
						BotaniaBlockEntities.SHULK_ME_NOT, BotaniaBlockEntities.THERMALILY,

						// functional flowers
						BotaniaBlockEntities.AGRICARNATION, BotaniaBlockEntities.AGRICARNATION_PETITE,
						BotaniaBlockEntities.BELLETHORNE, BotaniaBlockEntities.BELLETHORNE_PETITE,
						BotaniaBlockEntities.BUBBELL, BotaniaBlockEntities.BUBBELL_PETITE,
						BotaniaBlockEntities.CLAYCONIA, BotaniaBlockEntities.CLAYCONIA_PETITE,
						BotaniaBlockEntities.DAFFOMILL, BotaniaBlockEntities.DREADTHORN,
						BotaniaBlockEntities.EXOFLAME, BotaniaBlockEntities.FALLEN_KANADE,
						BotaniaBlockEntities.HEISEI_DREAM, BotaniaBlockEntities.HYACIDUS,
						BotaniaBlockEntities.JADED_AMARANTHUS, BotaniaBlockEntities.JIYUULIA,
						BotaniaBlockEntities.JIYUULIA_PETITE, BotaniaBlockEntities.LABELLIA,
						BotaniaBlockEntities.MARIMORPHOSIS, BotaniaBlockEntities.MARIMORPHOSIS_PETITE,
						BotaniaBlockEntities.MEDUMONE, BotaniaBlockEntities.ORECHID,
						BotaniaBlockEntities.ORECHID_IGNEM, BotaniaBlockEntities.SPECTRANTHEMUM,
						BotaniaBlockEntities.TANGLEBERRIE, BotaniaBlockEntities.TANGLEBERRIE_PETITE,
						BotaniaBlockEntities.TIGERSEYE, BotaniaBlockEntities.VINCULOTUS
				),
				blockEntityApi(HopperhockBlockEntity.WandHud::new,
						BotaniaBlockEntities.HOPPERHOCK, BotaniaBlockEntities.HOPPERHOCK_PETITE
				),
				blockEntityApi(LooniumBlockEntity.WandHud::new, BotaniaBlockEntities.LOONIUM),
				blockEntityApi(PollidisiacBlockEntity.WandHud::new, BotaniaBlockEntities.POLLIDISIAC),
				blockEntityApi(RannuncarpusBlockEntity.WandHud::new,
						BotaniaBlockEntities.RANNUNCARPUS, BotaniaBlockEntities.RANNUNCARPUS_PETITE
				),
				blockEntityApi(SpectrolusBlockEntity.WandHud::new, BotaniaBlockEntities.SPECTROLUS)
		));

		registration.register(MonocleHud.BLOCK_LOOKUP, List.of(
				blockApi(ComparatorSettingHud::new, Blocks.COMPARATOR),
				blockApi(DaylightDetectorHud::new, Blocks.DAYLIGHT_DETECTOR),
				blockApi(RepeaterSettingHud::new, Blocks.REPEATER),
				blockApi(SculkSensorHud::new, Blocks.SCULK_SENSOR, Blocks.CALIBRATED_SCULK_SENSOR)
		));

		registration.register(PoolOverlayProvider.LOOKUP, List.of(
				blockApiSelf(BotaniaBlocks.MANA_VOID),
				blockApi(blockState -> PoolOverlayProvider.simple(ALCHEMY_CATALYST_OVERLAY),
						BotaniaBlocks.ALCHEMY_CATALYST
				),
				blockApi(blockState -> PoolOverlayProvider.simple(CONJURATION_CATALYST_OVERLAY),
						BotaniaBlocks.CONJURATION_CATALYST
				)
		));
	}

	public static void registerCapabilityFallbackProviders(ApiProviderRegistration registration) {
		registration.register(MonocleHud.BLOCK_LOOKUP, List.of(
				blockApi(RedstonePowerHud::new, block -> RedstonePowerHud.isApplicable(block.defaultBlockState()))
		));
	}

	private ClientBlockCapabilities() {}
}
