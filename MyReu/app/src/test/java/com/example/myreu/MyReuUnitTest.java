package com.example.myreu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.myreu.ui.di.DI;
import com.example.myreu.ui.model.Meeting;
import com.example.myreu.ui.model.Room;
import com.example.myreu.ui.service.MyReuApiService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class MyReuUnitTest {

    private MyReuApiService service;
    String chosenRoom, chosenRoom2;
    Integer icon, icon2;
    Room room, room2;

    List<String> participants = new ArrayList<>();
    List<Meeting> createdReu = new ArrayList<>();
    List<Meeting> filterListTest = new ArrayList<>();

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
        fakeVariables();
        createdReu = Arrays.asList(
                new Meeting("Le management", room, "27/10/2021", "12:00", participants),
                new Meeting("Reunion DG", room2, "25/11/2021", "9:00", participants),
                new Meeting("RH and Teammate", room2, "10/11/2021", "17:00", participants),
                new Meeting("Marketing", room, "25/11/2021", "11:00", participants));
    }

    //-- Declaration of fake variables for testing --
    public void fakeVariables() {
        chosenRoom = "Peach";
        icon = R.drawable.peach_circle;
        room = new Room(chosenRoom, icon);
        chosenRoom2 = "Toad";
        icon2 = R.drawable.toad_circle;
        room2 = new Room(chosenRoom2, icon2);
        participants.add("Sophie");
        participants.add("Manuel");
    }

    //-- Certify that the meeting list exist --
    @Test
    public void getReuList() {
        List<Meeting> meetingList = service.getAllMyReu();
        meetingList.addAll(createdReu);
        assertEquals(meetingList.get(0).getSubject(), createdReu.get(0).getSubject());
    }

    //-- Test that assert we can add a Meeting to meeting list --
    @Test
    public void addAReu() {
        Meeting reuToAdd = createdReu.get(1);
        Meeting reuToAdd2 = createdReu.get(2);
        Meeting reuToAdd3 = createdReu.get(0);
        service.addAReu(reuToAdd);
        service.addAReu(reuToAdd2);
        service.addAReu(reuToAdd3);
        assertTrue(service.getAllMyReu().contains(reuToAdd2));
        assertFalse(service.getAllMyReu().contains(createdReu.get(3)));
    }

    //-- Test that assert we can remove a Meeting to meeting list --
    @Test
    public void deleteReu() {
        List<Meeting> meetingList = service.getAllMyReu();
        meetingList.addAll(createdReu);
        Meeting reuToDelete = service.getAllMyReu().get(1);
        service.deleteAReu(reuToDelete);
        assertFalse(service.getAllMyReu().contains(reuToDelete));
        assertTrue(service.getAllMyReu().contains(meetingList.get(0)));
    }

    //-- Test that says we can filter the meeting based on the date --
    @Test
    public void filterReuByDate() {
        List<Meeting> meetingList = service.getAllMyReu();
        Meeting reuToAdd = createdReu.get(0);
        Meeting reuToAdd2 = createdReu.get(1);
        Meeting reuToAdd3 = createdReu.get(2);
        Meeting reuToAdd4 = createdReu.get(3);
        service.addAReu(reuToAdd);
        service.addAReu(reuToAdd2);
        service.addAReu(reuToAdd3);
        service.addAReu(reuToAdd4);

        //-- Set dateS1 as reference date --
        String dateS1 = "25/11/2021";

        //-- Obtain all date and compare to dateS1 --
        for (int i = 0; i < meetingList.size(); i++) {
            String dateS2 = meetingList.get(i).getDate();

            //-- Comparing && Add to filterListTest if true --
            if (dateS1.equals(dateS2)) {
                filterListTest.add(meetingList.get(i));
                assertTrue(meetingList.containsAll(filterListTest));
                assertFalse(filterListTest.contains(service.getAllMyReu().get(0)));
            }
        }
    }
    //-- Test that says we can filter the meeting based on the Room --
    @Test
    public void filterReuByRoom() {
        List<Meeting> meetingList = service.getAllMyReu();
        Meeting reuToAdd = createdReu.get(0);
        Meeting reuToAdd2 = createdReu.get(1);
        Meeting reuToAdd3 = createdReu.get(2);
        Meeting reuToAdd4 = createdReu.get(3);
        service.addAReu(reuToAdd);
        service.addAReu(reuToAdd2);
        service.addAReu(reuToAdd3);
        service.addAReu(reuToAdd4);

        //-- Set chosenRoom as reference room --
        String chosenRoom = "Toad";

        //-- Obtain all room and compare to chosenRoom --
        for (int i = 0; i < meetingList.size(); i++) {
            String filteredRoom = meetingList.get(i).getRoom().getRoom();

            //-- Comparing && Add elements to filterListTest if true --
            if (chosenRoom.equals(filteredRoom)) {
                filterListTest.add(meetingList.get(i));
                assertTrue(meetingList.containsAll(filterListTest));
                assertFalse(filterListTest.contains(service.getAllMyReu().get(0)));
            }
        }
    }
}


