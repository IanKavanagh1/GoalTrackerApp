package feature_goals

import android.content.Context
import android.support.v7.widget.RecyclerView
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

        //p0.goalIconView.setImageIcon(data.get(p1).iconId)
        p0.goalNameText.text = data[p1].goalName
        p0.goalProgressText.text = data[p1].goalProgress.toString()
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