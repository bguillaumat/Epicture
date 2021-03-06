package brice_bastien.epicture;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.web.webdriver.DriverAtoms;
import androidx.test.espresso.web.webdriver.Locator;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.web.sugar.Web.onWebView;
import static androidx.test.espresso.web.webdriver.DriverAtoms.clearElement;
import static androidx.test.espresso.web.webdriver.DriverAtoms.findElement;
import static androidx.test.espresso.web.webdriver.DriverAtoms.webClick;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class QueryTest {


	private String username = "AsianPw";
	private String pwd = "aNQqH6SGUwm%wgr8x2$&y";


	@Rule
	public ActivityTestRule<SplashScreen> mActivityTestRule = new ActivityTestRule<>(SplashScreen.class);

	@Rule
	public GrantPermissionRule mGrantPermissionRule =
			GrantPermissionRule.grant(
					"android.permission.READ_EXTERNAL_STORAGE",
					"android.permission.WRITE_EXTERNAL_STORAGE",
					"android.permission.CAMERA");

	@Test
	public void queryTest() {

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

		ViewInteraction actionMenuItemView = onView(
				allOf(withId(R.id.app_bar_search), withContentDescription("Rechercher"),
						childAtPosition(
								childAtPosition(
										withId(R.id.bottom_app_bar),
										1),
								1),
						isDisplayed()));
		actionMenuItemView.perform(click());

		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ViewInteraction appCompatImageView = onView(
				allOf(withClassName(is("androidx.appcompat.widget.AppCompatImageView")), withContentDescription("Rechercher"),
						childAtPosition(
								allOf(withClassName(is("android.widget.LinearLayout")),
										childAtPosition(
												withId(R.id.app_bar_search),
												0)),
								1),
						isDisplayed()));
		appCompatImageView.perform(click());

		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ViewInteraction searchAutoComplete = onView(
				allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
						childAtPosition(
								allOf(withClassName(is("android.widget.LinearLayout")),
										childAtPosition(
												withClassName(is("android.widget.LinearLayout")),
												1)),
								0),
						isDisplayed()));
		searchAutoComplete.perform(replaceText("pikachu"), closeSoftKeyboard());

		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ViewInteraction searchAutoComplete2 = onView(
				allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")), withText("pikachu"),
						childAtPosition(
								allOf(withClassName(is("android.widget.LinearLayout")),
										childAtPosition(
												withClassName(is("android.widget.LinearLayout")),
												1)),
								0),
						isDisplayed()));
		searchAutoComplete2.perform(pressImeActionButton());

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ViewInteraction toggleButton = onView(
				allOf(withId(R.id.button_favorite),
						childAtPosition(
								childAtPosition(
										withClassName(is("android.widget.RelativeLayout")),
										0),
								0),
						isDisplayed()));
		toggleButton.perform(click());

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		toggleButton.check(ViewAssertions.matches(isChecked()));

		ViewInteraction toggleButton2 = onView(
				allOf(withId(R.id.button_favorite),
						childAtPosition(
								childAtPosition(
										withClassName(is("android.widget.RelativeLayout")),
										0),
								0),
						isDisplayed()));
		toggleButton2.perform(click());

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		toggleButton.check(ViewAssertions.matches(isNotChecked()));

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
