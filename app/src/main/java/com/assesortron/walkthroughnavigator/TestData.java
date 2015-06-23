package com.assesortron.walkthroughnavigator;

import com.assesortron.walkthroughnavigator.Navigator.DisplayObject;

/**
 * Created by otf on 6/23/15.
 */
public class TestData implements DisplayObject {
    private int a1;
    private String a2;
    private String a3;
    private boolean complete;

    public TestData(int a1, String a2, String a3, boolean complete) {
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.complete = complete;
    }

    @Override
    public String axis1Name() {
        return "floor";
    }

    @Override
    public String axis2Name() {
        return "room";
    }

    @Override
    public String axis3Name() {
        return "trade";
    }

    @Override
    public Comparable getAxis1Value() {
        return a1;
    }

    @Override
    public Comparable getAxis2Value() {
        return a2;
    }

    @Override
    public Comparable getAxis3Value() {
        return a3;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    public String displayMessage() {
        return "view " + a3 + " for " + axis2Name() + " " +a2 + " " + axis1Name() + " " + a1;
    }
}
