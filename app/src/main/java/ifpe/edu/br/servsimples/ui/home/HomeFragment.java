/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.IServerManagerInterfaceWrapper;
import ifpe.edu.br.servsimples.managers.ServerManager;
import ifpe.edu.br.servsimples.model.Notification;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.ui.UIInterfaceWrapper;
import ifpe.edu.br.servsimples.util.PersistHelper;


public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private List<Notification> notifications = new ArrayList<>();

    private ImageView mIvNotificationIcon;
    private TextView mTvNotificationCountNum;
    private TextView mTvNotificationCountText;
    private TextView mTvNotificationStatus;
    private UIInterfaceWrapper.FragmentUtil mFragmentUtil;

    public HomeFragment() {
    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViewsByIds(view);
        requireActivity().setTitle("Início");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ServerManager.getsInstance()
                .getUser(PersistHelper.getCurrentUser(getContext()), new IServerManagerInterfaceWrapper.ServerRequestCallback() {
            @Override
            public void onSuccess(User user) {
                notifications = user.getNotifications();
                List<Notification> newNotifications = new ArrayList<>();
                for (Notification n : notifications) {
                    if (n.isNew()) {
                        newNotifications.add(n);
                    }
                }
                if (newNotifications.isEmpty()) {
                    mTvNotificationStatus.setVisibility(View.VISIBLE);
                    mTvNotificationStatus.setText("Você não possui novas notificações");
                }
                if (newNotifications.size() == 1) {
                    mTvNotificationStatus.setVisibility(View.GONE);
                    mIvNotificationIcon.setVisibility(View.VISIBLE);
                    mTvNotificationCountNum.setText("1");
                    mTvNotificationCountText.setText("Notificação nova");
                    showNotificationListener();
                } else {
                    mTvNotificationStatus.setVisibility(View.GONE);
                    mIvNotificationIcon.setVisibility(View.VISIBLE);
                    mTvNotificationCountNum.setText(String.valueOf(newNotifications.size()));
                    mTvNotificationCountText.setText("Notificações novas");
                    showNotificationListener();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "Ocorreu um problema", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNotificationListener() {
        mIvNotificationIcon.setOnClickListener(v -> {
            NotificationsFragment notificationsFragment = NotificationsFragment.newInstance(notifications);
            mFragmentUtil.openFragment(notificationsFragment, true);
        });
    }

    private void findViewsByIds(View view) {
        mIvNotificationIcon = view.findViewById(R.id.iv_home_notification);
        mTvNotificationCountNum = view.findViewById(R.id.tv_home_notification_count);
        mTvNotificationCountText = view.findViewById(R.id.tv_home_notification_text_count);
        mTvNotificationStatus = view.findViewById(R.id.tv_home_status);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof UIInterfaceWrapper.FragmentUtil) {
            mFragmentUtil = (UIInterfaceWrapper.FragmentUtil) context;
        }
        super.onAttach(context);
    }
}