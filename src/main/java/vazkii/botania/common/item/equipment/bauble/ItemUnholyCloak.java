/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.base.Predicates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ModSounds;

import java.util.List;

public class ItemUnholyCloak extends ItemHolyCloak {

	private static final Identifier texture = new Identifier(LibResources.MODEL_UNHOLY_CLOAK);
	private static final Identifier textureGlow = new Identifier(LibResources.MODEL_UNHOLY_CLOAK_GLOW);

	public ItemUnholyCloak(Settings props) {
		super(props);
	}

	@Override
	public boolean effectOnDamage(LivingHurtEvent event, PlayerEntity player, ItemStack stack) {
		if (!event.getSource().bypassesArmor()) {
			int range = 6;
			@SuppressWarnings("unchecked")
			List<Monster> mobs = (List<Monster>) (List<?>) player.world.getEntitiesByClass(Entity.class, new Box(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range, player.getZ() + range), Predicates.instanceOf(Monster.class));
			for (Monster mob : mobs) {
				if (mob instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) mob;
					entity.damage(DamageSource.player(player), event.getAmount());
				}
			}

			player.world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.unholyCloak, SoundCategory.PLAYERS, 1F, 1F);
			for (int i = 0; i < 90; i++) {
				float rad = i * 4F * (float) Math.PI / 180F;
				float xMotion = (float) Math.cos(rad) * 0.2F;
				float zMotion = (float) Math.sin(rad) * 0.2F;
				WispParticleData data = WispParticleData.wisp(0.6F + (float) Math.random() * 0.2F, 0.4F + (float) Math.random() + 0.25F, 0F, 0F);
				player.world.addParticle(data, player.getX(), player.getY() + 0.5, player.getZ(), xMotion, 0F, zMotion);
			}

			return true;
		}

		return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	Identifier getCloakTexture() {
		return texture;
	}

	@Override
	@Environment(EnvType.CLIENT)
	Identifier getCloakGlowTexture() {
		return textureGlow;
	}

}
