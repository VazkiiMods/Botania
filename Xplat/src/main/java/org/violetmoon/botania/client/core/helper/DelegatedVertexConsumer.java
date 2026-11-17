/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.helper;

import com.mojang.blaze3d.vertex.VertexConsumer;

/**
 * Dirty hack to override some vertex data in {@link RenderHelper}.
 */
class DelegatedVertexConsumer implements VertexConsumer {
	private final VertexConsumer delegate;

	DelegatedVertexConsumer(VertexConsumer delegate) {
		this.delegate = delegate;
	}

	@Override
	public VertexConsumer addVertex(float x, float y, float z) {
		return delegate.addVertex(x, y, z);
	}

	@Override
	public VertexConsumer setColor(int red, int green, int blue, int alpha) {
		return delegate.setColor(red, green, blue, alpha);
	}

	@Override
	public VertexConsumer setUv(float u, float v) {
		return delegate.setUv(u, v);
	}

	@Override
	public VertexConsumer setUv1(int u, int v) {
		return delegate.setUv1(u, v);
	}

	@Override
	public VertexConsumer setUv2(int u, int v) {
		return delegate.setUv2(u, v);
	}

	@Override
	public VertexConsumer setNormal(float x, float y, float z) {
		return delegate.setNormal(x, y, z);
	}
}
