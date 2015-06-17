package com.assesortron.walkthroughnavigator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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

    private DisplayFragment.ParentListener parentListener;
    private DisplayFragment<DisplayObject> previewFragment;
    private DisplayFragment<DisplayObject> displayFragment;
    private List<DisplayFragment<DisplayObject>> previewFragments = new ArrayList<>();
    private List<DisplayFragment<DisplayObject>> displayFragments = new ArrayList<>();
    private ViewFlipper workingFragmentSpace;
    private ViewFlipper previewFragmentSpace;
    private Spinner axisOne, axisTwo, axisThree;
    private RadioButton completeFilter, toDoFilter;
    private Button allFilter;
    private Space fragmentOverlay;
    private AxisMatrix axisMatrix;

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
            if (axisMatrix != null && !axisMatrix.getFirst().isEmpty()) {
                setFields();
            }
        }
    }

    public void setPreviewFragments() throws IllegalAccessException, java.lang.InstantiationException {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (previewFragments == null || previewFragments.isEmpty()) {
            for (int i = 0; i < NUM_PREVIEW_FRAGS; i++) {
                DisplayFragment<DisplayObject> df = previewFragment.newInstance();
                df.setObjects(axisMatrix.getFirst(), this);
                ft.add(df, previewTag(i));
            }
            ft.commit();
//            for (int i = 0; i < NUM_PREVIEW_FRAGS; i++) {
//                previewFragments.add((DisplayFragment<DisplayObject>) getFragmentManager().findFragmentByTag(previewTag(i)));
//            }
        }
    }

    private String previewTag(int i) {
        return "preview" + i;
    }


//        if (getView() != null && getView().findViewById(R.id.navigator_preview_fragment) != null) {
//            getFragmentManager().beginTransaction().add(R.id.navigator_preview_fragment, previewFragments.get(0));
//        }
//
//    }

//    public void setDisplayFragments() throws IllegalAccessException, java.lang.InstantiationException {
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        if (displayFragments == null || displayFragments.isEmpty()) {
//            for (int i = 0; i < NUM_DISPLAY_FRAGS; i++) {
//                DisplayFragment<DisplayObject> df = (displayFragment.newInstance());
//                df.setObjects(axisMatrix.getFirst(), this);
//                ft.add(df, "display" +i);
//            }
//            ft.commit();
//            for (int i = 0; i < NUM_DISPLAY_FRAGS; i++) {{
//                displayFragments.add((DisplayFragment<DisplayObject>)getFragmentManager().findFragmentByTag("display" + i));
//            }}
//        }
//
//
//        if (getView() != null && getView().findViewById(R.id.navigator_work_fragment) != null) {
//            getFragmentManager().beginTransaction().add(R.id.navigator_work_fragment, displayFragments.get(0));
//        }
//    }

    public void setUp(List<DisplayObject> obj,
                      DisplayFragment<DisplayObject> previewFragment,
                      DisplayFragment<DisplayObject> displayFragment) {
        this.previewFragment = previewFragment;
        this.displayFragment = displayFragment;
        axisMatrix = new AxisMatrix(obj);
        Log.i("Navigator", "SetUp");

        if(getView() != null) {
            setVariables();
            setListeners();
            setFields();
        }
    }

    private void setVariables() {
        Log.i("Navigator", "setVariables");
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
        Log.i("Navigator", "setFields");
        ArrayAdapter<Comparable> a1 = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, axisMatrix.getAxisOne());
        ArrayAdapter<Comparable> a2 = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, axisMatrix.getAxisTwo());
        ArrayAdapter<Comparable> a3 = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, axisMatrix.getAxisThree());


        a1.insert(axisMatrix.getFirst().get(0).axis1Name(), 0);
        a2.insert(axisMatrix.getFirst().get(0).axis2Name(), 0);
        a3.insert(axisMatrix.getFirst().get(0).axis3Name(), 0);

        axisOne.setAdapter(a1);
        axisTwo.setAdapter(a2);
        axisThree.setAdapter(a3);

//        previewFragment.setObjects(axisMatrix.getFirst(), this);
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.add(previewFragment, "first");
//        ft.commit();

        try {
            setPreviewFragments();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }

    }

    private void setListeners() {
        allFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeFilter.setSelected(false);
                toDoFilter.setSelected(false);
            }
        });
        completeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allFilter.setSelected(false);
                toDoFilter.setSelected(false);
            }
        });
        toDoFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allFilter.setSelected(false);
                completeFilter.setSelected(false);
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
                Log.i("touch", "");
                return gd.onTouchEvent(event);
            }
        });
    }

//    private List<DisplayObject> orderBy (Comparator<DisplayObject> comparator) {
//        List<DisplayObject> li = new ArrayList<>(objList);
//        Collections.sort(li, comparator);
//        return li;
//    }

    private int viewIndex = 0;

//    private void displayCurrentData() {
//        List<DisplayObject> data = axisMatrix.getCurrentData();
//        DisplayFragment<DisplayObject> df = (DisplayFragment<DisplayObject>)getFragmentManager().findFragmentByTag(previewTag(viewIndex++ % 3));
//        df.setObjects(data, this);
//    }

    private void displayNextFirstAxis() {
        List<DisplayObject> data = axisMatrix.getNextFirstAxis();
        previewFragmentSpace.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_bottom));
        previewFragmentSpace.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_out_top));
        DisplayFragment<DisplayObject> df = (DisplayFragment<DisplayObject>)getFragmentManager().findFragmentByTag(previewTag(viewIndex++ % 3));
        df.setObjects(data, this);
//        previewFragmentSpace.setOutAnimation(getActivity(),android.R.anim.fade_out);
        Toast.makeText(getActivity(),"display next floor",Toast.LENGTH_SHORT).show();
        //TODO
    }

    private void displayPreviousFirstAxis() {
        List<DisplayObject> data = axisMatrix.getPreviousFirstAxis();
        previewFragmentSpace.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_top));
        previewFragmentSpace.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_out_bottom));
        DisplayFragment<DisplayObject> df = (DisplayFragment<DisplayObject>)getFragmentManager().findFragmentByTag(previewTag(viewIndex++%3));
        df.setObjects(data, this);
//        previewFragments.get(viewIndex++%3).setObjects(data, this);
  //      previewFragmentSpace.animate();
        Toast.makeText(getActivity(),"display previous floor",Toast.LENGTH_SHORT).show();
        //TODO
    }

    private void displayNextSecondAxis() {
        List<DisplayObject> data = axisMatrix.getNextSecondAxis();
        previewFragmentSpace.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_in));
        previewFragmentSpace.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.left_out));
        DisplayFragment<DisplayObject> df = (DisplayFragment<DisplayObject>)getFragmentManager().findFragmentByTag(previewTag(viewIndex++ % 3));
        df.setObjects(data, this);
        Toast.makeText(getActivity(),"display next area",Toast.LENGTH_SHORT).show();
        //TODO
    }

    private void displayPreviousSecondAxis() {
        List<DisplayObject> data = axisMatrix.getPreviousSecondAxis();
        previewFragmentSpace.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.left_in));
        previewFragmentSpace.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_out));
        DisplayFragment<DisplayObject> df = (DisplayFragment<DisplayObject>)getFragmentManager().findFragmentByTag(previewTag(viewIndex++ % 3));
        df.setObjects(data, this);        Toast.makeText(getActivity(),"display previous area",Toast.LENGTH_SHORT).show();
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
        if (previewFragmentSpace.getChildCount() == 0) {
            previewFragmentSpace.addView(view);
        } else {
            Log.i("View added","");
            previewFragmentSpace.addView(view, 1);
            previewFragmentSpace.showNext();
            previewFragmentSpace.removeViewAt(0);
        }
    }

    @Override
    public void display(DisplayObject obj) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        Toast.makeText(getActivity(), "Object Displaying!", Toast.LENGTH_SHORT).show();
    }



    public class AxisMatrix {
        private List<List<List<DisplayObject>>> axisMatrix;

        private List<Comparable> axis1;
        private List<Comparable> axis2;
        private List<Comparable> axis3;

        private int axisOneIndex = 0;
        private int axisTwoIndex = 0;


        public AxisMatrix (List<DisplayObject> objs) {
            Log.i("Axis Matrix constructor", objs.size() + " items");
            getAxisSets(objs);
            buildAxisMatrix(objs);
        }

        private void buildAxisMatrix(List<DisplayObject> objs) {
            axisMatrix = new ArrayList<>();
            for (int i = 0; i < axis1.size(); i++) {
                axisMatrix.add(new ArrayList<List<DisplayObject>>());
                for (int j = 0; j < axis2.size(); j++) {
                    axisMatrix.get(i).add(new ArrayList<DisplayObject>());
                }
            }
            for (DisplayObject obj : objs) {
                axisMatrix.get(axis1.indexOf(obj.getAxis1Value())).get(axis2.indexOf(obj.getAxis2Value())).add(obj);
            }
            for (int i = 0; i < axis1.size(); i++) {
                for (int j = 0; j < axis2.size(); j++) {
                    Collections.sort(axisMatrix.get(i).get(j), axisThreeComparator);
                }
            }
        }

        private void getAxisSets(List<DisplayObject> objList) {
            Set<Comparable> axis1Values = new LinkedHashSet<>();
            Set<Comparable> axis2Values = new LinkedHashSet<>();
            Set<Comparable> axis3Values = new LinkedHashSet<>();

            for (DisplayObject obj : objList) {
                axis1Values.add(obj.getAxis1Value());
                axis2Values.add(obj.getAxis2Value());
                axis3Values.add(obj.getAxis3Value());
            }

            axis1 = new ArrayList<>(axis1Values);
            axis2 = new ArrayList<>(axis2Values);
            axis3 = new ArrayList<>(axis3Values);

            Collections.sort(axis1);
            Collections.sort(axis2);
            Collections.sort(axis3);
        }

        /***
         * returns the size of the first axis
         * @return the size of the first axis
         */
        public int getFirstAxisCount() {
            return axis1.size();
        }

        /**
         * returns the size of the second axis for the CURRENT first axis
         * @return the size of the second axis
         */
        public int getSecondAxisCount() {
            return axisMatrix.get(axisOneIndex).size();
        }

        public List<DisplayObject> getFirst() {
            return axisMatrix.get(0).get(0);
        }

        public List<DisplayObject>getNextFirstAxis() {
            if (axisOneIndex >= axis1.size()) {
                axisOneIndex = 0;
            } else {
                axisOneIndex++;
            }
            return get(axisOneIndex, axisTwoIndex);
        }

        public List<DisplayObject> getPreviousFirstAxis() {
            if (axisOneIndex <= 0) {
                axisOneIndex = (axis1.size() - 2);
            } else {
                axisOneIndex--;
            }
            return get(axisOneIndex, axisTwoIndex);
        }

        public List<DisplayObject> getNextSecondAxis() {
            if (axisTwoIndex >= axisMatrix.get(axisOneIndex).size() - 1) {
                axisTwoIndex = 0;
            } else {
                axisTwoIndex++;
            }
            return get(axisOneIndex, axisTwoIndex);
        }

        public List<DisplayObject> getPreviousSecondAxis() {
            if (axisTwoIndex <= 0) {
                axisTwoIndex = axisMatrix.get(axisOneIndex).size() - 1;
            } else {
                axisTwoIndex--;
            }
            return get(axisOneIndex, axisTwoIndex);
        }

        private List<DisplayObject> get(int axis1Loc, int axis2Loc) {
            return axisMatrix.get(axis1Loc).get(axis2Loc);
        }

        public List<Comparable> getAxisOne() {
            return axis1;
        }
        public List<Comparable> getAxisTwo() {
            return axis2;
        }
        public List<Comparable> getAxisThree() {
            return axis3;
        }
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

        public boolean isComplete();

    }

}
