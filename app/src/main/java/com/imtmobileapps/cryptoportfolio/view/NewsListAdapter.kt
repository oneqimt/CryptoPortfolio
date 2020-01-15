package com.imtmobileapps.cryptoportfolio.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.databinding.ItemCryptoHeaderBinding
import com.imtmobileapps.cryptoportfolio.databinding.ItemHoldingsBinding
import com.imtmobileapps.cryptoportfolio.databinding.ItemListNewsBinding
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import com.imtmobileapps.cryptoportfolio.model.TotalValues
import com.imtmobileapps.cryptoportfolio.util.getPublishedFormat
import io.cryptocontrol.cryptonewsapi.models.Article

class NewsListAdapter(
    var cryptoValue: CryptoValue,
    var totalValues: TotalValues,
    val articleList: ArrayList<Article>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        // set a default
        val view = DataBindingUtil.inflate<ItemListNewsBinding>(
            inflater,
            R.layout.item_list_news,
            parent,
            false
        )

        when (viewType) {
            CellType.CRYPTO_HEADER.ordinal -> {

                val cryptoview = DataBindingUtil.inflate<ItemCryptoHeaderBinding>(
                    inflater,
                    R.layout.item_crypto_header,
                    parent,
                    false
                )
                return CryptoHeaderViewHolder(cryptoview)
            }
            CellType.TOTAL_HOLDINGS.ordinal -> {
                val holdingsview = DataBindingUtil.inflate<ItemHoldingsBinding>(
                    inflater,
                    R.layout.item_holdings,
                    parent,
                    false
                )

                return HoldingsViewHolder(holdingsview)
            }

            CellType.NEWS_ITEM.ordinal -> {
                val newsview = DataBindingUtil.inflate<ItemListNewsBinding>(
                    inflater,
                    R.layout.item_list_news,
                    parent,
                    false
                )

                return NewsViewHolder(newsview, articleList)
            }

        }
        // should not get here
        return NewsViewHolder(view, articleList)

    }

    fun updateNewsList(newArticleList: List<Article>){
        articleList.clear()
        articleList.addAll(newArticleList)
        notifyDataSetChanged()
    }

    fun updateCryptoValue(cryptoValue: CryptoValue){
        this.cryptoValue = cryptoValue
        notifyDataSetChanged()
    }

    fun updateTotalValues(totalValues: TotalValues){
        this.totalValues = totalValues
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = articleList.size + 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            CellType.CRYPTO_HEADER.ordinal -> {
                val cryptoViewHolder = holder as CryptoHeaderViewHolder
                cryptoViewHolder.view.cryptoItemHeader = cryptoValue
            }
            CellType.TOTAL_HOLDINGS.ordinal -> {
                val holdingsViewHolder = holder as HoldingsViewHolder
                holdingsViewHolder.view.total = totalValues
                holdingsViewHolder.view.cryptoHoldings = cryptoValue
            }
            CellType.NEWS_ITEM.ordinal -> {
                val newsViewHolder = holder as NewsViewHolder
                newsViewHolder.view.article = articleList[position - 2]

                val str = getPublishedFormat(articleList[position -2].publishedAt)
                println("PUBLISHED AT is now: $str")
                newsViewHolder.newsPublishedAt.text = str

                newsViewHolder.view.listener = newsViewHolder
            }
        }

    }

    override fun getItemViewType(position: Int): Int {

        return when(position){
            0 -> CellType.CRYPTO_HEADER.ordinal
            1 -> CellType.TOTAL_HOLDINGS.ordinal
            else -> CellType.NEWS_ITEM.ordinal
        }

    }

    class CryptoHeaderViewHolder(
        var view: ItemCryptoHeaderBinding
    ) : RecyclerView.ViewHolder(view.root) {

    }

    class HoldingsViewHolder(var view: ItemHoldingsBinding) :
        RecyclerView.ViewHolder(view.root) {

    }

    class NewsViewHolder(var view: ItemListNewsBinding, var articleList: ArrayList<Article>) :
        RecyclerView.ViewHolder(view.root), NewsClickListener {

        private var selectedNews : Article? = null

        val newsPublishedAt = view.newsPublishedAt

        override fun onNewsClicked(v: View) {
            selectedNews = articleList[adapterPosition -2]
            println("URL of article is : ${selectedNews?.url}")

        }
    }

    @Suppress("UNUSED_PARAMETER")
    enum class CellType(viewType: Int) {
        CRYPTO_HEADER(0),
        TOTAL_HOLDINGS(1),
        NEWS_ITEM(2)
    }
}