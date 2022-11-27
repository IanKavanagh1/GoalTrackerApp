package exercise_planner

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentHeartBeatMonitorBinding

class HeartBeatMonitorFragment : Fragment(), SensorEventListener
{
    private var sensorManager: SensorManager? = null

    // Heartbeat Sensor Variables
    private var heartRateSensor: Sensor? = null
    private var heartRateText: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = FragmentHeartBeatMonitorBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        activity?.let {
            sensorManager = it.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }

        heartRateText = view?.findViewById(R.id.currentHeartRateText)
        heartRateText?.text = getString(R.string.shared_single_value_int, 0)
    }

    override fun onResume() {
        super.onResume()

        activity?.let {

            // Get and Set up Heartbeat Sensor
            heartRateSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_HEART_RATE)

            if(heartRateSensor == null)
            {
                Toast.makeText(it,"Step Sensor Not Supported On This Device", Toast.LENGTH_SHORT).show()
            }
            else
            {
                sensorManager?.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_FASTEST)
                Log.d("Exercise Fragment", "Registering Listener to HeartBeat Sensor")
            }
        }
    }

    override fun onPause() {
        super.onPause()

        sensorManager?.unregisterListener(this, heartRateSensor)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        val currentHeartRate = event!!.values[0].toInt()
        heartRateText?.text = getString(R.string.shared_single_value_int, currentHeartRate)
        Log.d("Exercise Fragment", "Current Heart Rate : $currentHeartRate")
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}