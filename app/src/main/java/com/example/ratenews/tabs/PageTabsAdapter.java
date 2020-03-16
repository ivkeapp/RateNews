package com.example.ratenews.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ratenews.fragments.BlicFragment;
import com.example.ratenews.fragments.DanasFragment;
import com.example.ratenews.fragments.InformerFragment;
import com.example.ratenews.fragments.B92Fragment;
import com.example.ratenews.fragments.LieFragment;
import com.example.ratenews.fragments.N1Fragment;
import com.example.ratenews.fragments.TrueFragment;

public class PageTabsAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TrueFragment();
            case 1:
                return new LieFragment();
            case 2:
                return new BlicFragment();
            case 3:
                return new N1Fragment();
            case 4:
                return new InformerFragment();
            case 5:
                return new B92Fragment();
            case 6:
                return new DanasFragment();
        }
        return null;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Istinito";
            case 1: return "Lažno";
            case 2: return "Blic";
            case 3: return "N1";
            case 4: return "Informer";
            case 5: return "B92";
            case 6: return "Danas";
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 7;
    }


}