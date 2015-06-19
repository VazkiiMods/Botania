package buildcraft.api.statements;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * This interface can be used by tiles to override external trigger
 * behaviour.
 *
 * Please use wisely.
 */
public interface ITriggerExternalOverride {
	public enum Result {
		TRUE, FALSE, IGNORE
	}

	Result override(ForgeDirection side, IStatementContainer source, IStatementParameter[] parameters);
}
