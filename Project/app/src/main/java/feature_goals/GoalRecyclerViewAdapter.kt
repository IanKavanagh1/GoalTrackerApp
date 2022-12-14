package feature_goals

import android.content.Context
import android.graphics.drawable.Icon
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.goal_tracker.R

class GoalRecyclerViewAdapter(context: Context, goalData: ArrayList<GoalDataModel>, goalRecyclerViewInterface: GoalRecyclerViewInterface ) : RecyclerView.Adapter<GoalRecyclerViewAdapter.GoalViewHolder>()
{
    // store data
    private var data = goalData
    private var cont = context

    // Initialise interface
    private var gRVInterface = goalRecyclerViewInterface

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GoalViewHolder {
        // Initialise layout and return
        val inflater = LayoutInflater.from(cont)
        val view = inflater.inflate(R.layout.goal_recycler_item, p0, false)

        return GoalViewHolder(view, gRVInterface)
    }

    override fun onBindViewHolder(p0: GoalViewHolder, p1: Int) {

        // Bind data to UI elements
        when(data[p1].goalType)
        {
            GoalTypes.Fitness.ordinal ->{
                val icon = Icon.createWithResource(cont, R.drawable.ic_baseline_fitness_center_24)
                p0.goalIconView.setImageIcon(icon)
            }
            GoalTypes.HealthEating.ordinal ->{
                val icon = Icon.createWithResource(cont, R.drawable.ic_baseline_food_bank_24)
                p0.goalIconView.setImageIcon(icon)
            }
        }

        data[p1].goalProgress

        p0.goalNameText.text = data[p1].goalName

        // Only show the progress label if we have progress greater than 0
        if(data[p1].goalProgress > 0f)
        {
            p0.goalProgressText.text = data[p1].goalProgress.toString()
            p0.goalProgressText.visibility = View.VISIBLE
        }
        else
        {
            p0.goalProgressText.visibility = View.INVISIBLE
        }

        // Show the completed label if the goal is completed
        if(data[p1].goalCompleted == 1)
        {
            p0.goalCompletedText.visibility = View.VISIBLE
            p0.goalProgressText.visibility = View.INVISIBLE
        }
        else
        {
            p0.goalCompletedText.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int
    {
        // Return the total number of goals the user has
        return data.size
    }

    class GoalViewHolder(itemView: View, recyclerViewInterface: GoalRecyclerViewInterface) : RecyclerView.ViewHolder(itemView)
    {
        // Display the Goal data
        var goalIconView: ImageView = itemView.findViewById(R.id.goal_icon)
        var goalNameText: TextView = itemView.findViewById(R.id.goal_name)
        var goalProgressText: TextView = itemView.findViewById(R.id.goal_progress)
        var goalCompletedText: TextView = itemView.findViewById(R.id.goal_completed)

        // Set up on click listener for items
        val view = itemView.setOnClickListener {
            if(recyclerViewInterface != null)
            {
               val pos = adapterPosition

               if(pos != RecyclerView.NO_POSITION)
               {
                   recyclerViewInterface.onItemClick(pos)
               }
            }
        }
    }
}