package brice_bastien.epicture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import brice_bastien.epicture.ImgurApi.ImgurApi;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

	Storage store = new Storage();
	private SharedPreferences sharedPreferences;
	public PostsFragment postsFragment;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
		return inflater.inflate(R.layout.fragment_bottomsheet, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		NavigationView navigationView = getView().findViewById(R.id.navigation_view);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				sharedPreferences = getActivity().getSharedPreferences(getString(R.string.user_info_pref), Context.MODE_PRIVATE);
				String Token = sharedPreferences.getString("User_Token", null);
				String Username = sharedPreferences.getString("Username", null);

				ImgurApi imgurApi = new ImgurApi(getContext(), Username, Token);

				switch (menuItem.getItemId()) {
					case (R.id.nav1):
						Intent intentAccount = new Intent(getActivity(), AccountSetting.class);
						startActivity(intentAccount);
						break;
					case (R.id.nav2):
						imgurApi.getUserImg(postsFragment);
						// TODO interface in PostsFragment for access postsFragments ref
						break;
					case (R.id.nav3):
						Intent intent = new Intent(getContext(), LoginActivity.class);
						sharedPreferences.edit().clear().apply();
						getActivity().finish();
						startActivity(intent);
						break;
				}
				return true;
			}
		});

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStop() {
		getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
		super.onStop();
	}
}
