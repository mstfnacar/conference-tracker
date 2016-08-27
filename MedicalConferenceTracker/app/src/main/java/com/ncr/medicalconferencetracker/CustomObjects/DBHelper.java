package com.ncr.medicalconferencetracker.CustomObjects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Mustafa on 20.08.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private final String TAG = "DBHelper_DEBUG";
    private static final String DATABASE_NAME = "TrackerDB.db";
    private static final String CONFERENCE_TABLE_NAME = "conference";
    private static final String CONFERENCE_TABLE_NAME_COLUMN_NAME = "name";
    private static final String CONFERENCE_TABLE_DESCRIPTION_COLUMN_NAME = "description";
    private static final String CONFERENCE_TABLE_ID_COLUMN_NAME = "id";

    private static final String ADMIN_TABLE_NAME = "admin";
    private static final String DOCTOR_TABLE_NAME = "doctor";
    private static final String ACTOR_TABLE_USERNAME_COLUMN_NAME = "name";
    private static final String ACTOR_TABLE_PASSWORD_COLUMN_NAME = "password";

    private static final String TOPIC_TABLE_NAME = "topic";
    private static final String TOPIC_TABLE_NAME_COLUMN_NAME = "name";
    private static final String TOPIC_TABLE_DESCRIPTION_COLUMN_NAME = "description";
    private static final String TOPIC_TABLE_ID_COLUMN_NAME = "id";
    private static final String TOPIC_TABLE_OWNER_COLUMN_NAME = "owner";

    private static final String INVITE_TABLE_NAME = "invites";
    private static final String INVITE_TABLE_DOCTORNAME_COLUMN_NAME = "doctorname";
    private static final String INVITE_TABLE_ADMINNAME_COLUMN_NAME = "adminname";
    private static final String INVITE_TABLE_CONFERENCENAME_COLUMN_NAME = "conferencename";
    private static final String INVITE_TABLE_ID_COLUMN_NAME = "id";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL( "create table if not exists conference " +
                        "(id integer primary key, " +
                        "name text, " +
                        "description text) ");

        db.execSQL( "create table if not exists admin " +
                "(id integer primary key, " +
                "name text, " +
                "password text) ");

        db.execSQL( "create table if not exists doctor " +
                "(id integer primary key, " +
                "name text, " +
                "password text) ");

        db.execSQL( "create table if not exists topic " +
                "(id integer primary key, " +
                "name text, " +
                "description text, " +
                "owner text, " +
                "seenby integer, " +
                "FOREIGN KEY(owner) REFERENCES doctor(name), " +
                "FOREIGN KEY(seenby) REFERENCES admin(id)) ");

        db.execSQL( "create table if not exists invites " +
                "(id integer primary key, " +
                "doctorname text, " +
                "conferencename text, " +
                "FOREIGN KEY(doctorname) REFERENCES doctor(name), " +
                "FOREIGN KEY(conferencename) REFERENCES conference(name)) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        db.execSQL("DROP TABLE IF EXISTS admin");
        db.execSQL("DROP TABLE IF EXISTS doctor");
        db.execSQL("DROP TABLE IF EXISTS conference");
        db.execSQL("DROP TABLE IF EXISTS topic");

        onCreate(db);
    }

    public void clearDb()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ADMIN_TABLE_NAME, null, null);
        db.delete(DOCTOR_TABLE_NAME, null, null);
        db.delete(CONFERENCE_TABLE_NAME, null, null);
        db.delete(TOPIC_TABLE_NAME, null, null);
    }

    public boolean insertActor(String name, String password, int actorType)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACTOR_TABLE_USERNAME_COLUMN_NAME, name);
        contentValues.put(ACTOR_TABLE_PASSWORD_COLUMN_NAME, password);
        if(actorType == 0)
            db.insert(ADMIN_TABLE_NAME, null, contentValues);
        else if(actorType == 1)
            db.insert(DOCTOR_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean checkLoginCredentials(String username, String password, int actorType)
    {
        boolean result = false;
        try
        {
            String tableName = "";
            if(actorType == 0)
                tableName = "admin";
            else
                tableName = "doctor";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from "+ tableName + " where name='"+ username +"' and password='" + password +"'", null );

            if (res != null && res.getCount() > 0)
            {
                result = true;
            }
        }
        catch(SQLException e)
        {
            result = false;
        }

        return result;
    }

    public ArrayList<ConferenceObj> getConferenceList()
    {
        ArrayList<ConferenceObj> conferenceList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + CONFERENCE_TABLE_NAME +"";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            ConferenceObj newConference = new ConferenceObj(cursor.getString(cursor.getColumnIndex(CONFERENCE_TABLE_NAME_COLUMN_NAME)),
                                                        cursor.getString(cursor.getColumnIndex(CONFERENCE_TABLE_DESCRIPTION_COLUMN_NAME)),
                                                        cursor.getInt(cursor.getColumnIndex(CONFERENCE_TABLE_ID_COLUMN_NAME)));

            conferenceList.add(newConference);
            cursor.moveToNext();
        }


        return conferenceList;
    }

    public void insertConference(String name, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONFERENCE_TABLE_NAME_COLUMN_NAME, name);
        contentValues.put(CONFERENCE_TABLE_DESCRIPTION_COLUMN_NAME, description);

        db.insert(CONFERENCE_TABLE_NAME, null, contentValues);
    }

    public void updateConference (String name, String description, int id )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONFERENCE_TABLE_NAME_COLUMN_NAME, name);
        contentValues.put(CONFERENCE_TABLE_DESCRIPTION_COLUMN_NAME, description);
        db.update(CONFERENCE_TABLE_NAME, contentValues, "id = ? ", new String[] { String.valueOf(id) } );
    }

    public void deleteConference(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONFERENCE_TABLE_NAME, "id = ?", new String[] { String.valueOf(id) });
    }

    public ArrayList<TopicObj> getTopicList()
    {
        ArrayList<TopicObj> topicList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + TOPIC_TABLE_NAME +"";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            TopicObj newTopic = new TopicObj(cursor.getString(cursor.getColumnIndex(TOPIC_TABLE_NAME_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(TOPIC_TABLE_DESCRIPTION_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(TOPIC_TABLE_OWNER_COLUMN_NAME)),
                    cursor.getInt(cursor.getColumnIndex(TOPIC_TABLE_ID_COLUMN_NAME)));

            topicList.add(newTopic);
            cursor.moveToNext();
        }


        return topicList;
    }

    public boolean insertTopic(String name, String description, String ownerName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOPIC_TABLE_NAME_COLUMN_NAME, name);
        contentValues.put(TOPIC_TABLE_DESCRIPTION_COLUMN_NAME, description);
        contentValues.put(TOPIC_TABLE_OWNER_COLUMN_NAME, ownerName);

        try {
            db.insert(TOPIC_TABLE_NAME, null, contentValues);
            return true;
        }
        catch (SQLException e) {
            // failed
            return false;
        }
    }

    public ArrayList<String> getDoctorList()
    {
        ArrayList<String> doctorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + DOCTOR_TABLE_NAME +"";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            doctorList.add(cursor.getString(cursor.getColumnIndex(ACTOR_TABLE_USERNAME_COLUMN_NAME)));
            cursor.moveToNext();
        }


        return doctorList;
    }

    public boolean insertInvite(String doctorName, String conferenceName){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INVITE_TABLE_DOCTORNAME_COLUMN_NAME, doctorName);
        contentValues.put(INVITE_TABLE_CONFERENCENAME_COLUMN_NAME, conferenceName);

        try {
            db.insert(INVITE_TABLE_NAME, null, contentValues);
            return true;
        }
        catch (SQLException e) {
            // failed
            return false;
        }

    }

    public boolean isDoctorAvailable(String doctorName)
    {
        boolean result = false;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from "+ DOCTOR_TABLE_NAME + " where name='"+ doctorName +"'", null );

            if (res != null && res.getCount() > 0)
            {
                result = true;
            }
        }
        catch(SQLException e)
        {
            result = false;
        }

        return result;
    }

    public ArrayList<InviteObj> getInviteList(String doctorName)
    {
        ArrayList<InviteObj> inviteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + INVITE_TABLE_NAME +" where " + INVITE_TABLE_DOCTORNAME_COLUMN_NAME + "='"+ doctorName +"' ";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            InviteObj newInvite = new InviteObj(cursor.getString(cursor.getColumnIndex(INVITE_TABLE_CONFERENCENAME_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(INVITE_TABLE_DOCTORNAME_COLUMN_NAME)),
                    cursor.getInt(cursor.getColumnIndex(INVITE_TABLE_ID_COLUMN_NAME)));

            inviteList.add(newInvite);
            cursor.moveToNext();
        }


        return inviteList;
    }

    public void deleteInvite(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INVITE_TABLE_NAME, "id = ?", new String[] { String.valueOf(id) });
    }

}
