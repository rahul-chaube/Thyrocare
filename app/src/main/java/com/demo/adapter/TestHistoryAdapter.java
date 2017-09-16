package com.demo.adapter;

import android.content.Context;
import android.icu.util.Calendar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.demo.R;
import com.demo.model.TestModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by rahul on 17/9/17.
 */

public class TestHistoryAdapter extends RecyclerView.Adapter<TestHistoryAdapter.ViewHolderClass>{

List<TestModel> testModelList;
    Context context;
    public TestHistoryAdapter(Context context, List<TestModel> testModelList)
    {
        this.context=context;
        this.testModelList=testModelList;
        Log.e("Test History Model "," "+testModelList.size());
    }
    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new ViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderClass holder, int position) {
        final TestModel data=testModelList.get(position);
        holder.txtName.setText(data.getName());
        holder.checkBox.setText("");
        holder.textAmount.setText(""+data.getTotalamount());
        String myFormat = "MMM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        calendar.setTimeInMillis(data.getTime());
        holder.txtDec.setText(sdf.format(calendar.getTime()));
        holder.txtDec.setTextSize(12);
    }

    @Override
    public int getItemCount() {
        return testModelList.size();
    }

    public class  ViewHolderClass extends RecyclerView.ViewHolder {
        TextView txtName,txtDec,textAmount;
        CheckBox checkBox;
        boolean isChecked=false;
        public ViewHolderClass(View itemView) {
            super(itemView);
            txtName= (TextView) itemView.findViewById(R.id.testName);
            txtDec= (TextView) itemView.findViewById(R.id.testSortDesc);
            textAmount= (TextView) itemView.findViewById(R.id.testAmount);
            checkBox= (CheckBox) itemView.findViewById(R.id.testChecked);

                checkBox.setVisibility(View.GONE);

        }
    }
}
