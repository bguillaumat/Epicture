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

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

	Storage store = new Storage();
	private SharedPreferences sharedPreferences;

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
						Toast.makeText(getContext(), "Click !", Toast.LENGTH_LONG).show();
						break;
					case (R.id.nav2):
						Toast.makeText(getContext(), "Click !", Toast.LENGTH_LONG).show();
						break;
					case (R.id.nav3):
						Intent intent = new Intent(getContext(), LoginActivity.class);
						sharedPreferences = getActivity().getSharedPreferences(getString(R.string.user_info_pref), Context.MODE_PRIVATE);
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
