/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 5:45:58 PM (GMT)]
 */
package vazkii.botania.common.item.brew;

import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import net.minecraft.item.Item;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ItemBrewBase extends Item implements IBrewItem {

	private static final String TAG_BREW_KEY = "brewKey";
	private static final String TAG_SWIGS_LEFT = "swigsLeft";

	private final int swigs;
	private final int drinkSpeed;
	private final Supplier<Item> baseItem;

	public ItemBrewBase(Properties builder, int swigs, int drinkSpeed, Supplier<Item> baseItem) {
		super(builder);
		this.swigs = swigs;
		this.drinkSpeed = drinkSpeed;
		this.baseItem = baseItem;
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "swigs_taken"), (stack, world, entity) -> swigs - getSwigsLeft(stack));
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return drinkSpeed;
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		return ActionResult.success(player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, LivingEntity living) {
		if(!world.isRemote) {
			for(EffectInstance effect : getBrew(stack).getPotionEffects(stack)) {
				EffectInstance newEffect = new EffectInstance(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), true, true);
				if(effect.getPotion().isInstant())
					effect.getPotion().affectEntity(living, living, living, newEffect.getAmplifier(), 1F);
				else living.addPotionEffect(newEffect);
			}

			if(world.rand.nextBoolean())
				world.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1F, 1F);

			int swigs = getSwigsLeft(stack);
			if(living instanceof PlayerEntity && !((PlayerEntity) living).abilities.isCreativeMode) {
				if(swigs == 1) {
					ItemStack result = new ItemStack(baseItem.get());
					if(!((PlayerEntity) living).inventory.addItemStackToInventory(result))
						return result;
					else {
						return ItemStack.EMPTY;
					}
				}


				setSwigsLeft(stack, swigs - 1);
			}
		}

		return stack;
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> list) {
		if(isInGroup(tab)) {
			for(String s : BotaniaAPI.brewMap.keySet()) {
				ItemStack stack = new ItemStack(this);
				setBrew(stack, s);
				list.add(stack);
			}
		}
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
		return new TranslationTextComponent(getTranslationKey(), new TranslationTextComponent(getBrew(stack).getUnlocalizedName(stack)),
			new StringTextComponent(Integer.toString(getSwigsLeft(stack))).applyTextStyle(TextFormatting.BOLD));
	}

	// [VanillaCopy] PotionUtils.addPotionTooltip, with custom effect list
	@OnlyIn(Dist.CLIENT)
	public static void addPotionTooltip(List<EffectInstance> list, List<ITextComponent> lores, float durationFactor) {
		List<Tuple<String, AttributeModifier>> list1 = Lists.newArrayList();
		if (list.isEmpty()) {
			lores.add((new TranslationTextComponent("effect.none")).applyTextStyle(TextFormatting.GRAY));
		} else {
			for(EffectInstance effectinstance : list) {
				ITextComponent itextcomponent = new TranslationTextComponent(effectinstance.getEffectName());
				Effect effect = effectinstance.getPotion();
				Map<IAttribute, AttributeModifier> map = effect.getAttributeModifierMap();
				if (!map.isEmpty()) {
					for(Map.Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierAmount(effectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						list1.add(new Tuple<>(entry.getKey().getName(), attributemodifier1));
					}
				}

				if (effectinstance.getAmplifier() > 0) {
					itextcomponent.appendText(" ").appendSibling(new TranslationTextComponent("potion.potency." + effectinstance.getAmplifier()));
				}

				if (effectinstance.getDuration() > 20) {
					itextcomponent.appendText(" (").appendText(EffectUtils.getPotionDurationString(effectinstance, durationFactor)).appendText(")");
				}

				lores.add(itextcomponent.applyTextStyle(effect.getEffectType().getColor()));
			}
		}

		if (!list1.isEmpty()) {
			lores.add(new StringTextComponent(""));
			lores.add((new TranslationTextComponent("potion.whenDrank")).applyTextStyle(TextFormatting.DARK_PURPLE));

			for(Tuple<String, AttributeModifier> tuple : list1) {
				AttributeModifier attributemodifier2 = tuple.getB();
				double d0 = attributemodifier2.getAmount();
				double d1;
				if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
					d1 = attributemodifier2.getAmount();
				} else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D) {
					lores.add((new TranslationTextComponent("attribute.modifier.plus." + attributemodifier2.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), new TranslationTextComponent("attribute.name." + (String)tuple.getA()))).applyTextStyle(TextFormatting.BLUE));
				} else if (d0 < 0.0D) {
					d1 = d1 * -1.0D;
					lores.add((new TranslationTextComponent("attribute.modifier.take." + attributemodifier2.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), new TranslationTextComponent("attribute.name." + (String)tuple.getA()))).applyTextStyle(TextFormatting.RED));
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
		addPotionTooltip(getBrew(stack).getPotionEffects(stack), list, 1);
	}

	@Override
	public Brew getBrew(ItemStack stack) {
		String key = ItemNBTHelper.getString(stack, TAG_BREW_KEY, "");
		return BotaniaAPI.getBrewFromKey(key);
	}

	public static void setBrew(ItemStack stack, Brew brew) {
		setBrew(stack, (brew == null ? BotaniaAPI.fallbackBrew : brew).getKey());
	}

	public static void setBrew(ItemStack stack, String brew) {
		ItemNBTHelper.setString(stack, TAG_BREW_KEY, brew);
	}

	@Nonnull
	public static String getSubtype(ItemStack stack) {
		return stack.hasTag() ? ItemNBTHelper.getString(stack, TAG_BREW_KEY, "none") : "none";
	}
	
	public int getSwigsLeft(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_SWIGS_LEFT, swigs);
	}

	public void setSwigsLeft(ItemStack stack, int swigs) {
		ItemNBTHelper.setInt(stack, TAG_SWIGS_LEFT, swigs);
	}
}
