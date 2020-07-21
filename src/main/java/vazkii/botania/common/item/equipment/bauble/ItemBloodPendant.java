/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.List;

public class ItemBloodPendant extends ItemBauble implements IBrewContainer, IBrewItem, IManaUsingItem {

	private static final String TAG_BREW_KEY = "brewKey";

	public ItemBloodPendant(Settings props) {
		super(props);
	}

	@Override
	public void appendStacks(ItemGroup tab, DefaultedList<ItemStack> list) {
		super.appendStacks(tab, list);
		if (isIn(tab)) {
			for (Brew brew : BotaniaAPI.instance().getBrewRegistry()) {
				ItemStack brewStack = getItemForBrew(brew, new ItemStack(this));
				if (!brewStack.isEmpty()) {
					list.add(brewStack);
				}
			}
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void addHiddenTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext adv) {
		super.addHiddenTooltip(stack, world, tooltip, adv);

		Brew brew = getBrew(stack);
		if (brew == ModBrews.fallbackBrew) {
			tooltip.add(new TranslatableText("botaniamisc.notInfused").formatted(Formatting.LIGHT_PURPLE));
			return;
		}

		tooltip.add(new TranslatableText("botaniamisc.brewOf", I18n.translate(brew.getTranslationKey(stack))).formatted(Formatting.LIGHT_PURPLE));
		for (StatusEffectInstance effect : brew.getPotionEffects(stack)) {
			Formatting format = effect.getEffectType().getType().getFormatting();
			MutableText cmp = new TranslatableText(effect.getTranslationKey());
			if (effect.getAmplifier() > 0) {
				cmp.append(" ");
				cmp.append(new TranslatableText("botania.roman" + (effect.getAmplifier() + 1)));
			}
			tooltip.add(cmp.formatted(format));
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		Brew brew = ((IBrewItem) stack.getItem()).getBrew(stack);
		if (brew != ModBrews.fallbackBrew && player instanceof PlayerEntity && !player.world.isClient) {
			PlayerEntity eplayer = (PlayerEntity) player;
			StatusEffectInstance effect = brew.getPotionEffects(stack).get(0);
			float cost = (float) brew.getManaCost(stack) / effect.getDuration() / (1 + effect.getAmplifier()) * 2.5F;
			boolean doRand = cost < 1;
			if (ManaItemHandler.instance().requestManaExact(stack, eplayer, (int) Math.ceil(cost), false)) {
				StatusEffectInstance currentEffect = player.getStatusEffect(effect.getEffectType());
				boolean nightVision = effect.getEffectType() == StatusEffects.NIGHT_VISION;
				if (currentEffect == null || currentEffect.getDuration() < (nightVision ? 305 : 3)) {
					StatusEffectInstance applyEffect = new StatusEffectInstance(effect.getEffectType(), nightVision ? 385 : 80, effect.getAmplifier(), true, true);
					player.addStatusEffect(applyEffect);
				}

				if (!doRand || Math.random() < cost) {
					ManaItemHandler.instance().requestManaExact(stack, eplayer, (int) Math.ceil(cost), true);
				}
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(BipedEntityModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, VertexConsumerProvider buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !player.getEquippedStack(EquipmentSlot.CHEST).isEmpty();
		bipedModel.torso.rotate(ms);
		ms.translate(-0.25, 0.4, armor ? 0.05 : 0.12);
		ms.scale(0.5F, -0.5F, -0.5F);

		BakedModel model = MiscellaneousIcons.INSTANCE.bloodPendantChain;
		VertexConsumer buffer = buffers.getBuffer(TexturedRenderLayers.getEntityCutout());
		MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer()
				.render(ms.peek(), buffer, null, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV);

		model = MiscellaneousIcons.INSTANCE.bloodPendantGem;
		int color = MinecraftClient.getInstance().getItemColors().getColorMultiplier(stack, 1);
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer()
				.render(ms.peek(), buffer, null, model, r, g, b, 0xF000F0, OverlayTexture.DEFAULT_UV);
	}

	@Override
	public Brew getBrew(ItemStack stack) {
		String key = ItemNBTHelper.getString(stack, TAG_BREW_KEY, "");
		return BotaniaAPI.instance().getBrewRegistry().get(Identifier.tryParse(key));
	}

	public static void setBrew(ItemStack stack, Brew brew) {
		setBrew(stack, BotaniaAPI.instance().getBrewRegistry().getId(brew));
	}

	public static void setBrew(ItemStack stack, Identifier brew) {
		ItemNBTHelper.setString(stack, TAG_BREW_KEY, brew.toString());
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		if (!brew.canInfuseBloodPendant() || brew.getPotionEffects(stack).size() != 1 || brew.getPotionEffects(stack).get(0).getEffectType().isInstant()) {
			return ItemStack.EMPTY;
		}

		ItemStack brewStack = new ItemStack(this);
		setBrew(brewStack, brew);
		return brewStack;
	}

	@Override
	public int getManaCost(Brew brew, ItemStack stack) {
		return brew.getManaCost() * 10;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return getBrew(stack) != ModBrews.fallbackBrew;
	}

}
