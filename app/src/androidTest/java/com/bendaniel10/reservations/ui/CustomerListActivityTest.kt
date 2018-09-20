package com.bendaniel10.reservations.ui

import android.support.test.rule.ActivityTestRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CustomerListActivityTest {


    @get:Rule
    val activityRule = ActivityTestRule(CustomerListActivity::class.java)

    private lateinit var activity: CustomerListActivity

    @Before
    fun setUp() {

        this.activity = activityRule.activity

    }

    @Test
    fun activityShouldSetLateInitVariables() {

        Assert.assertNotNull("Binding must not be null after activity has been launched.", activity.binding)
        Assert.assertNotNull("View model must not be null after activity has been launched.", activity.viewModel)
        Assert.assertNotNull("Customer position updated broadcast receiver must not be null after activity has been launched.", activity.customerPositionUpdatedBroadcastReceiver)
        Assert.assertNotNull("Reservation broadcast receiver must not be null after activity has been launched.", activity.reservationClearedBroadcastReceiver)
        Assert.assertNotNull("Recycler view adapter must not be null after activity has been launched.", activity.customerListAdapter)

    }
}