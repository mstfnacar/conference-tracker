package com.ncr.medicalconferencetracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;

import com.ncr.medicalconferencetracker.CustomObjects.ConferenceObj;
import com.ncr.medicalconferencetracker.CustomObjects.DBHelper;
import com.ncr.medicalconferencetracker.CustomObjects.InviteObj;
import com.ncr.medicalconferencetracker.CustomObjects.TopicObj;

import java.util.ArrayList;

/**
 * Created by Mustafa on 22.08.2016.
 */
public class DatabaseTest extends InstrumentationTestCase {

    DBHelper db;
    SQLiteDatabase dbWrite;
    SQLiteDatabase dbRead;

    ContentValues contentValues;

    protected void setUp() throws Exception{
        super.setUp();
        db = new DBHelper(getInstrumentation().getTargetContext());
    }

    @Override
    protected void tearDown() throws Exception {
        db.clearDb();
        db.close();
        super.tearDown();
    }

    public void testLogin() throws Exception{

        // test admin login/register
        db.insertActor("testadmin", "testadminpass", 0);
        boolean resultTrue = db.checkLoginCredentials("testadmin", "testadminpass", 0);
        assertEquals(true, resultTrue);

        boolean resultFalse = db.checkLoginCredentials("testadminfalse", "testadminpass", 0);
        assertEquals(false, resultFalse);

        // test doctor login/register
        db.insertActor("testdoctor", "testdoctorpass", 1);
        boolean resultTrueDoctor = db.checkLoginCredentials("testdoctor", "testdoctorpass", 1);
        assertEquals(true, resultTrueDoctor);

        boolean resultFalseDoctor = db.checkLoginCredentials("testdoctorfalse", "testdoctorpass", 1);
        assertEquals(false, resultFalseDoctor);
    }

    public void testConferenceEntry() throws Exception{

        // test insert conference
        ArrayList<ConferenceObj> conferenceList = new ArrayList<>();
        db.insertConference("conferencename", "conferencedescription");
        conferenceList = db.getConferenceList();

        assertEquals(1, conferenceList.size());

        String conferenceName = conferenceList.get(0).getName();
        String conferenceDescription = conferenceList.get(0).getDescription();
        int conferenceId = conferenceList.get(0).getId();

        assertEquals("conferencename", conferenceName);
        assertEquals("conferencedescription", conferenceDescription);

        // test update conference
        db.updateConference("newconferencename", "newconferencedescription", conferenceId);
        conferenceList = db.getConferenceList();
        String newConferenceName = conferenceList.get(0).getName();
        String newConferenceDescription = conferenceList.get(0).getDescription();

        assertEquals("newconferencename", newConferenceName);
        assertEquals("newconferencedescription", newConferenceDescription);

        // test delete conference
        db.deleteConference(conferenceId);
        conferenceList = db.getConferenceList();

        assertEquals(0, conferenceList.size());
    }

    public void testTopicEntry() throws Exception{

        // test insert topic
        ArrayList<TopicObj> topicList = new ArrayList<>();
        db.insertTopic("topicname", "topicdescription","testdoctor");
        topicList = db.getTopicList();

        assertEquals(1, topicList.size());

        String topicName = topicList.get(0).getName();
        String topicDescription = topicList.get(0).getDescription();
        String testDoctorName = topicList.get(0).getOwnerName();

        assertEquals("topicname", topicName);
        assertEquals("topicdescription", topicDescription);
        assertEquals("testdoctor", testDoctorName);
    }

    public void testInviteEntry() throws Exception{

        // test insert invite
        ArrayList<InviteObj> inviteList = new ArrayList<>();
        db.insertInvite("doctorname", "conferencename");
        inviteList = db.getInviteList("doctorname");

        assertEquals(1, inviteList.size());

        String doctorName = inviteList.get(0).getDoctorName();
        String conferenceName = inviteList.get(0).getConferenceName();

        assertEquals("doctorname", doctorName);
        assertEquals("conferencename", conferenceName);
    }

    public void testDoctorEntry() throws Exception{

        // test insert doctor and availability of doctor
        db.insertActor("testdoctor", "testdoctorpass", 1);
        boolean result = db.isDoctorAvailable("testdoctor");

        assertEquals(true, result);
    }



}