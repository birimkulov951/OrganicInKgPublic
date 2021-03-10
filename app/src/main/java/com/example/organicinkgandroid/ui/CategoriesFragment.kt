
package com.example.organicinkgandroid.ui

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.adapters.CategoriesAdapter
import com.example.organicinkgandroid.model.CategoriesItem
import com.example.organicinkgandroid.model.network.category.CategoryResponse
import com.example.organicinkgandroid.retrofit.ApiClient
import com.example.organicinkgandroid.retrofit.SessionManager
import com.example.organicinkgandroid.utils.Constants.CATEGORY_DATA
import kotlinx.android.synthetic.main.fragment_categories.view.*
import retrofit2.Call
import retrofit2.Response

class CategoriesFragment : Fragment(), CategoriesAdapter.OnItemClickListener {

    private val TAG = "CategoriesFragment"
    private lateinit var sessionManager: SessionManager

    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var apiClient: ApiClient

    lateinit var navController: NavController

    // Vars
    private var categoriesMainList = ArrayList<CategoriesItem>()
    lateinit var mainSearch: EditText
    lateinit var productToSearch: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_categories, container, false)
        (activity as MainActivity).hideBasketToast()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())
        categoriesAdapter = CategoriesAdapter(requireContext(), this)

        rootView.categories_fragment_filter_button.setOnClickListener{
            navController.navigate(R.id.action_categoriesFragment_to_filterSettingsFragment)
        }

        mainSearch = rootView.categories_edit_text_search
        mainSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                productToSearch = mainSearch.text.toString()
                getProductsByName(productToSearch)
            };true
        }


        rootView.categories_fragment_progressbar.visibility = View.VISIBLE
        getParentCategoryList(rootView)

        return rootView
    }


    private fun getParentCategoryList(rootView: View) {

        val call = apiClient.getApiService().getCategoryList(100)

        call.enqueue(object : retrofit2.Callback<CategoryResponse> {

            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {

                categoriesMainList.clear()

                if (response.isSuccessful) {
                    rootView.categories_fragment_progressbar.visibility = View.GONE

                    if (response.body()!!.result.isEmpty()) {
                        rootView.categories_fragment_is_empty_text.visibility = View.VISIBLE

                    } else {
                        for (i in response.body()!!.result.indices) {
                            var vectorImagePath = ""
                            try {
                                vectorImagePath = response.body()!!.result[i].imagePath!!
                            } catch (e: KotlinNullPointerException) {
                                Log.e(TAG, "onResponse: imagePath == KotlinNullPointerException")
                            }

                            if (response.body()!!.result[i].parentCategory == null) {
                                categoriesMainList.add(
                                    CategoriesItem(
                                        response.body()!!.result[i].id!!,
                                        vectorImagePath,
                                        response.body()!!.result[i].name!!,
                                        response.body()!!.result[i].description!!,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        false
                                    )
                                )
                            } else {
                                addCategoryToParentCategory(
                                    response.body()!!.result[i].parentCategory!!.id,
                                    response.body()!!.result[i].name!!
                                )
                            }

                        }

                        rootView.categories_recyclerview.adapter = categoriesAdapter
                        categoriesAdapter.setCategoriesItems(categoriesMainList)
                        //val lac: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_slide_right)
                        //rootView.categories_recyclerview.layoutAnimation = lac
                        rootView.categories_recyclerview.startLayoutAnimation()
                    }
                }
                Log.e(TAG, "onResponse: $categoriesMainList")

            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                if (!isNetworkAvailable(context)) {
                    /*rootView.categories_fragment_progressbar.visibility = View.GONE
                    rootView.categories_fragment_is_empty_text.visibility = View.VISIBLE
                    rootView.categories_fragment_is_empty_text.text = getString(R.string.no_internet)*/
                    (activity as MainActivity).customToast(getString(R.string.no_internet),true)
                    Log.e("HomeFragment", "Failed to retrieve ${t.message}")

                } else {
                    (activity as MainActivity).customToast(getString(R.string.unknown_error),true)
                }

                Log.e(TAG, t.message.toString())
            }

        })
    }

    private fun addCategoryToParentCategory(id: Int, name: String) {
        for (item in 0 until categoriesMainList.size) {
            if (categoriesMainList[item].itemId == id) {
                Log.e(TAG, "addCategoryToParentCategory: ${categoriesMainList[item].itemId == id}")
                when {
                    categoriesMainList[item].item1 == null -> categoriesMainList[item].item1 = name
                    categoriesMainList[item].item2 == null -> categoriesMainList[item].item2 = name
                    categoriesMainList[item].item3 == null -> categoriesMainList[item].item3 = name
                    categoriesMainList[item].item4 == null -> categoriesMainList[item].item4 = name
                    categoriesMainList[item].item5 == null -> categoriesMainList[item].item5 = name
                    categoriesMainList[item].item6 == null -> categoriesMainList[item].item6 = name
                    categoriesMainList[item].item7 == null -> categoriesMainList[item].item7 = name
                    categoriesMainList[item].item8 == null -> categoriesMainList[item].item8 = name
                    categoriesMainList[item].item9 == null -> categoriesMainList[item].item9 = name
                }
            }
        }
    }

    private fun getProductsByName(productName : String) {
        val bundle = Bundle()
        bundle.putString("productName", productName)
        hideKeyBoard()
        if(productName.isEmpty()){
        }
        else {
            Log.e("productName", productName)
            navController.navigate(R.id.action_categoriesFragment_to_searchResultFragment2, bundle)
        }
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    override fun onItemClick(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]

        val cardViewInvisible = v?.findViewById<CardView>(R.id.categories_card_view_invisible)

        val arrowIcon = v?.findViewById<ImageView>(R.id.categories_icon_arrow)
        val textTitle = v?.findViewById<TextView>(R.id.categories_text_title)
        val textDescription = v?.findViewById<TextView>(R.id.categories_description)
        val textOne = v?.findViewById<TextView>(R.id.categories_text_one)
        val textTwo = v?.findViewById<TextView>(R.id.categories_text_two)
        val textThree = v?.findViewById<TextView>(R.id.categories_text_three)
        val textFour = v?.findViewById<TextView>(R.id.categories_text_four)
        val textFive = v?.findViewById<TextView>(R.id.categories_text_five)
        val textSix = v?.findViewById<TextView>(R.id.categories_text_six)
        val textSeven = v?.findViewById<TextView>(R.id.categories_text_seven)
        val textEight = v?.findViewById<TextView>(R.id.categories_text_eight)
        val textNine = v?.findViewById<TextView>(R.id.categories_text_nine)
        val textTen = v?.findViewById<TextView>(R.id.categories_text_ten)
        val textEleven = v?.findViewById<TextView>(R.id.categories_text_eleven)
        val textTwelve = v?.findViewById<TextView>(R.id.categories_text_twelve)
        val textThirteen = v?.findViewById<TextView>(R.id.categories_text_thirteen)
        val textFourteen = v?.findViewById<TextView>(R.id.categories_text_fourteen)
        val textFifteen = v?.findViewById<TextView>(R.id.categories_text_fifteen)
        val textSixteen = v?.findViewById<TextView>(R.id.categories_text_sixteen)
        val textSeventeen = v?.findViewById<TextView>(R.id.categories_text_seventeen)
        val textEighteen = v?.findViewById<TextView>(R.id.categories_text_eighteen)
        val textNineteen = v?.findViewById<TextView>(R.id.categories_text_nineteen)
        val textTwenty = v?.findViewById<TextView>(R.id.categories_text_twenty)

        val rotateRight = AnimationUtils.loadAnimation(activity, R.anim.rotate_right)
        val rotateLeft = AnimationUtils.loadAnimation(activity, R.anim.rotate_left)

        if (!clickedItem.isExpanded) {
            // Animates when item is clicked
            TransitionManager.beginDelayedTransition(cardViewInvisible, AutoTransition())
            textDescription?.visibility = View.VISIBLE
            arrowIcon?.startAnimation(rotateRight)
            arrowIcon?.setColorFilter(Color.parseColor("#000000"))
            textTitle?.setTextColor(Color.parseColor("#000000"))

            clickedItem.isExpanded = true

            if (clickedItem.item1 != null) {
                textOne?.visibility = View.VISIBLE
            }
            if (clickedItem.item2 != null) {
                textTwo?.visibility = View.VISIBLE
            }
            if (clickedItem.item3 != null) {
                textThree?.visibility = View.VISIBLE
            }
            if (clickedItem.item4 != null) {
                textFour?.visibility = View.VISIBLE
            }
            if (clickedItem.item5 != null) {
                textFive?.visibility = View.VISIBLE
            }
            if (clickedItem.item6 != null) {
                textSix?.visibility = View.VISIBLE
            }
            if (clickedItem.item7 != null) {
                textSeven?.visibility = View.VISIBLE
            }
            if (clickedItem.item8 != null) {
                textEight?.visibility = View.VISIBLE
            }
            if (clickedItem.item9 != null) {
                textNine?.visibility = View.VISIBLE
            }
            if (clickedItem.item10 != null) {
                textTen?.visibility = View.VISIBLE
            }
            if (clickedItem.item11 != null) {
                textEleven?.visibility = View.VISIBLE
            }
            if (clickedItem.item12 != null) {
                textTwelve?.visibility = View.VISIBLE
            }
            if (clickedItem.item13 != null) {
                textThirteen?.visibility = View.VISIBLE
            }
            if (clickedItem.item14 != null) {
                textFourteen?.visibility = View.VISIBLE
            }
            if (clickedItem.item15 != null) {
                textFifteen?.visibility = View.VISIBLE
            }
            if (clickedItem.item16 != null) {
                textSixteen?.visibility = View.VISIBLE
            }
            if (clickedItem.item17 != null) {
                textSeventeen?.visibility = View.VISIBLE
            }
            if (clickedItem.item18 != null) {
                textEighteen?.visibility = View.VISIBLE
            }
            if (clickedItem.item19 != null) {
                textNineteen?.visibility = View.VISIBLE
            }
            if (clickedItem.item20 != null) {
                textTwenty?.visibility = View.VISIBLE
            }

        } else {
            // if RecyclerView item is not expanded
            //TransitionManager.beginDelayedTransition(cardViewInvisible, AutoTransition())
            arrowIcon?.setColorFilter(Color.parseColor("#8E8E8E"))
            textTitle?.setTextColor(Color.parseColor("#8E8E8E"))
            arrowIcon?.startAnimation(rotateLeft)

            textDescription?.visibility = View.GONE
            textOne?.visibility = View.GONE
            textTwo?.visibility = View.GONE
            textThree?.visibility = View.GONE
            textFour?.visibility = View.GONE
            textFive?.visibility = View.GONE
            textSix?.visibility = View.GONE
            textSeven?.visibility = View.GONE
            textEight?.visibility = View.GONE
            textNine?.visibility = View.GONE
            textTen?.visibility = View.GONE
            textEleven?.visibility = View.GONE
            textTwelve?.visibility = View.GONE
            textThirteen?.visibility = View.GONE
            textFourteen?.visibility = View.GONE
            textFifteen?.visibility = View.GONE
            textSixteen?.visibility = View.GONE
            textSeventeen?.visibility = View.GONE
            textEighteen?.visibility = View.GONE
            textNineteen?.visibility = View.GONE
            textTwenty?.visibility = View.GONE
            clickedItem.isExpanded = false
        }
        categoriesAdapter.notifyItemRangeChanged(categoriesAdapter.itemCount,position+1)
    }

    override fun onItemOne(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item1)
    }

    override fun onItemTwo(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item2)
    }

    override fun onItemThree(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item3)
    }

    override fun onItemFour(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item4)
    }

    override fun onItemFive(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item5)
    }

    override fun onItemSix(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item6)
    }

    override fun onItemSeven(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item7)
    }

    override fun onItemEight(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item8)
    }

    override fun onItemNine(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item9)
    }

    override fun onItemTen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item10)
    }

    override fun onItemEleven(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item11)
    }

    override fun onItemTwelve(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item12)
    }

    override fun onItemThirteen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item13)
    }

    override fun onItemFourteen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item14)
    }

    override fun onItemFifteen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item15)
    }

    override fun onItemSixteen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item16)
    }

    override fun onItemSeventeen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item17)
    }

    override fun onItemEighteen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item18)
    }

    override fun onItemNineteen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item19)
    }

    override fun onItemTwenty(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        openSubCategoryFragment(clickedItem.item20)
    }

    private fun openSubCategoryFragment(itemName: String?) {
        val categoryToSend = Bundle()
        categoryToSend.putString(CATEGORY_DATA,itemName)
        navController.navigate(R.id.action_categoriesFragment_to_categoriesSubFragment, categoryToSend)
    }

    private fun hideKeyBoard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}