package vazkii.botania.neoforge.internal_caps;

import com.mojang.serialization.Codec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.GaiaFightParticipant;
import vazkii.botania.api.internal.ItemSource;
import vazkii.botania.common.helper.EthicalTntHelper;
import vazkii.botania.common.internal_caps.*;

import java.util.List;
import java.util.function.Supplier;

public final class ForgeInternalEntityCapabilities {
	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, BotaniaAPI.MODID);

	public static final Supplier<AttachmentType<GaiaFightParticipant>> GAIA_FIGHT_PARTICIPANT =
			register(BotaniaDataAttachments.GAIA_FIGHT_PARTICIPANT, GaiaFightParticipant.CODEC);
	public static final Supplier<AttachmentType<Short>> ITEM_LIFETIME =
			register(BotaniaDataAttachments.ITEM_LIFETIME, Codec.SHORT);
	public static final Supplier<AttachmentType<ItemSource>> ITEM_SOURCE =
			register(BotaniaDataAttachments.ITEM_SOURCE, ItemSources.CODEC);
	public static final Supplier<AttachmentType<List<ItemStack>>> KEPT_ITEMS =
			register(BotaniaDataAttachments.KEPT_ITEMS, ItemStack.CODEC.listOf());
	public static final Supplier<AttachmentType<SingleStack>> LOONIUM_DROP =
			register(BotaniaDataAttachments.LOONIUM_DROP, SingleStack.CODEC);
	public static final Supplier<AttachmentType<Unit>> SLIME_CHUNK_SPAWNED =
			registerUnit(BotaniaDataAttachments.SLIME_CHUNK_SPAWNED);
	public static final Supplier<AttachmentType<Unit>> SLOW_DESPAWN =
			registerUnit(BotaniaDataAttachments.SLOW_DESPAWN);
	public static final Supplier<AttachmentType<Integer>> SPECTRAL_FLOAT_TICKS =
			register(BotaniaDataAttachments.SPECTRAL_FLOAT_TICKS, Codec.INT);
	public static final Supplier<AttachmentType<Unit>> TIGERSEYE_PACIFIED =
			registerUnit(BotaniaDataAttachments.TIGERSEYE_PACIFIED);
	public static final Supplier<AttachmentType<Unit>> UNETHICAL_TNT =
			registerUnit(BotaniaDataAttachments.UNETHICAL_TNT);

	private static Supplier<AttachmentType<Unit>> registerUnit(ResourceLocation id) {
		return register(id, Unit.CODEC);
	}

	private static <T> Supplier<AttachmentType<T>> register(ResourceLocation id, Codec<T> codec) {
		return registerSynchronized(id, codec, null);
	}

	private static <T> Supplier<AttachmentType<T>> registerSynchronized(ResourceLocation id, Codec<T> codec,
			@Nullable StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
		AttachmentType.Builder<T> builder = AttachmentType.builder((Supplier<T>) () -> {
			throw new UnsupportedOperationException(
					"This data attachments must not be created implicitly. Use hasData(), getExistingData(), or getExistingDataOrNull() instead of getData() for it.");
		}).serialize(codec);
		if (streamCodec != null) {
			builder.sync(streamCodec);
		}
		return ATTACHMENT_TYPES.register(id.getPath(), builder::build);
	}

	public static void init(IEventBus eventBus) {
		ATTACHMENT_TYPES.register(eventBus);
	}

	public static void trackTntSpawning(EntityEvent.EntityConstructing e) {
		if (e.getEntity() instanceof PrimedTnt tnt) {
			EthicalTntHelper.addTrackedTntEntity(tnt);
		}
	}

	private ForgeInternalEntityCapabilities() {}
}
