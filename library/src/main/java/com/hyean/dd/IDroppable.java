package com.hyean.dd;

/**
 * Created by yangzc on 17/4/19.
 */

public interface IDroppable {

    /**
     * 捕获可拖拽物体
     * @param draggable
     */
    void captureDraggable(IDraggable draggable);

    /**
     * 是否是捕获状态
     * @param capturing
     */
    void setCapturing(boolean capturing);

    /**
     * 获得已经捕获的拖拽对象
     * @return
     */
    IDraggable getCapturedDraggable();
}
