package com.example.dailyplannerpro

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TaskStorage {
    private const val TASKS_KEY = "tasks"

    fun saveTasks(context: Context, taskList: ArrayList<Task>) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(taskList)
        editor.putString(TASKS_KEY, json)
        editor.apply() // Save the data
    }

    fun loadTasks(context: Context): ArrayList<Task> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(TASKS_KEY, null)
        val type = object : TypeToken<ArrayList<Task>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }
}