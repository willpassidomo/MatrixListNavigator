package com.assesortron.walkthroughnavigator;

import android.app.Fragment;
import android.view.View;

import java.util.List;

/**
 * Created by otf on 6/16/15.
 */
public abstract class DisplayFragment<T> extends Fragment {
        abstract void setObjects(List<T> objs, ParentListener pl);
        abstract T updateObject();
        abstract DisplayFragment<T> newInstance();

        public interface ParentListener {
            public void viewCreated(View view);
            public void display(Navigator.DisplayObject obj);
            public void updateObject(Navigator.DisplayObject obj);
            public List<Navigator.DisplayObject> getObjects();
        }
}
