/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.model.Notification;
import ifpe.edu.br.servsimples.util.DateUtils;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;

public class NotificationAdapter extends BaseAdapter {

    private static final String TAG = Notification.class.getSimpleName();
    private List<Notification> mNotifications;
    private Context mContext;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            if (ServSimplesAppLogger.ISLOGABLE) {
                ServSimplesAppLogger.e(TAG, "can't set adapter");
            }
        }
        this.mContext = context;
        this.mNotifications = notifications;
    }

    @Override
    public int getCount() {
        return mNotifications.size();
    }

    @Override
    public Notification getItem(int position) {
        return mNotifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v;
        NotificationAdapter.ViewHolder holder;
        if (view == null) {
            v = LayoutInflater.from(mContext)
                    .inflate(R.layout.notification_list_item, parent, false);
            holder = new NotificationAdapter.ViewHolder();
            holder.mDate = v.findViewById(R.id.tv_notification_date_value);
            holder.mMessage = v.findViewById(R.id.tv_notification_message_value);
            holder.mBackground = v.findViewById(R.id.iv_notification_backgound);
            v.setTag(holder);
        } else {
            v = view;
            holder = (NotificationAdapter.ViewHolder) view.getTag();
        }
        Notification notification = mNotifications.get(position);
        holder.mDate.setText(DateUtils.timestampToDateAndTimeString(notification.getTimestamp()));
        holder.mMessage.setText(notification.getMessage());
        if (notification.isNew()) {
            holder.mBackground.setBackgroundColor(Color.rgb(147, 197, 93));
        } else {
            holder.mBackground.setBackgroundColor(Color.rgb(220, 173, 74));
        }
        return v;
    }

    private static class ViewHolder {
        private TextView mDate;
        private TextView mMessage;
        private ImageView mBackground;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
