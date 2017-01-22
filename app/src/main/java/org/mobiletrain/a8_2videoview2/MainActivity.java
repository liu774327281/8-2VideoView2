package org.mobiletrain.a8_2videoview2;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView currentTime;
    private TextView totalTime;
    private VideoView videoView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
    private SeekBar seekBar;
    private boolean isPlaying = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isPlaying) {
                //videoView.getCurrentPosition()表示获取当前播放的位置
                currentTime.setText(dateFormat.format(new Date(videoView.getCurrentPosition())));
                //设置SeekBar当前的进度
                seekBar.setProgress(videoView.getCurrentPosition());
                //每隔1秒发送一条空消息
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentTime = ((TextView) findViewById(R.id.show_current_time));
        totalTime = ((TextView) findViewById(R.id.show_total_time));
        seekBar = ((SeekBar) findViewById(R.id.seek_bar));
        videoView = (VideoView) findViewById(R.id.video_view);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "曾经的你.mp4");
        videoView.setVideoURI(Uri.parse(file.getAbsolutePath()));
        //设置seekbar的滑动监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //fromUser表示SeekBar的进度条的改变是否有互用手动触发
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //视频播放跳转到某一个时间点
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //暂停播放
                videoView.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPlaying = false;
    }

    public void play(View view) {
        videoView.start();
        isPlaying = true;
        //获取视频文件的总长度，只能在视频播放时获取视频总长度
        int duration = videoView.getDuration();
        totalTime.setText(dateFormat.format(new Date(duration)));
        seekBar.setMax(duration);
        mHandler.sendEmptyMessage(0);

    }
}
