/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.helper;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class CoreShaders {
	private static ShaderInstance starfieldShaderInstance;
	private static ShaderInstance doppleganger;

	public static void init(ResourceManager resourceManager,
			List<Pair<ShaderInstance, Consumer<ShaderInstance>>> registrations) throws IOException {
		registrations.add(Pair.of(
				new ShaderInstance(resourceManager, "botania__rendertype_starfield", DefaultVertexFormat.POSITION),
				inst -> starfieldShaderInstance = inst)
		);
		registrations.add(Pair.of(
				new ShaderInstance(resourceManager, "botania__doppleganger", DefaultVertexFormat.NEW_ENTITY),
				inst -> doppleganger = inst)
		);
	}

	public static ShaderInstance starfield() {
		return starfieldShaderInstance;
	}

	public static ShaderInstance doppleganger() {
		return doppleganger;
	}
}
