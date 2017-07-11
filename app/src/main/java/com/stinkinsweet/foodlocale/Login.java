package com.stinkinsweet.foodlocale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Funkies PC on 8/20/2016.
 */
public class Login extends AppCompatActivity
{
    private TextView txtFood1, txtSub3, txtSub4;
    private EditText txtUser, txtPass;
    private Button btnLogin;
    private String user, pass;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;


private Firebase ref;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            finish();
            Intent f = new Intent(Login.this, Second.class);
            startActivity(f);
            Toast.makeText(getApplicationContext(), "Already signed in", Toast.LENGTH_LONG).show();
        }
        progressDialog=new ProgressDialog(this);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        txtUser = (EditText) findViewById(R.id.username);
        txtPass = (EditText) findViewById(R.id.password);
        txtFood1 = (TextView) findViewById(R.id.txtFood1);
        txtSub3 = (TextView) findViewById(R.id.txtSub3);
        txtSub4 = (TextView) findViewById(R.id.txtSub4);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logOut) {
            Toast.makeText(getBaseContext(),"Log in to krlo beta",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void Login(View view) {
        user = txtUser.getText().toString().trim();
        pass = txtPass.getText().toString().trim();
        if (user.equals("") || pass.equals("")) {
            Toast.makeText(getApplicationContext(), "Fields cannot be left EMPTY", Toast.LENGTH_SHORT).show();
            txtUser.setText("");
            txtPass.setText("");

        } else

        if(!user.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
        {
            Toast.makeText(getApplicationContext(),"Invalid Email! TRY AGAIN", Toast.LENGTH_SHORT).show();
        }
        else
        if (user.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))

        {
            progressDialog.setMessage("Working on it ....");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();


                        // Name, email address, and profile photo Url


                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String name = user.getEmail();
                            finish();
                            int i=name.indexOf("@");
                            Toast.makeText(Login.this, "LogIn Successful Welcome " + name.substring(0,i), Toast.LENGTH_LONG).show();
                            Intent f = new Intent(Login.this, Second.class);
                            startActivity(f);
                        }

                    else
                    {
                        Toast.makeText(Login.this, "Invalid Entries", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        /* String method="login";
        BackgroundTask backgroundTask=new BackgroundTask(this);
        backgroundTask.execute(method,user,pass);*/



      /*  DatabaseOperations DOP = new DatabaseOperations(CTX);
        Cursor CR = DOP.getInformation(DOP);
        CR.moveToFirst();
        boolean login_status = false;
        String NAME = "";
        do {
            if (user.equals(CR.getString(0)) && pass.equals(CR.getString(1))) {

                login_status = true;
                NAME = CR.getString(0);
            }
        } while (CR.moveToNext());
        if (login_status) {
            Toast.makeText(getBaseContext(), "Successfully Logged In \n Welcome " + NAME, Toast.LENGTH_LONG).show();
            Intent l1 = new Intent(Login.this, Second.class);
            startActivity(l1);
        } else {
            Toast.makeText(getBaseContext(), "Invalid Username or password", Toast.LENGTH_LONG).show();
            txtUser.setText("");
            txtPass.setText("");
        }

       */
        }
    }


    public void register(View view) {

        Intent f2 = new Intent(Login.this, First.class);
        startActivity(f2);

    }


}




