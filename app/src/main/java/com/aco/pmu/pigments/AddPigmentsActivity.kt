package com.aco.pmu.pigments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aco.pmu.R
import kotlinx.android.synthetic.main.activity_add_pigments.*


class AddPigmentsActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_PIGMENTSNAME = "EXTRA_PIGMENTSNAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pigments)

        if (intent.hasExtra(EXTRA_ID)) {
        toolbarLabel.text = "Редактировать пигмент"
            pigmentsNameEditText.setText(intent.getStringExtra(EXTRA_PIGMENTSNAME))

        } else {
            toolbarLabel.text = "Новый пигмент"
        }

        ic_menu_done_pigment.setOnClickListener {
            savePigment()
        }

        ic_menu_close_pigment.setOnClickListener {
            finish()
        }
    }

    private fun savePigment() {
        if (pigmentsNameEditText.text.toString().trim().isBlank()) {
            Toast.makeText(baseContext, "Введите пигмент!", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent().apply {
            putExtra(EXTRA_PIGMENTSNAME, pigmentsNameEditText.text.toString())
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



