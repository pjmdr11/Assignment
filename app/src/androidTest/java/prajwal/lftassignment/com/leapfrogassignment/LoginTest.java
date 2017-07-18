package prajwal.lftassignment.com.leapfrogassignment;

import android.content.ComponentName;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.core.deps.guava.util.concurrent.ExecutionError;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import prajwal.lftassignment.com.leapfrogassignment.Helpers.ProgressIdlingResource;

import static android.app.PendingIntent.getActivity;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by prajwal on 7/18/2017.
 */
@RunWith(AndroidJUnit4.class)
public class LoginTest {
    private String emailAddress, passWord;
    private ProgressIdlingResource progressIdlingResource;

    @Rule
    public IntentsTestRule<LoginActivity> loginActivityActivityTestRule =
            new IntentsTestRule<LoginActivity>(LoginActivity.class,true,true);

    /*@Rule
    public IntentsTestRule<DashBoardActivity> dashBoardActivityIntentsTestRule =
            new IntentsTestRule<DashBoardActivity>(DashBoardActivity.class);*/

    @Before
    public void setUp() {
        this.emailAddress = "demotest123@gmail.com";
        this.passWord = "demotest123";
        progressIdlingResource=new ProgressIdlingResource(loginActivityActivityTestRule.getActivity());
        Espresso.registerIdlingResources(progressIdlingResource);

    }


    @Test
    public void testLogin() throws Exception {
        onView(withId(R.id.et_email_login)).perform(typeText(emailAddress), closeSoftKeyboard());
        onView(withId(R.id.et_password_login)).perform(typeText(passWord), closeSoftKeyboard());
        onView(withId(R.id.btn_signin_login)).perform(click());

        Intents.intended(IntentMatchers.hasComponent(new ComponentName(getTargetContext(), DashBoardActivity.class)),times(2));

       // intended(toPackage("prajwal.lftassignment.com.leapfrogassignment.DASHBOARDACTIVITY"));
    }




}
