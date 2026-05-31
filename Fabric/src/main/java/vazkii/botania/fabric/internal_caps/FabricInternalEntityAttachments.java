package vazkii.botania.fabric.internal_caps;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import vazkii.botania.api.attachment.DataIdBase;

import java.util.Map;

/**
 * Manages the mapping from Botania's data attachment IDs to Fabric's {@link AttachmentType}.
 */
@SuppressWarnings("UnstableApiUsage")
public final class FabricInternalEntityAttachments {
	private static final Map<DataIdBase<?>, AttachmentType<?>> FOR_ENTITIES = new Reference2ObjectOpenHashMap<>();

	public static <T> void register(DataIdBase<T> id) {
		if (FOR_ENTITIES.containsKey(id)) {
			throw new IllegalArgumentException("Entity capability API ID is already registered: " + id.getId());
		}
		AttachmentType<T> attachmentType = AttachmentRegistry.createPersistent(id.getId(), id.getCodec());
		FOR_ENTITIES.put(id, attachmentType);
	}

	@SuppressWarnings("unchecked")
	public static <T> AttachmentType<T> getAttachmentTypeById(DataIdBase<T> id) {
		return (AttachmentType<T>) FOR_ENTITIES.get(id);
	}

	private FabricInternalEntityAttachments() {}
}
