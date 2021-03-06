package crazypants.enderio.machines.machine.teleport;

import com.enderio.core.client.gui.button.CheckBox;
import com.enderio.core.client.gui.button.ToggleButton;
import com.enderio.core.client.gui.widget.TextFieldEnder;
import com.enderio.core.client.render.ColorUtil;
import crazypants.enderio.api.teleport.ITravelAccessable;
import crazypants.enderio.api.teleport.ITravelAccessable.AccessMode;
import crazypants.enderio.base.gui.GuiContainerBaseEIO;
import crazypants.enderio.base.gui.IconEIO;
import crazypants.enderio.machines.lang.Lang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;

public class GuiTravelAccessable<T extends TileEntity & ITravelAccessable> extends GuiContainerBaseEIO implements ITravelAccessableRemoteExec.GUI {

  private static final int ID_PUBLIC = 0;
  private static final int ID_PRIVATE = 1;
  private static final int ID_PROTECTED = 2;

  private final @Nonnull CheckBox publicCB;
  private final @Nonnull CheckBox privateCB;
  private final @Nonnull CheckBox protectedCB;
  private final @Nonnull ToggleButton visibleCB;

  private final @Nonnull TextFieldEnder tf;

  private final @Nonnull String publicStr;
  private final @Nonnull String privateStr;
  private final @Nonnull String protectedStr;

  protected final @Nonnull T te;
  private final int col0x;
  private final int col1x;
  private final int col2x;

  protected final @Nonnull World world;

  public GuiTravelAccessable(@Nonnull InventoryPlayer playerInv, @Nonnull T te, @Nonnull World world) {
    this(te, new ContainerTravelAccessable(playerInv, te, world));
  }

  public GuiTravelAccessable(@Nonnull T te, @Nonnull ContainerTravelAccessable container) {
    super(container, "travel_accessable");
    this.te = te;
    this.world = container.world;

    publicStr = Lang.GUI_AUTH_PUBLIC.get();
    privateStr = Lang.GUI_AUTH_PRIVATE.get();
    protectedStr = Lang.GUI_AUTH_PROTECTED.get();

    FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

    tf = new TextFieldEnder(fr, 28, 10, 90, 16);

    col1x = 88;
    col0x = (col1x - fr.getStringWidth(protectedStr) / 2) / 2;
    int tmp = (col1x + fr.getStringWidth(protectedStr) / 2);
    col2x = tmp + (176 - tmp) / 2;

    int x = 0;
    int y = 50;

    x = col0x - 8;
    privateCB = new CheckBox(this, ID_PRIVATE, x, y);
    privateCB.setSelected(te.getAccessMode() == AccessMode.PRIVATE);

    x = col1x - 8;
    protectedCB = new CheckBox(this, ID_PROTECTED, x, y);
    protectedCB.setSelected(te.getAccessMode() == AccessMode.PROTECTED);

    x = col2x - 8;
    publicCB = new CheckBox(this, ID_PUBLIC, x, y);
    publicCB.setSelected(te.getAccessMode() == AccessMode.PUBLIC);

    visibleCB = new ToggleButton(this, -1, 150, 10, IconEIO.VISIBLE_NO, IconEIO.VISIBLE_YES);
    visibleCB.setSelected(te.isVisible());
    visibleCB.setToolTip(Lang.GUI_AUTH_VISIBLE.getLines().toArray(new String[0]));

    ySize = 185;

    textFields.add(tf);
  }

  @Override
  protected void actionPerformed(@Nonnull GuiButton b) {
    if (b.id >= 0) {
      privateCB.setSelected(b.id == ID_PRIVATE);
      protectedCB.setSelected(b.id == ID_PROTECTED);
      publicCB.setSelected(b.id == ID_PUBLIC);

      AccessMode curMode = b.id == ID_PRIVATE ? AccessMode.PRIVATE : b.id == ID_PROTECTED ? AccessMode.PROTECTED : AccessMode.PUBLIC;
      te.setAccessMode(curMode);

      doSetAccessMode(curMode);
    } else if (b == visibleCB) {
      te.setVisible(visibleCB.isSelected());
      doSetVisible(visibleCB.isSelected());
    }
  }

  @Override
  public void initGui() {
    super.initGui();
    buttonList.clear();

    publicCB.setPaintSelectedBorder(false);
    publicCB.onGuiInit();
    privateCB.onGuiInit();
    protectedCB.onGuiInit();
    visibleCB.onGuiInit();

    tf.setMaxStringLength(32);
    tf.setFocused(true);
    String txt = te.getLabel();
    if (txt != null && txt.length() > 0) {
      tf.setText(txt);
    }

    ((ContainerTravelAccessable) inventorySlots).addGhostSlots(getGhostSlotHandler().getGhostSlots());

  }

  @Override
  public void updateScreen() {
    super.updateScreen();
  }

  @Override
  public void mouseClicked(int x, int y, int par3) throws IOException {
    super.mouseClicked(x, y, par3);
  }

  @Override
  public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    bindGuiTexture();
    int sx = (width - xSize) / 2;
    int sy = (height - ySize) / 2;

    drawTexturedModalRect(sx, sy, 0, 0, this.xSize, this.ySize);

    int col = ColorUtil.getRGB(Color.white);
    int x = sx;
    int y = sy + 38;

    FontRenderer fr = getFontRenderer();
    x = sx + col0x - fr.getStringWidth(privateStr) / 2;
    fr.drawStringWithShadow(privateStr, x, y, col);

    x = sx + col1x - fr.getStringWidth(protectedStr) / 2;
    fr.drawStringWithShadow(protectedStr, x, y, col);

    x = sx + col2x - fr.getStringWidth(publicStr) / 2;
    fr.drawStringWithShadow(publicStr, x, y, col);

    checkLabelForChange();

    super.drawGuiContainerBackgroundLayer(f, i, j);
  }

  private void checkLabelForChange() {
    String newTxt = tf.getText();
    if (newTxt.length() == 0) {
      newTxt = null;
    }

    String curText = te.getLabel();
    if (curText != null && curText.length() == 0) {
      curText = null;
    }

    boolean changed = false;
    if (newTxt == null) {
        changed = curText != null;
    } else {
      changed = !newTxt.equals(curText);
    }
    if (!changed) {
      return;
    }
    te.setLabel(newTxt);
    doSetLabel(newTxt);
  }

  @Override
  protected void drawForegroundImpl(int mouseX, int mouseY) {
    super.drawForegroundImpl(mouseX, mouseY);

    if (te.getAccessMode() != AccessMode.PROTECTED) {
      bindGuiTexture();
      GL11.glColor4f(1, 1, 1, 0.75f);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glDisable(GL11.GL_DEPTH_TEST);
      drawTexturedModalRect(43, 72, 5, 35, 90, 18);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GL11.glColor4f(1, 1, 1, 1);
    }
  }

  @Override
  public void drawScreen(int par1, int par2, float par3) {
    super.drawScreen(par1, par2, par3);
  }

}
