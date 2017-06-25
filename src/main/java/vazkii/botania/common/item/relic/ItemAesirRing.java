/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 29, 2015, 10:16:29 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import baubles.api.BaubleType;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.item.IWireframeCoordinateListProvider;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

import java.util.List;
import java.util.UUID;

public class ItemAesirRing extends ItemRelicBauble implements IWireframeCoordinateListProvider {

	private final Multimap<String, AttributeModifier> attributes = HashMultimap.create();

	public ItemAesirRing() {
		super(LibItemNames.AESIR_RING);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onDropped(ItemTossEvent event) {
		if(event.getEntityItem() != null && !event.getEntityItem().getItem().isEmpty() && !event.getEntityItem().world.isRemote) {
			ItemStack stack = event.getEntityItem().getItem();
			if(stack.getItem() == this) {
				event.getEntityItem().setDead();

				UUID user = getSoulbindUUID(stack);
				for(Item item : new Item[] { ModItems.thorRing, ModItems.lokiRing, ModItems.odinRing }) {
					ItemStack stack1 = new ItemStack(item);
					bindToUUID(user, stack1);
					EntityItem entity = new EntityItem(event.getEntityItem().world, event.getEntityItem().posX, event.getEntityItem().posY, event.getEntityItem().posZ, stack1);
					entity.motionX = event.getEntityItem().motionX;
					entity.motionY = event.getEntityItem().motionY;
					entity.motionZ = event.getEntityItem().motionZ;
					entity.age = event.getEntityItem().age;
					entity.setPickupDelay(event.getEntityItem().delayBeforeCanPickup);
					entity.world.spawnEntity(entity);
				}
			}
		}
	}

	@Override
	public void onValidPlayerWornTick(ItemStack stack, EntityPlayer player) {
		((ItemOdinRing) ModItems.odinRing).onValidPlayerWornTick(stack, player);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}

	@Override
	public List<BlockPos> getWireframesToDraw(EntityPlayer player, ItemStack stack) {
		return ((IWireframeCoordinateListProvider) ModItems.lokiRing).getWireframesToDraw(player, stack);
	}

	@Override
	public BlockPos getSourceWireframe(EntityPlayer player, ItemStack stack) {
		return ((IWireframeCoordinateListProvider) ModItems.lokiRing).getSourceWireframe(player, stack);
	}

	@Override
	public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {
		attributes.clear();
		fillModifiers(attributes, stack);
		player.getAttributeMap().applyAttributeModifiers(attributes);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		attributes.clear();
		fillModifiers(attributes, stack);
		player.getAttributeMap().removeAttributeModifiers(attributes);
	}


	private void fillModifiers(Multimap<String, AttributeModifier> attributes, ItemStack stack) {
		if(stack.isEmpty()) // workaround for Azanor/Baubles#156
			return;
		
		attributes.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(getBaubleUUID(stack), "Bauble modifier", 20, 0));
	}
}
