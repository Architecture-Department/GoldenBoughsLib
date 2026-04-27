package architecture.goldenboughs_lib.network.codec;

import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * 用于创建具有不同组件数量的复合StreamCodecs的构建器。
 *
 * @param <B> 缓冲区类型
 * @param <C> 容器类型
 */
public class CompositeStreamCodecBuilder<B, C> {
	private final List<Entry<?, B, C>> entries = new ArrayList<>();
	private Function<Iterator<Object>, C> decoderFactory;

	private CompositeStreamCodecBuilder() {
	}

	/**
	 * 创建一个新的构建器实例
	 *
	 * @param <B> 缓冲区类型
	 * @param <C> 容器类型
	 * @return 新的构建器
	 */
	public static <B, C> CompositeStreamCodecBuilder<B, C> builder() {
		return new CompositeStreamCodecBuilder<>();
	}

	/**
	 * 向复合编解码器添加编解码器和获取器对
	 *
	 * @param codec  此组件的编解码器
	 * @param getter 此组件的获取器函数
	 * @param <T>    组件类型
	 * @return 此构建器用于链式调用
	 */
	public <T> CompositeStreamCodecBuilder<B, C> withComponent(
		StreamCodec<? super B, T> codec,
		Function<C, T> getter) {
		entries.add(new Entry<>(codec, getter));
		return this;
	}

	/**
	 * 设置从解码组件创建容器的工厂函数
	 *
	 * @param decoderFactory 一个函数，接受解码组件列表并创建容器
	 * @return 此构建器用于链式调用
	 */
	public CompositeStreamCodecBuilder<B, C> decoderFactory(Function<Iterator<Object>, C> decoderFactory) {
		this.decoderFactory = decoderFactory;
		return this;
	}

	/**
	 * 构建复合StreamCodec
	 *
	 * @return 构建的StreamCodec
	 */
	public StreamCodec<B, C> build() {
		if (decoderFactory == null) {
			throw new IllegalStateException("The decoder factory must be set up");
		}

		return new StreamCodec<>() {
			@Override
			public @NotNull C decode(@NotNull B buffer) {
				List<Object> components = new ArrayList<>(entries.size());
				for (Entry<?, B, C> entry : entries) {
					components.add(entry.codec.decode(buffer));
				}
				return decoderFactory.apply(components.iterator());
			}

			@Override
			public void encode(@NotNull B buffer, @NotNull C container) {
				for (Entry<?, B, C> entry : entries) {
					//noinspection unchecked
					((StreamCodec<B, Object>) entry.codec).encode(buffer, entry.getter.apply(container));
				}
			}
		};
	}

	private record Entry<T, B, C>(StreamCodec<? super B, T> codec, Function<C, T> getter) {
	}
}
