/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;

import java.util.List;

public class ItemGoddessCharm extends ItemBauble implements IManaUsingItem {

	public static final int COST = 1000;

	public ItemGoddessCharm(Settings props) {
		super(props);
	}

	public static void onExplosion(World world, Vec3d vec, List<BlockPos> affectedBlocks) {
		List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, new Box(vec.x, vec.y, vec.z, vec.x, vec.y, vec.z).expand(8));

		for (PlayerEntity player : players) {
			ItemStack charm = EquipmentHandler.findOrEmpty(ModItems.goddessCharm, player);
			if (!charm.isEmpty() && ManaItemHandler.instance().requestManaExact(charm, player, COST, true)) {
				affectedBlocks.clear();
				return;
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(BipedEntityModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, VertexConsumerProvider buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		bipedModel.head.rotate(ms);
		ms.translate(0.275, -0.4, 0);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90F));
		ms.scale(0.55F, -0.55F, -0.55F);
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, ms, buffers);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
