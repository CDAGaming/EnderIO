package crazypants.enderio.powertools.machine.capbank;

import com.enderio.core.common.ContainerEnder;
import com.enderio.core.common.util.ArrayInventory;
import crazypants.enderio.base.integration.baubles.BaublesUtil;
import crazypants.enderio.powertools.machine.capbank.network.InventoryImpl;
import crazypants.enderio.util.ShadowInventory;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

public abstract class ContainerCapBank extends ContainerEnder<TileCapBank> {

  private final static int SIDE_SLOT_Y_OFFSET = 12;

  private static int sideSlotY(int no) {
    return SIDE_SLOT_Y_OFFSET + no * 18;
  }

  private InventoryImpl inv;

  // Note: Modifying the Baubles inventory on the client side of an integrated
  // server is a bad idea as Baubles does some very bad things...
  protected IInventory baubles;

  public static @Nonnull ContainerCapBank create(@Nonnull InventoryPlayer playerInv, @Nonnull TileCapBank cb, final int baublesSize) {
    return new ContainerCapBank(playerInv, cb) {
      @Override
      protected int getBaublesSize() {
        return baublesSize;
      }
    };
  }

  private ContainerCapBank(@Nonnull InventoryPlayer playerInv, @Nonnull TileCapBank cb) {
    super(playerInv, cb);
  }

  protected abstract int getBaublesSize();

  public boolean hasBaublesSlots() {
    return getBaublesSize() > 0;
  }

  @Override
  protected void addSlots(final @Nonnull InventoryPlayer playerInv) {
    if (getInv().getNetwork() != null && getInv().getNetwork().getInventory() != null) {
      inv = getInv().getNetwork().getInventory();
    } else {
      inv = new InventoryImpl();
    }

    baubles = BaublesUtil.instance().getBaubles(playerInv.player);

    if (baubles != null && BaublesUtil.WhoAmI.whoAmI(playerInv.player.world) == BaublesUtil.WhoAmI.SPCLIENT) {
      baubles = new ShadowInventory(baubles);
    }

    if (hasBaublesSlots() && (baubles == null || baubles.getSizeInventory() != getBaublesSize())) {
      baubles = new ArrayInventory(getBaublesSize()) {
        @Override
        public boolean isItemValidForSlot(int i, @Nonnull ItemStack itemstack) {
          return false;
        }

        @Override
        public @Nonnull ItemStack getStackInSlot(int slot) {
          return new ItemStack(Blocks.BARRIER);
        }

        @Override
        public @Nonnull ItemStack decrStackSize(int slot, int amount) {
          return ItemStack.EMPTY;
        }

        @Override
        public @Nonnull ItemStack removeStackFromSlot(int index) {
          return ItemStack.EMPTY;
        }

      };
    }

    int armorOffset = 21, baublesOffset = 196;

    // charging slots
    for (int i = 0; i < 4; i++) {
      addSlotToContainer(new SlotImpl(inv, i, 59 + armorOffset + i * 20, 59));
    }

    for (final EntityEquipmentSlot slt : EntityEquipmentSlot.values()) {
      if (slt.getSlotType() == Type.ARMOR) {
        addSlotToContainer(new Slot(playerInv, 36 + slt.getIndex(), -15 + armorOffset, sideSlotY(3 - slt.getIndex())) {

          @Override
          public int getSlotStackLimit() {
            return 1;
          }

          @Override
          public boolean isItemValid(@Nonnull ItemStack par1ItemStack) {
            if (par1ItemStack.isEmpty()) {
              return false;
            }
            return par1ItemStack.getItem().isValidArmor(par1ItemStack, slt, playerInv.player);
          }

          @Override
          @SideOnly(Side.CLIENT)
          public String getSlotTexture() {
            return ItemArmor.EMPTY_SLOT_NAMES[slt.getIndex()];
          }
        });
      }
    }

    addSlotToContainer(new Slot(playerInv, 40, -15 + armorOffset, sideSlotY(4)) {
      @Override
      @Nullable
      @SideOnly(Side.CLIENT)
      public String getSlotTexture() {
        return "minecraft:items/empty_armor_slot_shield";
      }
    });

    if (hasBaublesSlots()) {
      for (int i = 0; i < baubles.getSizeInventory(); i++) {
        addSlotToContainer(new Slot(baubles, i, baublesOffset, sideSlotY(i)) {
          @Override
          public boolean isItemValid(@Nonnull ItemStack par1ItemStack) {
            return inventory.isItemValidForSlot(getSlotIndex(), par1ItemStack);
          }

          @Override
          public boolean canTakeStack(@Nonnull EntityPlayer playerIn) {
            ItemStack stackInSlot = inventory.getStackInSlot(getSlotIndex());
            if (stackInSlot.getItem() == Item.getItemFromBlock(Blocks.BARRIER)) {
              return false;
            }
            return super.canTakeStack(playerIn);
          }
        });
      }
    }
  }

  public void updateInventory() {
    if (getInv().getNetwork() != null && getInv().getNetwork().getInventory() != null) {
      inv.setCapBank(getInv().getNetwork().getInventory().getCapBank());
    }
  }

  @Override
  public @Nonnull Point getPlayerInventoryOffset() {
    Point p = super.getPlayerInventoryOffset();
    p.translate(21, 0);
    return p;
  }

  @Override
  public @Nonnull ItemStack transferStackInSlot(@Nonnull EntityPlayer entityPlayer, int slotIndex) {
    int otherSlots = 4 + 5; // charging + armor + off-hand
    int startBaublesSlot = otherSlots;
    int endBaublesSlot = hasBaublesSlots() ? 0 : startBaublesSlot + getBaublesSize();

    ItemStack copystack = ItemStack.EMPTY;
    Slot slot = inventorySlots.get(slotIndex);
    if (slot != null && slot.getHasStack()) {

      ItemStack origStack = slot.getStack();
      copystack = origStack.copy();

      // Note: Merging into Baubles slots is disabled because the used vanilla
      // merge method does not check if the item can go into the slot or not.

      if (slotIndex < 4) {
        // merge from machine input slots to inventory
        if (!mergeItemStackIntoArmor(entityPlayer, origStack, slotIndex)
            && /*
                * !(baubles != null && mergeItemStack(origStack, startBaublesSlot, endBaublesSlot, false)) &&
                */!mergeItemStack(origStack, startPlayerSlot, endHotBarSlot, false)) {
          return ItemStack.EMPTY;
        }

      } else {
        // Check from inv-> charge then inv->hotbar or hotbar->inv
        if (!inv.isItemValidForSlot(0, origStack) || !mergeItemStack(origStack, 0, 4, false)) {

          if (slotIndex >= startBaublesSlot && slotIndex < endBaublesSlot) {
            if (!mergeItemStack(origStack, startHotBarSlot, endHotBarSlot, false) && !mergeItemStack(origStack, startPlayerSlot, endPlayerSlot, false)) {
              return ItemStack.EMPTY;
            }
          } else if (slotIndex < endPlayerSlot) {
            if (/*
                 * !(baubles != null && mergeItemStack(origStack, startBaublesSlot, endBaublesSlot, false)) &&
                 */!mergeItemStack(origStack, startHotBarSlot, endHotBarSlot, false)) {
              return ItemStack.EMPTY;
            }
          } else if (slotIndex >= startHotBarSlot && slotIndex < endHotBarSlot) {
            if (/*
                 * !(baubles != null && mergeItemStack(origStack, startBaublesSlot, endBaublesSlot, false)) &&
                 */!mergeItemStack(origStack, startPlayerSlot, endPlayerSlot, false)) {
              return ItemStack.EMPTY;
            }
          }

        }
      }

      if (origStack.getCount() == 0) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }

      slot.onSlotChanged();

      if (origStack.getCount() == copystack.getCount()) {
        return ItemStack.EMPTY;
      }

      return slot.onTake(entityPlayer, origStack);
    }

    return copystack;
  }

  private boolean mergeItemStackIntoArmor(EntityPlayer entityPlayer, ItemStack origStack, int slotIndex) {
    if (origStack == null || EntityLiving.getSlotForItemStack(origStack).getSlotType() != EntityEquipmentSlot.Type.ARMOR) {
      return false;
    }
    int index = EntityLiving.getSlotForItemStack(origStack).getIndex();
    NonNullList<ItemStack> ai = entityPlayer.inventory.armorInventory;
    if (ai.get(index).isEmpty()) {
      ai.set(index, origStack.copy());
      origStack.setCount(0);
      return true;
    }
    return false;
  }

  private static class SlotImpl extends Slot {
    public SlotImpl(@Nonnull IInventory inv, int idx, int x, int y) {
      super(inv, idx, x, y);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack itemStack) {
      return inventory.isItemValidForSlot(getSlotIndex(), itemStack);
    }
  }
}
