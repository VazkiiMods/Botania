/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.internal_caps;

import vazkii.botania.api.attachment.DataIdBase;
import vazkii.botania.api.internal.GaiaFightParticipant;
import vazkii.botania.api.internal.ItemSource;

import java.util.function.Consumer;

public final class BotaniaDataAttachments {

	public static void registerDataAttachments(Consumer<DataIdBase<?>> consumer) {
		// data holders
		consumer.accept(GaiaFightParticipant.HOLDER);
		consumer.accept(ItemLifetime.HOLDER);
		consumer.accept(ItemSource.HOLDER);
		consumer.accept(KeptItems.HOLDER);
		consumer.accept(LooniumDrop.HOLDER);
		consumer.accept(SpectralFloatTicks.HOLDER);

		// marker data
		consumer.accept(PhantomInked.MARKER);
		consumer.accept(SlimeChunkSpawned.MARKER);
		consumer.accept(SlowDespawn.MARKER);
		consumer.accept(TigerseyePacified.MARKER);
		consumer.accept(UnethicalTnt.MARKER);
	}

	private BotaniaDataAttachments() {}
}
