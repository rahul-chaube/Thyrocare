package com.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.demo.R;
import com.demo.model.TestList;

import io.realm.RealmResults;

/**
 * Created by rahul on 15/9/17.
 */

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.ViewHolderClass>{
    Context createTestScreen;
    RealmResults<TestList> testInfoModels;
    public TestListAdapter(Context createTestScreen, RealmResults<TestList> testInfoModels) {
        Log.e(" I am working ",testInfoModels.size()+" ");
        this.createTestScreen=createTestScreen;
        this.testInfoModels=testInfoModels;
    }

    public TestListAdapter() {

    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(createTestScreen).inflate(R.layout.row, parent, false);
        return new ViewHolderClass(view);}

    @Override
    public void onBindViewHolder(ViewHolderClass holder, int position) {
        TestList data=testInfoModels.get(position);
        holder.txtName.setText(data.getTestName());
        holder.checkBox.setText("");
        holder.textAmount.setText(""+data.getAmmount());
        holder.txtDec.setText(data.getSortDesc());
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

        }
    }
}
