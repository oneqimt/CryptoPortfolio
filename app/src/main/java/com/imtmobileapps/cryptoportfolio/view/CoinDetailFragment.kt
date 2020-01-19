package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.databinding.FragmentCoinDetailBinding
import com.imtmobileapps.cryptoportfolio.model.Coin
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.model.TotalValues
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import com.imtmobileapps.cryptoportfolio.viewmodel.CoinDetailViewModel
import kotlinx.android.synthetic.main.fragment_coin_detail.*


class CoinDetailFragment : Fragment() {

    var selectedCoin: CryptoValue? = null

    private lateinit var viewModel: CoinDetailViewModel
    private lateinit var dataBinding: FragmentCoinDetailBinding
    var prefHelper = PreferencesHelper()
    var totalValues: TotalValues? = null

    var coin : Coin = Coin("0", "BITCOIN", "BTC", "", "")
    var cryptoValue : CryptoValue = CryptoValue("BITCOIN", coin, "111", "45%", "22", "increase", 1.2)
    var totalv = TotalValues(1, "2", "3", "1", "increase")
    var newsListAdapter = NewsListAdapter(cryptoValue, totalv, arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_coin_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        
        // if args are not null
        arguments?.let {
            selectedCoin = CoinDetailFragmentArgs.fromBundle(it).selectedCoin
        }

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(CoinDetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        viewModel.refresh(prefHelper.getCurrentPersonId()!!)
        viewModel.setCrypto(selectedCoin)


        recyclerDetails.apply {
            layoutManager = LinearLayoutManager(context)
           // getRecycledViewPool().clear();
            adapter = newsListAdapter
            newsListAdapter.setRecyclerView(this)
            // pass this to the adapter
            var errorStr = activity?.resources?.getString(R.string.news_error_text)
            newsListAdapter.setNewsErrorString(errorStr)
            
            val resId: Int = R.anim.layout_animation_fall_down
            val animation: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(activity, resId)
            recyclerDetails.animation = animation.animation
    
        }
        
        observeViewModel()

    }
    
    fun observeViewModel() {

        viewModel.cryptoLiveData.observe(this, Observer { crypto ->
            selectedCoin = crypto
            crypto?.let {
                dataBinding.crypto = crypto
                newsListAdapter.updateCryptoValue(crypto)
            }
        })

        viewModel.totals.observe(this, Observer { totals ->
            totalValues = totals
            totals?.let {
                dataBinding.total = totals
                newsListAdapter.updateTotalValues(totals)
                
            }

        })


        viewModel.articles.observe(this, Observer { articles ->
            articles?.let {
                recyclerDetails.visibility = View.VISIBLE
                newsListAdapter.updateNewsList(articles)

            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {

            }
        })
        
        viewModel.newsLoading.observe(this, Observer { newsIsLoading ->
            newsIsLoading?.let {
                println("$TAG DENNIS viewModel.newsloading is : $it")
                if (it){
                    newsListAdapter.showPreloader(true)
                }  else {
                    newsListAdapter.showPreloader(false)
                }
            }
        })
        
        viewModel.newsLoadError.observe(this, Observer { newsError ->
            newsError?.let {
                println("$TAG DENNIS newsLoadError is : $it")
                // Could pass the actual error from here - would need to send a string not a boolean
                var errorStr = activity?.resources?.getString(R.string.news_error_text)
                if (it){
                    newsListAdapter.updateNewsError(newsError, errorStr)
                }
            }
        })
    }
    
    companion object {
        private val TAG = CoinDetailFragment::class.java.simpleName
    }
}
