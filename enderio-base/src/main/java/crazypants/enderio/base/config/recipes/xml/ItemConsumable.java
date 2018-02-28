package crazypants.enderio.base.config.recipes.xml;

import crazypants.enderio.base.config.recipes.InvalidRecipeConfigException;
import crazypants.enderio.base.config.recipes.StaxFactory;

import javax.xml.stream.XMLStreamException;

public class ItemConsumable extends Item {

  private boolean consumed = false;

  @Override
  public boolean setAttribute(StaxFactory factory, String name, String value) throws InvalidRecipeConfigException, XMLStreamException {
    if ("consumed".equals(name)) {
      try {
        this.consumed = Boolean.valueOf(value);
      } catch (NumberFormatException e) {
        throw new InvalidRecipeConfigException("Invalid value in 'consumed': Not true/false");
      }
      return true;
    }

    return super.setAttribute(factory, name, value);
  }

  public boolean getConsumed() {
    return consumed;
  }

}
