package com.assesortron.walkthroughnavigator;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//import test.persistence.Constants;

/**
 * Created by willpassidomo on 1/15/15.
 */
public class WalkThrough implements Navigator.DisplayObject {
   private String id;
   private Date date;
    private String floor;
    private String area;
    private String Trade;
    private String Progress;
    private String notes;
    private List<Uri> pictures = new ArrayList<Uri>();

    public WalkThrough() {
        this.id = UUID.randomUUID().toString();
        this.date = new Date();
    }

    public WalkThrough(String floor, String area, String trade) {
        this();
        this.floor = floor;
        this.area = area;
        this.Trade = trade;
    }

    public WalkThrough(String nothing) {}

    public static WalkThrough getDBWalkThrough() {
        return new WalkThrough("");
    }

    public void setNote(String note) {
        this.setNotes(note);
    }

    public String getNotes() {
        return notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(String date) {
        this.date = new Date(date);
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setArea(String area) {this.area = area;}

    public String getArea() { return this.area;}

    public String getTrade() {
        return Trade;
    }

    public void setTrade(String trade) {
        Trade = trade;
    }

    public String getProgress() {
        return Progress;
    }

    public void setProgress(String progress) {
        Progress = progress;
    }

    public void addSitePicture(Uri pictureUri) {
        this.pictures.add(pictureUri);
    }

    public void removeSitePicture(Uri pictureUri) {
        this.pictures.remove(pictureUri);
    }

    public void removeSitePicture(int i) {
        this.pictures.remove(i);
    }

    public List<Uri> getPictures() {
        return this.pictures;
    }

    public Uri getPicture(int i) {
        return this.pictures.get(i);
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setSitePictures(List<Uri> pictures) {
        this.pictures = pictures;
    }

    public void addSitePicture(String stringUri) {
        Uri uri = (Uri.parse(stringUri));
        addSitePicture(uri);
    }

    @Override
    public String axis1Name() {
        return "floor";
    }

    @Override
    public String axis2Name() {
        return "area";
    }

    @Override
    public String axis3Name() {
        return "trade";
    }

    @Override
    public Comparable getAxis1Value() {
        return this.floor;
    }

    @Override
    public Comparable getAxis2Value() {
        return this.area;
    }

    @Override
    public Comparable getAxis3Value() {
        return this.Trade;
    }

//    public static abstract class WalkThroughEntry implements BaseColumns {
//
//        public static final String COLUMN_WALK_THROUGH_ID = "walkThroughID";
//        public static final String COLUMN_WALK_THROUGH_DATE = "walkThroughDate";
//        public static final String COLUMN_WALK_THROUGH_FLOOR = "walkThroughFloor";
//        public static final String COLUMN_WALK_THROUGH_TRADE = "walkThroughTrade";
//        public static final String COLUMN_WALK_THROUGH_PROGRESS = "walkThroughProgress";
//        public static final String COLUMN_WALK_THROUGH_NOTE = "walkThroughNote";
//
//        public static final String TABLE_NAME_WALK_THROUGHS = "walkThroughs";
//
//        public static final String TEXT_TYPE = " TEXT";
//        public static final String INTEGER_TYPE = "INTEGER";
//        public static final String REAL_TYPE = "REAL";
//        private static final String COMMA_SEP = ",";
//
//        public static final String CREATE_WALKTHROUGH_TABLE = Constants.createTableString(
//                        TABLE_NAME_WALK_THROUGHS,
//                        COLUMN_WALK_THROUGH_ID + Constants.TEXT_TYPE,
//                        COLUMN_WALK_THROUGH_DATE + Constants.TEXT_TYPE,
//                        COLUMN_WALK_THROUGH_FLOOR + Constants.TEXT_TYPE,
//                        COLUMN_WALK_THROUGH_TRADE + Constants.TEXT_TYPE,
//                        COLUMN_WALK_THROUGH_PROGRESS + Constants.TEXT_TYPE,
//                        COLUMN_WALK_THROUGH_NOTE + Constants.TEXT_TYPE
//        );
//    }
//
//    public static abstract class WalkThroughPictureBridge implements BaseColumns {
//
//        public static final String COLUMN_WALK_THROUGH_PICTURE_URI = "walkThroughPictureURI";
//        public static final String TABLE_NAME_WALK_THROUGH_PICTURE_BRIDGE = "walkThroughPictureBridge";
//
//        public static final String CREATE_WALKTHROUGH_PICTURE_TABLE =
//                "CREATE TABLE " + TABLE_NAME_WALK_THROUGH_PICTURE_BRIDGE + " (" +
//                        WalkThroughPictureBridge._ID + " INTEGER PRIMARY KEY, " +
//                        WalkThroughEntry.COLUMN_WALK_THROUGH_ID + WalkThroughEntry.TEXT_TYPE + WalkThroughEntry.COMMA_SEP +
//                        COLUMN_WALK_THROUGH_PICTURE_URI + WalkThroughEntry.TEXT_TYPE + ")";
//
//    }
}
