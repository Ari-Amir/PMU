package com.aco.pmu.records


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.aco.pmu.R
import com.aco.pmu.records.adapters.AdapterForBottomDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nguyenhoanglam.imagepicker.model.Image
import kotlinx.android.synthetic.main.activity_add_record.*
import kotlinx.android.synthetic.main.fragment_bottom_dialog.*


class BottomDialogFragment : BottomSheetDialogFragment() {


    private var adapter: AdapterForBottomDialogFragment? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val dialog = dialog as BottomSheetDialog?
                val bottomSheet = dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet as View)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                //behavior.peekHeight = 0 // Remove this line to hide a dark background if you manually hide the dialog.
            }
        })


        val list = arguments?.getStringArrayList(AddRecordActivity.EXTRA_IMAGE_ATTR)
        val imagePosition = arguments!!.getInt(AddRecordActivity.EXTRA_IMAGE_POSITION)

        val listOfImages = mutableListOf<Image>()

        for (i in list!!) {
            val splitted = i.split(',','[',']')
            val id = splitted[1].toLong()
            val name = splitted[2].substring(1)
            val path = splitted[3].substring(1)

            val image = Image(id, name, path)
            listOfImages.add(image)
        }

        adapter = AdapterForBottomDialogFragment(this.requireContext())
        adapter!!.setData(listOfImages)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        this.bottomDialogRecyclerView.layoutManager = layoutManager
        this.bottomDialogRecyclerView.adapter = adapter
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(bottomDialogRecyclerView)
        this.bottomDialogRecyclerView.scrollToPosition(imagePosition)
    }
}


