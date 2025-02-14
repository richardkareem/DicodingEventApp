package com.org.dicodingeventapp.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.org.dicodingeventapp.data.local.datastore.SettingPreferences
import com.org.dicodingeventapp.data.local.datastore.dataStore
import com.org.dicodingeventapp.databinding.FragmentSettingBinding
import com.org.dicodingeventapp.utils.MyWorker
import java.util.concurrent.TimeUnit


class SettingFragment : Fragment() {
   private var _binding : FragmentSettingBinding? = null
    val binding get() = _binding
    private lateinit var workManager : WorkManager
    private lateinit var periodicWokTask: PeriodicWorkRequest

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val settingViewModel = ViewModelProvider(this,SettingViewModelFactory(pref))[SettingViewModel::class.java]
        workManager = WorkManager.getInstance(requireContext())
        //dark theme
        settingViewModel.getThemeSettings().observe(viewLifecycleOwner){
            isDarkMode ->
            if(isDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding?.swSettingTheme?.isChecked = true
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding?.swSettingTheme?.isChecked = false
            }
        }



        binding?.swSettingTheme?.setOnCheckedChangeListener{ _:CompoundButton? , isChecked:Boolean ->
            settingViewModel.saveThemeSetting(isChecked)

        }

        //notification
        binding?.swSettingBroadcast?.setOnCheckedChangeListener { _: CompoundButton?, isChecked ->
         if(isChecked){
             onNotificationActive()
         }else{
             cancelNotification()
         }
        }
    }

    private fun onNotificationActive(){

            val data = Data.Builder()
                .putString("UPCOMING EVENT", "upcoming event dicoding")
                .build()
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            periodicWokTask = PeriodicWorkRequest.Builder(MyWorker::class.java, 1, TimeUnit.DAYS)
                .setInputData(data)
                .setConstraints(constraints)
                .build()
            workManager.enqueue(periodicWokTask)
//            val onTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
//                .setInputData(data)
//                .setConstraints(constraints)
//                .build()
//            workManager.enqueue(onTimeWorkRequest)

//            workManager.getWorkInfoByIdLiveData(onTimeWorkRequest.id)
//                .observe()
//

    }

    private fun cancelNotification(){
        Toast.makeText(requireContext(), "work manager dibatalkan", Toast.LENGTH_SHORT).show()
        workManager.cancelWorkById(periodicWokTask.id)
    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}