package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Team;
import com.inspiregeniussquad.handstogether.appViews.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class TeamsListAdapter extends RecyclerView.Adapter<TeamsListAdapter.TeamsList> {

    private ArrayList<Team> teamArrayList;
    private Context context;
    private TeamClickListener teamClickListener;

    private ImageLoader imageLoader;


    public TeamsListAdapter(Context context, ArrayList<Team> teamArrayList, TeamClickListener teamClickListener){
        this.context = context;
        this.teamArrayList = teamArrayList;
        this.teamClickListener =  teamClickListener;

        imageLoader = ImageLoader.getInstance();
    }


    @NonNull
    @Override
    public TeamsList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teams_list_item, parent, false);
        return new TeamsList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamsList holder, int position) {
        holder.setTeams(teamArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return teamArrayList.size();
    }

    class TeamsList extends RecyclerView.ViewHolder {

        private CardView teamCv;
        private CircularImageView teamLogoIv, logoPlaceHolderIv;
        private TextView teamNameTv, teamMottoTv;

        TeamsList(View itemView) {
            super(itemView);

            teamCv = itemView.findViewById(R.id.team_cv);
            teamLogoIv = itemView.findViewById(R.id.team_logo);
            logoPlaceHolderIv = itemView.findViewById(R.id.team_logo_placeholder);
            teamNameTv = itemView.findViewById(R.id.team_name);
            teamMottoTv = itemView.findViewById(R.id.team_motto);

        }

        void setTeams(final Team teams) {

            File logoImage = DiskCacheUtils.findInCache(teams.getTeamLogoUri(), imageLoader.getDiskCache());
            if(logoImage != null && logoImage.exists()) {
                Picasso.get().load(logoImage).fit().into(teamLogoIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        logoPlaceHolderIv.setVisibility(View.GONE);
                        teamLogoIv.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            } else {
                imageLoader.loadImage(teams.getTeamLogoUri(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        logoPlaceHolderIv.setVisibility(View.GONE);
                        teamLogoIv.setVisibility(View.VISIBLE);
                        Picasso.get().load(imageUri).fit().into(teamLogoIv);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }

            teamNameTv.setText(teams.getTeamName());
            teamMottoTv.setText(teams.getTeamMotto());

            teamCv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    teamClickListener.onTeamClicked(teams);
                }
            });
        }

    }

    public interface TeamClickListener {
        void onTeamClicked(Team team);
    }
}