package crazypants.enderio.base.block.darksteel.ladder;

import com.enderio.core.api.client.gui.IResourceTooltipProvider;
import crazypants.enderio.base.EnderIOTab;
import crazypants.enderio.base.config.Config;
import crazypants.enderio.base.init.IModObject;
import crazypants.enderio.base.render.IDefaultRenderers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockDarkSteelLadder extends BlockLadder implements IResourceTooltipProvider, IDefaultRenderers, IModObject.WithBlockItem {

  public static BlockDarkSteelLadder create(@Nonnull IModObject modObject) {
    return new BlockDarkSteelLadder(modObject);
  }

  protected BlockDarkSteelLadder(@Nonnull IModObject modObject) {
    modObject.apply(this);
    setSoundType(SoundType.METAL);
    setCreativeTab(EnderIOTab.tabEnderIO);
    setHardness(0.4F);
  }

  @Override
  public @Nonnull Material getMaterial(@Nonnull IBlockState state) {
    return Material.IRON;
  }

  @Override
  public void onEntityCollidedWithBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entity) {
    if (entity.onGround || entity.collidedVertically) {
      return;
    }

    if (entity.motionY >= 0.1) {
      entity.setPosition(entity.posX, entity.posY + Config.darkSteelLadderSpeedBoost, entity.posZ);
    } else if (entity.motionY <= -0.1) {
      Block blockUnder = entity.world
          .getBlockState(new BlockPos(MathHelper.floor(entity.posX), MathHelper.floor(entity.posY) - 3, MathHelper.floor(entity.posZ))).getBlock();
      if (blockUnder == Blocks.AIR || blockUnder == this) { // prevent clipping into block
        entity.setPosition(entity.posX, entity.posY - Config.darkSteelLadderSpeedBoost, entity.posZ);
      }
    }
  }

  @Override
  public @Nonnull String getUnlocalizedNameForTooltip(@Nonnull ItemStack itemStack) {
    return getUnlocalizedName();
  }

}
