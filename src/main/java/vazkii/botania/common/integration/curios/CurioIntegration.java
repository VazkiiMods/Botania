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
import nerdhub.cardinal.components.api.event.ItemComponentCallbackV2;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.util.Pair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosComponent;
import top.theillusivec4.curios.api.SlotTypeInfo;
import top.theillusivec4.curios.api.SlotTypePreset;

import top.theillusivec4.curios.api.type.component.ICurio;
import top.theillusivec4.curios.api.type.component.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.component.IRenderableCurio;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ItemKeepIvy;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// Classloading-safe way to attach curio behaviour to our items
public class CurioIntegration extends EquipmentHandler {

	public static void init() {
		CuriosApi.enqueueSlotType(SlotTypeInfo.BuildScheme.REGISTER, SlotTypePreset.CHARM.getInfoBuilder().build());
		CuriosApi.enqueueSlotType(SlotTypeInfo.BuildScheme.REGISTER, SlotTypePreset.RING.getInfoBuilder().size(2).build());
		CuriosApi.enqueueSlotType(SlotTypeInfo.BuildScheme.REGISTER, SlotTypePreset.BELT.getInfoBuilder().build());
		CuriosApi.enqueueSlotType(SlotTypeInfo.BuildScheme.REGISTER, SlotTypePreset.BODY.getInfoBuilder().build());
		CuriosApi.enqueueSlotType(SlotTypeInfo.BuildScheme.REGISTER, SlotTypePreset.HEAD.getInfoBuilder().build());
		CuriosApi.enqueueSlotType(SlotTypeInfo.BuildScheme.REGISTER, SlotTypePreset.NECKLACE.getInfoBuilder().build());
	}

	public static void keepCurioDrops(LivingEntity livingEntity, ICuriosItemHandler handler, DamageSource source,
		int lootingLevel, boolean recentlyHit, List<Pair<Predicate<ItemStack>, ICurio.DropRule>> overrides) { //TODO make this less hacky
		overrides.add(new Pair<>(stack -> {
			if (ItemKeepIvy.hasIvy(stack)) {
				stack.removeSubTag(ItemKeepIvy.TAG_KEEP);
				return true;
			}
			return false;
		}, ICurio.DropRule.ALWAYS_KEEP));
	}

	@Override
	protected Inventory getAllWornItems(LivingEntity living) {
		return CuriosApi.getCuriosHelper().getCuriosHandler(living)
			.map(h -> {
				List<ItemStack> list = new ArrayList<>();
				for (ICurioStacksHandler sh : h.getCurios().values()) {
					Inventory stacks = sh.getStacks();
					for (int i = 0; i < stacks.size(); i++) {
						list.add(stacks.getStack(i));
					}
				}
				return new SimpleInventory(list.toArray(new ItemStack[0]));
			})
			.orElseGet(() -> new SimpleInventory(0));
	}

	@Override
	protected ItemStack findItem(Item item, LivingEntity living) {
		return CuriosApi.getCuriosHelper().findEquippedCurio(item, living)
				.map(ImmutableTriple::getRight)
				.orElse(ItemStack.EMPTY);
	}

	@Override
	protected ItemStack findItem(Predicate<ItemStack> pred, LivingEntity living) {
		return CuriosApi.getCuriosHelper().findEquippedCurio(pred, living)
				.map(ImmutableTriple::getRight)
				.orElse(ItemStack.EMPTY);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void registerComponentEvent(Item item) {
		ItemComponentCallbackV2.event(item).register((item1, stack, components) -> {
			components.put(CuriosComponent.ITEM, new Wrapper(stack));
			Botania.proxy.runOnClient(() -> () -> components.put(CuriosComponent.ITEM_RENDER, new RenderWrapper(stack)));
		});
	}

	@Override
	public boolean isAccessory(ItemStack stack) {
		return super.isAccessory(stack) || CuriosComponent.ITEM.getNullable(stack) != null;
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
		public void curioTick(String identifier, int index, LivingEntity entity) {
			getItem().onWornTick(stack, entity);
		}

		@Override
		public void onEquip(String identifier, int index, LivingEntity entity) {
			getItem().onEquipped(stack, entity);
		}

		@Override
		public void onUnequip(String identifier, int index, LivingEntity entity) {
			getItem().onUnequipped(stack, entity);
		}

		@Override
		public boolean canEquip(String identifier, LivingEntity entity) {
			return getItem().canEquip(stack, entity);
		}

		@Override
		public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(String identifier) {
			return getItem().getEquippedAttributeModifiers(stack);
		}

		@Override
		public void playRightClickEquipSound(LivingEntity entity) {
			entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.equipBauble, entity.getSoundCategory(), 0.1F, 1.3F);
		}

		@Override
		public boolean canRightClickEquip() {
			return true;
		}

	}

	private static class RenderWrapper implements IRenderableCurio {
		private final ItemStack stack;

		RenderWrapper(ItemStack stack) {
			this.stack = stack;
		}

		private ItemBauble getItem() {
			return (ItemBauble) stack.getItem();
		}

		@Override
		@Environment(EnvType.CLIENT)
		public void render(String identifier, int index, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (!getItem().hasRender(stack, livingEntity)) {
				return;
			}

			EntityRenderer<?> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);
			if (!(renderer instanceof FeatureRendererContext<?, ?>)) {
				return;
			}
			EntityModel<?> model = ((FeatureRendererContext<?, ?>) renderer).getModel();
			if (!(model instanceof BipedEntityModel<?>)) {
				return;
			}

			ItemStack cosmetic = getItem().getCosmeticItem(stack);
			if (!cosmetic.isEmpty()) {
				IRenderableCurio sub = CuriosComponent.ITEM_RENDER.getNullable(cosmetic);
				if (sub != null) {
					sub.render(identifier, index, matrixStack, renderTypeBuffer, light, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			} else {
				getItem().doRender((BipedEntityModel<?>) model, stack, livingEntity, matrixStack, renderTypeBuffer, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
			}
		}
	}
}
