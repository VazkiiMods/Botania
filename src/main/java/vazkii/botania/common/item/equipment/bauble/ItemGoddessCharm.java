/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 30, 2016, 6:22:03 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ItemGoddessCharm extends ItemBauble implements IManaUsingItem, IBaubleRender {

	public static final int COST = 1000;
	
	public ItemGoddessCharm() {
		super(LibItemNames.GODDESS_CHARM);
	}

	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Detonate event) {
		Explosion e = event.getExplosion();
		Vec3d vec = e.getPosition();
		List<EntityPlayer> players = event.getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(vec.x, vec.y, vec.z, vec.x, vec.y, vec.z).grow(8));
		
		for(EntityPlayer player : players) {
			ItemStack charm = BaublesApi.getBaublesHandler(player).getStackInSlot(6);
			if(!charm.isEmpty() && charm.getItem() instanceof ItemGoddessCharm && ManaItemHandler.requestManaExact(charm, player, COST, true)) {
				event.getAffectedBlocks().clear();
				return;
			}
		}
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.CHARM;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if (type == RenderType.HEAD) {
			GlStateManager.pushMatrix();
			Helper.translateToHeadLevel(player);
			Helper.translateToFace();
			Helper.defaultTransforms();
			GlStateManager.rotate(-90F, 0F, 1F, 0F);
			GlStateManager.translate(0.5F, 0.2F, 0.45F);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
