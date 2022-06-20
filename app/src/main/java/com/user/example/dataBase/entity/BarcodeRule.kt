package com.user.example.database.entity

import androidx.room.Entity
import android.os.Parcel
import android.os.Parcelable


@Entity(tableName = "barcode_rules")
data class BarcodeRule (
    var Name: String,
    var FullLength: Int,
    var ItemNo: String,
    var ItemNoStartIndex: Int,
    var ItemNoLength: Int,
    var ProductionDateStartIndex: Int,
    var ProductionDateLength: Int,
    var ProductionDateFormat: String,
    var WeightStartIndex: Int,
    var WeightLength: Int,
    var WeightDecimalPoint: Int,
    var WeightUnit: Int,
    var GS1: Boolean
): BaseEntity(), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Name)
        parcel.writeInt(FullLength)
        parcel.writeString(ItemNo)
        parcel.writeInt(ItemNoStartIndex)
        parcel.writeInt(ItemNoLength)
        parcel.writeInt(ProductionDateStartIndex)
        parcel.writeInt(ProductionDateLength)
        parcel.writeString(ProductionDateFormat)
        parcel.writeInt(WeightStartIndex)
        parcel.writeInt(WeightLength)
        parcel.writeInt(WeightDecimalPoint)
        parcel.writeInt(WeightUnit)
        parcel.writeByte(if (GS1) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BarcodeRule> {
        override fun createFromParcel(parcel: Parcel): BarcodeRule {
            return BarcodeRule(parcel)
        }

        override fun newArray(size: Int): Array<BarcodeRule?> {
            return arrayOfNulls(size)
        }
    }
}