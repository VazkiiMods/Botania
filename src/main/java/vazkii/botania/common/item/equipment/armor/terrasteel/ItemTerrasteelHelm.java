/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 14, 2014, 3:13:05 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.armor.terrasteel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTerrasteelHelm extends ItemTerrasteelArmor implements IManaDiscountArmor, IAncientWillContainer, IManaGivingItem {

	private static final String TAG_ANCIENT_WILL = "AncientWill";

	public ItemTerrasteelHelm() {
		this(LibItemNames.TERRASTEEL_HELM);
	}

	public ItemTerrasteelHelm(String name) {
		super(EntityEquipmentSlot.HEAD, name);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		super.onArmorTick(world, player, stack);
		if(hasArmorSet(player)) {
			int food = player.getFoodStats().getFoodLevel();
			if(food > 0 && food < 18 && player.shouldHeal() && player.ticksExisted % 80 == 0)
				player.heal(1F);
			ManaItemHandler.dispatchManaExact(stack, player, 1, true);
		}
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, EntityPlayer player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.2F : 0F;
	}

	@Override
	public void addAncientWill(ItemStack stack, int will) {
		ItemNBTHelper.setBoolean(stack, TAG_ANCIENT_WILL + will, true);
	}

	@Override
	public boolean hasAncientWill(ItemStack stack, int will) {
		return hasAncientWill_(stack, will);
	}

	public static boolean hasAncientWill_(ItemStack stack, int will) {
		return ItemNBTHelper.getBoolean(stack, TAG_ANCIENT_WILL + will, false);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addArmorSetDescription(ItemStack stack, List<String> list) {
		super.addArmorSetDescription(stack, list);
		for(int i = 0; i < 6; i++)
			if(hasAncientWill(stack, i))
				addStringToTooltip(I18n.format("botania.armorset.will" + i + ".desc"), list);
	}

	public static boolean hasAnyWill(ItemStack stack) {
		for(int i = 0; i < 6; i++)
			if(hasAncientWill_(stack, i))
				return true;

		return false;
	}

	@SideOnly(Side.CLIENT)
	public static void renderOnPlayer(ItemStack stack, EntityPlayer player) {
		if(hasAnyWill(stack) && !((ItemTerrasteelArmor) stack.getItem()).hasPhantomInk(stack)) {
			GlStateManager.pushMatrix();
			float f = MiscellaneousIcons.INSTANCE.terrasteelHelmWillIcon.getMinU();
			float f1 = MiscellaneousIcons.INSTANCE.terrasteelHelmWillIcon.getMaxU();
			float f2 = MiscellaneousIcons.INSTANCE.terrasteelHelmWillIcon.getMinV();
			float f3 = MiscellaneousIcons.INSTANCE.terrasteelHelmWillIcon.getMaxV();
			IBaubleRender.Helper.translateToHeadLevel(player);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.rotate(90F, 0F, 1F, 0F);
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			GlStateManager.translate(-0.26F, -1.45F, -0.39F);
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, MiscellaneousIcons.INSTANCE.terrasteelHelmWillIcon.getIconWidth(), MiscellaneousIcons.INSTANCE.terrasteelHelmWillIcon.getIconHeight(), 1F / 16F);
			GlStateManager.popMatrix();
		}
	}

	@SubscribeEvent
	public void onEntityAttacked(LivingHurtEvent event) {
		Entity attacker = event.getSource().getImmediateSource();
		if(attacker instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) attacker;
			if(hasArmorSet(player)) {
				boolean crit = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding();
				ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
				if(crit && !stack.isEmpty() && stack.getItem() instanceof ItemTerrasteelHelm) {
					boolean ahrim = hasAncientWill(stack, 0);
					boolean dharok = hasAncientWill(stack, 1);
					boolean guthan = hasAncientWill(stack, 2);
					boolean torag = hasAncientWill(stack, 3);
					boolean verac = hasAncientWill(stack, 4);
					boolean karil = hasAncientWill(stack, 5);

					if(ahrim)
						event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 20, 1));
					if(dharok)
						event.setAmount(event.getAmount() * (1F + (1F - player.getHealth() / player.getMaxHealth()) * 0.5F));
					if(guthan)
						player.heal(event.getAmount() * 0.25F);
					if(torag)
						event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 1));
					if(verac)
						event.getSource().setDamageBypassesArmor();
					if(karil)
						event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WITHER, 60, 1));
				}
			}
		}
	}

}
