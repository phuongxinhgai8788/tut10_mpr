package vn.edu.hanu.myteam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{
    ImageView imageView;
    TextView nameTv, mailTv;
    String restUrl, imageUrl;
    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        nameTv = view.findViewById(R.id.nameTv);
        mailTv = view.findViewById(R.id.mailTv);
        imageView = view.findViewById(R.id.image);


         userId = 1;
        setContent(userId);

        ImageButton userDecrease = view.findViewById(R.id.user1);
        ImageButton userIncrease = view.findViewById(R.id.user2);

        userDecrease.setOnClickListener(this);
        userIncrease.setOnClickListener(this);
        return view;
    }
    private void setContent(int userId){
       RestLoader restLoader = new RestLoader();
       ImageLoader imageLoader = new ImageLoader();


        restUrl = "https://jsonplaceholder.typicode.com/users/"+userId;
        imageUrl = "https://robohash.org/"+userId+"?set=set2";

        restLoader.execute(restUrl);

        imageLoader.execute(imageUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user1:
                userId--;
                    setContent(userId);

                break;
            case R.id.user2:
                userId++;
                    setContent(userId);

                break;
        }
    }

    private class RestLoader extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection;
            try{
                url = new URL (strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();
                Scanner sc = new Scanner(is);
                StringBuilder result = new StringBuilder();
                String line;
                while(sc.hasNextLine()){
                    line = sc.nextLine();
                    result.append(line);
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(result == null){
                return;
            }
            try {
                JSONObject root = new JSONObject(result);
                //get name
                String name = root.getString("name");
                nameTv.setText(name);
                //get email
                String email = root.getString("email");
                mailTv.setText(email);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private class ImageLoader extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try{
                URL url = new URL (strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            imageView.setImageBitmap(bitmap);
        }
    }
}