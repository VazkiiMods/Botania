package vazkii.botania.api.mana;

import net.minecraft.world.World;

public interface IManaSpreader extends IManaBlock{

	World getWorldObj();
	int getxCoord();
	int getyCoord();
	int getzCoord();
	float getRotationX();
	float getRotationY();
	void setCanShoot(boolean canShoot);
	int getBurstParticleTick();
	void setBurstParticleTick(int i);
	int getLastBurstDeathTick();
	void setLastBurstDeathTick(int ticksExisted);

}
