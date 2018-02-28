package crazypants.enderio.machines.machine.sagmill;

import com.enderio.core.client.gui.widget.GuiToolTip;
import crazypants.enderio.base.machine.gui.GuiInventoryMachineBase;
import crazypants.enderio.base.machine.gui.PowerBar;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;

import javax.annotation.Nonnull;
import java.awt.*;

public class GuiSagMill extends GuiInventoryMachineBase<TileSagMill> {

  boolean isSimple;

  public GuiSagMill(@Nonnull InventoryPlayer par1InventoryPlayer, @Nonnull TileSagMill inventory) {
    super(inventory, ContainerSagMill.create(par1InventoryPlayer, inventory), "crusher", "crusher_light");
    isSimple = inventory instanceof TileSagMill.Simple;
    if (!isSimple) {
      addToolTip(new GuiToolTip(new Rectangle(142, 23, 5, 17), "") {

        @Override
        protected void updateText() {
          text.clear();
          text.add(getTileEntity().getBallDurationScaled(100) + "%");
        }
      });
    }

    addProgressTooltip(79, 31, 18, 24);
    redstoneButton.setIsVisible(!isSimple);

    addDrawingElement(new PowerBar<>(inventory, this));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    bindGuiTexture(isSimple ? 1 : 0);

    drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);

    if (shouldRenderProgress()) {
      int barHeight = getProgressScaled(24);
      drawTexturedModalRect(guiLeft + 79, guiTop + 31, 200, 0, 18, barHeight + 1);
    }

    int barHeight = getTileEntity().getBallDurationScaled(16);
    if (!isSimple && barHeight > 0) {
      drawTexturedModalRect(guiLeft + 142, guiTop + 23 + (16 - barHeight), 186, 31, 4, barHeight);
    }
    super.drawGuiContainerBackgroundLayer(par1, par2, par3);
  }

}
