package vazkii.botania.client.render.entity;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import vazkii.botania.client.core.handler.ContributorFancinessHandler;
import vazkii.botania.client.core.handler.LayerTerraHelmet;
import vazkii.botania.client.core.handler.ManaTabletRenderHandler;
import vazkii.botania.common.entity.ModEntities;

import java.util.function.Consumer;

public final class EntityRenderers {
	public interface EntityRendererConsumer {
		<E extends Entity> void accept(EntityType<? extends E> entityType,
				EntityRendererProvider<E> entityRendererFactory);
	}

	public static void init(EntityRendererConsumer consumer) {
		consumer.accept(ModEntities.MANA_BURST, NoopRenderer::new);
		consumer.accept(ModEntities.PLAYER_MOVER, NoopRenderer::new);
		consumer.accept(ModEntities.FLAME_RING, NoopRenderer::new);
		consumer.accept(ModEntities.MAGIC_LANDMINE, RenderMagicLandmine::new);
		consumer.accept(ModEntities.MAGIC_MISSILE, NoopRenderer::new);
		consumer.accept(ModEntities.FALLING_STAR, NoopRenderer::new);
		consumer.accept(ModEntities.ENDER_AIR, NoopRenderer::new);
		consumer.accept(ModEntities.THROWN_ITEM, ItemEntityRenderer::new);
		consumer.accept(ModEntities.PIXIE, RenderPixie::new);
		consumer.accept(ModEntities.DOPPLEGANGER, RenderDoppleganger::new);
		consumer.accept(ModEntities.SPARK, RenderManaSpark::new);
		consumer.accept(ModEntities.CORPOREA_SPARK, RenderCorporeaSpark::new);
		consumer.accept(ModEntities.POOL_MINECART, RenderPoolMinecart::new);
		consumer.accept(ModEntities.PINK_WITHER, RenderPinkWither::new);
		consumer.accept(ModEntities.MANA_STORM, RenderManaStorm::new);
		consumer.accept(ModEntities.BABYLON_WEAPON, RenderBabylonWeapon::new);

		consumer.accept(ModEntities.THORN_CHAKRAM, ThrownItemRenderer::new);
		consumer.accept(ModEntities.VINE_BALL, ThrownItemRenderer::new);
		consumer.accept(ModEntities.ENDER_AIR_BOTTLE, ThrownItemRenderer::new);
	}

	public static void addAuxiliaryPlayerRenders(PlayerRenderer renderer,
			Consumer<RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>> consumer) {
		consumer.accept(new ContributorFancinessHandler(renderer));
		consumer.accept(new ManaTabletRenderHandler(renderer));
		consumer.accept(new LayerTerraHelmet(renderer));
	}

	private EntityRenderers() {}
}
