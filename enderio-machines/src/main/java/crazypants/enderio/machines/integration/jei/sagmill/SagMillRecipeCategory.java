package crazypants.enderio.machines.integration.jei.sagmill;

import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.gui.tooltip.TooltipHandlerGrinding;
import crazypants.enderio.base.integration.jei.energy.EnergyIngredient;
import crazypants.enderio.base.integration.jei.energy.EnergyIngredientRenderer;
import crazypants.enderio.base.recipe.IRecipe;
import crazypants.enderio.base.recipe.RecipeOutput;
import crazypants.enderio.base.recipe.sagmill.SagMillRecipeManager;
import crazypants.enderio.machines.EnderIOMachines;
import crazypants.enderio.machines.lang.Lang;
import crazypants.enderio.machines.machine.sagmill.GuiSagMill;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.List;

import static crazypants.enderio.machines.init.MachineObject.block_sag_mill;
import static crazypants.enderio.machines.init.MachineObject.block_simple_sag_mill;
import static crazypants.enderio.machines.machine.sagmill.ContainerSagMill.*;

public class SagMillRecipeCategory extends BlankRecipeCategory<SagRecipe> implements ITooltipCallback<ItemStack> {

  public static final @Nonnull String UID = "SagMill";

  public static void register(IModRegistry registry, IGuiHelper guiHelper) {

    registry.addRecipeCategories(new SagMillRecipeCategory(guiHelper));
    registry.handleRecipes(IRecipe.class, SagRecipe::new, SagMillRecipeCategory.UID);
    registry.addRecipeClickArea(GuiSagMill.class, 155, 42, 16, 16, SagMillRecipeCategory.UID);
    registry.addRecipeCategoryCraftingItem(new ItemStack(block_sag_mill.getBlockNN()), SagMillRecipeCategory.UID);
    registry.addRecipeCategoryCraftingItem(new ItemStack(block_simple_sag_mill.getBlockNN()), SagMillRecipeCategory.UID);

    registry.addRecipes(SagMillRecipeManager.getInstance().getRecipes(), UID);

    registry.getRecipeTransferRegistry().addRecipeTransferHandler(
        new SagMillRecipeTransferHandler(registry, SagMillRecipeCategory.UID, FIRST_RECIPE_SLOT, NUM_RECIPE_SLOT, FIRST_INVENTORY_SLOT, NUM_INVENTORY_SLOT),
        SagMillRecipeCategory.UID);
    registry.getRecipeTransferRegistry().addRecipeTransferHandler(new SimpleSagMillRecipeTransferHandler(registry, SagMillRecipeCategory.UID, FIRST_RECIPE_SLOT,
        NUM_RECIPE_SLOT, FIRST_INVENTORY_SLOT - 1, NUM_INVENTORY_SLOT), SagMillRecipeCategory.UID);
  }

  // Offsets from full size gui, makes it much easier to get the location
  // correct
  private int xOff = 45;
  private int yOff = 3;

  @Nonnull
  private final IDrawable background;

  @Nonnull
  protected final IDrawableAnimated arrow;

  private final TooltipHandlerGrinding ballsTT = new TooltipHandlerGrinding();

  public SagMillRecipeCategory(IGuiHelper guiHelper) {
    ResourceLocation backgroundLocation = EnderIO.proxy.getGuiTexture("crusher");
    background = guiHelper.createDrawable(backgroundLocation, xOff, yOff, 109, 78);

    IDrawableStatic flameDrawable = guiHelper.createDrawable(backgroundLocation, 201, 1, 16, 22);
    arrow = guiHelper.createAnimatedDrawable(flameDrawable, 200, IDrawableAnimated.StartDirection.TOP, false);
  }

  @Override
  public @Nonnull String getUid() {
    return UID;
  }

  @Override
  public @Nonnull String getTitle() {
    return block_sag_mill.getBlockNN().getLocalizedName();
  }

  @Override
  public @Nonnull IDrawable getBackground() {
    return background;
  }

  @Override
  public void drawExtras(@Nonnull Minecraft minecraft) {
    arrow.draw(minecraft, 80 - xOff, 32 - yOff);
  }

  @Override
  public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull SagRecipe recipeWrapper, @Nonnull IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.addTooltipCallback(this);
    IGuiIngredientGroup<EnergyIngredient> group = recipeLayout.getIngredientsGroup(EnergyIngredient.class);

    guiItemStacks.init(0, true, 79 - xOff, 11 - yOff);
    guiItemStacks.init(1, false, 48 - xOff, 58 - yOff);
    guiItemStacks.init(2, false, 69 - xOff, 58 - yOff);
    guiItemStacks.init(3, false, 90 - xOff, 58 - yOff);
    guiItemStacks.init(4, false, 111 - xOff, 58 - yOff);
    guiItemStacks.init(5, true, 121 - xOff, 22 - yOff);
    group.init(6, true, EnergyIngredientRenderer.INSTANCE, 134 - xOff, 58 - yOff, 60, 10, 0, 0);

    guiItemStacks.addTooltipCallback(new ITooltipCallback<ItemStack>() {
      @Override
      public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack ingredient, @Nonnull List<String> tooltip) {
        switch (slotIndex) {
        case 1:
        case 2:
        case 3:
        case 4:
          if (slotIndex <= recipeWrapper.getRecipe().getOutputs().length) {
            RecipeOutput output = recipeWrapper.getRecipe().getOutputs()[slotIndex - 1];
            float chance = output.getChance();
            if (chance > 0 && chance < 1) {
              int chanceInt = (int) (chance * 100);
              Object[] objects = { chanceInt };
              tooltip.add(TextFormatting.GRAY + MessageFormat.format(Lang.JEI_SAGMILL_CHANCE.get(), objects));
            }
          }
          return;
        case 5:
          if (ballsTT.shouldHandleItem(ingredient)) {
            ballsTT.addDetailedEntries(ingredient, Minecraft.getMinecraft().player, tooltip, true);
          }
          return;
        default:
          return;
        }
      }
    });

    guiItemStacks.set(ingredients);
    group.set(ingredients);
  }

  @Override
  public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack ingredient, @Nonnull List<String> tooltip) {
  }

  @Override
  public @Nonnull String getModName() {
    return EnderIOMachines.MODID;
  }

}
