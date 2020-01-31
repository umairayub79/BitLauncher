package umairayub.bitlauncher.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import java.util.List;

import spencerstudios.com.bungeelib.Bungee;
import spencerstudios.com.jetdblib.JetDB;
import umairayub.bitlauncher.R;
import umairayub.bitlauncher.adapters.Adapter;
import umairayub.bitlauncher.listeners.OnSwipeTouchListener;
import umairayub.bitlauncher.model.App;

public class MainActivity extends AppCompatActivity {

    TextView tvSettings;
    ListView listView;
    List<App> HomeAppList;
    MainActivity context = MainActivity.this;
    Boolean theme;
    Boolean status_bar;
    ConstraintLayout root;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        status_bar = JetDB.getBoolean(context, "status", false);
        theme = JetDB.getBoolean(context, "theme", false);

        if (status_bar) {
            if (theme) {
                setTheme(R.style.AppThemeDarkFullScreen);
            } else {
                setTheme(R.style.AppThemeFullScreen);
            }
        } else {
            if (theme) {
                setTheme(R.style.AppThemeDark);
            } else {
                setTheme(R.style.AppTheme);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.relativeLayout);
        tvSettings = findViewById(R.id.tvSettings);
        listView = findViewById(R.id.listview);

        listView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Intent i = new Intent(context, AppDrawerActivity.class);
                startActivity(i);
                Bungee.slideLeft(context);
            }
        });
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
                String packageName = HomeAppList.get(i).packageName;

                //Getting the launch intent for the package
                Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
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
        // if HomeAppList is not empty  then create adapter and show the  app list
        Adapter adapter = new Adapter(context, HomeAppList);
        listView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }


}
