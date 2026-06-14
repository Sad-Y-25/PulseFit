package com.example.pulsefit.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulsefit.R;
import com.example.pulsefit.models.LeaderboardUser;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<LeaderboardUser> userList;

    public LeaderboardAdapter(List<LeaderboardUser> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderboardUser user = userList.get(position);
        
        holder.tvUserName.setText(user.getName());
        holder.tvCalories.setText(String.valueOf(user.getCaloriesBurned()));
        
        int rank = position + 1;
        
        if (rank == 1) {
            holder.tvRank.setVisibility(View.GONE);
            holder.ivRankBadge.setVisibility(View.VISIBLE);
            holder.ivRankBadge.setColorFilter(Color.parseColor("#FFD700")); // Gold
            holder.cvLeaderboardItem.setStrokeColor(Color.parseColor("#FFD700"));
        } else if (rank == 2) {
            holder.tvRank.setVisibility(View.GONE);
            holder.ivRankBadge.setVisibility(View.VISIBLE);
            holder.ivRankBadge.setColorFilter(Color.parseColor("#C0C0C0")); // Silver
            holder.cvLeaderboardItem.setStrokeColor(Color.parseColor("#C0C0C0"));
        } else if (rank == 3) {
            holder.tvRank.setVisibility(View.GONE);
            holder.ivRankBadge.setVisibility(View.VISIBLE);
            holder.ivRankBadge.setColorFilter(Color.parseColor("#CD7F32")); // Bronze
            holder.cvLeaderboardItem.setStrokeColor(Color.parseColor("#CD7F32"));
        } else {
            holder.tvRank.setVisibility(View.VISIBLE);
            holder.ivRankBadge.setVisibility(View.GONE);
            holder.tvRank.setText(String.valueOf(rank));
            holder.cvLeaderboardItem.setStrokeColor(Color.parseColor("#40FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvUserName, tvCalories;
        ImageView ivRankBadge;
        MaterialCardView cvLeaderboardItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvCalories = itemView.findViewById(R.id.tvCalories);
            ivRankBadge = itemView.findViewById(R.id.ivRankBadge);
            cvLeaderboardItem = itemView.findViewById(R.id.cvLeaderboardItem);
        }
    }
}
