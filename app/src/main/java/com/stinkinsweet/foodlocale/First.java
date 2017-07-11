package com.stinkinsweet.foodlocale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class First extends AppCompatActivity {
    private TextView txtFood,txtSub1,txtSub2;
    private EditText txtUser,txtPass,txtConfirm,txtName;
    private Button btnReg;
    private String user,pass,confirm;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            finish();
            Intent f = new Intent(First.this, Second.class);
            startActivity(f);
            Toast.makeText(getApplicationContext(), "Already signed in", Toast.LENGTH_LONG).show();
        }



        progressDialog=new ProgressDialog(this);
        btnReg = (Button) findViewById(R.id.btnReg);
        txtUser = (EditText) findViewById(R.id.username);
        txtPass = (EditText) findViewById(R.id.password);
        txtConfirm = (EditText) findViewById(R.id.confirm);
        txtFood = (TextView) findViewById(R.id.txtFood);
        txtSub1 = (TextView) findViewById(R.id.txtSub1);
        txtSub2 = (TextView) findViewById(R.id.txtSub2);

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



    public void RegInfo(View view)
        {

            user=txtUser.getText().toString().trim();
            pass=txtPass.getText().toString().trim();
            confirm=txtConfirm.getText().toString().trim();

            if(user.equals("")||pass.equals(""))
            {
                Toast.makeText(getApplicationContext(),"Fields cannot be left EMPTY", Toast.LENGTH_SHORT).show();
                txtUser.setText("");
                txtPass.setText("");
                txtConfirm.setText("");

            }
            else
            if(!user.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
            {
                Toast.makeText(getApplicationContext(),"Invalid Email! TRY AGAIN", Toast.LENGTH_SHORT).show();
            }
            else
            if(!pass.equals(confirm)) {
                Toast.makeText(getApplicationContext(),"Passwords do not match! TRY AGAIN", Toast.LENGTH_SHORT).show();
                txtPass.setText("");
                txtConfirm.setText("");
            }


            else
              if((user.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))&&(pass.equals(confirm))) {

                  progressDialog.setMessage("Registering you to FooD LocalE....");
                  progressDialog.show();
                  firebaseAuth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful())
                          {   progressDialog.dismiss();
                              Toast.makeText(getBaseContext(),"Congratulations! You have registered to FooD LocalE",Toast.LENGTH_LONG).show();
                              finish();

                              startActivity(new Intent(First.this,Second.class));
                          }
                          else
                          {
                              progressDialog.dismiss();
                              Toast.makeText(getBaseContext(), "Something went wrong\n", Toast.LENGTH_SHORT).show();
                          }
                      }
                  });

                /* ***MYSQL***
                String method ="register";

                  BackgroundTask backgroundTask=new BackgroundTask(this);
                  backgroundTask.execute(method,user,pass);

                  Intent f=new Intent(First.this,Second.class);
                  startActivity(f);
                */


                  /* ***SQLite***
                  DatabaseOperations DB=new DatabaseOperations(ctx);
                DB.insertInformation(DB,user,pass);
                   Toast.makeText(getBaseContext(),"Congratulations! You have registered to FooD LocalE",Toast.LENGTH_LONG).show();
                Intent f=new Intent(First.this,Second.class);
                startActivity(f);
                */
            }
            else {
                  Toast.makeText(getBaseContext(), "Something went WRONG!", Toast.LENGTH_LONG).show();
              }

        }

        }






