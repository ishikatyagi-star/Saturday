package com.hackathon.saturday.export

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.hackathon.saturday.data.local.entity.Task
import com.hackathon.saturday.data.local.entity.Deadline
import com.hackathon.saturday.data.local.entity.Event
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object OfficeKitBridge {

    fun shareTasks(context: Context, tasks: List<Task>, deadlines: List<Deadline>, events: List<Event>) {
        val content = buildString {
            appendLine("Saturday Export - ${formatDate(System.currentTimeMillis())}")
            appendLine("=".repeat(40))
            if (tasks.isNotEmpty()) {
                appendLine("\nTASKS:")
                tasks.forEach { appendLine("• ${it.title} ${it.dueDate?.let { d -> "(Due: ${formatDate(d)})" } ?: ""}") }
            }
            if (deadlines.isNotEmpty()) {
                appendLine("\nDEADLINES:")
                deadlines.forEach { appendLine("• ${it.title} (Due: ${formatDate(it.dueDate)})") }
            }
            if (events.isNotEmpty()) {
                appendLine("\nEVENTS:")
                events.forEach { appendLine("• ${it.title} at ${it.location ?: "TBD"} (${formatDate(it.eventDate)})") }
            }
        }

        val file = File(context.cacheDir, "saturday_export.txt")
        file.writeText(content)

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Saturday Campus Export")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Send to Office Kit"))
    }

    private fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(timestamp))
    }
}
