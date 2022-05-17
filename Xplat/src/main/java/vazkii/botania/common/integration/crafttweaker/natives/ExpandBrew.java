package vazkii.botania.common.integration.crafttweaker.natives;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;
import com.blamejared.crafttweaker_annotations.annotations.TaggableElement;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.brew.ModBrews;

import java.util.List;

@ZenRegister()
@Document("mods/Botania/Brew")
@NativeTypeRegistration(value = Brew.class, zenCodeName = "mods.botania.Brew")
@TaggableElement("botania:brews")
public class ExpandBrew {

	/**
	 * Checks whether this brew can be infused with a Blood Pendant.
	 *
	 * @return True if it can be infused, false otherwise.
	 */
	@ZenCodeType.Method
	@ZenCodeType.Getter("canInfuseBloodPendant")
	public static boolean canInfuseBloodPendant(Brew internal) {
		return internal.canInfuseBloodPendant();
	}

	/**
	 * Checks whether this brew can be infused with incense.
	 *
	 * @return True if it can be infused, false otherwise.
	 */
	@ZenCodeType.Method
	@ZenCodeType.Getter("canInfuseIncense")
	public static boolean canInfuseIncense(Brew internal) {
		return internal.canInfuseIncense();
	}

	/**
	 * Gets the translation key of the brew.
	 *
	 * @return The translation key of the brew.
	 */
	@ZenCodeType.Method
	@ZenCodeType.Getter("translationKey")
	public static String getTranslationKey(Brew internal) {
		return internal.getTranslationKey();
	}

	/**
	 * Gets the translation key of the brew with extra context provided by the stack.
	 *
	 * @param stack The stack to provide extra context with.
	 * @return The translation key of the brew.
	 * @docParam stack <item:minecraft:dirt>
	 */
	@ZenCodeType.Method
	public static String getTranslationKey(Brew internal, ItemStack stack) {
		return internal.getTranslationKey(stack);
	}

	/**
	 * Gets the color of the brew for the ItemStack passed in.
	 *
	 * @param stack The stack to get the color of.
	 * @return The color of the brew for the given stack.
	 * @docParam stack <item:minecraft:dirt>
	 */
	@ZenCodeType.Method
	public static int getColor(Brew internal, ItemStack stack) {
		return internal.getColor(stack);
	}

	/**
	 * Gets the mana cost of the brew.
	 *
	 * @return The mana cost of the brew.
	 */
	@ZenCodeType.Method
	@ZenCodeType.Getter("manaCost")
	public static int getManaCost(Brew internal) {
		return internal.getManaCost();
	}

	/**
	 * Gets the mana cost of the brew for the ItemStack passed in.
	 *
	 * @param stack The stack to get the mana cost of.
	 * @return The mana cost of the brew.
	 * @docParam stack <item:minecraft:dirt>
	 */
	@ZenCodeType.Method
	public static int getManaCost(Brew internal, ItemStack stack) {
		return internal.getManaCost(stack);
	}

	/**
	 * Gets the {@link MobEffectInstance}s that this brew applies when consumed.
	 *
	 * @param stack An ItemStack to provide extra context.
	 * @return The {@link MobEffectInstance}s that this brew applies when consumed.
	 * @docParam stack <item:minecraft:dirt>
	 */
	@ZenCodeType.Method
	public static List<MobEffectInstance> getPotionEffects(Brew internal, ItemStack stack) {
		return internal.getPotionEffects(stack);
	}

	/**
	 * Gets the command string of this brew.
	 *
	 * @return The command string of this brew.
	 */
	@ZenCodeType.Method
	public static String getCommandString(Brew internal) {
		return "<brew:" + ModBrews.registry.getKey(internal).toString() + ">";
	}
}
