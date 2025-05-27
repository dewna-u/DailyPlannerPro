package com.example.dailyplannerpro

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews


class TaskWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.task_widget)
            val tasks = TaskStorage.loadTasks(context)
            val taskText = tasks.joinToString("\n") { it.name }

            views.setTextViewText(R.id.widget_task_list, taskText)

            // Set up an intent for clicking the widget
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(R.id.widget_task_list, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}