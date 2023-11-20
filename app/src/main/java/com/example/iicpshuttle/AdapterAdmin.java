package com.example.iicpshuttle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.MyViewHolder> {
    Context context;
    ArrayList<ScheduleAdmin> list;


    public AdapterAdmin(Context context, ArrayList<ScheduleAdmin> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_schedule_admin,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ScheduleAdmin scheduleAdmin = list.get(position);
        holder.date.setText(scheduleAdmin.getDate());
        holder.departure.setText(scheduleAdmin.getDeparture());
        holder.status.setText(scheduleAdmin.getStatus());
        holder.time.setText(scheduleAdmin.getTime());

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, departure, status, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.Date);
            departure = itemView.findViewById((R.id.Departure));
            status = itemView.findViewById((R.id.Status));
            time = itemView.findViewById((R.id.Time));
        }
    }
}
