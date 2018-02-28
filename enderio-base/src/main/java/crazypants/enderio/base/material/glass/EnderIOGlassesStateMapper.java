package crazypants.enderio.base.material.glass;

import com.google.common.collect.Maps;
import crazypants.enderio.base.init.ModObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;
import java.util.Map;

public class EnderIOGlassesStateMapper extends StateMapperBase {

  public static void create() {
    EnderIOGlassesStateMapper mapper = new EnderIOGlassesStateMapper();
    for (FusedQuartzType glasstype : FusedQuartzType.values()) {
      ModelLoader.setCustomStateMapper(glasstype.getBlock(), mapper);
    }
  }

  @Override
  protected @Nonnull ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
    Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());

    map.remove(BlockColored.COLOR);

    return new ModelResourceLocation(Block.REGISTRY.getNameForObject(ModObject.blockFusedQuartz.getBlockNN()), this.getPropertyString(map));
  }

}
