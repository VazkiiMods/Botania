/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 6, 2014, 5:11:23 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.awt.*;
import java.util.List;

public class ItemBloodPendant extends ItemBauble implements IBrewContainer, IBrewItem, IManaUsingItem {

	private static final String TAG_BREW_KEY = "brewKey";

	public ItemBloodPendant(Properties props) {
		super(props);
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> list) {
		super.fillItemGroup(tab, list);
		if(isInGroup(tab)) {
			for(String s : BotaniaAPI.brewMap.keySet()) {
				ItemStack brewStack = getItemForBrew(BotaniaAPI.brewMap.get(s), new ItemStack(this));
				if(!brewStack.isEmpty())
					list.add(brewStack);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addHiddenTooltip(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag adv) {
		super.addHiddenTooltip(stack, world, list, adv);

		Brew brew = getBrew(stack);
		if(brew == BotaniaAPI.fallbackBrew) {
			list.add(new TranslationTextComponent("botaniamisc.notInfused").applyTextStyle(TextFormatting.LIGHT_PURPLE));
			return;
		}

		list.add(new TranslationTextComponent("botaniamisc.brewOf", I18n.format(brew.getUnlocalizedName(stack))).applyTextStyle(TextFormatting.LIGHT_PURPLE));
		for(EffectInstance effect : brew.getPotionEffects(stack)) {
			TextFormatting format = effect.getPotion().type.getColor();
			ITextComponent cmp = new TranslationTextComponent(effect.getEffectName());
			if(effect.getAmplifier() > 0) {
				cmp.appendText(" ");
				cmp.appendSibling(new TranslationTextComponent("botania.roman" + (effect.getAmplifier() + 1)));
			}
			list.add(cmp.applyTextStyle(format));
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		Brew brew = ((IBrewItem) stack.getItem()).getBrew(stack);
		if (brew != BotaniaAPI.fallbackBrew && player instanceof PlayerEntity && !player.world.isRemote) {
			PlayerEntity eplayer = (PlayerEntity) player;
			EffectInstance effect = brew.getPotionEffects(stack).get(0);
			float cost = (float) brew.getManaCost(stack) / effect.getDuration() / (1 + effect.getAmplifier()) * 2.5F;
			boolean doRand = cost < 1;
			if (ManaItemHandler.requestManaExact(stack, eplayer, (int) Math.ceil(cost), false)) {
				EffectInstance currentEffect = player.getActivePotionEffect(effect.getPotion());
				boolean nightVision = effect.getPotion() == Effects.NIGHT_VISION;
				if (currentEffect == null || currentEffect.getDuration() < (nightVision ? 305 : 3)) {
					EffectInstance applyEffect = new EffectInstance(effect.getPotion(), nightVision ? 385 : 80, effect.getAmplifier(), true, true);
					player.addPotionEffect(applyEffect);
				}

				if (!doRand || Math.random() < cost)
					ManaItemHandler.requestManaExact(stack, eplayer, (int) Math.ceil(cost), true);
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		AccessoryRenderHelper.rotateIfSneaking(player);
		boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty();
		GlStateManager.rotatef(180F, 1F, 0F, 0F);
		GlStateManager.translatef(-0.26F, -0.4F, armor ? 0.2F : 0.15F);
		GlStateManager.scalef(0.5F, 0.5F, 0.5F);

		for (TextureAtlasSprite icon : new TextureAtlasSprite[]{MiscellaneousIcons.INSTANCE.bloodPendantChain, MiscellaneousIcons.INSTANCE.bloodPendantGem}) {
			float f = icon.getMinU();
			float f1 = icon.getMaxU();
			float f2 = icon.getMinV();
			float f3 = icon.getMaxV();
			IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, icon.getWidth(), icon.getHeight(), 1F / 32F);

			Color color = new Color(Minecraft.getInstance().getItemColors().getColor(stack, 1));
			GlStateManager.color3f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
			int light = 15728880;
			int lightmapX = light % 65536;
			int lightmapY = light / 65536;
			GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lightmapX, lightmapY);
		}
		GlStateManager.color3f(1, 1, 1);
	}

	@Override
	public Brew getBrew(ItemStack stack) {
		String key = ItemNBTHelper.getString(stack, TAG_BREW_KEY, "");
		return BotaniaAPI.getBrewFromKey(key);
	}

	public static void setBrew(ItemStack stack, Brew brew) {
		setBrew(stack, brew.getKey());
	}

	public static void setBrew(ItemStack stack, String brew) {
		ItemNBTHelper.setString(stack, TAG_BREW_KEY, brew);
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		if(!brew.canInfuseBloodPendant() || brew.getPotionEffects(stack).size() != 1 || brew.getPotionEffects(stack).get(0).getPotion().isInstant())
			return ItemStack.EMPTY;

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
		return getBrew(stack) != BotaniaAPI.fallbackBrew;
	}

}
