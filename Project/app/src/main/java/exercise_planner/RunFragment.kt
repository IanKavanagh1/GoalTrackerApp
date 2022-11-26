package exercise_planner

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentRunBinding
import java.math.RoundingMode
import kotlin.math.*

class RunFragment : Fragment()
{
    private var startRunBtn: Button? = null
    private var stopRunBtn: Button? = null
    private var startLonTextView: TextView? = null
    private var startLatTextView: TextView? = null
    private var runTimeTextView: TextView? = null
    private var totalDistanceTextView: TextView? = null

    private var locationManager: LocationManager? = null

    private var startTime: Long = 0
    private var endTime: Long = 0
    private var totalRunTime: Long = 0

    private var startLongitude: Double = 0.0
    private var startLatitude: Double = 0.0
    private var endLongitude: Double = 0.0
    private var endLatitude: Double = 0.0
    private var totalDistanceCovered: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = FragmentRunBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        activity?.let {
            locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        startRunBtn = view?.findViewById(R.id.startRunBtn)
        stopRunBtn = view?.findViewById(R.id.stopRunBtn)

        startLatTextView = view?.findViewById(R.id.startLat)
        startLonTextView = view?.findViewById(R.id.startLon)

        runTimeTextView = view?.findViewById(R.id.runTime)
        totalDistanceTextView = view?.findViewById(R.id.distanceRan)
        totalDistanceTextView?.text = getString(R.string.distance_value, 0.0)

        startRunBtn?.setOnClickListener { startRun() }
        stopRunBtn?.setOnClickListener { stopRun()  }
    }

    private fun startRun() {

        activity?.let {
            if (ActivityCompat.checkSelfPermission(it,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            {
                // User has not granted permissions so request the permission and return
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1
                )

                //User has not granted permissions, so do not add any listeners for the requested sensor
                Toast.makeText(it,"LOCATION ACCESS PERMISSION REQUIRED", Toast.LENGTH_SHORT).show()
                return
            }

            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                0f,
                object : LocationListener {
                    override fun onLocationChanged(p0: Location) {
                        Log.d("Run Fragment", "Start Location: ${p0.latitude} ${p0.longitude}")
                        startLatTextView?.text = ("${p0.latitude}")
                        startLonTextView?.text = ("${p0.longitude}")

                        startLatitude = p0.latitude
                        startLongitude = p0.longitude
                    }
                })

            startTime = System.currentTimeMillis()/1000;
            runTimeTextView?.text = "0"
        }
    }

    private fun stopRun()
    {
        endTime = System.currentTimeMillis()/1000;
        totalRunTime = endTime - startTime

        runTimeTextView?.text = ("$totalRunTime")

        var distance = calculateTotalDistance(startLatitude, endLatitude, startLongitude,
            endLongitude)

        totalDistanceTextView?.text = getString(R.string.distance_value, distance)
    }

    private fun calculateTotalDistance(startLat: Double, endLat: Double, startLon: Double, endLon: Double) : Double
    {
        // Convert from degrees to radians
        var startLatRad = Math.toRadians(startLat)
        var endLatRad = Math.toRadians(endLat)
        var startLonRad = Math.toRadians(startLon)
        var endLonRad = Math.toRadians(endLon)

        // Haversine Formula
        var dLat = endLatRad - startLatRad
        var dLon = endLonRad - startLonRad

        var a = sin(dLat / 2).pow(2.0) +
                cos(startLatRad) * cos(endLatRad) *
                sin(dLon / 2).pow(2.0)

        var c = 2 * asin(sqrt(a))

        // Radius of earth is 6371 km
        var r = 6371

        totalDistanceCovered = c * r

        return totalDistanceCovered.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }
}