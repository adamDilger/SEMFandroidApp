/*

Copyright (C) 2015  Adam Dilger

This program is free software: you can redistribute it and/or modify it under the terms of the GNU
    Affero General Public License as published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version. 

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
    without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License along with this program.
    If not, see <http://www.gnu.org/licenses/>.

 */

package com.semfapp.adamdilger.semf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.semfapp.adamdilger.semf.Take5Data.CheckBoxData;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by adamdilger on 23/11/2015.
 */
public class Take5RecyclerFragment extends Fragment {

    public static final String ARRAY_IDENTIFIER_CODE = "arrayidentifiercode";
    public static final int SECTION_ONE_CODE = 1;
    public static final int SECTION_TWO_CODE = 2;
    public static final int SECTION_THREE_CODE = 3;

    private RecyclerView recyclerView;
    private Take5CheckListAdapter adapter;

    private String title;
    private Take5Data data;
    private ArrayList<CheckBoxData> array;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = Take5Data.get(getContext());
        int arrayIdentifier = getArguments().getInt(ARRAY_IDENTIFIER_CODE);

        switch (arrayIdentifier) {
            case SECTION_ONE_CODE:
                array = data.getSectionOneCheckBoxs();
                title = "Stop, step back and think";
                break;
            case SECTION_TWO_CODE:
                array = data.getSectionTwoCheckBoxs();
                title = "Identify the hazard(s)";
                break;
            case SECTION_THREE_CODE:
                array = data.getSectionThreeCheckBoxs();
                break;
            default:
                array = new ArrayList<>();
        }

        adapter = new Take5CheckListAdapter(getContext(), array);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take5_empty_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());

        RelativeLayout headerView = (RelativeLayout)inflater.inflate(R.layout.take5_recycler_header, null);
        TextView textView = (TextView) headerView.findViewById(R.id.header_title);
        textView.setText(title);

        recyclerView.addItemDecoration(new HeaderDecoration(headerView,false,0f,1));

        return view;
    }

    public class HeaderDecoration extends RecyclerView.ItemDecoration {

        private final View mView;
        private final boolean mHorizontal;
        private final float mParallax;
        private final int mColumns;

        public HeaderDecoration(View view, boolean scrollsHorizontally, float parallax, int columns) {
            mView = view;
            mHorizontal = scrollsHorizontally;
            mParallax = parallax;
            mColumns = columns;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            // layout basically just gets drawn on the reserved space on top of the first view
            mView.layout(parent.getLeft(), 0, parent.getRight(), mView.getMeasuredHeight());

            for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                if (parent.getChildAdapterPosition(view) == 0) {
                    c.save();
                    if (mHorizontal) {
                        c.clipRect(parent.getLeft(), parent.getTop(), view.getLeft(), parent.getBottom());
                        final int width = mView.getMeasuredWidth();
                        final float left = (view.getLeft() - width) * mParallax;
                        c.translate(left, 0);
                        mView.draw(c);
                    } else {
                        c.clipRect(parent.getLeft(), parent.getTop(), parent.getRight(), view.getTop());
                        final int height = mView.getMeasuredHeight();
                        final float top = (view.getTop() - height) * mParallax;
                        c.translate(0, top);
                        mView.draw(c);
                    }
                    c.restore();
                    break;
                }
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) < mColumns) {
                if (mHorizontal) {
                    if (mView.getMeasuredWidth() <= 0) {
                        mView.measure(View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.AT_MOST),
                                View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(), View.MeasureSpec.AT_MOST));
                    }
                    outRect.set(mView.getMeasuredWidth(), 0, 0, 0);
                } else {
                    if (mView.getMeasuredHeight() <= 0) {
                        mView.measure(View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.AT_MOST),
                                View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(), View.MeasureSpec.AT_MOST));
                    }
                    outRect.set(0, mView.getMeasuredHeight(), 0, 0);
                }
            } else {
                outRect.setEmpty();
            }
        }
    }
}