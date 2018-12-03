package com.inspiregeniussquad.handstogether.appStorage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity
public class TeamData {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int teamId;

    private String teamName;
    private String teamLogoUrl;
    private String teamMotto;
    private String teamMembersCount;
//    private TeamMembers teamMembers;

    public TeamData() {

    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamLogoUrl() {
        return teamLogoUrl;
    }

    public void setTeamLogoUrl(String teamLogoUrl) {
        this.teamLogoUrl = teamLogoUrl;
    }

    public String getTeamMotto() {
        return teamMotto;
    }

    public void setTeamMotto(String teamMotto) {
        this.teamMotto = teamMotto;
    }

    public String getTeamMembersCount() {
        return teamMembersCount;
    }

    public void setTeamMembersCount(String teamMembersCount) {
        this.teamMembersCount = teamMembersCount;
    }

//    public TeamMembers getTeamMembers() {
//        return teamMembers;
//    }
//
//    public void setTeamMembers(TeamMembers teamMembers) {
//        this.teamMembers = teamMembers;
//    }
}
