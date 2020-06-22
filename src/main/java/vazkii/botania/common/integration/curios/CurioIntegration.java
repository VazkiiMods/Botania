/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.curios;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.api.event.LivingCurioDropRulesEvent;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

import vazkii.botania.client.core.handler.BaubleRenderHandler;
import vazkii.botania.common.capability.SimpleCapProvider;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ItemKeepIvy;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

import java.util.function.Predicate;

// Classloading-safe way to attach curio behaviour to our items
public class CurioIntegration extends EquipmentHandler {

	public static void sendImc(InterModEnqueueEvent evt) {
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("charm"));
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("ring").setSize(2));
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("belt"));
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("body"));
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("head"));
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("necklace"));
	}

	public static void keepCurioDrops(LivingCurioDropRulesEvent event) {
		event.addOverride(stack -> {
			if (ItemKeepIvy.hasIvy(stack)) {
				stack.removeChildTag(ItemKeepIvy.TAG_KEEP);
				return true;
			}
			return false;
		}, ICurio.DropRule.ALWAYS_KEEP);
	}

	@Override
	protected LazyOptional<IItemHandlerModifiable> getAllWornItems(LivingEntity living) {
		return CuriosAPI.getCuriosHandler(living).map(h -> {
			IItemHandlerModifiable[] invs = h.getCurioMap().values().toArray(new IItemHandlerModifiable[0]);
			return new CombinedInvWrapper(invs);
		});
	}

	@Override
	protected ItemStack findItem(Item item, LivingEntity living) {
		return CuriosAPI.getCurioEquipped(item, living)
				.map(ImmutableTriple::getRight)
				.orElse(ItemStack.EMPTY);
	}

	@Override
	protected ItemStack findItem(Predicate<ItemStack> pred, LivingEntity living) {
		return CuriosAPI.getCurioEquipped(pred, living)
				.map(ImmutableTriple::getRight)
				.orElse(ItemStack.EMPTY);
	}

	@Override
	protected ICapabilityProvider initCap(ItemStack stack) {
		return new SimpleCapProvider<>(CuriosCapability.ITEM, new Wrapper(stack));
	}

	@Override
	public boolean isAccessory(ItemStack stack) {
		return super.isAccessory(stack) || stack.getCapability(CuriosCapability.ITEM).isPresent();
	}

	public static class Wrapper implements ICurio {
		private final ItemStack stack;

		Wrapper(ItemStack stack) {
			this.stack = stack;
		}

		private ItemBauble getItem() {
			return (ItemBauble) stack.getItem();
		}

		@Override
		public void onCurioTick(String identifier, int index, LivingEntity entity) {
			getItem().onWornTick(stack, entity);
		}

		@Override
		public void onEquipped(String identifier, LivingEntity entity) {
			getItem().onEquipped(stack, entity);
		}

		@Override
		public void onUnequipped(String identifier, LivingEntity entity) {
			getItem().onUnequipped(stack, entity);
		}

		@Override
		public boolean canEquip(String identifier, LivingEntity entity) {
			return getItem().canEquip(stack, entity);
		}

		@Override
		public Multimap<String, AttributeModifier> getAttributeModifiers(String identifier) {
			return getItem().getEquippedAttributeModifiers(stack);
		}

		@Override
		public boolean shouldSyncToTracking(String identifier, LivingEntity entity) {
			return true;
		}

		@Override
		public void playEquipSound(LivingEntity entity) {
			entity.world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), ModSounds.equipBauble, entity.getSoundCategory(), 0.1F, 1.3F);
		}

		@Override
		public boolean canRightClickEquip() {
			return true;
		}

		@Override
		public boolean hasRender(String identifier, LivingEntity entity) {
			return getItem().hasRender(stack, entity);
		}

		// Don't use Curios' built-in render method since we need more data passed
		@Override
		public void render(String identifier, MatrixStack ms, IRenderTypeBuffer buffers, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {}

		public void doRender(BaubleRenderHandler layer, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			getItem().doRender(layer, stack, player, ms, buffers, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
		}
	}
}
