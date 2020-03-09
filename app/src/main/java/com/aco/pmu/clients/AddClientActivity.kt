package com.aco.pmu.clients

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aco.pmu.records.RecordsAdapter
import com.aco.pmu.records.RecordsViewModel
import kotlinx.android.synthetic.main.activity_add_client.*
import kotlinx.android.synthetic.main.activity_add_client.toolbarLabel
import com.aco.pmu.R
import com.aco.pmu.appDatabase.RecordsEntity


class AddClientActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_FIRSTNAME = "EXTRA_FIRSTNAME"
        const val EXTRA_LASTNAME = "EXTRA_LASTNAME"
        const val EXTRA_PHONENUMBER1 = "EXTRA_PHONENUMBER1"
        const val EXTRA_PHONENUMBER2 = "EXTRA_PHONENUMBER2"
        const val EXTRA_CLIENTREMARK = "EXTRA_CLIENTREMARK"
    }

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)


        if (intent.hasExtra(EXTRA_ID)) {
            toolbarLabel.text = "Редактировать клиента"
            nameEditText.setText(intent.getStringExtra(EXTRA_FIRSTNAME))
            surnameEditText.setText(intent.getStringExtra(EXTRA_LASTNAME))
            phoneNumberEditText1.setMaskedText(intent.getStringExtra(EXTRA_PHONENUMBER1))
            phoneNumberEditText2.setMaskedText(intent.getStringExtra(EXTRA_PHONENUMBER2))
            clientRemarkEditText.setText(intent.getStringExtra(EXTRA_CLIENTREMARK))

        } else {
            toolbarLabel.text = "Новый клиент"
        }

        ic_menu_done_client.setOnClickListener {
            saveClient()
        }

        ic_menu_close_client.setOnClickListener {
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.records_recycler_view)
        val adapter = RecordsAdapter()


        recyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        recyclerView.adapter = adapter
        val recordsViewModel: RecordsViewModel =
            ViewModelProviders.of(this).get(RecordsViewModel::class.java)
        recordsViewModel.getAllRecordsOfCurrentClient(reformatPhoneNumber())
            .observeOnce(this, Observer<List<RecordsEntity>> {
                adapter.submitList(it)
            })
    }

    private fun saveClient() {
        if (nameEditText.text.toString().trim().isBlank()) {
            Toast.makeText(baseContext, "Введите имя!", Toast.LENGTH_SHORT).show()
            return
        }

        if (phoneNumberEditText1.unmaskedText.length < 10) {
            Toast.makeText(baseContext, "Введите номер телефона!", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent().apply {
            putExtra(EXTRA_FIRSTNAME, nameEditText.text.toString())
            putExtra(EXTRA_LASTNAME, surnameEditText.text.toString())
            putExtra(EXTRA_PHONENUMBER1, phoneNumberEditText1.unmaskedText.toString())
            putExtra(EXTRA_PHONENUMBER2, phoneNumberEditText2.unmaskedText.toString())
            putExtra(EXTRA_CLIENTREMARK, clientRemarkEditText.text.toString())
            if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
                putExtra(
                    EXTRA_ID, intent.getIntExtra(
                        EXTRA_ID, -1
                    )
                )
            }
        }

        setResult(Activity.RESULT_OK, data)
        finish()
    }

    fun reformatPhoneNumber(): String {
        val a = phoneNumberEditText1.text.toString()
        val b = a.replace("(", "")
        val c = b.replace(")", "")
        val d = c.replace("-", " ")
        return d
    }
}
