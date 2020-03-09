package com.aco.pmu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aco.pmu.clients.ClientsFragment
import com.aco.pmu.more.MoreFragment
import com.aco.pmu.records.RecordsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            val fragment = RecordsFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
        }
    }


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_records -> {
                    val fragment = RecordsFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit()

                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_clients -> {
                    val fragment = ClientsFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit()

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_analytics -> {
                    val fragment =
                        AnalyticsFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit()

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_more -> {
                    val fragment = MoreFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit()

                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        supportFragmentManager
//            .findFragmentByTag(ClientsFragment::class.java.simpleName)
//            ?.onActivityResult(requestCode, resultCode, data)
//    }
}
