package crazypants.enderio.base;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import static crazypants.enderio.base.init.ModObject.itemEnderface;

public class EnderIOTab extends CreativeTabs {

  public static final @Nonnull CreativeTabs tabEnderIO, tabEnderIOItems, tabEnderIOMaterials, tabEnderIOMachines, tabEnderIOMobs;

  static {
    tabEnderIOMachines = new EnderIOTab(3, "machines");
    tabEnderIOItems = new EnderIOTab(1, "items");
    tabEnderIOMaterials = new EnderIOTab(2, "materials");
    tabEnderIO = new EnderIOTab(0, "main");
    tabEnderIOMobs = new EnderIOTab(4, "mobs");
  }

  private final int meta;

  public EnderIOTab(int meta, String name) {
    super(getUnloc(name));
    this.meta = meta;
  }

  public EnderIOTab(int meta, int index, String name) {
    super(index, getUnloc(name));
    this.meta = meta;
  }

  private static String getUnloc(String name) {
    return EnderIO.DOMAIN + "." + name;
  }

  @SuppressWarnings("null")
  @Override
  @SideOnly(Side.CLIENT)
  public @Nonnull ItemStack getTabIconItem() {
    return new ItemStack(itemEnderface.getItem(), 1, meta);
  }

}
