import com.hackathon.saturday.data.local.db.*
import com.hackathon.saturday.data.local.entity.Task
import com.hackathon.saturday.data.local.entity.Deadline
import com.hackathon.saturday.data.local.entity.Event
import com.hackathon.saturday.data.local.entity.Flashcard
import com.hackathon.saturday.data.model.ExtractionResult
import com.hackathon.saturday.data.model.TodayItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ActionRepository(
    private val taskDao: TaskDao,
    private val deadlineDao: DeadlineDao,
    private val eventDao: EventDao,
    private val flashcardDao: FlashcardDao
) {
    fun getTodayItems(startOfDay: Long): Flow<List<TodayItem>> {
        val tasksFlow = taskDao.getPending(startOfDay)
        val deadlinesFlow = deadlineDao.getUpcoming(startOfDay)
        val eventsFlow = eventDao.getUpcoming(startOfDay)
        return combine(tasksFlow, deadlinesFlow, eventsFlow) { tasks, deadlines, events ->
            val items = mutableListOf<TodayItem>()
            items.addAll(tasks.map { TodayItem.TodayTask(it) })
            items.addAll(deadlines.map { TodayItem.TodayDeadline(it) })
            items.addAll(events.map { TodayItem.TodayEvent(it) })
            items.sortedBy { it.timestamp }
        }
    }

    suspend fun saveExtraction(result: ExtractionResult) {
        taskDao.insertAll(result.tasks)
        deadlineDao.insertAll(result.deadlines)
        eventDao.insertAll(result.events)
        flashcardDao.insertAll(result.flashcards)
    }

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAll()
    fun getPendingDeadlines(startOfDay: Long): Flow<List<Deadline>> = deadlineDao.getUpcoming(startOfDay)
    fun getUpcomingEvents(startOfDay: Long): Flow<List<Event>> = eventDao.getUpcoming(startOfDay)
    fun getAllFlashcards(): Flow<List<Flashcard>> = flashcardDao.getAll()

    suspend fun completeTask(id: Long) = taskDao.updateCompletion(id, true)
    suspend fun completeDeadline(id: Long) = deadlineDao.updateCompletion(id, true)
}
