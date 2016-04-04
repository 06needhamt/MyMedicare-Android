package com.needham.thomas.medicare.root.Classes;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by Thomas Needham on 04/04/2016.
 * This class crates a border that will be placed around a text view
 */
public class ListViewBorder extends ShapeDrawable {
    /**
     * Constructs a new border
     *
     * @param s the shape of the border
     */
    public ListViewBorder(Shape s) {
        super(s);
        this.getPaint().setARGB(0xFF, 0x00, 0x00, 0x00);
        this.getPaint().setStyle(Paint.Style.STROKE);
        this.getPaint().setStrokeWidth(5.0f);

    }
}
//ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
