package com.rahul.cliproject.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val userName: String,
    @ColumnInfo(name = "total_amount") val amount: Int?=0,
    @ColumnInfo(name = "last_payed_to") val lastPayedTo: String?=""
)