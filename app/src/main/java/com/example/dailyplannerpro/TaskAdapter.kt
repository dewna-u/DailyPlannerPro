package com.example.dailyplannerpro

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val context: Context,
    private val tasks: ArrayList<Task>
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val taskDescription: TextView = itemView.findViewById(R.id.task_description)
        val taskPriority: TextView = itemView.findViewById(R.id.task_priority)
        val editButton: Button = itemView.findViewById(R.id.edit_button)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
        val viewButton: Button = itemView.findViewById(R.id.view_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.name
        holder.taskDescription.text = task.description
        holder.taskPriority.text = task.priority

        // Edit button: Pass task and position for editing
        holder.editButton.setOnClickListener {
            val intent = Intent(context, AddTaskActivity::class.java)
            intent.putExtra("task", task)
            intent.putExtra("position", position) // Pass task position
            (context as MainActivity).startActivityForResult(intent, 2) // Request code 2 for editing
        }

        holder.deleteButton.setOnClickListener {
            tasks.removeAt(position)
            notifyItemRemoved(position) // Notify that the item was removed
            TaskStorage.saveTasks(context, tasks)
        }

        holder.viewButton.setOnClickListener {
            val intent = Intent(context, TaskDetailActivity::class.java)
            intent.putExtra("task", task)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
