package com.rk.weather.ui.main

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.jakewharton.threetenabp.AndroidThreeTen
import com.rk.weather.R
import com.rk.weather.databinding.ActivityMainBinding
import com.rk.weather.utills.hide
import com.rk.weather.utills.show

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val sharedPreferences by lazy   {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setupNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuBookmark -> {
                findNavController(R.id.container_fragment).navigate(R.id.bookmarkFragment)
                true
            }
            R.id.menuItemSearch -> {
                findNavController(R.id.container_fragment).navigate(R.id.searchFragment)
                true
            }
            else -> false
        }
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.container_fragment)
        binding.toolbar.navigationIcon?.setTint(Color.parseColor("#130e51"))
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.bookmarkFragment -> {
                    binding.toolbar.hide()
                }
                R.id.dashBoardFragment -> {
                    binding.toolbar.show()
                    binding.toolbar.menu.let { if (it.size() > 0) { it.getItem(0).setVisible(true) } }
                    binding.toolbar.menu.let { if (it.size() > 1) { it.getItem(1).setVisible(true) } }
                }
                R.id.searchFragment -> {
                    binding.toolbar.hide()
                }
                R.id.weatherDetailsFragment -> {
                    binding.toolbar.show()
                    binding.toolbar.menu.let { if (it.size() > 0) { it.getItem(0).setVisible(false) } }
                    binding.toolbar.menu.let { if (it.size() > 1) { it.getItem(1).setVisible(false) } }
                }
                else -> {
                    binding.toolbar.show()
                }
            }
        }
    }
}