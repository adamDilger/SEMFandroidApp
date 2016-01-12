package com.semfapp.adamdilger.semf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by adamdilger on 12/01/2016.
 */
public class IncidentReportActivity extends AppCompatActivity {

    private final int TOTAL_PAGES = 5;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Incedent Report");
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    private class ScreenSlidePagerAdapter extends PagerAdapterTemplate {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentPage(R.layout.fragment_incident_1);
                case 1:
                    return new FragmentPageText(R.layout.fragment_incident_2,R.string.incident_string1);
                case 2:
                    return new FragmentPageText(R.layout.fragment_incident_2,R.string.incident_html2);
                case 3:
                    return new FragmentPageText(R.layout.fragment_incident_2,R.string.incident_string3);
                case 4:
                    return new FragmentPageText(R.layout.fragment_incident_3,R.string.incident_html4);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TOTAL_PAGES;
        }
    }

    private class FragmentPage extends Fragment {
        int layout;

        public FragmentPage(int layout) {
            this.layout = layout;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(layout, null);
        }
    }

    private class FragmentPageText extends FragmentPage {

        int string;

        public FragmentPageText(int layout,int string) {
            super(layout);

            this.string = string;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(layout, null);

//            TextView textView = (TextView)v.findViewById(R.id.content_text);
//            textView.setText(string);

            WebView webView = (WebView)v.findViewById(R.id.webView);
            webView.loadData(getText(string).toString(), "text/html", "utf-8");

            return v;
        }
    }
}
