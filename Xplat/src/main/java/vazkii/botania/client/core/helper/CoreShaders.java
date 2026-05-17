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
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.network.TriConsumer;
import vazkii.botania.xplat.BotaniaConfig;

import java.util.function.Consumer;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class CoreShaders {
	@Nullable
	private static ShaderInstance starfieldShaderInstance;
	@Nullable
	private static ShaderInstance gaiaNoiseDynamic;
	@Nullable
	private static ShaderInstance gaiaNoiseConstant;
	@Nullable
	private static ShaderInstance manaPool;
	@Nullable
	private static ShaderInstance terraPlate;
	@Nullable
	private static ShaderInstance enchanter;
	@Nullable
	private static ShaderInstance pylon;
	@Nullable
	private static ShaderInstance halo;
	@Nullable
	private static ShaderInstance filmGrainParticle;
	@Nullable
	private static ShaderInstance gaiaBossBar;

	// This is abstracted this way instead of just directly constructing the ShaderInstance
	// Because Fabric is cute and hides the ResourceProvider from modders (why?)
	public static void init(TriConsumer<ResourceLocation, VertexFormat, Consumer<ShaderInstance>> registrations) {
		registrations.accept(
				botaniaRL("starfield"),
				DefaultVertexFormat.POSITION,
				inst -> starfieldShaderInstance = inst
		);
		registrations.accept(
				botaniaRL("gaia_noise_dynamic"),
				DefaultVertexFormat.NEW_ENTITY,
				inst -> gaiaNoiseDynamic = inst
		);
		registrations.accept(
				botaniaRL("gaia_noise_constant"),
				DefaultVertexFormat.NEW_ENTITY,
				inst -> gaiaNoiseConstant = inst
		);
		registrations.accept(
				botaniaRL("mana_pool"),
				DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
				inst -> manaPool = inst
		);
		registrations.accept(
				botaniaRL("terra_plate_rune"),
				DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
				inst -> terraPlate = inst
		);
		registrations.accept(
				botaniaRL("enchanter_rune"),
				DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
				inst -> enchanter = inst
		);
		registrations.accept(
				botaniaRL("pylon"),
				DefaultVertexFormat.NEW_ENTITY,
				inst -> pylon = inst
		);
		registrations.accept(
				botaniaRL("halo"),
				DefaultVertexFormat.POSITION_TEX_COLOR,
				inst -> halo = inst
		);
		registrations.accept(
				botaniaRL("film_grain_particle"),
				DefaultVertexFormat.PARTICLE,
				inst -> filmGrainParticle = inst
		);
		registrations.accept(
				botaniaRL("gaia_boss_bar"),
				DefaultVertexFormat.POSITION_TEX,
				inst -> gaiaBossBar = inst
		);
	}

	@Nullable
	public static ShaderInstance starfield() {
		// Intended to not respect useShaders config. The render kind of relies entirely
		// on the shader, like the end portal.
		return starfieldShaderInstance;
	}

	@Nullable
	public static ShaderInstance gaiaNoiseDynamic() {
		if (BotaniaConfig.client().useShaders()) {
			return gaiaNoiseDynamic;
		} else {
			return GameRenderer.getRendertypeEntityTranslucentShader();
		}
	}

	@Nullable
	public static ShaderInstance gaiaNoiseConstant() {
		if (BotaniaConfig.client().useShaders()) {
			return gaiaNoiseConstant;
		} else {
			return GameRenderer.getRendertypeEntityTranslucentShader();
		}
	}

	@Nullable
	public static ShaderInstance manaPool() {
		if (BotaniaConfig.client().useShaders()) {
			return manaPool;
		} else {
			return GameRenderer.getPositionColorTexLightmapShader();
		}
	}

	@Nullable
	public static ShaderInstance terraPlate() {
		if (BotaniaConfig.client().useShaders()) {
			return terraPlate;
		} else {
			return GameRenderer.getPositionColorTexLightmapShader();
		}
	}

	@Nullable
	public static ShaderInstance enchanter() {
		if (BotaniaConfig.client().useShaders()) {
			return enchanter;
		} else {
			return GameRenderer.getPositionColorTexLightmapShader();
		}
	}

	@Nullable
	public static ShaderInstance pylon() {
		if (BotaniaConfig.client().useShaders()) {
			return pylon;
		} else {
			return GameRenderer.getRendertypeEntityTranslucentShader();
		}
	}

	@Nullable
	public static ShaderInstance halo() {
		if (BotaniaConfig.client().useShaders()) {
			return halo;
		} else {
			return GameRenderer.getPositionTexColorShader();
		}
	}

	@Nullable
	public static ShaderInstance filmGrainParticle() {
		if (BotaniaConfig.client().useShaders()) {
			return filmGrainParticle;
		} else {
			return GameRenderer.getParticleShader();
		}
	}

	@Nullable
	public static ShaderInstance gaiaBossBar() {
		if (BotaniaConfig.client().useShaders()) {
			return gaiaBossBar;
		} else {
			return GameRenderer.getPositionTexShader();
		}
	}
}
