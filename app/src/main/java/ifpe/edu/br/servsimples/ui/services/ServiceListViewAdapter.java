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

public class ServiceListViewAdapter extends BaseAdapter {

    private static final String TAG = ServiceListViewAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<Service> mServices;

    public ServiceListViewAdapter(Context context, List<Service> services) {
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v;
        ServiceListViewAdapter.ViewHolder holder;
        if (view == null) {
            v = LayoutInflater.from(mContext)
                    .inflate(R.layout.service_list_item, viewGroup, false);
            holder = new ServiceListViewAdapter.ViewHolder();
            holder.serviceName = v.findViewById(R.id.tv_availability_item_date_value);
            holder.serviceValue = v.findViewById(R.id.tv_availability_item_starttime_label_value);
            holder.serviceTime = v.findViewById(R.id.tv_availability_item_enttime_value);
            v.setTag(holder);
        } else {
            v = view;
            holder = (ServiceListViewAdapter.ViewHolder) view.getTag();
        }
        if (mServices.isEmpty()) {
            holder.serviceName.setText("");
            holder.serviceValue.setText("");
            holder.serviceTime.setText("");
        } else {
            holder.serviceName.setText(mServices.get(position).getName());
            holder.serviceValue.setText(mServices.get(position).getCost().getValue());
            holder.serviceTime.setText(mServices.get(position).getCost().getTime());
        }
        return v;
    }

    private static class ViewHolder {
        private TextView serviceName;
        private TextView serviceValue;
        private TextView serviceTime;
    }
}
