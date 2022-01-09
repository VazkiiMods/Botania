package vazkii.botania.xplat;

import vazkii.botania.api.BotaniaAPI;

import java.util.ServiceLoader;
import java.util.stream.Collectors;

public interface IClientXplatAbstractions {
	IClientXplatAbstractions INSTANCE = find();

	private static IClientXplatAbstractions find() {
		var providers = ServiceLoader.load(IClientXplatAbstractions.class).stream().toList();
		if (providers.size() != 1) {
			var names = providers.stream().map(p -> p.type().getName()).collect(Collectors.joining(",", "[", "]"));
			throw new IllegalStateException("There should be exactly one IClientXplatAbstractions implementation on the classpath. Found: " + names);
		} else {
			var provider = providers.get(0);
			BotaniaAPI.LOGGER.debug("Instantiating client xplat impl: " + provider.type().getName());
			return provider.get();
		}
	}
}
