package com.aco.pmu.pigments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aco.pmu.R
import com.aco.pmu.appDatabase.PigmentsEntity
import kotlinx.android.synthetic.main.fragment_pigments.*

class PigmentsFragment : Fragment() {

    companion object {
        const val ADD_PIGMENTS_REQUEST = 1
        const val EDIT_PIGMENTS_REQUEST = 2
    }

    private lateinit var pigmentsViewModel: PigmentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_pigments, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        floatingActionButton.setOnClickListener {
            startActivityForResult(
                Intent(activity, AddPigmentsActivity::class.java),
                ADD_PIGMENTS_REQUEST
            )
        }

        toolbar.title = "Пигменты"

        recycler_view.layoutManager = LinearLayoutManager(activity)

        var adapter = PigmentsAdapter()

        recycler_view.adapter = adapter

        pigmentsViewModel = ViewModelProviders.of(this).get(PigmentsViewModel::class.java)

        pigmentsViewModel.getAlPigments().observe(this, Observer<List<PigmentsEntity>> {
            adapter.submitList(it)
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
                var pigmentToDelete = adapter.getPigmentAt(viewHolder.adapterPosition)

                val alertDialog = AlertDialog.Builder(activity)
                alertDialog.setTitle("Удаление бренда пигмента")
                alertDialog.setMessage("Вы действительно хотите удалить ${pigmentToDelete.pigmentName}?")

                alertDialog.setPositiveButton("Да") { dialog, which ->
                    pigmentsViewModel.delete(pigmentToDelete)
                    Toast.makeText(
                        activity,
                        "${pigmentToDelete.pigmentName} удален(а) из списка Ваших брендов пигментов!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                alertDialog.setNegativeButton("Нет") { dialog, which ->
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

        adapter.setOnItemClickListener(object : PigmentsAdapter.OnItemClickListener {
            override fun onItemClick(pigmentsEntity: PigmentsEntity) {
                var intent = Intent(activity, AddPigmentsActivity::class.java)
                intent.putExtra(AddPigmentsActivity.EXTRA_ID, pigmentsEntity.id)
                intent.putExtra(AddPigmentsActivity.EXTRA_PIGMENTSNAME, pigmentsEntity.pigmentName)

                startActivityForResult(intent,
                    EDIT_PIGMENTS_REQUEST
                )
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_PIGMENTS_REQUEST && resultCode == Activity.RESULT_OK) {
            val newPigment = PigmentsEntity(
                data!!.getStringExtra(AddPigmentsActivity.EXTRA_PIGMENTSNAME)
                )
            pigmentsViewModel.insert(newPigment)

            Toast.makeText(
                activity,
                "${newPigment.pigmentName} добавлен(а) в список Ваших брендов пигментов!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (requestCode == EDIT_PIGMENTS_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(AddPigmentsActivity.EXTRA_ID, -1)

            if (id == -1) {
                Toast.makeText(
                    activity,
                    "Невозможно обновить! Упс... видимо что-то случилось!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            val updatePigment = PigmentsEntity(
                data!!.getStringExtra(AddPigmentsActivity.EXTRA_PIGMENTSNAME)
            )
            updatePigment.id = data.getIntExtra(AddPigmentsActivity.EXTRA_ID, -1)
            pigmentsViewModel.update(updatePigment)

        }
    }
}



