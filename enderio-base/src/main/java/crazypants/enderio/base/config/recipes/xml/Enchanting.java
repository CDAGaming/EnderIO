package crazypants.enderio.base.config.recipes.xml;

import com.enderio.core.common.util.stackable.Things;
import crazypants.enderio.base.config.recipes.InvalidRecipeConfigException;
import crazypants.enderio.base.config.recipes.StaxFactory;
import crazypants.enderio.base.recipe.MachineRecipeRegistry;
import crazypants.enderio.base.recipe.enchanter.EnchanterRecipe;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

public class Enchanting extends AbstractConditional {

  private ItemIntegerAmount input;
  private Enchantment enchantment;

  @Override
  public Object readResolve() throws InvalidRecipeConfigException {
    try {
      super.readResolve();
      if (input == null) {
        throw new InvalidRecipeConfigException("Missing <input>");
      }
      if (enchantment == null) {
        throw new InvalidRecipeConfigException("Missing <enchantment>");
      }

      valid = input.isValid() && enchantment.isValid();

    } catch (InvalidRecipeConfigException e) {
      throw new InvalidRecipeConfigException(e, "in <enchanting>");
    }
    return this;
  }

  @Override
  public void enforceValidity() throws InvalidRecipeConfigException {
    input.enforceValidity();
    enchantment.enforceValidity();
    if (input.getThing().isEmpty() || enchantment.getEnchantment() == null) {
      throw new InvalidRecipeConfigException("Valid child elements are invalid in <enchanting>");
    }
  }

  @Override
  public void register(@Nonnull String recipeName) {
    if (isValid() && isActive()) {
      final Things thing = input.getThing();
      final net.minecraft.enchantment.Enchantment enchantment2 = enchantment.getEnchantment();
      if (!thing.isEmpty() && enchantment2 != null) {
        EnchanterRecipe recipe = new EnchanterRecipe(thing, input.getAmount(), enchantment2, enchantment.getCostMultiplier());
        MachineRecipeRegistry.instance.registerRecipe(MachineRecipeRegistry.ENCHANTER, recipe);
      }
    }
  }

  @Override
  public boolean setElement(StaxFactory factory, String name, StartElement startElement) throws InvalidRecipeConfigException, XMLStreamException {
    if ("input".equals(name)) {
      if (input == null) {
        input = factory.read(new ItemIntegerAmount().setAllowDelaying(false), startElement);
        return true;
      }
    }
    if ("enchantment".equals(name)) {
      if (enchantment == null) {
        enchantment = factory.read(new Enchantment(), startElement);
        return true;
      }
    }

    return super.setElement(factory, name, startElement);
  }

}