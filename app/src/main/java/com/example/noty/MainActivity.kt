package com.example.noty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.noty.data.model.Task
import com.example.noty.navigator.Navigator
import com.example.noty.ui.add.AddFragment
import com.example.noty.ui.list.ListFragment
import com.example.noty.ui.setting.SettingFragment

class MainActivity : AppCompatActivity(), Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val editFragment = intent.getStringExtra(KEY_TO_NAVIGATE)
//        if (editFragment != null) {
//            if (editFragment == ADD_FRAGMENT) {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, AddFragment())
//                    .commit()
//            }
//        }else {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container, ListFragment()
        ).commit()

//        supportFragmentManager.commit {
//            setCustomAnimations(
//                enter = R.anim.slide_in,
//                exit = R.anim.fade_out,
//                popEnter = R.anim.fade_in,
//                popExit = R.anim.slide_out)
//            replace(R.id.fragment_container, ListFragment())
//        }
    }


    override fun launch(task: Any?) {
        when (task) {
            is Task -> {
                launchFragment(AddFragment.newInstance(task))
            }
            null -> {
                launchFragment(AddFragment())
            }
        }
    }

    override fun goBack() {
        supportFragmentManager.popBackStack()
    }

    override fun update(task: Task) {

    }

    override fun launchSetting() {
        launchFragment(SettingFragment())
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}