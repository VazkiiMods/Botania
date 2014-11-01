package vazkii.botania.common.plugins.buildcraft;

import vazkii.botania.common.block.tile.TileRuneAltar;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;
import buildcraft.api.statements.ITriggerExternal;

public class TriggerRuneAltarCanCraft extends StatementBase implements
		ITriggerExternal {

	@Override
	public String getUniqueTag() {
		return "botania:runeAltarCanCraft";
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon("botania:triggers/runeAltarCanCraft.png");
	}

	@Override
	public String getDescription() {
		return StatCollector.translateToLocal("botania.trigger.runeAltarCanCraft");
	}

	@Override
	public boolean isTriggerActive(TileEntity target, ForgeDirection side,
			IStatementContainer source, IStatementParameter[] parameters) {
		if (target instanceof TileRuneAltar) {
			return ((TileRuneAltar) target).hasValidRecipe();
		} else {
			return false;
		}
	}

}
