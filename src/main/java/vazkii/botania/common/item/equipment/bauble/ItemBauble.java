/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 20, 2014, 3:30:06 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import net.minecraft.item.Item;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public abstract class ItemBauble extends Item implements ICosmeticAttachable, IPhantomInkable {

	private static final String TAG_BAUBLE_UUID_MOST = "baubleUUIDMost";
	private static final String TAG_BAUBLE_UUID_LEAST = "baubleUUIDLeast";
	private static final String TAG_COSMETIC_ITEM = "cosmeticItem";
	private static final String TAG_PHANTOM_INK = "phantomInk";

	public ItemBauble(Properties props) {
		super(props);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack par1ItemStack, World world, List<ITextComponent> stacks, ITooltipFlag flags) {
		if(Screen.hasShiftDown())
			addHiddenTooltip(par1ItemStack, world, stacks, flags);
		else stacks.add(new TranslationTextComponent("botaniamisc.shiftinfo"));
	}

	@OnlyIn(Dist.CLIENT)
	public void addHiddenTooltip(ItemStack par1ItemStack, World world, List<ITextComponent> stacks, ITooltipFlag flags) {
		String key = vazkii.botania.client.core.helper.RenderHelper.getKeyDisplayString("key.curios.open.desc");

		if(key != null)
			stacks.add(new TranslationTextComponent("botania.baubletooltip", key));

		ItemStack cosmetic = getCosmeticItem(par1ItemStack);
		if(!cosmetic.isEmpty())
			stacks.add(new TranslationTextComponent("botaniamisc.hasCosmetic", cosmetic.getDisplayName()));

		if(hasPhantomInk(par1ItemStack))
			stacks.add(new TranslationTextComponent("botaniamisc.hasPhantomInk"));
	}

	@Override
	public ItemStack getCosmeticItem(ItemStack stack) {
		CompoundNBT cmp = ItemNBTHelper.getCompound(stack, TAG_COSMETIC_ITEM, true);
		if(cmp == null)
			return ItemStack.EMPTY;
		return ItemStack.read(cmp);
	}

	@Override
	public void setCosmeticItem(ItemStack stack, ItemStack cosmetic) {
		CompoundNBT cmp = new CompoundNBT();
		if(!cosmetic.isEmpty())
			cmp = cosmetic.write(cmp);
		ItemNBTHelper.setCompound(stack, TAG_COSMETIC_ITEM, cmp);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return !getContainerItem(stack).isEmpty();
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		return getCosmeticItem(itemStack);
	}

	public static UUID getBaubleUUID(ItemStack stack) {
		long most = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_MOST, 0);
		if(most == 0) {
			UUID uuid = UUID.randomUUID();
			ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_MOST, uuid.getMostSignificantBits());
			ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_LEAST, uuid.getLeastSignificantBits());
			return getBaubleUUID(stack);
		}

		long least = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_LEAST, 0);
		return new UUID(most, least);
	}

	@Override
	public boolean hasPhantomInk(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_PHANTOM_INK, false);
	}

	@Override
	public void setPhantomInk(ItemStack stack, boolean ink) {
		ItemNBTHelper.setBoolean(stack, TAG_PHANTOM_INK, ink);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return EquipmentHandler.initBaubleCap(stack);
	}

	public void onWornTick(ItemStack stack, LivingEntity entity) {}

	public void onEquipped(ItemStack stack, LivingEntity entity) {
		if(!entity.world.isRemote && entity instanceof ServerPlayerEntity) {
			PlayerHelper.grantCriterion((ServerPlayerEntity) entity, new ResourceLocation(LibMisc.MOD_ID, "main/bauble_wear"), "code_triggered");
		}
	}

	public void onUnequipped(ItemStack stack, LivingEntity entity) {}

	public boolean canEquip(ItemStack stack, LivingEntity entity) {
		return true;
	}

	public Multimap<String, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
		return HashMultimap.create();
	}

	public boolean hasRender(ItemStack stack, LivingEntity living) {
		return !(stack.getItem() instanceof ICosmeticAttachable && !((ICosmeticAttachable) stack.getItem()).getCosmeticItem(stack).isEmpty())
				&& !(stack.getItem() instanceof IPhantomInkable && ((IPhantomInkable) stack.getItem()).hasPhantomInk(stack))
				&& ConfigHandler.CLIENT.renderAccessories.get()
				&& living.getActivePotionEffect(Effects.INVISIBILITY) == null;
	}

	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {}
}
