package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.databinding.FragmentCoinDetailBinding
import com.imtmobileapps.cryptoportfolio.model.*
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import com.imtmobileapps.cryptoportfolio.viewmodel.CoinDetailViewModel
import io.cryptocontrol.cryptonewsapi.models.Article
import kotlinx.android.synthetic.main.fragment_coin_detail.*
import javax.xml.transform.Source


class CoinDetailFragment : Fragment() {

    var selectedCoin: CryptoValue? = null
    private lateinit var viewModel: CoinDetailViewModel
    private lateinit var dataBinding: FragmentCoinDetailBinding
    var prefHelper = PreferencesHelper()
    var totalValues: TotalValues? = null


    var coin : Coin = Coin("0", "BITCOIN", "BTC", "", "")
    var cryptoValue : CryptoValue = CryptoValue("BITCOIN", coin, "111", "45%", "22", "increase", 1.2)
    var totalv = TotalValues(1, "2", "3", "1", "increase")
    private val newsListAdapter = NewsListAdapter(cryptoValue, totalv, arrayListOf())


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

        val newList = ArrayList<News>()
        newList.add(News(0, "TitleOne", "a cool desc"))
        newList.add(News(0, "Titletwo", "a better desc"))
        newList.add(News(0, "Titlethree", "a even better desc"))
        newList.add(News(0, "Titlefour", "a four better desc"))
        newList.add(News(0, "Titlefive", "a five better desc"))
        newList.add(News(0, "Titlesix", "a six better desc"))


        viewModel = activity?.run {
            ViewModelProviders.of(this).get(CoinDetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


       // viewModel = ViewModelProviders.of(this).get(CoinDetailViewModel::class.java)
        viewModel.refresh(prefHelper.getCurrentPersonId()!!)
        viewModel.setNewsList(newList)
        viewModel.setCrypto(selectedCoin)


        recyclerDetails.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsListAdapter
        }

        // get the news for coin
        val coinName = selectedCoin?.coin?.coinName?.toLowerCase()
        println("OK COIN NAME is $coinName")
        viewModel.getCoinNews(coinName!!)

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

        viewModel.news.observe(this, Observer { news ->
            news?.let {
                recyclerDetails.visibility = View.VISIBLE
                newsListAdapter.updateNewsList(news)
            }

        })

        viewModel.articles.observe(this, Observer { articles ->
            articles?.let {
                for (article : Article in it){
                    println("IN OBSERvER and article is: ${article.title}")

                    println("IN OBSERVER and article source is : ${article.source.name}")
                }
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {

            }
        })
    }
}
