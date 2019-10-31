/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 29, 2015, 10:13:26 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;

public class ItemFlugelEye extends ItemRelic implements ICoordBoundItem, IManaUsingItem {

	public ItemFlugelEye(Properties props) {
		super(props);
	}

	private static final String TAG_X = "x";
	private static final String TAG_Y = "y";
	private static final String TAG_Z = "z";
	private static final String TAG_DIMENSION = "dim";

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		PlayerEntity player = ctx.getPlayer();

		if(player != null && player.isSneaking()) {
			if(world.isRemote) {
				for(int i = 0; i < 10; i++) {
					float x1 = (float) (pos.getX() + Math.random());
					float y1 = pos.getY() + 1;
					float z1 = (float) (pos.getZ() + Math.random());
                    WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.5F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
                    world.addParticle(data, x1, y1, z1, 0, -(-0.05F + (float) Math.random() * 0.05F), 0);
                }
			} else {
				ItemStack stack = ctx.getItem();
				ItemNBTHelper.setInt(stack, TAG_X, pos.getX());
				ItemNBTHelper.setInt(stack, TAG_Y, pos.getY());
				ItemNBTHelper.setInt(stack, TAG_Z, pos.getZ());
				ItemNBTHelper.setString(stack, TAG_DIMENSION, world.getDimension().getType().toString());
				world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 5F);
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
		float x = (float) (living.posX - Math.random() * living.getWidth());
		float y = (float) (living.posY + Math.random());
		float z = (float) (living.posZ - Math.random() * living.getWidth());
        WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.7F, (float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
        living.world.addParticle(data, x, y, z, 0, 0.05F + (float) Math.random() * 0.05F, 0);
    }

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		return ActionResult.newResult(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, LivingEntity living) {
		if(!(living instanceof PlayerEntity))
			return stack;

		PlayerEntity player = (PlayerEntity) living;
		int x = ItemNBTHelper.getInt(stack, TAG_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_Z, 0);
		DimensionType dim = DimensionType.byName(new ResourceLocation(ItemNBTHelper.getString(stack, TAG_DIMENSION, "minecraft:overworld")));

		int cost = (int) (MathHelper.pointDistanceSpace(x + 0.5, y + 0.5, z + 0.5, player.posX, player.posY, player.posZ) * 10);

		if(y > -1 && dim == world.getDimension().getType() && ManaItemHandler.requestManaExact(stack, player, cost, true)) {
			moveParticlesAndSound(player);
			if(player instanceof ServerPlayerEntity)
				((ServerPlayerEntity) player).connection.setPlayerLocation(x + 0.5, y + 1.6, z + 0.5, player.rotationYaw, player.rotationPitch);
			moveParticlesAndSound(player);
		}

		return stack;
	}

	private static void moveParticlesAndSound(Entity entity) {
		PacketHandler.sendToNearby(entity.world, entity, new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.FLUGEL_EFFECT, entity.posX, entity.posY, entity.posZ, entity.getEntityId()));
		entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
	}

	@Override
	public int getUseDuration(ItemStack par1ItemStack) {
		return 40;
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack par1ItemStack) {
		return UseAction.BOW;
	}

	@Override
	public BlockPos getBinding(ItemStack stack) {
		int x = ItemNBTHelper.getInt(stack, TAG_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_Z, 0);
		return y == -1 ? null : new BlockPos(x, y, z);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public ResourceLocation getAdvancement() {
		return new ResourceLocation(LibMisc.MOD_ID, "challenge/flugel_eye");
	}

}
