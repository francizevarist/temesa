package com.pos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.R;
import com.pos.model.Tickets;

import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.MyViewHolder> {

    private Context context;
    private List<Tickets> ticketsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView price;
        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.text_title);
            price = view.findViewById(R.id.text_price);
            icon = view.findViewById(R.id.thumbnail);
        }
    }


    public TicketsAdapter(Context context, List<Tickets> ticketsList) {
        this.context = context;
        this.ticketsList = ticketsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tickets ticket = ticketsList.get(position);
        holder.title.setText(ticket.getTicket_title());
        holder.price.setText(ticket.getTicket_price());

        switch (ticket.getTicket_icon().toLowerCase()){
            case "truck":
                holder.icon.setImageResource(R.drawable.appicon_truck);
                break;
            case "bus":
                holder.icon.setImageResource(R.drawable.appicon_bus);
                break;
            case "suv":
                holder.icon.setImageResource(R.drawable.appicon_vihicles);
                break;
            case "saloon":
                holder.icon.setImageResource(R.drawable.appicon_vihicles);
                break;
            case "pickup":
                holder.icon.setImageResource(R.drawable.appicon_vihicles);
                break;
            case "animal":
                holder.icon.setImageResource(R.drawable.appicon_animal);
                break;
            case "bike":
                holder.icon.setImageResource(R.drawable.appicon_bike);
                break;
            case "motorbike":
                holder.icon.setImageResource(R.drawable.appicon_motorbike);
                break;
            case "person":
                holder.icon.setImageResource(R.drawable.appicon_person);
                break;

        }


    }

    @Override
    public int getItemCount() {
        return ticketsList.size();
    }

}
