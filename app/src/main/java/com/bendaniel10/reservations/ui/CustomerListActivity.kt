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
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.bendaniel10.reservations.R
import com.bendaniel10.reservations.backgroundjob.ClearReservationWorker
import com.bendaniel10.reservations.databinding.CustomerListActivityBinding
import com.bendaniel10.reservations.di.module.CustomerListModule
import com.bendaniel10.reservations.paging.CustomerListAdapter
import com.bendaniel10.reservations.viewmodel.CustomerListViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class CustomerListActivity : DaggerAppCompatActivity() {


    @Inject
    lateinit var viewModelProviderFactory: CustomerListModule.CustomerListViewModelProviderFactory

    lateinit var viewModel: CustomerListViewModel

    lateinit var binding: CustomerListActivityBinding

    lateinit var customerListAdapter: CustomerListAdapter

    lateinit var customerPositionUpdatedBroadcastReceiver: BroadcastReceiver

    lateinit var reservationClearedBroadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(CustomerListViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.customer_list_activity)

        setAdapter()

        setUpListUpdateObserver()

        setUpListItemSelectionObserver()

        customerPositionUpdatedBroadcastReceiver = buildCustomerPositionUpdatedBroadcastReceiver()

        LocalBroadcastManager.getInstance(this).registerReceiver(customerPositionUpdatedBroadcastReceiver, IntentFilter(RestaurantTableListActivity.INTENT_ACTION_CUSTOMER_POSITION_REFRESHED))

        reservationClearedBroadcastReceiver = buildReservationClearedBroadcastReceiver()

        LocalBroadcastManager.getInstance(this).registerReceiver(reservationClearedBroadcastReceiver, IntentFilter(ClearReservationWorker.INTENT_ACTION_RESERVATIONS_CLEARED))

    }

    private fun buildReservationClearedBroadcastReceiver(): BroadcastReceiver {

        return object : BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {

                customerListAdapter.notifyDataSetChanged()

            }

        }
    }

    private fun buildCustomerPositionUpdatedBroadcastReceiver(): BroadcastReceiver {

        return object : BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {

                if (intent == null) {

                    return

                }

                val customerPosition = intent.getIntExtra(RestaurantTableListActivity.INTENT_EXTRA_CUSTOMER_POSITION, -1)

                if (customerPosition == -1) {

                    return

                }

                customerListAdapter.notifyItemChanged(customerPosition)

            }

        }

    }


    private fun setAdapter() {

        val recyclerView = binding.customersListRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(this)

        val horizontalRuleDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        recyclerView.addItemDecoration(horizontalRuleDecoration)

        this.customerListAdapter = CustomerListAdapter(viewModel.selectedCustomer, viewModel.reservationDao)

        recyclerView.adapter = customerListAdapter

    }


    private fun setUpListUpdateObserver() {

        viewModel.customersPagedListLiveData.observe(this, Observer {

            customerListAdapter.submitList(it)

        })

    }


    private fun setUpListItemSelectionObserver() {

        viewModel.selectedCustomer.observe(this, Observer {

            val intent = Intent(this, RestaurantTableListActivity::class.java)

            intent.putExtra(RestaurantTableListActivity.INTENT_EXTRA_POSITION_CUSTOMER_PAIR, it)

            startActivity(intent)

        })

    }

    override fun onDestroy() {
        super.onDestroy()

        LocalBroadcastManager.getInstance(this).unregisterReceiver(customerPositionUpdatedBroadcastReceiver)

        LocalBroadcastManager.getInstance(this).unregisterReceiver(reservationClearedBroadcastReceiver)

    }
}
