/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 29, 2015, 10:13:41 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ItemOdinRing extends ItemRelicBauble {

	private static final Set<String> damageNegations = new HashSet<>();
	private static final Set<String> fireNegations = new HashSet<>();

	public ItemOdinRing(Properties props) {
		super(props);

		damageNegations.add(DamageSource.DROWN.damageType);
		damageNegations.add(DamageSource.FALL.damageType);
		damageNegations.add(DamageSource.LAVA.damageType);
		damageNegations.add(DamageSource.IN_WALL.damageType);
		damageNegations.add(DamageSource.STARVE.damageType);
		fireNegations.add(DamageSource.IN_FIRE.damageType);
		fireNegations.add(DamageSource.ON_FIRE.damageType);
	}

	@Override
	public void onValidPlayerWornTick(ItemStack stack, EntityPlayer player) {
		if(player.isBurning() && ConfigHandler.COMMON.ringOfOdinFireResist.get())
			player.extinguish();
	}

	@SubscribeEvent
	public static void onPlayerAttacked(LivingAttackEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			boolean negate = damageNegations.contains(event.getSource().damageType)
					|| (ConfigHandler.COMMON.ringOfOdinFireResist.get() && fireNegations.contains(event.getSource().damageType));
			if(!getOdinRing(player).isEmpty() && negate)
				event.setCanceled(true);
		}
	}

	/* todo 1.13
	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}
	*/

	public static ItemStack getOdinRing(EntityPlayer player) {
		IItemHandler baubles = null; // BaublesApi.getBaublesHandler(player);
		int slot = -1; // todo 1.13 BaublesApi.isBaubleEquipped(player, ModItems.odinRing);
		if (slot < 0) {
			return ItemStack.EMPTY;
		}
		return baubles.getStackInSlot(slot);
	}

	@Override
	public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {
		if(!player.world.isRemote) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			fillModifiers(attributes, stack);
			player.getAttributeMap().applyAttributeModifiers(attributes);
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		if(!player.world.isRemote) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			fillModifiers(attributes, stack);
			player.getAttributeMap().removeAttributeModifiers(attributes);
		}
	}

	private void fillModifiers(Multimap<String, AttributeModifier> attributes, ItemStack stack) {
		if(stack.isEmpty()) // workaround for Azanor/Baubles#156
			return;
		
		attributes.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(getBaubleUUID(stack), "Odin Ring", 20, 0).setSaved(false));
	}

	@Override
	public ResourceLocation getAdvancement() {
		return new ResourceLocation(LibMisc.MOD_ID, "challenge/odin_ring");
	}

}

