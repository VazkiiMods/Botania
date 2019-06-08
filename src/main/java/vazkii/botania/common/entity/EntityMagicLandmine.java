/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 12, 2014, 7:59:00 PM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ModSounds;

import javax.annotation.Nonnull;

import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class EntityMagicLandmine extends Entity {
	@ObjectHolder(LibMisc.MOD_ID + ":magic_landmine")
	public static EntityType<?> TYPE;

	public EntityDoppleganger summoner;

	public EntityMagicLandmine(World world) {
		super(TYPE, world);
		setSize(0F, 0F);
	}

	@Override
	public void tick() {
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		super.tick();

		float range = 2.5F;

		float r = 0.2F;
		float g = 0F;
		float b = 0.2F;

		//Botania.proxy.wispFX(world, posX, posY, posZ, r, g, b, 0.6F, -0.2F, 1);
		for(int i = 0; i < 6; i++)
			Botania.proxy.wispFX(posX - range + Math.random() * range * 2, posY, posZ - range + Math.random() * range * 2, r, g, b, 0.4F, -0.015F, 1);

		if(ticksExisted >= 55) {
			world.playSound(null, posX, posY, posZ, ModSounds.gaiaTrap, SoundCategory.NEUTRAL, 0.3F, 1F);

			float m = 0.35F;
			g = 0.4F;
			for(int i = 0; i < 25; i++)
				Botania.proxy.wispFX(posX, posY + 1, posZ, r, g, b, 0.5F, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m);

			if(!world.isRemote) {
				List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range));
				for(PlayerEntity player : players) {
					player.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, summoner), 10);
					player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 25, 0));
					EffectInstance wither = new EffectInstance(Effects.WITHER, 120, 2);
					wither.getCurativeItems().clear();
					player.addPotionEffect(wither);
				}
			}

			remove();
		}
	}

	@Override
	protected void registerData() {
	}

	@Override
	protected void readAdditional(@Nonnull CompoundNBT var1) {
	}

	@Override
	protected void writeAdditional(@Nonnull CompoundNBT var1) {
	}
}
