/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.brew;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ItemBrewBase extends Item implements IBrewItem {

	private static final String TAG_BREW_KEY = "brewKey";
	private static final String TAG_SWIGS_LEFT = "swigsLeft";

	private final int swigs;
	private final int drinkSpeed;
	private final Supplier<Item> baseItem;

	public ItemBrewBase(Settings builder, int swigs, int drinkSpeed, Supplier<Item> baseItem) {
		super(builder);
		this.swigs = swigs;
		this.drinkSpeed = drinkSpeed;
		this.baseItem = baseItem;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return drinkSpeed;
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		return ItemUsage.consumeHeldItem(world, player, hand);
	}

	@Nonnull
	@Override
	public ItemStack finishUsing(@Nonnull ItemStack stack, World world, LivingEntity living) {
		if (!world.isClient) {
			for (StatusEffectInstance effect : getBrew(stack).getPotionEffects(stack)) {
				StatusEffectInstance newEffect = new StatusEffectInstance(effect.getEffectType(), effect.getDuration(), effect.getAmplifier(), true, true);
				if (effect.getEffectType().isInstant()) {
					effect.getEffectType().applyInstantEffect(living, living, living, newEffect.getAmplifier(), 1F);
				} else {
					living.addStatusEffect(newEffect);
				}
			}

			if (world.random.nextBoolean()) {
				world.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1F, 1F);
			}

			int swigs = getSwigsLeft(stack);
			if (living instanceof PlayerEntity && !((PlayerEntity) living).abilities.creativeMode) {
				if (swigs == 1) {
					ItemStack result = new ItemStack(baseItem.get());
					if (!((PlayerEntity) living).inventory.insertStack(result)) {
						return result;
					} else {
						return ItemStack.EMPTY;
					}
				}

				setSwigsLeft(stack, swigs - 1);
			}
		}

		return stack;
	}

	@Override
	public void appendStacks(ItemGroup tab, DefaultedList<ItemStack> list) {
		if (isIn(tab)) {
			for (Brew brew : BotaniaAPI.instance().getBrewRegistry()) {
				if (brew == ModBrews.fallbackBrew) {
					continue;
				}
				ItemStack stack = new ItemStack(this);
				setBrew(stack, brew);
				list.add(stack);
			}
		}
	}

	@Nonnull
	@Override
	public Text getName(@Nonnull ItemStack stack) {
		return new TranslatableText(getTranslationKey(), new TranslatableText(getBrew(stack).getTranslationKey(stack)),
				new LiteralText(Integer.toString(getSwigsLeft(stack))).formatted(Formatting.BOLD));
	}

	// [VanillaCopy] PotionUtils.addPotionTooltip, with custom effect list
	@Environment(EnvType.CLIENT)
	public static void addPotionTooltip(List<StatusEffectInstance> list, List<Text> lores, float durationFactor) {
		List<Pair<EntityAttribute, EntityAttributeModifier>> list1 = Lists.newArrayList();
		if (list.isEmpty()) {
			lores.add((new TranslatableText("effect.none")).formatted(Formatting.GRAY));
		} else {
			for (StatusEffectInstance effectinstance : list) {
				MutableText iformattabletextcomponent = new TranslatableText(effectinstance.getTranslationKey());
				StatusEffect effect = effectinstance.getEffectType();
				Map<EntityAttribute, EntityAttributeModifier> map = effect.getAttributeModifiers();
				if (!map.isEmpty()) {
					for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : map.entrySet()) {
						EntityAttributeModifier attributemodifier = entry.getValue();
						EntityAttributeModifier attributemodifier1 = new EntityAttributeModifier(attributemodifier.getName(), effect.adjustModifierAmount(effectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						list1.add(new Pair<>(entry.getKey(), attributemodifier1));
					}
				}

				if (effectinstance.getAmplifier() > 0) {
					iformattabletextcomponent = new TranslationTextComponent("potion.withAmplifier", iformattabletextcomponent, new TranslationTextComponent("potion.potency." + effectinstance.getAmplifier()));
				}

				if (effectinstance.getDuration() > 20) {
					iformattabletextcomponent = new TranslationTextComponent("potion.withDuration", iformattabletextcomponent, EffectUtils.getPotionDurationString(effectinstance, durationFactor));
				}

				lores.add(iformattabletextcomponent.mergeStyle(effect.getEffectType().getColor()));
			}
		}

		if (!list1.isEmpty()) {
			lores.add(LiteralText.EMPTY);
			lores.add((new TranslatableText("potion.whenDrank")).formatted(Formatting.DARK_PURPLE));

			for (Pair<EntityAttribute, EntityAttributeModifier> pair : list1) {
				EntityAttributeModifier attributemodifier2 = pair.getSecond();
				double d0 = attributemodifier2.getValue();
				double d1;
				if (attributemodifier2.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
					d1 = attributemodifier2.getValue();
				} else {
					d1 = attributemodifier2.getValue() * 100.0D;
				}

				if (d0 > 0.0D) {
					lores.add((new TranslatableText("attribute.modifier.plus." + attributemodifier2.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(d1), new TranslatableText(pair.getFirst().getTranslationKey()))).formatted(Formatting.BLUE));
				} else if (d0 < 0.0D) {
					d1 = d1 * -1.0D;
					lores.add((new TranslatableText("attribute.modifier.take." + attributemodifier2.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(d1), new TranslatableText(pair.getFirst().getTranslationKey()))).formatted(Formatting.RED));
				}
			}
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext flags) {
		addPotionTooltip(getBrew(stack).getPotionEffects(stack), list, 1);
	}

	@Override
	public Brew getBrew(ItemStack stack) {
		String key = ItemNBTHelper.getString(stack, TAG_BREW_KEY, "");
		return BotaniaAPI.instance().getBrewRegistry().get(Identifier.tryParse(key));
	}

	public static void setBrew(ItemStack stack, @Nullable Brew brew) {
		Identifier id;
		if (brew != null) {
			id = BotaniaAPI.instance().getBrewRegistry().getId(brew);
		} else {
			id = prefix("fallback");
		}
		setBrew(stack, id);
	}

	public static void setBrew(ItemStack stack, Identifier brew) {
		ItemNBTHelper.setString(stack, TAG_BREW_KEY, brew.toString());
	}

	@Nonnull
	public static String getSubtype(ItemStack stack) {
		return stack.hasTag() ? ItemNBTHelper.getString(stack, TAG_BREW_KEY, "none") : "none";
	}

	public int getSwigs() {
		return swigs;
	}

	public int getSwigsLeft(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_SWIGS_LEFT, swigs);
	}

	public void setSwigsLeft(ItemStack stack, int swigs) {
		ItemNBTHelper.setInt(stack, TAG_SWIGS_LEFT, swigs);
	}
}
