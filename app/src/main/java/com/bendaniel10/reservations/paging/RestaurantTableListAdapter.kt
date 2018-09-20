package com.bendaniel10.reservations.paging

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bendaniel10.reservations.R
import com.bendaniel10.reservations.database.dao.ReservationDao
import com.bendaniel10.reservations.database.entity.RestaurantTable
import com.bendaniel10.reservations.databinding.ListRestaurantItemTablesBinding
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RestaurantTableListAdapter(private val selectedRestaurantTable: MutableLiveData<Pair<Int, RestaurantTable>>, private val reservationDao: ReservationDao) : PagedListAdapter<RestaurantTable, RestaurantTableListAdapter.RestaurantTableListViewHolder>(RestaurantTableListAdapter.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantTableListViewHolder {

        val binding = DataBindingUtil.inflate<ListRestaurantItemTablesBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_restaurant_item_tables,
                parent,
                false
        )

        return RestaurantTableListViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RestaurantTableListViewHolder, position: Int) {

        val restaurantTable = getItem(position)
        val binding = holder.binding
        val context = holder.binding.root.context

        if (restaurantTable != null) {

            val isTableReserved = isTableReserved(restaurantTable.id)

            val tableStatusText: String

            tableStatusText = if (restaurantTable.available && isTableReserved) {

                context.getString(R.string.reserved)

            } else if (restaurantTable.available) {

                context.getString(R.string.available)

            } else {

                context.getString(R.string.unavailable)

            }

            binding.tableStatus.text = tableStatusText

            val tableDrawable = if (restaurantTable.available && !isTableReserved) R.drawable.ic_table_available else R.drawable.ic_table_not_available
            binding.tableImageView.setImageDrawable(ContextCompat.getDrawable(context, tableDrawable))

            binding.root.setOnClickListener {

                selectedRestaurantTable.postValue(Pair(position, restaurantTable))

            }

        }

    }


    //Using blocking Get wrapper since room doesn't allow queries on the main thread. This query doesn't really have much impact on performance
    private fun isTableReserved(restaurantTableId: Int): Boolean {

        return Single.fromCallable<Boolean> {

            reservationDao.countReservationByRestaurantTable(restaurantTableId) > 0

        }.subscribeOn(Schedulers.io())
                .blockingGet()

    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RestaurantTable>() {

            override fun areItemsTheSame(oldItem: RestaurantTable?, newItem: RestaurantTable?): Boolean {

                return oldItem?.id?.equals(newItem?.id) ?: false

            }

            override fun areContentsTheSame(oldItem: RestaurantTable?, newItem: RestaurantTable?): Boolean {

                return oldItem?.id?.equals(newItem?.id) ?: false

            }

        }

    }


    class RestaurantTableListViewHolder(val binding: ListRestaurantItemTablesBinding) : RecyclerView.ViewHolder(binding.root)

}