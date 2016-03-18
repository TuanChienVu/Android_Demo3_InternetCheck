package com.vutuanchien.demologin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPTransfer extends AppCompatActivity {

    static final String SFTP_HOST = "192.168.1.106";
    static final String SFTP_USER = "root";
    static final String SFTP_PASS = "111111";
    static final int SFTP_PORT = 22;

    Button btnClick;
    Boolean success;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sftptransfer);

        btnClick = (Button) findViewById(R.id.btnClick);
        // creating connection detector class instance
        connectionDetector = new ConnectionDetector(getApplicationContext());
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get Internet status
                isInternetPresent = connectionDetector.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {
                    new FTPConnection().execute("");
                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    showAlertDialog(SFTPTransfer.this, "No Internet Connection",
                            "Check your internet connection.", false);
                }
            }
        });
    }

    private class FTPConnection extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                JSch ssh = new JSch();
                Session session = ssh.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
                // Remember that this is just for testing and we need a quick access, you can add an identity and known_hosts file to prevent
                // Man In the Middle attacks
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.setPassword(SFTP_PASS);
                session.connect();
                Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp sftp = (ChannelSftp) channel;

                sftp.cd("/var/www/html/PmOne/web/demo/upload");
                // If you need to display the progress of the upload, read how to do it in the end of the article
                // use the put method , if you are using android remember to remove "file://" and use only the relative path
                sftp.put(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tuan Chien Vu.docx", "/var/www/html/PmOne/web/demo/upload");
                success = true;
                channel.disconnect();
                session.disconnect();
            } catch (JSchException e) {
                System.out.println(e.getMessage().toString());
                e.printStackTrace();
            } catch (SftpException e) {
                System.out.println(e.getMessage().toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (success) {
                // The file has been uploaded succesfully
                Toast.makeText(getBaseContext(), "Thanh Cong", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Toast.makeText(getBaseContext(), "Đang Tải Dữ Liệu", Toast.LENGTH_SHORT).show();
        }
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
