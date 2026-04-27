package architecture.goldenboughs_lib.mixed.client;

import architecture.goldenboughs_lib.api.NoMixinException;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;

import java.util.Map;

public interface IModelBuilder<T extends ModelBuilder<T>> {
	static <T extends ModelBuilder<T>> IModelBuilder<T> of(ModelBuilder<T> obj) {
		return (IModelBuilder<T>) obj;
	}

	default Map<String, String> goldenboughs_lib$getTexture() {
		throw new NoMixinException();
	}
}
