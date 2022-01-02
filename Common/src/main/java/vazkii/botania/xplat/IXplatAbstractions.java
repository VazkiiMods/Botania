package vazkii.botania.xplat;

import java.util.ServiceLoader;
import java.util.stream.Collectors;

public interface IXplatAbstractions {
	boolean isModLoaded(String modId);

	IXplatAbstractions INSTANCE = find();

	private static IXplatAbstractions find() {
		var providers = ServiceLoader.load(IXplatAbstractions.class).stream().toList();
		if (providers.size() != 1) {
			var names = providers.stream().map(p -> p.type().getName()).collect(Collectors.joining(",", "[", "]"));
			throw new IllegalStateException("There should be exactly one IXplatAbstractions implementation on the classpath. Found: " + names);
		} else {
			var provider = providers.get(0);
			// todo PatchouliAPI.LOGGER.debug("Instantiating xplat impl: " + provider.type().getName());
			return provider.get();
		}
	}
}
