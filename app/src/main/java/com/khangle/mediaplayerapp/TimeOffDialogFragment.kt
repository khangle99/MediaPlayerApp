package com.khangle.mediaplayerapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.khangle.mediaplayerapp.databinding.FragmentTimeOffDialogBinding
import com.khangle.mediaplayerapp.media.MusicService
import com.khangle.mediaplayerapp.media.TIME_OFF_ACTION
import java.util.concurrent.TimeUnit


class TimeOffDialogFragment() : DialogFragment() {



    lateinit var binding: FragmentTimeOffDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_off_dialog, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelBtn.setOnClickListener { dismiss() }
        binding.fiveMinBtn.setOnClickListener {
            setAlarm(5)
            dismiss()
        }
        binding.tenMinBtn.setOnClickListener {
            setAlarm(10)
            dismiss()
        }
        binding.fiftMinBtn.setOnClickListener {
            setAlarm(15)
            dismiss()
        }
        binding.fityMinBtn.setOnClickListener {
            setAlarm(30)
            dismiss()
        }
        binding.confirmBtn.setOnClickListener {
            val minutes = binding.customMinutesEditText.text.toString().toInt()
            setAlarm(minutes)
            dismiss()
        }
    }
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent


    private fun setAlarm(time: Int) {

        alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, MusicService.TimeOffBroadcastReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }
        val intent = Intent().also { intent ->
            intent.setAction(TIME_OFF_ACTION)
        }
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmMgr?.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + TimeUnit.MINUTES.toMillis(time.toLong()),
            alarmIntent
        )
    }


}