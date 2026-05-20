package vazkii.botania.fabric.internal_caps;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.internal.ItemSource;
import vazkii.botania.common.internal_caps.BotaniaDataAttachments;
import vazkii.botania.common.internal_caps.ItemSources;
import vazkii.botania.common.internal_caps.SingleStack;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class FabricInternalEntityAttachments {

	public static final AttachmentType<Short> ITEM_LIFETIME =
			register(BotaniaDataAttachments.ITEM_LIFETIME, Codec.SHORT);
	public static final AttachmentType<ItemSource> ITEM_SOURCE =
			register(BotaniaDataAttachments.ITEM_SOURCE, ItemSources.CODEC);
	public static final AttachmentType<List<ItemStack>> KEPT_ITEMS =
			register(BotaniaDataAttachments.KEPT_ITEMS, ItemStack.CODEC.listOf());
	public static final AttachmentType<SingleStack> LOONIUM_DROP =
			register(BotaniaDataAttachments.LOONIUM_DROP, SingleStack.CODEC);
	public static final AttachmentType<Unit> SLIME_CHUNK_SPAWNED =
			registerUnit(BotaniaDataAttachments.SLIME_CHUNK_SPAWNED);
	public static final AttachmentType<Unit> SLOW_DESPAWN =
			registerUnit(BotaniaDataAttachments.SLOW_DESPAWN);
	public static final AttachmentType<Integer> SPECTRAL_FLOAT_TICKS =
			register(BotaniaDataAttachments.SPECTRAL_FLOAT_TICKS, Codec.INT);
	public static final AttachmentType<Unit> TIGERSEYE_PACIFIED =
			registerUnit(BotaniaDataAttachments.TIGERSEYE_PACIFIED);
	public static final AttachmentType<Unit> UNETHICAL_TNT =
			registerUnit(BotaniaDataAttachments.UNETHICAL_TNT);

	private static AttachmentType<Unit> registerUnit(ResourceLocation id) {
		return register(id, Unit.CODEC);
	}

	private static <T> AttachmentType<T> register(ResourceLocation id, Codec<T> codec) {
		return AttachmentRegistry.createPersistent(id, codec);
	}

	private static <T> AttachmentType<T> registerSynchronized(ResourceLocation id, Codec<T> codec,
			StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
		return AttachmentRegistry.create(id,
				builder -> builder.persistent(codec).syncWith(streamCodec, AttachmentSyncPredicate.all()));
	}
}
