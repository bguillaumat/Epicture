package brice_bastien.epicture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import brice_bastien.epicture.ImgurApi.ImgurApi;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

	private SharedPreferences sharedPreferences;
	public PostsFragment postsFragment;
	public ImgurApi imgurApi;


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
				switch (menuItem.getItemId()) {
					case (R.id.nav1):
						Intent intentAccount = new Intent(getActivity(), AccountSetting.class);
						startActivity(intentAccount);
						dismiss();
						break;
					case (R.id.navFav):
						imgurApi.getUserFavorite(postsFragment);
						dismiss();
						break;
					case (R.id.nav2):
						imgurApi.getUserImg(postsFragment);
						dismiss();
						break;
					case (R.id.nav3):
						Intent intent = new Intent(getContext(), LoginActivity.class);
						sharedPreferences = getActivity().getSharedPreferences(getString(R.string.user_info_pref), Context.MODE_PRIVATE);
						sharedPreferences.edit().clear().apply();
						startActivity(intent);
						getActivity().finish();
						dismiss();
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
