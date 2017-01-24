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

    private final int TOTAL_PAGES = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    String setToolbarTitle() {
        return "Incident Procedure";
    }

    @Override
    void createPdf() {}

    @Override
    Fragment getFragmentAtIndex(int index) {
        switch (index) {
            case 0:
                return FragmentPage.getInstance(R.layout.fragment_incident_1);
            case 1:
                return FragmentPageText.getInstance(R.layout.fragment_incident_2,R.string.incident_html1);
            case 2:
                return FragmentPageText.getInstance(R.layout.fragment_incident_2,R.string.incident_html2);
            case 3:
                return FragmentPageText.getInstance(R.layout.fragment_incident_2,R.string.incident_html3);
            default:
                return null;
        }
    }

    @Override
    int getFragmentCount() {
        return TOTAL_PAGES;
    }

    public static class FragmentPage extends Fragment {
        public static FragmentPage getInstance(int layout) {
            Bundle bundle = new Bundle();
            bundle.putInt("layout", layout);

            FragmentPage fragmentPage = new FragmentPage();
            fragmentPage.setArguments(bundle);
            return fragmentPage;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(
                    getArguments().getInt("layout", R.layout.fragment_incident_1),
                    null);
        }
    }

    public static class FragmentPageText extends Fragment {

        public static FragmentPageText getInstance(int layout, int string) {
            Bundle bundle = new Bundle();
            bundle.putInt("layout", layout);
            bundle.putInt("string", string);

            FragmentPageText fragmentPage = new FragmentPageText();
            fragmentPage.setArguments(bundle);
            return fragmentPage;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            int layout = getArguments().getInt("layout", R.layout.fragment_incident_1);
            int string = getArguments().getInt("string", android.R.string.unknownName);

            View v = inflater.inflate(layout, null);

            WebView webView = (WebView)v.findViewById(R.id.webView);
            webView.loadData(getText(string).toString(), "text/html", "utf-8");

            return v;
        }
    }
}
