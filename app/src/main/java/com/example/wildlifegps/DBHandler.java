package com.example.wildlifegps;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class DBHandler extends SQLiteOpenHelper {

        //For generating IDs
        public static int sightingIDCount=0;
        public static int animalIDCount=0;
        public static int commentIDCount=0;
        public static int petIDCount=0;

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
        public static final String TABLE_NAME_OBSERVERS = "Observers";
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
                    +"longitude decimal(8, 5),"
                    +"latitude decimal(8,5),"
                    +"timeStamp varchar(20),"
                    +"title varchar(100),"
                    +"description varchar(280),"
                    +"imgFile varchar(100),"
                    +"flagCount int DEFAULT 0,"
                    +"username varchar(30) not null,"
                    +"animal_id int,"
                    +"constraint FK_Animal_Sighting foreign key (animal_id) references Animals(animal_id),"
                    +"constraint FK_UserSighting foreign key (username) references Users(username)"
            +");");

        db.execSQL("create table Tags("
                +"content varchar(15) primary key"
                +")");

        db.execSQL("create table SightingTags("
                +"sighting_id int,"
                +"content varchar(15),"
                +"primary key (sighting_id, content),"
                +"constraint FK_Sighting foreign key (sighting_id)"
                +"references Sightings(sighting_id),"
                +"constraint FK_Tag foreign key (content)"
                +"references Tags(content)"
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

        db.execSQL("create table Animals("
                +"animal_id int primary key,"
                +"comm_name varchar(30)"
                +")");

        db.execSQL("create table Species("
                +"science_name varchar(45) primary key,"
                +"animal_id int,"
                +"conserve_status varchar(2),"
                +"diet varchar(200),"
                +"appearence varchar(200),"
                +"constraint FK_SpeciesAnimal foreign key (animal_id)"
                +"references Animals(animal_id)"
                +")");

        db.execSQL("create table Pets("
                +"pet_id int primary key,"
                +"animal_id int,"
                +"pet_name varchar(40),"
                +"lost_or_found int,"
                +"constraint FK_PetAnimal foreign key (animal_id)"
                +"references Animals(animal_id)"
                +")");

        db.execSQL("create table Observers("
                +"username varchar(30),"
                +"animal_id int,"
                +"primary key(username, animal_id),"
                +"constraint FK_UserObserver foreign key(username)"
                +"references Users(username),"
                +"constraint FK_AnimalObserved foreign key(animal_id)"
                +"references Animals(animal_id)"
                +")");
    }

    //ADD
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String username=user.getUsername();
        String password=user.getPassword();

        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);

        db.insert(TABLE_NAME_USERS, null, contentValues);
        db.close();

    }

        public void addSighting(Sighting sighting){
            sightingIDCount++;
            SQLiteDatabase db = this.getWritableDatabase();
            int ID=sighting.getID();
            String title=sighting.getTitle();
            Location location=sighting.getLocation();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            Calendar timestamp=sighting.getTimestamp();
            String time = timestamp.get(Calendar.HOUR_OF_DAY) + ":" + timestamp.get(Calendar.MINUTE) + " " + timestamp.get(Calendar.MONTH) +
                    ":" + timestamp.get(Calendar.DAY_OF_MONTH) + ":" + timestamp.get(Calendar.YEAR);
            int flagCount = sighting.getFlagCount();
            String description = sighting.getDescription();
            String filename=sighting.getImageFileName().toString();
//            byte[] filename = sighting.getImageFileName();
            String username = sighting.getOwner().getUsername();
            int aniID = sighting.getAnimal().getAnimalID();

            ContentValues contentValues = new ContentValues();
            contentValues.put("sighting_id", ID);
            contentValues.put("longitude", longitude);
            contentValues.put("latitude", latitude);
            contentValues.put("timeStamp", time);
            contentValues.put("title", title);
            contentValues.put("description", description);
            contentValues.put("imgFile", filename);
            contentValues.put("flagCount", flagCount);
            contentValues.put("username", username);
            contentValues.put("animal_id", aniID);

            db.insert(TABLE_NAME_SIGHTINGS, null, contentValues);
            db.close();

        }

        public void addAnimal(Animal animal){
            animalIDCount++;
            SQLiteDatabase db = this.getWritableDatabase();

            String commName= animal.getCommonName();
            int id= animal.getAnimalID();

            ContentValues contentValues = new ContentValues();
            contentValues.put("animal_id", id);
            contentValues.put("comm_name", commName);

            db.insert(TABLE_NAME_ANIMALS, null, contentValues);
            db.close();

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
        db.close();

    }

    public void addTag(Sighting sighting, String tag){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean bool= false;
        int sightingID = sighting.getID();

            Cursor cursor = db.rawQuery("SELECT content FROM "+ TABLE_NAME_TAGS + " WHERE content = '" + tag + "'",null);
            bool=cursor.moveToFirst();

        if(!bool){
            ContentValues contentValues = new ContentValues();
            contentValues.put("content", tag);
            db.insert(TABLE_NAME_TAGS, null, contentValues);
        }

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("content", tag);
        contentValues1.put("sighting_id", sightingID);
        db.insert(TABLE_NAME_SIGHTINGTAGS, null, contentValues1);
        cursor.close();
        db.close();
    }

    public void addPet(Pet pet){
            petIDCount++;
        SQLiteDatabase db = this.getWritableDatabase();
        int petID = petIDCount;
        String petName = pet.getPetName();
        String common = pet.getCommonName();
        int animalID= pet.getAnimalID();
        int lostFound = pet.getLostFound();

        ContentValues contentValues = new ContentValues();
        contentValues.put("animal_id", animalID);
        contentValues.put("comm_name", common);
        db.insert(TABLE_NAME_ANIMALS, null, contentValues);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("pet_id", petID);
        contentValues2.put("pet_name", petName);
        contentValues2.put("animal_id", animalID);
        contentValues2.put("lost_or_found", lostFound);
        db.insert(TABLE_NAME_PETS, null, contentValues2);
        db.close();

    }

    //UPDATE
    public void updateFlagCount(Sighting sighting){
        SQLiteDatabase db = this.getWritableDatabase();

        int ID= sighting.getID();
        int flag = sighting.getFlagCount();

        db.execSQL("UPDATE " + TABLE_NAME_SIGHTINGS + " SET flagCount = '" + flag+ "' WHERE sighting_id = '"+ID+"'");
        db.close();

    }

    public void updateSighting(Sighting sighting, String imageFile, String desc, ArrayList<String> tags, String title){
        SQLiteDatabase db = this.getWritableDatabase();
        int sightingID=sighting.getID();

        //add each tag
        for(int i =0; i < tags.size();i++){
            addTag(sighting, tags.get(i));
        }

        db.execSQL("UPDATE " + TABLE_NAME_SIGHTINGS + " SET imgFile = '" + imageFile+ "' WHERE sighting_id = '"+sightingID+"'");
        db.execSQL("UPDATE " + TABLE_NAME_SIGHTINGS + " SET description = '" + desc+ "' WHERE sighting_id = '"+sightingID+"'");
        db.execSQL("UPDATE " + TABLE_NAME_SIGHTINGS + " SET title = '" + title+ "' WHERE sighting_id = '"+sightingID+"'");
        db.close();
    }

    public void updateTime(Sighting sighting){
        //assume the sighting object has updated timestamp
        SQLiteDatabase db = this.getWritableDatabase();

        int sightingID= sighting.getID();
        Calendar timestamp = sighting.getTimestamp();
        String time = timestamp.get(Calendar.HOUR_OF_DAY) + ":" + timestamp.get(Calendar.MINUTE) + " " + timestamp.get(Calendar.MONTH) +
                ":" + timestamp.get(Calendar.DAY_OF_MONTH) + ":" + timestamp.get(Calendar.YEAR);

        db.execSQL("UPDATE " + TABLE_NAME_SIGHTINGS + " SET timeStamp = '" + time+ "' WHERE sighting_id = '"+sightingID+"'");
        db.close();
    }

    //DELETE
    public void deleteSighting(Sighting sighting){
        SQLiteDatabase db = this.getWritableDatabase();

        int ID= sighting.getID();

        db.execSQL("DELETE FROM "+ TABLE_NAME_SIGHTINGS+" WHERE sighting_id = '"+ ID+"'");
        db.close();
    }


    //SEARCH
    //for registering an account check if username is taken
    public boolean searchUsername(String username){
        boolean bool=false;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT username FROM "+ TABLE_NAME_USERS + " WHERE username = '" + username+"'",null);
        bool=cursor.moveToFirst();

        cursor.close();
        return bool;
    }
    //for login credentials
    public boolean searchUser(String username, String password) {
        boolean bool = false;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME_USERS + " WHERE username = '" + username + "' AND password = '" + password +"'",null);
        bool=cursor.moveToFirst();

        //db.close();
        cursor.close();
        db.close();
        return bool;

    }
    public ArrayList<Sighting> searchByTag(String tag){
        ArrayList<Sighting> sightings = new ArrayList<>();
        String time;
        String username;
        Location location = new Location("");
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME_SIGHTINGS +" s " + " LEFT OUTER JOIN " + TABLE_NAME_SIGHTINGTAGS + " st "
                + "ON s.sighting_id = st.sighting_id",null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Sighting temp = new Sighting();

                temp.setID(Integer.parseInt(cursor.getString(0)));
                location.setLongitude(Double.parseDouble(cursor.getString(1)));
                location.setLatitude(Double.parseDouble(cursor.getString(2)));
                //temp.setLocation(cursor.getString(1));
                time=cursor.getString(3);
                //HH:MM MM/DD/YY
                String[] arr = time.replaceAll("[\\:\\/]", " ").split(" ");
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(arr[4]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
                temp.setTimestamp(cal);
                temp.setTitle(cursor.getString(4));
                temp.setDescription(cursor.getString(5));
                temp.setImageFileName(Uri.parse(cursor.getString(6)));
//                //String image
//                byte[] imgArr= cursor.getBlob(cursor.getColumnIndex("imgFile"));
//                Bitmap image = BitmapFactory.decodeByteArray(imgArr, 0, imgArr.length);
//                temp.setImage(image);
                temp.setFlagCount(Integer.parseInt(cursor.getString(7)));
                //temp.getTags();
                username=(cursor.getString(8));
                temp.getOwner().setUsername(username);
                //temp.setAnimal(Integer.parseInt(cursor.getString(9)));
                sightings.add(temp);
            }
        cursor.close();
            db.close();
            return sightings;
        }

    public ArrayList<Sighting> searchByLocation(Location loc){
        ArrayList<Sighting> sightings = new ArrayList<>();
        String time;
        String username;
        Location location = new Location("");
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME_SIGHTINGS + " WHERE longitude BETWEEN " + (loc.getLongitude()-.07143) + " AND "
                + (loc.getLongitude()+.07143) + " AND latitude BETWEEN " + (loc.getLatitude()-.07143) + " AND " + (loc.getLatitude()+.07143), null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Sighting temp = new Sighting();

                temp.setID(Integer.parseInt(cursor.getString(0)));
                location.setLongitude(Double.parseDouble(cursor.getString(1)));
                location.setLatitude(Double.parseDouble(cursor.getString(2)));
                //temp.setLocation(cursor.getString(1));
                time=cursor.getString(3);
                //HH:MM MM/DD/YY
                String[] arr = time.replaceAll("[\\:\\/]", " ").split(" ");
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(arr[4]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
                temp.setTimestamp(cal);
                temp.setTitle(cursor.getString(4));
                temp.setDescription(cursor.getString(5));
            temp.setImageFileName(Uri.parse(cursor.getString(6)));
//            //String image
//            byte[] imgArr= cursor.getBlob(cursor.getColumnIndex("imgFile"));
//            Bitmap image = BitmapFactory.decodeByteArray(imgArr, 0, imgArr.length);
//            temp.setImage(image);
                temp.setFlagCount(Integer.parseInt(cursor.getString(7)));
                //temp.getTags();
                username=(cursor.getString(8));
                temp.getOwner().setUsername(username);
                sightings.add(temp);
            }
        cursor.close();
            db.close();
            return sightings;
        }

    public ArrayList<Sighting> filterByTime(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Sighting> sightings = new ArrayList<>();
        Location location = new Location("");
        String time;
        String username;
        String arr[];
        Calendar cal;

        //sighting_id may nee to be surrounded by ' '
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_SIGHTINGS + " ORDER BY sighting_id DESC", null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Sighting temp = new Sighting();
                temp.setID(Integer.parseInt(cursor.getString(0)));
                location.setLongitude(Double.parseDouble(cursor.getString(1)));
                location.setLatitude(Double.parseDouble(cursor.getString(2)));
                //temp.setLocation(cursor.getString(1));
                time=cursor.getString(3);
                //HH:MM MM/DD/YY
                arr = time.replaceAll("[\\:\\/]", " ").split(" ");
                cal = Calendar.getInstance();
                cal.set(Integer.parseInt(arr[4]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
                temp.setTimestamp(cal);
                temp.setTitle(cursor.getString(4));
                temp.setDescription(cursor.getString(5));
                temp.setImageFileName(Uri.parse(cursor.getString(6)));
//                //String image
//                byte[] imgArr= cursor.getBlob(cursor.getColumnIndex("imgFile"));
//                Bitmap image = BitmapFactory.decodeByteArray(imgArr, 0, imgArr.length);
//                temp.setImage(image);
                temp.setFlagCount(Integer.parseInt(cursor.getString(7)));
                //temp.getTags();
                username=(cursor.getString(8));
                temp.getOwner().setUsername(username);
                sightings.add(temp);
            }
            cursor.close();
            db.close();
            return sightings;
        }

    public Animal getAnimal(int animal_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> observers = new ArrayList<>();
        Animal animal = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_ANIMALS + " WHERE animal_id = '"+ animal_id+"'", null);

        animal.setAnimalID(Integer.parseInt(cursor.getString(0)));
        animal.setCommonName(cursor.getString(1));

        Cursor cursor1 = db.rawQuery("SELECT * FROM " + TABLE_NAME_ANIMALS + " a " + " LEFT OUTER JOIN " + TABLE_NAME_OBSERVERS + " o "
                + " ON a.animal_id = o.animal_id", null);

        cursor1.moveToFirst();
        while(!cursor1.isAfterLast()) {
            observers.add(cursor.getString(0));
        }

        animal.setObservers(observers);

        cursor1.close();
        //check if it is a species
        boolean bool = false;
        Cursor cursor2 = db.rawQuery("SELECT * FROM "+ TABLE_NAME_SPECIES + " WHERE animal_id = '" + animal.getAnimalID() + "'",null);
        bool=cursor.moveToFirst();
        if(bool){
            Species species = new Species(animal.getAnimalID(), animal.getCommonName(), cursor2.getString(0), cursor2.getString(2),
                    cursor2.getString(3), cursor2.getString(4));
            cursor2.close();
            db.close();

            return species;
        }
        else{
            cursor2.close();
            db.close();
            return animal;
        }
    }

        public int getDailySightingCount(int animalID, Calendar c){
            SQLiteDatabase db = this.getWritableDatabase();
            //Convert calendar to date in format MM/DD/YYYY
            String date = c.get(Calendar.MONTH) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR);
            //Look for sightings that include %MM/DD/YYYY% and have same animal id
            Cursor cursor = db.rawQuery("SELECT COUNT(sighting_id) FROM "
                    + TABLE_NAME_SIGHTINGS + " WHERE animal_id = " + animalID
                    + " AND timeStamp LIKE '%" + date + "%'", null);
            int result;
            if(cursor.moveToFirst()){
                result = cursor.getInt(0);
            }
            else{
                result = 0;
            }
            cursor.close();
            db.close();
            return result;
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
