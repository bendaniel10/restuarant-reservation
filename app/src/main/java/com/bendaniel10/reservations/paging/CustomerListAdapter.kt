package com.bendaniel10.reservations.paging

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bendaniel10.reservations.R
import com.bendaniel10.reservations.database.dao.ReservationDao
import com.bendaniel10.reservations.database.entity.Customer
import com.bendaniel10.reservations.databinding.ListItemCustomerBinding
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class CustomerListAdapter(private val selectedCustomer: MutableLiveData<Pair<Int, Customer>>,
                          private val reservationDao: ReservationDao) : PagedListAdapter<Customer,
        CustomerListAdapter.CustomerListViewHolder>(CustomerListAdapter.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerListViewHolder {


        val binding = DataBindingUtil.inflate<ListItemCustomerBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_item_customer,
                parent,
                false
        )

        return CustomerListViewHolder(binding)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomerListViewHolder, position: Int) {

        val customer = getItem(position)
        val binding = holder.binding
        val context = holder.binding.root.context

        if (customer != null) {

            val customerHasAReservation = doesCustomerHaveAReservation(customer.id)

            holder.binding.customerFullnameTextField.text = """${customer.customerFirstName} ${customer.customerLastName}"""

            holder.binding.tableReservationStatusTxt.text = if (customerHasAReservation) context.getString(R.string.has_a_reservation) else context.getString(R.string.no_reservation)

            binding.root.setOnClickListener {

                selectedCustomer.postValue(Pair(position, customer))

            }


        }
    }

    //Using blocking Get wrapper since room doesn't allow queries on the main thread. This query doesn't really have much impact on performance
    private fun doesCustomerHaveAReservation(customerId: Int): Boolean {

        return Single.fromCallable<Boolean> {

            reservationDao.countReservationByCustomer(customerId) > 0

        }.subscribeOn(Schedulers.io())
                .blockingGet()

    }


    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Customer>() {

            override fun areItemsTheSame(oldItem: Customer?, newItem: Customer?): Boolean {

                return oldItem?.id?.equals(newItem?.id) ?: false

            }

            override fun areContentsTheSame(oldItem: Customer?, newItem: Customer?): Boolean {

                return oldItem?.id?.equals(newItem?.id) ?: false

            }

        }

    }


    class CustomerListViewHolder(val binding: ListItemCustomerBinding) : RecyclerView.ViewHolder(binding.root)

}