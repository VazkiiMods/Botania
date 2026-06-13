/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.Avatar;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.DataComponentHelper;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.network.clientbound.AvatarSkiesRodEffectPacket;
import vazkii.botania.network.clientbound.AvatarSkiesRodUpdatePacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SkiesRodItem extends Item {

	private static final ResourceLocation AVATAR_OVERLAY = ResourceLocation.parse(ResourcesLib.MODEL_AVATAR_TORNADO);

	private static final int FLY_TIME = 20;
	private static final int FALL_MULTIPLIER = 3;
	private static final int MAX_COUNTER = FLY_TIME * FALL_MULTIPLIER;
	private static final int COST = 350;
	public static final int AVATAR_COOLDOWN = 20;

	public SkiesRodItem(Properties props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity ent, int slot, boolean active) {
		if (ent instanceof Player player) {
			boolean preventingDamage = getFlyCounter(stack) > 0;
			boolean held = player.getMainHandItem() == stack || player.getOffhandItem() == stack;

			if (preventingDamage && !isFlying(stack)) {
				setFlyCounter(stack, getFlyCounter(stack) - 1);
			}

			if (getFlyCounter(stack) >= MAX_COUNTER) {
				setFlying(stack, false);
			} else if (isFlying(stack)) {
				if (held) {
					player.fallDistance = 0F;
					double my = ManaItemHandler.instance().hasProficiency(player, stack) ? 1.6 : 1.25;
					Vec3 oldMot = player.getDeltaMovement();
					if (player.isFallFlying()) {
						double boost = my * 1.2;
						Vec3 lookDir = player.getLookAngle();
						player.setDeltaMovement(oldMot.add(
								lookDir.x * 0.1 + (lookDir.x * boost - oldMot.x) * 0.5,
								lookDir.y * 0.1 + (lookDir.y * boost - oldMot.y) * 0.5,
								lookDir.z * 0.1 + (lookDir.z * boost - oldMot.z) * 0.5));
					} else {
						player.setDeltaMovement(new Vec3(oldMot.x(), my, oldMot.z()));
					}

					player.playSound(BotaniaSounds.airRod, 1F, 1F);
					if (getFlyCounter(stack) % 3 == 0) {
						player.gameEvent(GameEvent.FLAP);
					}
					for (int i = 0; i < 5; i++) {
						WispParticleData data = WispParticleData.wisp(0.35F + (float) Math.random() * 0.1F, 0.25F, 0.25F, 0.25F);
						world.addParticle(data, player.getX(), player.getY(), player.getZ(),
								0.2F * (float) (Math.random() - 0.5),
								-0.01F * (float) Math.random(),
								0.2F * (float) (Math.random() - 0.5));
					}
				}

				setFlyCounter(stack, getFlyCounter(stack) + FALL_MULTIPLIER);
				if (getFlyCounter(stack) == MAX_COUNTER) {
					setFlying(stack, false);
				}
			}

			if (preventingDamage) {
				player.fallDistance = 0;
			}
		}
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return getFlyCounter(stack) > 0;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		float frac = 1 - (getFlyCounter(stack) / (float) MAX_COUNTER);
		return Math.round(13 * frac);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		float frac = 1 - (getFlyCounter(stack) / (float) MAX_COUNTER);
		return Mth.hsvToRgb(frac / 3.0F, 1.0F, 1.0F);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		int fly = getFlyCounter(stack);
		if (fly == 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, false)) {
			ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, true);
			setFlying(stack, true);
			player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
			return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
		}

		return InteractionResultHolder.pass(stack);
	}

	public static boolean isFlying(ItemStack stack) {
		return stack.has(BotaniaDataComponents.FLYING);
	}

	private void setFlying(ItemStack stack, boolean flying) {
		DataComponentHelper.setFlag(stack, BotaniaDataComponents.FLYING, flying);
	}

	private int getFlyCounter(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.REMAINING_TICKS, 0);
	}

	private void setFlyCounter(ItemStack stack, int counter) {
		DataComponentHelper.setIntNonZero(stack, BotaniaDataComponents.REMAINING_TICKS, counter);
	}

	public record AvatarBehavior(ItemStack rod, Avatar avatar) implements AvatarWieldable {
		@Override
		public void onAvatarUpdate(ServerLevel world, BlockPos pos, ManaReceiver receiver) {
			long gameTime = world.getGameTime();
			ActivationTimes activationTimes = new ActivationTimes(rod.get(BotaniaDataComponents.LAST_ACTIVATION_TIMES),
					gameTime - AVATAR_COOLDOWN);

			if (receiver.getCurrentMana() >= COST && avatar.isEnabled()) {
				AABB aabb = MathHelper.inflateBoxAround(pos, 5, 3);
				List<ServerPlayer> players = world.getPlayers(
						player -> player.canBeSeenByAnyone() && player.getBoundingBox().intersects(aabb));
				for (Player p : players) {
					if (!p.isShiftKeyDown() && activationTimes.canActivate(p.getUUID())
							&& receiver.getCurrentMana() >= COST) {
						if (p.getDeltaMovement().lengthSqr() > 0.2 * 0.2
								&& p.getDeltaMovement().lengthSqr() < 5 * 5 && p.isFallFlying()) {
							doAvatarElytraBoost(p, world);
							doAvatarMiscEffects(p, receiver);
							activationTimes.setActivationTime(p.getUUID(), gameTime);
						} else if (p.getDeltaMovement().y() > 0.3 && p.getDeltaMovement().y() < 2
								&& !p.isFallFlying()) {
							doAvatarJump(p, world);
							doAvatarMiscEffects(p, receiver);
							avatar.markForPersisting();
						}
					}
				}
			}
			if (activationTimes.hasChanged) {
				DataComponentHelper.setUnlessDefault(rod, BotaniaDataComponents.LAST_ACTIVATION_TIMES,
						activationTimes.uuidMap, Object2LongMaps.emptyMap());
				avatar.markForPersisting();
			}
		}

		@Override
		public ResourceLocation getOverlayResource() {
			return AVATAR_OVERLAY;
		}

		/**
		 * Wrapper around the cooldown map to minimize map allocations.
		 * 
		 * @implNote Internally this uses an {@link Object2LongArrayMap} for two reasons. Firstly, under normal
		 *           circumstances there are never going to be more than a few entries in the map. Secondly, the entries
		 *           that do exist in the map will likely be in ascending order of activation times, which is beneficial
		 *           for detecting the need to remove an entry. (It's likely only going to be one at a time, unless
		 *           multiple players somehow enter elytra boost range in the same tick.)
		 */
		private static class ActivationTimes {
			@Nullable
			public final Map<UUID, Long> immutableUuidMap;
			public Object2LongMap<UUID> uuidMap = Object2LongMaps.emptyMap();
			public boolean hasChanged;

			public ActivationTimes(@Nullable Map<UUID, Long> immutableUuidMap, long cutOffTime) {
				this.immutableUuidMap = immutableUuidMap;
				removeOutdatedEntries(cutOffTime);
			}

			/**
			 * If any entries have a time index on or before the cut-off time, make a copy of the immutable source map
			 * without those entries.
			 * (This method needs to run before any result of {@link #canActivate(UUID)} can be trusted.)
			 */
			private void removeOutdatedEntries(long cutOffTime) {
				if (immutableUuidMap == null) {
					// we currently store no activation times, nothing to do
					return;
				}

				// check if there are any outdated activation times
				for (long time : immutableUuidMap.values()) {
					if (time <= cutOffTime) {
						// we found an outdated activation time
						copyRemainingEntries(immutableUuidMap, cutOffTime);
						break;
					}
				}
			}

			private void copyRemainingEntries(Map<UUID, Long> immutableMap, long cutOffTime) {
				for (Map.Entry<UUID, Long> entry : immutableMap.entrySet()) {
					if (entry.getValue() > cutOffTime) {
						if (uuidMap == Object2LongMaps.EMPTY_MAP) {
							// it's unlikely we will have a lot of entries, so array map is the most efficient option
							uuidMap = new Object2LongArrayMap<>(immutableMap.size());
						}
						uuidMap.put(entry.getKey(), (long) entry.getValue());
					}
				}
				hasChanged = true;
			}

			public void setActivationTime(UUID uuid, long activationTime) {
				if (uuidMap == Object2LongMaps.EMPTY_MAP) {
					// initialize the proper UUID map
					Map<UUID, Long> sourceMap = immutableUuidMap != null ? immutableUuidMap : uuidMap;
					// it's very likely this is the only entry we will add this tick
					uuidMap = new Object2LongArrayMap<>(sourceMap.size() + 1);
					uuidMap.putAll(sourceMap);
				}
				uuidMap.put(uuid, activationTime);
				hasChanged = true;
			}

			public boolean canActivate(UUID uuid) {
				return hasChanged
						? !uuidMap.containsKey(uuid)
						: immutableUuidMap == null || !immutableUuidMap.containsKey(uuid);
			}
		}
	}

	public static void doAvatarElytraBoost(Player p, Level world) {
		Vec3 lookDir = p.getLookAngle();
		double mult = 1.25 * Math.pow(Math.E, -0.5 * p.getDeltaMovement().length());
		p.setDeltaMovement(p.getDeltaMovement().x() + lookDir.x() * mult,
				p.getDeltaMovement().y() + lookDir.y() * mult,
				p.getDeltaMovement().z() + lookDir.z() * mult);

		if (!world.isClientSide) {
			XplatAbstractions.INSTANCE.sendToPlayer(p, new AvatarSkiesRodUpdatePacket(true));
			XplatAbstractions.INSTANCE.sendToTracking(p, new AvatarSkiesRodEffectPacket(true, p.getId()));
		}
	}

	public static void doAvatarJump(Player p, Level world) {
		PlayerHelper.setCurrentImpulseImpactPos(p, 3, p);
		p.setDeltaMovement(p.getDeltaMovement().x(), 2.8, p.getDeltaMovement().z());

		if (!world.isClientSide) {
			XplatAbstractions.INSTANCE.sendToPlayer(p, new AvatarSkiesRodUpdatePacket(false));
			XplatAbstractions.INSTANCE.sendToTracking(p, new AvatarSkiesRodEffectPacket(false, p.getId()));
		}
	}

	private static void doAvatarMiscEffects(Player p, ManaReceiver tile) {
		p.level().playSound(null, p.getX(), p.getY(), p.getZ(), BotaniaSounds.dash, SoundSource.PLAYERS, 1F, 1F);
		p.gameEvent(GameEvent.FLAP);
		tile.receiveMana(-COST);
	}

	@SoftImplement("IItemExtension")
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || reequipAnimation(oldStack, newStack);
	}

	@SoftImplement("FabricItem")
	public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
		return reequipAnimation(oldStack, newStack);
	}

	private boolean reequipAnimation(ItemStack before, ItemStack after) {
		return isFlying(before) != isFlying(after);
	}

}
