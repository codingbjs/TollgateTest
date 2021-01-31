package com.commsens.tollgatetest;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.commsens.tollgatetest.databinding.TollgateListItmeBinding;

import java.util.ArrayList;

public class TollgateListAdapter extends RecyclerView.Adapter<TollgateListAdapter.ViewHolder> {

    TollgateListItmeBinding tollgateListItmeBinding;

    ArrayList <Tollgate> tollgateArrayList;

    public TollgateListAdapter(ArrayList<Tollgate> tollgateArrayList) {
        this.tollgateArrayList = tollgateArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        tollgateListItmeBinding = TollgateListItmeBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(tollgateListItmeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tollgate tollgate = tollgateArrayList.get(position);
        holder.bindTollgateListItem(tollgate);
    }

    @Override
    public int getItemCount() {
        return tollgateArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TollgateListItmeBinding tollgateListItmeBinding;

        public ViewHolder(TollgateListItmeBinding tollgateListItmeBinding) {
            super(tollgateListItmeBinding.getRoot());
            this.tollgateListItmeBinding = tollgateListItmeBinding;
        }

        public void bindTollgateListItem(Tollgate tollgate) {
            tollgateListItmeBinding.countNum.setText(String.valueOf(getAdapterPosition()+1));
            tollgateListItmeBinding.unitName.setText(tollgate.getUnitName());
            tollgateListItmeBinding.unitNo.setText(tollgate.getUnitCode());
            tollgateListItmeBinding.xValue.setText(tollgate.getxValue());
            tollgateListItmeBinding.yValue.setText(tollgate.getyValue());
            tollgateListItmeBinding.routeName.setText(tollgate.getRouteName());
            tollgateListItmeBinding.routeNo.setText(tollgate.getRouteNo());
        }
    }
}
