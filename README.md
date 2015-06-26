# MatrixListNavigator
to set up this Fragment, add the fragment to your activity and call the 
```java
setup(List<DisplayObject> dos, DisplayFragment df);
```
method. When the Parent Activity needs to pull the current state of the objects from the Navigator, it may call the
```java
getObjects();
```

## DisplayObject Interface:
In order to be displayed in MatrixListNavigator, you objects must implement the DisplayObject interface. 

```java 
    public String axis1Name();
    public String axis2Name();
    public String axis3Name();

    public Comparable getAxis1Value();
    public Comparable getAxis2Value();
    public Comparable getAxis3Value();

    public boolean isComplete();
```

## DisplayFragment Interface:
when an object is slected from the list with a "click", the Display fragment provided in setup() is shown and it's
```java
setObject(T obj);
```
is called. When the DisplayFragment is finished manipulating the object, it must call the 
```java
parentListener.updateObject(obj);
```
with the finished object. calling this method will shift the view back to the Navigator



### Notes:
This component was designed to be particularly light weight. The swipe area is a ViewPager, but rather than filling the ViewPager fully with Fragments, I instantiate only 2 or 3 Fragments and continually pass them in or out with new data in order to provide the animation and keep memory usage low
