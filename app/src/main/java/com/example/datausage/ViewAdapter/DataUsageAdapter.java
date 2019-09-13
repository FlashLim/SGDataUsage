package com.example.datausage.ViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.datausage.DataModel.AnnualDataUsage;
import com.example.datausage.Network.Model.DataUsageRecord;
import com.example.datausage.R;
import com.example.datausage.Util.Helper;

import java.util.ArrayList;
import java.util.List;

public class DataUsageAdapter extends RecyclerView.Adapter<DataUsageAdapter.MyViewHolder> {
    private List<AnnualDataUsage> mDataSet;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView year, dataYear;
        Button button;

        public MyViewHolder(View v) {
            super(v);
            year = v.findViewById(R.id.year);
            dataYear = v.findViewById(R.id.year_data);
            button = v.findViewById(R.id.button);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DataUsageAdapter(List<AnnualDataUsage>  myDataSet, Context context) {
        mDataSet = myDataSet;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DataUsageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_data_usage, parent, false);

        return new MyViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.year.setText(String.valueOf(mDataSet.get(position).getYear()));
        holder.dataYear.setText(Helper.round(mDataSet.get(position).getTotalMobileData(), 6));

        List<DataUsageRecord> dataUsageRecords = mDataSet.get(position).getQuarterRecords();
        for (int i=0; i<dataUsageRecords.size()-1; i++){
            if (Double.parseDouble(dataUsageRecords.get(i).getVolumeOfMobileData())
                    > Double.parseDouble(dataUsageRecords.get(i+1).getVolumeOfMobileData())){
                holder.button.setVisibility(View.VISIBLE);
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(v, position);
                    }
                });
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private void showPopup(View view, int position) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        ListView listView = popupView.findViewById(R.id.quarter_list);
        List<String> listItem = new ArrayList<>();
        for(DataUsageRecord dataUsageRecord : mDataSet.get(position).getQuarterRecords()) {
            listItem.add(dataUsageRecord.getQuarter() + " " +dataUsageRecord.getVolumeOfMobileData());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupView.setElevation(5.0f);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

}