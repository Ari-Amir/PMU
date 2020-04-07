package com.aco.pmu.records

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aco.pmu.R
import com.aco.pmu.clients.ClientsSelectFragment
import com.aco.pmu.pigments.PigmentsSelectFragment1
import com.aco.pmu.pigments.PigmentsSelectFragment2
import com.aco.pmu.pigments.PigmentsSelectFragment3
import com.aco.pmu.procedures.ProceduresSelectFragment
import com.aco.pmu.records.adapters.AdapterForAddRecordsActivity
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_add_record.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddRecordActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_DATE = "EXTRA_DATE"
        const val EXTRA_TIME = "EXTRA_TIME"
        const val EXTRA_FIRSTANDLASTNAME = "EXTRA_FIRSTANDLASTNAME"
        const val EXTRA_PHONENUMBER1 = "EXTRA_PHONENUMBER1"
        const val EXTRA_PHONENUMBER2 = "EXTRA_PHONENUMBER2"
        const val EXTRA_PROCEDURES = "EXTRA_PROCEDURES"
        const val EXTRA_PIGMENT1 = "EXTRA_PIGMENT1"
        const val EXTRA_PIGMENT2 = "EXTRA_PIGMENT2"
        const val EXTRA_PIGMENT3 = "EXTRA_PIGMENT3"
        const val EXTRA_PIGMENTQUANTITY1 = "EXTRA_PIGMENTQUANTITY1"
        const val EXTRA_PIGMENTQUANTITY2 = "EXTRA_PIGMENTQUANTITY2"
        const val EXTRA_PIGMENTQUANTITY3 = "EXTRA_PIGMENTQUANTITY3"
        const val EXTRA_PRICE = "EXTRA_PRICE"
        const val EXTRA_COMMENTS = "EXTRA_COMMENTS"
        const val EXTRA_STATUS = "EXTRA_STATUS"
        const val EXTRA_IMAGE_ATTR = "EXTRA_IMAGE_ATTR"
        const val EXTRA_IMAGE_POSITION = "EXTRA_IMAGE_POSITION"
        const val EXTRA_PHOTOS_PATH = "EXTRA_PHOTOS_PATH"
    }

    private var pickImageButton: Button? = null
    var adapter: AdapterForAddRecordsActivity? = null
    var images = ArrayList<Image>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)

        findViewById<View>(R.id.dateSelectTextView).setOnClickListener {
            clickDatePicker()
        }
        findViewById<View>(R.id.timeSelectTextView).setOnClickListener {
            clickTimePicker()
        }
        findViewById<View>(R.id.nameSurnameSelectTextView).setOnClickListener {
            clickNameSurname()
        }
        findViewById<View>(R.id.procedureSelectTextView).setOnClickListener {
            clickProcedure()
        }
        findViewById<View>(R.id.pigmentsSelectTextView1).setOnClickListener {
            clickPigments1()
        }
        findViewById<View>(R.id.pigmentsSelectTextView2).setOnClickListener {
            clickPigments2()
        }
        findViewById<View>(R.id.pigmentsSelectTextView3).setOnClickListener {
            clickPigments3()
        }
        findViewById<Button>(R.id.buttonPickImage).setOnClickListener { selectImage() }

        ic_menu_done_record.setOnClickListener {

            if (dateSelectTextView.text.toString().trim().isBlank()) {
                Toast.makeText(baseContext, "Введите дату...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nameSurnameSelectTextView.text.toString().trim().isBlank()) {
                Toast.makeText(baseContext, "Введите имя клиента...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (doneSwitch.isChecked && priceEditText.text.toString().trim().isBlank()) {
                Toast.makeText(baseContext, "Введите стоимость процедуры...", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            loaderAddRecordActivity.startAnimation()
            loaderAddRecordActivity.setIsVisible(true)
            loaderAddRecordActivity.isClickable = true
            loaderAddRecordActivity.translationZ = 1f

            CoroutineScope(Dispatchers.IO).launch {
                coroutine()
            }
        }

        ic_menu_close_record.setOnClickListener {
            finish()
        }


        val pigmentsCount1 = (0..15).map { it.toString() }
        val spinner1 = findViewById<Spinner>(R.id.spinner1)
        if (spinner1 != null) {
            val arrayAdapter = ArrayAdapter(this, R.layout.spinner, pigmentsCount1)
            spinner1.adapter = arrayAdapter

            spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

        val pigmentsCount2 = (0..15).map { it.toString() }
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        if (spinner2 != null) {
            val arrayAdapter = ArrayAdapter(this, R.layout.spinner, pigmentsCount2)
            spinner2.adapter = arrayAdapter

            spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

        val pigmentsCount3 = (0..15).map { it.toString() }
        val spinner3 = findViewById<Spinner>(R.id.spinner3)
        if (spinner3 != null) {
            val arrayAdapter = ArrayAdapter(this, R.layout.spinner, pigmentsCount3)
            spinner3.adapter = arrayAdapter

            spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }


        findViewById<EditText>(R.id.priceEditText).addTextChangedListener(object : TextWatcher {
            var currency = findViewById<TextView>(R.id.KZTTextView)
            var priceEditText = findViewById<EditText>(R.id.priceEditText)
//            private val space = ' '

            override fun afterTextChanged(s: Editable) {

//                // Remove spacing char
//                if (s.length > 0 && s.length % 4 == 0) {
//                    val c = s[s.length - 1]
//                    if (space == c) {
//                        s.delete(s.length - 1, s.length)
//                    }
//                }
//                // Insert char where needed.
//                if (s.length == 4) {
//                    val c = s[s.length - 1]
//                    // Only if its a digit where there should be a space we insert a space
//                    if (Character.isDigit(c) && TextUtils.split(s.toString(), space.toString()).size <= 1) {
//                        s.insert(s.length - 3, space.toString())
//                    }
//                }
//
//
//                if (s.length == 6) {
//                    val c = s[s.length -1]
//                    // Only if its a digit where there should be a space we insert a space
////                    if (Character.isDigit(c) && TextUtils.split(s.toString(), space.toString()).size <= 1) {
//                        s.delete(s.length -5, 2)
//                        //s.insert(s.length - 3, space.toString())
//                        Toast.makeText(this@AddRecordActivity, "erterter", Toast.LENGTH_SHORT).show()
////                    }
//                }
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (priceEditText.text.isBlank()) {
                    currency.visibility = View.INVISIBLE
                } else {
                    currency.visibility = View.VISIBLE
                }
            }

        })

        doneSwitch.setOnCheckedChangeListener { doneSwitch, isChecked ->
            if (isChecked) {
                doneTextView.text = "Процедура выполнена"
                doneTextView.setTextColor(resources.getColor(R.color.green2))
                priceEditText.isEnabled = true
                addRecordFrame.visibility = View.VISIBLE

            } else {
                doneTextView.text = "Процедура не выполнена"
                doneTextView.setTextColor(resources.getColor(R.color.red0))
                priceEditText.setText("")
                priceEditText.isEnabled = false
                addRecordFrame.visibility = View.INVISIBLE
            }
        }

        pickImageButton?.setOnClickListener { selectImage() }
        adapter = AdapterForAddRecordsActivity(this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imagesRecyclerView.layoutManager = layoutManager
        imagesRecyclerView.adapter = adapter


        if (intent.hasExtra(EXTRA_ID)) {
            showAdditionalPhoneNumber()
            toolbarLabel.text = "Редактировать запись"
            dateSelectTextView.text = (intent.getStringExtra(EXTRA_DATE))
            timeSelectTextView.text = (intent.getStringExtra(EXTRA_TIME))
            nameSurnameSelectTextView.text = intent.getStringExtra(EXTRA_FIRSTANDLASTNAME)
            phoneNumberSelectTextView1.text = (intent.getStringExtra(EXTRA_PHONENUMBER1))
            phoneNumberSelectTextView2.text = (intent.getStringExtra(EXTRA_PHONENUMBER2))
            procedureSelectTextView.text = (intent.getStringExtra(EXTRA_PROCEDURES))
            pigmentsSelectTextView1.text = (intent.getStringExtra(EXTRA_PIGMENT1))
            pigmentsSelectTextView2.text = (intent.getStringExtra(EXTRA_PIGMENT2))
            pigmentsSelectTextView3.text = (intent.getStringExtra(EXTRA_PIGMENT3))
            spinner1.setSelection(intent.getIntExtra(EXTRA_PIGMENTQUANTITY1, -1))
            spinner2.setSelection(intent.getIntExtra(EXTRA_PIGMENTQUANTITY2, -1))
            spinner3.setSelection(intent.getIntExtra(EXTRA_PIGMENTQUANTITY3, -1))
            priceEditText.setText(intent.getStringExtra(EXTRA_PRICE))
            commentsEditText.setText(intent.getStringExtra(EXTRA_COMMENTS))
            doneSwitch.isChecked = intent.getBooleanExtra(EXTRA_STATUS, false)
            adapter?.setData(Helper().getImagesFromPath(intent.getStringExtra(EXTRA_PHOTOS_PATH)))
            images.addAll(adapter!!.images)

        } else {
            toolbarLabel.text = "Новая запись"
        }


        adapter?.setOnItemClickListener(object : AdapterForAddRecordsActivity.OnItemClickListener {
            override fun onItemClick(images: ArrayList<Image>, viewHolder: RecyclerView.ViewHolder) {
                val imagePosition = adapter!!.getImagePosition(viewHolder.adapterPosition)
                val listOfImagesAttr = ArrayList<String>()

                for (i in images) {
                    val id = i.id.toString()
                    val name = i.name
                    val path = i.path

                    val subList = mutableListOf<String>()
                    subList.add(id)
                    subList.add(name)
                    subList.add(path)

                    listOfImagesAttr.add(subList.toString())
                }

                val bundle = Bundle()
                bundle.putStringArrayList(EXTRA_IMAGE_ATTR, listOfImagesAttr)
                bundle.putInt(EXTRA_IMAGE_POSITION, imagePosition)
                val fragment = BottomDialogFragment()
                fragment.arguments = bundle

                supportFragmentManager
                    .beginTransaction().add(fragment, "").addToBackStack("").commit()
            }
        })


        adapter?.setOnItemLongClickListener(object : AdapterForAddRecordsActivity.OnItemLongClickListener {

            override fun onItemLongClick(viewHolder: RecyclerView.ViewHolder) {
                vibrate()
                val imageToDelete = adapter!!.getImageAt(viewHolder.adapterPosition)

                val alertDialog = AlertDialog.Builder(this@AddRecordActivity)
                alertDialog.setMessage("Убрать данное фото?")

                alertDialog.setPositiveButton("Да") { dialog, which ->
                    adapter!!.removeImage(imageToDelete)
                    images.remove(imageToDelete)
                    adapter!!.notifyDataSetChanged()
                }

                alertDialog.setNegativeButton("Нет") { _, _ ->
                    adapter!!.notifyItemChanged(
                        viewHolder.adapterPosition
                    )
                }

                alertDialog.setOnCancelListener {
                    onDismissAlertDialog(viewHolder)
                }
                alertDialog.show()

            }

                fun onDismissAlertDialog (viewHolder: RecyclerView.ViewHolder) {
                    adapter!!.notifyItemChanged(
                        viewHolder.adapterPosition
                    )
                }
        })
    }

    suspend fun backToMainThread() {
        withContext (Main) {
            loaderAddRecordActivity.stopAnimation()
        }
    }

    suspend fun coroutine() {
        saveRecord()
        backToMainThread()
    }

    fun saveRecord() {

            val dateString = dateSelectTextView.text.toString()

            //val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)

            fun replaceMonth(): String {

                when {
                    dateSelectTextView.text.contains("января") -> return dateSelectTextView.text.toString()
                        .replace("января", "january")
                    dateSelectTextView.text.contains("февраля") -> return dateSelectTextView.text.toString()
                        .replace("февраля", "february")
                    dateSelectTextView.text.contains("марта") -> return dateSelectTextView.text.toString()
                        .replace("марта", "march")
                    dateSelectTextView.text.contains("апреля") -> return dateSelectTextView.text.toString()
                        .replace("апреля", "april")
                    dateSelectTextView.text.contains("мая") -> return dateSelectTextView.text.toString()
                        .replace("мая", "may")
                    dateSelectTextView.text.contains("июня") -> return dateSelectTextView.text.toString()
                        .replace("июня", "june")
                    dateSelectTextView.text.contains("июля") -> return dateSelectTextView.text.toString()
                        .replace("июля", "july")
                    dateSelectTextView.text.contains("августа") -> return dateSelectTextView.text.toString()
                        .replace("августа", "august")
                    dateSelectTextView.text.contains("сентября") -> return dateSelectTextView.text.toString()
                        .replace("сентября", "september")
                    dateSelectTextView.text.contains("октября") -> return dateSelectTextView.text.toString()
                        .replace("октября", "october")
                    dateSelectTextView.text.contains("ноября") -> return dateSelectTextView.text.toString()
                        .replace("ноября", "november")
                    dateSelectTextView.text.contains("декабря") -> return dateSelectTextView.text.toString()
                        .replace("декабря", "december")
                }
                return dateString
            }

            val parsedDate = Date.parse(replaceMonth())


            val data = Intent().apply {
                putExtra(EXTRA_DATE, parsedDate)
                putExtra(EXTRA_TIME, timeSelectTextView.text.toString())
                putExtra(EXTRA_FIRSTANDLASTNAME, nameSurnameSelectTextView.text.toString())
                putExtra(EXTRA_PHONENUMBER1, phoneNumberSelectTextView1.text.toString())
                putExtra(EXTRA_PHONENUMBER2, phoneNumberSelectTextView2.text.toString())
                putExtra(EXTRA_PROCEDURES, procedureSelectTextView.text.toString())
                putExtra(EXTRA_PIGMENT1, pigmentsSelectTextView1.text.toString())
                putExtra(EXTRA_PIGMENT2, pigmentsSelectTextView2.text.toString())
                putExtra(EXTRA_PIGMENT3, pigmentsSelectTextView3.text.toString())
                putExtra(EXTRA_PIGMENTQUANTITY1, spinner1.selectedItemPosition)
                putExtra(EXTRA_PIGMENTQUANTITY2, spinner2.selectedItemPosition)
                putExtra(EXTRA_PIGMENTQUANTITY3, spinner3.selectedItemPosition)
                putExtra(EXTRA_PRICE, priceEditText.text.toString())
                putExtra(EXTRA_COMMENTS, commentsEditText.text.toString())
                putExtra(EXTRA_STATUS, doneSwitch.isChecked)
                putExtra(EXTRA_PHOTOS_PATH, Helper().getImagesPaths(images))

                if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
                    putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, -1))
                }
            }

            setResult(Activity.RESULT_OK, data)
            finish()
    }


    fun clickDatePicker() {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)
        val dateTextView = findViewById<TextView>(R.id.dateSelectTextView)

        val sdf = SimpleDateFormat("d MMMM YYYY", Locale.getDefault())

        val dateCallback = DatePickerDialog.OnDateSetListener { _, y, m, d ->
            dateTextView.text = sdf.format(Date(y - 1900, m, d))
        }
        val dpd = DatePickerDialog(this, dateCallback, year, month, day)

        dpd.show()
    }

    fun clickTimePicker() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val timeTextView = findViewById<TextView>(R.id.timeSelectTextView)


        val timeCallBack = TimePickerDialog.OnTimeSetListener { _, h, m ->
            timeTextView.setText(String.format("%02d:%02d", h, m))
        }

        val tpd = TimePickerDialog(this, timeCallBack, hour, minute, true)

        tpd.show()
    }

    fun clickNameSurname() {
        val fragment = ClientsSelectFragment()
        supportFragmentManager
            .beginTransaction().add(fragment, "").addToBackStack(null).commit()
    }

    fun clickProcedure() {
        val fragment = ProceduresSelectFragment()
        supportFragmentManager
            .beginTransaction().add(fragment, "").addToBackStack(null).commit()
    }

    fun clickPigments1() {
        val fragment = PigmentsSelectFragment1()
        supportFragmentManager
            .beginTransaction().add(fragment, "").addToBackStack(null).commit()
    }

    fun clickPigments2() {
        val fragment = PigmentsSelectFragment2()
        supportFragmentManager
            .beginTransaction().add(fragment, "").addToBackStack(null).commit()
    }

    fun clickPigments3() {
        val fragment = PigmentsSelectFragment3()
        supportFragmentManager
            .beginTransaction().add(fragment, "").addToBackStack(null).commit()
    }

    fun sendClientsData(intent: Intent) {
        nameSurnameSelectTextView.text = intent.getStringExtra(EXTRA_FIRSTANDLASTNAME)
        phoneNumberSelectTextView1.text = intent.getStringExtra(EXTRA_PHONENUMBER1)
        phoneNumberSelectTextView2.text = intent.getStringExtra(EXTRA_PHONENUMBER2)
        if (intent.hasExtra(EXTRA_PHONENUMBER2)) {
            val params =
                phoneNumberSelectSelectDivider2.layoutParams as ConstraintLayout.LayoutParams
            additionalPhoneNumber.visibility = View.VISIBLE
            params.topToBottom = R.id.additionalPhoneNumber
            mainDivider2.visibility = View.VISIBLE
            ic_telephone2.visibility = View.VISIBLE
            phoneNumberSelectTextView2.visibility = View.VISIBLE
            phoneNumberSelectSelectDivider1.visibility = View.VISIBLE
        } else {
            val params =
                phoneNumberSelectSelectDivider2.layoutParams as ConstraintLayout.LayoutParams
            additionalPhoneNumber.visibility = View.INVISIBLE
            params.topToBottom = R.id.phoneNumberSelectSelectDivider1
            mainDivider2.visibility = View.INVISIBLE
            ic_telephone2.visibility = View.INVISIBLE
            phoneNumberSelectTextView2.visibility = View.INVISIBLE
            phoneNumberSelectSelectDivider1.visibility = View.INVISIBLE
        }
    }

    fun sendProceduresData(intent: Intent) {
        procedureSelectTextView.text = intent.getStringExtra(EXTRA_PROCEDURES)
    }

    fun sendPigmentsData1(intent: Intent) {
        pigmentsSelectTextView1.text = intent.getStringExtra(EXTRA_PIGMENT1)
    }

    fun sendPigmentsData2(intent: Intent) {
        pigmentsSelectTextView2.text = intent.getStringExtra(EXTRA_PIGMENT2)
    }

    fun sendPigmentsData3(intent: Intent) {
        pigmentsSelectTextView3.text = intent.getStringExtra(EXTRA_PIGMENT3)
    }

    fun phoneMask(s: String): String {
        val n = "+7 ${s[0]}${s[1]}${s[2]} ${s[3]}${s[4]}${s[5]} ${s[6]}${s[7]} ${s[8]}${s[9]}"
        return n
    }

    fun showAdditionalPhoneNumber() {
        if (intent.getStringExtra(EXTRA_PHONENUMBER2).length > 1) {
            val params =
                phoneNumberSelectSelectDivider2.layoutParams as ConstraintLayout.LayoutParams
            additionalPhoneNumber.visibility = View.VISIBLE
            params.topToBottom = R.id.additionalPhoneNumber
            mainDivider2.visibility = View.VISIBLE
            ic_telephone2.visibility = View.VISIBLE
            phoneNumberSelectTextView2.visibility = View.VISIBLE
            phoneNumberSelectSelectDivider1.visibility = View.VISIBLE

        }
    }

    fun selectImage() {
        ImagePicker.with(this)
            .setFolderMode(false)
            .setShowCamera(true)
            .setSavePath("//PMU camera")
            .setStatusBarColor("#000000")
            .setToolbarColor("#000000")
            .setToolbarIconColor("#97bf0d")
            .setToolbarTextColor("#97bf0d")
            .setProgressBarColor("#000000")
            .setDoneTitle("OK")
            .setImageTitle("Галерея")
            .setLimitMessage("Максимум 6 фото")
            .setMultipleMode(true)
            .setSelectedImages(images)
            .setMaxSize(6)
            .setBackgroundColor("#212121")
            .setAlwaysShowDoneButton(true)
            .setRequestCode(100)
            .setKeepScreenOn(true)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES)
            adapter!!.setData(images)

            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE))
    }

//    override fun onStop() {
//        super.onStop()
//
//        val tempImageFolder = File(
//            Environment.getExternalStorageDirectory().toString() + "/tempImages"
//        )
//        if (tempImageFolder.isDirectory) {
//            val children = tempImageFolder.list()
//            for (i in children.indices) {
//                File(tempImageFolder, children[i]).delete()
//            }
//            tempImageFolder.delete()
//        }
//    }
}
