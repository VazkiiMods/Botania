/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.integration.shared;

import vazkii.botania.common.proxy.Proxy;

import java.math.RoundingMode;
import java.text.NumberFormat;

public class LocaleHelper {
	public static NumberFormat getIntegerFormat() {
		return NumberFormat.getIntegerInstance(Proxy.INSTANCE.getLocale());
	}

	public static NumberFormat getPercentageFormat(int fractionDigits) {
		final NumberFormat formatter = NumberFormat.getPercentInstance(Proxy.INSTANCE.getLocale());
		formatter.setMinimumFractionDigits(fractionDigits);
		formatter.setMaximumFractionDigits(fractionDigits);
		formatter.setRoundingMode(RoundingMode.HALF_UP);
		return formatter;
	}

	public static NumberFormat getDecimalFractionFormat(int fractionDigits) {
		final NumberFormat formatter = NumberFormat.getNumberInstance(Proxy.INSTANCE.getLocale());
		formatter.setMinimumFractionDigits(fractionDigits);
		formatter.setMaximumFractionDigits(fractionDigits);
		formatter.setRoundingMode(RoundingMode.HALF_UP);
		return formatter;
	}

	public static String formatAsPercentage(double value, int fractionDigits) {
		final NumberFormat formatter = getPercentageFormat(fractionDigits);
		final double minValue = Math.pow(10, -fractionDigits) / 100;
		return (value < minValue
				? "< " + formatter.format(minValue)
				: formatter.format(value)).replace('\u00a0', ' ');
	}

	public static String formatAsDecimalFraction(double value, int fractionDigits) {
		final NumberFormat formatter = getDecimalFractionFormat(fractionDigits);
		return formatter.format(value).replace('\u00a0', ' ');
	}
}
