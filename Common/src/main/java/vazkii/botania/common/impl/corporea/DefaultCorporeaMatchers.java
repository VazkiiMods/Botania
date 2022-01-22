package vazkii.botania.common.impl.corporea;

import vazkii.botania.api.corporea.CorporeaHelper;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class DefaultCorporeaMatchers {
	public static void init() {
		CorporeaHelper.instance().registerRequestMatcher(prefix("string"), CorporeaStringMatcher.class, CorporeaStringMatcher::createFromNBT);
		CorporeaHelper.instance().registerRequestMatcher(prefix("item_stack"), CorporeaItemStackMatcher.class, CorporeaItemStackMatcher::createFromNBT);
	}

	private DefaultCorporeaMatchers() {}
}
