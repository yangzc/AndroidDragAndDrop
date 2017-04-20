package com.hyena.dd.samples;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;

import com.hyena.dd.core.IDraggable;
import com.hyena.dd.core.IDraggableGhost;

/**
 * Created by yangzc on 17/4/20.
 */

public class DraggableButton extends Button implements IDraggable {

    private IDraggableGhost mDraggableGhost;
    private String text;

    public DraggableButton(Context context) {
        super(context);
        init();
    }

    public DraggableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DraggableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.text = (String) getText();
    }

    @Override
    public IDraggableGhost toGhost() {
        if (mDraggableGhost == null) {
            DraggableGhostButton ghost = new DraggableGhostButton(getContext());
            ghost.setText(getText());
            ghost.setNatureDraggable(this);
            mDraggableGhost = ghost;
        }
        setText("being ghost");
        return mDraggableGhost;
    }

    @Override
    public void reset() {
        if (!TextUtils.isEmpty(text))
            setText(text);
    }
}
