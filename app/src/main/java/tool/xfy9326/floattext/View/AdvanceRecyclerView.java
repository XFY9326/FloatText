package tool.xfy9326.floattext.View;

import android.content.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;

/*
高级列表操作
支持空列表时显示空布局
*/

public class AdvanceRecyclerView extends RecyclerView
{
    private View emptyView;
	
    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged ()
		{
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted (int positionStart, int itemCount)
		{
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved (int positionStart, int itemCount)
		{
            checkIfEmpty();
        }
    };

    public AdvanceRecyclerView (Context context)
	{
        super(context);
    }

    public AdvanceRecyclerView (Context context, AttributeSet attrs)
	{
        super(context, attrs);
    }

    public AdvanceRecyclerView (Context context, AttributeSet attrs, int defStyle)
	{
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter (Adapter adapter)
	{
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null)
		{
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null)
		{
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    public void setEmptyView (View emptyView)
	{
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    void checkIfEmpty ()
	{
        if (emptyView != null && getAdapter() != null)
		{
            final boolean emptyViewVisible = getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }
}
