/**
 * This class was created by <Flaxbeard>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 25, 2014, 2:57:16 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityThrownItem;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemGravityRod extends ItemMod implements IManaUsingItem {

	private static final float RANGE = 3F;
	private static final int COST = 2;

	private static final String TAG_TICKS_TILL_EXPIRE = "ticksTillExpire";
	private static final String TAG_TICKS_COOLDOWN = "ticksCooldown";
	private static final String TAG_TARGET = "target";
	private static final String TAG_DIST = "dist";

	public ItemGravityRod() {
		super(LibItemNames.GRAVITY_ROD);
		setMaxStackSize(1);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
		return newStack.getItem() != this;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity par3Entity, int slot, boolean held) {
		if(!(par3Entity instanceof EntityPlayer))
			return;

		int ticksTillExpire = ItemNBTHelper.getInt(stack, TAG_TICKS_TILL_EXPIRE, 0);
		int ticksCooldown = ItemNBTHelper.getInt(stack, TAG_TICKS_COOLDOWN, 0);

		if(ticksTillExpire == 0) {
			ItemNBTHelper.setInt(stack, TAG_TARGET, -1);
			ItemNBTHelper.setDouble(stack, TAG_DIST, -1);
		}

		if(ticksCooldown > 0)
			ticksCooldown--;

		ticksTillExpire--;
		ItemNBTHelper.setInt(stack, TAG_TICKS_TILL_EXPIRE, ticksTillExpire);
		ItemNBTHelper.setInt(stack, TAG_TICKS_COOLDOWN, ticksCooldown);

		EntityPlayer player = (EntityPlayer) par3Entity;
		PotionEffect haste = player.getActivePotionEffect(MobEffects.HASTE);
		float check = haste == null ? 0.16666667F : haste.getAmplifier() == 1 ? 0.5F : 0.4F;
		if(player.getHeldItemMainhand() == stack && player.swingProgress == check && !world.isRemote)
			leftClick(player);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		int targetID = ItemNBTHelper.getInt(stack, TAG_TARGET, -1);
		int ticksCooldown = ItemNBTHelper.getInt(stack, TAG_TICKS_COOLDOWN, 0);
		double length = ItemNBTHelper.getDouble(stack, TAG_DIST, -1);

		if(ticksCooldown == 0) {
			Entity item = null;
			if(targetID != -1 && player.world.getEntityByID(targetID) != null) {
				Entity taritem = player.world.getEntityByID(targetID);

				boolean found = false;
				Vector3 target = Vector3.fromEntityCenter(player);
				List<Entity> entities = new ArrayList<>();
				int distance = 1;
				while(entities.size() == 0 && distance < 25) {
					target = target.add(new Vector3(player.getLookVec()).multiply(distance)).add(0, 0.5, 0);
					entities = player.world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(target.x - RANGE, target.y - RANGE, target.z - RANGE, target.x + RANGE, target.y + RANGE, target.z + RANGE));
					distance++;
					if(entities.contains(taritem))
						found = true;
				}

				if(found)
					item = player.world.getEntityByID(targetID);
			}

			if(item == null) {
				Vector3 target = Vector3.fromEntityCenter(player);
				List<Entity> entities = new ArrayList<>();
				int distance = 1;
				while(entities.size() == 0 && distance < 25) {
					target = target.add(new Vector3(player.getLookVec()).multiply(distance)).add(0, 0.5, 0);
					entities = player.world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(target.x - RANGE, target.y - RANGE, target.z - RANGE, target.x + RANGE, target.y + RANGE, target.z + RANGE));
					distance++;
				}

				if(entities.size() > 0) {
					item = entities.get(0);
					length = 5.5D;
					if(item instanceof EntityItem)
						length = 2.0D;
				}
			}

			if(item != null) {
				if(BotaniaAPI.isEntityBlacklistedFromGravityRod(item.getClass()))
					return ActionResult.newResult(EnumActionResult.FAIL, stack);

				if(ManaItemHandler.requestManaExactForTool(stack, player, COST, true)) {
					if(item instanceof EntityItem)
						((EntityItem) item).setPickupDelay(5);

					if(item instanceof EntityLivingBase) {
						EntityLivingBase targetEntity = (EntityLivingBase)item;
						targetEntity.fallDistance = 0.0F;
						if(targetEntity.getActivePotionEffect(MobEffects.SLOWNESS) == null)
							targetEntity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 2, 3, true, true));
					}

					Vector3 target3 = Vector3.fromEntityCenter(player)
							.add(new Vector3(player.getLookVec()).multiply(length)).add(0, 0.5, 0);
					if(item instanceof EntityItem)
						target3 = target3.add(0, 0.25, 0);

					for(int i = 0; i < 4; i++) {
						float r = 0.5F + (float) Math.random() * 0.5F;
						float b = 0.5F + (float) Math.random() * 0.5F;
						float s = 0.2F + (float) Math.random() * 0.1F;
						float m = 0.1F;
						float xm = ((float) Math.random() - 0.5F) * m;
						float ym = ((float) Math.random() - 0.5F) * m;
						float zm = ((float) Math.random() - 0.5F) * m;
						Botania.proxy.wispFX(item.posX + item.width / 2, item.posY + item.height / 2, item.posZ + item.width / 2, r, 0F, b, s, xm, ym, zm);
					}

					MathHelper.setEntityMotionFromVector(item, target3, 0.3333333F);

					ItemNBTHelper.setInt(stack, TAG_TARGET, item.getEntityId());
					ItemNBTHelper.setDouble(stack, TAG_DIST, length);
				}

				ItemNBTHelper.setInt(stack, TAG_TICKS_TILL_EXPIRE, 5);
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}
		}
		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	public static void leftClick(EntityPlayer player) {
		ItemStack stack = player.getHeldItemMainhand();
		if(!stack.isEmpty() && stack.getItem() == ModItems.gravityRod) {
			int targetID = ItemNBTHelper.getInt(stack, TAG_TARGET, -1);
			ItemNBTHelper.getDouble(stack, TAG_DIST, -1);
			Entity item;

			if(targetID != -1 && player.world.getEntityByID(targetID) != null) {
				Entity taritem = player.world.getEntityByID(targetID);

				boolean found = false;
				Vector3 target = Vector3.fromEntityCenter(player);
				List<Entity> entities = new ArrayList<>();
				int distance = 1;
				while(entities.size() == 0 && distance < 25) {
					target = target.add(new Vector3(player.getLookVec()).multiply(distance)).add(0, 0.5, 0);
					entities = player.world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(target.x - RANGE, target.y - RANGE, target.z - RANGE, target.x + RANGE, target.y + RANGE, target.z + RANGE));
					distance++;
					if(entities.contains(taritem))
						found = true;
				}

				if(found) {
					item = player.world.getEntityByID(targetID);
					ItemNBTHelper.setInt(stack, TAG_TARGET, -1);
					ItemNBTHelper.setDouble(stack, TAG_DIST, -1);
					Vector3 moveVector = new Vector3(player.getLookVec().normalize());
					if(item instanceof EntityItem) {
						((EntityItem) item).setPickupDelay(20);
						float mot = IManaProficiencyArmor.Helper.hasProficiency(player, stack) ? 2.25F : 1.5F;
						item.motionX = moveVector.x * mot;
						item.motionY = moveVector.y;
						item.motionZ = moveVector.z * mot;
						if(!player.world.isRemote) {
							EntityThrownItem thrown = new EntityThrownItem(item.world, item.posX, item.posY, item.posZ, (EntityItem) item);
							item.world.spawnEntity(thrown);
						}
						item.setDead();
					} else {
						item.motionX = moveVector.x * 3.0F;
						item.motionY = moveVector.y * 1.5F;
						item.motionZ = moveVector.z * 3.0F;
					}
					ItemNBTHelper.setInt(stack, TAG_TICKS_COOLDOWN, 10);
				}
			}
		}
	}
}