package com.example.myapplication.presentation

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.Calendar


class MainActivity : ComponentActivity() {


    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (intent.getStringExtra("startChallenge") != null) {
            val ss: String = intent.getStringExtra("startChallenge").toString()
            if (ss == "start") {
                val intent = Intent(this@MainActivity, StartChallengeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = Color.White
                ) {
                    InitiateUI()
                }
            }
        }
    }
}

@Composable
@Preview
fun InitiateUI() {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()

    var sHour by remember { mutableIntStateOf(0) }
    var sMinute by remember { mutableIntStateOf(0) }

    var h1 by remember { mutableIntStateOf(0) }
    var h2 by remember { mutableIntStateOf(0) }
    var m1 by remember { mutableIntStateOf(0) }
    var m2 by remember { mutableIntStateOf(0) }

    val timeLeft by remember { mutableStateOf(30) }


    // Creating a TimePicker dialog
    val mTimePickerDialog = TimePickerDialog(
        mContext, { _, mHour: Int, mMinute: Int ->
            sHour = mHour
            sMinute = mMinute
            val hour = mHour.toDigits()
            if (mHour.toString().length == 1) {
                h1 = 0
                h2 = hour[0]
            } else {
                h1 = hour[0]
                h2 = hour[1]
            }
            val min = mMinute.toDigits()
            if (mMinute.toString().length == 1) {
                m1 = 0
                m2 = min[0]
            } else {
                m1 = min[0]
                m2 = min[1]
            }
        }, mCalendar[Calendar.HOUR_OF_DAY], mCalendar[Calendar.MINUTE], true
    )


    Column {
        Box(
            modifier = Modifier
                .requiredHeight(80.dp)
                .fillMaxWidth()
                .background(color = Color(0xFFFF5722))
        ) {

        }
        Column(
            modifier = Modifier
                .padding(5.dp)
                .requiredHeight(250.dp)
                .clip(shape = RoundedCornerShape(4))
                .background(color = Color(0xFFE2E2E2))
        ) {

            Row(
                modifier = Modifier
                    .requiredHeight(55.dp)
                    .fillMaxWidth()
                    .background(color = Color(0xFFE2E2E2)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Left
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .requiredWidth(80.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "00: $timeLeft",
                            fontStyle = FontStyle.Normal,
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(50.dp, 0.dp, 20.dp, 0.dp))

                Text(
                    text = "FLAGS CHALLENGE",
                    fontStyle = FontStyle.Normal,
                    fontSize = 20.sp,
                    color = Color(0xFFFF5722),
                    fontWeight = FontWeight.Bold
                )
            }
            Divider(color = Color(0xFFC7C3C3), thickness = Dp(1F))

            SetChallengeTime({
                mTimePickerDialog.show()
            }, mContext, onSave = {
                setTimer(mainActivity = mContext, sHour, sMinute)
            }, h1, h2, m1, m2)
        }
    }
}

@Composable
fun SetChallengeTime(
    onClick: () -> Unit, mContext: Context, onSave: () -> Unit, h1: Int, h2: Int, m1: Int, m2: Int
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "SCHEDULE", fontSize = 25.sp, color = Color.Black, fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Row(modifier = Modifier.clickable { onClick() }

        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hour",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )

                Row {
                    Column(
                        modifier = Modifier
                            .size(65.dp)
                            .padding(5.dp, 5.dp, 3.dp, 5.dp)
                            .background(
                                Color(0xFFD5D5D5), shape = RoundedCornerShape(15.dp)
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$h1",
                            fontSize = 18.sp,
                            color = Color(0xFF777373),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Column(
                        modifier = Modifier
                            .size(65.dp)
                            .padding(0.dp, 5.dp, 10.dp, 5.dp)
                            .background(Color(0xFFD5D5D5), shape = RoundedCornerShape(15.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$h2",
                            fontSize = 18.sp,
                            color = Color(0xFF777373),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Minute",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )

                Row {
                    Column(
                        modifier = Modifier
                            .size(65.dp)
                            .padding(5.dp, 5.dp, 3.dp, 5.dp)
                            .background(Color(0xFFD5D5D5), shape = RoundedCornerShape(15.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$m1",
                            fontSize = 18.sp,
                            color = Color(0xFF777373),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Column(
                        modifier = Modifier
                            .size(65.dp)
                            .padding(0.dp, 5.dp, 10.dp, 5.dp)
                            .background(Color(0xFFD5D5D5), shape = RoundedCornerShape(15.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$m2",
                            fontSize = 18.sp,
                            color = Color(0xFF777373),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Second",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )

                Row {
                    Column(
                        modifier = Modifier
                            .size(65.dp)
                            .padding(5.dp, 5.dp, 3.dp, 5.dp)
                            .background(Color(0xFFD5D5D5), shape = RoundedCornerShape(15.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "0",
                            fontSize = 18.sp,
                            color = Color(0xFF777373),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Column(
                        modifier = Modifier
                            .size(65.dp)
                            .padding(0.dp, 5.dp, 10.dp, 5.dp)
                            .background(Color(0xFFD5D5D5), shape = RoundedCornerShape(15.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "0",
                            fontSize = 18.sp,
                            color = Color(0xFF777373),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
        Button(
            onClick = { onSave()
                      Toast.makeText(mContext,"Time Saved.",Toast.LENGTH_LONG).show()},
            modifier = Modifier
                .requiredWidth(150.dp)
                .requiredHeight(55.dp)
                .padding(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF5722), contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Save")
        }
    }
}


@SuppressLint("ScheduleExactAlarm")
fun setTimer(mainActivity: Context, mHour: Int, mMinute: Int) {
    Log.e("TimeH", mHour.toString())
    Log.e("minute", mMinute.toString())
    val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, mHour)
        set(Calendar.MINUTE, mMinute)
    }
    val alarmManager = mainActivity.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    val intent = Intent(mainActivity, AlarmReceiver::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(mainActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    alarmManager?.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
    )

}

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("Alarm", "received")
        val activityIntent = Intent(context, MainActivity::class.java)
        activityIntent.setComponent(
            ComponentName(
                "com.example.myapplication",
                "com.example.myapplication.presentation.MainActivity"
            )
        )
        activityIntent.putExtra("startChallenge", "start")
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        activityIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        context?.startActivity(activityIntent)
    }
}

fun Int.toDigits(): List<Int> = toString().map { it.toString().toInt() }