package com.inspiregeniussquad.handstogether.appFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SuperFragment extends Fragment {

    protected Gson gson = new Gson();
    private Unbinder unbinder;

    //here fragment is attached with activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //used for initializing fragments
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //view creation and views are inflated here
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(view);
        return view;
    }

    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    //view creation completed for fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //this was invoked after onCreate()
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //this makes the fragment visible
    @Override
    public void onStart() {
        super.onStart();
    }

    //this makes the fragment interactive with user
    @Override
    public void onResume() {
        super.onResume();
    }

    //the fragment is now no longer interactive
    @Override
    public void onPause() {
        super.onPause();
    }

    //the fragment is no longer visible to user
    @Override
    public void onStop() {
        super.onStop();
    }

    //fragment was removed and view cleared
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //removing binding on view destroyed
        unbinder.unbind();
    }

    //fragment resources were cleared
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //it was called when the fragment was no longer attached to the activity
    @Override
    public void onDetach() {
        super.onDetach();
    }

    //for starting activity with single intent values
    protected void goTo(Context from, Class to, boolean close, String key, String data) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            if (key != null && data != null) {
                startActivity(new Intent(from, to).putExtra(key, data));
            }
            if (close) {
                getActivity().finish();
            }
        }

    }

    //for starting activity with two intent values
    protected void goTo(Context from, Class to, boolean close, String key, String data, String key1, String data1) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            if (key != null && data != null && key1 != null && data1 != null) {
                startActivity(new Intent(from, to).putExtra(key, data).putExtra(key1, data1));
            }
            if (close) {
                getActivity().finish();
            }
        }

    }

    //for starting activity without intent values
    protected void goTo(Context from, Class to, boolean close) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            startActivity(new Intent(from, to));
            if (close) {
                getActivity().finish();
            }
        }
    }
}
