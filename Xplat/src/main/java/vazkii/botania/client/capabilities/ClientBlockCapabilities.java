/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.capabilities;

import net.minecraft.world.level.block.Blocks;

import vazkii.botania.api.block.MonocleHud;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.capability.registration.ApiIdRegistration;
import vazkii.botania.api.capability.registration.ApiProviderRegistration;
import vazkii.botania.client.gui.monocle.ComparatorSettingHud;
import vazkii.botania.client.gui.monocle.DaylightDetectorHud;
import vazkii.botania.client.gui.monocle.RedstonePowerHud;
import vazkii.botania.client.gui.monocle.RepeaterSettingHud;
import vazkii.botania.client.gui.monocle.SculkSensorHud;
import vazkii.botania.common.block.block_entity.AnimatedTorchBlockEntity;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.BotanicalBreweryBlockEntity;
import vazkii.botania.common.block.block_entity.CraftyCrateBlockEntity;
import vazkii.botania.common.block.block_entity.EyeOfTheAncientsBlockEntity;
import vazkii.botania.common.block.block_entity.HoveringHourglassBlockEntity;
import vazkii.botania.common.block.block_entity.ManaEnchanterBlockEntity;
import vazkii.botania.common.block.block_entity.corporea.CorporeaRetainerBlockEntity;
import vazkii.botania.common.block.block_entity.flower.functional.HopperhockBlockEntity;
import vazkii.botania.common.block.block_entity.flower.functional.LooniumBlockEntity;
import vazkii.botania.common.block.block_entity.flower.functional.PollidisiacBlockEntity;
import vazkii.botania.common.block.block_entity.flower.functional.RannuncarpusBlockEntity;
import vazkii.botania.common.block.block_entity.flower.generating.SpectrolusBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPrismBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import vazkii.botania.common.block.block_entity.mana.SpreaderTurntableBlockEntity;

import java.util.List;

import static vazkii.botania.common.capabilities.BlockCapabilities.*;

public final class ClientBlockCapabilities {

	public static void registerCapabilityTypes(ApiIdRegistration registration) {
		registration.register(MonocleHud.BLOCK_LOOKUP);
		registration.register(WandHUD.BLOCK_LOOKUP);
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
	}

	public static void registerCapabilityFallbackProviders(ApiProviderRegistration registration) {
		registration.register(MonocleHud.BLOCK_LOOKUP, List.of(
				blockApi(RedstonePowerHud::new, block -> RedstonePowerHud.isApplicable(block.defaultBlockState()))
		));
	}

	private ClientBlockCapabilities() {}
}
