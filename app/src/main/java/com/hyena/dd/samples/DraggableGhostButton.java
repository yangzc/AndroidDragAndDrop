package com.hyena.dd.samples;

import android.content.Context;
import android.util.AttributeSet;

import com.hyean.dd.IDraggable;
import com.hyean.dd.IDraggableGhost;


/**
 * Created by yangzc on 17/4/20.
 */

public class DraggableGhostButton extends DraggableButton implements IDraggableGhost {

    private IDraggable mNatureDraggable;

    public DraggableGhostButton(Context context) {
        super(context);
    }

    public DraggableGhostButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableGhostButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setNatureDraggable(IDraggable draggable) {
        this.mNatureDraggable = draggable;
    }

    @Override
    public IDraggable getNatureDraggable() {
        return mNatureDraggable;
    }
}
