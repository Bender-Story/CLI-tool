package com.rahul.cliproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.rahul.cliproject.data.User
import com.rahul.cliproject.database.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()
    private var rowViewModels: ArrayList<RowViewModel>? = arrayListOf()
    lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDataBase()
        initRecyclerView()
        observeList()
    }

    private fun initDataBase() {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "users"
        ).allowMainThreadQueries().build()
        viewModel.initDb(db)
    }

    /**
     * observer to check live data changes
     */
    private fun observeList() {

        viewModel.error.observe(this, Observer {
            addNextLine(it,false)
            addNextLine("Bank >",true)
        })

        viewModel.success.observe(this, Observer {
            addNextLine(it,false)
            addNextLine("Bank >",true)
        })
    }

    private fun initRecyclerView() {
        recyclerView.adapter = MainAdapter(rowViewModels) { command ->
//            addNextLine()
//            db.let {  it.userDao().insert(User(name,0,"")) }
            viewModel.executeCommand(command)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        addNextLine("Bank >",true)
    }

    private fun addNextLine(text :String?, showEditText:Boolean) {
        (recyclerView.adapter as MainAdapter).add(RowViewModel(text, showEditText))
    }
}