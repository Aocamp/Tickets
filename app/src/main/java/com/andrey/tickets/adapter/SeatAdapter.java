package com.andrey.tickets.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrey.tickets.listener.OnItemClick;
import com.andrey.tickets.R;
import com.andrey.tickets.model.Seat;

import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder>{
    class SeatViewHolder extends RecyclerView.ViewHolder{
        ImageView seatImage;
        TextView seatText;

        public SeatViewHolder(View v){
            super(v);
            seatImage = v.findViewById(R.id.img_seat);
            seatText = v.findViewById(R.id.text_Seat);
        }
    }

    private List<Seat> mUsers;
    private OnItemClick mCallback;

    private final LayoutInflater mInflater;

    public SeatAdapter(Context context, List<Seat> mUsers, OnItemClick listener) {
        this.mUsers = mUsers;
        mInflater = LayoutInflater.from(context);
        mCallback = listener;
    }

    @NonNull
    @Override
    public SeatAdapter.SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_seat, parent, false);
        return new SeatAdapter.SeatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SeatAdapter.SeatViewHolder holder, int position) {
        final Seat seat = mUsers.get(position);

        holder.seatText.setText(seat.getName());

        if (seat.getTaken() == 1){
            holder.seatImage.setImageResource(R.drawable.seat_selected);
        }

        holder.seatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seat.getTaken() == 0){
                    holder.seatImage.setImageResource(R.drawable.seat_selected_by_user);
                    holder.seatImage.setTag(R.drawable.seat_selected_by_user);
                    seat.setTaken(1);
                    mCallback.onClick(seat);
                }
            }
        });
    }

    public void setItem(List<Seat> users){
        mUsers = users;
        notifyDataSetChanged();
    }

    public void clearItem() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mUsers != null)
            return mUsers.size();
        else return 0;
    }
}