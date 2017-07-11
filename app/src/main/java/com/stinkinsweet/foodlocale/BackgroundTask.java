package com.stinkinsweet.foodlocale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Funkies PC on 9/17/2016.
 */
public class BackgroundTask extends AsyncTask<String,Void,String> {

  /*  public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public BackgroundTask(AsyncResponse delegate){
        this.delegate = delegate;
    }*/

    Context ctx;
    AlertDialog alertdialog;
    BackgroundTask(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertdialog=new AlertDialog.Builder(ctx).create();
        alertdialog.setTitle("Login Information");
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_URL = "http://192.168.1.7/foodlocale/register.php";
        String login_URL = "http://10.0.2.2/foodlocale/login.php";
        String method = params[0];
        if (method.equals("register")) {
            String name = params[1];
            String pass = params[2];

            try {
                URL url = new URL(reg_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Registration Sucessfull!!";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }


        }

        else if(method.equals("login"))
        {
            String login_name=params[1];
            String login_pass=params[2];
            try {
                URL url=new URL(login_URL);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputstream,"UTF-8"));
                String data = URLEncoder.encode("user","UTF-8")+"="+URLEncoder.encode(login_name,"UTF-8")+"&"+
                        URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(login_pass,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputstream.close();

                InputStream inputstream=httpURLConnection.getInputStream();
                BufferedReader bufferedreader=new BufferedReader(new InputStreamReader(inputstream,"iso-8859-1"));
                String response="";
                String line="";
                while ((line=bufferedreader.readLine())!=null)
                {
                    response+=line;
                }
                bufferedreader.close();
                inputstream.close();
                httpURLConnection.disconnect();
                return response;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();

            }
            catch (java.io.IOException e) {
                e.printStackTrace();
            }


        }
        return null;
    }

        @Override
        protected void onProgressUpdate (Void...values){
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute (String result) {
            if (result.equals("Registration Sucessfull!!"))
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();


                else
            {

                alertdialog.setMessage(result);
                alertdialog.show();}
            }
        }




