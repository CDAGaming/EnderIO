package crazypants.enderio.machines.machine.generator.stirling;

import com.enderio.core.client.gui.widget.GuiToolTip;
import com.enderio.core.client.render.ColorUtil;
import crazypants.enderio.base.capacitor.DefaultCapacitorData;
import crazypants.enderio.base.capacitor.ICapacitorData;
import crazypants.enderio.base.lang.LangPower;
import crazypants.enderio.base.machine.gui.GuiInventoryMachineBase;
import crazypants.enderio.base.machine.gui.PowerBar;
import crazypants.enderio.machines.lang.Lang;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.awt.*;
import java.text.MessageFormat;

@SideOnly(Side.CLIENT)
public class GuiStirlingGenerator<T extends TileStirlingGenerator> extends GuiInventoryMachineBase<T> {

  private final boolean isSimple;

  public GuiStirlingGenerator(@Nonnull InventoryPlayer par1InventoryPlayer, @Nonnull T te) {
    super(te, new ContainerStirlingGenerator<T>(par1InventoryPlayer, te), "stirling_generator", "simple_stirling_generator");

    isSimple = te instanceof TileStirlingGenerator.Simple;

    if (!isSimple) {
      final ContainerStirlingGenerator<?> c = (ContainerStirlingGenerator<?>) inventorySlots;
      Rectangle r = new Rectangle(c.getUpgradeOffset(), new Dimension(16, 16));
      MessageFormat fmt = new MessageFormat(Lang.GUI_STIRGEN_UPGRADES.get());
      ttMan.addToolTip(new GuiToolTip(r, Lang.GUI_STIRGEN_SLOT.get(), formatUpgrade(fmt, DefaultCapacitorData.ACTIVATED_CAPACITOR),
          formatUpgrade(fmt, DefaultCapacitorData.ENDER_CAPACITOR)) {
        @Override
        public boolean shouldDraw() {
          return !c.getUpgradeSlot().getHasStack() && super.shouldDraw();
        }
      });
    } else {
      redstoneButton.setIsVisible(false);
    }
    addProgressTooltip(80, 52, 14, 14);

    addDrawingElement(new PowerBar<>(te, this));
  }

  @Override
  public void initGui() {
    super.initGui();
    ((ContainerStirlingGenerator<?>) inventorySlots).addGhostslots(getGhostSlotHandler().getGhostSlots());
  }

  private static float getFactor(@Nonnull ICapacitorData upgrade) {
    return TileStirlingGenerator.getEnergyMultiplier(upgrade) * TileStirlingGenerator.getBurnTimeMultiplier(upgrade);
  }

  private static String formatUpgrade(@Nonnull MessageFormat fmt, @Nonnull ICapacitorData upgrade) {
    float efficiency = getFactor(upgrade) / getFactor(DefaultCapacitorData.BASIC_CAPACITOR);
    Object[] args = new Object[] { upgrade.getLocalizedName(), efficiency, TextFormatting.WHITE, TextFormatting.GRAY };
    return fmt.format(args, new StringBuffer(), null).toString();
  }

  @Override
  protected String formatProgressTooltip(int scaledProgress, float remaining) {
    int totalBurnTime = getTileEntity().totalBurnTime;
    int remainingTicks = (int) (remaining * totalBurnTime);
    int remainingSecs = remainingTicks / 20;
    int remainingPower = getTileEntity().getPowerUsePerTick() * remainingTicks;
    Object[] objects = { remaining, remainingSecs / 60, remainingSecs % 60, remainingPower };
    return MessageFormat.format(Lang.GUI_STIRGEN_REMAINING.get(), objects);
  }

  @Override
  protected int scaleProgressForTooltip(float progress) {
    int totalBurnTime = getTileEntity().totalBurnTime;
    int scale = Math.max(100, (totalBurnTime + 19) / 20);
    return (int) (progress * scale);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    bindGuiTexture(isSimple ? 1 : 0);
    int sx = (width - xSize) / 2;
    int sy = (height - ySize) / 2;

    drawTexturedModalRect(sx, sy, 0, 0, this.xSize, this.ySize);
    int scaled;

    if (shouldRenderProgress()) {
      scaled = getProgressScaled(12);
      drawTexturedModalRect(sx + 80, sy + 64 - scaled, 176, 12 - scaled, 14, scaled + 2);
    }

    super.drawGuiContainerBackgroundLayer(par1, par2, par3);

    FontRenderer fr = getFontRenderer();
    int y = guiTop + fr.FONT_HEIGHT / 2 + 3;

    int output = 0;
    if (getTileEntity().isActive()) {
      output = getTileEntity().getPowerUsePerTick();
    }
    String txt = Lang.GUI_STIRGEN_OUTPUT.get(LangPower.RFt(output));
    int sw = fr.getStringWidth(txt);
    fr.drawStringWithShadow(txt, guiLeft + xSize / 2 - sw / 2, y, ColorUtil.getRGB(Color.WHITE));

    txt = Lang.GUI_STIRGEN_RATE
        .get(Math.round(getTileEntity().getBurnTimeMultiplier() / TileStirlingGenerator.getBurnTimeMultiplier(DefaultCapacitorData.BASIC_CAPACITOR) * 100));
    sw = fr.getStringWidth(txt);
    y += fr.FONT_HEIGHT + 3;
    fr.drawStringWithShadow(txt, guiLeft + xSize / 2 - sw / 2, y, ColorUtil.getRGB(Color.WHITE));
  }

}
