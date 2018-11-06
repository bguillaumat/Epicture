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

    private String Token = "";
    private String Etc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		BottomAppBar bar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);

        if (Token == null || Token.length() == 0) {
        	Token = getIntent().getStringExtra("ACCESS_TOKEN");
        	closeData();
		} else {
			try {
				openData();
			} catch (IOException ignored) {
				onStop();
			}
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
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean
                (SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
        Toast.makeText(this, switchPref.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        closeData();
        super.onPause();
    }

    private void openData() throws IOException {
        String FILENAME = "data";
        String token = "";
        String etc = "";
        FileInputStream fos = openFileInput(FILENAME);
        Scanner sc = new Scanner(fos);
        if (sc.hasNextLine()) token = sc.nextLine();
        if (sc.hasNextLine()) etc = sc.nextLine();
        fos.close();
        Token = token;
        Etc = etc;
    }

    private void closeData() {
        String FILENAME = "data";
        String token = Token;
        String etc = Etc;
        String finalStr = (token + '\n' + etc + '\n');
        try (FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE)) {
            fos.write(finalStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
				Toast.makeText(getApplicationContext(), "Fav Push", Toast.LENGTH_SHORT).show();
				break;
			case (R.id.app_bar_search):
				Toast.makeText(getApplicationContext(), "Search Push", Toast.LENGTH_SHORT).show();
				break;
			case (android.R.id.home):
				Toast.makeText(getApplicationContext(), "Menu Push", Toast.LENGTH_SHORT).show();
				BottomNavigationDrawerFragment bottomNavDrawerFragment = new BottomNavigationDrawerFragment();
				getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
				bottomNavDrawerFragment.show(getSupportFragmentManager(), bottomNavDrawerFragment.getTag());
				break;
		}
        return super.onOptionsItemSelected(item);
    }

}
