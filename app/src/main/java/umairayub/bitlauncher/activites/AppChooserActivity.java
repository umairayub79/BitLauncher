package umairayub.bitlauncher.activites;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import spencerstudios.com.jetdblib.JetDB;
import umairayub.bitlauncher.R;
import umairayub.bitlauncher.adapters.Adapter;
import umairayub.bitlauncher.model.App;
import umairayub.madialog.MaDialog;
import umairayub.madialog.MaDialogListener;

//this is a one time activity to select apps for home screen
//this activity can be launched from settings again if needed

public class AppChooserActivity extends AppCompatActivity {

    // Declearing vars/views;
    ListView Listview;
    ProgressBar progressBar;
    Adapter adapter;
    ArrayList<App> apps = new ArrayList<>();
    ArrayList<App> SelectedApps = new ArrayList<>();
    AppChooserActivity context = AppChooserActivity.this;
    ChipGroup mChipsGroup;
    Boolean status_bar;
    Boolean theme;

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
        setContentView(R.layout.activity_app_chooser);

        //Inintializing views
        Listview = findViewById(R.id.listViewAllApps);
        progressBar = findViewById(R.id.progressBar);
        mChipsGroup = findViewById(R.id.chipGroup);
        FloatingActionButton fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SelectedApps.isEmpty()) {
                    //Saving user selected apps in sharedPrefs with JetDB
                    JetDB.putListOfObjects(context, SelectedApps, "apps");
                    //Finishing this activity
                    finish();
                } else {
                    new MaDialog.Builder(context)
                            .setTitle("Oops!")
                            .setMessage("You can't leave without selecting any apps")
                            .setPositiveButtonText("Ok, Let me select some apps")
                            .setPositiveButtonListener(new MaDialogListener() {
                                @Override
                                public void onClick() {

                                }
                            })
                            .build();
                }
            }
        });

        new LoadApps().execute();
        Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // getting the app's name from apps array of object at position i
                String AppName = apps.get(i).Appname;
                Log.i("Settings", apps.get(i).packageName);
                //Making sure users select 5 apps Only
                if (SelectedApps.size() < 5) {
                    //Making a app object
                    final App app = apps.get(i);
                    //adding the app object in SelectedApps Array
                    SelectedApps.add(app);

                    //Making a Chip
                    final Chip chip = new Chip(context);
                    chip.setText(AppName);
                    chip.setCloseIconResource(R.drawable.ic_clear);
                    chip.setCloseIconEnabled(true);
                    chip.setCloseIconTintResource(R.color.colorWhite);
                    chip.setPadding(2, 2, 2, 2);
                    chip.setTextColor(Color.WHITE);
                    chip.setChipBackgroundColorResource(R.color.colorAccent);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // removing the chip
                            mChipsGroup.removeView(chip);
                            // removing the app from Arraylist
                            SelectedApps.remove(app);
                            //re adding the app to apps array
                            apps.add(app);
                            //Sorting the apps array again
                            Collections.sort(apps, new Comparator<App>() {
                                @Override
                                public int compare(App item, App t1) {
                                    return item.Appname.toLowerCase().compareTo(t1.Appname.toLowerCase());
                                }
                            });

                            adapter.notifyDataSetChanged();

                        }
                    });
                    // adding chip to chipsGroup
                    mChipsGroup.addView(chip);
                    //we remove the selected apps from apps array
                    apps.remove(i);
                    adapter.notifyDataSetChanged();

                    // showing a dialog if user tries to selected more than 5 apps
                } else {
                    new MaDialog.Builder(context)
                            .setTitle("Oops")
                            .setMessage("You can't select more then 5 apps")
                            .setPositiveButtonText("Oh, Okay")
                            .setPositiveButtonListener(new MaDialogListener() {
                                @Override
                                public void onClick() {

                                }
                            })
                            .build();
                }


            }
        });

    }


    public class LoadApps extends AsyncTask<Integer, Integer, List<App>> {

        PackageManager packageManager = getPackageManager();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<App> doInBackground(Integer... integers) {
            if (!apps.isEmpty()) {
                apps.clear();
            }
            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ApplicationInfo> applications = getPackageManager().getInstalledApplications(0);
            for (ApplicationInfo application : applications) {
                if (packageManager.getLaunchIntentForPackage(application.packageName) != null) {

                    //Skipping Bit Launcher from apps list
                    if (application.packageName.equals("umairayub.bitlauncher")) {
                        continue;
                    }

                    App app = new App();
                    app.Appname = application.loadLabel(packageManager).toString();
                    app.packageName = application.packageName;
                    apps.add(app);


                }
            }
            return apps;
        }

        @Override
        protected void onPostExecute(List<App> items) {
            progressBar.setVisibility(View.GONE);
            Listview.setVisibility(View.VISIBLE);
            adapter = new Adapter(context, items);
            Collections.sort(items, new Comparator<App>() {
                @Override
                public int compare(App item, App t1) {
                    return item.Appname.toLowerCase().compareTo(t1.Appname.toLowerCase());
                }
            });
            Listview.setAdapter(adapter);

            super.onPostExecute(items);
        }
    }
}
