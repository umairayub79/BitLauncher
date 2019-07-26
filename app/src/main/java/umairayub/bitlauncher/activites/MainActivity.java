package umairayub.bitlauncher.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import spencerstudios.com.jetdblib.JetDB;
import umairayub.bitlauncher.R;
import umairayub.bitlauncher.adapters.Adapter;
import umairayub.bitlauncher.model.App;

public class MainActivity extends AppCompatActivity {

    TextView tvSettings;
    ListView listView;
    List<App> HomeAppList;
    MainActivity context = MainActivity.this;

    Boolean theme;
    Boolean status_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        status_bar = JetDB.getBoolean(context, "status", false);
        theme = JetDB.getBoolean(context, "theme", false);

        if (status_bar) {
            if (theme) {
                setTheme(R.style.AppThemeDarkFullScreen);
                Toast.makeText(context, "FullScreen Dark", Toast.LENGTH_SHORT).show();
            } else {
                setTheme(R.style.AppThemeFullScreen);
                Toast.makeText(context, "FullScreen Light", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (theme) {
                setTheme(R.style.AppThemeDark);
                Toast.makeText(context, " Dark", Toast.LENGTH_SHORT).show();
            } else {
                setTheme(R.style.AppTheme);
                Toast.makeText(context, " Light", Toast.LENGTH_SHORT).show();
            }
        }


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tvSettings = findViewById(R.id.tvSettings);
        listView = findViewById(R.id.listview);


        tvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Getting the package name from HomeAppList
                String packname = HomeAppList.get(i).packageName;

                //Getting the launch intent for the package
                Intent intent = getPackageManager().getLaunchIntentForPackage(packname);
                if (intent != null) {
                    //launching the activity if intent is not null
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Getting the List of user selected apps from SharedPrefs using JetDB
        HomeAppList = JetDB.getListOfObjects(context, App.class, "apps");
        //checking if HomeAppList is empty then launching the app chooser activity
        if (HomeAppList.isEmpty()) {
            Intent i = new Intent(context, AppChooserActivity.class);
            startActivity(i);
        }
        // if HomeAppList is not empty  created a adapter and show the  app list
        Adapter adapter = new Adapter(context, HomeAppList);
        listView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }


}
