package vazkii.botania.common.item.relic;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.item.IRelic;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.advancements.RelicBindTrigger;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.xplat.IXplatAbstractions;

import javax.annotation.Nullable;

import java.util.List;
import java.util.UUID;

public class RelicImpl implements IRelic {
	private static final String TAG_SOULBIND_UUID = "soulbindUUID";

	private final ItemStack stack;
	@Nullable
	private final ResourceLocation advancementId;

	public RelicImpl(ItemStack stack, @Nullable ResourceLocation advancementId) {
		this.stack = stack;
		this.advancementId = advancementId;
	}

	@Override
	public void bindToUUID(UUID uuid) {
		ItemNBTHelper.setString(stack, TAG_SOULBIND_UUID, uuid.toString());
	}

	@Nullable
	@Override
	public UUID getSoulbindUUID() {
		if (ItemNBTHelper.verifyExistance(stack, TAG_SOULBIND_UUID)) {
			try {
				return UUID.fromString(ItemNBTHelper.getString(stack, TAG_SOULBIND_UUID, ""));
			} catch (IllegalArgumentException ex) { // Bad UUID in tag
				ItemNBTHelper.removeEntry(stack, TAG_SOULBIND_UUID);
			}
		}

		return null;
	}

	@Nullable
	@Override
	public ResourceLocation getAdvancement() {
		return advancementId;
	}

	@Override
	public void tickBinding(Player player) {
		if (stack.isEmpty()) {
			return;
		}

		if (getSoulbindUUID() == null) {
			bindToUUID(player.getUUID());
			if (player instanceof ServerPlayer serverPlayer) {
				RelicBindTrigger.INSTANCE.trigger(serverPlayer, stack);
			}
		} else if (!isRightPlayer(player) && player.tickCount % 10 == 0 && shouldDamageWrongPlayer()) {
			player.hurt(damageSource(), 2);
		}
	}

	@Override
	public boolean isRightPlayer(Player player) {
		return player.getUUID().equals(getSoulbindUUID());
	}

	private static DamageSource damageSource() {
		return new DamageSource("botania-relic") {};
	}

	public static void addDefaultTooltip(ItemStack stack, List<Component> tooltip) {
		var relic = IXplatAbstractions.INSTANCE.findRelic(stack);
		if (relic == null) {
			return;
		}
		if (relic.getSoulbindUUID() == null) {
			tooltip.add(new TranslatableComponent("botaniamisc.relicUnbound"));
		} else {
			var player = ClientProxy.INSTANCE.getClientPlayer();
			if (player == null || !relic.isRightPlayer(player)) {
				tooltip.add(new TranslatableComponent("botaniamisc.notYourSagittarius"));
			} else {
				tooltip.add(new TranslatableComponent("botaniamisc.relicSoulbound", player.getName()));
			}
		}
	}
}
