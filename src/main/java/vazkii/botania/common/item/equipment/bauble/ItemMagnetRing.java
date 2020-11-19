/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.mixin.AccessorItemEntity;

import java.util.List;

public class ItemMagnetRing extends ItemBauble {

	private static final String TAG_COOLDOWN = "cooldown";

	private final int range;

	public ItemMagnetRing(Settings props) {
		this(props, 6);
	}

	public ItemMagnetRing(Settings props, int range) {
		super(props);
		this.range = range;
	}

	public static void onTossItem(PlayerEntity player) {
		ItemStack ring = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ItemMagnetRing, player);
		if (!ring.isEmpty()) {
			setCooldown(ring, 100);
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		super.onWornTick(stack, living);

		if (living.isSpectator()) {
			return;
		}

		int cooldown = getCooldown(stack);

		if (BotaniaAPI.instance().hasSolegnoliaAround(living)) {
			if (cooldown < 0) {
				setCooldown(stack, 2);
			}
			return;
		}

		if (cooldown <= 0) {
			if (living.isSneaking() == ConfigHandler.COMMON.invertMagnetRing.getValue()) {
				double x = living.getX();
				double y = living.getY() + 0.75;
				double z = living.getZ();

				int range = ((ItemMagnetRing) stack.getItem()).range;
				List<ItemEntity> items = living.world.getNonSpectatingEntities(ItemEntity.class, new Box(x - range, y - range, z - range, x + range, y + range, z + range));
				int pulled = 0;
				for (ItemEntity item : items) {
					if (((ItemMagnetRing) stack.getItem()).canPullItem(item)) {
						if (pulled > 200) {
							break;
						}

						MathHelper.setEntityMotionFromVector(item, new Vector3(x, y, z), 0.45F);
						if (living.world.isClient) {
							boolean red = living.world.random.nextBoolean();
							float r = red ? 1F : 0F;
							float b = red ? 0F : 1F;
							SparkleParticleData data = SparkleParticleData.sparkle(1F, r, 0F, b, 3);
							living.world.addParticle(data, item.getX(), item.getY(), item.getZ(), 0, 0, 0);
						}
						pulled++;
					}
				}
			}
		} else {
			setCooldown(stack, cooldown - 1);
		}
	}

	private boolean canPullItem(ItemEntity item) {
		int pickupDelay = ((AccessorItemEntity) item).getPickupDelay();
		if (!item.isAlive() || pickupDelay >= 40 || BotaniaAPI.instance().hasSolegnoliaAround(item) /* todo 1.16-fabric || item.getPersistentData().getBoolean("PreventRemoteMovement")*/) {
			return false;
		}

		ItemStack stack = item.getStack();
		if (stack.isEmpty() || stack.getItem() instanceof IManaItem || stack.getItem() instanceof IRelic || ModTags.Items.MAGNET_RING_BLACKLIST.contains(stack.getItem())) {
			return false;
		}

		BlockPos pos = item.getBlockPos();

		if (ModTags.Blocks.MAGNET_RING_BLACKLIST.contains(item.world.getBlockState(pos).getBlock())) {
			return false;
		}

		if (ModTags.Blocks.MAGNET_RING_BLACKLIST.contains(item.world.getBlockState(pos.down()).getBlock())) {
			return false;
		}

		return true;
	}

	public static int getCooldown(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
	}

	public static void setCooldown(ItemStack stack, int cooldown) {
		ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cooldown);
	}
}
