package com.vutuanchien.demologin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vutuanchien.demologin.models.Models;
import com.vutuanchien.demologin.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    List<User> userList = new ArrayList<>();

    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector connectionDetector;

    public boolean checkUser(String user, String pass) {
        boolean kq = false;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).user.equals(user) && (userList.get(i).pass.equals(pass))) {
                kq = true;
                break;
            }
        }
        return kq;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnclick = (Button) findViewById(R.id.btnLogin);
        final TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
        final EditText edUser = (EditText) findViewById(R.id.email);
        final EditText edPass = (EditText) findViewById(R.id.password);



        // creating connection detector class instance
        connectionDetector = new ConnectionDetector(getApplicationContext());
        btnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get Internet status
                isInternetPresent = connectionDetector.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {

                    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://192.168.1.106/").build();
                    API apiInterface = restAdapter.create(API.class);
                    apiInterface.getData(new Callback<Models>() {
                        @Override
                        public void success(Models models, Response response) {

                            userList.clear();
                            userList = models.users;
                            if (checkUser(edUser.getText().toString(), edPass.getText().toString())) {
                                tvStatus.setText("Successful");
                                Intent intent = new Intent(MainActivity.this, SFTPTransfer.class);
                                startActivity(intent);
                            } else {
                                tvStatus.setText("Not Successful");
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getBaseContext(), "Error by Json", Toast.LENGTH_SHORT).show();
                            Log.d("Error", error.toString());
                        }
                    });

                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    showAlertDialog(MainActivity.this, "No Internet Connection",
                            "Check your internet connection.", false);
                }
            }

        });

    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
