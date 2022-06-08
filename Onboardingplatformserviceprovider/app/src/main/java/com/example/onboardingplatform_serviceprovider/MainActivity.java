package com.example.onboardingplatform_serviceprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);


        Button login = findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String un = username.getText().toString();
                String pw = password.getText().toString();

                String User_st = username.getText().toString();
                String Pass_st = password.getText().toString();
                int va =0;
                if (!"".equals(Pass_st)){
                    va=Integer.parseInt(Pass_st);
                }
                if ( User_st.equals("1") || User_st.equals("2") && Pass_st.equals("1234")){
                    Toast t = Toast.makeText(getApplication(),"Correct Password",Toast.LENGTH_LONG);
                    t.show();
                    Intent ii = new Intent(getApplicationContext(),location_share.class);

                    startActivity(ii);
                }
                else{
                    Toast t = Toast.makeText(getApplication(),"Incorrect Password",Toast.LENGTH_LONG);
                    t.show();
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                }
                /*
                Intent i = new Intent(getApplicationContext(),location_share.class);
                startActivity(i);

                 */
            }
        });
    }
}