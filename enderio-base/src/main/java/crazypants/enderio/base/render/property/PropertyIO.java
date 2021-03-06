package crazypants.enderio.base.render.property;

import com.enderio.core.common.util.NNList;
import com.enderio.core.common.util.NNList.NNIterator;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import crazypants.enderio.base.render.property.IOMode.EnumIOMode;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PropertyIO extends PropertyHelper<IOMode> {

  private final static @Nonnull ImmutableSet<IOMode> allowedValues;
  private final static @Nonnull Map<String, IOMode> nameToValue = Maps.newHashMap();
  static {
    List<IOMode> values = new ArrayList<IOMode>();
    NNIterator<EnumFacing> faces = NNList.FACING.iterator();
    while (faces.hasNext()) {
      EnumFacing facing = faces.next();
      NNIterator<EnumIOMode> iomodes = IOMode.EnumIOMode.IOMODES.iterator();
      while (iomodes.hasNext()) {
        IOMode value = IOMode.get(facing, iomodes.next());
        values.add(value);
        nameToValue.put(value.toString(), value);
      }
    }
    allowedValues = ImmutableSet.copyOf(values);
  }

  private static final @Nonnull PropertyIO instance = new PropertyIO("iomode");

  protected PropertyIO(String name) {
    super(name, IOMode.class);
  }

  public static @Nonnull PropertyIO getInstance() {
    return instance;
  }

  @Override
  public @Nonnull Collection<IOMode> getAllowedValues() {
    return allowedValues;
  }

  @Override
  public @Nonnull String getName(@Nonnull IOMode value) {
    return value.toString();
  }

  @Override
  @SideOnly(Side.CLIENT)
  public @Nonnull Optional<IOMode> parseValue(@Nonnull String value) {
    return Optional.fromNullable(nameToValue.get(value));
  }
 
}
