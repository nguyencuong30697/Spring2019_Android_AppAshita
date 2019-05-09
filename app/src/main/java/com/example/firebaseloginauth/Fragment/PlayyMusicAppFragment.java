package com.example.firebaseloginauth.Fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseloginauth.Adapter.CustomCommentAdapter;
import com.example.firebaseloginauth.DTO.CommentDTO;
import com.example.firebaseloginauth.DTO.MusicDTO;
import com.example.firebaseloginauth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayyMusicAppFragment extends Fragment  {

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://fir-loginauth-3808b.appspot.com");
    private FirebaseAuth mAuth;
    EditText editComment;
    Button btnPost;
    List<MusicDTO> list_music;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference myRefComment;
    TextView txtTitle, txtTimeStart, txtTimeTotal;
    SeekBar seekBar;
    ImageButton btnBack, btnPlay, btnStop, btnNext;
    ImageView imXoay;
    int positionMusicMain = 0;
    MediaPlayer mediaPlayer;
    public static Boolean stopMusic = true;
    Animation animation;
    Handler handler;
    List<CommentDTO> list_music_comment;
    ListView listViewComment;
    CustomCommentAdapter adapterComment;
    long NumberOfComment = 0;
    Context context;
    ScrollView scrollView2;
    int d = 0;


//    SensorManager sensorManager;
//    Sensor cambiengiatoc;
//    float lastX,lastY,lastZ;
//    long lastTime = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playmusic,container,false);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtTimeStart = view.findViewById(R.id.txtTimeStart);
        txtTimeTotal = view.findViewById(R.id.txtTimeTotal);
        seekBar = view.findViewById(R.id.seekBarSong);
        btnBack = view.findViewById(R.id.btnBack);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnStop = view.findViewById(R.id.btnStop);
        btnNext = view.findViewById(R.id.btnNext);
        imXoay = view.findViewById(R.id.imXoay);
        btnPost = view.findViewById(R.id.btnPost);
        editComment = view.findViewById(R.id.editComment);
        scrollView2 = view.findViewById(R.id.scrollView2);



        context = getActivity().getApplicationContext();


//        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
//        cambiengiatoc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        if (cambiengiatoc == null){
//            Toast.makeText(context,"Ko sd dc",Toast.LENGTH_LONG).show();
//        }else{
//            sensorManager.registerListener(this,cambiengiatoc,SensorManager.SENSOR_DELAY_NORMAL);
//        }


        handler = new Handler();

        animation = AnimationUtils.loadAnimation(getActivity(),R.anim.dis_rotate);

        Bundle b = getArguments();
        if (b!= null){
            positionMusicMain = b.getInt("position");
        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionMusicMain--;
                if(positionMusicMain<0){
                    positionMusicMain= list_music.size() -1;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                KhoiTaoMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                setTimeTotal();
                updateTimeSong();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){//Nếu đang phát -> pause
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play_button);
                }else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
                setTimeTotal();
                updateTimeSong();
                imXoay.startAnimation(animation);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionMusicMain++;
                if(positionMusicMain>list_music.size()-1){
                    positionMusicMain= 0;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                KhoiTaoMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                setTimeTotal();
                updateTimeSong();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                btnPlay.setImageResource(R.drawable.play_button);

                animation.setDuration(0);
                imXoay.startAnimation(animation);
                KhoiTaoMediaPlayer();

            }
        });



        listViewComment = view.findViewById(R.id.list_commment);

        listViewComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

// Disallow the touch request for parent scroll on touch of child view
                scrollView2.requestDisallowInterceptTouchEvent(true);

                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        scrollView2.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });


        list_music_comment = new ArrayList<CommentDTO>();



        list_music = new ArrayList<MusicDTO>();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("music");
            getDataInFireBase();


        }

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sComment = editComment.getText().toString();
                String nameUser = user.getDisplayName().toString();
                myRefComment.child(String.valueOf(NumberOfComment)).setValue(new  CommentDTO(nameUser,sComment));
                editComment.setText("");
                getCommentDataInFireBase();
            }
        });

        return view;
    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if(stopMusic){
//
//            cambiengiatoc = event.sensor;
//            if(cambiengiatoc.getType() == Sensor.TYPE_ACCELEROMETER){
//
//                float x = event.values[0];
//                float y = event.values[1];
//                float z = event.values[2];
//
//                long currentTimeSensor = System.currentTimeMillis();
//                if((currentTimeSensor-lastTime) > 100){
//                    long diffTime = currentTimeSensor - lastTime;
//                    lastTime = currentTimeSensor;
//                    float speed = Math.abs(x+y+z-lastX-lastY-lastZ)/diffTime * 10000;
//                    if(speed>2000) {
//                            if(lastX>x){
//                                Log.d("222/22",stopMusic+"/");
//                                positionMusicMain++;
//                                if(positionMusicMain>list_music.size()-1){
//                                    positionMusicMain= 0;
//                                }
//                                if(mediaPlayer.isPlaying()){
//                                    mediaPlayer.stop();
//                                }
//                                mediaPlayer.release();
//                                KhoiTaoMediaPlayer();
//                                mediaPlayer.start();
//                                btnPlay.setImageResource(R.drawable.pause);
//                                setTimeTotal();
//                                updateTimeSong();
//                        }
//                    }
//                    lastX = x;
//                    lastY = y;
//                    lastZ = z;
//                }
//            }
//        }else{
//            sensorManager.unregisterListener(this);
//        }
//    }

    public void getDataInFireBase(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getChildrenCount() == 0){
                }else{
                    for(DataSnapshot dataS : dataSnapshot.getChildren()){
                        MusicDTO musicDTO1 = new MusicDTO();
                        musicDTO1 = dataS.getValue(MusicDTO.class);
                        list_music.add(musicDTO1);
                    }
                    KhoiTaoMediaPlayer();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Data    ", "Failed to read value.", error.toException());
            }
        });
    }

    public void getCommentDataInFireBase(){
        myRefComment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getChildrenCount() == 0){
                }else{
                    NumberOfComment = dataSnapshot.getChildrenCount();
                    list_music_comment.clear();
                    for(DataSnapshot dataS : dataSnapshot.getChildren()){
                        CommentDTO commentDTO = new CommentDTO();
                        commentDTO = dataS.getValue(CommentDTO.class);
                        list_music_comment.add(commentDTO);
                    }
                    adapterComment = new CustomCommentAdapter(context,R.layout.layout_custom_list_comment,list_music_comment);
                    listViewComment.setAdapter(adapterComment);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Data    ", "Failed to read value.", error.toException());
            }
        });
    }

    private void updateTimeSong(){

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (stopMusic) {
                        SimpleDateFormat timeUpdate = new SimpleDateFormat("mm:ss");
                        txtTimeStart.setText(timeUpdate.format(mediaPlayer.getCurrentPosition()));
                        // update progress của seekbar
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        //kiểm tra time khi kết thúc -> next
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                positionMusicMain++;
                                if (positionMusicMain > list_music.size() - 1) {
                                    positionMusicMain = 0;
                                }
                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();
                                }
                                KhoiTaoMediaPlayer();
                                mediaPlayer.start();
                                btnPlay.setImageResource(R.drawable.pause);
                                setTimeTotal();
                                updateTimeSong();
                            }
                        });
                        handler.postDelayed(this, 1000);
                    }else{
                        handler.removeCallbacksAndMessages(null);
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }



                }
            }, 100);
    }

    public void KhoiTaoMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(list_music.get(positionMusicMain).getLinkMusic());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtTitle.setText(list_music.get(positionMusicMain).getDisplayname());
        Uri myUri = Uri.parse(list_music.get(positionMusicMain).getPhotoMusic());
        Picasso.with(getActivity()).load(myUri).into(imXoay);
        animation.setDuration(mediaPlayer.getDuration());
        myRefComment = database.getReference("comment").child(list_music.get(positionMusicMain).getIdMusic());
        getCommentDataInFireBase();
    }

    private void setTimeTotal(){
        SimpleDateFormat time = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(time.format(mediaPlayer.getDuration()));
        //gán max của seekBar = mediaplayer.getDuration()
        seekBar.setMax(mediaPlayer.getDuration());


    }


}
