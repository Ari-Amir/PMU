package com.aco.pmu.clients

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aco.pmu.R
import com.aco.pmu.appDatabase.ClientsEntity
import kotlinx.android.synthetic.main.fragment_clients.*

class ClientsFragment : Fragment() {

    companion object {
        const val ADD_CLIENT_REQUEST = 1
        const val EDIT_CLIENT_REQUEST = 2
    }

    private lateinit var clientsViewModel: ClientsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_clients, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        floatingActionButton.setOnClickListener {
            startActivityForResult(
                Intent(activity, AddClientActivity::class.java),
                ADD_CLIENT_REQUEST
            )
        }

        toolbar.title = "Клиенты"

        recycler_view.layoutManager = LinearLayoutManager(activity)

        var adapter = ClientsAdapter()

        recycler_view.adapter = adapter

        clientsViewModel = ViewModelProviders.of(this).get(ClientsViewModel::class.java)

        clientsViewModel.getAllClients().observe(viewLifecycleOwner, Observer<List<ClientsEntity>> {
            adapter.submitList(it)
        })

        clientsViewModel.getClientsCount().observe(viewLifecycleOwner, object : Observer<Int> {
            override fun onChanged(@Nullable integer: Int) {
                text_view_clientsCount.text = "Всего клиентов: ${integer.toString()}"
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
                var clientToDelete = adapter.getClientAt(viewHolder.adapterPosition)

                val alertDialog = AlertDialog.Builder(activity)
                alertDialog.setTitle("Удаление контакта")
                alertDialog.setMessage("Вы действительно хотите удалить ${clientToDelete.firstName} ${clientToDelete.lastName}?")

                alertDialog.setPositiveButton("Да") { dialog, which ->
                    clientsViewModel.delete(clientToDelete)
                    Toast.makeText(
                        activity,
                        "${clientToDelete.firstName} ${clientToDelete.lastName} удален(а) из списка Ваших контактов!",
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

        adapter.setOnItemClickListener(object : ClientsAdapter.OnItemClickListener {
            override fun onItemClick(clientsEntity: ClientsEntity) {
                var intent = Intent(activity, AddClientActivity::class.java)
                intent.putExtra(AddClientActivity.EXTRA_ID, clientsEntity.id)
                intent.putExtra(AddClientActivity.EXTRA_FIRSTNAME, clientsEntity.firstName)
                intent.putExtra(AddClientActivity.EXTRA_LASTNAME, clientsEntity.lastName)
                intent.putExtra(AddClientActivity.EXTRA_PHONENUMBER1, clientsEntity.phoneNumber1)
                intent.putExtra(AddClientActivity.EXTRA_PHONENUMBER2, clientsEntity.phoneNumber2)
                intent.putExtra(AddClientActivity.EXTRA_CLIENTREMARK, clientsEntity.clientRemark)

                startActivityForResult(intent,
                    EDIT_CLIENT_REQUEST
                )
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_CLIENT_REQUEST && resultCode == Activity.RESULT_OK) {
            val newClient = ClientsEntity(
                data!!.getStringExtra(AddClientActivity.EXTRA_FIRSTNAME),
                data.getStringExtra(AddClientActivity.EXTRA_LASTNAME),
                data.getStringExtra(AddClientActivity.EXTRA_PHONENUMBER1),
                data.getStringExtra(AddClientActivity.EXTRA_PHONENUMBER2),
                data.getStringExtra(AddClientActivity.EXTRA_CLIENTREMARK)

                )
            clientsViewModel.insert(newClient)

            Toast.makeText(
                activity,
                "${newClient.firstName} ${newClient.lastName} добавлен(а) в список Ваших контактов!",
                Toast.LENGTH_SHORT
            ).show()

        } else if (requestCode == EDIT_CLIENT_REQUEST && resultCode == Activity.RESULT_OK) {

            val updateClient = ClientsEntity(
                data!!.getStringExtra(AddClientActivity.EXTRA_FIRSTNAME),
                data.getStringExtra(AddClientActivity.EXTRA_LASTNAME),
                data.getStringExtra(AddClientActivity.EXTRA_PHONENUMBER1),
                data.getStringExtra(AddClientActivity.EXTRA_PHONENUMBER2),
                data.getStringExtra(AddClientActivity.EXTRA_CLIENTREMARK)
            )
            updateClient.id = data.getIntExtra(AddClientActivity.EXTRA_ID, -1)
            clientsViewModel.update(updateClient)

        }
    }
}


