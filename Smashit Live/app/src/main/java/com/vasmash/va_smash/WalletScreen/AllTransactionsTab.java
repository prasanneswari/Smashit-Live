package com.vasmash.va_smash.WalletScreen;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.vasmash.va_smash.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AllTransactionsTab extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    List<String> addtrndins;
    static public ArrayList<String> both;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions_tab);
        tabLayout= findViewById(R.id.tabLayouttrade);
        viewPager= findViewById(R.id.viewPagertrade);

        addtrndins=new ArrayList<>();

        addtrndins.add("All");
        addtrndins.add("Paid");
        addtrndins.add("Receive");
        both = new ArrayList(addtrndins);
       // both.addAll(catnameL);
       // Log.d("both111",":::"+both);


        for (int k = 0; k < both.size(); k++) {
           // Log.d("lengthhh",":::"+both.size());
            tabLayout.addTab(tabLayout.newTab().setText(both.get(k)));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //this is the tablayout
        final Tablayout_IBHomeAdapter adapter = new Tablayout_IBHomeAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    public class Tablayout_IBHomeAdapter extends FragmentStatePagerAdapter {
        //integer to count number of tabs
        private int tabCount;
        //Constructor to the class
        public Tablayout_IBHomeAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount= tabCount;
        }
        //Overriding method getItem
        @Override
        public Fragment getItem(final int position) {
            //Returning the current tabs
            return AllTransactions.newInstance(position);
        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return tabCount;
        }
    }
    public void finishActivity(View v){
        finish();
    }


}
