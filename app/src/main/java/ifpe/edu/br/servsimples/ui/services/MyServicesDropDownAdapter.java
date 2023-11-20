/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.model.Service;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;

public class MyServicesDropDownAdapter extends BaseAdapter {

    private static final String TAG = MyServicesDropDownAdapter.class.getSimpleName();
    private final Context mContext;
    private List<Service> mServices;

    public MyServicesDropDownAdapter(Context context, List<Service> services) {
        if (services == null) {
            if (ServSimplesAppLogger.ISLOGABLE) {
                ServSimplesAppLogger.e(TAG, "can't set adapter");
            }
        }
        this.mContext = context;
        this.mServices = services;
    }

    @Override
    public int getCount() {
        return mServices.size();
    }

    @Override
    public Service getItem(int position) {
        return mServices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        MyServicesDropDownAdapter.ViewHolder holder;
        if (view == null) {
            v =  LayoutInflater.from(mContext)
                    .inflate(R.layout.myservices_dropdown_item, viewGroup, false);
            holder = new MyServicesDropDownAdapter.ViewHolder();
            holder.serviceName = v.findViewById(R.id.myservices_dropdownitem_name);
            v.setTag(holder);
        } else {
            v = view;
            holder = (MyServicesDropDownAdapter.ViewHolder) view.getTag();
        }
        holder.serviceName.setText(mServices.get(i).getName());
        return v;
    }

    private static class ViewHolder {
        private TextView serviceName;
    }
}
