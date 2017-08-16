package net.transcode001.creambox;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import twitter4j.MediaEntity;
import twitter4j.Status;


public class TweetAdapter extends ArrayAdapter<twitter4j.Status>{
        private LayoutInflater mInflater;
        private URL url;
        private InputStream mStream;
        private Bitmap bmp;
        private ImageView imageView;
        private Status item;
        private ListView lv;
        private RecyclerView.ViewHolder holder;



        public TweetAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.tweet_layout, null);
            }


            //imageView = (ImageView) convertView.findViewById(R.id.icon);
            //Boolean getView=Boolean.FALSE;

            //getUserIcon();
            item = getItem(position);
            TextView text = (TextView) convertView.findViewById(R.id.text);
            if(getItem(position).isRetweet()){
                item=getItem(position).getRetweetedStatus();
                text.setTextColor(Color.rgb(0,100,0));
            }else{
                item = getItem(position);
                text.setTextColor(Color.BLACK);
                //getUserIcon(getItem(position));
            }


            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(item.getUser().getName());
            TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
            screenName.setText("@" + item.getUser().getScreenName());
            text.setText(item.getText());
            //SmartImageView sImageView = (SmartImageView) convertView.findViewById(R.id.icon);
            //sImageView.setImageUrl(item.getUser().getProfileImageURL());

            //imageView = (ImageView)convertView.findViewById(R.id.icon);

            getUserIcon(item,convertView);
            TextView via=(TextView) convertView.findViewById(R.id.via);

            String[] viaText = item.getSource().split("<*>",-1);
            String[] viaTexts = viaText[1].split("<",0);

            via.setText("via "+viaTexts[0]);
            return convertView;


        }

        private Boolean getUserIcon(twitter4j.Status status, View view) {
            final twitter4j.Status userStatus = status;
            final View convertView = view;
            AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    try {
                        url = new URL(userStatus.getUser().getProfileImageURL());
                        mStream = url.openStream();
                        bmp = BitmapFactory.decodeStream(mStream);
                        System.out.println("Reading...");
                        //imageView.setImageBitmap(bmp);
                        return true;
                    } catch (MalformedURLException e) {
                        Log.e("MalformedURLException", e.toString());
                        return false;
                    } catch (IOException e) {
                        Log.e("IOException", e.toString());
                        return false;
                    }
                }
                @Override
                protected void onPostExecute(Boolean bool) {
                    if (bool == Boolean.TRUE) {
                        //if(tag.equals(imageView.getTag()))
                        ((ImageView)convertView.findViewById(R.id.icon)).setImageBitmap(bmp);
                        //imageView.setImageBitmap(bmp);
                    }
                }
            };
            task.execute();
            return true;
        }

}