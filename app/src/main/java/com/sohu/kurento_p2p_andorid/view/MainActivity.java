package com.sohu.kurento_p2p_andorid.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sohu.kurento.util.LogCat;
import com.sohu.kurento_p2p_andorid.R;
import com.sohu.kurento_p2p_andorid.controller.net.P2pSocketClient;
import com.sohu.kurento_p2p_andorid.controller.net.UserResponseBean;
import com.sohu.kurento_p2p_andorid.model.bean.UserBean;
import com.sohu.kurento_p2p_andorid.util.Constants;
import com.sohu.kurento_p2p_andorid.view.play.PlayActivity;
import com.sohu.utillib.RecycleViewSupport.DividerItemDecoration;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private TextView nameprotv;
    private TextView nametv;
    private LinearLayout namell;
    private RecyclerView friendlist;
    private FloatingActionButton refreshlist;
    FloatingActionButton registerBtn;
    private UserBroadCastReceiver userBroadCastReceiver;
    private ProgressDialog progressDialog;
    UserListeViewAdapter adapter = null;

    public static final int P2PSETTINGS_RESULTCODE = 33;
    public static final int P2PPLAY_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.USER_RECEIVER_FILTER);
        registerReceiver(userBroadCastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(userBroadCastReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class),
                    P2PSETTINGS_RESULTCODE);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialize() {

        nameprotv = (TextView) findViewById(R.id.name_pro_tv);
        nametv = (TextView) findViewById(R.id.name_tv);
        namell = (LinearLayout) findViewById(R.id.name_ll);
        friendlist = (RecyclerView) findViewById(R.id.friend_list);
        friendlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //设置item增加, 移除动画。
        friendlist.setItemAnimator(new DefaultItemAnimator());
        //添加分割線
        friendlist.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                RecyclerView.VERTICAL));
        adapter = new UserListeViewAdapter(getApplicationContext());
        adapter.setListener(itemClickListener);
        friendlist.setAdapter(adapter);

        refreshlist = (FloatingActionButton) findViewById(R.id.refresh_list_btn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        registerBtn = (FloatingActionButton) findViewById(R.id.fab);
        registerBtn.setOnClickListener(this);
        refreshlist.setOnClickListener(this);

        userBroadCastReceiver = new UserBroadCastReceiver();
        progressDialog = new ProgressDialog(MainActivity.this, android.R.style
                .Theme_DeviceDefault_Dialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showUserName();
    }

    private UserListViewItemClickListener itemClickListener = new UserListViewItemClickListener();

    /**
     * RecycleView item onClick listener.
     */
    class UserListViewItemClickListener implements UserListeViewAdapter
            .UserItemClickListener {

        @Override
        public void onItemClickListener(UserBean userBean) {

            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            intent.putExtra("caller", nametv.getText().toString());
            intent.putExtra("callee", userBean.getName());
            intent.putExtra(PlayActivity.EXTRA_ROOMID, nametv.getText().toString());
            intent.putExtra("call", true);
            startActivityForResult(intent, P2PPLAY_CODE);
        }
    }

    private void showUserName() {
        if (checkUserBean())
            nametv.setText(((P2pApp) getApplication()).getUser().getName());
    }

    private boolean checkUserBean() {

        UserBean userBean = ((P2pApp) getApplication()).getUser();
        if (userBean != null)
            return true;
        else return false;
    }

    /**
     * 获取sharePreferences.
     */
    private void initPreferenceData() {
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());

        //get url
        String url = sharedPref.getString(getString(R.string.pref_key_host), getString(R.string
                .pref_default_host));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh_list_btn: {
                requestList();

                break;
            }

            case R.id.fab: {
                //                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                //                        .setAction("Action", null).show();

                register();
                break;
            }
        }
    }


    private void requestList() {

        progressDialog.show();
        P2pSocketClient socketClient = P2pSocketClient.newInstance();
        if (socketClient.isOPened()) {
            socketClient.userList();
            progressDialog.show();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.no_connect), Toast
                    .LENGTH_SHORT).show();
            progressDialog.dismiss();
            ((P2pApp) getApplicationContext()).wsConnect();
        }
    }

    private void register() {

        RegisterDialog registerDialog = (RegisterDialog) new RegisterDialog.Builder(0.8f).create
                (new RegisterDialog(MainActivity.this, android.R.style
                        .Theme_Holo_Dialog_NoActionBar));
        registerDialog.setCanceledOnTouchOutside(false);
        registerDialog.setDialogInterface(new RegisterDialog.RegisterDialogEvents() {
            @Override
            public void cancel() {

            }

            @Override
            public void ok(String name) {
                P2pSocketClient socketClient = P2pSocketClient.newInstance();
                if (socketClient.isOPened()) {
                    socketClient.register(name);
                    progressDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_connect), Toast
                            .LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    ((P2pApp) getApplicationContext()).wsConnect();
                }
            }
        });
        registerDialog.show();
    }

    /**
     * 被呼叫提示。提示对话框。
     *
     * @param name
     */
    private void inComingCall(final String name) {

        Dialog dialog = new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.sym_call_incoming)
                .setTitle(R.string.incoming_call).setPositiveButton(getString(R.string.accept_call), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                        intent.putExtra("caller", name);
                        intent.putExtra("callee", nametv.getText().toString());
                        intent.putExtra("call", false);
                        intent.putExtra(PlayActivity.EXTRA_ROOMID, nametv.getText().toString());
                        startActivityForResult(intent, P2PPLAY_CODE);

                    }
                }).setNegativeButton(R.string.reject_call, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        P2pSocketClient.newInstance().inComingCallResponse(name, null, false);
                    }
                }).create();
        dialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == P2PSETTINGS_RESULTCODE && resultCode == RESULT_OK) {      //setting code.

        } else if (requestCode == P2PPLAY_CODE && resultCode == RESULT_OK) {    //play Activity.

        }
    }

    /**
     * 添加user监听，当websocket 断开时更新user.
     */
    public class UserBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            UserResponseBean userResponse = intent.getParcelableExtra("data");
            switch (userResponse.getType()) {

                case onList: {

                    ArrayList<UserBean> users = userResponse.getListData();
                    adapter.notifyDataOnDataChanged(users);
                    progressDialog.dismiss();
                    break;
                }

                case register: {

                    String name = userResponse.getName();
                    nametv.setText(name);
                    progressDialog.dismiss();
                    break;
                }

                case clear: {

                    LogCat.debug("user has unregister.");
                    nametv.setText("");
                    adapter.clearData();
                    break;
                }

                case removeUser: {
                    UserBean userBean = userResponse.getUserBean();
                    adapter.notifyDataOnRemoveUser(userBean);

                    break;
                }

                case addUser: {

                    UserBean userBean = userResponse.getUserBean();
                    adapter.notifyDataOnAddUser(userBean);

                    break;
                }

                case inComingCall: {

                    inComingCall(userResponse.getName());
                    break;
                }

                case error: {
                    reportMsg(userResponse.getMsg());
                }
            }

        }
    }

    private void reportMsg(String msg) {
        new android.app.AlertDialog.Builder(this)
                .setTitle(getText(R.string.channel_error_title))
                .setMessage(msg)
                .setCancelable(false)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }
}
