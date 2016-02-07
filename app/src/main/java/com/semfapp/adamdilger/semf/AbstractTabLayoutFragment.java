package com.semfapp.adamdilger.semf;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by adamdilger on 7/02/16.
 */
public abstract class AbstractTabLayoutFragment extends AppCompatActivity {
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    abstract String setToolbarTitle();
    abstract void createPdf();

    /**
     * getFragmentAtIndex
     * @param index page number
     * @return Fragment for page Index
     */
    abstract Fragment getFragmentAtIndex(int index);

    /**
     * @return total page count
     */
    abstract int getFragmentCount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(setToolbarTitle());
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pagerAdapter = new PagerAdapterTemplate(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //set tablayout with viewpager
        tabLayout.setupWithViewPager(viewPager);

        // adding functionality to tab and viewpager to manage each other when a page is changed or when a tab is selected
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Setting tabs from adpater
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Submit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createPdf();
                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    public QuestionFragment getQuestionFragment(int questionId) {
        QuestionFragment qf = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(QuestionFragment.QUESTION_TEXT_CODE, questionId);
        qf.setArguments(args);
        return qf;
    }



    class PagerAdapterTemplate extends FragmentPagerAdapter {
        public PagerAdapterTemplate(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle (int position) {
            return "Page " + String.valueOf(position + 1);
        }

        @Override
        public Fragment getItem(int position) {
            return getFragmentAtIndex(position);
        }

        @Override
        public int getCount() {
            return getFragmentCount();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Emailer.EMAILER_REQUEST_CODE
                && resultCode == RESULT_CANCELED) {
            finish();
        }
    }
}
