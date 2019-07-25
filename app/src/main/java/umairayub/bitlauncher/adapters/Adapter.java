package umairayub.bitlauncher.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import umairayub.bitlauncher.R;
import umairayub.bitlauncher.model.App;

public class Adapter extends BaseAdapter {

    private final Context mContext;
    private final List<App> appList;
    private ArrayList<App> arraylist;


    // 1
    public Adapter(Context context,List<App> applist) {
        this.mContext = context;
        this.appList = applist;
    }

    // 2
    @Override
    public int getCount() {
        return appList.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return appList.get(position);
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        final TextView name = (TextView)convertView.findViewById(R.id.tv_item);

        name.setText(appList.get(position).Appname);

        return convertView;

    }


}
