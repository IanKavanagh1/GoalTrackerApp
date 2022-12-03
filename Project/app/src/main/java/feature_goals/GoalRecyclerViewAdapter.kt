package feature_goals

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.goal_tracker.R

class GoalRecyclerViewAdapter(context: Context, goalData: ArrayList<GoalDataModel>, goalRecylerViewInterface: GoalRecylerViewInterface ) : RecyclerView.Adapter<GoalRecyclerViewAdapter.GoalViewHolder>()
{
    private var data = goalData
    private var cont = context

    private var gRVInterface = goalRecylerViewInterface

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GoalViewHolder {
        val inflater = LayoutInflater.from(cont)
        val view = inflater.inflate(R.layout.goal_recycler_item, p0, false)

        return GoalViewHolder(view, gRVInterface)
    }

    override fun onBindViewHolder(p0: GoalViewHolder, p1: Int) {
        //p0.goalIconView.setImageIcon(data.get(p1).iconId)
        p0.goalNameText.text = data[p1].goalName
        p0.goalProgressText.text = data[p1].goalProgress.toString()
    }

    override fun getItemCount(): Int
    {
        return data.size
    }

    class GoalViewHolder(itemView: View, recylerViewInterface: GoalRecylerViewInterface) : RecyclerView.ViewHolder(itemView)
    {
        var goalIconView: ImageView = itemView.findViewById(R.id.goal_icon)
        var goalNameText: TextView = itemView.findViewById(R.id.goal_name)
        var goalProgressText: TextView = itemView.findViewById(R.id.goal_progress)

        val view = itemView.setOnClickListener {
            if(recylerViewInterface != null)
            {
               val pos = adapterPosition

               if(pos != RecyclerView.NO_POSITION)
               {
                   recylerViewInterface.onItemClick(pos)
               }
            }
        }
    }
}