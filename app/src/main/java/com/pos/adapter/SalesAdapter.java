package com.pos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pos.R;
import com.pos.model.SalesInfo;
import com.pos.model.Tickets;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.MyViewHolder> {

    private Context context;
    private List<SalesInfo> salesInfoList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text_serialno;
        public TextView text_tickettype;
        public TextView text_ticketcount;
        public TextView text_amount;

        public MyViewHolder(View view) {
            super(view);
            text_serialno = view.findViewById(R.id.text_serialno);
            text_tickettype = view.findViewById(R.id.text_tickettype);
            text_ticketcount = view.findViewById(R.id.text_ticketcount);
            text_amount = view.findViewById(R.id.text_amount);
        }
    }


    public SalesAdapter(Context context, List<SalesInfo> salesInfoList) {
        this.context = context;
        this.salesInfoList = salesInfoList;
    }

    @Override
    public SalesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_sales, parent, false);

        return new SalesAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(SalesAdapter.MyViewHolder holder, int position) {
        SalesInfo salesInfo = salesInfoList.get(position);
        holder.text_serialno.setText(salesInfo.getSerialno());
        holder.text_tickettype.setText(salesInfo.getTickettype());
        holder.text_ticketcount.setText(salesInfo.getTicketcount());
        holder.text_amount.setText(salesInfo.getAmount());
    }

    @Override
    public int getItemCount() {
        return salesInfoList.size();
    }

}

