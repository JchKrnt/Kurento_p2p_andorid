package com.example.jingbiaowang.simplep2p;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jingbiaowang.simplep2p.model.P2pSocketClient;
import com.example.jingbiaowang.simplep2p.model.SocketResonseEvents;
import com.example.jingbiaowang.simplep2p.play.PlayActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SocketResonseEvents.P2PSocketConnectEvents {

    private EditText registeret;
    private Button registerbtn;
    private EditText callet;
    private Button callbtn;

    private final static int REQUEST_CODE = 323;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);


    }


    private void initialize() {

        registeret = (EditText) findViewById(R.id.register_et);
        registerbtn = (Button) findViewById(R.id.register_btn);
        callet = (EditText) findViewById(R.id.call_et);
        callbtn = (Button) findViewById(R.id.call_btn);

        registerbtn.setOnClickListener(this);
        callbtn.setOnClickListener(this);

        P2pSocketClient socketClient = P2pSocketClient.newInstance();
        socketClient.setConnectEvents(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConnected() {

        P2pSocketClient.newInstance().register(registeret.getText().toString());
    }

    @Override
    public void onRegister(String msg) {
        Dialog dialog = new AlertDialog.Builder(MainActivity.this, android.support.design.R.style.Base_Theme_AppCompat_Dialog_Alert)
                .setTitle("注册").setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void incomingCall(final String from) {
        new AlertDialog.Builder(MainActivity.this, android.support.design.R.style.Base_Theme_AppCompat_Dialog_Alert).setTitle("有呼叫")
                .setMessage("来自" + from + "的呼叫").setPositiveButton("接听", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("call", false);
                intent.putExtra("from", from);
                startActivityForResult(intent, REQUEST_CODE);
            }
        }).setNegativeButton("扣死", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                P2pSocketClient.newInstance().incomingCallResponse(from, null, false);
            }
        }).create().show();
    }


    @Override
    public void onError(String msg) {

    }

    @Override
    public void onSocketClosed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.call_btn: {


                if (callet.getText().toString() != null && !callet.getText().toString().trim().equals("")) {

                    Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                    intent.putExtra("call", true);
                    intent.putExtra("from", registeret.getText().toString().trim());
                    intent.putExtra("to", callet.getText().toString().trim());
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Toast.makeText(getApplicationContext(), "请填写呼叫名称", Toast.LENGTH_SHORT).show();
                }

                break;
            }

            case R.id.register_btn: {

                if (registeret.getText().toString() != null && !registeret.getText().toString().trim().equals(""))
                    register();
                else {
                    Toast.makeText(getApplicationContext(), "请填写注册名称", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    /**
     * connect socket.
     */
    private void register() {

        P2pSocketClient.newInstance().connect(Constants.getWSUrl());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
