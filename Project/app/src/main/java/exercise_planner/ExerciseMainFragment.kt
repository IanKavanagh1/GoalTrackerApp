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
import shared.Consts

class ExerciseMainFragment : Fragment(), SensorEventListener
{
    private var sensorManager: SensorManager? = null
    private var running = false

    // Step Sensor Variables
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var stepSensor: Sensor? = null

    // Calorie Burned Variables
    private var totalBurnedCalories = 0f

    // UI Variables
    private var stepCounterText: TextView? = null
    private var averageHeartRateText: TextView? = null
    private var checkHeartRateBtn: Button? = null
    private var calorieBurnedText: TextView? = null
    private var goToRunFragButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = FragmentExerciseMainBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        activity?.let {
            sensorManager = it.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }

        stepCounterText = view?.findViewById(R.id.stepCounter)
        checkHeartRateBtn = view?.findViewById(R.id.checkHeartRate)

        checkHeartRateBtn?.setOnClickListener { goToCheckHeartRateFragment() }

        goToRunFragButton = view?.findViewById(R.id.runButton)
        goToRunFragButton?.setOnClickListener { goToRunFragment() }

        averageHeartRateText = view?.findViewById(R.id.averageHeartRate)
        averageHeartRateText?.text = getString(R.string.shared_single_value_int, 73)

        calorieBurnedText = view?.findViewById(R.id.caloriesBurned)
        calorieBurnedText?.text = getString(R.string.calorie_value, 300.5)
    }

    override fun onResume() {
        super.onResume()

        activity?.let {
            running = true

            // Get and Set up Step Sensor
            stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

            if(stepSensor == null)
            {
                Toast.makeText(it,"Step Sensor Not Supported On This Device", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if(ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACTIVITY_RECOGNITION) !=
                    PackageManager.PERMISSION_GRANTED )
                {
                    // User has not granted permissions so request the permission and return false
                    requestPermissions(arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),1)

                    //User has not granted permissions, so do not add any listeners for the requested sensor
                    Toast.makeText(it,"ACTIVITY RECOGNITION PERMISSION REQUIRED", Toast.LENGTH_SHORT).show()
                    return
                }

                sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)
                Log.d("Exercise Fragment", "Registering Listener to Step Sensor")
            }
        }
    }

    override fun onPause() {
        super.onPause()

        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running)
        {
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            stepCounterText?.text = getString(R.string.shared_single_value_int, currentSteps)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    private fun resetData()
    {
        previousTotalSteps = totalSteps
        stepCounterText?.text = getString(R.string.shared_single_value_int, 0)
        saveData()
    }

    //TODO: Review how we are saving data from sensors
    private fun saveData()
    {
        activity?.let {
            var sharedPreferences = it.getSharedPreferences(Consts.USER_PREFS, AppCompatActivity.MODE_PRIVATE)
            var editor = sharedPreferences.edit()

            editor.apply {
                putFloat("steps", previousTotalSteps)
            }.apply()
        }
    }

    private fun loadData()
    {
        activity?.let {
            var sharedPreferences = it.getSharedPreferences(Consts.USER_PREFS, AppCompatActivity.MODE_PRIVATE)

            val savedStepCount = sharedPreferences.getFloat("steps", 0f)
            Log.d("Exercise", "Saved Steps : $savedStepCount")
            previousTotalSteps = savedStepCount
        }
    }

    private fun goToCheckHeartRateFragment()
    {
        activity?.let {
            if(ActivityCompat.checkSelfPermission(it, android.Manifest.permission.BODY_SENSORS) !=
                PackageManager.PERMISSION_GRANTED )
            {
                // User has not granted permissions so request the permission and return false
                requestPermissions(arrayOf(android.Manifest.permission.BODY_SENSORS),1)
                return
            }

            it.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, HeartBeatMonitorFragment(), "")
                ?.addToBackStack("true")?.commit()
        }
    }

    private fun goToRunFragment()
    {
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

        if(requestCode == 1)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                activity?.let {
                    it.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, HeartBeatMonitorFragment(), "")
                        ?.addToBackStack("true")?.commit()
                }
            }
            else
            {
                activity?.let {
                    //User has not granted permissions, so do not add any listeners for the requested sensor
                    Toast.makeText(it,"BODY SENSOR PERMISSION REQUIRED", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}