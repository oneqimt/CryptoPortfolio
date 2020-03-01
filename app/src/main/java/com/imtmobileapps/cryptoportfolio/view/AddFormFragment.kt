package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.databinding.FragmentAddFormBinding
import com.imtmobileapps.cryptoportfolio.model.Coin
import com.imtmobileapps.cryptoportfolio.model.CoinHolding
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.model.Holdings
import com.imtmobileapps.cryptoportfolio.util.CoinApp.TEST_APP
import com.imtmobileapps.cryptoportfolio.util.PreferencesHelper
import com.imtmobileapps.cryptoportfolio.viewmodel.AddFormViewModel
import kotlinx.android.synthetic.main.fragment_add_form.*

class AddFormFragment : Fragment() {
    
    var coinToAdd: Coin? = null
    private lateinit var dataBinding: FragmentAddFormBinding
    private lateinit var viewModel: AddFormViewModel
    private var currentHolding: CryptoValue? = null
    private var flag : Int = 0
    var prefHelper = PreferencesHelper()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_form, container, false)
        return dataBinding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        arguments?.let {
            coinToAdd = AddFormFragmentArgs.fromBundle(it).coinToAdd
        }
        
        dataBinding.coinToAdd = coinToAdd
        
        println("$TAG $TEST_APP coinToAdd sent in is: $coinToAdd")
        
        (activity as AppCompatActivity).supportActionBar?.setTitle("Add ${coinToAdd?.coinName}")
        
        viewModel = activity?.run {
            ViewModelProvider(this).get(AddFormViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        
        // set defaults
        youHaveText.visibility = View.GONE
        quantityHeldText.visibility = View.GONE
        holdingValueText.visibility = View.GONE
        
        /*var filter : DecimalInputFilter = DecimalInputFilter()
        addFormQuantity.filters = arrayOf(filter)*/
        // will observe coins
        viewModel.getPersonHoldingsFromDatabase()
        
        addHoldingButton.setOnClickListener {
    
            val quantity = addFormQuantity.text.toString().toDouble()
            val cost = addFormCost.text.toString().toDouble()
            val holdingToAdd = Holdings(0, 0, quantity, cost, prefHelper.getCurrentPersonId())
            val coinHolding = CoinHolding(coinToAdd, holdingToAdd)
            
            if (flag == 1){
                viewModel.updateHolding(coinHolding)
            }else{
                viewModel.addHolding(coinHolding)
            }
            
        }
        
        observeModel()
    }
    
    private fun observeModel() {
        flag = 0
        viewModel.coins.observe(viewLifecycleOwner, Observer { cryptoValues ->
            cryptoValues.let {
                for (crypto in cryptoValues) {
                    
                    if (crypto.coin.coinSymbol.equals(coinToAdd?.coinSymbol, true)) {
                        println("$TAG $TEST_APP WE have a MATCH coin is : ${crypto.coin}")
                        flag = 1
                        currentHolding = crypto
                        dataBinding.currentHolding = crypto
                        youHaveText.visibility = View.VISIBLE
                        quantityHeldText.visibility = View.VISIBLE
                        holdingValueText.visibility = View.VISIBLE
                        
                        break
                    }
                    
                }
                
            }
        })
        
    }
    
    companion object {
        private val TAG = AddHoldingFragment::class.java.simpleName
    }
    
}
