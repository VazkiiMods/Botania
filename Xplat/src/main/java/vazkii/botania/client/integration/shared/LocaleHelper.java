package vazkii.botania.client.integration.shared;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.proxy.Proxy;

import java.math.RoundingMode;
import java.text.NumberFormat;

public class LocaleHelper {
	public static NumberFormat getIntegerFormat() {
		return NumberFormat.getIntegerInstance(Proxy.INSTANCE.getLocale());
	}

	@NotNull
	public static NumberFormat getPercentageFormat(int fractionDigits) {
		final NumberFormat formatter = NumberFormat.getPercentInstance(Proxy.INSTANCE.getLocale());
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
}
