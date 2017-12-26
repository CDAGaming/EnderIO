package crazypants.enderio.machines.machine.slicensplice;

import java.util.List;

import javax.annotation.Nonnull;

import static crazypants.enderio.base.config.Config.slicenspliceToolDamageChance;
import static crazypants.enderio.machines.capacitor.CapacitorKey.SLICE_POWER_BUFFER;
import static crazypants.enderio.machines.capacitor.CapacitorKey.SLICE_POWER_INTAKE;
import static crazypants.enderio.machines.capacitor.CapacitorKey.SLICE_POWER_USE;

import crazypants.enderio.base.machine.baselegacy.AbstractPoweredTaskEntity;
import crazypants.enderio.base.machine.baselegacy.SlotDefinition;
import crazypants.enderio.base.paint.IPaintable;
import crazypants.enderio.base.recipe.IMachineRecipe;
import crazypants.enderio.base.recipe.IManyToOneRecipe;
import crazypants.enderio.base.recipe.MachineRecipeInput;
import crazypants.enderio.base.recipe.MachineRecipeRegistry;
import crazypants.enderio.base.recipe.ManyToOneMachineRecipe;
import crazypants.enderio.base.recipe.RecipeInput;
import crazypants.enderio.util.Prep;
import info.loenwind.autosave.annotations.Storable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;

@Storable
public class TileSliceAndSplice extends AbstractPoweredTaskEntity implements IPaintable.IPaintableTileEntity {

  protected final static int axeIndex = 6;
  protected final static int shearsIndex = 7;
  private EntityLivingBase fakePlayer;

  public TileSliceAndSplice() {
    super(new SlotDefinition(8, 1, 1), SLICE_POWER_INTAKE, SLICE_POWER_BUFFER, SLICE_POWER_USE);
  }

  @Override
  public @Nonnull String getMachineName() {
    return MachineRecipeRegistry.SLICENSPLICE;
  }

  @Override
  public int getInventoryStackLimit() {
    return 1;
  }

  @Override
  protected IMachineRecipe canStartNextTask(float chance) {
    if (!hasTools()) {
      return null;
    }
    return super.canStartNextTask(chance);
  }

  private @Nonnull ItemStack getAxe() {
    return getStackInSlot(axeIndex);
  }

  private @Nonnull ItemStack getShears() {
    return getStackInSlot(shearsIndex);
  }

  @Override
  protected boolean checkProgress(boolean redstoneChecksPassed) {
    if (!hasTools()) {
      return false;
    }
    return super.checkProgress(redstoneChecksPassed);
  }

  private boolean hasTools() {
    return Prep.isValid(getAxe()) && Prep.isValid(getShears());
  }

  @Override
  protected void taskComplete() {
    super.taskComplete();
    damageTool(getAxe(), axeIndex);
    damageTool(getShears(), shearsIndex);
  }

  @Override
  protected int usePower(int wantToUse) {
    if (random.nextFloat() < slicenspliceToolDamageChance) {
      damageTool(getAxe(), axeIndex);
    }
    if (random.nextFloat() < slicenspliceToolDamageChance) {
      damageTool(getShears(), shearsIndex);
    }
    return super.usePower(wantToUse);
  }

  private void damageTool(@Nonnull ItemStack tool, int toolIndex) {
    if (Prep.isValid(tool) && tool.isItemStackDamageable()) {
      tool.damageItem(1, getFakePlayer());
      if (tool.getItemDamage() >= tool.getMaxDamage()) {
        tool.shrink(1);
      }
      markDirty();
    }
  }

  @SuppressWarnings("null")
  private @Nonnull EntityLivingBase getFakePlayer() {
    if (fakePlayer == null) {
      fakePlayer = FakePlayerFactory
          .getMinecraft(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(world.provider.getDimension()));
    }
    return fakePlayer;
  }

  @Override
  protected @Nonnull MachineRecipeInput[] getRecipeInputs() {
    MachineRecipeInput[] res = new MachineRecipeInput[slotDefinition.getNumInputSlots() - 2];
    int fromSlot = slotDefinition.minInputSlot;
    for (int i = 0; i < res.length; i++) {
      res[i] = new MachineRecipeInput(fromSlot, getStackInSlot(fromSlot));
      fromSlot++;
    }
    return res;
  }

  @Override
  public boolean isMachineItemValidForSlot(int slot, @Nonnull ItemStack itemstack) {
    if (Prep.isInvalid(itemstack)) {
      return false;
    }
    if (!slotDefinition.isInputSlot(slot)) {
      return false;
    }
    if (slot == axeIndex) {
      return itemstack.getItem() instanceof ItemAxe;
    }
    if (slot == shearsIndex) {
      return itemstack.getItem() instanceof ItemShears;
    }

    ItemStack currentStackInSlot = getStackInSlot(slot);
    if (Prep.isValid(currentStackInSlot)) {
      return currentStackInSlot.isItemEqual(itemstack);
    }

    int numSlotsFilled = 0;
    for (int i = slotDefinition.getMinInputSlot(); i <= slotDefinition.getMaxInputSlot(); i++) {
      if (i >= 0 && i < inventory.length && i != axeIndex && i != shearsIndex) {
        if (Prep.isValid(getStackInSlot(i))) {
          numSlotsFilled++;
        }
      }
    }
    List<IMachineRecipe> recipes = MachineRecipeRegistry.instance.getRecipesForInput(getMachineName(), MachineRecipeInput.create(slot, itemstack));

    return isValidInputForAlloyRecipe(slot, itemstack, numSlotsFilled, recipes);
  }

  private boolean isValidInputForAlloyRecipe(int slot, ItemStack itemstack, int numSlotsFilled, List<IMachineRecipe> recipes) {
    ItemStack[] resultInv = new ItemStack[slotDefinition.getNumInputSlots()];
    for (int i = slotDefinition.getMinInputSlot(); i <= slotDefinition.getMaxInputSlot(); i++) {
      if (i >= 0 && i < inventory.length && i != axeIndex && i != shearsIndex) {
        if (i == slot) {
          resultInv[i] = itemstack;
        } else {
          resultInv[i] = inventory[i];
        }
      }
    }

    for (IMachineRecipe recipe : recipes) {
      if (recipe instanceof ManyToOneMachineRecipe) {
        for (IManyToOneRecipe oneRecipe : ((ManyToOneMachineRecipe) recipe).getRecipesThatHaveTheseAsValidRecipeComponents(resultInv)) {
          boolean valid = true;
          for (int i = 0; valid && i < resultInv.length; i++) {
            // skip Tool slots and empty slots
            if (resultInv[i] == null || resultInv[i].isEmpty())
              continue;
            // check if the current item set is valid for this recipe
            for (RecipeInput ri : oneRecipe.getInputs()) {
              if (ri.getSlotNumber() != i)
                continue;
              if (!ri.isInput(resultInv[i]))
                valid = false;
              break;
            }
          }
          // Invalid, check the next recipe
          if (!valid)
            continue;
          return true;
        }
      }
    }
    return false;
  }

}
