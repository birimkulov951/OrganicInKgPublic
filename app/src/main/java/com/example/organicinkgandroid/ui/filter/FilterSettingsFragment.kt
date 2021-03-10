package com.example.organicinkgandroid.ui.filter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.retrofit.SessionManager
import com.example.organicinkgandroid.utils.Constants.ASC
import com.example.organicinkgandroid.utils.Constants.DESC
import com.example.organicinkgandroid.utils.Constants.NEW
import com.example.organicinkgandroid.utils.Constants.PRICE
import kotlinx.android.synthetic.main.fragment_filter_settings.*
import kotlinx.android.synthetic.main.fragment_filter_settings.view.*

class FilterSettingsFragment : Fragment() {

    private val TAG: String = "FilterFragment"

    private lateinit var sessionManager: SessionManager

    lateinit var navController: NavController

    // Helpers
    private var selectedCategory: String = "null"
    private var selectedLocation: String = "null"
    private var priceFrom: Int = -1
    private var priceUntil: Int = -1
    private var filtrateBy: String = "NEW"
    private var sortDirection : String = "DESC"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_filter_settings, container, false)

        sessionManager = SessionManager(requireContext())

        // Fetching filter settings
        selectedCategory = sessionManager.fetchSelectedCategory()!!
        selectedLocation = sessionManager.fetchSelectedLocation()!!
        priceFrom = sessionManager.fetchPriceStart()!!
        priceUntil = sessionManager.fetchPriceEnd()!!
        filtrateBy =  sessionManager.fetchFiltrateBy()!!
        sortDirection = sessionManager.fetchSortDirection()!!

        observeHelper(rootView)

        rootView.search_filter_fragment_back_button.setOnClickListener{

            hideKeyboard(rootView)

            (activity as MainActivity).onBackPressed()

        }

        rootView.search_filter_fragment_clean_settings.setOnClickListener{

            hideKeyboard(rootView)
            // Delete all filter settings here
            selectedCategory = "null"
            selectedLocation = "null"
            priceFrom = -1
            priceUntil = -1
            filtrateBy = "NEW"
            sortDirection = "null"

            sessionManager.saveSelectedCategory(selectedCategory)
            sessionManager.saveSelectedLocation(selectedLocation)
            sessionManager.savePriceStart(priceFrom)
            sessionManager.savePriceEnd(priceUntil)
            sessionManager.saveFiltrateBy(filtrateBy)
            sessionManager.saveSortDirection(sortDirection)

            observeHelper(rootView)
        }

        // Other filterSettings fragments
        rootView.search_filter_select_category_card_view_1.setOnClickListener{
            hideKeyboard(rootView)
            navController.navigate(R.id.action_filterSettingsFragment_to_categoryFilterFragment)
        }
        rootView.search_filter_select_location_card_view_2.setOnClickListener{
            hideKeyboard(rootView)
            navController.navigate(R.id.action_filterSettingsFragment_to_locationFiltrationFragment)
        }
        rootView.filter_settings_done_button.setOnClickListener {
            getProductsByFilter()
        }

        // EditText listener
        rootView.search_filter_fragment_edit_text_from.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    priceFrom = s.toString().toInt()

                    if (s.toString().toInt() >= sessionManager.fetchPriceEnd()!!.toInt() && sessionManager.fetchPriceEnd()!!.toInt() !=-1) {
                        sessionManager.savePriceStart(priceUntil)
                        sessionManager.savePriceEnd(s.toString().toInt())
                    } else {
                        sessionManager.savePriceStart(s.toString().toInt())
                    }

                    if (s.toString().contains("-")) {
                        (activity as MainActivity).customToastTwo(getString(R.string.input_only_positive_numbers),false)
                    }

                    rootView.search_filter_fragment_edit_text_from.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                } else {
                    sessionManager.savePriceStart(-1)
                    sessionManager.savePriceEnd(priceUntil)
                    rootView.search_filter_fragment_edit_text_from.setBackgroundResource(R.drawable.edit_text_frame_background)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // EditText listener
        rootView.search_filter_fragment_edit_text_until.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    priceUntil = s.toString().toInt()

                    if (s.toString().toInt() <= sessionManager.fetchPriceStart()!!.toInt() && sessionManager.fetchPriceStart()!!.toInt() != -1) {
                        sessionManager.savePriceStart(s.toString().toInt())
                        sessionManager.savePriceEnd(priceFrom)
                    } else {
                        sessionManager.savePriceStart(priceFrom)
                        sessionManager.savePriceEnd(s.toString().toInt())
                    }

                    if (s.toString().contains("-")) {
                        (activity as MainActivity).customToastTwo(getString(R.string.input_only_positive_numbers),false)
                    }

                    rootView.search_filter_fragment_edit_text_until.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
                } else {
                    sessionManager.savePriceStart(priceFrom)
                    sessionManager.savePriceEnd(-1)
                    rootView.search_filter_fragment_edit_text_until.setBackgroundResource(R.drawable.edit_text_frame_background)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // RadioButton listener
        rootView.search_filter_fragment_radio_group.setOnCheckedChangeListener { _, checkedId ->

            hideKeyboard(rootView)
        }

        return rootView
    }

    private fun observeHelper(view: View) {

        if (selectedCategory != "null") {
            view.search_filter_select_category_card_view_1_text.text = selectedCategory
            view.search_filter_select_category_card_view_1_text.setTextColor(
                Color.parseColor(
                    "#009B00"
                )
            )
            view.search_filter_select_category_card_view_1_image.setImageResource(R.drawable.ic_baseline_add_32_green)
        } else {
            view.search_filter_select_category_card_view_1_text.text =
                getString(R.string.select_category)
            view.search_filter_select_category_card_view_1_text.setTextColor(
                Color.parseColor(
                    "#8E8E8E"
                )
            )
            view.search_filter_select_category_card_view_1_image.setImageResource(R.drawable.ic_baseline_add_32)
        }

        if (selectedLocation != "null") {
            view.search_filter_select_location_card_view_2_text.text = selectedLocation
            view.search_filter_select_location_card_view_2_text.setTextColor(
                Color.parseColor(
                    "#009B00"
                )
            )
            view.search_filter_select_location_card_view_2_image.setImageResource(R.drawable.ic_baseline_add_32_green)
        } else {
            view.search_filter_select_location_card_view_2_text.text =
                getString(R.string.select_location)
            view.search_filter_select_location_card_view_2_text.setTextColor(
                Color.parseColor(
                    "#8E8E8E"
                )
            )
            view.search_filter_select_location_card_view_2_image.setImageResource(R.drawable.ic_baseline_add_32)
        }

        if (priceFrom != -1) {
            view.search_filter_fragment_edit_text_from.setText(priceFrom.toString(), TextView.BufferType.EDITABLE)
            view.search_filter_fragment_edit_text_from.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
        } else {
            view.search_filter_fragment_edit_text_from.setText("", TextView.BufferType.EDITABLE)
        }

        if (priceUntil != -1) {
            view.search_filter_fragment_edit_text_until.setText(priceUntil.toString(), TextView.BufferType.EDITABLE)
            view.search_filter_fragment_edit_text_until.setBackgroundResource(R.drawable.edit_text_frame_background_edited)
        } else {
            view.search_filter_fragment_edit_text_until.setText("", TextView.BufferType.EDITABLE)
        }

        when (filtrateBy) {
            "NEW" -> {
                view.search_filter_fragment_radiobutton_by_new.isChecked = true
            }
            "PRICE" -> {if (sortDirection == ASC) {
                view.search_filter_fragment_radiobutton_by_cheap.isChecked = true
            }
                else{
                view.search_filter_fragment_radiobutton_by_high_cost.isChecked = true
            }

            }
            else -> {
                // By default.
                view.search_filter_fragment_radiobutton_by_default.isChecked = true
            }

        }


    }

    private fun getProductsByFilter() {
        checkRadioButtons()
        sessionManager.saveSelectedCategory(selectedCategory)
        sessionManager.saveSelectedLocation(selectedLocation)
        sessionManager.savePriceStart(priceFrom)
        sessionManager.savePriceEnd(priceUntil)
        sessionManager.saveFiltrateBy(filtrateBy)
        sessionManager.saveSortDirection(sortDirection)
        (activity as MainActivity).onBackPressed()
        //navController.navigate(R.id.action_filterSettingsFragment_to_homeFragment)
    }

    private fun checkRadioButtons() {
        if (search_filter_fragment_radiobutton_by_new.isChecked || search_filter_fragment_radiobutton_by_default.isChecked){
            filtrateBy = NEW
            sortDirection = DESC
        }
        else if(search_filter_fragment_radiobutton_by_cheap.isChecked){
            filtrateBy = PRICE
            sortDirection = ASC
        }
        else if (search_filter_fragment_radiobutton_by_high_cost.isChecked){
            filtrateBy = PRICE
            sortDirection = DESC
        }

    }

    private fun hideKeyboard(rootView: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(rootView.windowToken, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}