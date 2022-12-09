package exercise_planner

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentExerciseMainBinding
import database.HeartRateDatabaseOpenHelper
import shared.Consts

class ExerciseMainFragment : Fragment(), SensorEventListener
{
    private var sensorManager: SensorManager? = null
    private var running = false
    private var heartRateDatabaseOpenHelper: HeartRateDatabaseOpenHelper? = null

    // Step Sensor Variables
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var stepSensor: Sensor? = null

    // UI Variables
    private var stepCounterText: TextView? = null
    private var averageHeartRateText: TextView? = null
    private var checkHeartRateBtn: Button? = null
    private var goToRunFragButton: Button? = null

    // Database name
    private val dbName = Consts.HEART_RATE_DATABASE + ".db"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentExerciseMainBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        activity?.let {

            // set up sensor manager
            sensorManager = it.getSystemService(Context.SENSOR_SERVICE) as SensorManager

            // set up heart rate database
            heartRateDatabaseOpenHelper = HeartRateDatabaseOpenHelper(it, dbName, null, 1)
        }

        stepCounterText = view?.findViewById(R.id.stepCounter)
        checkHeartRateBtn = view?.findViewById(R.id.checkHeartRate)

        checkHeartRateBtn?.setOnClickListener { goToCheckHeartRateFragment() }

        goToRunFragButton = view?.findViewById(R.id.runButton)
        goToRunFragButton?.setOnClickListener { goToRunFragment() }

        averageHeartRateText = view?.findViewById(R.id.averageHeartRate)
        averageHeartRateText?.text = getString(R.string.shared_single_value_int, calculateAverageHeartRate())
    }

    override fun onResume() {
        super.onResume()

        activity?.let {
            running = true

            // Get and Set up Step Sensor
            stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

            // verify the device has the step sensor
            if(stepSensor == null)
            {
                // Display notification if it does not
                Toast.makeText(it,"Step Sensor Not Supported On This Device", Toast.LENGTH_SHORT).show()
            }
            else
            {
                // verify the user has granted permission to the Activity Recognition permission
                if(ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACTIVITY_RECOGNITION) !=
                    PackageManager.PERMISSION_GRANTED )
                {
                    // User has not granted permissions so request the permission and return false
                    requestPermissions(arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),1)

                    //User has not granted permissions, so do not add any listeners for the requested sensor
                    Toast.makeText(it,"ACTIVITY RECOGNITION PERMISSION REQUIRED", Toast.LENGTH_SHORT).show()
                    return
                }

                // once we have confirmed permission is granted we can register the listener
                sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)
                Log.d("Exercise Fragment", "Registering Listener to Step Sensor")
            }
        }
    }

    override fun onPause() {
        super.onPause()

        // remove listener
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running)
        {
            // grab the total steps from the senor and display
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            stepCounterText?.text = getString(R.string.shared_single_value_int, currentSteps)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    private fun goToCheckHeartRateFragment()
    {
        activity?.let {

            // verify the user has granted permission to body sensors
            if(ActivityCompat.checkSelfPermission(it, android.Manifest.permission.BODY_SENSORS) !=
                PackageManager.PERMISSION_GRANTED )
            {
                // User has not granted permissions so request the permission and return false
                requestPermissions(arrayOf(android.Manifest.permission.BODY_SENSORS),1)
                return
            }

            // once verified we have permission, transition to the heart rate UI
            it.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, HeartBeatMonitorFragment(), "")
                ?.addToBackStack("true")?.commit()
        }
    }

    private fun goToRunFragment()
    {
        // transition to the Run UI
        activity?.let {
            it.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frameLayout, RunFragment(), "")
                ?.addToBackStack("true")?.commit()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // check the request permission result
        if(requestCode == 1)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // if we have been given permission we can transition to the HeartRate UI
                activity?.let {
                    it.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, HeartBeatMonitorFragment(), "")
                        ?.addToBackStack("true")?.commit()
                }
            }
            else
            {
                activity?.let {
                    //User has not granted permissions, so do not add any listeners for the requested sensor
                    // and display a toast letting user know that permission is required
                    Toast.makeText(it,"BODY SENSOR PERMISSION REQUIRED", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Returns the average heart rate for the logged in user
    private fun calculateAverageHeartRate() : Int
    {
        activity?.let {
            val sharedPreferences = it.getSharedPreferences(Consts.USER_PREFS, AppCompatActivity.MODE_PRIVATE)
            val userId = sharedPreferences.getInt(Consts.PREFS_USER_ID, -1)

            // gran the user heart rate data from the heart rate database
            val data = heartRateDatabaseOpenHelper?.getUserHeartRateData(userId)

            var averageHeartRate = 0

            // loop through the data list
            for(i in 0 until data!!.size)
            {
                // add all heart rate values
                averageHeartRate += data[i]
            }

            // check that the size is greater than 0 so that we don't divide by 0
            if( data.size > 0 )
            {
                // work out the average and return
                return averageHeartRate / data.size
            }
        }
        return 0
    }
}