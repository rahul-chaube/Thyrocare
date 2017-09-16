package com.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.demo.R;
import com.demo.activity.Checkout;
import com.demo.model.TestList;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by rahul on 15/9/17.
 */

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.ViewHolderClass>{
    Context createTestScreen;
    RealmResults<TestList> testInfoModels;
    int code=0;
    public TestListAdapter(Context createTestScreen, RealmResults<TestList> testInfoModels) {
        this.createTestScreen=createTestScreen;
        this.testInfoModels=testInfoModels;
    }
    ArrayList<String> selectedTestsList=new ArrayList<>();

    public TestListAdapter(Checkout checkout, RealmResults<TestList> testLists, int i) {
        this.createTestScreen=checkout;
        this.testInfoModels=testLists;
        this.code=i;
    }

    public  ArrayList<String> getSelectedTest()
    {
        return  selectedTestsList;
    }

    public TestListAdapter() {

    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(createTestScreen).inflate(R.layout.row, parent, false);
        return new ViewHolderClass(view);}

    @Override
    public void onBindViewHolder(ViewHolderClass holder, int position) {
        final TestList data=testInfoModels.get(position);
        holder.txtName.setText(data.getTestName());
        holder.checkBox.setText("");
        holder.textAmount.setText(""+data.getAmmount());
        holder.txtDec.setText(data.getSortDesc());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    selectedTestsList.add(data.getTestId());
                }
                else
                {
                    selectedTestsList.remove(data.getTestId());
                }
                Log.e("Selected List ",selectedTestsList.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return testInfoModels.size();
    }

    class  ViewHolderClass extends RecyclerView.ViewHolder {
        TextView txtName,txtDec,textAmount;
        CheckBox checkBox;
        boolean isChecked=false;
        public ViewHolderClass(View itemView) {
            super(itemView);
            txtName= (TextView) itemView.findViewById(R.id.testName);
            txtDec= (TextView) itemView.findViewById(R.id.testSortDesc);
            textAmount= (TextView) itemView.findViewById(R.id.testAmount);
            checkBox= (CheckBox) itemView.findViewById(R.id.testChecked);

            if (code==1)
            {
                checkBox.setVisibility(View.GONE);
            }

        }
    }
}
