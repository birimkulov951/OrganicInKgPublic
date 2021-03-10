package com.example.organicinkgpublic.ui.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.organicinkgpublic.MainActivity
import com.example.organicinkgpublic.R
import com.example.organicinkgpublic.adapters.RegionAdapter
import com.example.organicinkgpublic.model.Region
import com.example.organicinkgpublic.model.network.supplier.PlaceOfProductionResponse
import com.example.organicinkgpublic.retrofit.ApiClient
import com.example.organicinkgpublic.retrofit.SessionManager
import kotlinx.android.synthetic.main.fragment_location_filter.*
import kotlinx.android.synthetic.main.fragment_location_filter.view.*
import retrofit2.Call
import retrofit2.Response

class LocationFiltraterFragment: Fragment(), RegionAdapter.OnItemClickListener{

    private val TAG: String = "LocationFiltrationFragment"

    lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    lateinit var progressBar: ProgressBar
    private var adapter: RegionAdapter = RegionAdapter(this)


    private val listItems: ArrayList<Region> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_location_filter, container, false)
        progressBar = rootView.location_filter_progress_bar
        progressBar.visibility = View.VISIBLE
        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        getPlacesOfProduction()

//        listItems.add(Region(getString(R.string.bishkek)))
//        listItems.add(Region(getString(R.string.batken)))
//        listItems.add(Region(getString(R.string.jalal_abad)))
//        listItems.add(Region(getString(R.string.yssyk_kol)))
//        listItems.add(Region(getString(R.string.naryn)))
//        listItems.add(Region(getString(R.string.osh)))
//        listItems.add(Region(getString(R.string.talas)))
//        listItems.add(Region(getString(R.string.chuy)))




        rootView.location_filter_back_button.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return rootView

    }

    private fun getPlacesOfProduction() {
        var helperList: HashSet<Region> = HashSet()
        val call = apiClient.getApiService().getPlaceOfProductionList().enqueue(object:
            retrofit2.Callback<PlaceOfProductionResponse> {
            override fun onFailure(call: Call<PlaceOfProductionResponse>, t: Throwable) {
               // TODO("Not yet implemented")
            }

            override fun onResponse(
                call: Call<PlaceOfProductionResponse>,
                response: Response<PlaceOfProductionResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body()!!.result.size != 0) {
                    for (i in 0..response.body()!!.result.size - 1) {
                      //  listItems.add(Region(response.body()!!.result[i].region))
                        helperList.add(Region((response.body()!!.result[i].region)))

                    }
                    listItems.addAll(helperList)
                    Log.e("region", helperList.size.toString())
                    updateUI()
                }
            }
        })
    }

    private fun updateUI() {
        location_fragment_recycler_view.layoutManager = LinearLayoutManager(context)
        location_fragment_recycler_view.adapter = adapter
        adapter.setQuestionItems(listItems)
    }

    // Updating settings in DB
    override fun onItemClick(v: View?, position: Int) {
        val clickedItem = listItems[position]
        sessionManager.saveSelectedLocation(clickedItem.region)
        (activity as MainActivity).onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}