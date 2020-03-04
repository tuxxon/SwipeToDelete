package com.touchizen.swipedelete;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private Context mContext;
    private RecyclerListAdapter mRecyclerListAdapter;

    private Drawable deleteIcon;
    private int intrinsicWidth;
    private int intrinsicHeight;
    private Drawable background;
    private int backgroundColor = Color.parseColor("#f44336");
    private Paint clearPaint = new Paint();

    public SwipeToDeleteCallback(Context context, RecyclerListAdapter recyclerListAdapter) {
        this(context,recyclerListAdapter,0, ItemTouchHelper.LEFT);
    }

    public SwipeToDeleteCallback(Context context, RecyclerListAdapter recyclerListAdapter, int dragDirs, int swipeDirs) {
        super(dragDirs,swipeDirs);

        this.mContext = context;
        this.mRecyclerListAdapter = recyclerListAdapter;
        this.deleteIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_delete_white_24);
        this.intrinsicHeight = deleteIcon.getIntrinsicHeight();
        this.intrinsicWidth = deleteIcon.getIntrinsicWidth();
        this.background = new ColorDrawable(backgroundColor);
        this.clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    public int getMovementFlags(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder
    ) {

        /**
         * To disable "swipe" for specific item return 0 here.
         * For example:
         * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
         * if (viewHolder?.adapterPosition == 0) return 0
         */

//        if (viewHolder.getAdapterPosition() == 10)
//            return 0;

        return super.getMovementFlags(recyclerView, viewHolder);
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder target
    ) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mRecyclerListAdapter.removeAt(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(
            @NonNull Canvas c,
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            float dX,
            float dY,
            int actionState,
            boolean isCurrentlyActive
    ) {
        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getBottom() - itemView.getTop();
        boolean isCanceled = (dX == 0f) && !isCurrentlyActive;

        if (isCanceled) {
            clearCanvas(c,
                    itemView.getRight() + dX,
                    itemView.getTop(),
                    itemView.getRight(),
                    itemView.getBottom()
            );
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        // Draw the red delete background
        background.setBounds(
                (int) (itemView.getRight() + dX),
                itemView.getTop(),
                itemView.getRight(),
                itemView.getBottom()
        );
        background.draw(c);

        // Calculate position of delete icon
        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int deleteIconMargin = (itemHeight - intrinsicHeight) /2;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + intrinsicHeight;

        // Draw the delete icon
        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        deleteIcon.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void clearCanvas(Canvas c, float left, float top, float right, float bottom) {
        c.drawRect(left, top, right, bottom, clearPaint );
    }

}
