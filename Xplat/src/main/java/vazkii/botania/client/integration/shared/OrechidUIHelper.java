package vazkii.botania.client.integration.shared;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.OrechidRecipe;
import vazkii.botania.common.handler.OrechidManager;

import java.text.NumberFormat;
import java.util.stream.Stream;

/**
 * Shared helper methods for the various recipe display mod plugins.
 */
public class OrechidUIHelper {
	/**
	 * How far off from the actual chance an approximated outputs/inputs ratio calculated by
	 * {@link #getRatioForChance(double)} should be at most.
	 */
	private static final float MAX_ACCEPTABLE_RATIO_ERROR = 0.05f;

	/**
	 * How many input blocks an approximated outputs/inputs ratio calculated by
	 * {@link #getRatioForChance(double)} should have at most if the
	 * number of outputs in the ratio is greater than 1.
	 */
	private static final int MAX_NUM_INPUTS_FOR_RATIO = 16;

	@NotNull
	public static Component getPercentageComponent(double chance) {
		final String chanceText = LocaleHelper.formatAsPercentage(chance, 1);
		return Component.literal(chanceText);
	}

	@NotNull
	public static Component getRatioTooltipComponent(@NotNull IntIntPair ratio) {
		final NumberFormat formatter = LocaleHelper.getIntegerFormat();
		return Component.translatable("botaniamisc.conversionRatio",
				formatter.format(ratio.secondInt()),
				formatter.format(ratio.firstInt()));
	}

	@NotNull
	public static Component getBiomeChanceTooltipComponent(double chance, @NotNull String biomeTranslationKey) {
		return Component.translatable("botaniamisc.conversionChanceBiome",
				getPercentageComponent(chance),
				Component.translatable(biomeTranslationKey).withStyle(ChatFormatting.ITALIC)
		).withStyle(ChatFormatting.GRAY);
	}

	@NotNull
	public static Stream<Component> getBiomeChanceAndRatioTooltipComponents(double chance, OrechidRecipe recipe) {
		final var biomeTranslationKey = OrechidUIHelper.getPlayerBiomeTranslationKey();
		final var player = Minecraft.getInstance().player;
		if (biomeTranslationKey == null || player == null) {
			return Stream.empty();
		}

		final var biomeChance = OrechidUIHelper.getChance(recipe, player.blockPosition());
		if (biomeChance == null || Mth.equal(chance, biomeChance)) {
			return Stream.empty();
		}

		final var biomeRatio = OrechidUIHelper.getRatioForChance(biomeChance);
		return Stream.of(
				OrechidUIHelper.getBiomeChanceTooltipComponent(biomeChance, biomeTranslationKey),
				Component.literal("(")
						.append(OrechidUIHelper.getRatioTooltipComponent(biomeRatio))
						.append(")")
						.withStyle(ChatFormatting.GRAY)
		);
	}

	@Nullable
	public static <T extends OrechidRecipe> Double getChance(T recipe, @Nullable BlockPos pos) {
		final var level = Minecraft.getInstance().level;
		if (level == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		final var type = (RecipeType<? extends OrechidRecipe>) recipe.getType();
		final var state = recipe.getInput().getDisplayed().get(0);
		final int totalWeight = OrechidManager.getTotalDisplayWeightAt(level, type, state, pos);
		final int weight = pos != null
				? recipe.getWeight(level, pos)
				: recipe.getWeight();
		return (double) weight / totalWeight;
	}

	/**
	 * Determines a "visually pleasing" ratio to be expected between input and output that is not too far off the
	 * precise ratio.
	 *
	 * @param actualRatio The actual ratio.
	 * @return A pair of ints, first int being the number of input blocks, and second int being the expected number of
	 *         output blocks.
	 */
	@NotNull
	public static IntIntPair getRatioForChance(double actualRatio) {
		// First shot: 1 desired output from N input blocks
		int bestNumOutputs = 1;
		int bestNumInputs = (int) Math.round(1 / actualRatio);
		double bestError = calcError(actualRatio, bestNumOutputs, bestNumInputs);

		// Now try to bring the error below an acceptable margin, but only with relatively small integer ratios.
		if (bestNumInputs < MAX_NUM_INPUTS_FOR_RATIO && bestError > MAX_ACCEPTABLE_RATIO_ERROR) {
			// This calculates an approximation for outputs/inputs for the given chance using continued fractions.
			// (also see https://en.wikipedia.org/wiki/Continued_fraction#Infinite_continued_fractions_and_convergents)
			int numOutputsNminus1 = 1;
			int numOutputsNminus2 = 0;
			int numInputsNminus1 = 0;
			int numInputsNminus2 = 1;
			double remainderN = actualRatio;
			do {
				int coefficientN = (int) Math.floor(remainderN);
				int numOutputsN = coefficientN * numOutputsNminus1 + numOutputsNminus2;
				int numInputsN = coefficientN * numInputsNminus1 + numInputsNminus2;

				if (numInputsN > MAX_NUM_INPUTS_FOR_RATIO) {
					// numbers are getting too big
					break;
				}

				final double errorN = calcError(actualRatio, numOutputsN, numInputsN);
				if (errorN < bestError) {
					bestNumOutputs = numOutputsN;
					bestNumInputs = numInputsN;
					bestError = errorN;
				}

				// shift values for next iteration
				numOutputsNminus2 = numOutputsNminus1;
				numOutputsNminus1 = numOutputsN;
				numInputsNminus2 = numInputsNminus1;
				numInputsNminus1 = numInputsN;
				remainderN = 1 / (remainderN - coefficientN);

			} while (numInputsNminus1 != 0 && bestError > MAX_ACCEPTABLE_RATIO_ERROR);
		}

		return IntIntImmutablePair.of(bestNumInputs, bestNumOutputs);
	}

	private static double calcError(double chance, int numOutputs, int numInputs) {
		return Math.abs((double) numOutputs / numInputs - chance) / chance;
	}

	public static String getPlayerBiomeTranslationKey() {
		final var player = Minecraft.getInstance().player;
		if (player == null) {
			return null;
		}
		final var biomeKey = player.level().getBiome(player.blockPosition()).unwrapKey().orElse(null);
		if (biomeKey == null) {
			return "argument.id.invalid";
		}
		return String.format("biome.%s.%s", biomeKey.location().getNamespace(), biomeKey.location().getPath());
	}
}
