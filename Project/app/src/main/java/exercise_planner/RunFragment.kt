package exercise_planner

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
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
import com.example.goal_tracker.LoadingDialog
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentRunBinding
import database.ExerciseDatabaseOpenHelper
import shared.Consts
import java.math.RoundingMode
import java.util.concurrent.TimeUnit
import kotlin.math.*

class RunFragment : Fragment()
{
    private lateinit var startRunBtn: Button
    private lateinit var stopRunBtn: Button
    private lateinit var runTimeTextView: TextView
    private lateinit var totalDistanceTextView: TextView

    private var locationManager: LocationManager? = null

    private var startTime: Long = 0
    private var endTime: Long = 0
    private var totalRunTime: Long = 0

    private var startLongitude: Double = 0.0
    private var startLatitude: Double = 0.0
    private var endLongitude: Double = 0.0
    private var endLatitude: Double = 0.0
    private var totalDistanceCovered: Double = 0.0

    private var startLocationListener: LocationListener? = null
    private var endLocationListener: LocationListener? = null

    private lateinit var exerciseDatabaseOpenHelper: ExerciseDatabaseOpenHelper

    private var userId = 0

    private lateinit var loadingDialog: LoadingDialog

    // Database name
    private val dbName = Consts.EXERCISE_DATABASE + ".db"

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val binding = FragmentRunBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        activity?.let {
            locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            loadingDialog = LoadingDialog(it)

            exerciseDatabaseOpenHelper = ExerciseDatabaseOpenHelper(it, dbName, null, 1)

            val sharedPreferences = it.getSharedPreferences(Consts.USER_PREFS, AppCompatActivity.MODE_PRIVATE)
            userId = sharedPreferences.getInt(Consts.PREFS_USER_ID, -1)
        }

        startRunBtn = view!!.findViewById(R.id.startRunBtn)
        stopRunBtn = view!!.findViewById(R.id.stopRunBtn)

        runTimeTextView = view!!.findViewById(R.id.runTime)
        totalDistanceTextView = view!!.findViewById(R.id.distanceRan)
        totalDistanceTextView.text = getString(R.string.distance_value, 0.0)

        runTimeTextView.text = getString(R.string.shared_single_value_int, 0)

        startRunBtn.setOnClickListener { startRun() }
        stopRunBtn.setOnClickListener { stopRun()  }
    }

    private fun startRun()
    {
        activity?.let{
            if (ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            {
                // User has not granted permissions so request the permission and return
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1
                )

                //User has not granted permissions, so do not add any listeners for the requested sensor
                Toast.makeText(it,"LOCATION ACCESS PERMISSION REQUIRED", Toast.LENGTH_SHORT).show()
                return
            }
        }

        startLocationListener = LocationListener { p0 ->
            startLongitude = p0.longitude
            startLatitude = p0.latitude

            // Once we have a location, we can remove the listener
            if(startLatitude > 0 || startLatitude < 0  && startLongitude > 0 || startLongitude < 0) {
                Log.d("Start Run", "Location Acquired, Removing Listener")
                locationManager?.removeUpdates(startLocationListener!!)
            }
        }

        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            5000,
            0f,
            startLocationListener!!)

        startTime = System.currentTimeMillis()
        runTimeTextView.text = getString(R.string.shared_single_value_int, 0)
    }

    private fun stopRun()
    {
        // Start Loading Dialog
        loadingDialog.startLoadingDialog()

        activity?.let {
            if (ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            {
                // User has not granted permissions so request the permission and return
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1
                )

                //User has not granted permissions, so do not add any listeners for the requested sensor
                Toast.makeText(it, "LOCATION ACCESS PERMISSION REQUIRED", Toast.LENGTH_SHORT).show()
                return
            }
        }

        endLocationListener = LocationListener { p0 ->
            endLongitude = p0.longitude
            endLatitude = p0.latitude

            // Once we have a location, we can remove the listener
            if(endLatitude > 0 || endLatitude < 0  && endLongitude > 0 || endLongitude < 0) {
                Log.d("Stop Run", "Location Acquired, Removing Listener")

                //Only calculate distance once we have received the end location
                val distance = calculateTotalDistance(startLatitude, endLatitude, startLongitude,
                    endLongitude)

                // Display the total distance covered
                totalDistanceTextView.text = getString(R.string.distance_value, distance)

                // Unsubscribe from event listener
                locationManager?.removeUpdates(endLocationListener!!)

                // Add data to database
                exerciseDatabaseOpenHelper.insertData(startLatitude, startLongitude, endLatitude,
                    endLongitude, distance, totalRunTime, userId)

                // Remove Loading Dialog
                loadingDialog.dismissDialog()
            }
        }

        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            5000,
            0f,
            endLocationListener!!)

        // Work out the total time it took for the run
        endTime = System.currentTimeMillis()
        totalRunTime = endTime - startTime

        // Breakdown and display the time into Hours, Minutes and Seconds
        val hours = TimeUnit.MILLISECONDS.toHours(totalRunTime) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(totalRunTime) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(totalRunTime) % 60

        // Only display what is needed, i.e if hours and minutes are 0 only so seconds
        if(hours == 0L && minutes == 0L)
        {
            runTimeTextView.text = getString(R.string.time_seconds_formatter, seconds)
        }
        // only show minutes and seconds
        else if(hours == 0L && minutes > 0L)
        {
            runTimeTextView.text = getString(R.string.time_minutes_seconds_formatter, minutes, seconds)
        }
        // show hours minutes and seconds
        else
        {
            runTimeTextView.text = getString(R.string.time_hours_minutes_seconds_formatter, hours,
                minutes, seconds)
        }
    }

    // Function to workout the total distance between the start and end location
    // using the latitude and longitude values received from the sensors
    private fun calculateTotalDistance(startLat: Double, endLat: Double, startLon: Double, endLon: Double) : Double
    {
        // Convert from degrees to radians
        val startLatRad = Math.toRadians(startLat)
        val endLatRad = Math.toRadians(endLat)
        val startLonRad = Math.toRadians(startLon)
        val endLonRad = Math.toRadians(endLon)

        // Haversine Formula
        val dLat = endLatRad - startLatRad
        val dLon = endLonRad - startLonRad

        val a = sin(dLat / 2).pow(2.0) +
                cos(startLatRad) * cos(endLatRad) *
                sin(dLon / 2).pow(2.0)

        val c = 2 * asin(sqrt(a))

        // Radius of earth is 6371 km
        val r = 6371

        totalDistanceCovered = c * r

        return totalDistanceCovered.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }
}