package com.ziadsyahrul.sub2bfaa

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ziadsyahrul.sub2bfaa.databinding.ActivitySettingBinding
import com.ziadsyahrul.sub2bfaa.reminder.AlarmReceiver

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var sharedPreference: SharedPreferences

    companion object{
        const val SHARED_PREFERENCE = "sharedpreference"
        const val BOOLEAN_KEY = "booleankey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReceiver()
        sharedPreference = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)

        setAlarm()
        binding.alarmReminderSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                alarmReceiver.setAlarmReminder(this, AlarmReceiver.TYPE_REPEATING, "Daily Reminder Notification XIXI")
            }else{
                alarmReceiver.cancelReminder(this)
            }
            saveChange(isChecked)
        }

    }
    private fun saveChange(checked: Boolean) {
        val editor = sharedPreference.edit()
        editor.putBoolean(BOOLEAN_KEY, checked)
        editor.apply()
    }

    private fun setAlarm() {
        binding.alarmReminderSwitch.isChecked = sharedPreference.getBoolean(BOOLEAN_KEY, false)
    }
}