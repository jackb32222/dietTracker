package com.diet.tracker.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.diet.tracker.R
import com.diet.tracker.databinding.ActivityWeightBinding
import com.diet.tracker.datasource.model.UserInfo
import com.diet.tracker.datasource.model.Weight
import com.diet.tracker.ui.adapter.WeightAdapter
import com.diet.tracker.viewmodel.AuthViewModel
import com.diet.tracker.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeightActivity : AppCompatActivity(), WeightAdapter.OnWeightChangeListener {

    private lateinit var binding: ActivityWeightBinding

    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private var userInfo: UserInfo = UserInfo()
    private val weightAdapter: WeightAdapter by lazy {
        WeightAdapter(userInfo.weights, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weight)

        initViews()
        initObserver()
        userViewModel.getUserInfo(authViewModel.getUserId())
    }

    private fun initViews() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.log_weight)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.rvWeight.adapter = weightAdapter
    }

    private fun initObserver() {
        userViewModel.lvUser.observe(this, object : Observer<UserInfo> {
            override fun onChanged(userInfo: UserInfo?) {
                userViewModel.lvUser.removeObserver(this)
                renderWeight(userInfo)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_weight, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuRefresh -> refresh()
            else -> {}
        }
        return true
    }

    private fun renderWeight(userInfo: UserInfo?) {
        this.userInfo.day = userInfo?.day ?: 1
        this.userInfo.weights = userInfo?.weights ?: UserInfo.defaultWeights()

        updateAdapter()
    }

    private fun refresh() {
        userInfo.run {
            weights.forEach { it.weight = 0.0 }
            updateAdapter()

            userViewModel.saveUserInfo(
                userId = authViewModel.getUserId(),
                userInfo = this
            )
        }
    }

    private fun updateAdapter() {
        weightAdapter.weightList = this.userInfo.weights
        weightAdapter.notifyDataSetChanged()
    }

    override fun onWeightChanged(position: Int, weight: Double) {
        userInfo.run {
            weights[position].weight = weight
            weights[position].day = position + 1
            userViewModel.saveUserInfo(
                userId = authViewModel.getUserId(),
                userInfo = this
            )
        }
    }
}