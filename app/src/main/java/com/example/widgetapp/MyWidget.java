package com.example.widgetapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class MyWidget extends AppWidgetProvider {

    private static final int[] imgTab= {R.drawable.mem1, R.drawable.mem2, R.drawable.mem3};
    private static int imgId = 0;

    private static final int[] musicTab= {R.raw.bonny1,R.raw.bonny2,R.raw.bonny3};
    private static int musicId=0;
    private static MediaPlayer player;

    private static boolean onUpdateWorked = false;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Toast.makeText(context, "kurwaaaaaaaa", Toast.LENGTH_SHORT).show();

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
       // views.setTextViewText(R.id.appwidget_text, "New text for the view");

        //internety
        String link = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));

        PendingIntent pendingIntent = PendingIntent.getActivity(context,appWidgetId,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.web_button, pendingIntent);

        //memy
        views.setImageViewResource(R.id.imageView, imgTab[imgId]);

        Intent intent1 = new Intent(context, MyWidget.class);

        intent1.setAction("com.example.widgetapp.action.prevImg");
        PendingIntent piPrevImg = PendingIntent.getBroadcast(context,0,intent1,0);
        views.setOnClickPendingIntent(R.id.prev_image_button,piPrevImg);

        intent1.setAction("com.example.widgetapp.action.nextImg");
        PendingIntent piNextImg = PendingIntent.getBroadcast(context,0,intent1,0);
        views.setOnClickPendingIntent(R.id.next_image_button,piNextImg);
        // muzyka nie wiem czy dobrze
        intent1.setAction("com.example.widgetapp.action.startM");
        PendingIntent piStartM = PendingIntent.getBroadcast(context,0,intent1,0);
        views.setOnClickPendingIntent(R.id.start_button,piStartM);

        intent1.setAction("com.example.widgetapp.action.stopM");
        PendingIntent piStopM = PendingIntent.getBroadcast(context,0,intent1,0);
        views.setOnClickPendingIntent(R.id.stop_button,piStopM);

        intent1.setAction("com.example.widgetapp.action.nextM");
        PendingIntent piNextM = PendingIntent.getBroadcast(context,0,intent1,0);
        views.setOnClickPendingIntent(R.id.next_button,piNextM);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals("com.example.widgetapp.action.prevImg")){
            imgId--;
                if(imgId<0){
                    imgId=imgTab.length-1;
                }
        }
        else if(intent.getAction().equals("com.example.widgetapp.action.nextImg")){
            imgId++;
            if(imgId>=imgTab.length){
                imgId=0;
            }
        }// tutaj sie zaczyna muzyka ktora nie wiem czy robie odbrze
        else if(intent.getAction().equals("com.example.widgetapp.action.startM")){

            if(player== null){
                player=MediaPlayer.create(context,musicTab[musicId]);
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        releasePlayer();
                    }
                });
                player.start();
            }else{
                if(!player.isPlaying()){
                    player.start();
                }
            }
            Toast.makeText(context, "startM", Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction().equals("com.example.widgetapp.action.stopM")){

            if(player!=null){
                if(player.isPlaying()){
                    player.pause();
                }

            }
            Toast.makeText(context, "stopM", Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction().equals("com.example.widgetapp.action.nextM")){
            Toast.makeText(context, "nextM", Toast.LENGTH_SHORT).show();
            nextMusic(context);
        }
        else{
            //to musze jakos inaczej zrobic bo wchodzi tu nie potrzebnie
            Toast.makeText(context, "dupa nie działą", Toast.LENGTH_SHORT).show();
        }
        onUpdate(context);
    }

    private void onUpdate(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        onUpdateWorked = true;
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    private void releasePlayer(){
        if(player != null){
            player.release();
        }
    }
    private void nextMusic(Context context){
        if(player!=null){
            if(player.isPlaying()==false){
                addMusicId();
                player=MediaPlayer.create(context,musicTab[musicId]);
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        releasePlayer();
                    }
                });
                player.start();
            }else {
                addMusicId();
                player.reset();
                player=MediaPlayer.create(context,musicTab[musicId]);
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        releasePlayer();
                    }
                });
                player.start();
            }
        }else{
            addMusicId();
            player=MediaPlayer.create(context, musicTab[musicId]);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    releasePlayer();
                }
            });
            player.start();
        }
    }
    public void addMusicId(){
        if(musicId>= musicTab.length-1){
            musicId=0;
        }else{
            musicId++;
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

