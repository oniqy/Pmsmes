package com.example.pmsmes.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemMoveStage extends ItemTouchHelper.Callback{
    private final ItemTouchHelperContract mAdapter;

    public ItemMoveStage(ItemTouchHelperContract mAdapter) {
        this.mAdapter = mAdapter;
    }
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }
    //Hướng di chuyển
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, 0);

    }
    // đối tượng cần di chuyển
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof AdapterStage.MyViewHolder) {
                AdapterStage.MyViewHolder myViewHolder=
                        (AdapterStage.MyViewHolder) viewHolder;
                mAdapter.onColumnSelected(myViewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }
    //hoàn tất di chuyển
    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof AdapterStage.MyViewHolder) {
            AdapterStage.MyViewHolder myViewHolder=
                    (AdapterStage.MyViewHolder) viewHolder;
            mAdapter.onColumnClear(myViewHolder);
        }
    }
    //quá trình di chuyển
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        mAdapter.onColumnMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }
    public interface ItemTouchHelperContract {

        void onColumnMoved(int fromPosition, int toPosition);
        void onColumnSelected(AdapterStage.MyViewHolder myViewHolder);
        void onColumnClear(AdapterStage.MyViewHolder myViewHolder);

    }
}
