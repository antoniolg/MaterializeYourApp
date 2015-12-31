/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.antonioleiva.materializeyourapp;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private List<ViewModel> items;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(List<ViewModel> items) {
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        ViewModel item = items.get(position);
        holder.text.setText(item.getText());
        holder.image.setImageBitmap(null);
        Picasso.with(holder.image.getContext()).load(item.getImage()).into(holder.image);
        holder.itemView.setTag(item);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    @Override public void onClick(final View v) {
        onItemClickListener.onItemClick(v, (ViewModel) v.getTag());
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, ViewModel viewModel);

    }
}
