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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ModSounds;

import javax.annotation.Nonnull;

import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;

import java.util.List;

public class EntityMagicLandmine extends Entity {

	public EntityDoppleganger summoner;

	public EntityMagicLandmine(World world) {
		super(world);
		setSize(0F, 0F);
	}

	@Override
	public void onUpdate() {
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		super.onUpdate();

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
				List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range));
				for(EntityPlayer player : players) {
					player.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, summoner), 10);
					player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 25, 0));
					PotionEffect wither = new PotionEffect(MobEffects.WITHER, 120, 2);
					wither.getCurativeItems().clear();
					player.addPotionEffect(wither);
				}
			}

			setDead();
		}
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound var1) {
	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound var1) {
	}

}
