/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common;

import org.jetbrains.annotations.ApiStatus;

import vazkii.botania.api.block.EdibleBlockWithEffects;
import vazkii.botania.api.block.ExoflameHeatable;
import vazkii.botania.api.block.HourglassTrigger;
import vazkii.botania.api.block.PhantomInkableBlock;
import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.capability.BlockApiNoContext;
import vazkii.botania.api.capability.BlockApiWithContext;
import vazkii.botania.api.capability.EntityApiNoContext;
import vazkii.botania.api.capability.EntityApiWithContext;
import vazkii.botania.api.capability.ItemApiNoContext;
import vazkii.botania.api.capability.ItemApiWithContext;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.item.BlockProvider;
import vazkii.botania.api.item.CoordBoundItem;
import vazkii.botania.api.item.HourglassMaterial;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaCollisionGhost;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.api.mana.spark.SparkAttachable;

public final class BotaniaCapabilities {
	public static void registerCapabilities(ApiIdRegistration registration) {
		// item capabilities
		registration.register(AvatarWieldable.LOOKUP);
		registration.register(BlockProvider.LOOKUP);
		registration.register(CoordBoundItem.LOOKUP);
		registration.register(HourglassMaterial.LOOKUP);
		registration.register(ManaItem.LOOKUP);
		registration.register(Relic.LOOKUP);

		// block capabilities
		registration.register(EdibleBlockWithEffects.LOOKUP);
		registration.register(ExoflameHeatable.LOOKUP);
		registration.register(HourglassTrigger.LOOKUP);
		registration.register(ManaCollisionGhost.LOOKUP);
		registration.register(ManaReceiver.LOOKUP);
		registration.register(SparkAttachable.LOOKUP);
		registration.register(ManaTrigger.LOOKUP);
		registration.register(Wandable.LOOKUP);
		registration.register(WandBindable.LOOKUP);
		registration.register(PhantomInkableBlock.LOOKUP);
	}

	private BotaniaCapabilities() {}

	@ApiStatus.Internal
	public interface ApiIdRegistration {
		void register(BlockApiNoContext<?> apiId);
		void register(BlockApiWithContext<?, ?> apiId);
		void register(EntityApiNoContext<?> apiId);
		void register(EntityApiWithContext<?, ?> apiId);
		void register(ItemApiNoContext<?> apiId);
		void register(ItemApiWithContext<?, ?> apiId);
	}
}
