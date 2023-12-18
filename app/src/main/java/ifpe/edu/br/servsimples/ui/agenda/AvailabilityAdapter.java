/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.agenda;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.model.Availability;
import ifpe.edu.br.servsimples.util.DateUtils;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;

public class AvailabilityAdapter extends BaseAdapter {

    private static final String TAG = AvailabilityAdapter.class.getSimpleName();
    private final List<Availability> mAvailabilities;
    private final Context mContext;

    public AvailabilityAdapter(Context context, List<Availability> mAvailabilities) {
        if (mAvailabilities == null || mAvailabilities.isEmpty()) {
            if (ServSimplesAppLogger.ISLOGABLE) {
                ServSimplesAppLogger.e(TAG, "can't set adapter");
            }
        }
        this.mContext = context;
        this.mAvailabilities = mAvailabilities;
    }

    @Override
    public int getCount() {
        return mAvailabilities.size();
    }

    @Override
    public Availability getItem(int position) {
        return mAvailabilities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v;
        AvailabilityAdapter.ViewHolder holder;
        if (view == null) {
            v = LayoutInflater.from(mContext)
                    .inflate(R.layout.availability_list_item, parent, false);
            holder = new AvailabilityAdapter.ViewHolder();
            holder.mDate = v.findViewById(R.id.tv_availability_item_date_value);
            holder.mStartTime = v.findViewById(R.id.tv_availability_item_starttime_label_value);
            holder.mEndTime = v.findViewById(R.id.tv_availability_item_enttime_value);
            v.setTag(holder);
        } else {
            v = view;
            holder = (AvailabilityAdapter.ViewHolder) view.getTag();
        }
        Availability availability = mAvailabilities.get(position);
        holder.mDate.setText(DateUtils.timestampToDateString(availability.getStartTime()));
        holder.mStartTime.setText(DateUtils.timestampToTimeString(availability.getStartTime()));
        holder.mEndTime.setText(DateUtils.timestampToTimeString(availability.getEndTime()));
        return v;
    }

    private static class ViewHolder {
        private TextView mDate;
        private TextView mStartTime;
        private TextView mEndTime;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}