/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 27, 2014, 2:41:19 AM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.entity.EntitySignalFlare;
import vazkii.botania.common.lib.LibItemNames;

public class ItemBottledMana extends ItemMod {

	IIcon[] icons;
	private static final String TAG_SEED = "randomSeed";

	public ItemBottledMana() {
		setUnlocalizedName(LibItemNames.MANA_BOTTLE);
		setMaxStackSize(1);
		setMaxDamage(6);
	}

	public void effect(EntityPlayer player, int id) {
		switch(id) {
		case 0 : { // Random motion
			player.motionX = (Math.random() - 0.5) * 3;
			player.motionZ = (Math.random() - 0.5) * 3;
			break;
		}
		case 1 : { // Water
			if(!player.worldObj.isRemote && !player.worldObj.provider.isHellWorld)
				player.worldObj.setBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), Blocks.flowing_water);
			break;
		}
		case 2 : { // Set on Fire
			if(!player.worldObj.isRemote)
				player.setFire(4);
			break;
		}
		case 3 : { // Mini Explosion
			if(!player.worldObj.isRemote)
				player.worldObj.createExplosion(null, player.posX, player.posY, player.posZ, 0.25F, false);
			break;
		}
		case 4 : { // Mega Jump
			if(!player.worldObj.provider.isHellWorld) {
				if(!player.worldObj.isRemote)
					player.addPotionEffect(new PotionEffect(Potion.resistance.id, 300, 5));
				player.motionY = 6;
			}

			break;
		}
		case 5 : { // Randomly set HP
			if(!player.worldObj.isRemote)
				player.setHealth(player.worldObj.rand.nextInt(19) + 1);
			break;
		}
		case 6 : { // Lots O' Hearts
			if(!player.worldObj.isRemote)
				player.addPotionEffect(new PotionEffect(Potion.field_76444_x.id, 20 * 60 * 2, 9));
			break;
		}
		case 7 : { // All your inventory is belong to us
			if(!player.worldObj.isRemote)
				for(int i = 0; i < player.inventory.getSizeInventory(); i++)
					if(i != player.inventory.currentItem) {
						ItemStack stackAt = player.inventory.getStackInSlot(i);
						if(stackAt != null)
							player.dropPlayerItemWithRandomChoice(stackAt, true);
						player.inventory.setInventorySlotContents(i, null);
					}

			break;
		}
		case 8 : { // Break your neck
			player.rotationPitch = (float) Math.random() * 360F;
			player.rotationYaw = (float) Math.random() * 180F;

			break;
		}
		case 9 : { // Highest Possible
			int x = MathHelper.floor_double(player.posX);
			MathHelper.floor_double(player.posY);
			int z = MathHelper.floor_double(player.posZ);
			for(int i = 256; i > 0; i--) {
				Block block = player.worldObj.getBlock(x, i, z);
				if(!block.isAir(player.worldObj, x, i, z)) {
					if(player instanceof EntityPlayerMP) {
						EntityPlayerMP mp = (EntityPlayerMP) player;
						mp.playerNetServerHandler.setPlayerLocation(player.posX, i + 1.6, player.posZ, player.rotationYaw, player.rotationPitch);
					}
					break;
				}
			}

			break;
		}
		case 10 : { // HYPERSPEEEEEED
			if(!player.worldObj.isRemote)
				player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 60, 200));
			break;
		}
		case 11 : { // Night Vision
			if(!player.worldObj.isRemote)
				player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 6000, 0));
			break;
		}
		case 12 : { // Flare
			if(!player.worldObj.isRemote) {
				EntitySignalFlare flare = new EntitySignalFlare(player.worldObj);
				flare.setPosition(player.posX, player.posY, player.posZ);
				flare.setColor(player.worldObj.rand.nextInt(16));
				player.worldObj.playSoundAtEntity(player, "random.explode", 40F, (1.0F + (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

				player.worldObj.spawnEntityInWorld(flare);

				int range = 5;
				List<EntityLivingBase> entities = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
				for(EntityLivingBase entity : entities)
					if(entity != player && (!(entity instanceof EntityPlayer) || MinecraftServer.getServer() == null || MinecraftServer.getServer().isPVPEnabled()))
						entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 50, 5));
			}

			break;
		}
		case 13 : { // Pixie Friend
			if(!player.worldObj.isRemote) {
				EntityPixie pixie = new EntityPixie(player.worldObj);
				pixie.setPosition(player.posX, player.posY + 1.5, player.posZ);
				player.worldObj.spawnEntityInWorld(pixie);
			}
			break;
		}
		case 14 : { // Nausea + Blindness :3
			if(!player.worldObj.isRemote) {
				player.addPotionEffect(new PotionEffect(Potion.confusion.id, 160, 3));
				player.addPotionEffect(new PotionEffect(Potion.blindness.id, 160, 0));
			}

			break;
		}
		case 15 : { // Drop own Head
			if(!player.worldObj.isRemote) {
				player.attackEntityFrom(DamageSource.magic, player.getHealth() - 1);
				ItemStack stack = new ItemStack(Items.skull, 1, 3);
				ItemNBTHelper.setString(stack, "SkullOwner", player.getCommandSenderName());
				player.dropPlayerItemWithRandomChoice(stack, true);
			}
			break;
		}
		}
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		getSeed(par1ItemStack);
	}

	public void randomEffect(EntityPlayer player, ItemStack stack) {
		effect(player, new Random(getSeed(stack)).nextInt(16));
	}

	long getSeed(ItemStack stack) {
		long seed = ItemNBTHelper.getLong(stack, TAG_SEED, -1);
		if(seed == -1)
			return randomSeed(stack);
		return seed;
	}

	long randomSeed(ItemStack stack) {
		long seed = Math.abs(itemRand.nextLong());
		ItemNBTHelper.setLong(stack, TAG_SEED, seed);
		return seed;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add(StatCollector.translateToLocal("botaniamisc.bottleTooltip"));
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[6];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
	}

	@Override
	public IIcon getIconFromDamage(int par1) {
		return icons[Math.min(icons.length - 1, par1)];
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		randomEffect(par3EntityPlayer, par1ItemStack);
		par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() + 1);
		randomSeed(par1ItemStack);

		if(par1ItemStack.getItemDamage() == 6)
			return new ItemStack(Items.glass_bottle);
		return par1ItemStack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 20;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.drink;
	}

}
