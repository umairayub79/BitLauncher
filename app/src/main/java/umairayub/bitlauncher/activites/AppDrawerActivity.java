package umairayub.bitlauncher.activites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;
import spencerstudios.com.jetdblib.JetDB;
import umairayub.bitlauncher.R;
import umairayub.bitlauncher.listeners.OnSwipeTouchListener;

public class AppDrawerActivity extends AppCompatActivity {

    Boolean status_bar;
    Boolean theme;
    private List<String> packageNames = new ArrayList<>();
    private List<String> appNamesPosition = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private Context context = AppDrawerActivity.this;

    @Override
    protected void onResume() {
        super.onResume();
        if (getActivities(getPackageManager()).size() - 1 != packageNames.size()) {
            fetchAppList();
        }
    }

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
        setContentView(R.layout.activity_app_drawer);

        EditText editTextFilter = findViewById(R.id.searchFilter);

        adapter = createNewAdapter();
        listView = findViewById(R.id.listViewAllApps_d);
        listView.setAdapter(adapter);
        fetchAppList();

        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private ArrayAdapter<String> createNewAdapter() {
        return new ArrayAdapter<String>(this, R.layout.drawer_item, new ArrayList<String>()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                return view;
            }
        };
    }

    private void fetchAppList() {
        packageNames.clear();
        adapter.clear();
        for (ResolveInfo resolver : getActivities(getPackageManager())) {
            String appName = (String) resolver.loadLabel(getPackageManager());
            if (appName.equals("BitLauncher"))
                continue;
            adapter.add(appName);
            appNamesPosition.add(appName);
            packageNames.add(resolver.activityInfo.packageName);
        }
        setActions();
    }

    private List<ResolveInfo> getActivities(PackageManager packageManager) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        Collections.sort(activities, new ResolveInfo.DisplayNameComparator(packageManager));
        return activities;
    }

    private void setActions() {
        onClickHandler();
        onSwipeHandler();
    }

    private void onClickHandler() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String appName = adapterView.getItemAtPosition(i).toString();
                String packageName = packageNames.get(appNamesPosition.indexOf(appName));
                try {
                    startActivity(getPackageManager().getLaunchIntentForPackage(packageName));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onSwipeHandler() {
        listView.setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.slideRight(context);
    }

}
