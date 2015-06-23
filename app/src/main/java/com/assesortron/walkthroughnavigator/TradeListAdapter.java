package com.assesortron.walkthroughnavigator;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;
import com.assesortron.walkthroughnavigator.DisplayFragment.ParentListener;

/**
 * Created by otf on 6/17/15.
 */
public class TradeListAdapter implements ListAdapter {

    Context context;
    List<Navigator.DisplayObject> objs;
    ParentListener parentListener;

    TradeListAdapter(Context context, ParentListener parentListener, List<Navigator.DisplayObject> objs) {
        this.context = context;
        this.parentListener = parentListener;
        this.objs = objs;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return objs.size();
    }

    @Override
    public Object getItem(int position) {
        return objs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater infalInfalter = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInfalter.inflate(R.layout.list_preview_trades, null);
        }
        TextView axis3 = (TextView)view.findViewById(R.id.preview_axis_three_label);
        CheckBox complete = (CheckBox)view.findViewById(R.id.trade_entry_compete);

        final Navigator.DisplayObject wt = objs.get(position);

        axis3.setText(wt.getAxis3Value().toString());
        complete.setChecked(wt.isComplete());
        complete.setClickable(false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentListener.display(wt);
            }
        });
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return objs.isEmpty();
    }
}
