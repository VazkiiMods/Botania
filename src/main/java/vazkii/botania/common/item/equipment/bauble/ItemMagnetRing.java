/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 17, 2014, 3:16:36 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.subtile.functional.SubTileSolegnolia;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class ItemMagnetRing extends ItemBauble {

	private static final String TAG_COOLDOWN = "cooldown";

	private static final Tag<Item> BLACKLIST = new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "magnet_ring_blacklist"));
	private static final Tag<Block> BLOCK_BLACKLIST = new BlockTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "magnet_ring_blacklist"));

	private final int range;

	public ItemMagnetRing(Properties props) {
		this(props, 6);
		MinecraftForge.EVENT_BUS.addListener(this::onTossItem);
	}

	public ItemMagnetRing(Properties props, int range) {
		super(props);
		this.range = range;
		addPropertyOverride(new ResourceLocation("botania", "on"), (stack, worldIn, entityIn) -> ItemMagnetRing.getCooldown(stack) <= 0 ? 1 : 0);
	}

	private void onTossItem(ItemTossEvent event) {
		ItemStack ring = EquipmentHandler.findOrEmpty(this, event.getPlayer());
		if(!ring.isEmpty()) {
			setCooldown(ring, 100);
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		super.onWornTick(stack, living);

		if(living.isSpectator())
			return;

		int cooldown = getCooldown(stack);

		if(SubTileSolegnolia.hasSolegnoliaAround(living)) {
			if(cooldown < 0)
				setCooldown(stack, 2);
			return;
		}

		if(cooldown <= 0) {
			if(living.isSneaking() == ConfigHandler.COMMON.invertMagnetRing.get()) {
				double x = living.posX;
				double y = living.posY + 0.75;
				double z = living.posZ;

				int range = ((ItemMagnetRing) stack.getItem()).range;
				List<ItemEntity> items = living.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
				int pulled = 0;
				for(ItemEntity item : items)
					if(((ItemMagnetRing) stack.getItem()).canPullItem(item)) {
						if(pulled > 200)
							break;

						MathHelper.setEntityMotionFromVector(item, new Vector3(x, y, z), 0.45F);
						if(living.world.isRemote) {
							boolean red = living.world.rand.nextBoolean();
							Botania.proxy.sparkleFX(item.posX, item.posY, item.posZ, red ? 1F : 0F, 0F, red ? 0F : 1F, 1F, 3);
						}
						pulled++;
					}
			}
		} else setCooldown(stack, cooldown - 1);
	}

	private boolean canPullItem(ItemEntity item) {
		if(!item.isAlive() || item.pickupDelay >= 40 || SubTileSolegnolia.hasSolegnoliaAround(item) || item.getPersistentData().getBoolean("PreventRemoteMovement"))
			return false;

		ItemStack stack = item.getItem();
		if(stack.isEmpty() || stack.getItem() instanceof IManaItem || stack.getItem() instanceof IRelic || BLACKLIST.contains(stack.getItem()))
			return false;

		BlockPos pos = new BlockPos(item);

		if(BLOCK_BLACKLIST.contains(item.world.getBlockState(pos).getBlock()))
			return false;

		if(BLOCK_BLACKLIST.contains(item.world.getBlockState(pos.down()).getBlock()))
			return false;

		return true;
	}

	public static int getCooldown(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
	}

	public static void setCooldown(ItemStack stack, int cooldown) {
		ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cooldown);
	}
}
