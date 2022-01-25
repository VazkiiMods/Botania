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
	public VertexConsumer vertex(double x, double y, double z) {
		return delegate.vertex(x, y, z);
	}

	@Override
	public VertexConsumer color(int red, int green, int blue, int alpha) {
		return delegate.color(red, green, blue, alpha);
	}

	@Override
	public VertexConsumer uv(float u, float v) {
		return delegate.uv(u, v);
	}

	@Override
	public VertexConsumer overlayCoords(int u, int v) {
		return delegate.overlayCoords(u, v);
	}

	@Override
	public VertexConsumer uv2(int u, int v) {
		return delegate.uv2(u, v);
	}

	@Override
	public VertexConsumer normal(float x, float y, float z) {
		return delegate.normal(x, y, z);
	}

	@Override
	public void endVertex() {
		delegate.endVertex();
	}

	@Override
	public void defaultColor(int red, int green, int blue, int alpha) {
		delegate.defaultColor(red, green, blue, alpha);
	}

	@Override
	public void unsetDefaultColor() {
		delegate.unsetDefaultColor();
	}
}
