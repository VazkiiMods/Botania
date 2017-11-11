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

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.entity.EntitySignalFlare;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class ItemBottledMana extends ItemMod {

	private static final String TAG_SEED = "randomSeed";

	public ItemBottledMana() {
		super(LibItemNames.MANA_BOTTLE);
		setMaxStackSize(1);
		setMaxDamage(6);
	}

	public void effect(ItemStack stack, EntityLivingBase living, int id) {
		switch(id) {
		case 0 : { // Random motion
			living.motionX = (Math.random() - 0.5) * 3;
			living.motionZ = (Math.random() - 0.5) * 3;
			break;
		}
		case 1 : { // Water
			if(!living.world.isRemote && !living.world.provider.doesWaterVaporize())
				living.world.setBlockState(new BlockPos(living), Blocks.FLOWING_WATER.getDefaultState());
			break;
		}
		case 2 : { // Set on Fire
			if(!living.world.isRemote)
				living.setFire(4);
			break;
		}
		case 3 : { // Mini Explosion
			if(!living.world.isRemote)
				living.world.createExplosion(null, living.posX, living.posY, living.posZ, 0.25F, false);
			break;
		}
		case 4 : { // Mega Jump
			if(!living.world.provider.doesWaterVaporize()) {
				if(!living.world.isRemote)
					living.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 300, 5));
				living.motionY = 6;
			}

			break;
		}
		case 5 : { // Randomly set HP
			if(!living.world.isRemote)
				living.setHealth(living.world.rand.nextInt(19) + 1);
			break;
		}
		case 6 : { // Lots O' Hearts
			if(!living.world.isRemote)
				living.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 20 * 60 * 2, 9));
			break;
		}
		case 7 : { // All your inventory is belong to us
			if(!living.world.isRemote && living instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) living;
				for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
					ItemStack stackAt = player.inventory.getStackInSlot(i);
					if(stackAt != stack) {
						if(!stackAt.isEmpty())
							player.entityDropItem(stackAt, 0);
						player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
					}
				}
			}

			break;
		}
		case 8 : { // Break your neck
			living.rotationPitch = (float) Math.random() * 360F;
			living.rotationYaw = (float) Math.random() * 180F;

			break;
		}
		case 9 : { // Highest Possible
			int x = MathHelper.floor(living.posX);
			MathHelper.floor(living.posY);
			int z = MathHelper.floor(living.posZ);
			for(int i = 256; i > 0; i--) {
				Block block = living.world.getBlockState(new BlockPos(x, i, z)).getBlock();
				if(!block.isAir(living.world.getBlockState(new BlockPos(x, i, z)), living.world, new BlockPos(x, i, z))) {
					if(living instanceof EntityPlayerMP) {
						EntityPlayerMP mp = (EntityPlayerMP) living;
						mp.connection.setPlayerLocation(living.posX, i + 1.6, living.posZ, living.rotationYaw, living.rotationPitch);
					}
					break;
				}
			}

			break;
		}
		case 10 : { // HYPERSPEEEEEED
			if(!living.world.isRemote)
				living.addPotionEffect(new PotionEffect(MobEffects.SPEED, 60, 200));
			break;
		}
		case 11 : { // Night Vision
			if(!living.world.isRemote)
				living.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 6000, 0));
			break;
		}
		case 12 : { // Flare
			if(!living.world.isRemote) {
				EntitySignalFlare flare = new EntitySignalFlare(living.world);
				flare.setPosition(living.posX, living.posY, living.posZ);
				flare.setColor(living.world.rand.nextInt(16));
				flare.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 40F, (1.0F + (living.world.rand.nextFloat() - living.world.rand.nextFloat()) * 0.2F) * 0.7F);

				living.world.spawnEntity(flare);

				int range = 5;
				List<EntityLivingBase> entities = living.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(living.posX - range, living.posY - range, living.posZ - range, living.posX + range, living.posY + range, living.posZ + range));
				for(EntityLivingBase entity : entities)
					if(entity != living && (!(entity instanceof EntityPlayer) || FMLCommonHandler.instance().getMinecraftServerInstance() == null || FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled()))
						entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 50, 5));
			}

			break;
		}
		case 13 : { // Pixie Friend
			if(!living.world.isRemote) {
				EntityPixie pixie = new EntityPixie(living.world);
				pixie.setPosition(living.posX, living.posY + 1.5, living.posZ);
				living.world.spawnEntity(pixie);
			}
			break;
		}
		case 14 : { // Nausea + Blindness :3
			if(!living.world.isRemote) {
				living.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 160, 3));
				living.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 160, 0));
			}

			break;
		}
		case 15 : { // Drop own Head
			if(!living.world.isRemote && living instanceof EntityPlayer) {
				living.attackEntityFrom(DamageSource.MAGIC, living.getHealth() - 1);
				ItemStack skull = new ItemStack(Items.SKULL, 1, 3);
				ItemNBTHelper.setString(skull, "SkullOwner", living.getName());
				living.entityDropItem(skull, 0);
			}
			break;
		}
		}
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World world, Entity par3Entity, int par4, boolean par5) {}

	private void randomEffect(EntityLivingBase player, ItemStack stack) {
		effect(stack, player, new Random(getSeed(stack)).nextInt(16));
	}

	private long getSeed(ItemStack stack) {
		long seed = ItemNBTHelper.getLong(stack, TAG_SEED, -1);
		if(seed == -1)
			return randomSeed(stack);
		return seed;
	}

	private long randomSeed(ItemStack stack) {
		long seed = Math.abs(itemRand.nextLong());
		ItemNBTHelper.setLong(stack, TAG_SEED, seed);
		return seed;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, World world, List<String> stacks, ITooltipFlag flags) {
		stacks.add(I18n.format("botaniamisc.bottleTooltip"));
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack par1ItemStack, World world, EntityLivingBase living) {
		randomEffect(living, par1ItemStack);
		par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() + 1);
		randomSeed(par1ItemStack);

		if(par1ItemStack.getItemDamage() == 6)
			return new ItemStack(Items.GLASS_BOTTLE);
		return par1ItemStack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 20;
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.DRINK;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemAppendMeta(this, 6, LibItemNames.MANA_BOTTLE);
	}

}
