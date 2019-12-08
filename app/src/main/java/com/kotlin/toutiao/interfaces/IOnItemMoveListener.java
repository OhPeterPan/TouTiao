package com.kotlin.toutiao.interfaces;

/**
 * Item移动后 触发
 * Created by Meiji on 2017/3/11.
 */

public interface IOnItemMoveListener {
    /**
     *
     * @param fromPosition  拖拽的item的position
     * @param toPosition    目标item的position
     */
    void onItemMove(int fromPosition, int toPosition);
}
