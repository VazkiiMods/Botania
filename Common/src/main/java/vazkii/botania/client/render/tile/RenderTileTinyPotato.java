/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.item.TinyPotatoRenderCallback;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.core.handler.ContributorList;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockTinyPotato;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.mixin.client.AccessorModelManager;

import javax.annotation.Nonnull;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class RenderTileTinyPotato implements BlockEntityRenderer<TileTinyPotato> {
	public static final String DEFAULT = "default";
	public static final String HALLOWEEN = "halloween";
	private static final Pattern ESCAPED = Pattern.compile("[^a-z0-9/._-]");
	private final BlockRenderDispatcher blockRenderDispatcher;

	public RenderTileTinyPotato(BlockEntityRendererProvider.Context ctx) {
		this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
	}

	private static boolean matches(String name, String match) {
		return name.equals(match) || name.startsWith(match + " ");
	}

	private static String removeFromFront(String name, String match) {
		return name.substring(match.length()).trim();
	}

	public static BakedModel getModelFromDisplayName(Component displayName) {
		var name = displayName.getString().trim().toLowerCase(Locale.ROOT);
		if (matches(name, "enchanted")) {
			name = removeFromFront(name, "enchanted");
		}
		return getModel(name);
	}

	private static BakedModel getModel(String name) {
		ModelManager bmm = Minecraft.getInstance().getModelManager();
		Map<ResourceLocation, BakedModel> mm = ((AccessorModelManager) bmm).getBakedRegistry();
		BakedModel missing = bmm.getMissingModel();
		ResourceLocation location = taterLocation(name);
		BakedModel model = mm.get(location);
		if (model == null) {
			if (ClientProxy.dootDoot) {
				return mm.getOrDefault(taterLocation(HALLOWEEN), missing);
			} else {
				return mm.getOrDefault(taterLocation(DEFAULT), missing);
			}
		}
		return model;
	}

	private static ResourceLocation taterLocation(String name) {
		return prefix(LibResources.PREFIX_TINY_POTATO + "/" + normalizeName(name));
	}

	private static String normalizeName(String name) {
		return ESCAPED.matcher(name).replaceAll("_");
	}

	@Override
	public void render(@Nonnull TileTinyPotato potato, float partialTicks, PoseStack ms, @Nonnull MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();

		String name = potato.name.getString().toLowerCase(Locale.ROOT).trim();
		boolean enchanted = matches(name, "enchanted");
		if (enchanted) {
			name = removeFromFront(name, "enchanted");
		}
		RenderType layer = Sheets.translucentCullBlockSheet();
		BakedModel model = getModel(name);

		ms.translate(0.5F, 0F, 0.5F);
		Direction potatoFacing = potato.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		float rotY = 0;
		switch (potatoFacing) {
			default:
			case SOUTH:
				rotY = 180F;
				break;
			case NORTH:
				break;
			case EAST:
				rotY = 90F;
				break;
			case WEST:
				rotY = 270F;
				break;
		}
		ms.mulPose(Vector3f.YN.rotationDegrees(rotY));

		float jump = potato.jumpTicks;
		if (jump > 0) {
			jump -= partialTicks;
		}

		float up = (float) Math.abs(Math.sin(jump / 10 * Math.PI)) * 0.2F;
		float rotZ = (float) Math.sin(jump / 10 * Math.PI) * 2;
		float wiggle = (float) Math.sin(jump / 10 * Math.PI) * 0.05F;

		ms.translate(wiggle, up, 0F);
		ms.mulPose(Vector3f.ZP.rotationDegrees(rotZ));

		boolean render = !(name.equals("mami") || name.equals("soaryn") || name.equals("eloraam") && jump != 0);
		if (render) {
			ms.pushPose();
			ms.translate(-0.5F, 0, -0.5F);
			VertexConsumer buffer = ItemRenderer.getFoilBuffer(buffers, layer, true, enchanted);

			renderModel(ms, buffer, light, overlay, model);
			ms.popPose();
		}

		ms.translate(0F, 1.5F, 0F);
		ms.pushPose();
		ms.mulPose(Vector3f.ZP.rotationDegrees(180F));
		renderItems(potato, potatoFacing, name, partialTicks, ms, buffers, light, overlay);

		ms.pushPose();
		TinyPotatoRenderCallback.EVENT.invoker().onRender(potato, potato.name, partialTicks, ms, buffers, light, overlay);
		ms.popPose();
		ms.popPose();

		ms.mulPose(Vector3f.ZP.rotationDegrees(-rotZ));
		ms.mulPose(Vector3f.YN.rotationDegrees(-rotY));

		renderName(potato, name, ms, buffers, light);
		ms.popPose();
	}

	private void renderName(TileTinyPotato potato, String name, PoseStack ms, MultiBufferSource buffers, int light) {
		Minecraft mc = Minecraft.getInstance();
		HitResult pos = mc.hitResult;
		if (Minecraft.renderNames()
				&& !name.isEmpty() && pos != null && pos.getType() == HitResult.Type.BLOCK
				&& potato.getBlockPos().equals(((BlockHitResult) pos).getBlockPos())) {
			ms.pushPose();
			ms.translate(0F, -0.6F, 0F);
			ms.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
			float f1 = 0.016666668F * 1.6F;
			ms.scale(-f1, -f1, f1);
			int halfWidth = mc.font.width(potato.name.getString()) / 2;

			float opacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
			int opacityRGB = (int) (opacity * 255.0F) << 24;
			mc.font.drawInBatch(potato.name, -halfWidth, 0, 0x20FFFFFF, false, ms.last().pose(), buffers, true, opacityRGB, light);
			mc.font.drawInBatch(potato.name, -halfWidth, 0, 0xFFFFFFFF, false, ms.last().pose(), buffers, false, 0, light);
			if (name.equals("pahimar") || name.equals("soaryn")) {
				ms.translate(0F, 14F, 0F);
				String str = name.equals("pahimar") ? "[WIP]" : "(soon)";
				halfWidth = mc.font.width(str) / 2;

				mc.font.drawInBatch(str, -halfWidth, 0, 0x20FFFFFF, false, ms.last().pose(), buffers, true, opacityRGB, light);
				mc.font.drawInBatch(str, -halfWidth, 0, 0xFFFFFFFF, false, ms.last().pose(), buffers, true, 0, light);
			}

			ms.popPose();
		}
	}

	private void renderItems(TileTinyPotato potato, Direction facing, String name, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();
		ms.mulPose(Vector3f.ZP.rotationDegrees(180F));
		ms.translate(0F, -1F, 0F);
		float s = 1F / 3.5F;
		ms.scale(s, s, s);

		for (int i = 0; i < potato.inventorySize(); i++) {
			ItemStack stack = potato.getItemHandler().getItem(i);
			if (stack.isEmpty()) {
				continue;
			}

			ms.pushPose();
			Direction side = Direction.values()[i];
			if (side.getAxis() != Axis.Y) {
				float sideAngle = side.toYRot() - facing.toYRot();
				side = Direction.fromYRot(sideAngle);
			}

			boolean block = stack.getItem() instanceof BlockItem;
			boolean mySon = stack.getItem() instanceof ItemBlockTinyPotato;

			switch (side) {
				case UP -> {
					if (mySon) {
						ms.translate(0F, 0.6F, 0.5F);
					} else if (block) {
						ms.translate(0F, 0.3F, 0.5F);
					}
					ms.translate(0F, -0.5F, -0.4F);
				}
				case DOWN -> {
					ms.translate(0, -2.3F, -0.88F);
					if (mySon) {
						ms.translate(0, .65F, 0.6F);
					} else if (block) {
						ms.translate(0, 1, 0.6F);
					}
				}
				case NORTH -> {
					ms.translate(0, -1.9F, 0.02F);
					if (mySon) {
						ms.translate(0, 1, 0.6F);
					} else if (block) {
						ms.translate(0, 1, 0.6F);
					}
				}
				case SOUTH -> {
					ms.translate(0, -1.6F, -0.89F);
					if (mySon) {
						ms.translate(0, 1.4F, 0.5F);
					} else if (block) {
						ms.translate(0, 1.0F, 0.5F);
					}
				}
				case EAST -> {
					if (mySon) {
						ms.translate(-0.4F, 0.65F, 0F);
					} else if (block) {
						ms.translate(-0.4F, 0.8F, 0F);
					} else {
						ms.mulPose(Vector3f.YP.rotationDegrees(-90F));
					}
					ms.translate(-0.3F, -1.9F, 0.04F);
				}
				case WEST -> {
					if (mySon) {
						ms.translate(1F, 0.65F, 1F);
					} else if (block) {
						ms.translate(1F, 0.8F, 1F);
					} else {
						ms.mulPose(Vector3f.YP.rotationDegrees(-90F));
					}
					ms.translate(-0.3F, -1.9F, -0.92F);
				}
			}

			if (mySon) {
				ms.scale(1.1F, 1.1F, 1.1F);
			} else if (block) {
				ms.scale(0.5F, 0.5F, 0.5F);
			}
			if (block && side == Direction.NORTH) {
				ms.mulPose(Vector3f.YP.rotationDegrees(180F));
			}
			renderItem(ms, buffers, light, overlay, stack);
			ms.popPose();
		}
		ms.popPose();

		ms.pushPose();
		if (!name.isEmpty()) {
			ContributorList.firstStart();

			float scale = 1F / 4F;
			ms.translate(0F, 1F, 0F);
			ms.scale(scale, scale, scale);
			switch (name) {
				case "phi", "vazkii" -> {
					ms.pushPose();
					ms.translate(-0.15, 0.1, 0.4);
					ms.mulPose(Vector3f.YP.rotationDegrees(90F));
					ms.mulPose(new Vector3f(1, 0, 1).rotationDegrees(20));
					renderModel(ms, buffers, light, overlay, MiscellaneousIcons.INSTANCE.phiFlowerModel);
					ms.popPose();
					if (name.equals("vazkii")) {
						ms.scale(1.25F, 1.25F, 1.25F);
						ms.mulPose(Vector3f.XP.rotationDegrees(180F));
						ms.mulPose(Vector3f.YP.rotationDegrees(-90F));
						ms.translate(0.2, -1.25, 0);
						renderModel(ms, buffers, light, overlay, MiscellaneousIcons.INSTANCE.nerfBatModel);
					}
				}
				case "haighyorkie" -> {
					ms.scale(1.25F, 1.25F, 1.25F);
					ms.mulPose(Vector3f.ZP.rotationDegrees(180F));
					ms.mulPose(Vector3f.YP.rotationDegrees(-90F));
					ms.translate(-0.5F, -1.2F, -0.075F);
					renderModel(ms, buffers, light, overlay, MiscellaneousIcons.INSTANCE.goldfishModel);
				}
				case "martysgames", "marty" -> {
					ms.scale(0.7F, 0.7F, 0.7F);
					ms.mulPose(Vector3f.ZP.rotationDegrees(180F));
					ms.translate(-0.3F, -2.7F, -1.2F);
					ms.mulPose(Vector3f.ZP.rotationDegrees(15F));
					renderItem(ms, buffers, light, overlay, new ItemStack(ModItems.infiniteFruit, 1).setHoverName(new TextComponent("das boot")));
				}
				case "jibril" -> {
					ms.scale(1.5F, 1.5F, 1.5F);
					ms.translate(0F, 0.8F, 0F);
					ItemFlightTiara.ClientLogic.renderHalo(null, null, ms, buffers, partialTicks);
				}
				case "kingdaddydmac" -> {
					ms.scale(0.5F, 0.5F, 0.5F);
					ms.mulPose(Vector3f.ZP.rotationDegrees(180));
					ms.mulPose(Vector3f.YP.rotationDegrees(90));
					ms.pushPose();
					ms.translate(0F, -2.5F, 0.65F);
					ItemStack ring = new ItemStack(ModItems.manaRing);
					renderItem(ms, buffers, light, overlay, ring);
					ms.translate(0F, 0F, -4F);
					renderItem(ms, buffers, light, overlay, ring);
					ms.popPose();
					ms.translate(1.5, -4, -2.5);
					renderBlock(ms, buffers, light, overlay, Blocks.CAKE);
				}
				default -> {
					ItemStack icon = ContributorList.getFlower(name);
					if (!icon.isEmpty()) {
						ms.mulPose(Vector3f.XP.rotationDegrees(180));
						ms.mulPose(Vector3f.YP.rotationDegrees(180));
						ms.translate(0, -0.75, -0.5);
						Minecraft.getInstance().getItemRenderer().renderStatic(icon, ItemTransforms.TransformType.HEAD,
								light, overlay, ms, buffers, 0);
					}
				}
			}
		}
		ms.popPose();
	}

	private void renderModel(PoseStack ms, MultiBufferSource buffers, int light, int overlay, BakedModel model) {
		renderModel(ms, buffers.getBuffer(Sheets.translucentCullBlockSheet()), light, overlay, model);
	}

	private void renderModel(PoseStack ms, VertexConsumer buffer, int light, int overlay, BakedModel model) {
		blockRenderDispatcher.getModelRenderer().renderModel(ms.last(), buffer, null, model, 1, 1, 1, light, overlay);
	}

	private void renderItem(PoseStack ms, MultiBufferSource buffers, int light, int overlay, ItemStack stack) {
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.HEAD,
				light, overlay, ms, buffers, 0);
	}

	private void renderBlock(PoseStack ms, MultiBufferSource buffers, int light, int overlay, Block block) {
		blockRenderDispatcher.renderSingleBlock(block.defaultBlockState(), ms, buffers, light, overlay);
	}
}
