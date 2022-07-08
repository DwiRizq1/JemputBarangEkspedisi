package com.example.jemputbarangekspedisi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jemputbarangekspedisi.R;
import com.example.jemputbarangekspedisi.model.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
   private Context context;
   private List<User> list;
   private Dialog dialog;

   public interface Dialog{
      void onClick(int pos);

   }

   public void setDialog(Dialog dialog){
      this.dialog = dialog;

   }

   public UserAdapter(Context context, List<User> list){
      this.context = context;
      this.list = list;
   }

   @NonNull
   @Override
   public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
      return new MyViewHolder(itemView);
   }
   @Override
   public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
      holder.nama.setText(list.get(position).getNama());
      holder.barang.setText(list.get(position).getBarang());
      holder.berat.setText(list.get(position).getBerat());
   }

   @Override
   public int getItemCount(){
      return list.size();
   }

   class MyViewHolder extends RecyclerView.ViewHolder{
      TextView nama, barang, berat;

      public MyViewHolder(@NonNull View itemView){
         super(itemView);
         nama = itemView.findViewById(R.id.nama);
         barang = itemView.findViewById(R.id.barang);
         berat = itemView.findViewById(R.id.berat);
         itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (dialog!=null){
                  dialog.onClick(getLayoutPosition());
               }
            }
         });
      }
   }

}
