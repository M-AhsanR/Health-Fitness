package com.giggly.app.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.giggly.app.Fragments.CategoryFragment;
import com.giggly.app.Fragments.FavouriteFragment;
import com.giggly.app.Fragments.FeedFragment;
import com.giggly.app.Fragments.SettingFragment;

public class PagerAdapterr extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterr(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FeedFragment tab1 = new FeedFragment();
                return tab1;
//            case 1:
//                SearchMainFragment tab2 = new SearchMainFragment();
//                return tab2;
            case 1:
                FavouriteFragment tab3 = new FavouriteFragment();
                return tab3;

            case 2:
                CategoryFragment tab4 = new CategoryFragment();
                return tab4;

            case 3:
                SettingFragment tab5 = new SettingFragment();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
