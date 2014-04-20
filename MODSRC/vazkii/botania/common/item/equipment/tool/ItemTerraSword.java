/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 14, 2014, 7:34:56 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.lib.LibItemNames;

public class ItemTerraSword extends ItemManasteelSword implements ILensEffect {

	public ItemTerraSword() {
		super(BotaniaAPI.terrasteelToolMaterial, LibItemNames.TERRA_SWORD);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if(par3Entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) par3Entity;
			if(player.getCurrentEquippedItem() == par1ItemStack && player.swingProgress == 0.16666667F && !par2World.isRemote && par2World.rand.nextInt(2) == 0) {
				EntityManaBurst burst = getBurst(player, par1ItemStack);
				par2World.spawnEntityInWorld(burst);
				ManasteelToolCommons.damageItem(par1ItemStack, 1, player, MANA_PER_DAMAGE);
				par2World.playSoundAtEntity(player, "random.levelup", 1F, 1.4F);
			}
		}
	}

	public EntityManaBurst getBurst(EntityPlayer player, ItemStack stack) {
		EntityManaBurst burst = new EntityManaBurst(player);

		float motionModifier = 5F;
		
		burst.setColor(0x20FF20);
		burst.setMana(MANA_PER_DAMAGE);
		burst.setStartingMana(MANA_PER_DAMAGE);
		burst.setMinManaLoss(40);
		burst.setManaLossPerTick(4F);
		burst.setGravity(0F);
		burst.setMotion(burst.motionX * motionModifier, burst.motionY * motionModifier, burst.motionZ * motionModifier);

		burst.setSourceLens(stack);
		return burst;
	}

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		// NO-OP
	}

	@Override
	public boolean collideBurst(IManaBurst burst, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		Entity entity = (Entity) burst;
		if(pos.entityHit != null && pos.entityHit instanceof EntityLivingBase && !(pos.entityHit instanceof EntityPlayer)) {
			EntityLivingBase living = (EntityLivingBase) pos.entityHit;
			if(living.hurtTime == 0) {
				int cost = MANA_PER_DAMAGE / 3;
				int mana = burst.getMana();
				if(mana >= cost) {
					burst.setMana(mana - cost);
					float damage = 4F + BotaniaAPI.terrasteelToolMaterial.getDamageVsEntity();
					if(!burst.isFake() && !entity.worldObj.isRemote)
						living.attackEntityFrom(DamageSource.magic, damage);
				}
			}
		}
		return dead;
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		// NO-OP
	}

	@Override
	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		return true;
	}

}
