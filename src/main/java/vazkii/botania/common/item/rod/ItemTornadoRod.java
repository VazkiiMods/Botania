/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.network.PacketAvatarTornadoRod;
import vazkii.botania.common.network.PacketBotaniaEffect;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemTornadoRod extends Item implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_TORNADO);

	private static final int FLY_TIME = 20;
	private static final int FALL_MULTIPLIER = 3;
	private static final int MAX_COUNTER = FLY_TIME * FALL_MULTIPLIER;
	private static final int COST = 350;

	private static final String TAG_FLYING = "flying";
	private static final String TAG_FLYCOUNTER = "flyCounter";

	public ItemTornadoRod(Properties props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity ent, int slot, boolean active) {
		if (ent instanceof Player) {
			Player player = (Player) ent;
			boolean damaged = getFlyCounter(stack) > 0;
			boolean held = player.getMainHandItem() == stack || player.getOffhandItem() == stack;

			if (damaged && !isFlying(stack)) {
				setFlyCounter(stack, getFlyCounter(stack) - 1);
			}

			if (getFlyCounter(stack) >= MAX_COUNTER) {
				setFlying(stack, false);
			} else if (isFlying(stack)) {
				if (held) {
					player.fallDistance = 0F;
					double my = IManaProficiencyArmor.hasProficiency(player, stack) ? 1.6 : 1.25;
					Vec3 oldMot = player.getDeltaMovement();
					if (player.isFallFlying()) {
						Vec3 lookDir = player.getLookAngle();
						player.setDeltaMovement(new Vec3(lookDir.x() * my, lookDir.y() * my, lookDir.z() * my));
					} else {
						player.setDeltaMovement(new Vec3(oldMot.x(), my, oldMot.z()));
					}

					player.playSound(ModSounds.airRod, 0.1F, 0.25F);
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

			if (damaged) {
				player.fallDistance = 0;
			}
		}
	}

	@Override
	public boolean isBarVisible(@Nonnull ItemStack stack) {
		return getFlyCounter(stack) > 0;
	}

	@Override
	public int getBarWidth(@Nonnull ItemStack stack) {
		float frac = 1 - (getFlyCounter(stack) / (float) MAX_COUNTER);
		return Math.round(13 * frac);
	}

	@Override
	public int getBarColor(@Nonnull ItemStack stack) {
		float frac = 1 - (getFlyCounter(stack) / (float) MAX_COUNTER);
		return Mth.hsvToRgb(frac / 3.0F, 1.0F, 1.0F);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		int fly = getFlyCounter(stack);
		if (fly == 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, false)) {
			ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, true);
			setFlying(stack, true);
			return InteractionResultHolder.success(stack);
		}

		return InteractionResultHolder.pass(stack);
	}

	public static boolean isFlying(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_FLYING, false);
	}

	private void setFlying(ItemStack stack, boolean flying) {
		ItemNBTHelper.setBoolean(stack, TAG_FLYING, flying);
	}

	private int getFlyCounter(ItemStack stack) {
		return stack.getOrCreateTag().getInt(TAG_FLYCOUNTER);
	}

	private void setFlyCounter(ItemStack stack, int counter) {
		stack.getOrCreateTag().putInt(TAG_FLYCOUNTER, counter);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		BlockEntity te = tile.tileEntity();
		Level world = te.getLevel();
		Map<UUID, Integer> cooldowns = tile.getBoostCooldowns();

		if (!world.isClientSide) {
			decAvatarCooldowns(cooldowns);
		}
		if (!world.isClientSide && tile.getCurrentMana() >= COST && tile.isEnabled()) {
			int range = 5;
			int rangeY = 3;
			List<Player> players = world.getEntitiesOfClass(Player.class,
					new AABB(te.getBlockPos().offset(-0.5 - range, -0.5 - rangeY, -0.5 - range),
							te.getBlockPos().offset(0.5 + range, 0.5 + rangeY, 0.5 + range)));
			for (Player p : players) {
				int cooldown = 0;
				if (cooldowns.containsKey(p.getUUID())) {
					cooldown = cooldowns.get(p.getUUID());
				}
				if (!p.isShiftKeyDown() && cooldown <= 0) {
					if (p.getDeltaMovement().length() > 0.2 && p.getDeltaMovement().length() < 5 && p.isFallFlying()) {
						doAvatarElytraBoost(p, world);
						doAvatarMiscEffects(p, tile);
						cooldowns.put(p.getUUID(), 20);
						te.setChanged();
					} else if (p.getDeltaMovement().y() > 0.3 && p.getDeltaMovement().y() < 2 && !p.isFallFlying()) {
						doAvatarJump(p, world);
						doAvatarMiscEffects(p, tile);
					}
				}
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
			PacketAvatarTornadoRod.sendTo((ServerPlayer) p, true);
			PacketBotaniaEffect.sendNearby(p,
					PacketBotaniaEffect.EffectType.AVATAR_TORNADO_BOOST,
					p.getX(), p.getY(), p.getZ(),
					p.getId());
		}
	}

	public static void doAvatarJump(Player p, Level world) {
		p.setDeltaMovement(p.getDeltaMovement().x(), 2.8, p.getDeltaMovement().z());

		if (!world.isClientSide) {
			PacketAvatarTornadoRod.sendTo((ServerPlayer) p, false);
			PacketBotaniaEffect.sendNearby(p,
					PacketBotaniaEffect.EffectType.AVATAR_TORNADO_JUMP,
					p.getX(), p.getY(), p.getZ(),
					p.getId()
			);
		}
	}

	private void doAvatarMiscEffects(Player p, IAvatarTile tile) {
		p.level.playSound(null, p.getX(), p.getY(), p.getZ(), ModSounds.dash, SoundSource.PLAYERS, 1F, 1F);
		p.addEffect(new MobEffectInstance(ModPotions.featherfeet, 100, 0));
		tile.receiveMana(-COST);
	}

	private void decAvatarCooldowns(Map<UUID, Integer> cooldownTag) {
		for (UUID key : cooldownTag.keySet()) {
			int val = cooldownTag.get(key);
			if (val > 0) {
				cooldownTag.put(key, val - 1);
			} else {
				cooldownTag.remove(key);
			}
		}
	}

	/* todo 1.16-fabric FAPI#860
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
		return newStack.getItem() != this || isFlying(oldStack) != isFlying(newStack);
	}
	*/

	@Override
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}

}
