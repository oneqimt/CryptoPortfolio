package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.databinding.FragmentCoinDetailBinding
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.model.TotalValues
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import com.imtmobileapps.cryptoportfolio.viewmodel.CoinDetailViewModel
import kotlinx.android.synthetic.main.fragment_coin_detail.*

/**
 * A simple [Fragment] subclass.
 */
class CoinDetailFragment : Fragment() {

    var selectedCoin : CryptoValue? = null
    private lateinit var viewModel : CoinDetailViewModel
    private lateinit var dataBinding: FragmentCoinDetailBinding
    var prefHelper = PreferencesHelper()
    var totalValues : TotalValues? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_coin_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // if args are not null
        arguments?.let {
            selectedCoin = CoinDetailFragmentArgs.fromBundle(it).selectedCoin
        }

        viewModel = ViewModelProviders.of(this).get(CoinDetailViewModel::class.java)
        viewModel.setCrypto(selectedCoin)

        // TEMP
        symbolImage.setOnClickListener {

            val personId = prefHelper.getCurrentPersonId()
            viewModel.getTotals(personId!!)
            //viewModel.logout()
        }

        observeViewModel()

    }

    fun observeViewModel(){

        viewModel.cryptoLiveData.observe(this , Observer {  crypto ->
            selectedCoin = crypto
            crypto?.let {
                dataBinding.crypto = crypto
            }
        })

        viewModel.totals.observe(this, Observer { totals ->
            totalValues = totals
            // add databinding
        })
    }
}
