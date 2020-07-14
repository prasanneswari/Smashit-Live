package com.vasmash.va_smash.SearchClass.Adapters;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.vasmash.va_smash.SearchClass.SearchFragments.HashtagsFragment;
import com.vasmash.va_smash.SearchClass.SearchFragments.PelopleFragment;
import com.vasmash.va_smash.SearchClass.SearchFragments.SoundsFragment;
import com.vasmash.va_smash.SearchClass.SearchFragments.UsersFragment;
import com.vasmash.va_smash.SearchClass.SearchFragments.VideosFragment;

public class Tablayout_searchtabAdapter extends FragmentStatePagerAdapter {
    //integer to count number of tabs
    int tabCount;
    String searchadaptertext;
    String searchtext;


    //Constructor to the class
    public Tablayout_searchtabAdapter(FragmentManager fm,int tabCount, String searchadaptertext,String searchtext) {
        super(fm);
        //Initializing tab count
      //  Log.d("adaptersearchtext",":::;"+searchtext+"::::"+searchadaptertext);
        this.tabCount=tabCount;
        this.searchadaptertext=searchadaptertext;
        this.searchtext=searchtext;
        notifyDataSetChanged();
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                PelopleFragment tab1 = new PelopleFragment(searchadaptertext,searchtext);
                return tab1;
            case 1:
                UsersFragment tab2 = new UsersFragment(searchadaptertext,searchtext);
                return tab2;
            case 2:
                VideosFragment tab3 = new VideosFragment(searchadaptertext,searchtext);
                return tab3;
            case 3:
                HashtagsFragment tab4 = new HashtagsFragment(searchadaptertext,searchtext);
                return tab4;
            case 4:
                SoundsFragment tab5 = new SoundsFragment(searchadaptertext,searchtext);
                return tab5;

            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

}