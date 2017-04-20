package com.hyean.dd;

/**
 * Created by yangzc on 17/4/19.
 */

public interface IDraggable {

    /**
     * 转化为幽灵状态并返回幽灵
     */
    IDraggableGhost toGhost();

    /**
     * 状态还原
     */
    void reset();

}
