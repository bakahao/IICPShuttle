package com.example.iicpshuttle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import android.widget.TextView;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DriverStudentListAdapter extends RecyclerView.Adapter<DriverStudentListAdapter.MyViewHolder> {
    Context context;
    ArrayList<StudentListDetails> list;


    public DriverStudentListAdapter(Context context, ArrayList<StudentListDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_show_student_list,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StudentListDetails studentListDetails = list.get(position);

        if (studentListDetails != null) {
            try {
                // Convert Long to String before setting it in the TextView
                String studentIDString = String.valueOf(studentListDetails.getStudentID());
                holder.studentID.setText(studentIDString);
                holder.userName.setText(studentListDetails.getUserName());
            } catch (Exception e) {
                // Log the exception to identify the problematic data
                Log.e("AdapterError", "Error converting studentID to String", e);
            }
        }
    }


    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView studentID, userName, email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            studentID = itemView.findViewById(R.id.studentID);
            userName = itemView.findViewById((R.id.userName));
            email = itemView.findViewById((R.id.email));
        }
    }
}


