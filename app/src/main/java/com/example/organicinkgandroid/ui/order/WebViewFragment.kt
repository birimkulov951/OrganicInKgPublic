package com.example.organicinkgandroid.ui.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.organicinkgandroid.R
import com.example.organicinkgandroid.retrofit.SessionManager
import kotlinx.android.synthetic.main.fragment_web_view.*
import kotlinx.android.synthetic.main.fragment_web_view.view.*

class WebViewFragment : Fragment() {

    lateinit var navController : NavController
    private lateinit var sessionManager: SessionManager


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_web_view, container, false)
        val webView = rootView.web_view

        val url = arguments?.getString("ELSOM_URL","null")

        webView.loadUrl(url!!)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.canGoBack()

        webView.setOnKeyListener(View.OnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == MotionEvent.ACTION_UP && webView.canGoBack()){
                webView.goBack()
                return@OnKeyListener true

            }
            false
        })


        rootView.web_view_payment_done.setOnClickListener{
            navController.navigate(R.id.action_webViewFragment_to_orderConfirmationFragment)
        }

        rootView.web_view_fragment_back_btn.setOnClickListener{
            requireActivity().onBackPressed()
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}

