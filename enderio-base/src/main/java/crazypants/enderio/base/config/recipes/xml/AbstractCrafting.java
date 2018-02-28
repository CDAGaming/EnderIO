package crazypants.enderio.base.config.recipes.xml;

import crazypants.enderio.base.config.recipes.InvalidRecipeConfigException;
import crazypants.enderio.base.config.recipes.StaxFactory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCrafting extends AbstractConditional {

  protected List<Output> outputs;

  @Override
  public Object readResolve() throws InvalidRecipeConfigException {
    super.readResolve();
    if (outputs == null || outputs.isEmpty()) {
      throw new InvalidRecipeConfigException("Missing <output>");
    }

    valid = checkOutputCount(getOutputs().size());

    return this;
  }

  protected boolean checkOutputCount(int count) {
    return count == 1;
  }

  public Output getOutput() {
    for (Output output : outputs) {
      if (output.isValid() && output.isActive()) {
        return output;
      }
    }
    return null;
  }

  public List<Output> getOutputs() {
    List<Output> result = new ArrayList<Output>();
    for (Output output : outputs) {
      if (output.isValid() && output.isActive()) {
        result.add(output);
      }
    }
    return result;
  }

  @Override
  public boolean setAttribute(StaxFactory factory, String name, String value) throws InvalidRecipeConfigException, XMLStreamException {
    return super.setAttribute(factory, name, value);
  }

  @Override
  public boolean setElement(StaxFactory factory, String name, StartElement startElement) throws InvalidRecipeConfigException, XMLStreamException {
    if ("output".equals(name)) {
      if (outputs == null) {
        outputs = new ArrayList<Output>();
      }
      outputs.add(factory.read(new Output(), startElement));
      return true;
    }

    return super.setElement(factory, name, startElement);
  }

  @Override
  public void enforceValidity() throws InvalidRecipeConfigException {
    for (Output output : outputs) {
      if (output.isActive()) {
        output.enforceValidity();
      }
    }
  }

}