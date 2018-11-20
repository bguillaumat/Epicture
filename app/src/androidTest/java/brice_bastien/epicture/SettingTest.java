package brice_bastien.epicture;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import androidx.preference.PreferenceManager;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.web.webdriver.DriverAtoms;
import androidx.test.espresso.web.webdriver.Locator;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.web.sugar.Web.onWebView;
import static androidx.test.espresso.web.webdriver.DriverAtoms.clearElement;
import static androidx.test.espresso.web.webdriver.DriverAtoms.findElement;
import static androidx.test.espresso.web.webdriver.DriverAtoms.webClick;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingTest {

	private String username = "AsianPw";
	private String pwd = "aNQqH6SGUwm%wgr8x2$&y";

	@Rule
	public ActivityTestRule<SplashScreen> mActivityTestRule = new ActivityTestRule<>(SplashScreen.class);

	@Rule
	public GrantPermissionRule mGrantPermissionRule =
			GrantPermissionRule.grant(
					"android.permission.READ_EXTERNAL_STORAGE",
					"android.permission.WRITE_EXTERNAL_STORAGE");

	@Before
	public void ClearSharedPreferences() {
		Context context = getInstrumentation().getTargetContext();
		SharedPreferences.Editor preferencesEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		preferencesEditor.clear().commit();
	}

	@Test
	public void settingTest() {
		// Added a sleep statement to match the app's execution delay.
		// The recommended way to handle such scenarios is to use Espresso idling resources:
		// https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Added a sleep statement to match the app's execution delay.
		// The recommended way to handle such scenarios is to use Espresso idling resources:
		// https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		try {
			onWebView()
					.withElement(findElement(Locator.ID, "username"))
					.perform(clearElement())
					.perform(DriverAtoms.webKeys(username))
					.withElement(findElement(Locator.ID, "password"))
					.perform(clearElement())
					.perform(DriverAtoms.webKeys(pwd))
					.withElement(findElement(Locator.ID, "allow"))
					.perform(webClick());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ViewInteraction overflowMenuButton = onView(
				allOf(withContentDescription("Options supplémentaires"),
						childAtPosition(
								childAtPosition(
										withId(R.id.bottom_app_bar),
										1),
								2),
						isDisplayed()));
		overflowMenuButton.perform(click());

		// Added a sleep statement to match the app's execution delay.
		// The recommended way to handle such scenarios is to use Espresso idling resources:
		// https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ViewInteraction appCompatTextView = onView(
				allOf(withId(R.id.title), withText("Paramètres"),
						childAtPosition(
								childAtPosition(
										withId(R.id.content),
										0),
								0),
						isDisplayed()));
		appCompatTextView.perform(click());

		// Added a sleep statement to match the app's execution delay.
		// The recommended way to handle such scenarios is to use Espresso idling resources:
		// https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ViewInteraction linearLayout = onView(
				allOf(childAtPosition(
						allOf(withId(R.id.recycler_view),
								childAtPosition(
										withId(android.R.id.list_container),
										0)),
						4),
						isDisplayed()));
		linearLayout.perform(click());

		ViewInteraction linearLayout2 = onView(
				allOf(childAtPosition(
						allOf(withId(R.id.recycler_view),
								childAtPosition(
										withId(android.R.id.list_container),
										0)),
						5),
						isDisplayed()));
		linearLayout2.perform(click());

		DataInteraction appCompatCheckedTextView = onData(anything())
				.inAdapterView(allOf(withId(R.id.select_dialog_listview),
						childAtPosition(
								withId(R.id.contentPanel),
								0)))
				.atPosition(2);
		appCompatCheckedTextView.perform(click());

		ViewInteraction linearLayout3 = onView(
				allOf(childAtPosition(
						allOf(withId(R.id.recycler_view),
								childAtPosition(
										withId(android.R.id.list_container),
										0)),
						6),
						isDisplayed()));
		linearLayout3.perform(click());

		DataInteraction appCompatCheckedTextView2 = onData(anything())
				.inAdapterView(allOf(withId(R.id.select_dialog_listview),
						childAtPosition(
								withId(R.id.contentPanel),
								0)))
				.atPosition(2);
		appCompatCheckedTextView2.perform(click());

		ViewInteraction linearLayout4 = onView(
				allOf(childAtPosition(
						allOf(withId(R.id.recycler_view),
								childAtPosition(
										withId(android.R.id.list_container),
										0)),
						7),
						isDisplayed()));
		linearLayout4.perform(click());


		pressBack();

		// Added a sleep statement to match the app's execution delay.
		// The recommended way to handle such scenarios is to use Espresso idling resources:
		// https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ViewInteraction overflowMenuButton2 = onView(
				allOf(withContentDescription("Options supplémentaires"),
						childAtPosition(
								childAtPosition(
										withId(R.id.bottom_app_bar),
										1),
								2),
						isDisplayed()));
		overflowMenuButton2.perform(click());

		// Added a sleep statement to match the app's execution delay.
		// The recommended way to handle such scenarios is to use Espresso idling resources:
		// https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ViewInteraction appCompatTextView2 = onView(
				allOf(withId(R.id.title), withText("Paramètres"),
						childAtPosition(
								childAtPosition(
										withId(R.id.content),
										0),
								0),
						isDisplayed()));
		appCompatTextView2.perform(click());

		// Added a sleep statement to match the app's execution delay.
		// The recommended way to handle such scenarios is to use Espresso idling resources:
		// https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ViewInteraction switch_ = onView(
				allOf(withId(R.id.switchWidget),
						childAtPosition(
								allOf(withId(android.R.id.widget_frame),
										childAtPosition(
												IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
												1)),
								0),
						isDisplayed()));
		switch_.check(matches(isDisplayed()));
		switch_.check(matches(isChecked()));

		ViewInteraction switch_2 = onView(
				allOf(withId(R.id.switchWidget),
						childAtPosition(
								allOf(withId(android.R.id.widget_frame),
										childAtPosition(
												IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
												1)),
								0),
						isDisplayed()));
		switch_2.check(matches(isDisplayed()));
		switch_2.check(matches(isChecked()));
	}

	private static Matcher<View> childAtPosition(
			final Matcher<View> parentMatcher, final int position) {

		return new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("Child at position " + position + " in parent ");
				parentMatcher.describeTo(description);
			}

			@Override
			public boolean matchesSafely(View view) {
				ViewParent parent = view.getParent();
				return parent instanceof ViewGroup && parentMatcher.matches(parent)
						&& view.equals(((ViewGroup) parent).getChildAt(position));
			}
		};
	}
}
