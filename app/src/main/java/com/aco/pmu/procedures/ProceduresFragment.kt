package com.aco.pmu.procedures

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
import com.aco.pmu.appDatabase.ProceduresEntity
import kotlinx.android.synthetic.main.fragment_procedures.*
import kotlinx.android.synthetic.main.fragment_procedures.toolbar

class ProceduresFragment : Fragment() {

    companion object {
        const val ADD_PROCEDURE_REQUEST = 1
        const val EDIT_PROCEDURE_REQUEST = 2
    }

    private lateinit var proceduresViewModel: ProceduresViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_procedures, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        floatingActionButton.setOnClickListener {
            startActivityForResult(
                Intent(activity, AddProcedureActivity::class.java),
                ADD_PROCEDURE_REQUEST
            )
        }

        toolbar.title = "Процедуры"

        recycler_view.layoutManager = LinearLayoutManager(activity)

        var adapter = ProceduresAdapter()

        recycler_view.adapter = adapter

        proceduresViewModel = ViewModelProviders.of(this).get(ProceduresViewModel::class.java)

        proceduresViewModel.getAllProcedures().observe(this, Observer<List<ProceduresEntity>> {
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
                var procedureToDelete = adapter.getProcedureAt(viewHolder.adapterPosition)

                val alertDialog = AlertDialog.Builder(activity)
                alertDialog.setTitle("Удаление процедуры")
                alertDialog.setMessage("Вы действительно хотите удалить ${procedureToDelete.procedureName}?")

                alertDialog.setPositiveButton("Да") { dialog, which ->
                    proceduresViewModel.delete(procedureToDelete)
                    Toast.makeText(
                        activity,
                        "${procedureToDelete.procedureName} удален(а) из списка Ваших процедур!",
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

        adapter.setOnItemClickListener(object : ProceduresAdapter.OnItemClickListener {
            override fun onItemClick(proceduresEntity: ProceduresEntity) {
                var intent = Intent(activity, AddProcedureActivity::class.java)
                intent.putExtra(AddProcedureActivity.EXTRA_ID, proceduresEntity.id)
                intent.putExtra(AddProcedureActivity.EXTRA_PROCEDURENAME, proceduresEntity.procedureName)

                startActivityForResult(intent,
                    EDIT_PROCEDURE_REQUEST
                )
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_PROCEDURE_REQUEST && resultCode == Activity.RESULT_OK) {
            val newProcedure = ProceduresEntity(
                data!!.getStringExtra(AddProcedureActivity.EXTRA_PROCEDURENAME)
                )
            proceduresViewModel.insert(newProcedure)

            Toast.makeText(
                activity,
                "${newProcedure.procedureName} добавлен(а) в список Ваших процедур!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (requestCode == EDIT_PROCEDURE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(AddProcedureActivity.EXTRA_ID, -1)

            if (id == -1) {
                Toast.makeText(
                    activity,
                    "Невозможно обновить! Упс... видимо что-то случилось!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            val updateProcedure = ProceduresEntity(
                data!!.getStringExtra(AddProcedureActivity.EXTRA_PROCEDURENAME)
            )
            updateProcedure.id = data.getIntExtra(AddProcedureActivity.EXTRA_ID, -1)
            proceduresViewModel.update(updateProcedure)

        }
    }
}



