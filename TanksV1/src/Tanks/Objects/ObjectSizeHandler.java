package Tanks.Objects;

import java.awt.*;

public class ObjectSizeHandler {

    public static int defaultWidth = 1920;
    public static int defaultHeight = 1080;

    public static float[] scaleConstant()
    {
        float[] scale = new float[2];

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        if(screenSize.getWidth() < defaultWidth){
            scale[0] = (float) (screenSize.getWidth()/defaultWidth);
        } else { scale[0] = 1; }

        if(screenSize.getHeight() < defaultHeight){
            scale[1] = (float) (screenSize.getHeight()/defaultHeight);
        } else { scale[1] = 1; }

        return scale;
    }
}
