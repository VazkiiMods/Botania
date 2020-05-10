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

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ModSounds;

import java.util.List;

public class ItemUnholyCloak extends ItemHolyCloak {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_UNHOLY_CLOAK);
	private static final ResourceLocation textureGlow = new ResourceLocation(LibResources.MODEL_UNHOLY_CLOAK_GLOW);

	public ItemUnholyCloak(Properties props) {
		super(props);
	}

	@Override
	public boolean effectOnDamage(LivingHurtEvent event, PlayerEntity player, ItemStack stack) {
		if (!event.getSource().isUnblockable()) {
			int range = 6;
			List mobs = player.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(player.getPosX() - range, player.getPosY() - range, player.getPosZ() - range, player.getPosX() + range, player.getPosY() + range, player.getPosZ() + range), Predicates.instanceOf(IMob.class));
			for (IMob mob : (List<IMob>) mobs) {
				if (mob instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) mob;
					entity.attackEntityFrom(DamageSource.causePlayerDamage(player), event.getAmount());
				}
			}

			player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.unholyCloak, SoundCategory.PLAYERS, 1F, 1F);
			for (int i = 0; i < 90; i++) {
				float rad = i * 4F * (float) Math.PI / 180F;
				float xMotion = (float) Math.cos(rad) * 0.2F;
				float zMotion = (float) Math.sin(rad) * 0.2F;
				WispParticleData data = WispParticleData.wisp(0.6F + (float) Math.random() * 0.2F, 0.4F + (float) Math.random() + 0.25F, 0F, 0F);
				player.world.addParticle(data, player.getPosX(), player.getPosY() + 0.5, player.getPosZ(), xMotion, 0F, zMotion);
			}

			return true;
		}

		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	ResourceLocation getCloakTexture() {
		return texture;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	ResourceLocation getCloakGlowTexture() {
		return textureGlow;
	}

}
