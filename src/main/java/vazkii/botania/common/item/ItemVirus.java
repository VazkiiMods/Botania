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

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
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

import javax.annotation.Nonnull;

public class ItemVirus extends ItemMod {

	private static final int SUBTYPES = 2;

	public ItemVirus() {
		super(LibItemNames.VIRUS);
		setHasSubtypes(true);
		MinecraftForge.EVENT_BUS.register(ItemVirus.class);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase living, EnumHand hand) {
		if(living instanceof AbstractHorse && !(living instanceof EntityLlama)) {
			if(player.world.isRemote)
				return true;
			AbstractHorse horse = (AbstractHorse) living;
			if(horse.isTame()) {
				IItemHandler inv = horse.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				ItemStack saddle = inv.getStackInSlot(0);

				// Not all AbstractHorse's have saddles in slot 0
				if(!saddle.isEmpty() && saddle.getItem() != Items.SADDLE) {
					horse.entityDropItem(saddle, 0);
					saddle = ItemStack.EMPTY;
				}

				for (int i = 1; i < inv.getSlots(); i++)
					if(!inv.getStackInSlot(i).isEmpty())
						horse.entityDropItem(inv.getStackInSlot(i), 0);

				if (horse instanceof AbstractChestHorse && ((AbstractChestHorse) horse).hasChest())
					horse.entityDropItem(new ItemStack(Blocks.CHEST), 0);

				horse.setDead();

				AbstractHorse newHorse = stack.getItemDamage() == 0 ? new EntityZombieHorse(player.world) : new EntitySkeletonHorse(player.world);
				newHorse.setTamedBy(player);
				newHorse.setPositionAndRotation(horse.posX, horse.posY, horse.posZ, horse.rotationYaw, horse.rotationPitch);

				// Put the saddle back
				if(!saddle.isEmpty())
					newHorse.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).insertItem(0, saddle, false);

				AbstractAttributeMap oldAttributes = horse.getAttributeMap();
				AbstractAttributeMap attributes = newHorse.getAttributeMap();

				IAttributeInstance movementSpeed = attributes.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED);
				movementSpeed.setBaseValue(oldAttributes.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue());
				movementSpeed.applyModifier(new AttributeModifier("Ermergerd Virus D:", movementSpeed.getBaseValue(), 0));

				IAttributeInstance health = attributes.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
				health.setBaseValue(oldAttributes.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
				health.applyModifier(new AttributeModifier("Ermergerd Virus D:", health.getBaseValue(), 0));

				IAttributeInstance jumpHeight = attributes.getAttributeInstance(AbstractHorse.JUMP_STRENGTH);
				jumpHeight.setBaseValue(oldAttributes.getAttributeInstance(AbstractHorse.JUMP_STRENGTH).getBaseValue());
				jumpHeight.applyModifier(new AttributeModifier("Ermergerd Virus D:", jumpHeight.getBaseValue() * 0.5, 0));

				newHorse.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F + living.world.rand.nextFloat(), living.world.rand.nextFloat() * 0.7F + 1.3F);
				newHorse.onInitialSpawn(player.world.getDifficultyForLocation(new BlockPos(newHorse)), null);
				newHorse.setGrowingAge(horse.getGrowingAge());
				player.world.spawnEntity(newHorse);
				newHorse.spawnExplosionParticle();

				stack.shrink(1);
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

		if((entity instanceof EntityZombieHorse || entity instanceof EntitySkeletonHorse)
				&& event.getSource() == DamageSource.FALL
				&& ((AbstractHorse) entity).isTame()) {
			event.setCanceled(true);
		}
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
		if(isInCreativeTab(tab)) {
			for(int i = 0; i < SUBTYPES; i++)
				list.add(new ItemStack(this, 1, i));
		}
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		return super.getTranslationKey(stack) + stack.getItemDamage();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemAppendMeta(this, 2, LibItemNames.VIRUS);
	}

}
