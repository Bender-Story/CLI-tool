package com.rahul.cliproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahul.cliproject.data.CommandData
import com.rahul.cliproject.data.User
import com.rahul.cliproject.database.AppDatabase
import com.rahul.cliproject.utils.filterEmpty
import org.koin.core.KoinComponent
import org.koin.ext.isInt

class MainViewModel : ViewModel(), KoinComponent {
    val error: MutableLiveData<String?> = MutableLiveData()
    val success: MutableLiveData<String?> = MutableLiveData()
    var userLoggedIn: String = ""
    lateinit var db: AppDatabase
    fun initDb(db: AppDatabase) {
        this.db = db
    }

    fun executeCommand(command: String) {
        var strings = command.split(" ")
        // check if the command is empty
        if (strings.isEmpty()) showError("Command cannot be empty.")
        // check if command length is less or more than the standard once
        else if (strings.size == 1 || strings.size > 3) showError("@ $command @ is a invalid Command.")
        // if the length is valid execute the command
        else if (strings.size == 2) executeCommand(CommandData(strings[0], strings[1]), command)
        else if (strings.size == 3) executeCommand(
            CommandData(strings[0], strings[1], strings[2]),
            command
        )
    }

    private fun executeCommand(commandData: CommandData, command: String) {
        when (commandData.prifix) {
            "add" -> addUser(commandData)
            "login" -> loginUser(commandData)
            "topup" -> topUpUser(commandData)
            "pay" -> payUser(commandData)
            else -> showError("@ $command @ is a invalid Command.")

        }
    }

    private fun showError(message: String) {
        error.postValue("--------> $message <--------")
    }

    private fun showSuccessMessage(message: String) {
        success.postValue(message)
    }

    private fun addUser(commandData: CommandData) {
        var name = commandData.firstValue
        var user = getUser(commandData.firstValue)
        // check if user already exists
        if (user == null) {
            db.userDao().insert(User(commandData.firstValue))
            showSuccessMessage("$name account is being created.")
        } else showError("Duplicate user please use a different name.")
    }

    private fun loginUser(commandData: CommandData) {
        val user = getUser(commandData.firstValue)
        // check if user exists else throw error
        if (user == null) {
            var name = commandData.firstValue
            userLoggedIn = ""
            showError("@ $name @ is a invalid user.")
        } else {
            userLoggedIn = user.userName
            var balance = user.amount
            showSuccessMessage("Hello, $userLoggedIn ! \nYour balance is $balance ")
        }
    }

    private fun getUser(userName: String): User {
        return db.userDao().getUser(userName)
    }

    private fun updateUser(user: User) {
        db.userDao().updateUser(user)
    }

    private fun topUpUser(commandData: CommandData) {
        if (!userLoggedIn.isNullOrEmpty()) {
            var user = getUser(userLoggedIn)
            // check if the topup value is int
            if (commandData.firstValue.isInt()) {
                var addBalance = user.amount?.plus(commandData.firstValue.toInt())
                updateUser(User(user.userName, addBalance, user.lastPayedTo))
                showSuccessMessage("Your balance is $addBalance.")
            } else {
                showError("Top up amount should be a number.")
            }
        } else {
            showError("User needs to login first.")
        }
    }

    private fun payUser(commandData: CommandData) {
        if (commandData.secondValue.isNullOrEmpty()) {
            showError(" invalid Command.")
        } else if (userLoggedIn.isNullOrEmpty()) {
            showError("User needs to login first.")
        } else {
            var user = getUser(userLoggedIn)
            if (commandData.secondValue.isInt()) {
//                var addBalance = user.amount?.plus(commandData.firstValue.toInt())
//                updateUser(User(user.userName, addBalance, user.lastPayedTo))
                var otherUser = getUser(commandData.firstValue)
                if (otherUser != null) {
                    pay(user, getUser(commandData.firstValue), commandData.secondValue.toInt())
                } else {
                    showError("You are trying to pay a Invalid user.")
                }

            } else {
                showError("Pay amount should be a number.")
            }
        }
    }

    private fun pay(user: User, otherUser: User, value: Int) {
        if (value <= user.amount.filterEmpty()) {
            var mainUsersBalance = user.amount?.minus(value.filterEmpty())
            var otherUsersBalance = otherUser.amount?.plus(value.filterEmpty())
            updateUser(User(user.userName, mainUsersBalance, otherUser.userName))
            updateUser(User(otherUser.userName, otherUsersBalance, otherUser.lastPayedTo))
            val otherUserName = otherUser.userName
            showSuccessMessage("Transferred $value to $otherUserName.\n" +
                    "Your balance is $mainUsersBalance.")
        } else showError("Insufficient Balance please topup.")
    }

}