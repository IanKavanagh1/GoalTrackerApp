package feature_goals

import android.os.Parcel
import android.os.Parcelable

data class GoalDataModel(
    val iconId: Int,
    val goalType: Int,
    val goalName: String?,
    val goalProgress: Float) : Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(iconId)
        parcel.writeInt(goalType)
        parcel.writeString(goalName)
        parcel.writeFloat(goalProgress)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoalDataModel> {
        override fun createFromParcel(parcel: Parcel): GoalDataModel {
            return GoalDataModel(parcel)
        }

        override fun newArray(size: Int): Array<GoalDataModel?> {
            return arrayOfNulls(size)
        }
    }
}
