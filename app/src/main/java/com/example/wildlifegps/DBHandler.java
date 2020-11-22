package com.example.wildlifegps;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.Calendar;

public class DBHandler extends SQLiteOpenHelper {

        //information of database
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "WildlifeDB.db";
        public static final String TABLE_NAME_USERS = "Users";
        public static final String TABLE_NAME_SIGHTINGS = "Sightings";
        public static final String TABLE_NAME_TAGS = "Tags";
        public static final String TABLE_NAME_SIGHTINGTAGS = "SightingTags";
        public static final String TABLE_NAME_COMMENTS = "Comments";
        public static final String TABLE_NAME_ANIMALS = "Animals";
        public static final String TABLE_NAME_SPECIES = "Species";
        public static final String TABLE_NAME_PETS = "Pets";
        public static final String COLUMN_USERNAME = "Username";
        public static final String COLUMN_PASSWORD = "Password";
        //initialize the database
        public DBHandler(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table Users ("
                    +"username varchar(30) primary key,"
                    +"password varchar(80) not null"
            +")");

            db.execSQL("create table Sightings("
                    +"sighting_id int primary key,"
                    +"location varchar(30) not null,"
                    +"timeStamp varchar(20),"
                    +"title varchar(100),"
                    +"description varchar(280),"
                    +"imgFile varchar(100),"
                    +"flagCount int DEFAULT 0,"
                    +"username varchar(30) not null,"
                    +"constraint FK_UserSighting foreign key (username) references Users(username)"
            +");");

            db.execSQL("create table Tags("
                    +"tag_id int primary key,"
                    +"content varchar(15) not null"
            +")");

            db.execSQL("create table SightingTags("
                    +"sighting_id int,"
                    +"tag_id int,"
                    +"primary key (sighting_id, tag_id),"
                    +"constraint FK_Sighting foreign key (sighting_id)"
                    +"references Sightings(sighting_id),"
                    +"constraint FK_Tag foreign key (tag_id)"
                    +"references Tags(tag_id)"
            +")");

            db.execSQL("create table Comments("
                    +"comment_id int primary key,"
                    +"username varchar(30),"
                    +"sighting_id int,"
                    +"time varchar(20),"
                    +"content varchar (280),"
                    +"constraint FK_CommentSighting foreign key (sighting_id)"
                    +"references Sightings(sighting_id)"
            +")");
        }

        public void addUser(User user){
            SQLiteDatabase db = this.getWritableDatabase();
            String username=user.getUsername();
            String password=user.getPassword();

//            db.execSQL("INSERT into "+TABLE_NAME_USERS+" VALUES ("+username+", "+password+")");

            ContentValues contentValues = new ContentValues();
            contentValues.put("username", username);
            contentValues.put("password", password);

            db.insert(TABLE_NAME_USERS, null, contentValues);

        }

        public void addSighting(Sighting sighting){
            SQLiteDatabase db = this.getWritableDatabase();
            int ID=sighting.getID();
            String title=sighting.getTitle();
            String location=sighting.getLocation();

            Calendar timestamp=sighting.getTimestamp();
            String time = timestamp.get(Calendar.HOUR_OF_DAY) + ":" + timestamp.get(Calendar.MINUTE) + " " + timestamp.get(Calendar.MONTH) +
                    ":" + timestamp.get(Calendar.DAY_OF_MONTH) + ":" + timestamp.get(Calendar.YEAR);
            int flagCount = sighting.getFlagCount();
            String description = sighting.getDescription();
            String filename = sighting.getImageFileName();
            String username = sighting.getOwner().getUsername();

            ContentValues contentValues = new ContentValues();
            contentValues.put("sighting_id", ID);
            contentValues.put("location", location);
            contentValues.put("time", time);
            contentValues.put("title", title);
            contentValues.put("description", description);
            contentValues.put("filename", filename);
            contentValues.put("flagCount", flagCount);
            contentValues.put("username", username);

            db.insert(TABLE_NAME_SIGHTINGS, null, contentValues);

        }

        public void addComment(Comment comment){
            SQLiteDatabase db = this.getWritableDatabase();

            int commentID = comment.getID();
            String username= comment.getUser().getUsername();
            int sightID = comment.getSighting().getID();
            Calendar timestamp = comment.getTimeStamp();
            String time = timestamp.get(Calendar.HOUR_OF_DAY) + ":" + timestamp.get(Calendar.MINUTE) + " " + timestamp.get(Calendar.MONTH) +
                    ":" + timestamp.get(Calendar.DAY_OF_MONTH) + ":" + timestamp.get(Calendar.YEAR);
            String content=comment.getComment();

            ContentValues contentValues = new ContentValues();
            contentValues.put("comment_id", commentID);
            contentValues.put("username", username);
            contentValues.put("sight_id", sightID);
            contentValues.put("time", time);
            contentValues.put("content", content);

            db.insert(TABLE_NAME_COMMENTS, null, contentValues);

        }

        public void addTag(Sighting sighting, String tag){
            SQLiteDatabase db = this.getWritableDatabase();

            //THIS NEEDS TO BE UNIQUE LATER
            int ID = 1;

            ContentValues contentValues = new ContentValues();
            contentValues.put("tag_id", ID);
            contentValues.put("content", tag);

            db.insert(TABLE_NAME_TAGS, null, contentValues);

        }

        public void updateFlagCount(Sighting sighting){
            SQLiteDatabase db = this.getWritableDatabase();

            int ID= sighting.getID();
            int flag = sighting.getFlagCount();

            db.execSQL("UPDATE " + TABLE_NAME_SIGHTINGS + " SET flagCount = " + flag+ " WHERE sighting_id = "+ID);

        }
        public void deleteSighting(Sighting sighting){
            SQLiteDatabase db = this.getWritableDatabase();

            int ID= sighting.getID();

            db.execSQL("DELETE FROM "+ TABLE_NAME_SIGHTINGS+" WHERE sighting_id = "+ ID);

        }
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {}
//        public String loadHandler(String tableName) {
//            String result="";
//            String query = "Select*FROM"+tableName;
//
//            return result;
//        }
//        public void addHandler(User user) {}
//
//        public User findHandler(String username) {
//
//        }
//
//        public boolean updateHandler(String username, String password) {}
}
