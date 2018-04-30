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

import baubles.api.BaubleType;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.mana.ITinyPlanetExcempt;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;

import java.util.List;

public class ItemTinyPlanet extends ItemBauble implements IBaubleRender {

	public static final String TAG_ORBIT = "orbit";

	public ItemTinyPlanet() {
		super(LibItemNames.TINY_PLANET);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.CHARM;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		double x = player.posX;
		double y = player.posY + 1.2F;
		double z = player.posZ;
		if(player.world.isRemote)
			y -= 1.62F;

		applyEffect(player.world, x, y, z);
	}

	public static void applyEffect(World world, double x, double y, double z) {
		int range = 8;
		List<EntityThrowable> entities = world.getEntitiesWithinAABB(EntityThrowable.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range), Predicates.instanceOf(IManaBurst.class));
		for(EntityThrowable entity : entities) {
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
			Vector3 currentVec = new Vector3(entity.posX, entity.posY, entity.posZ);
			Vector3 moveVector = targetVec.subtract(currentVec);

			burst.setMotion(moveVector.x, moveVector.y, moveVector.z);

			incrementOrbitTime(entity);
		}
	}

	public static int getEntityOrbitTime(Entity entity) {
		NBTTagCompound cmp = entity.getEntityData();
		if(cmp.hasKey(TAG_ORBIT))
			return cmp.getInteger(TAG_ORBIT);
		else return 0;
	}

	public static void incrementOrbitTime(Entity entity) {
		NBTTagCompound cmp = entity.getEntityData();
		int time = getEntityOrbitTime(entity);
		cmp.setInteger(TAG_ORBIT, time + 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if(type == RenderType.HEAD) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0, -1.5F, 0.5F);
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(ModBlocks.tinyPlanet.getDefaultState(), 1.0F);
		}
	}

}
