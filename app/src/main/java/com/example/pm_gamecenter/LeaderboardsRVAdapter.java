package com.example.pm_gamecenter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderboardsRVAdapter extends RecyclerView.Adapter<LeaderboardsRVAdapter.LeaderboardViewHolder> {

    private Context context;
    private ArrayList<LeaderboardsCard> leaderboardCards;

    // Constructor to receive context and the data
    public LeaderboardsRVAdapter(Context context, ArrayList<LeaderboardsCard> leaderboardCards) {
        this.context = context;
        this.leaderboardCards = leaderboardCards;
    }
    @NonNull
    @Override
    public LeaderboardsRVAdapter.LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_leadboard, parent, false);

        return new LeaderboardsRVAdapter.LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardsRVAdapter.LeaderboardViewHolder holder, int position) {
        //holder.cardProfilePicture.setImageURI(Uri.parse(leaderboardCards.get(position).getProfilePicturePath()));
        holder.cardProfilePicture.setImageResource(R.drawable.bmp_avatar_default_s);
        holder.cardUsername.setText(leaderboardCards.get(position).getUsername());
        holder.cardScore.setText(String.valueOf(leaderboardCards.get(position).getScore()));
    }

    @Override
    public int getItemCount() {
        return leaderboardCards.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        ImageView cardProfilePicture;
        TextView cardUsername, cardScore;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);

            cardProfilePicture = itemView.findViewById(R.id.card_picture);
            cardUsername = itemView.findViewById(R.id.card_username);
            cardScore = itemView.findViewById(R.id.card_score);

        }
    }
}
