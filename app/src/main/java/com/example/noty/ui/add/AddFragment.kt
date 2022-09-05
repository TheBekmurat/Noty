package com.example.noty.ui.add

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.noty.MainActivity
import com.example.noty.R
import com.example.noty.ui.base.BaseFragment
import com.example.noty.databinding.FragmentAddBinding
import com.example.noty.data.model.Task
import com.example.noty.navigator.navigator
import com.example.noty.ui.viewModel.AddViewModel
import com.example.noty.utils.AlarmReceiver
import com.example.noty.utils.DatePickerFragment
import com.example.noty.utils.TimePickerFragment
import com.example.noty.utils.formatDate
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
const val CHANNEL_ID = "channel_id"
private const val HH_MM_FORMAT = "HH:mm"
private const val DD_MMM_YYYY_FORMAT = "dd-MMM,yyyy"
const val TITLE_KEY = "title"
const val ID_KEY = "id"
const val TASK_KEY = "task_key"

class AddFragment : BaseFragment<FragmentAddBinding>() {

    private val calendar = Calendar.getInstance()
    private var oldTask: Task? = null
    private val viewModel by viewModels<AddViewModel> {
        ViewModelProvider.AndroidViewModelFactory(application = requireActivity().application)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArg()
        showKeyboardAtStart()
        initListeners()
        showCurrentTime()
        showVisibility()
    }

    private fun initListeners() {
        binding.fabBtn.setOnClickListener {
            addTask()
        }
        binding.etDays.setOnClickListener {
            showDatePicker()
        }
        binding.etHour.setOnClickListener {
            showTimePicker()
        }
        binding.goBack.setOnClickListener {
            navigator().goBack()
        }
        binding.switchVisibilityEt.setOnCheckedChangeListener { _, isChecked ->
            editTextVisibility(isChecked)
        }
    }

    private fun showDatePicker() {
        val datePickerFragment =
            DatePickerFragment(requireContext()) { _, year, month, dayOfMonth ->
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.YEAR, year)
                binding.etDays.hint = formatDate(DD_MMM_YYYY_FORMAT, calendar)
            }
        datePickerFragment.datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerFragment.datePickerDialog.show()
    }

    private fun showTimePicker() {
        TimePickerFragment { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            when {
                calendar.timeInMillis == Calendar.getInstance().timeInMillis -> binding.etHour.hint =
                    formatDate(HH_MM_FORMAT, calendar)
                calendar.timeInMillis > Calendar.getInstance().timeInMillis -> binding.etHour.hint =
                    formatDate(HH_MM_FORMAT, calendar)
                else -> Toast.makeText(requireContext(),
                    "The date you entered is in the past",
                    Toast.LENGTH_SHORT).show()
            }
        }.show(parentFragmentManager, "timePicker")
    }

    private fun getArg() {
        arguments.let {
            oldTask = it?.getParcelable(ARG_PARAM1)
            binding.etTitle.setText(oldTask?.title)
        }
    }

    companion object {
        fun newInstance(task: Task) = AddFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PARAM1, task)
            }
        }
    }

    private fun showCurrentTime() {
        if (oldTask != null) {
            if (oldTask?.date != "") {
                val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm", Locale.getDefault())
                val oldDate = sdf.parse(oldTask?.date)
                val currentTime =
                    SimpleDateFormat(HH_MM_FORMAT, Locale.getDefault()).format(oldDate)
                binding.etHour.hint = currentTime
            } else if (oldTask?.date == "") {
                val currentTime = SimpleDateFormat(HH_MM_FORMAT, Locale.getDefault()).format(Date())
                binding.etHour.hint = currentTime
            }
        } else {
            val currentTime = SimpleDateFormat(HH_MM_FORMAT, Locale.getDefault()).format(Date())
            binding.etHour.hint = currentTime
        }
    }

    private fun addTask() {
        val capitalizeText = binding.etTitle.text.toString().capitalize()
        viewModel.chooseTask(
            oldTask,
            capitalizeText,
            navigator(),
            formatDate("MMM dd,yyyy HH:mm", calendar),
            binding.switchVisibilityEt.isChecked)
        if (binding.switchVisibilityEt.isChecked) {
            val alarmManager =
                requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmClockInfo =
                AlarmManager.AlarmClockInfo(calendar.timeInMillis, getAlarmInfoPendingIntent())
            alarmManager.setAlarmClock(alarmClockInfo,
                getAlarmActionPendingIntent(capitalizeText, calendar.timeInMillis))
        }
    }

    private fun getAlarmInfoPendingIntent(): PendingIntent {
        val alarmInfoIntent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return PendingIntent.getActivity(requireContext(), 0, alarmInfoIntent, 0)
    }

    private fun getAlarmActionPendingIntent(title: String, taskId: Long): PendingIntent {
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra(TITLE_KEY, title)
            putExtra(ID_KEY, taskId)
        }
        val data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        intent.data = data
        return PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
    }

    private fun showKeyboardAtStart() {
        binding.etTitle.requestFocus()
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etTitle, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun editTextVisibility(visible: Boolean) {
        val transition = Fade().apply {
            duration = 600
            addTarget(R.id.et_hour)
            addTarget(R.id.et_days)
        }
        TransitionManager.beginDelayedTransition(binding.root, transition)
        binding.etHour.visibility = if (!visible) View.GONE else View.VISIBLE
        binding.etDays.visibility = if (!visible) View.GONE else View.VISIBLE
    }

    private fun showVisibility() {
        if (oldTask != null) {
            if (oldTask?.date != "") {
                binding.switchVisibilityEt.isChecked = true
            }
        }
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentAddBinding =
        FragmentAddBinding.inflate(inflater, container, false)
}