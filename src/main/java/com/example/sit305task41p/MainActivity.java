package com.example.sit305task41p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  // History display.
  private TextView workout_history_dialog;
  private EditText workout_type_input;

  // Clock.
  boolean running;
  private Chronometer timer;
  private long pausedTime;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    workout_type_input = (EditText)findViewById(R.id.workout_type_input);
    workout_history_dialog = (TextView)findViewById(R.id.workoutHistory);
    workout_history_dialog.setText(readData());

    timer = (Chronometer)findViewById(R.id.timer);
    pausedTime = 0;
  }

  public void startTimer(View view) {
    // Starts and reset clock.
    running = true;
    timer.setBase(SystemClock.elapsedRealtime() - pausedTime);
    timer.start();
  }

  public void pausedTimer(View view) {
    if(running) {
      // Pauses and holds display.
      running = false;
      pausedTime = SystemClock.elapsedRealtime() - timer.getBase();
      timer.stop();
    }
  }

  public void stopTimer(View view) {
    // Checks to see if there is input before submiting.
    if (workout_type_input.getText().toString().isEmpty()) {
      Toast.makeText(this, "Enter activity", Toast.LENGTH_LONG).show();
    }
    else {
      running = false;
      pausedTime = 0;

      // Makes output for the previous time.
      long timeTaken = SystemClock.elapsedRealtime() - timer.getBase();
      long second = (timeTaken / 1000) % 60;
      long minute = (timeTaken / (1000 * 60)) % 60;
      String timeStr = String.format("You spent %02d:%02d on %s last time.",  minute, second, workout_type_input.getText().toString());

      timer.setBase(SystemClock.elapsedRealtime());
      timer.stop();

      workout_history_dialog.setText(timeStr);
      writeData(timeStr);
    }
  }

  public String readData() {
      // Reads data in from previous session.
      SharedPreferences mPrefs = getSharedPreferences("label", 0);
      String mString = mPrefs.getString("history", "00:00 on push ups");
      return mString;
  }

  public void writeData(String data) {
      // Writes data of current session.
      SharedPreferences mPrefs = getSharedPreferences("label", 0);
      SharedPreferences.Editor mEditor = mPrefs.edit();
      mEditor.putString("history", data).commit();
  }
}
