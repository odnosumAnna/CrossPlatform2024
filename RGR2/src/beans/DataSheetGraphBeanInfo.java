package beans;

import java.beans.*;

public class DataSheetGraphBeanInfo extends SimpleBeanInfo {
    private PropertyDescriptor[] propertyDescriptors;

    public DataSheetGraphBeanInfo() {
        try {
            propertyDescriptors = new PropertyDescriptor[]
                    {
                            new PropertyDescriptor("color", DataSheetGraph.class),
                            new PropertyDescriptor("connected", DataSheetGraph.class),
                            new PropertyDescriptor("deltaX", DataSheetGraph.class),
                            new PropertyDescriptor("deltaY", DataSheetGraph.class)
                    };
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors;
    }
}
