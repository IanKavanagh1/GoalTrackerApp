package exercise_planner

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentHeartBeatMonitorBinding
import database.HeartRateDatabaseOpenHelper
import shared.Consts

class HeartBeatMonitorFragment : Fragment(), SensorEventListener
{
    private var sensorManager: SensorManager? = null

    // Heartbeat Sensor Variables
    private var heartRateSensor: Sensor? = null
    private var heartRateText: TextView? = null

    private var heartRateDatabaseOpenHelper: HeartRateDatabaseOpenHelper? = null

    private var userId = 0

    // Database name
    private val dbName = Consts.HEART_RATE_DATABASE + ".db"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentHeartBeatMonitorBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        activity?.let {

            // set up sensor manager
            sensorManager = it.getSystemService(Context.SENSOR_SERVICE) as SensorManager

            // set up heart rate database
            heartRateDatabaseOpenHelper = HeartRateDatabaseOpenHelper(it, dbName, null, 1)

            // grab userId from shared prefs
            val sharedPreferences = it.getSharedPreferences(Consts.USER_PREFS, AppCompatActivity.MODE_PRIVATE)

            userId = sharedPreferences.getInt(Consts.PREFS_USER_ID, -1)
        }

        heartRateText = view?.findViewById(R.id.currentHeartRateText)
        heartRateText?.text = getString(R.string.shared_single_value_int, 0)
    }

    override fun onResume() {
        super.onResume()

        activity?.let {

            // Get and Set up Heartbeat Sensor
            heartRateSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_HEART_RATE)

            // verify the device has the hear rate sensor
            if(heartRateSensor == null)
            {
                // display notification if the device does not support the heart rate sensor
                Toast.makeText(it,"Step Sensor Not Supported On This Device", Toast.LENGTH_SHORT).show()
            }
            else
            {
                // add listener to heart rate sensor
                sensorManager?.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_FASTEST)
                Log.d("Exercise Fragment", "Registering Listener to HeartBeat Sensor")
            }
        }
    }

    override fun onPause() {
        super.onPause()

        // remove listener
        sensorManager?.unregisterListener(this, heartRateSensor)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        // grab the heart rate from the sensor
        val currentHeartRate = event!!.values[0].toInt()
        // display the current heart rate to the user
        heartRateText?.text = getString(R.string.shared_single_value_int, currentHeartRate)
        Log.d("Exercise Fragment", "Current Heart Rate : $currentHeartRate")

        // Only add heart rate once the sensor has picked up a reading
        // to avoid adding a lot of 0's while the sensor is trying to get a reading
        if( currentHeartRate > 0 )
        {
            // store the heart rate to the database
            heartRateDatabaseOpenHelper?.insertData(currentHeartRate, userId)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}