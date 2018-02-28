package crazypants.enderio.base.config.recipes.xml;

import crazypants.enderio.base.config.recipes.InvalidRecipeConfigException;
import crazypants.enderio.base.config.recipes.StaxFactory;

import javax.xml.stream.XMLStreamException;

public class ItemIntegerAmount extends Item {

  private int amount = 1;

  @Override
  public boolean setAttribute(StaxFactory factory, String name, String value) throws InvalidRecipeConfigException, XMLStreamException {
    if ("amount".equals(name)) {
      try {
        this.amount = Integer.parseInt(value);
      } catch (NumberFormatException e) {
        throw new InvalidRecipeConfigException("Invalid value in 'amount': Not a number");
      }
      return true;
    }

    return super.setAttribute(factory, name, value);
  }

  public int getAmount() {
    return amount;
  }

}
