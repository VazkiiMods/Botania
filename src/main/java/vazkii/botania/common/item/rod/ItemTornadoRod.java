/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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

	private static final Identifier avatarOverlay = new Identifier(LibResources.MODEL_AVATAR_TORNADO);

	private static final int FLY_TIME = 20;
	private static final int FALL_MULTIPLIER = 3;
	private static final int MAX_COUNTER = FLY_TIME * FALL_MULTIPLIER;
	private static final int COST = 350;

	private static final String TAG_FLYING = "flying";
	private static final String TAG_FLYCOUNTER = "flyCounter";

	public ItemTornadoRod(Settings props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity ent, int slot, boolean active) {
		if (ent instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) ent;
			boolean damaged = getFlyCounter(stack) > 0;
			boolean held = player.getMainHandStack() == stack || player.getOffHandStack() == stack;

			if (damaged && !isFlying(stack)) {
				setFlyCounter(stack, getFlyCounter(stack) - 1);
			}

			if (getFlyCounter(stack) >= MAX_COUNTER) {
				setFlying(stack, false);
			} else if (isFlying(stack)) {
				if (held) {
					player.fallDistance = 0F;
					double my = IManaProficiencyArmor.hasProficiency(player, stack) ? 1.6 : 1.25;
					Vec3d oldMot = player.getVelocity();
					if (player.isFallFlying()) {
						Vec3d lookDir = player.getRotationVector();
						player.setVelocity(new Vec3d(lookDir.getX() * my, lookDir.getY() * my, lookDir.getZ() * my));
					} else {
						player.setVelocity(new Vec3d(oldMot.getX(), my, oldMot.getZ()));
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

	/* todo 1.16-fabric
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getFlyCounter(stack) > 0;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return getFlyCounter(stack) / (double) MAX_COUNTER;
	}
	*/

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		int fly = getFlyCounter(stack);
		if (fly == 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, false)) {
			ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, true);
			setFlying(stack, true);
			return TypedActionResult.success(stack);
		}

		return TypedActionResult.pass(stack);
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
		World world = te.getWorld();
		Map<UUID, Integer> cooldowns = tile.getBoostCooldowns();

		if (!world.isClient) {
			decAvatarCooldowns(cooldowns);
		}
		if (!world.isClient && tile.getCurrentMana() >= COST && tile.isEnabled()) {
			int range = 5;
			int rangeY = 3;
			List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class,
					new Box(te.getPos().add(-0.5 - range, -0.5 - rangeY, -0.5 - range),
							te.getPos().add(0.5 + range, 0.5 + rangeY, 0.5 + range)));
			for (PlayerEntity p : players) {
				int cooldown = 0;
				if (cooldowns.containsKey(p.getUuid())) {
					cooldown = cooldowns.get(p.getUuid());
				}
				if (!p.isSneaking() && cooldown <= 0) {
					if (p.getVelocity().length() > 0.2 && p.getVelocity().length() < 5 && p.isFallFlying()) {
						doAvatarElytraBoost(p, world);
						doAvatarMiscEffects(p, tile);
						cooldowns.put(p.getUuid(), 20);
						te.markDirty();
					} else if (p.getVelocity().getY() > 0.3 && p.getVelocity().getY() < 2 && !p.isFallFlying()) {
						doAvatarJump(p, world);
						doAvatarMiscEffects(p, tile);
					}
				}
			}
		}
	}

	public static void doAvatarElytraBoost(PlayerEntity p, World world) {
		Vec3d lookDir = p.getRotationVector();
		double mult = 1.25 * Math.pow(Math.E, -0.5 * p.getVelocity().length());
		p.setVelocity(p.getVelocity().getX() + lookDir.getX() * mult,
				p.getVelocity().getY() + lookDir.getY() * mult,
				p.getVelocity().getZ() + lookDir.getZ() * mult);

		if (!world.isClient) {
			PacketAvatarTornadoRod.sendTo((ServerPlayerEntity) p, true);
			PacketBotaniaEffect.sendNearby(p,
					PacketBotaniaEffect.EffectType.AVATAR_TORNADO_BOOST,
					p.getX(), p.getY(), p.getZ(),
					p.getEntityId());
		}
	}

	public static void doAvatarJump(PlayerEntity p, World world) {
		p.setVelocity(p.getVelocity().getX(), 2.8, p.getVelocity().getZ());

		if (!world.isClient) {
			PacketAvatarTornadoRod.sendTo((ServerPlayerEntity) p, false);
			PacketBotaniaEffect.sendNearby(p,
					PacketBotaniaEffect.EffectType.AVATAR_TORNADO_JUMP,
					p.getX(), p.getY(), p.getZ(),
					p.getEntityId()
			);
		}
	}

	private void doAvatarMiscEffects(PlayerEntity p, IAvatarTile tile) {
		p.world.playSound(null, p.getX(), p.getY(), p.getZ(), ModSounds.dash, SoundCategory.PLAYERS, 1F, 1F);
		p.addStatusEffect(new StatusEffectInstance(ModPotions.featherfeet, 100, 0));
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
	public Identifier getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}

}
