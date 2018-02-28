package crazypants.enderio.base.init;

import com.enderio.core.common.util.stackable.IProducer;
import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.gui.handler.GuiHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IModObject extends IProducer {

  @Nonnull
  String getUnlocalisedName();

  default @Nonnull ResourceLocation getRegistryName() {
    return new ResourceLocation(EnderIO.DOMAIN, getUnlocalisedName());
  }

  default @Nonnull <B extends Block> B apply(@Nonnull B block) {
    block.setUnlocalizedName(getUnlocalisedName());
    block.setRegistryName(getRegistryName());
    return block;
  }

  default @Nonnull <I extends Item> I apply(@Nonnull I item) {
    item.setUnlocalizedName(getUnlocalisedName());
    item.setRegistryName(getRegistryName());
    return item;
  }

  default @Nullable Class<? extends TileEntity> getTEClass() {
    if (this instanceof Registerable) {
      IModTileEntity tileEntity = ((Registerable) this).getTileEntity();
      if (tileEntity != null) {
        return tileEntity.getTileEntityClass();
      }
    }
    return null;
  }

  public static interface Registerable extends IModObject {

    @Nonnull
    Class<?> getClazz();

    @Nullable
    String getBlockMethodName();

    @Nullable
    String getItemMethodName();

    @Nullable
    IModTileEntity getTileEntity();

    void setItem(@Nullable Item obj);

    void setBlock(@Nullable Block obj);

    default boolean openGui(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer entityPlayer) {
      return openGui(world, pos, entityPlayer, null, 0);
    }

    default boolean openGui(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer entityPlayer, @Nullable EnumFacing side) {
      return openGui(world, pos, entityPlayer, side, 0);
    }

    default boolean openGui(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer entityPlayer, @Nullable EnumFacing side, int param) {
      return GuiHelper.openGui(this, world, pos, entityPlayer, side, param);
    }

    default boolean openGui(@Nonnull World world, @Nonnull EntityPlayer entityPlayer, int a, int b, int c) {
      return GuiHelper.openGui(this, world, entityPlayer, a, b, c);
    }

    default boolean openClientGui(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer entityPlayer, @Nullable EnumFacing side, int param) {
      return GuiHelper.openClientGui(this, world, pos, entityPlayer, side, param);
    }

    default boolean openClientGui(@Nonnull World world, @Nonnull EntityPlayer entityPlayer, int a, int b, int c) {
      return GuiHelper.openClientGui(this, world, entityPlayer, a, b, c);
    }

  }

  /**
   * Interface to be implemented on blocks that are created from modObjects. It will be called at the right time to create and register the blockItem. Note that
   * the method shall NOT do the registering itself.
   *
   */
  public static interface WithBlockItem {

    default @Nullable Item createBlockItem(@Nonnull IModObject modObject) {
      return modObject.apply(new ItemBlock((Block) this));
    };

  }

  public static interface LifecycleInit {

    void init(@Nonnull IModObject modObject, @Nonnull FMLInitializationEvent event);

  }

  public static interface LifecyclePostInit {

    void init(@Nonnull IModObject modObject, @Nonnull FMLPostInitializationEvent event);

  }

}