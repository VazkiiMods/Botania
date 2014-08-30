/**
 * This class was created by <Flaxbeard>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Aug 25, 2014, 2:57:16 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityThrownItem;
import vazkii.botania.common.lib.LibItemNames;

public class ItemGravityRod extends ItemMod implements IManaUsingItem {

	static final int COST = 1; //CHANGE THIS

	public ItemGravityRod() {
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.GRAVITY_ROD);
	}
	
	@Override
    public void onUpdate(ItemStack stack, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		if (!stack.stackTagCompound.hasKey("ticksTillExpire")) {
			stack.stackTagCompound.setInteger("ticksTillExpire", 0);
		}
		if (!stack.stackTagCompound.hasKey("ticksCooldown")) {
			stack.stackTagCompound.setInteger("ticksCooldown", 0);
		}
		int ticksTillExpire = stack.stackTagCompound.getInteger("ticksTillExpire");	
		int ticksCooldown = stack.stackTagCompound.getInteger("ticksCooldown");

		if (ticksTillExpire == 0) {
			stack.stackTagCompound.setInteger("target", -1);
			stack.stackTagCompound.setDouble("dist", -1);
		}

		if (ticksCooldown > 0) {
			ticksCooldown--;
		}
		ticksTillExpire--;
		stack.stackTagCompound.setInteger("ticksTillExpire", ticksTillExpire);
		stack.stackTagCompound.setInteger("ticksCooldown", ticksCooldown);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		if (!stack.stackTagCompound.hasKey("ticksTillExpire")) {
			stack.stackTagCompound.setInteger("ticksTillExpire", 0);
		}
		if (!stack.stackTagCompound.hasKey("target")) {
			stack.stackTagCompound.setInteger("target", -1);
		}
		if (!stack.stackTagCompound.hasKey("ticksCooldown")) {
			stack.stackTagCompound.setInteger("ticksCooldown", 0);
		}
		if (!stack.stackTagCompound.hasKey("dist")) {
			stack.stackTagCompound.setDouble("dist", -1);
		}
		int targetID = stack.stackTagCompound.getInteger("target");
		int ticksCooldown = stack.stackTagCompound.getInteger("ticksCooldown");
		double length = stack.stackTagCompound.getDouble("dist");
		//length = 15;
		if (ticksCooldown == 0) {
			Entity item = null;
			if (targetID != -1 && player.worldObj.getEntityByID(targetID) != null) {
				Entity taritem = player.worldObj.getEntityByID(targetID);
	
				boolean found = false;
				Vector3 target = Vector3.fromEntityCenter(player);
				List<Entity> entities = new ArrayList<Entity>();
				int distance = 1;
				while (entities.size() == 0 && distance < 25) {
					
					final double range = 3F;
					target.add(new Vector3(player.getLookVec()).multiply(distance));
			
					target.y += 0.5;
					entities = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getBoundingBox(target.x - range, target.y - range, target.z - range, target.x + range, target.y + range, target.z + range));
					distance++;
					if (entities.contains(taritem)) {
						found = true;
					}
				}
				
				if (found) {
					item = player.worldObj.getEntityByID(targetID);
				}
				
			}
			
			if (item == null)
			{
				Vector3 target = Vector3.fromEntityCenter(player);
				List<Entity> entities = new ArrayList<Entity>();
				int distance = 1;
				while (entities.size() == 0 && distance < 25) {
		
					final double range = 3F;
					target.add(new Vector3(player.getLookVec()).multiply(distance));
			
					target.y += 0.5;
					entities = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getBoundingBox(target.x - range, target.y - range, target.z - range, target.x + range, target.y + range, target.z + range));
					distance++;
				}
				
				if (entities.size() > 0) {
					item = entities.get(0);
//					Vector3 target2 = new Vector3(item.posX, item.posY, item.posZ);
//					target2.subtract(Vector3.fromEntityCenter(player));
//					length = target2.mag();
//					length = Math.max(5, length);
					length = 5.5D;
					if (item instanceof EntityItem) {
						length = 2.0D;
					}
				}
			}
			if (ManaItemHandler.requestManaExact(stack, player, COST, true) && item != null) {
				if (item instanceof EntityItem) {
					((EntityItem)item).delayBeforeCanPickup = 5;
				}
				if (item instanceof EntityLivingBase) {
					EntityLivingBase targetEntity = ((EntityLivingBase)item);
					targetEntity.fallDistance = 0.0F;
					if (targetEntity.getActivePotionEffect(Potion.moveSlowdown) == null) {
						targetEntity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 2, 3, true));
					}
				}
				Vector3 target3 = Vector3.fromEntityCenter(player);
				target3.add(new Vector3(player.getLookVec()).multiply(length));
				target3.y += 0.5;
				if (item instanceof EntityItem) {
					target3.y += 0.25;
				}
	
				setEntityMotionFromVector(item, target3, 0.3333F);
				
				stack.stackTagCompound.setInteger("target", item.getEntityId());
				stack.stackTagCompound.setDouble("dist", length);
			}
		
			if (item != null) {
				stack.stackTagCompound.setInteger("ticksTillExpire", 5);
			}
		}
		return stack;
	}
	
	public static void setEntityMotionFromVector(Entity entity, Vector3 originalPosVector, float modifier) {
		Vector3 entityVector = Vector3.fromEntityCenter(entity);
		Vector3 finalVector = originalPosVector.copy().subtract(entityVector);

		if (finalVector.mag() > 1)
			finalVector.normalize();

		entity.motionX = finalVector.x * modifier;
		entity.motionY = finalVector.y * modifier;
		entity.motionZ = finalVector.z * modifier;
	}
	
	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	public static void leftClick(EntityPlayer player) {
		ItemStack stack = player.getHeldItem();
		if (stack != null && stack.getItem() == ModItems.gravityRod) {
			int targetID = stack.stackTagCompound.getInteger("target");
			double length = stack.stackTagCompound.getDouble("dist");
			Entity item = null;
			if (targetID != -1 && player.worldObj.getEntityByID(targetID) != null) {
				Entity taritem = player.worldObj.getEntityByID(targetID);

				boolean found = false;
				Vector3 target = Vector3.fromEntityCenter(player);
				List<Entity> entities = new ArrayList<Entity>();
				int distance = 1;
				while (entities.size() == 0 && distance < 25) {
					
					final double range = 3F;
					target.add(new Vector3(player.getLookVec()).multiply(distance));
			
					target.y += 0.5;
					entities = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getBoundingBox(target.x - range, target.y - range, target.z - range, target.x + range, target.y + range, target.z + range));
					distance++;
					if (entities.contains(taritem)) {
						found = true;
					}
				}
				
				if (found) {
					item = player.worldObj.getEntityByID(targetID);
					stack.stackTagCompound.setInteger("target", -1);
					stack.stackTagCompound.setDouble("dist", -1);
					Vector3 moveVector = new Vector3(player.getLookVec().normalize());
					if (item instanceof EntityItem) {
						((EntityItem)item).delayBeforeCanPickup = 20;
						item.motionX = moveVector.x * 1.5F;
						item.motionY = moveVector.y * 1.0F;
						item.motionZ = moveVector.z * 1.5F;
						if (!player.worldObj.isRemote) {
							EntityThrownItem thrown = new EntityThrownItem(item.worldObj, item.posX, item.posY, item.posZ, (EntityItem) item);
							item.worldObj.spawnEntityInWorld(thrown);
						}
						item.setDead();
					}
					else
					{
						item.motionX = moveVector.x * 3.0F;
						item.motionY = moveVector.y * 1.5F;
						item.motionZ = moveVector.z * 3.0F;
					}
					stack.stackTagCompound.setInteger("ticksCooldown", 10);
				}
			}
		}
	}
}