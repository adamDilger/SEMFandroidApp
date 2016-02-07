package com.semfapp.adamdilger.semf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by adamdilger on 12/01/2016.
 */
public class IncidentReportActivity extends AbstractTabLayoutFragment {

    private final int TOTAL_PAGES = 5;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    String setToolbarTitle() {
        return "Incedent Report";
    }

    @Override
    void createPdf() {}

    @Override
    Fragment getFragmentAtIndex(int index) {
        switch (index) {
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
    int getFragmentCount() {
        return TOTAL_PAGES;
    }

    class FragmentPage extends Fragment {
        int layout;

        public FragmentPage(int layout) {
            this.layout = layout;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(layout, null);
        }
    }

    class FragmentPageText extends FragmentPage {

        int string;

        public FragmentPageText(int layout,int string) {
            super(layout);

            this.string = string;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(layout, null);

            WebView webView = (WebView)v.findViewById(R.id.webView);
            webView.loadData(getText(string).toString(), "text/html", "utf-8");

            return v;
        }
    }
}
