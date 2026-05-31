package vazkii.botania.neoforge.internal_caps;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

import net.minecraft.world.entity.item.PrimedTnt;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.attachment.DataIdBase;
import vazkii.botania.common.internal_caps.BotaniaDataAttachments;
import vazkii.botania.common.internal_caps.UnethicalTnt;

import java.util.Map;
import java.util.function.Supplier;

public final class ForgeInternalEntityCapabilities {
	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
			DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, BotaniaAPI.MODID);
	private static final Map<DataIdBase<?>, Supplier<? extends AttachmentType<?>>> FOR_ENTITIES =
			new Reference2ObjectOpenHashMap<>();

	public static <T> void register(DataIdBase<T> id) {
		if (FOR_ENTITIES.containsKey(id)) {
			throw new IllegalArgumentException("Entity capability API ID is already registered: " + id.getId());
		}
		AttachmentType.Builder<T> builder = AttachmentType.builder((Supplier<T>) () -> {
			throw new UnsupportedOperationException(
					"This data attachments must not be created implicitly. Use hasData(), getExistingData(), or getExistingDataOrNull() instead of getData() for it.");
		}).serialize(id.getCodec());
		Supplier<AttachmentType<T>> deferredHolder = ATTACHMENT_TYPES.register(id.getId().getPath(), builder::build);
		FOR_ENTITIES.put(id, deferredHolder);
	}

	public static void init(IEventBus eventBus) {
		BotaniaDataAttachments.registerDataAttachments(ForgeInternalEntityCapabilities::register);
		ATTACHMENT_TYPES.register(eventBus);
	}

	@SuppressWarnings("unchecked")
	public static <T> Supplier<AttachmentType<T>> getAttachmentTypeById(DataIdBase<T> id) {
		return (Supplier<AttachmentType<T>>) FOR_ENTITIES.get(id);
	}

	public static void trackTntSpawning(EntityEvent.EntityConstructing e) {
		if (e.getEntity() instanceof PrimedTnt tnt) {
			UnethicalTnt.addTrackedTntEntity(tnt);
		}
	}

	private ForgeInternalEntityCapabilities() {}
}
