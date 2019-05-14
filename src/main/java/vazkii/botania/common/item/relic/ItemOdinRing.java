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
import top.theillusivec4.curios.api.CuriosAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.integration.curios.CurioIntegration;
import vazkii.botania.common.integration.curios.RelicCurio;
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

	public static class Curio extends RelicCurio {
		public Curio(ItemStack stack, ItemRelic relicDelegate) {
			super(stack, relicDelegate);
		}

		@Override
		public void onValidPlayerWornTick(EntityPlayer player) {
			if(player.isBurning() && ConfigHandler.COMMON.ringOfOdinFireResist.get())
				player.extinguish();
		}

		@Override
		public Multimap<String, AttributeModifier> getAttributeModifiers(String identifier) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			attributes.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(getBaubleUUID(stack), "Odin Ring", 20, 0).setSaved(false));
			return attributes;
		}
	}

	@SubscribeEvent
	public static void onPlayerAttacked(LivingAttackEvent event) {
		if(Botania.curiosLoaded && event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			boolean negate = damageNegations.contains(event.getSource().damageType)
					|| (ConfigHandler.COMMON.ringOfOdinFireResist.get() && fireNegations.contains(event.getSource().damageType));
			boolean hasRing = !CurioIntegration.findOrEmpty(ModItems.odinRing, player).isEmpty();
			if(hasRing && negate)
				event.setCanceled(true);
		}
	}

	@Override
	public ResourceLocation getAdvancement() {
		return new ResourceLocation(LibMisc.MOD_ID, "challenge/odin_ring");
	}

}

