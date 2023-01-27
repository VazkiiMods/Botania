package vazkii.botania.common.impl.corporea;

import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.impl.corporea.matcher.CorporeaConstantMatcher;
import vazkii.botania.common.impl.corporea.matcher.CorporeaItemStackMatcher;
import vazkii.botania.common.impl.corporea.matcher.CorporeaStringMatcher;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class DefaultCorporeaMatchers {
	public static void init() {
		CorporeaHelper.instance().registerRequestMatcher(prefix("string"), CorporeaStringMatcher.class,
			CorporeaStringMatcher::createFromNBT, CorporeaStringMatcher::createFromBuf);
		CorporeaHelper.instance().registerRequestMatcher(prefix("item_stack"), CorporeaItemStackMatcher.class,
			CorporeaItemStackMatcher::createFromNBT, CorporeaItemStackMatcher::createFromBuf);
		CorporeaHelper.instance().registerRequestMatcher(prefix("constant"), CorporeaConstantMatcher.class,
			CorporeaConstantMatcher::createFromNBT, CorporeaConstantMatcher::createFromBuf);
	}

	private DefaultCorporeaMatchers() {
	}
}
