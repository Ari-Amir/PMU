package com.aco.pmu.procedures

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aco.pmu.R
import kotlinx.android.synthetic.main.activity_add_procedure.*


class AddProcedureActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_PROCEDURENAME = "EXTRA_PROCEDURENAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_procedure)

        if (intent.hasExtra(EXTRA_ID)) {
        toolbarLabel.text = "Редактировать процедуру"
            procedureNameEditText.setText(intent.getStringExtra(EXTRA_PROCEDURENAME))

        } else {
            toolbarLabel.text = "Новая процедура"
        }

        ic_menu_done_procedure.setOnClickListener {
            saveProcedure()
        }

        ic_menu_close_procedure.setOnClickListener {
            finish()
        }
    }

    private fun saveProcedure() {
        if (procedureNameEditText.text.toString().trim().isBlank()) {
            Toast.makeText(baseContext, "Введите процедуру!", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent().apply {
            putExtra(EXTRA_PROCEDURENAME, procedureNameEditText.text.toString())
            if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
                putExtra(
                    EXTRA_ID, intent.getIntExtra(
                        EXTRA_ID, -1))
            }
        }

        setResult(Activity.RESULT_OK, data)
        finish()
    }
}



