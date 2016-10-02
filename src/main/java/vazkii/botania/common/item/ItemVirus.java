/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 21, 2014, 4:46:17 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibObfuscation;

public class ItemVirus extends ItemMod {

	private static final int SUBTYPES = 2;

	public ItemVirus() {
		super(LibItemNames.VIRUS);
		setHasSubtypes(true);
		MinecraftForge.EVENT_BUS.register(ItemVirus.class);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer player, EntityLivingBase par3EntityLivingBase, EnumHand hand) {
		if(par3EntityLivingBase instanceof EntityHorse) {
			if(player.worldObj.isRemote)
				return true;
			EntityHorse horse = (EntityHorse) par3EntityLivingBase;
			if(horse.getType() != HorseType.ZOMBIE && horse.getType() != HorseType.SKELETON && horse.isTame()) {
				IItemHandler inv = horse.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				ItemStack saddle = inv.getStackInSlot(0);

				for (int i = 1; i < inv.getSlots(); i++)
					if(inv.getStackInSlot(i) != null)
						horse.entityDropItem(inv.getStackInSlot(i), 0);
				if (horse.isChested())
					horse.entityDropItem(new ItemStack(Blocks.CHEST), 0);

				horse.setType(par1ItemStack.getItemDamage() == 0 ? HorseType.ZOMBIE : HorseType.SKELETON);

				// Reinit the horse chest to the right new size
				Method m = ReflectionHelper.findMethod(EntityHorse.class, horse, LibObfuscation.INIT_HORSE_CHEST);
				try {
					m.invoke(horse);
				} catch (IllegalAccessException | InvocationTargetException ignored) {}

				// Put the saddle back
				horse.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).insertItem(0, saddle, false);

				AbstractAttributeMap attributes = horse.getAttributeMap();
				IAttributeInstance movementSpeed = attributes.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED);
				IAttributeInstance health = attributes.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
				health.applyModifier(new AttributeModifier("Ermergerd Virus D:", health.getBaseValue(), 0));
				movementSpeed.applyModifier(new AttributeModifier("Ermergerd Virus D:", movementSpeed.getBaseValue(), 0));
				IAttributeInstance jumpHeight = attributes.getAttributeInstance(ReflectionHelper.getPrivateValue(EntityHorse.class, null, LibObfuscation.HORSE_JUMP_STRENGTH));
				jumpHeight.applyModifier(new AttributeModifier("Ermergerd Virus D:", jumpHeight.getBaseValue() * 0.5, 0));
				horse.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F + par3EntityLivingBase.worldObj.rand.nextFloat(), par3EntityLivingBase.worldObj.rand.nextFloat() * 0.7F + 1.3F);
				horse.spawnExplosionParticle();

				par1ItemStack.stackSize--;
				return true;
			}
		}
		return false;
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if(entity.isRiding() && entity.getRidingEntity() instanceof EntityLivingBase)
			entity = (EntityLivingBase) entity.getRidingEntity();

		if(entity instanceof EntityHorse && event.getSource() == DamageSource.fall) {
			EntityHorse horse = (EntityHorse) entity;
			if((horse.getType() == HorseType.ZOMBIE || horse.getType() == HorseType.SKELETON) && horse.isTame())
				event.setCanceled(true);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> list) {
		for(int i = 0; i < SUBTYPES; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack) + stack.getItemDamage();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemAppendMeta(this, 2, LibItemNames.VIRUS);
	}

}
