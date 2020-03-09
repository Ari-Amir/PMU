package com.aco.pmu.records

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aco.pmu.R
import com.aco.pmu.appDatabase.Converters
import com.aco.pmu.appDatabase.RecordsEntity
import kotlinx.android.synthetic.main.fragment_records.*
import kotlinx.android.synthetic.main.fragment_records.toolbar
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class RecordsFragment : Fragment(), CalendarView.CalendarListener {

    companion object {
        const val ADD_RECORD_REQUEST = 1
        const val EDIT_RECORD_REQUEST = 2
    }

    private lateinit var recordsViewModel: RecordsViewModel
    private lateinit var calendarView: CalendarView
    val adapter = RecordsAdapter()

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_records, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        calendarView = view!!.findViewById(R.id.calendarPicker) as CalendarView
        calendarView.setCalendarListener(this)
        calendarView.date = Date()

        recordsViewModel = ViewModelProviders.of(this).get(RecordsViewModel::class.java)
        getDatesFromRoom()

        floatingActionButton.setOnClickListener {
            startActivityForResult(
                Intent(activity, AddRecordActivity::class.java),
                ADD_RECORD_REQUEST
            )
        }

        recycler_view.layoutManager = LinearLayoutManager(activity)

        recycler_view.adapter = adapter

        val dateToday = DateTimeFormatter
            .ofPattern("d MMMM YYYY", Locale.ENGLISH)
            .withZone(ZoneOffset.systemDefault())
            .format(Instant.now())

        getDatesFromRoom()

        val dateTodayParsed = Date.parse(dateToday)

        recordsViewModel.getRecordsASelectedtDate(dateTodayParsed, dateTodayParsed)
            .observeOnce(viewLifecycleOwner, Observer<List<RecordsEntity>> {
                    adapter.submitList(it)
            })


        recordsViewModel.getRecordsCount().observe(viewLifecycleOwner, object : Observer<Int> {
            override fun onChanged(@Nullable integer: Int) {
                toolbar.title = "Записи ($integer)"
            }
        })

            ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var recordToDelete = adapter.getRecordAt(viewHolder.adapterPosition)

                val alertDialog = AlertDialog.Builder(activity)
                alertDialog.setTitle("Удаление записи")
                alertDialog.setMessage("Вы действительно хотите удалить ${recordToDelete.firstAndlastNames}?")

                alertDialog.setPositiveButton("Да") { dialog, which ->
                    recordsViewModel.delete(recordToDelete)
                    Toast.makeText(
                        activity,
                        "${recordToDelete.firstAndlastNames} удален(а) из Ваших записей!",
                        Toast.LENGTH_SHORT
                    ).show()

                    calendarView.updateView()
                    getDatesFromRoom()

                    recordsViewModel.getRecordsASelectedtDate(recordToDelete.date!!.toLong(), recordToDelete.date!!.toLong())
                        .observeOnce(viewLifecycleOwner, Observer<List<RecordsEntity>> {
                            if (it != null)
                                adapter.submitList(it)
                        })
                }

                alertDialog.setNegativeButton("Нет") { _, _ ->
                    adapter.notifyItemChanged(
                        viewHolder.adapterPosition
                    )
                }

                alertDialog.setOnCancelListener {
                    onDismissAlertDialog(viewHolder)
                }

                alertDialog.show()
            }

            fun onDismissAlertDialog (viewHolder: RecyclerView.ViewHolder) {
                adapter.notifyItemChanged(
                    viewHolder.adapterPosition
                )
            }
        }
        ).attachToRecyclerView(recycler_view)

        adapter.setOnItemClickListener(object : RecordsAdapter.OnItemClickListener {
            override fun onItemClick(recordsEntity: RecordsEntity) {
                var intent = Intent(activity, AddRecordActivity::class.java)
                intent.putExtra(AddRecordActivity.EXTRA_ID, recordsEntity.id)
                intent.putExtra(AddRecordActivity.EXTRA_DATE, Converters().dateFromTimestamp(recordsEntity.date))
                intent.putExtra(AddRecordActivity.EXTRA_TIME, recordsEntity.time)
                intent.putExtra(AddRecordActivity.EXTRA_FIRSTANDLASTNAME, recordsEntity.firstAndlastNames)
                intent.putExtra(AddRecordActivity.EXTRA_PHONENUMBER1, recordsEntity.phoneNumber1)
                intent.putExtra(AddRecordActivity.EXTRA_PHONENUMBER2, recordsEntity.phoneNumber2)
                intent.putExtra(AddRecordActivity.EXTRA_PROCEDURES, recordsEntity.procedure)
                intent.putExtra(AddRecordActivity.EXTRA_PIGMENT1, recordsEntity.pigmentName1)
                intent.putExtra(AddRecordActivity.EXTRA_PIGMENT2, recordsEntity.pigmentName2)
                intent.putExtra(AddRecordActivity.EXTRA_PIGMENT3, recordsEntity.pigmentName3)
                intent.putExtra(AddRecordActivity.EXTRA_PIGMENTQUANTITY1, recordsEntity.pigmentQuantity1)
                intent.putExtra(AddRecordActivity.EXTRA_PIGMENTQUANTITY2, recordsEntity.pigmentQuantity2)
                intent.putExtra(AddRecordActivity.EXTRA_PIGMENTQUANTITY3, recordsEntity.pigmentQuantity3)
                intent.putExtra(AddRecordActivity.EXTRA_PRICE, recordsEntity.price)
                intent.putExtra(AddRecordActivity.EXTRA_COMMENTS, recordsEntity.comments)
                intent.putExtra(AddRecordActivity.EXTRA_STATUS, recordsEntity.status)

                startActivityForResult(intent,
                    EDIT_RECORD_REQUEST
                )
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_RECORD_REQUEST && resultCode == Activity.RESULT_OK) {
            val newRecord = RecordsEntity(
                data!!.getLongExtra(AddRecordActivity.EXTRA_DATE, -1),
                data.getStringExtra(AddRecordActivity.EXTRA_TIME),
                data.getStringExtra(AddRecordActivity.EXTRA_FIRSTANDLASTNAME),
                data.getStringExtra(AddRecordActivity.EXTRA_PHONENUMBER1),
                data.getStringExtra(AddRecordActivity.EXTRA_PHONENUMBER2),
                data.getStringExtra(AddRecordActivity.EXTRA_PROCEDURES),
                data.getStringExtra(AddRecordActivity.EXTRA_PIGMENT1),
                data.getStringExtra(AddRecordActivity.EXTRA_PIGMENT2),
                data.getStringExtra(AddRecordActivity.EXTRA_PIGMENT3),
                data.getIntExtra(AddRecordActivity.EXTRA_PIGMENTQUANTITY1, -1),
                data.getIntExtra(AddRecordActivity.EXTRA_PIGMENTQUANTITY2, -1),
                data.getIntExtra(AddRecordActivity.EXTRA_PIGMENTQUANTITY3, -1),
                data.getStringExtra(AddRecordActivity.EXTRA_PRICE),
                data.getStringExtra(AddRecordActivity.EXTRA_COMMENTS),
                data.getBooleanExtra(AddRecordActivity.EXTRA_STATUS, false)

            )
            recordsViewModel.insert(newRecord)

            recordsViewModel.getRecordsASelectedtDate(newRecord.date!!.toLong(), newRecord.date!!.toLong())
                .observeOnce(viewLifecycleOwner, Observer<List<RecordsEntity>> {
                    if (it != null)
                        adapter.submitList(it)
                })

            val dateFromTimestamp = Date(newRecord.date!!)
            setAndMarkCurrentDate(dateFromTimestamp)
            getDatesFromRoom()

            Toast.makeText(
                activity,
                "${newRecord.firstAndlastNames} добавлен(а) в список Ваших записей!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (requestCode == EDIT_RECORD_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(AddRecordActivity.EXTRA_ID, -1)

//            if (id == -1) {
//                Toast.makeText(
//                    activity,
//                    "Невозможно обновить! Упс... видимо что-то случилось!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }

            val updateRecord = RecordsEntity(
                data!!.getLongExtra(AddRecordActivity.EXTRA_DATE, -1),
                data.getStringExtra(AddRecordActivity.EXTRA_TIME),
                data.getStringExtra(AddRecordActivity.EXTRA_FIRSTANDLASTNAME),
                data.getStringExtra(AddRecordActivity.EXTRA_PHONENUMBER1),
                data.getStringExtra(AddRecordActivity.EXTRA_PHONENUMBER2),
                data.getStringExtra(AddRecordActivity.EXTRA_PROCEDURES),
                data.getStringExtra(AddRecordActivity.EXTRA_PIGMENT1),
                data.getStringExtra(AddRecordActivity.EXTRA_PIGMENT2),
                data.getStringExtra(AddRecordActivity.EXTRA_PIGMENT3),
                data.getIntExtra(AddRecordActivity.EXTRA_PIGMENTQUANTITY1, -1),
                data.getIntExtra(AddRecordActivity.EXTRA_PIGMENTQUANTITY2, -1),
                data.getIntExtra(AddRecordActivity.EXTRA_PIGMENTQUANTITY3, -1),
                data.getStringExtra(AddRecordActivity.EXTRA_PRICE),
                data.getStringExtra(AddRecordActivity.EXTRA_COMMENTS),
                data.getBooleanExtra(AddRecordActivity.EXTRA_STATUS, false)
            )
            updateRecord.id = data.getIntExtra(AddRecordActivity.EXTRA_ID, -1)
            recordsViewModel.update(updateRecord)

            recordsViewModel.getRecordsASelectedtDate(updateRecord.date!!.toLong(), updateRecord.date!!.toLong())
                .observeOnce(viewLifecycleOwner, Observer<List<RecordsEntity>> {
                    if (it != null)
                        adapter.submitList(it)
                })


            val dateFromTimestamp = Date(updateRecord.date!!)
            setAndMarkCurrentDate(dateFromTimestamp)
            getDatesFromRoom()
        }
    }

    fun getDatesFromRoom() {
        val list: ArrayList<Long> = ArrayList()
        list.addAll(recordsViewModel.getRecordDates())
        calendarView.setHearts(list)
    }

    fun setAndMarkCurrentDate(date: Date) {
        calendarView.setCurrentDate(date)
        calendarView.markDayAsSelectedDay(date)
    }

    fun clearRecordsViewModel() {
        recordsViewModel.getRecordsASelectedtDate(0, 0)
            .observeOnce(viewLifecycleOwner, Observer<List<RecordsEntity>> {
                if (it != null)
                    adapter.submitList(it)
            })
    }

    override fun onDayClick(date: Date?) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val d = calendar.time.date
        val m = calendar.time.month
        val y = calendar.time.year
        val sdf = SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH)
        val newFormatDate = sdf.format(Date(y, m, d))
        val selectedDate = Date.parse(newFormatDate)
        recordsViewModel.getRecordsASelectedtDate(selectedDate, selectedDate)
                .observeOnce(viewLifecycleOwner, Observer<List<RecordsEntity>> {
                    if (it != null)
                    adapter.submitList(it)
                })
    }

    override fun onRightButtonClick() {
        getDatesFromRoom()
        clearRecordsViewModel()
    }

    override fun onLeftButtonClick() {
        getDatesFromRoom()
        clearRecordsViewModel()
    }
}
