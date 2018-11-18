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

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.web.sugar.Web;
import androidx.test.espresso.web.webdriver.DriverAtoms;
import androidx.test.espresso.web.webdriver.Locator;
import android.webkit.WebView;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static androidx.test.espresso.web.sugar.Web.onWebView;
import static androidx.test.espresso.web.webdriver.DriverAtoms.clearElement;
import static androidx.test.espresso.web.webdriver.DriverAtoms.findElement;
import static androidx.test.espresso.web.webdriver.DriverAtoms.getText;
import static androidx.test.espresso.web.webdriver.DriverAtoms.webClick;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SplashScreenTest {

	private String username = "AsianPw";
	private String pwd = "aNQqH6SGUwm%wgr8x2$&y";

	@Rule
	public ActivityTestRule<SplashScreen> mActivityTestRule = new ActivityTestRule<>(SplashScreen.class);

	@Rule
	public GrantPermissionRule mGrantPermissionRule =
			GrantPermissionRule.grant(
					"android.permission.READ_EXTERNAL_STORAGE",
					"android.permission.WRITE_EXTERNAL_STORAGE");

	@Test
	public void splashScreenTest() {
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

		ViewInteraction appCompatImageButton = onView(
				allOf(withContentDescription("Revenir en haut de la page"),
						childAtPosition(
								allOf(withId(R.id.bottom_app_bar),
										childAtPosition(
												withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
												2)),
								0),
						isDisplayed()));
		appCompatImageButton.perform(click());

		ViewInteraction navigationMenuItemView = onView(
				allOf(childAtPosition(
						allOf(withId(R.id.design_navigation_view),
								childAtPosition(
										withId(R.id.navigation_view),
										0)),
						1),
						isDisplayed()));
		navigationMenuItemView.perform(click());

		// Added a sleep statement to match the app's execution delay.
		// The recommended way to handle such scenarios is to use Espresso idling resources:
		// https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ViewInteraction switch_ = onView(
				allOf(withId(R.id.switch_mature_content), withText("Contenu Adulte"),
						childAtPosition(
								allOf(withId(R.id.linearLayout),
										childAtPosition(
												withClassName(is("android.widget.ScrollView")),
												0)),
								1)));
		switch_.perform(scrollTo(), click());

		ViewInteraction materialButton = onView(
				allOf(withId(R.id.save_btn), withText("Sauvegarder"),
						childAtPosition(
								allOf(withId(R.id.linearLayout),
										childAtPosition(
												withClassName(is("android.widget.ScrollView")),
												0)),
								6)));
		materialButton.perform(scrollTo(), click());

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		onView(withId(R.id.refreshSwipeSetting))
				.perform(swipeDown());

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		switch_.check(ViewAssertions.matches(isNotChecked()));
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
