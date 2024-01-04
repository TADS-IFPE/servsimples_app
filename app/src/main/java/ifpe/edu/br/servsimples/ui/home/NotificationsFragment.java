package ifpe.edu.br.servsimples.ui.home;

import android.os.Bundle;

import androidx.compose.ui.state.ToggleableState;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.IServerManagerInterfaceWrapper;
import ifpe.edu.br.servsimples.managers.ServerManager;
import ifpe.edu.br.servsimples.model.Notification;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.util.PersistHelper;

public class NotificationsFragment extends Fragment {
    private List<Notification> mNotifications = new ArrayList<>();
    private NotificationAdapter mAdapter;
    private ListView mLvNotifications;
    public NotificationsFragment() {
    }

    private NotificationsFragment(List<Notification> notifications) {
        this.mNotifications = notifications;
    }

    public static NotificationsFragment newInstance(List<Notification> notifications) {
        NotificationsFragment fragment = new NotificationsFragment(notifications);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        requireActivity().setTitle("Notificações");
        findViewsByIds(view);
        setupAdapter();
        setupListeners();
        return view;
    }

    private void setupListeners() {
        mLvNotifications.setOnItemClickListener((parent, view, position, id) -> {
            Notification selectedNotification = mNotifications.get(position);
            selectedNotification.isNew(false);
            User currentUser = PersistHelper.getCurrentUser(getContext());
            currentUser.getNotifications().add(selectedNotification);
            ServerManager.getsInstance()
                    .setNotificationViewed(currentUser, new IServerManagerInterfaceWrapper.AppointmentCallback() {
                @Override
                public void onSuccess() {
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getContext(), "houve um erro", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                }
            });
        });
    }

    private void setupAdapter() {
        mAdapter = new NotificationAdapter(getContext(), mNotifications);
        mLvNotifications.setAdapter(mAdapter);
    }

    private void findViewsByIds(View view) {
        mLvNotifications = view.findViewById(R.id.lv_shownotifications);
    }
}