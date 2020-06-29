/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.BaubleRenderHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.List;

public class ItemBloodPendant extends ItemBauble implements IBrewContainer, IBrewItem, IManaUsingItem {

	private static final String TAG_BREW_KEY = "brewKey";

	public ItemBloodPendant(Properties props) {
		super(props);
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> list) {
		super.fillItemGroup(tab, list);
		if (isInGroup(tab)) {
			for (Brew brew : BotaniaAPI.instance().getBrewRegistry()) {
				ItemStack brewStack = getItemForBrew(brew, new ItemStack(this));
				if (!brewStack.isEmpty()) {
					list.add(brewStack);
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addHiddenTooltip(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag adv) {
		super.addHiddenTooltip(stack, world, tooltip, adv);

		Brew brew = getBrew(stack);
		if (brew == ModBrews.fallbackBrew) {
			tooltip.add(new TranslationTextComponent("botaniamisc.notInfused").func_240699_a_(TextFormatting.LIGHT_PURPLE));
			return;
		}

		tooltip.add(new TranslationTextComponent("botaniamisc.brewOf", I18n.format(brew.getTranslationKey(stack))).func_240699_a_(TextFormatting.LIGHT_PURPLE));
		for (EffectInstance effect : brew.getPotionEffects(stack)) {
			TextFormatting format = effect.getPotion().getEffectType().getColor();
			IFormattableTextComponent cmp = new TranslationTextComponent(effect.getEffectName());
			if (effect.getAmplifier() > 0) {
				cmp.func_240702_b_(" ");
				cmp.func_230529_a_(new TranslationTextComponent("botania.roman" + (effect.getAmplifier() + 1)));
			}
			tooltip.add(cmp.func_240699_a_(format));
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		Brew brew = ((IBrewItem) stack.getItem()).getBrew(stack);
		if (brew != ModBrews.fallbackBrew && player instanceof PlayerEntity && !player.world.isRemote) {
			PlayerEntity eplayer = (PlayerEntity) player;
			EffectInstance effect = brew.getPotionEffects(stack).get(0);
			float cost = (float) brew.getManaCost(stack) / effect.getDuration() / (1 + effect.getAmplifier()) * 2.5F;
			boolean doRand = cost < 1;
			if (ManaItemHandler.instance().requestManaExact(stack, eplayer, (int) Math.ceil(cost), false)) {
				EffectInstance currentEffect = player.getActivePotionEffect(effect.getPotion());
				boolean nightVision = effect.getPotion() == Effects.NIGHT_VISION;
				if (currentEffect == null || currentEffect.getDuration() < (nightVision ? 305 : 3)) {
					EffectInstance applyEffect = new EffectInstance(effect.getPotion(), nightVision ? 385 : 80, effect.getAmplifier(), true, true);
					player.addPotionEffect(applyEffect);
				}

				if (!doRand || Math.random() < cost) {
					ManaItemHandler.instance().requestManaExact(stack, eplayer, (int) Math.ceil(cost), true);
				}
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(BaubleRenderHandler layer, ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty();
		layer.getEntityModel().bipedBody.translateRotate(ms);
		ms.translate(-0.25, 0.4, armor ? 0.05 : 0.12);
		ms.scale(0.5F, -0.5F, -0.5F);

		IBakedModel model = MiscellaneousIcons.INSTANCE.bloodPendantChain;
		IVertexBuilder buffer = buffers.getBuffer(Atlases.getCutoutBlockType());
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
				.renderModelBrightnessColor(ms.getLast(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);

		model = MiscellaneousIcons.INSTANCE.bloodPendantGem;
		int color = Minecraft.getInstance().getItemColors().getColor(stack, 1);
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
				.renderModelBrightnessColor(ms.getLast(), buffer, null, model, r, g, b, 0xF000F0, OverlayTexture.NO_OVERLAY);
	}

	@Override
	public Brew getBrew(ItemStack stack) {
		String key = ItemNBTHelper.getString(stack, TAG_BREW_KEY, "");
		return BotaniaAPI.instance().getBrewRegistry().getOrDefault(ResourceLocation.tryCreate(key));
	}

	public static void setBrew(ItemStack stack, Brew brew) {
		setBrew(stack, BotaniaAPI.instance().getBrewRegistry().getKey(brew));
	}

	public static void setBrew(ItemStack stack, ResourceLocation brew) {
		ItemNBTHelper.setString(stack, TAG_BREW_KEY, brew.toString());
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		if (!brew.canInfuseBloodPendant() || brew.getPotionEffects(stack).size() != 1 || brew.getPotionEffects(stack).get(0).getPotion().isInstant()) {
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
