package com.assesortron.walkthroughnavigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.assesortron.walkthroughnavigator.Navigator.*;


public class PreviewFragment extends DisplayFragment<DisplayObject> {
    Context context;
    TextView axisOneLabel, axisTwoLabel, axisThreeLabel, axisOneVal, axisTwoVal;
    ListView axisThreeList;
    List<DisplayObject> objs;
    ParentListener parentListener;

    public PreviewFragment() {
        Log.i("PreviewFragment","constructor");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("PreviewView", "OnCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("PreviewView", "OnCreateView");
        return inflater.inflate(R.layout.fragment_preview, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstances) {
        super.onActivityCreated(savedInstances);
        Log.i("PreviewView", "OnActivityCreated");
        if (getView() != null) {
            setVariables();
            if (objs != null) {
                setFields();
                parentListener.viewCreated(getView());
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setVariables() {
        axisOneVal = (TextView) getView().findViewById(R.id.preview_axis_one_val);
        axisTwoVal = (TextView) getView().findViewById(R.id.preview_axis_two_val);
        axisThreeList = (ListView) getView().findViewById(R.id.preview_axis_three_list);

        axisOneLabel = (TextView) getView().findViewById(R.id.preview_axis_one_label);
        axisTwoLabel = (TextView) getView().findViewById(R.id.preview_axis_two_label);
        axisThreeLabel = (TextView) getView().findViewById(R.id.preview_axis_three_label);
    }

    private void setFields() {
        axisOneLabel.setText(objs.get(0).axis1Name());
        axisTwoLabel.setText(objs.get(0).axis2Name());
        axisThreeLabel.setText(objs.get(0).axis3Name().toUpperCase());

        axisOneVal.setText(objs.get(0).getAxis1Value().toString());
        axisTwoVal.setText(objs.get(0).getAxis2Value().toString());

        TradeListAdapter tla = new TradeListAdapter(getActivity(), parentListener, objs);
        axisThreeList.setAdapter(tla);
    }

    @Override
    public void setObjects(List<DisplayObject> objs, ParentListener pl) {
        this.objs = objs;
        this.parentListener = pl;
        if (getView() != null) {
            setFields();
            pl.viewCreated(getView());
        }
    }

    @Override
    public DisplayObject updateObject() {
        return null;
    }

    @Override
    DisplayFragment<DisplayObject> newInstance() {
        return new PreviewFragment();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
