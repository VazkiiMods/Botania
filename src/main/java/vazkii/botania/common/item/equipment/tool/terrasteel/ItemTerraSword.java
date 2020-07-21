/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.terrasteel;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.network.PacketLeftClick;

import java.util.List;

public class ItemTerraSword extends ItemManasteelSword implements ILensEffect {

	private static final int MANA_PER_DAMAGE = 100;

	public ItemTerraSword(Settings props) {
		super(BotaniaAPI.instance().getTerrasteelItemTier(), props);
		MinecraftForge.EVENT_BUS.addListener(this::leftClick);
		MinecraftForge.EVENT_BUS.addListener(this::attackEntity);
	}

	private void leftClick(PlayerInteractEvent.LeftClickEmpty evt) {
		if (!evt.getItemStack().isEmpty()
				&& evt.getItemStack().getItem() == this) {
			PacketHandler.sendToServer(new PacketLeftClick());
		}
	}

	private void attackEntity(AttackEntityEvent evt) {
		if (!evt.getPlayer().world.isClient) {
			trySpawnBurst(evt.getPlayer());
		}
	}

	public void trySpawnBurst(PlayerEntity player) {
		if (!player.getMainHandStack().isEmpty()
				&& player.getMainHandStack().getItem() == this
				&& player.getAttackCooldownProgress(0) == 1) {
			EntityManaBurst burst = getBurst(player, player.getMainHandStack());
			player.world.spawnEntity(burst);
			ToolCommons.damageItem(player.getMainHandStack(), 1, player, MANA_PER_DAMAGE);
			player.world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.terraBlade, SoundCategory.PLAYERS, 0.4F, 1.4F);
		}
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	public EntityManaBurst getBurst(PlayerEntity player, ItemStack stack) {
		EntityManaBurst burst = new EntityManaBurst(player);

		float motionModifier = 7F;

		burst.setColor(0x20FF20);
		burst.setMana(MANA_PER_DAMAGE);
		burst.setStartingMana(MANA_PER_DAMAGE);
		burst.setMinManaLoss(40);
		burst.setManaLossPerTick(4F);
		burst.setGravity(0F);
		burst.setBurstMotion(burst.getVelocity().getX() * motionModifier,
				burst.getVelocity().getY() * motionModifier, burst.getVelocity().getZ() * motionModifier);

		burst.setSourceLens(stack.copy());
		return burst;
	}

	@Override
	public void apply(ItemStack stack, BurstProperties props) {}

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		return dead;
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		ThrownEntity entity = (ThrownEntity) burst;
		Box axis = new Box(entity.getX(), entity.getY(), entity.getZ(), entity.lastRenderX, entity.lastRenderY, entity.lastRenderZ).expand(1);
		List<LivingEntity> entities = entity.world.getNonSpectatingEntities(LivingEntity.class, axis);
		Entity thrower = entity.getOwner();

		for (LivingEntity living : entities) {
			if (living == thrower || living instanceof PlayerEntity && thrower instanceof PlayerEntity
					&& !((PlayerEntity) thrower).shouldDamagePlayer(((PlayerEntity) living))) {
				continue;
			}

			if (living.hurtTime == 0) {
				int cost = MANA_PER_DAMAGE / 3;
				int mana = burst.getMana();
				if (mana >= cost) {
					burst.setMana(mana - cost);
					float damage = 4F + BotaniaAPI.instance().getTerrasteelItemTier().getAttackDamage();
					if (!burst.isFake() && !entity.world.isClient) {
						DamageSource source = DamageSource.MAGIC;
						if (thrower instanceof PlayerEntity) {
							source = DamageSource.player((PlayerEntity) thrower);
						} else if (thrower instanceof LivingEntity) {
							source = DamageSource.mob((LivingEntity) thrower);
						}
						living.damage(source, damage);
						entity.remove();
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		return true;
	}
}
