package org.softsuave.bustlespot.tracker.ui

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.softsuave.bustlespot.Log
import org.softsuave.bustlespot.auth.utils.Result
import org.softsuave.bustlespot.auth.utils.UiEvent
import org.softsuave.bustlespot.auth.utils.timeStringToSeconds
import org.softsuave.bustlespot.data.network.models.response.DisplayItem
import org.softsuave.bustlespot.data.network.models.response.Project
import org.softsuave.bustlespot.data.network.models.response.TaskData
import org.softsuave.bustlespot.timer.TrackerModule
import org.softsuave.bustlespot.tracker.data.TrackerRepository
import org.softsuave.bustlespot.tracker.ui.model.GetTasksRequest

class HomeViewModel(
    private val trackerRepository: TrackerRepository
) : ViewModel() {


    private val trackerModule = TrackerModule(viewModelScope)

    val trackerTime: StateFlow<Int> = trackerModule.trackerTime
    val isTrackerRunning: StateFlow<Boolean> = trackerModule.isTrackerRunning
    val idealTime: StateFlow<Int> = trackerModule.idealTime
    var screenShotTakenTime: StateFlow<Int> = trackerModule.screenShotTakenTime
    val keyboradKeyEvents: StateFlow<Int> = trackerModule.keyboradKeyEvents
    val mouseKeyEvents: StateFlow<Int> = trackerModule.mouseKeyEvents
    val mouseMotionCount: StateFlow<Int> = trackerModule.mouseMotionCount
    val customeTimeForIdleTime: StateFlow<Int> = trackerModule.customeTimeForIdleTime
    val screenShotState: StateFlow<ImageBitmap?> = trackerModule.screenShotState
    var isTrackerStarted: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun startTrackerTimer() = trackerModule.startTimer()
    fun stopTrackerTimer() = trackerModule.stopTimer()
    fun resetTrackerTimer() = trackerModule.resetTimer()
    fun resumeTrackerTimer() = trackerModule.resumeTracker()

    fun startScreenshotTask() = trackerModule.startScreenshotTask()
    fun pauseScreenshotTask() = trackerModule.pauseScreenshotTask()
    fun resumeScreenshotTask() = trackerModule.resumeScreenshotTask()
    fun stopScreenshotTask() = trackerModule.stopScreenshotTask()

    fun resetIdleTimer() = trackerModule.resetIdleTimer()
    fun updateTrackerTimer() = trackerModule.updateTrackerTimer()
    fun addCustomTimeForIdleTime(time: Int) = trackerModule.addCustomTimeForIdleTime(time)


    private val _taskList = kotlinx.coroutines.flow.MutableStateFlow<List<TaskData>>(emptyList())
    private val _mainTaskList =
        kotlinx.coroutines.flow.MutableStateFlow<List<TaskData>>(emptyList())
    private val _mainProjectList =
        kotlinx.coroutines.flow.MutableStateFlow<List<Project>>(emptyList())

    private val _uiEvent =
        kotlinx.coroutines.flow.MutableStateFlow<UiEvent<TrackerScreenData>>(UiEvent.Loading)
    val uiEvent: StateFlow<UiEvent<TrackerScreenData>> get() = _uiEvent

    private val trackerScreenData = TrackerScreenData(mutableListOf(), mutableListOf())

    private val _selectedProject = kotlinx.coroutines.flow.MutableStateFlow<Project?>(null)
    val selectedProject: StateFlow<Project?> = _selectedProject.asStateFlow()

    private val _selectedTask = kotlinx.coroutines.flow.MutableStateFlow<TaskData?>(null)
    val selectedTask: StateFlow<TaskData?> = _selectedTask.asStateFlow()

    private val _projectDropDownState = kotlinx.coroutines.flow.MutableStateFlow(DropDownState())
    val projectDropDownState: StateFlow<DropDownState> = _projectDropDownState.asStateFlow()

    private val _taskDropDownState = kotlinx.coroutines.flow.MutableStateFlow(DropDownState())
    val taskDropDownState: StateFlow<DropDownState> = _taskDropDownState.asStateFlow()

    private val _trackerDialogState: MutableStateFlow<TrackerDialogState> =
        MutableStateFlow(TrackerDialogState())
    val trackerDialogState: StateFlow<TrackerDialogState> = _trackerDialogState.asStateFlow()

    private val _totalIdleTime: MutableStateFlow<Int> = MutableStateFlow(0)
    val totalIdleTime: StateFlow<Int> = _totalIdleTime.asStateFlow()

    // actual is 7200
    val idealTimeThreshold: Int = 7200

    fun getAllProjects(organisationId: String) {
        viewModelScope.launch {
            trackerRepository.getAllProjects(organisationId).collect { result ->
                when (result) {
                    is Result.Error -> {
                        _uiEvent.value = UiEvent.Failure(result.message ?: "Unknown Error")
                        _projectDropDownState.value = _projectDropDownState.value.copy(
                            errorMessage = result.message ?: "Failed to fetch projects"
                        )
                    }

                    Result.Loading -> {
                        _uiEvent.value = UiEvent.Loading
                    }

                    is Result.Success -> {
                        _mainProjectList.value = result.data.projectLists ?: emptyList()
                        _projectDropDownState.value = _projectDropDownState.value.copy(
                            dropDownList = result.data.projectLists ?: emptyList(),
                            errorMessage = if (result.data.projectLists.isNullOrEmpty()) "No projects to select" else ""
                        )
                        trackerScreenData.listOfProject?.addAll(
                            result.data.projectLists ?: emptyList()
                        )
                        _uiEvent.value = UiEvent.Success(trackerScreenData)
                        fetchAllTasksForProjects(
                            projects = result.data.projectLists ?: emptyList(),
                            organisationId
                        )
                    }
                }
            }
        }
    }

    private fun fetchAllTasksForProjects(projects: List<Project>, organisationId: String) {
        viewModelScope.launch {
            projects.forEach { project ->
                trackerRepository.getAllTask(
                    GetTasksRequest(projectId = project.projectId.toString(), organisationId)
                ).collect { result ->

                    when (result) {
                        is Result.Error -> {
                            _taskDropDownState.value = _taskDropDownState.value.copy(
                                errorMessage = result.message ?: "Failed to fetch tasks"
                            )
                        }

                        is Result.Loading -> {
                            Log.d("Loading at projects")
                        }

                        is Result.Success -> {
                            val taskList = result.data.taskDetails
                            _mainTaskList.value = _mainTaskList.value.plus(taskList)
                            trackerScreenData.listOfTask?.addAll(taskList)
                        }
                    }
                }
            }
        }
    }

    fun getAllTasks(
        organisationId: String,
        projectId: String
    ) {
        viewModelScope.launch {
            trackerRepository.getAllTask(
                GetTasksRequest(
                    organisationId = organisationId,
                    projectId = projectId
                )
            ).collect { result ->
                when (result) {
                    is Result.Error -> {
                        _uiEvent.value = UiEvent.Failure(result.message ?: "Unknown Error")
                        _taskDropDownState.value = _taskDropDownState.value.copy(
                            errorMessage = result.message ?: "Failed to fetch tasks"
                        )
                    }

                    Result.Loading -> {
                        _uiEvent.value = UiEvent.Loading
                    }

                    is Result.Success -> {
                        _mainTaskList.value = result.data.taskDetails
                        _taskDropDownState.value = _taskDropDownState.value.copy(
                            dropDownList = _taskList.value,
                            errorMessage = ""
                        )
                        trackerScreenData.listOfTask?.addAll(result.data.taskDetails)
                        _uiEvent.value = UiEvent.Success(trackerScreenData)
                    }
                }
            }
        }
    }


    fun handleDropDownEvents(dropDownEvents: DropDownEvents) {
        when (dropDownEvents) {
            is DropDownEvents.OnProjectSearch -> {
                _projectDropDownState.value = _projectDropDownState.value.copy(
                    inputText = dropDownEvents.inputText
                )
                if (dropDownEvents.inputText.isEmpty()) {
                    _selectedProject.value = null
                    _taskDropDownState.value =
                        _taskDropDownState.value.copy(dropDownList = emptyList())
                }
            }

            is DropDownEvents.OnProjectSelection -> {
                _selectedProject.value = dropDownEvents.selectedProject
                _projectDropDownState.value = _projectDropDownState.value.copy(
                    inputText = dropDownEvents.selectedProject.name,
                    errorMessage = ""
                )

                val filteredTasks =
                    _mainTaskList.value.filter { it.projectId == dropDownEvents.selectedProject.projectId }
                _taskDropDownState.value = _taskDropDownState.value.copy(
                    dropDownList = filteredTasks,
                    errorMessage = if (filteredTasks.isEmpty()) "No task available to select" else "",
                    inputText = ""
                )
                _selectedTask.value = null
            }

            is DropDownEvents.OnTaskSearch -> {
                if (dropDownEvents.inputText.isNotEmpty()) {
                    _taskDropDownState.value = _taskDropDownState.value.copy(
                        inputText = dropDownEvents.inputText
                    )
                }
                if (dropDownEvents.inputText.isEmpty()) {
                    if (!isTrackerRunning.value) {
                        _selectedTask.value = null
                        _taskDropDownState.value = _taskDropDownState.value.copy(
                            inputText = dropDownEvents.inputText
                        )
                    }
                }
                if (_selectedProject.value == null) {
                    _projectDropDownState.value = _projectDropDownState.value.copy(
                        errorMessage = "Please select the a project"
                    )
                }
            }

            is DropDownEvents.OnTaskSelection -> {
                _selectedTask.value = dropDownEvents.selectedTask
                _taskDropDownState.value = _taskDropDownState.value.copy(
                    inputText = dropDownEvents.selectedTask.name,
                    errorMessage = ""
                )
                trackerModule.setTrackerTime(
                    dropDownEvents.selectedTask.time ?: 0,
                    dropDownEvents.selectedTask.unTrackedTime ?: 0
                )
                trackerModule.setLastScreenShotTime(
                    dropDownEvents.selectedTask.lastScreenShotTime?.timeStringToSeconds() ?: 0
                )
                _totalIdleTime.value = dropDownEvents.selectedTask.unTrackedTime ?: 0
            }

            is DropDownEvents.OnProjectDropDownClick -> {
                println("Project dropdown clicked")
            }

            is DropDownEvents.OnTaskDropDownClick -> {
                if (_selectedProject.value == null) {
                    _projectDropDownState.value = _projectDropDownState.value.copy(
                        errorMessage = "Please select the a project"
                    )
                } else {
                    _projectDropDownState.value =
                        _projectDropDownState.value.copy(errorMessage = "")
                }
            }


        }
    }


    fun handleTrackerDialogEvents(
        trackerDialogEvents: TrackerDialogEvents,
        handleNavAction: () -> Unit = {}
    ) {
        when (trackerDialogEvents) {
            TrackerDialogEvents.ShowExitDialog -> {
                _trackerDialogState.value = _trackerDialogState.value.copy(
                    isDialogShown = true,
                    title = "Exit",
                    text = "Are you sure you want to exit?",
                    confirmButtonText = "Yes",
                    dismissButtonText = "No",
                    onConfirm = {
                        _trackerDialogState.value = _trackerDialogState.value.copy(
                            isDialogShown = false
                        )
                        stopTrackerTimer()
                        handleNavAction()
                    },
                    onDismiss = {
                        _trackerDialogState.value = _trackerDialogState.value.copy(
                            isDialogShown = false
                        )
                    }
                )
            }

            is TrackerDialogEvents.ShowIdleTimeDialog -> {
                _trackerDialogState.value = _trackerDialogState.value.copy(
                    isDialogShown = true,
                    title = "IdleTime",
                    text = "You are idle for %s. Do you want to add idle time to the session?",
                    confirmButtonText = "Okay",
                    dismissButtonText = "Cancel",
                    onConfirm = {
                        _trackerDialogState.value = _trackerDialogState.value.copy(
                            isDialogShown = false
                        )
                        if (idealTime.value < idealTimeThreshold)
                            _totalIdleTime.value += idealTime.value
                        resetIdleTimer()
                        resumeTrackerTimer()
                    },
                    onDismiss = {
                        _trackerDialogState.value = _trackerDialogState.value.copy(
                            isDialogShown = false
                        )
                        resetIdleTimer()
                        resumeTrackerTimer()
                    }
                )


            }

            is TrackerDialogEvents.ShowProjectChangeDialog -> {
                _trackerDialogState.value = _trackerDialogState.value.copy(
                    isDialogShown = true,
                    title = "Alert",
                    text = "Are you sure you want to stop tracker and change project?",
                    confirmButtonText = "Yes",
                    dismissButtonText = "No",
                    onConfirm = {
                        _trackerDialogState.value = _trackerDialogState.value.copy(
                            isDialogShown = false
                        )
                        resetTrackerTimer()
                        handleDropDownEvents(DropDownEvents.OnProjectSelection(trackerDialogEvents.selectedProject))
                    },
                    onDismiss = {
                        _trackerDialogState.value = _trackerDialogState.value.copy(
                            isDialogShown = false
                        )
                    }
                )
            }

            is TrackerDialogEvents.ShowTaskChangeDialog -> {
                _trackerDialogState.value = _trackerDialogState.value.copy(
                    isDialogShown = true,
                    title = "Alert",
                    text = "Are you sure you want to stop tracker and change Task?",
                    confirmButtonText = "Yes",
                    dismissButtonText = "No",
                    onConfirm = {
                        _trackerDialogState.value = _trackerDialogState.value.copy(
                            isDialogShown = false
                        )
                        resetTrackerTimer()
                        handleDropDownEvents(DropDownEvents.OnTaskSelection(trackerDialogEvents.selectedTask))
                    },
                    onDismiss = {
                        _trackerDialogState.value = _trackerDialogState.value.copy(
                            isDialogShown = false
                        )
                    }
                )
            }

            TrackerDialogEvents.ShowTrackerAlertDialog -> {
                TODO()
            }
        }
    }

    fun handleTrackerScreenEvents(trackerScreenEvents: TrackerScreenEvents) {
        when (trackerScreenEvents) {
            TrackerScreenEvents.OnExitClick -> {
                TODO()
            }
        }
    }

    fun handleTrackerTimerEvents(timerEvents: TimerEvents) {
        when (timerEvents) {
            TimerEvents.FetchTime -> TODO()
            TimerEvents.PauseTimer -> TODO()
            TimerEvents.ResetTimer -> TODO()
            TimerEvents.StartTimer -> {
                if (trackerTime.value != 0 && isTrackerRunning.value) {
                    resumeTrackerTimer()
                } else {
                    if (checkTaskAndProject()) {
                        startTrackerTimer()
                        isTrackerStarted.value = true
                    }
                }
            }

            TimerEvents.StopTimer -> {
                stopTrackerTimer()
            }

            TimerEvents.UpdateTime -> TODO()

            TimerEvents.ResumeTimer -> {
                resumeTrackerTimer()
            }
        }
    }

    private fun checkTaskAndProject(): Boolean {
        if (_selectedProject.value == null) {
            _projectDropDownState.value = _projectDropDownState.value.copy(
                errorMessage = "Please select the a project"
            )
        }
        if (_selectedTask.value == null) {
            _taskDropDownState.value = _taskDropDownState.value.copy(
                errorMessage = "Please select the a task"
            )
            return false
        }

        return true
    }


}


data class TrackerScreenData(
    val listOfProject: MutableList<Project>?,
    val listOfTask: MutableList<TaskData>?
)

sealed class DropDownEvents {
    data class OnProjectSearch(val inputText: String) : DropDownEvents()
    data class OnTaskSearch(val inputText: String) : DropDownEvents()
    data class OnProjectSelection(val selectedProject: Project) : DropDownEvents()
    data class OnTaskSelection(val selectedTask: TaskData) : DropDownEvents()
    data object OnProjectDropDownClick : DropDownEvents()
    data object OnTaskDropDownClick : DropDownEvents()
}

data class DropDownState(
    val errorMessage: String = "",
    val inputText: String = "",
    val dropDownList: List<DisplayItem> = emptyList()
)


data class TrackerDialogState(
    val isDialogShown: Boolean = false,
    val title: String = "",
    val text: String = "",
    val confirmButtonText: String = "",
    val dismissButtonText: String = "",
    val onConfirm: () -> Unit = {},
    val onDismiss: () -> Unit = {}
)

sealed class TrackerDialogEvents {
    data object ShowExitDialog : TrackerDialogEvents()
    data object ShowIdleTimeDialog : TrackerDialogEvents()
    data class ShowProjectChangeDialog(val selectedProject: Project) : TrackerDialogEvents()
    data class ShowTaskChangeDialog(val selectedTask: TaskData) : TrackerDialogEvents()

    //    data object showExitDialog : TrackerDialogEvents()
//    data object showProjectChangeDialog : TrackerDialogEvents()
//    data object showTaskChangeDialog : TrackerDialogEvents()
    // no confirmed design
    data object ShowTrackerAlertDialog : TrackerDialogEvents()
}

sealed class TrackerScreenEvents {
    data object OnExitClick : TrackerScreenEvents()
}


sealed class TimerEvents {
    data object StartTimer : TimerEvents()
    data object StopTimer : TimerEvents()
    data object ResetTimer : TimerEvents()
    data object PauseTimer : TimerEvents()
    data object ResumeTimer : TimerEvents()

    // ambitious
    data object UpdateTime : TimerEvents()
    data object FetchTime : TimerEvents()
}