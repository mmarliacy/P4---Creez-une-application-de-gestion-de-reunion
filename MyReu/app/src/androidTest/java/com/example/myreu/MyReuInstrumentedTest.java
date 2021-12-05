package com.example.myreu;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.example.myreu.utils.MenuIdOrTextViewAction.withMenuIdOrText;
import static com.example.myreu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static com.example.myreu.utils.SelectItemView.withIndex;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.myreu.ui.di.DI;
import com.example.myreu.ui.service.MyReuApiService;
import com.example.myreu.ui.ui.MyReuActivity;
import com.example.myreu.utils.DeleteViewAction;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented Test class for list of meeting
 * These tests need to be launch globally
 */

@RunWith(AndroidJUnit4.class)
public class MyReuInstrumentedTest {

    //-- Variables for test --
    private static final int ITEMS_COUNT = 0;
    MyReuApiService service;

    {
        service = DI.getListReuApiService();
    }
    //-- RULE/BEFORE -->
    @Rule
    public ActivityTestRule<MyReuActivity> mActivityRule = new ActivityTestRule<>(MyReuActivity.class);
    @Before
    public void setUp() {
        MyReuActivity mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }
    //-- TESTS -->
    @Test
    public void addAReu() {
        //-- Click on button "CreateReu" and create a meeting --
        onView((withId(R.id.createReu_Btn))).perform(click());
        onView(withId(R.id.add_subject_edit)).perform(typeText("Management & RH"));
        //-- Select visible room "Mario" --
        onView(withId(R.id.add_place_txf)).perform(click());
        onView(withText("Mario")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView((withId(R.id.act_add_place))).perform(pressBack());
        //-- Add fake participant to participant list --
        onView(withId(R.id.add_participants_edit)).perform(typeText("philippe.RH@myreu.fr"));
        onView((withId(R.id.act_add_place))).perform(pressBack());
        onView((withId(R.id.add_participants_btn))).perform(click());
        //-- Click on button "AddReu" to add a meeting to recyclerview --
        onView((withId(R.id.addReu_Btn))).perform(click());
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
        onView((withId(R.id.recyclerView))).check(withItemCount(ITEMS_COUNT + 1));
    }

    @Test
    public void filterReuByRoom() {
        //-- Click on button "CreateReu" and create a second meeting --
            onView((withId(R.id.createReu_Btn))).perform(click());
            onView(withId(R.id.add_subject_edit)).perform(typeText("Marketing-mix"));
        //-- Select visible room "Peach" --
            onView(withId(R.id.add_place_txf)).perform(click());
            onView(withText("Peach")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
            onView((withId(R.id.act_add_place))).perform(pressBack());
        //-- Add fake participant to participant list --
            onView(withId(R.id.add_participants_edit)).perform(typeText("seraph9-D@myreu.fr"));
            onView((withId(R.id.act_add_place))).perform(pressBack());
            onView((withId(R.id.add_participants_btn))).perform(click());
        //-- Click on button "AddReu" to add a second meeting to recyclerview --
            onView((withId(R.id.addReu_Btn))).perform(click());
            onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
        //-- Click on search menu and type text to filter by room name "Peach"--
            onView(withMenuIdOrText(R.id.filter_by_place, R.string.filtrer_par_lieu)).perform(click());
            onView(withMenuIdOrText(R.id.filter_by_place, R.string.filtrer_par_lieu)).perform(click());
            onView(withMenuIdOrText(R.id.filter_by_place, R.string.filtrer_par_lieu)).perform(typeText("peach"));
        //-- Check if the filter search works and matches the room of the list item
            onView(withIndex(withId(R.id.item_meeting_room), 0)).check(matches(withText("Peach")));
    }
    @Test
    public void filterReuByDate() {
        //-- Click on button "CreateReu" and create a third meeting --
            onView((withId(R.id.createReu_Btn))).perform(click());
            onView(withId(R.id.add_subject_edit)).perform(typeText("Promotion & Team"));
        //-- Select visible room "Peach" --
            onView(withId(R.id.add_place_txf)).perform(click());
            onView(withText("Peach")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        //-- Select date in DatePicker for this third meeting --
            onView(withId(R.id.add_date_txf)).perform(click());
            onView(withId(R.id.add_date_edit)).perform(clearText());
            onView(withId(R.id.add_date_txf)).perform(click());
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 10, 26));
            onView(withText("OK")).perform(click());
        //-- Add fake participant to participant list --
            onView((withId(R.id.act_add_place))).perform(pressBack());
            onView(withId(R.id.add_participants_edit)).perform(typeText("TH.thre@myreu.fr"));
            onView((withId(R.id.act_add_place))).perform(pressBack());
            onView((withId(R.id.add_participants_btn))).perform(click());
            onView((withId(R.id.addReu_Btn))).perform(click());
        //-- Click on search menu and type text --
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText("Filtrer par date")).perform(click());
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 10, 26));
            onView(withText("OK")).perform(click());
            onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
            onView(withIndex(withId(R.id.item_date), 0)).check(matches(withText("26/10/2021")));
    }
    @Test
    public void removeAReu() {
        //-- Remove an item at position 0, and check if after adding items ITEMS COUNT count one item less --
            onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
            onView(allOf(withId(R.id.recyclerView), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
            onView((withId(R.id.recyclerView))).check(withItemCount(ITEMS_COUNT+2));
    }
    @Test
    public void nameHasBeenSet() {
        onView((withId(R.id.recyclerView))).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.meeting_informations)).check(matches(isDisplayed()));
        onView(withId(R.id.myReu_subject)).check(matches(withText("Promotion & Team")));
    }
}