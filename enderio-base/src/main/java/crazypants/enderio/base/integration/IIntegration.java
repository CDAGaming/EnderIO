package crazypants.enderio.base.integration;

import com.enderio.core.common.util.UserIdent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public interface IIntegration extends IForgeRegistryEntry<IIntegration> {

  default boolean isInSameTeam(@Nonnull UserIdent identA, @Nonnull UserIdent identB) {
    return false;
  };

}
