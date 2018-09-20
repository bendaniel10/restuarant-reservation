package com.bendaniel10.reservations.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bendaniel10.reservations.R
import com.bendaniel10.reservations.app.NetworkState
import com.bendaniel10.reservations.backgroundjob.ClearReservationWorker
import com.bendaniel10.reservations.database.entity.Customer
import com.bendaniel10.reservations.databinding.ActivityRestaurantTableListBinding
import com.bendaniel10.reservations.di.module.RestaurantTableListModule
import com.bendaniel10.reservations.paging.RestaurantTableListAdapter
import com.bendaniel10.reservations.viewmodel.RestaurantTableListViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class RestaurantTableListActivity : DaggerAppCompatActivity() {


    @Inject
    lateinit var viewModelProviderFactory: RestaurantTableListModule.RestaurantTableListViewModelProviderFactory

    lateinit var viewModel: RestaurantTableListViewModel

    lateinit var binding: ActivityRestaurantTableListBinding

    lateinit var restaurantTableListAdapter: RestaurantTableListAdapter

    lateinit var currentCustomer: Customer

    private var currentCustomerPosition: Int = 0

    var reservationClearedBroadcastReceiver: BroadcastReceiver? = null


    companion object {

        const val INTENT_EXTRA_POSITION_CUSTOMER_PAIR = "com.bendaniel10.reservations.ui.RestaurantTableListActivity.INTENT_EXTRA_POSITION_CUSTOMER_PAIR"
        const val INTENT_EXTRA_CUSTOMER_POSITION = "com.bendaniel10.reservations.ui.RestaurantTableListActivity.INTENT_EXTRA_CUSTOMER_POSITION"
        const val INTENT_ACTION_CUSTOMER_POSITION_REFRESHED = "com.bendaniel10.reservations.ui.RestaurantTableListActivity.INTENT_ACTION_CUSTOMER_POSITION_REFRESHED"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!intent.hasExtra(INTENT_EXTRA_POSITION_CUSTOMER_PAIR)) {

            finish()

            return

        }

        @Suppress("UNCHECKED_CAST")
        val positionCustomerPair: Pair<Int, Customer> = intent.getSerializableExtra(INTENT_EXTRA_POSITION_CUSTOMER_PAIR) as Pair<Int, Customer>

        currentCustomer = positionCustomerPair.second
        currentCustomerPosition = positionCustomerPair.first

        viewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(RestaurantTableListViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_table_list)

        setAdapter()

        setUpListUpdateObserver()

        setUpListItemSelectionObserver()

        setUpProgressBarObserver()

        setUpNewReservationNotification()

        reservationClearedBroadcastReceiver = buildReservationClearedBroadcastReceiver()

        LocalBroadcastManager.getInstance(this).registerReceiver(reservationClearedBroadcastReceiver!!, IntentFilter(ClearReservationWorker.INTENT_ACTION_RESERVATIONS_CLEARED))

    }

    private fun setUpNewReservationNotification() {

        viewModel.newReservationsLiveData.observe(this, Observer {

            Toast.makeText(this, R.string.reservation_successful, Toast.LENGTH_SHORT).show()

        })
    }


    private fun buildReservationClearedBroadcastReceiver(): BroadcastReceiver {

        return object : BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {

                restaurantTableListAdapter.notifyDataSetChanged()

            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == android.R.id.home) {

            onBackPressed()

            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun setAdapter() {

        val recyclerView = binding.restaurantTableRecyclerView

        recyclerView.layoutManager = GridLayoutManager(this, 3)

        this.restaurantTableListAdapter = RestaurantTableListAdapter(viewModel.selectedRestaurantTable, viewModel.reservationDao)

        recyclerView.adapter = restaurantTableListAdapter

    }


    private fun setUpListUpdateObserver() {

        viewModel.restaurantTablePagedListLiveData.observe(this, Observer {

            restaurantTableListAdapter.submitList(it)

        })

    }


    private fun setUpProgressBarObserver() {

        viewModel.networkStateLiveData.observe(this, Observer {

            val progressBarVisibility = when (it) {

                NetworkState.LOADING -> View.VISIBLE
                NetworkState.LOADED -> View.GONE
                NetworkState.FAILED -> View.GONE
                else -> View.GONE
            }

            binding.progressBar.visibility = progressBarVisibility

        })

    }

    private fun setUpListItemSelectionObserver() {

        viewModel.selectedRestaurantTable.observe(this, Observer {

            if (it != null && it.second.available) {

                viewModel.reserveTableForCustomer(it.second, currentCustomer)

                restaurantTableListAdapter.notifyItemChanged(it.first)

                val broadcastIntent = Intent(INTENT_ACTION_CUSTOMER_POSITION_REFRESHED).apply {

                    this.putExtra(INTENT_EXTRA_CUSTOMER_POSITION, currentCustomerPosition)

                }

                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)

            } else {

                Toast.makeText(this, R.string.unavailable, Toast.LENGTH_SHORT).show()

            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()

        reservationClearedBroadcastReceiver?.let { LocalBroadcastManager.getInstance(this).unregisterReceiver(it) }


    }
}
