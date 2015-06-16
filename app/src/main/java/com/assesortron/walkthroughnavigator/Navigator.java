package com.assesortron.walkthroughnavigator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Space;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by otf on 6/13/15.
 */
public class Navigator extends Fragment implements DisplayFragment.ParentListener {

    public static final int NUM_PREVIEW_FRAGS = 3;
    public static final int NUM_DISPLAY_FRAGS = 3;

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    private int axisOneIndex = 0;
    private int axisTwoIndex = 0;

    private DisplayFragment.ParentListener parentListener;
    private DisplayFragment<DisplayObject> previewFragment;
    private DisplayFragment<DisplayObject> displayFragment;
    private List<DisplayFragment<DisplayObject>> previewFragments = new ArrayList<>();
    private List<DisplayFragment<DisplayObject>> displayFragments = new ArrayList<>();
    private ViewFlipper workingFragmentSpace;
    private ViewFlipper previewFragmentSpace;
    private List<DisplayObject> objList;
    private List<Comparable> axis1List, axis2List, axis3List;
    private Spinner axisOne, axisTwo, axisThree;
    private RadioButton completeFilter, toDoFilter;
    private Button allFilter;
    private Space fragmentOverlay;
    private List<List<List<DisplayObject>>> axisMatrix;

    private GestureDetector touchListener;

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




    public Navigator () {
        Log.i("Navigator", "constructor");
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Navigator", "OnCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Navigator", "OnCreateView");
        return inflater.inflate(R.layout.fragment_navigator, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("Navigator", "OnAttach");
        try {
            //TODO
            //call back for parent activity to get the current values of all of the matrix objects
        }
        catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must" +
                    "implement ParentListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstances) {
        super.onActivityCreated(savedInstances);
        Log.i("Navigator", "OnActivityCreated");
        if (getView() != null) {
            setVariables();
            setListeners();
            if (objList != null && !objList.isEmpty()) {
                setFields();
            }
        }
    }

    public void setPreviewFragments() throws IllegalAccessException, java.lang.InstantiationException {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (previewFragments == null || previewFragments.isEmpty()) {
            for (int i = 0; i < NUM_PREVIEW_FRAGS; i++) {
                DisplayFragment<DisplayObject> df = previewFragment.newInstance();
                df.setObject(objList.get(i), this);
                ft.add(df, i + "");
            }
            ft.commit();
            for (int i = 0; i < NUM_PREVIEW_FRAGS; i++) {
                previewFragments.add((DisplayFragment<DisplayObject>)getFragmentManager().findFragmentByTag(i + ""));
            }
            Log.i("# preview frags",previewFragments.size()+"");
        }

//        if (getView() != null && getView().findViewById(R.id.navigator_preview_fragment) != null) {
//            getFragmentManager().beginTransaction().add(R.id.navigator_preview_fragment, previewFragments.get(0));
//        }

    }

    public void setDisplayFragments() throws IllegalAccessException, java.lang.InstantiationException {
        if (displayFragments == null || displayFragments.isEmpty()) {
            for (int i = 0; i < NUM_DISPLAY_FRAGS; i++) {
                displayFragments.add(displayFragment.newInstance());
            }
        }


        if (getView() != null && getView().findViewById(R.id.navigator_work_fragment) != null) {
            getFragmentManager().beginTransaction().add(R.id.navigator_work_fragment, displayFragments.get(0));
        }
    }

    public void setUp(List<DisplayObject> obj,
                      DisplayFragment<DisplayObject> previewFragment,
                      DisplayFragment<DisplayObject> displayFragment) {
        this.previewFragment = previewFragment;
        this.displayFragment = displayFragment;
        this.objList = obj;
        Log.i("Navigator", "SetUp");

        if(getView() != null) {
            setVariables();
            setListeners();
            setFields();
        }
    }

    private void setVariables() {
        previewFragmentSpace = (ViewFlipper) getView().findViewById(R.id.navigator_preview_fragment);
        workingFragmentSpace = (ViewFlipper) getView().findViewById(R.id.navigator_work_fragment);

        axisOne = (Spinner) getView().findViewById(R.id.navigator_axis_one);
        axisTwo = (Spinner) getView().findViewById(R.id.navigator_axis_two);
        axisThree = (Spinner) getView().findViewById(R.id.navigator_axis_three);

        allFilter = (Button) getView().findViewById(R.id.navigator_all);
        completeFilter = (RadioButton) getView().findViewById(R.id.navigator_complete);
        toDoFilter = (RadioButton) getView().findViewById(R.id.navigator_todo);


    }

    private void setFields() {
        Set<Comparable> axis1Values = new LinkedHashSet<>();
        Set<Comparable> axis2Values = new LinkedHashSet<>();
        Set<Comparable> axis3Values = new LinkedHashSet<>();

        for (DisplayObject obj : objList) {
            axis1Values.add(obj.getAxis1Value());
            axis2Values.add(obj.getAxis2Value());
            axis3Values.add(obj.getAxis3Value());
        }

        axis1List = new ArrayList<>(axis1Values);
        axis2List = new ArrayList<>(axis2Values);
        axis3List = new ArrayList<>(axis3Values);

        Collections.sort(axis1List);
        Collections.sort(axis2List);
        Collections.sort(axis3List);

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

        buildAxisMatrix(axis1List,axis2List,objList);

        try {
            setPreviewFragments();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }

    }

    private void setListeners() {
        //TODO

        //TODO
        //delete
        previewFragmentSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("On Click ", "fired");
            }
        });

        GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                String direction = "none";
                switch (getDirection(velocityX, velocityY)) {
                    case UP:
                        displayNextFirstAxis();
                        break;
                    case DOWN:
                        displayPreviousFirstAxis();
                        break;
                    case LEFT:
                        displayNextSecondAxis();
                        break;
                    case RIGHT:
                        displayPreviousSecondAxis();
                        break;
                    default:
                        Toast.makeText(getActivity(), "fling not handled NAVIGATOR", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                Log.i("Long Press", "gesture detector");
                Toast.makeText(getActivity(), "Long Press (gesture detector)", Toast.LENGTH_SHORT).show();
            }
        };

        final GestureDetector gd = new GestureDetector(getActivity(), gestureListener);

        previewFragmentSpace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("touch","");
                return gd.onTouchEvent(event);
            }
        });
    }

    private void buildAxisMatrix(List<Comparable> a1, List<Comparable> a2,
                                 List<DisplayObject> objs) {
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

    private void displayNextFirstAxis() {
        Toast.makeText(getActivity(),"display next floor",Toast.LENGTH_SHORT).show();
        //TODO
    }

    private void displayPreviousFirstAxis() {
        Toast.makeText(getActivity(),"display previous floor",Toast.LENGTH_SHORT).show();
        //TODO
    }

    private void displayNextSecondAxis() {
        Toast.makeText(getActivity(),"display next area",Toast.LENGTH_SHORT).show();
        //TODO
    }

    private void displayPreviousSecondAxis() {
        Toast.makeText(getActivity(),"display previous area",Toast.LENGTH_SHORT).show();
        //TODO
    }

    private int getDirection(float velocityX, float velocityY) {
        if (velocityX > 0) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                return RIGHT;
            } else {
                if (velocityY < 0) {
                    return UP;
                } else {
                    return DOWN;
                }
            }
        } else {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                return LEFT;
            } else {
                if (velocityY < 0) {
                    return UP;
                } else {
                    return DOWN;
                }
            }
        }
    }

    @Override
    public void viewCreated(View view) {
        previewFragmentSpace.addView(view);
        Log.i("View added","");
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
