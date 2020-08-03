package com.rahul.cliproject

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahul.cliproject.databinding.LayoutTextViewBinding
import com.rahul.cliproject.utils.filterEmpty


class MainAdapter(
    private val items: ArrayList<RowViewModel>?,
    val onCommandExecuted: (String) -> Unit = {}
) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutTextViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items?.size.filterEmpty()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(items?.get(position)!!)
    }

    inner class ViewHolder(private val binding: LayoutTextViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RowViewModel) {
            binding.viewModel = item
            binding.executePendingBindings()
            binding.command.requestFocus()
            executeCommendListener(binding)
        }
    }

    fun executeCommendListener(binding: LayoutTextViewBinding) {
        binding.command.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action === KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    onCommandExecuted.invoke(binding.command.text.toString())
                    binding.command.isEnabled = false

                    return true
                }

                return false
            }

        })
    }

    fun add(rowViewModel: RowViewModel) {
        val position = items?.indexOf(rowViewModel)
        if (position!! == -1) {
            items?.add(rowViewModel)
            notifyItemInserted(items?.size.filterEmpty() - 1)
        }
    }

    fun removeAll() {
        items?.clear()
    }
}