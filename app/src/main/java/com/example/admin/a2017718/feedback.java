package com.example.admin.a2017718;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

/**
 * Created by admin on 2017/9/17.
 */

public class feedback extends AppCompatActivity {

    int i=10;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);


        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i>1) {
                    EditText content = (EditText) findViewById(R.id.content);
                    new post().execute("反馈", content.getText().toString());
                i--;
                }
            }
        });
    }

    class post extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            
            String value = null;
            try {
                String string = new httpcontent().POST(params[0], params[1]);
                JSONObject jsonObject = new JSONObject(string);
                value=jsonObject.getString("msg");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return value;
        }



    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            Toast.makeText(feedback.this, s, Toast.LENGTH_SHORT).show();
        }

    }
}


}
