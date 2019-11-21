/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 14, 2014, 7:34:56 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.terrasteel;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.network.PacketLeftClick;

import java.util.List;
import java.util.UUID;

public class ItemTerraSword extends ItemManasteelSword implements ILensEffect {

	// todo 1.14 use throwableentity's owner system instead of tracking it ourselves
	private static final String TAG_ATTACKER_UUID = "attackerUuid";

	private static final int MANA_PER_DAMAGE = 100;

	public ItemTerraSword(Properties props) {
		super(BotaniaAPI.TERRASTEEL_ITEM_TIER, props);
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
		if (!evt.getEntityPlayer().world.isRemote) {
			trySpawnBurst(evt.getEntityPlayer());
		}
	}

	public void trySpawnBurst(PlayerEntity player) {
		if (!player.getHeldItemMainhand().isEmpty()
				&& player.getHeldItemMainhand().getItem() == this
				&& player.getCooledAttackStrength(0) == 1) {
			EntityManaBurst burst = getBurst(player, player.getHeldItemMainhand());
			player.world.addEntity(burst);
			ToolCommons.damageItem(player.getHeldItemMainhand(), 1, player, MANA_PER_DAMAGE);
			player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.terraBlade, SoundCategory.PLAYERS, 0.4F, 1.4F);
		}
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	public EntityManaBurst getBurst(PlayerEntity player, ItemStack stack) {
		EntityManaBurst burst = new EntityManaBurst(player, Hand.MAIN_HAND);

		float motionModifier = 7F;

		burst.setColor(0x20FF20);
		burst.setMana(MANA_PER_DAMAGE);
		burst.setStartingMana(MANA_PER_DAMAGE);
		burst.setMinManaLoss(40);
		burst.setManaLossPerTick(4F);
		burst.setGravity(0F);
		burst.setBurstMotion(burst.getMotion().getX() * motionModifier,
				burst.getMotion().getY() * motionModifier, burst.getMotion().getZ() * motionModifier);

		ItemStack lens = stack.copy();
		ItemNBTHelper.setUuid(lens, TAG_ATTACKER_UUID, player.getUniqueID());
		burst.setSourceLens(lens);
		return burst;
	}

	@Override
	public void apply(ItemStack stack, BurstProperties props) {}

	@Override
	public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		return dead;
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		ThrowableEntity entity = (ThrowableEntity) burst;
		AxisAlignedBB axis = new AxisAlignedBB(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).grow(1);
		List<LivingEntity> entities = entity.world.getEntitiesWithinAABB(LivingEntity.class, axis);
		UUID attacker = ItemNBTHelper.getUuid(burst.getSourceLens(), TAG_ATTACKER_UUID);

		for(LivingEntity living : entities) {
			if(living instanceof PlayerEntity && (living.getUniqueID().equals(attacker)
				|| ServerLifecycleHooks.getCurrentServer() != null && !ServerLifecycleHooks.getCurrentServer().isPVPEnabled()))
				continue;

			if(living.hurtTime == 0) {
				int cost = MANA_PER_DAMAGE / 3;
				int mana = burst.getMana();
				if(mana >= cost) {
					burst.setMana(mana - cost);
					float damage = 4F + BotaniaAPI.TERRASTEEL_ITEM_TIER.getAttackDamage();
					if(!burst.isFake() && !entity.world.isRemote) {
						PlayerEntity player = attacker != null ? living.world.getPlayerByUuid(attacker) : null;
						living.attackEntityFrom(player == null
								? DamageSource.MAGIC
								: DamageSource.causePlayerDamage(player), damage);
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
