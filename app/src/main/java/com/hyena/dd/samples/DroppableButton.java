package com.hyena.dd.samples;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;

import com.hyena.dd.core.IDraggable;
import com.hyena.dd.core.IDroppable;

/**
 * Created by yangzc on 17/4/20.
 */

public class DroppableButton extends AppCompatButton implements IDroppable {

    private IDraggable mCapturedDraggable;

    public DroppableButton(Context context) {
        super(context);
    }

    public DroppableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DroppableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void captureDraggable(IDraggable draggable) {
        this.mCapturedDraggable = draggable;
        if (draggable != null && draggable instanceof Button) {
            setText("captured " + ((Button) draggable).getText());
        } else {
            setText("wait capture ...");
        }
    }

    @Override
    public void setCapturing(boolean capturing) {
        if (capturing) {
            setText("capturing...");
        } else {
            setText("wait capture ...");
        }
    }

    @Override
    public IDraggable getCapturedDraggable() {
        return mCapturedDraggable;
    }
}
