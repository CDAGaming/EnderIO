package crazypants.enderio.machine.invpanel.sensor;

import com.enderio.core.client.gui.widget.GhostSlot;
import com.enderio.core.client.gui.widget.TextFieldEnder;
import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.machine.gui.GuiPoweredMachineBase;
import crazypants.enderio.base.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class GuiSensor extends GuiPoweredMachineBase<TileInventoryPanelSensor> {
  
  
  private TextFieldEnder startTF;
  private TextFieldEnder stopTF;
  
  public GuiSensor(InventoryPlayer par1InventoryPlayer, TileInventoryPanelSensor te) {
    super(te, new ContainerSensor(par1InventoryPlayer, te), "invPanelSensor");
    
    recipeButton.setYOrigin(recipeButton.getBounds().y + 19);
    redstoneButton.setIsVisible(false);
    configB.setYOrigin(5);
    
    int tfWidth = 42;
    int tfHeight = 14;
    int tfX = xSize - tfWidth - 6 - 20;
    int tfY = 34;
    startTF = new TextFieldEnder(getFontRenderer(), tfX, tfY, tfWidth, tfHeight);
    startTF.setCanLoseFocus(true);
    startTF.setMaxStringLength(6);
    startTF.setText(te.getStartCount() + "");
    startTF.setCharFilter(TextFieldEnder.FILTER_NUMERIC);
    textFields.add(startTF);

    stopTF = new TextFieldEnder(getFontRenderer(), tfX, tfY + tfHeight + 4, tfWidth, tfHeight);
    stopTF.setCanLoseFocus(true);
    stopTF.setMaxStringLength(6);
    stopTF.setText(te.getStopCount() + "");
    stopTF.setCharFilter(TextFieldEnder.FILTER_NUMERIC);
    textFields.add(stopTF);
      
  }

  @Override
  public void initGui() {
    super.initGui();
    ((ContainerSensor) inventorySlots).addGhostSlots(getGhostSlots());
  }

  @Override
  protected void mouseClickMove(int mouseX, int mouseY, int button, long par4) {
    if (!getGhostSlots().isEmpty()) {
      GhostSlot slot = getGhostSlot(mouseX, mouseY);
      if (slot != null) {
        ItemStack st = Minecraft.getMinecraft().player.inventory.getItemStack();
        // don't replace already set slots while dragging an item
        if (st == null || slot.getStack() == null) {
          slot.putStack(st);
        }
      }
    }
    super.mouseClickMove(mouseX, mouseY, button, par4);
  }

  @Override
  protected int getPowerHeight() {
    return 57;
  }
  
  @Override
  protected boolean showRecipeButton() {
    return false;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    bindGuiTexture();
    int sx = (width - xSize) / 2;
    int sy = (height - ySize) / 2;
    drawTexturedModalRect(sx, sy, 0, 0, xSize, ySize);
    super.drawGuiContainerBackgroundLayer(par1, par2, par3);
    
    String txt = EnderIO.lang.localize("gui.inventorySensor.control1").trim();
    FontRenderer fr = getFontRenderer();
    fr.drawString(txt, startTF.xPosition - 3 - fr.getStringWidth(txt), startTF.yPosition + 3, 0x000000);
    txt = EnderIO.lang.localize("gui.inventorySensor.control2").trim();
    fr.drawString(txt, stopTF.xPosition - 3 - fr.getStringWidth(txt), stopTF.yPosition + 3, 0x000000);
    
    checkForTextChange();
  }
  
  private void checkForTextChange() {
    boolean dirty = false;
    TileInventoryPanelSensor te = getTileEntity();
    Integer val = startTF.getInteger();
    if(val != null && val.intValue() > 0 && val.intValue() != te.getStartCount()) {
      te.setStartCount(val);
      dirty = true;
    }
    val = stopTF.getInteger();
    if(val != null && val.intValue() > 0 && val.intValue() != te.getStopCount()) {
      te.setStopCount(val);
      dirty = true;
    }
    if(dirty) {
      PacketHandler.INSTANCE.sendToServer(new PacketItemCount(te));
    }
  }

  

}
