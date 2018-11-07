package brice_bastien.epicture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Storage store = new Storage();

    private String Token = "";
    private String Username = "";
    private String Etc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		BottomAppBar bar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);

        if (Token == null || Token.length() == 0) {
        	Token = getIntent().getStringExtra("ACCESS_TOKEN");
        	Username = getIntent().getStringExtra("ACCOUNT_USERNAME");
        	store.closeData(this, Token, Username, Etc);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Toast.makeText(getApplicationContext(), Token, Toast.LENGTH_LONG).show();
				BottomAppBar bar = findViewById(R.id.bottom_app_bar);
				if (bar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_END) {
					bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
				} else {
					bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
				}
            }
        });
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
    }

    @Override
    protected void onPause() {
        store.closeData(this, Token, Username, Etc);
        super.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case (R.id.action_settings):
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
			case (R.id.app_bar_fav):
				break;
			case (R.id.app_bar_search):
				break;
			case (android.R.id.home):
				BottomNavigationDrawerFragment bottomNavDrawerFragment = new BottomNavigationDrawerFragment();
				getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
				bottomNavDrawerFragment.show(getSupportFragmentManager(), bottomNavDrawerFragment.getTag());
				break;
		}
        return super.onOptionsItemSelected(item);
    }

}
