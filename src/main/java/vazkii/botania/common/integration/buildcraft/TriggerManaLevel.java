package vazkii.botania.common.integration.buildcraft;

import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;
import buildcraft.api.statements.ITriggerExternal;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;
import vazkii.botania.api.mana.IManaBlock;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.lib.LibTriggerNames;

public class TriggerManaLevel extends StatementBase implements ITriggerExternal {
	public enum State {
		EMPTY,
		CONTAINS,
		SPACE,
		FULL
	};

	private final State state;

	public TriggerManaLevel(State state) {
		this.state = state;
	}

	@Override
	public String getUniqueTag() {
		return "botania:mana" + state.name();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getGuiSprite() {
		return MiscellaneousIcons.INSTANCE.manaLevelTriggerIcons.get(state);
	}

	@Override
	public String getDescription() {
		return I18n.translateToLocal(LibTriggerNames.TRIGGER_MANA_PREFIX + WordUtils.capitalizeFully(state.name()));
	}

	@Override
	public boolean isTriggerActive(TileEntity target, EnumFacing side, IStatementContainer source, IStatementParameter[] parameters) {
		if(target instanceof IManaBlock) {
			if(state == State.EMPTY) return ((IManaBlock) target).getCurrentMana() == 0;
			else if(state == State.CONTAINS) return ((IManaBlock) target).getCurrentMana() > 0;
			else if(target instanceof IManaReceiver) {
				if(state == State.SPACE) return !((IManaReceiver) target).isFull();
				else if(state == State.FULL) return ((IManaReceiver) target).isFull();
			}
		}

		return false;
	}
}
