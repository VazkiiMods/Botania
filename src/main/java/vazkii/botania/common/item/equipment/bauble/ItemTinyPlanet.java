/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 20, 2014, 10:58:00 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.ITinyPlanetExcempt;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.Vector3;

import java.util.List;

public class ItemTinyPlanet extends ItemBauble {

	public static final String TAG_ORBIT = "orbit";

	public ItemTinyPlanet(Properties props) {
		super(props);
	}
	
	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		double x = player.getX();
		double y = player.getY() + player.getEyeHeight();
		double z = player.getZ();

		applyEffect(player.world, x, y, z);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.scalef(0.5F, 0.5F, 0.5F);
		GlStateManager.translatef(0, -1.5F, 0.5F);
		Minecraft.getInstance().getBlockRendererDispatcher().renderBlockBrightness(ModBlocks.tinyPlanet.getDefaultState(), 1.0F);
	}

	public static void applyEffect(World world, double x, double y, double z) {
		int range = 8;
		List<ThrowableEntity> entities = world.getEntitiesWithinAABB(ThrowableEntity.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range), Predicates.instanceOf(IManaBurst.class));
		for(ThrowableEntity entity : entities) {
			IManaBurst burst = (IManaBurst) entity;
			ItemStack lens = burst.getSourceLens();
			if(lens != null && lens.getItem() instanceof ITinyPlanetExcempt && !((ITinyPlanetExcempt) lens.getItem()).shouldPull(lens))
				continue;

			int orbitTime = getEntityOrbitTime(entity);
			if(orbitTime == 0)
				burst.setMinManaLoss(burst.getMinManaLoss() * 3);

			float radius = Math.min(7.5F, (Math.max(40, orbitTime) - 40) / 40F + 1.5F);
			int angle = orbitTime % 360;

			float xTarget = (float) (x + Math.cos(angle * 10 * Math.PI / 180F) * radius);
			float yTarget = (float) y;
			float zTarget = (float) (z + Math.sin(angle * 10 * Math.PI / 180F) * radius);

			Vector3 targetVec = new Vector3(xTarget, yTarget, zTarget);
			Vector3 currentVec = new Vector3(entity.getX(), entity.getY(), entity.getZ());
			Vector3 moveVector = targetVec.subtract(currentVec);

			burst.setBurstMotion(moveVector.x, moveVector.y, moveVector.z);

			incrementOrbitTime(entity);
		}
	}

	public static int getEntityOrbitTime(Entity entity) {
		CompoundNBT cmp = entity.getPersistentData();
		if(cmp.contains(TAG_ORBIT))
			return cmp.getInt(TAG_ORBIT);
		else return 0;
	}

	public static void incrementOrbitTime(Entity entity) {
		CompoundNBT cmp = entity.getPersistentData();
		int time = getEntityOrbitTime(entity);
		cmp.putInt(TAG_ORBIT, time + 1);
	}

}
