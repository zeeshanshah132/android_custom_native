package com.example.cutom_native_ad

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

class MainActivity : ComponentActivity() {

    private lateinit var adContainer3: FrameLayout
    private lateinit var adContainer5: FrameLayout
    private lateinit var loadAdButton: Button
    private var nativeAd: NativeAd? = null
    private var isAdLoading = false
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private lateinit var shimmerLayout_1: ShimmerFrameLayout
    private lateinit var shimmerLayout_2: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)

        adContainer3 = findViewById(R.id.sm_native_3)
        adContainer5 = findViewById(R.id.sm_native_5)
        loadAdButton = findViewById(R.id.load_ad_button)
        shimmerLayout_1 = findViewById(R.id.shimmer_layout_1)
        shimmerLayout_2 = findViewById(R.id.shimmer_layout_2)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Register network callback
        setupNetworkCallback()

        loadAdButton.setOnClickListener {
            if (isNetworkAvailable()) {
//                loadNativeAd3()
                loadNativeAd5()
            } else {
                Toast.makeText(
                    this,
                    "No internet connection. Please check WiFi or mobile data.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Initial check for internet to load ad
        if (isNetworkAvailable()) {
//            loadNativeAd3()
            loadNativeAd5()
        }
    }

    private fun setupNetworkCallback() {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                runOnUiThread {
                    if (!isAdLoading && nativeAd == null) {
//                        loadNativeAd3()
                        loadNativeAd5()
                    }
                }
            }

            override fun onLost(network: Network) {
                runOnUiThread {
                    dismissAd()
                    Toast.makeText(
                        this@MainActivity,
                        "Internet connection lost. Ad dismissed.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        val networkRequest =
            NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        )
    }


    private fun loadNativeAd3() {
        if (isAdLoading) {
            Toast.makeText(this, "Ad is already loading", Toast.LENGTH_SHORT).show()
            return
        }

        isAdLoading = true
        loadAdButton.isEnabled = false
        // Start shimmer effect
        shimmerLayout_1.isVisible = true
        shimmerLayout_1.startShimmer()
        Toast.makeText(this, "Loading ad...", Toast.LENGTH_SHORT).show()

        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { ad: NativeAd ->
                if (isDestroyed) {
                    ad.destroy()
                    return@forNativeAd
                }

                nativeAd?.destroy()
                nativeAd = ad

                val adView = LayoutInflater.from(this)
                    .inflate(R.layout.sm_native_v3, adContainer3, false) as NativeAdView

                populateNativeAdView(ad, adView)
                adContainer3.removeAllViews()
                adContainer3.addView(adView)

                isAdLoading = false
                loadAdButton.isEnabled = true
                // Stop and hide shimmer effect
                shimmerLayout_1.stopShimmer()
                shimmerLayout_1.isGone = true
                Toast.makeText(this, "Ad loaded successfully", Toast.LENGTH_SHORT).show()
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isAdLoading = false
                    loadAdButton.isEnabled = true
                    dismissAd()
                    // Stop and hide shimmer effect on ad failure
                    shimmerLayout_1.stopShimmer()
                    shimmerLayout_1.isGone = true
                    Toast.makeText(
                        this@MainActivity, "Ad failed to load: ${error.message}", Toast.LENGTH_LONG
                    ).show()
                }
            }).withNativeAdOptions(NativeAdOptions.Builder().build()).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }


    private fun loadNativeAd5() {
        if (isAdLoading) {
            Toast.makeText(this, "Ad is already loading", Toast.LENGTH_SHORT).show()
            return
        }

        isAdLoading = true
        loadAdButton.isEnabled = false
        // Start shimmer effect
        shimmerLayout_2.isVisible = true
        shimmerLayout_2.startShimmer()
        Toast.makeText(this, "Loading ad...", Toast.LENGTH_SHORT).show()

        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { ad: NativeAd ->
                if (isDestroyed) {
                    ad.destroy()
                    return@forNativeAd
                }

                nativeAd?.destroy()
                nativeAd = ad

                val adView = LayoutInflater.from(this)
                    .inflate(R.layout.sm_native_5, adContainer5, false) as NativeAdView

                populateNativeAdView(ad, adView)
                adContainer3.removeAllViews()
                adContainer3.addView(adView)

                isAdLoading = false
                loadAdButton.isEnabled = true
                // Stop and hide shimmer effect
                shimmerLayout_2.stopShimmer()
                shimmerLayout_2.isGone = true
                Toast.makeText(this, "Ad loaded successfully", Toast.LENGTH_SHORT).show()
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isAdLoading = false
                    loadAdButton.isEnabled = true
                    dismissAd()
                    // Stop and hide shimmer effect on ad failure
                    shimmerLayout_2.stopShimmer()
                    shimmerLayout_2.isGone = true
                    Toast.makeText(
                        this@MainActivity, "Ad failed to load: ${error.message}", Toast.LENGTH_LONG
                    ).show()
                }
            }).withNativeAdOptions(NativeAdOptions.Builder().build()).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun dismissAd() {
        nativeAd?.destroy()
        nativeAd = null
        adContainer3.removeAllViews()
        // Stop and hide shimmer effect when dismissing ad
        shimmerLayout_1.stopShimmer()
        shimmerLayout_1.isGone = true
        // Stop and hide shimmer effect when dismissing ad
        shimmerLayout_2.stopShimmer()
        shimmerLayout_2.isGone = true
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.apply {
            headlineView = adView.findViewById<TextView>(R.id.primary)
            bodyView = adView.findViewById<TextView>(R.id.secondary)
            callToActionView = adView.findViewById<Button>(R.id.cta)
            iconView = adView.findViewById<ImageView>(R.id.icon)
            starRatingView = adView.findViewById<RatingBar>(R.id.rating_bar)
        }

        (adView.headlineView as? TextView)?.text = nativeAd.headline
        (adView.bodyView as? TextView)?.text = nativeAd.body ?: ""

        nativeAd.callToAction?.let {
            (adView.callToActionView as? Button)?.apply {
                visibility = Button.VISIBLE
                text = it
            }
        } ?: run {
            adView.callToActionView?.visibility = Button.GONE
        }

        nativeAd.icon?.let {
            (adView.iconView as? ImageView)?.apply {
                visibility = ImageView.VISIBLE
                setImageDrawable(it.drawable)
            }
        } ?: run {
            adView.iconView?.visibility = ImageView.GONE
        }

        nativeAd.starRating?.let {
            (adView.starRatingView as? RatingBar)?.apply {
                visibility = RatingBar.VISIBLE
                rating = it.toFloat()
            }
        } ?: run {
            adView.starRatingView?.visibility = RatingBar.GONE
        }

        adView.setNativeAd(nativeAd)
    }

    override fun onDestroy() {
        nativeAd?.destroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
        shimmerLayout_1.stopShimmer()
        shimmerLayout_2.stopShimmer()
        super.onDestroy()
    }
}