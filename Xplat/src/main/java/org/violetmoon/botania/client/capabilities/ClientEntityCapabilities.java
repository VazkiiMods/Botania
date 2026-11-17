/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.capabilities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.MonocleHud;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.capability.registration.ApiIdRegistration;
import vazkii.botania.api.capability.registration.ApiProviderRegistration;
import vazkii.botania.api.capability.registration.EntityRegistrationNoContext;
import vazkii.botania.client.gui.monocle.ItemFrameHud;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.entity.CorporeaSparkEntity;
import vazkii.botania.common.entity.ManaSparkEntity;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ClientEntityCapabilities {

	public static void registerCapabilityTypes(ApiIdRegistration registration) {
		registration.register(MonocleHud.ENTITY_LOOKUP);
		registration.register(WandHUD.ENTITY_LOOKUP);
	}

	@SuppressWarnings("unchecked")
	public static void registerCapabilityProviders(ApiProviderRegistration registration) {
		registration.register(WandHUD.ENTITY_LOOKUP, List.of(
				entityApi(ManaSparkEntity.WandHud::new, BotaniaEntities.MANA_SPARK),
				entityApi(CorporeaSparkEntity.WandHud::new, BotaniaEntities.CORPOREA_SPARK)
		));

		registration.register(MonocleHud.ENTITY_LOOKUP, List.of(
				entityApi(ItemFrameHud::new, EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME)
		));
	}

	public static void registerCapabilityFallbackProviders(ApiProviderRegistration registration) {
		registration.register(MonocleHud.ENTITY_LOOKUP, List.of(
				entityApi(ItemFrameHud::new, ItemFrame.class::isInstance)
		));
	}

	@SuppressWarnings("unchecked")
	public static <A, E extends Entity> EntityRegistrationNoContext<A> entityApi(Function<E, @Nullable A> provider,
			EntityType<? extends E>... entityType) {
		return EntityRegistrationNoContext.forEntities(entity -> provider.apply((E) entity), entityType);
	}

	@SuppressWarnings("unchecked")
	public static <A, E extends Entity> EntityRegistrationNoContext<A> entityApi(Function<E, @Nullable A> provider,
			Predicate<Entity> predicate) {
		return EntityRegistrationNoContext.forEntityPredicate(entity -> provider.apply((E) entity), predicate);
	}

	private ClientEntityCapabilities() {}
}
