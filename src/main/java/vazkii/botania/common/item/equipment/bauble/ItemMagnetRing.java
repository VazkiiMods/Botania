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

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.subtile.functional.SubTileSolegnolia;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;

import java.util.List;

public class ItemMagnetRing extends ItemBauble {

	private static final String TAG_COOLDOWN = "cooldown";

	private static final List<ResourceLocation> BLACKLIST = Lists.newArrayList(new ResourceLocation("appliedenergistics2", "crystal_seed"));

	private final int range;

	public ItemMagnetRing() {
		this(LibItemNames.MAGNET_RING, 6);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public ItemMagnetRing(String name, int range) {
		super(name);
		this.range = range;
		addPropertyOverride(new ResourceLocation("botania", "on"), (stack, worldIn, entityIn) -> ItemMagnetRing.getCooldown(stack) <= 0 ? 1 : 0);
	}

	@SubscribeEvent
	public void onTossItem(ItemTossEvent event) {
		IItemHandler inv = BaublesApi.getBaublesHandler(event.getPlayer());
		for(int i = 0; i < inv.getSlots(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() instanceof ItemMagnetRing) {
				setCooldown(stack, 100);
				BotaniaAPI.internalHandler.sendBaubleUpdatePacket(event.getPlayer(), i);
			}
		}
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase living) {
		super.onWornTick(stack, living);

		if(living instanceof EntityPlayer && ((EntityPlayer) living).isSpectator())
			return;

		int cooldown = getCooldown(stack);

		if(SubTileSolegnolia.hasSolegnoliaAround(living)) {
			if(cooldown < 0)
				setCooldown(stack, 2);
			return;
		}

		if(cooldown <= 0) {
			if(living.isSneaking() == ConfigHandler.invertMagnetRing) {
				double x = living.posX;
				double y = living.posY + 0.75;
				double z = living.posZ;

				List<EntityItem> items = living.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
				int pulled = 0;
				for(EntityItem item : items)
					if(canPullItem(item)) {
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

	private boolean canPullItem(EntityItem item) {
		if(item.isDead || item.pickupDelay >= 40 || SubTileSolegnolia.hasSolegnoliaAround(item) || item.getEntityData().getBoolean("PreventRemoteMovement"))
			return false;

		ItemStack stack = item.getItem();
		if(stack.isEmpty() || stack.getItem() instanceof IManaItem || stack.getItem() instanceof IRelic || BLACKLIST.contains(Item.REGISTRY.getNameForObject(stack.getItem())) || BotaniaAPI.isItemBlacklistedFromMagnet(stack))
			return false;

		BlockPos pos = new BlockPos(item);

		if(BotaniaAPI.isBlockBlacklistedFromMagnet(item.world.getBlockState(pos)))
			return false;

		if(BotaniaAPI.isBlockBlacklistedFromMagnet(item.world.getBlockState(pos.down())))
			return false;

		return true;
	}

	public static int getCooldown(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
	}

	public static void setCooldown(ItemStack stack, int cooldown) {
		ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cooldown);
	}

	public static void addItemToBlackList(String item) {
		BLACKLIST.add(new ResourceLocation(item));
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}


}
