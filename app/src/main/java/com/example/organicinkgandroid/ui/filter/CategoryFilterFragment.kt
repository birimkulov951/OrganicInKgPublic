package com.example.organicinkgandroid.ui.filter

import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgandroid.MainActivity
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.adapters.CategoriesAdapter
import com.example.organicinkgandroid.model.CategoriesItem
import com.example.organicinkgandroid.model.network.category.CategoryResponse
import com.example.organicinkgandroid.retrofit.ApiClient
import com.example.organicinkgandroid.retrofit.SessionManager
import kotlinx.android.synthetic.main.fragment_category_filter.view.*
import retrofit2.Call
import retrofit2.Response
class CategoryFilterFragment : Fragment(), CategoriesAdapter.OnItemClickListener {

    private val TAG = "CategoryFilterFragment"

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    private lateinit var categoriesAdapter: CategoriesAdapter

    lateinit var navController: NavController

    private var categoriesMainList = ArrayList<CategoriesItem>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_category_filter, container, false)

        sessionManager = SessionManager(requireContext())
        apiClient = ApiClient()
        categoriesAdapter = CategoriesAdapter( requireContext(), this)

        rootView.category_filter_fragment_toolbar_back_button.setOnClickListener{
            (activity as MainActivity).onBackPressed()
            //navController.navigate(R.id.action_categoryFilterFragment_to_filterSettingsFragment)
        }

        getParentCategoryList(rootView)


        return rootView
    }


    private fun getParentCategoryList(rootView: View) {
        rootView.categories_filter_fragment_progressbar.visibility = View.VISIBLE

        val call = apiClient.getApiService().getCategoryList(50)

        call.enqueue(object : retrofit2.Callback<CategoryResponse> {

            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {

                categoriesMainList.clear()

                if (response.isSuccessful) {
                    rootView.categories_filter_fragment_progressbar.visibility = View.GONE

                    if (response.body()!!.result.isEmpty()) {
                        rootView.categories_filter_fragment_is_empty_text.visibility = View.VISIBLE

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
                                        //R.drawable.ic_bahchevye,
                                        response.body()!!.result[i].name!!,
                                        response.body()!!.result[i].description!!,
                                        null, null, null, null, null, null, null,null, null, null, null, null, null, null,  null, null,
                                        null, null, null, null, false
                                    )
                                )
                            } else {
                                addCategoryToParentCategory(
                                    response.body()!!.result[i].parentCategory!!.id,
                                    response.body()!!.result[i].name!!
                                )
                            }

                        }

                        //val lac: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_slide_right)
                        //categories_recyclerview.layoutAnimation = lac
                        rootView.category_filter_fragment_recycler_view.adapter = categoriesAdapter
                        categoriesAdapter.setCategoriesItems(categoriesMainList)
                        //category_filter_fragment_recycler_view.startLayoutAnimation()
                    }
                }
                else {
                    (activity as MainActivity).customToastTwo(getString(R.string.unknown_error),true)
                }
                Log.e(TAG, "onResponse: $categoriesMainList")

            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                (activity as MainActivity).customToastTwo(getString(R.string.unknown_error),true)
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


    override fun onItemClick(position: Int, v: View?) {
        Log.d(TAG, "onItemClick: clicked")
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
            saveSelectedCategory(clickedItem.itemTitle)

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
        categoriesAdapter.notifyItemRangeChanged(categoriesAdapter.itemCount,position)
    }

    override fun onItemOne(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item1)
    }

    override fun onItemTwo(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item2)
    }

    override fun onItemThree(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item3)
    }

    override fun onItemFour(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item4)
    }

    override fun onItemFive(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item5)
    }

    override fun onItemSix(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item6)
    }

    override fun onItemSeven(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item7)
    }

    override fun onItemEight(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item8)
    }

    override fun onItemNine(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item9)
    }

    override fun onItemTen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item10)
    }

    override fun onItemEleven(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item11)
    }

    override fun onItemTwelve(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item12)
    }

    override fun onItemThirteen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item13)
    }

    override fun onItemFourteen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item14)
    }

    override fun onItemFifteen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item15)
    }

    override fun onItemSixteen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item16)
    }

    override fun onItemSeventeen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item17)
    }

    override fun onItemEighteen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item18)
    }

    override fun onItemNineteen(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item19)
    }

    override fun onItemTwenty(position: Int, v: View?) {
        val clickedItem: CategoriesItem = categoriesMainList[position]
        saveSelectedCategory(clickedItem.item20)
    }

    private fun saveSelectedCategory(selectedCategoryName: String?) {
        sessionManager.saveSelectedCategory(selectedCategoryName!!)
        (activity as MainActivity).onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}