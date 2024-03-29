package com.instance.ceg.appFragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instance.ceg.R;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appInterfaces.FragmentInterfaceListener;
import com.instance.ceg.appUtils.AppHelper;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends SuperFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<String> fragmentNames;
    private ArrayList<Fragment> fragmentList;

    private NewsFeedFragment newsFeedFragment;
    private CircularFragment circularFragment;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private TabLayout.OnTabSelectedListener tabSelectedListener;

    private FragmentInterfaceListener fragmentRefreshListener;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initializeView(inflater.inflate(R.layout.fragment_home, container, false));
    }

    private View initializeView(View view) {

        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tablayout);
        swipeRefreshLayout = view.findViewById(R.id.home_swipe_refresh_layout);

        //swipe refresh initialization
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent),
                ContextCompat.getColor(getContext(), R.color.colorPrimaryDark),
                ContextCompat.getColor(getContext(), R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataStorage.saveBoolean(Keys.HOME_REFRESH_NEED, true);
                refreshHomeFragment();
            }
        });

        //array list
        fragmentList = new ArrayList<>();
        fragmentNames = new ArrayList<>();

        //creating fragments
        if (newsFeedFragment == null) {
            newsFeedFragment = new NewsFeedFragment();
        }

        if (circularFragment == null) {
            circularFragment = new CircularFragment();
        }

        //adding fragment names to list
        fragmentNames.add(getString(R.string.newsfeed));
        fragmentNames.add(getString(R.string.circular));

        //adding fragments to list
        if (fragmentList.isEmpty()) {
            fragmentList.add(newsFeedFragment);
            fragmentList.add(circularFragment);
        }

        //fragment adapter
        fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return fragmentNames.get(position);
            }
        };

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setting adapters for tab layout fragments
        setAdapterForTabElements();
    }

    private void setAdapterForTabElements() {

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(1);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            switch (i) {
                case 0:
                    TabLayout.Tab tabCall = tabLayout.getTabAt(i);
                    Objects.requireNonNull(tabCall).setIcon(R.drawable.newsfeed_icon);
                    break;
                case 1:
                    TabLayout.Tab tabCall2 = tabLayout.getTabAt(i);
                    Objects.requireNonNull(tabCall2).setIcon(R.drawable.circular_icon);
                    break;
            }
        }
    }

    public void refreshHomeFragment() {
        AppHelper.print("Refreshing home fragment");

        if (swipeRefreshLayout != null) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        }

        if (newsFeedFragment != null) {
            if (newsFeedFragment.isAdded()) {
                newsFeedFragment.refreshNewsFeed();
            } else {
                AppHelper.print("newsFeedFragment is not visible");
            }
        } else {
            AppHelper.print("News feed fragment null");
        }

        if (circularFragment != null) {
            if (circularFragment.isAdded()) {
                circularFragment.refreshCirculars();
            }
        }
    }

    public void setFragmentRefreshListener(FragmentInterfaceListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    public FragmentInterfaceListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (getFragmentRefreshListener() != null) {
                getFragmentRefreshListener().refreshFragments();
            }
        }
    }

}
