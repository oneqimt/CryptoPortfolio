package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.viewmodel.ManageHoldingsViewModel
import kotlinx.android.synthetic.main.fragment_add_holding.*
import kotlinx.android.synthetic.main.fragment_coin_list.*

class AddHoldingFragment : Fragment() {
    
    private lateinit var viewModel: ManageHoldingsViewModel
    private val manageCoinAdapter = ManageCoinAdapter(arrayListOf())
    
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_holding, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    
        viewModel = ViewModelProviders.of(this).get(ManageHoldingsViewModel::class.java)
        
        viewModel.fetchCoinsFromServer()
        
        //ddHoldingListView
        addHoldingListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = manageCoinAdapter
        }
        
        observeViewModel()
    }
    
    fun observeViewModel() {
        viewModel.coinsFromServer.observe(this, Observer { coins ->
            coins?.let {
                println("$TAG ${CoinApp.TEST_APP} in observer and coins are HERE!!!")
                manageCoinAdapter.updateCoinList(coins)
                
            }
            
        })
    }
    
    companion object {
        private val TAG = AddHoldingFragment::class.java.simpleName
    }
    
    
}
