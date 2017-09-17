package com.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.R;
import com.demo.activity.TestDetailActivity;
import com.demo.model.TestModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by rahul on 17/9/17.
 */

public class TestHistoryAdapter extends RecyclerView.Adapter<TestHistoryAdapter.ViewHolderClass> {
    private static final String TestName = "testName";
    private static final String Complete = "complete";
    private static final String TotalAmount = "amount";
    private static final String Data = "data";
    private static final String time = "time";

    List<TestModel> testModelList;
    Context context;

    public TestHistoryAdapter(Context context, List<TestModel> testModelList) {
        this.context = context;
        this.testModelList = testModelList;
    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new ViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderClass holder, int position) {
        final TestModel data = testModelList.get(position);
        holder.txtName.setText(data.getName());
        holder.checkBox.setText("");
        holder.textAmount.setText("" + data.getTotalamount());
        String myFormat = "MMM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        calendar.setTimeInMillis(data.getTime());
        holder.txtDec.setText(sdf.format(calendar.getTime()));
        holder.txtDec.setTextSize(12);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TestDetailActivity.class);
                String list[] = data.getData().toArray(new String[data.getData().size()]);
                intent.putExtra(Data, list);
                intent.putExtra(time, data.getTime());
                intent.putExtra(Complete, data.getComplete());
                intent.putExtra(TotalAmount, data.getTotalamount());
                intent.putExtra(TestName, data.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return testModelList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        TextView txtName, txtDec, textAmount;
        CheckBox checkBox;
        LinearLayout linearLayout;
        boolean isChecked = false;

        public ViewHolderClass(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.rowLinearLayout);
            txtName = (TextView) itemView.findViewById(R.id.testName);
            txtDec = (TextView) itemView.findViewById(R.id.testSortDesc);
            textAmount = (TextView) itemView.findViewById(R.id.testAmount);
            checkBox = (CheckBox) itemView.findViewById(R.id.testChecked);

            checkBox.setVisibility(View.GONE);

        }
    }
}
