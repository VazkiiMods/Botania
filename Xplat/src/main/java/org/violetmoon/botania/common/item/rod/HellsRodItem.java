/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import vazkii.botania.api.block.Avatar;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.entity.FlameRingEntity;
import vazkii.botania.common.handler.BotaniaSounds;

public class HellsRodItem extends Item {

	private static final ResourceLocation AVATAR_OVERLAY = ResourceLocation.parse(ResourcesLib.MODEL_AVATAR_FIRE);

	private static final int COST = 900;
	private static final int COOLDOWN = 1200;
	private static final int COOLDOWN_AVATAR = 300;

	public HellsRodItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level level = ctx.getLevel();
		Player player = ctx.getPlayer();
		ItemStack stack = ctx.getItemInHand();
		BlockPos pos = ctx.getClickedPos();

		if (player != null && ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, false)) {
			if (!level.isClientSide()) {
				FlameRingEntity entity = BotaniaEntities.FLAME_RING.create(level);
				if (entity != null) {
					entity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
					level.addFreshEntity(entity);

					player.getCooldowns().addCooldown(this, player.isCreative()
							? 10
							: ManaItemHandler.instance().hasProficiency(player, stack) ? COOLDOWN / 2 : COOLDOWN);
					ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, true);
					level.gameEvent(player, GameEvent.PROJECTILE_SHOOT, pos);
				}
			}
			player.playSound(BotaniaSounds.ROD_OF_THE_HELLS, 1, 1);
		}

		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	public record AvatarBehavior(ItemStack rod, Avatar avatar) implements AvatarWieldable {
		@Override
		public void onAvatarUpdate(ServerLevel level, BlockPos pos, ManaReceiver receiver) {
			if (receiver.getCurrentMana() >= COST && avatar.isEnabled()
					&& getTimeSinceLastActivation(level) >= COOLDOWN_AVATAR) {
				FlameRingEntity entity = BotaniaEntities.FLAME_RING.create(level);
				if (entity == null) {
					return;
				}
				entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				level.addFreshEntity(entity);
				receiver.receiveMana(-COST);
				level.gameEvent(null, GameEvent.PROJECTILE_SHOOT, pos);
				setLastActivationTime(level);
			}
		}

		@Override
		public ResourceLocation getOverlayResource() {
			return AVATAR_OVERLAY;
		}
	}

}
