package com.example.moneyprocessor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneyprocessor.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String data1[], data2[], data3[], data4[];
    Context context;

    public MyAdapter(Context ct, String s1[], String s2[], String s3[], String s4[]) {
        context = ct;
        data1 = s1;
        data2 = s2;
        data3 = s3;
        data4 = s4;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_movimentacao, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textAdapterTitulo.setText(data1[position]);
        holder.textAdapterValor.setText(data2[position]);
        holder.textAdapterData.setText(data3[position]);
        if(data4[position].equals("d")) {
            holder.textAdapterValor.setTextColor(Color.parseColor("#567a0d"));
        }else {
            holder.textAdapterValor.setTextColor(Color.parseColor("#b75301"));
        }

    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textAdapterTitulo, textAdapterValor, textAdapterData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textAdapterTitulo = itemView.findViewById(R.id.textAdapterTitulo);
            textAdapterValor = itemView.findViewById(R.id.textAdapterValor);
            textAdapterData = itemView.findViewById(R.id.textAdapterData);

        }

    }

}
