package be.howest.nmct3.workoutapp;



import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import be.howest.nmct3.workoutapp.Account.GenericAccountService;
import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.SettingsAdmin;
import be.howest.nmct3.workoutapp.sync.SyncUtils;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class DashboardFragment extends Fragment {

    private Object mSyncObserverHandle;


    public DashboardFragment() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment newInstance(Context context) {
        DashboardFragment frag = new DashboardFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.dashboard_fragment_layout, null);

        //NAAM INVULLEN
        TextView txtNavDrawerTitle = (TextView) root.findViewById(R.id.dashboardFirstnameLastnameId);
        String firstname = SettingsAdmin.getInstance(getActivity().getApplicationContext()).getFirstname();
        String lastname = SettingsAdmin.getInstance(getActivity().getApplicationContext()).getLastname();
        txtNavDrawerTitle.setText(firstname + " " + lastname);

        TextView Weight = (TextView) root.findViewById(R.id.dashboardWEIGHT);
        TextView Height = (TextView) root.findViewById(R.id.dashboardHEIGHT);
        TextView BMI = (TextView) root.findViewById(R.id.dashboardBMI);
        calculateBMI(Weight, Height, BMI);

        return root;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        SyncUtils.CreateSyncAccount(activity);
    }

    @Override
    public void onResume() {
        super.onResume();

        mSyncStatusObserver.onStatusChanged(0);

        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING | ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mSyncObserverHandle != null){
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    public void setRefreshActionButtonState(boolean refreshing) {
        /*if (mOptionsMenu == null) {
            return;
        }*/

        /*final MenuItem refreshItem = mOptionsMenu.findItem(R.id.menu_refresh);
        if (refreshItem != null) {
            if (refreshing) {
                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
            } else {
                refreshItem.setActionView(null);
            }
        }*/
    }
    /**
     * Crfate a new anonymous SyncStatusObserver. It's attached to the app's ContentResolver in
     * onResume(), and removed in onPause(). If status changes, it sets the state of the Refresh
     * button. If a sync is active or pending, the Refresh button is replaced by an indeterminate
     * ProgressBar; otherwise, the button itself is displayed.
     */
    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            getActivity().runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = GenericAccountService.GetAccount();
                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
                        setRefreshActionButtonState(false);
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, Contract.AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, Contract.AUTHORITY);
                    setRefreshActionButtonState(syncActive || syncPending);
                }
            });
        }
    };

    public void calculateBMI(TextView pWeight, TextView pHeight, TextView pBMI)
    {
        double iWeight = Double.parseDouble(pWeight.getText().toString());
        double iHeight = Double.parseDouble(pHeight.getText().toString());
        iHeight = iHeight/100;

        Log.d("WEIGHT", Double.toString(iWeight));
        Log.d("HEIGHT", Double.toString(iHeight));

        double myBMI = (iWeight / (iHeight * iHeight));
        myBMI *= 100;
        myBMI = Math.round(myBMI);
        myBMI /= 100;

        Log.d("BMI", Double.toString(myBMI));

        pBMI.setText(Double.toString(myBMI));
    }
}
