package com.bendaniel10.reservations.ui

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import android.view.ViewGroup
import com.bendaniel10.reservations.database.entity.Customer
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RestaurantTableListActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(RestaurantTableListActivity::class.java, false, false)

    @Test
    fun activityShouldSetLateInitVariables() {

        val customer = Customer(1,"Foo", "Bar")

        val intent = Intent()
        intent.putExtra(RestaurantTableListActivity.INTENT_EXTRA_POSITION_CUSTOMER_PAIR, Pair(1, customer))

        val activity = activityRule.launchActivity(intent)

        Assert.assertNotNull("Binding must not be null after activity has been launched.", activity.binding)
        Assert.assertNotNull("View model provider factory must not be null after activity has been launched.", activity.viewModelProviderFactory)
        Assert.assertNotNull("Customer must not be null after activity has been launched.", activity.currentCustomer)
        Assert.assertNotNull("Broadcast receiver must not be null after activity has been launched.", activity.reservationClearedBroadcastReceiver)
        Assert.assertNotNull("View model must not be null after activity has been launched.", activity.viewModel)
        Assert.assertNotNull("Recycler view adapter must not be null after activity has been launched.", activity.restaurantTableListAdapter)

    }

    @Test
    fun activityShouldFinishWithoutRequiredExtra() {

        val badActivity = activityRule.launchActivity(Intent())
        val activityRootView = (badActivity
                .findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup?

        Assert.assertNull("Activity must return/finish() if required intent extras weren't passed", activityRootView)

    }

}