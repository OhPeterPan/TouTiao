package com.kotlin.toutiao.widget.helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kotlin.toutiao.interfaces.IOnDragVHListener;
import com.kotlin.toutiao.interfaces.IOnItemMoveListener;

public class ItemDragHelperCallback extends ItemTouchHelper.Callback {
    //设置item的拖拽支持的功能
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int dragFlags;
        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager)
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        else
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        //dragFlags表示拖拽的开关    swipeFlags表示侧滑的开关
        return makeMovementFlags(dragFlags, 0);
    }

    //滑动时调用这个方法
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        //不同的itemType之间不可互相移动
        if (viewHolder.getItemViewType() != target.getItemViewType())
            return false;

        if ((recyclerView.getAdapter()) instanceof IOnItemMoveListener) {
            IOnItemMoveListener listener = ((IOnItemMoveListener) recyclerView.getAdapter());
            listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }

        return true;
    }

    //选中状态发生改变的时候调用这个方法
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {

        //捕获处于非空闲状态的item
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {

            if (viewHolder instanceof IOnDragVHListener) {
                IOnDragVHListener itemViewHolder = (IOnDragVHListener) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        if (viewHolder instanceof IOnDragVHListener) {
            IOnDragVHListener itemViewHolder = (IOnDragVHListener) viewHolder;
            itemViewHolder.onItemFinish();
        }

        super.clearView(recyclerView, viewHolder);
    }

    //不支持长按拖拽功能，手动实现这个功能，因为只有我的频道支持这个操作
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
