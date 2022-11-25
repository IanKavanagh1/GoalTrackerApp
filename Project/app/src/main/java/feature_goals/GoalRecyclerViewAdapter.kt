package feature_goals

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.goal_tracker.R

class GoalRecyclerViewAdapter(context: Context, goalData: ArrayList<GoalDataModel> ) : RecyclerView.Adapter<GoalRecyclerViewAdapter.GoalViewHolder>()
{
    var data = goalData
    var cont = context

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GoalViewHolder {
        var inflater = LayoutInflater.from(cont)
        var view = inflater.inflate(R.layout.goal_recycler_item, p0, false)

        return GoalViewHolder(view)
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

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var goalIconView = itemView.findViewById<ImageView>(R.id.goal_icon)
        var goalNameText = itemView.findViewById<TextView>(R.id.goal_name)
        var goalProgressText = itemView.findViewById<TextView>(R.id.goal_progress)

    }
}