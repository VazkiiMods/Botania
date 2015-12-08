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

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTerrasteelHelm extends ItemTerrasteelArmor implements IManaDiscountArmor, IAncientWillContainer, IManaGivingItem {

	private static final String TAG_ANCIENT_WILL = "AncientWill";
	static IIcon willIcon;

	public ItemTerrasteelHelm() {
		this(LibItemNames.TERRASTEEL_HELM);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public ItemTerrasteelHelm(String name) {
		super(0, name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
		willIcon = IconHelper.forName(par1IconRegister, "willFlame");
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
	public float getDiscount(ItemStack stack, int slot, EntityPlayer player) {
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
				addStringToTooltip(StatCollector.translateToLocal("botania.armorset.will" + i + ".desc"), list);
	}

	public static boolean hasAnyWill(ItemStack stack) {
		for(int i = 0; i < 6; i++)
			if(hasAncientWill_(stack, i))
				return true;

		return false;
	}

	@SideOnly(Side.CLIENT)
	public static void renderOnPlayer(ItemStack stack, RenderPlayerEvent event) {
		if(hasAnyWill(stack) && !((ItemTerrasteelArmor) stack.getItem()).hasPhantomInk(stack)) {
			GL11.glPushMatrix();
			float f = willIcon.getMinU();
			float f1 = willIcon.getMaxU();
			float f2 = willIcon.getMinV();
			float f3 = willIcon.getMaxV();
			Helper.translateToHeadLevel(event.entityPlayer);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
			GL11.glRotatef(90F, 0F, 1F, 0F);
			GL11.glRotatef(180F, 1F, 0F, 0F);
			GL11.glTranslatef(-0.26F, 0.15F, -0.39F);
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, willIcon.getIconWidth(), willIcon.getIconHeight(), 1F / 16F);
			GL11.glPopMatrix();
		}
	}

	@SubscribeEvent
	public void onEntityAttacked(LivingHurtEvent event) {
		Entity attacker = event.source.getEntity();
		if(attacker instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) attacker;
			if(hasArmorSet(player)) {
				boolean crit = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null;
				ItemStack stack = player.inventory.armorItemInSlot(3);
				if(crit && stack != null && stack.getItem() instanceof ItemTerrasteelHelm) {
					boolean ahrim = hasAncientWill(stack, 0);
					boolean dharok = hasAncientWill(stack, 1);
					boolean guthan = hasAncientWill(stack, 2);
					boolean torag = hasAncientWill(stack, 3);
					boolean verac = hasAncientWill(stack, 4);
					boolean karil = hasAncientWill(stack, 5);

					if(ahrim)
						event.entityLiving.addPotionEffect(new PotionEffect(Potion.weakness.id, 20, 1));
					if(dharok)
						event.ammount *= 1F + (1F - player.getHealth() / player.getMaxHealth()) * 0.5F;
					if(guthan)
						player.heal(event.ammount * 0.25F);
					if(torag)
						event.entityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 60, 1));
					if(verac)
						event.source.setDamageBypassesArmor();
					if(karil)
						event.entityLiving.addPotionEffect(new PotionEffect(Potion.wither.id, 60, 1));
				}
			}
		}
	}

}
