package com.assesortron.walkthroughnavigator;

import android.app.Activity;
import android.app.Fragment;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by otf on 6/13/15.
 */
public class Navigator extends Fragment {

    private ParentListener parentListener;
    private DisplayFragment displayFragment;
    private DisplayFragment previewFragment;
    private List<DisplayObject> objList;
    private Spinner axisOne, axisTwo, axisThree;
    private Space fragmentOverlay;
    private List<List<List<DisplayObject>>> axisMatrix;

    private Comparator<DisplayObject> axisOneComparator = new Comparator<DisplayObject>() {
        @Override
        public int compare(DisplayObject lhs, DisplayObject rhs) {
            return lhs.getAxis1Value().compareTo(rhs.getAxis1Value());
        }
    };
    private Comparator<DisplayObject> axisTwoComparator = new Comparator<DisplayObject>() {
        @Override
        public int compare(DisplayObject lhs, DisplayObject rhs) {
            return lhs.getAxis2Value().compareTo(rhs.getAxis2Value());
        }
    };
    private Comparator<DisplayObject> axisThreeComparator = new Comparator<DisplayObject>() {
        @Override
        public int compare(DisplayObject lhs, DisplayObject rhs) {
            return lhs.getAxis3Value().compareTo(rhs.getAxis3Value());
        }
    };




    public Navigator newInstance() {
        Navigator n = new Navigator();
        return n;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigator, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            parentListener = (ParentListener)activity;
        }
        catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must" +
                    "implement ParentListener");
        }
    }

    public void setFields(DisplayFragment fragment, List<DisplayObject> obj) {
        this.displayFragment = fragment;
        this.objList = obj;

        setVariables();
        setListeners();
    }

    private void setVariables() {
        fragmentOverlay = (Space)getView().findViewById(R.id.navigator_space);
         previewFragmentSpace = ()getView().findViewById(R.id.navigator_preview_fragment);
        workingFragmentSpace = getView().findViewById(R.id.navigator_work_fragment);

        axisOne = (Spinner)getView().findViewById(R.id.navigator_axis_one);
        axisTwo = (Spinner)getView().findViewById(R.id.navigator_axis_two);
        axisThree = (Spinner) getView().findViewById(R.id.navigator_axis_three);

        Set<Comparable> axis1Values = new LinkedHashSet<>();
        Set<Comparable> axis2Values = new LinkedHashSet<>();
        Set<Comparable> axis3Values = new LinkedHashSet<>();


        for (DisplayObject obj: objList) {
            axis1Values.add(obj.getAxis1Value());
            axis2Values.add(obj.getAxis2Value());
            axis3Values.add(obj.getAxis3Value());
        }

        List<Comparable> axis1List = new ArrayList<>(axis1Values);
        List<Comparable> axis2List = new ArrayList<>(axis2Values);
        List<Comparable> axis3List = new ArrayList<>(axis3Values);

        Collections.sort(axis1List); Collections.sort(axis2List);Collections.sort(axis3List);

        /* now we have sorted, unique lists of the 3 axis's
         */



        ArrayAdapter<Comparable> a1 = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, axis1List);
        ArrayAdapter<Comparable> a2 = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, axis2List);
        ArrayAdapter<Comparable> a3 = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, axis3List);



        a1.insert(objList.get(0).axis1Name(), 0);
        a2.insert(objList.get(0).axis2Name(), 0);
        a3.insert(objList.get(0).axis3Name(), 0);

        axisOne.setAdapter(a1);
        axisTwo.setAdapter(a2);
        axisThree.setAdapter(a3);
    }

    private void setListeners() {
        //TODO
        fragmentOverlay.setOnClickListener();
        fragmentOverlay.setOnLongClickListener();
        GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        }
        GestureDetector gd = new GestureDetector(getActivity(), gestureListener)

                //TODO
        // CONTINUE!!
    }

    private void buildAxisMatrix(List<Comparable> a1, List<Comparable> a2,
                                 List<Comparable> a3, List<DisplayObject> objs) {
        axisMatrix = new ArrayList<>();
        for (int i = 0; i < a1.size(); i++) {
            axisMatrix.add(new ArrayList<List<DisplayObject>>());
            for (int j = 0; j < a2.size(); j++) {
                axisMatrix.get(i).add(new ArrayList<DisplayObject>());
            }
        }
        for (DisplayObject obj: objs) {
            axisMatrix.get(a1.indexOf(obj.getAxis1Value())).get(a2.indexOf(obj.getAxis2Value())).add(obj);
        }
        for (int i = 0; i < a1.size(); i++) {
            for (int j = 0; j < a2.size(); j++) {
                Collections.sort(axisMatrix.get(i).get(j), axisThreeComparator);
            }
        }
    }

    private List<DisplayObject> orderBy (Comparator<DisplayObject> comparator) {
        List<DisplayObject> li = new ArrayList<>(objList);
        Collections.sort(li, comparator);
        return li;
    }




    public interface ParentListener<T> {

    }

    public abstract class DisplayFragment extends Fragment {
        abstract View getFragmentView(int layout, DisplayObject obj);
        abstract DisplayObject updateObject();
    }

    public interface DisplayObject {

        /*
         * get the number of columns this should be organizable by
         */
        public String axis1Name();
        public String axis2Name();
        public String axis3Name();

        public Comparable getAxis1Value();
        public Comparable getAxis2Value();
        public Comparable getAxis3Value();

    }

}
