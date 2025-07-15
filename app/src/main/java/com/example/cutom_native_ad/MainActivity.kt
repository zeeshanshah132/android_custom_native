package com.example.cutom_native_ad

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
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

    private lateinit var adContainer6: FrameLayout
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var loadAdButton: Button
    private var nativeAd: NativeAd? = null
    private var isAdLoading = false
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val adRefreshHandler = Handler(Looper.getMainLooper())
    private var isActivityPaused = false
    private val adRefreshRunnable = object : Runnable {
        override fun run() {
            if (!isActivityPaused && isNetworkAvailable() && !isAdLoading) {
                loadNativeAd3()
            }
            // Schedule the next refresh
            adRefreshHandler.postDelayed(this, AD_REFRESH_INTERVAL)
        }
    }

    companion object {
        private const val AD_REFRESH_INTERVAL = 30_000L // 30 seconds in milliseconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)

        adContainer6 = findViewById(R.id.ad_container_6)
        shimmerLayout = findViewById(R.id.shimmer_layout)
        loadAdButton = findViewById(R.id.load_ad_button)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Initialize shimmer visibility based on internet
        updateShimmerVisibility()

        // Register network callback
        setupNetworkCallback()

        loadAdButton.setOnClickListener {
            if (isNetworkAvailable()) {
                if (nativeAd == null) {
                    loadNativeAd3()
                } else {
                    Toast.makeText(this, "Ad already loaded", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "No internet connection. Please check WiFi or mobile data.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Initial check for internet to load ad
        if (isNetworkAvailable() && nativeAd == null) {
            loadNativeAd3()
        }

        // Start the ad refresh loop
        startAdRefresh()
    }

    private fun setupNetworkCallback() {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                runOnUiThread {
                    if (!isAdLoading && nativeAd == null) {
                        showShimmer()
                        loadNativeAd3()
                    }
                }
            }

            override fun onLost(network: Network) {
                runOnUiThread {
                    dismissAd()
                    hideShimmer()
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

    private fun showShimmer() {
        if (isNetworkAvailable()) {
            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
            adContainer6.visibility = View.GONE
        }
    }

    private fun hideShimmer() {
        if (shimmerLayout.visibility == View.VISIBLE) {
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
        }
        adContainer6.visibility = if (nativeAd != null) View.VISIBLE else View.GONE
    }

    private fun updateShimmerVisibility() {
        if (isNetworkAvailable() && nativeAd == null && !isAdLoading) {
            showShimmer()
        } else {
            hideShimmer()
        }
    }

    private fun startAdRefresh() {
        // Remove any existing callbacks to prevent duplicates
        adRefreshHandler.removeCallbacks(adRefreshRunnable)
        // Schedule the first refresh
        adRefreshHandler.postDelayed(adRefreshRunnable, AD_REFRESH_INTERVAL)
    }

    private fun stopAdRefresh() {
        adRefreshHandler.removeCallbacks(adRefreshRunnable)
    }

    private fun loadNativeAd3() {
        if (isAdLoading) {
            Toast.makeText(this, "Ad is already loading", Toast.LENGTH_SHORT).show()
            return
        }

        isAdLoading = true
        loadAdButton.isEnabled = false
        showShimmer()
        Toast.makeText(this, "Loading ad...", Toast.LENGTH_SHORT).show()

        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { ad: NativeAd ->
                if (isDestroyed) {
                    ad.destroy()
                    return@forNativeAd
                }

                // Destroy previous ad if exists
                nativeAd?.destroy()
                nativeAd = ad

                // Inflate and populate ad view
                val adView = LayoutInflater.from(this)
                    .inflate(R.layout.sm_native_v6, adContainer6, false) as NativeAdView
                populateNativeAdView(ad, adView)

                // Clear container and add new ad view
                adContainer6.removeAllViews()
                adContainer6.addView(adView)
                isAdLoading = false
                loadAdButton.isEnabled = true
                hideShimmer()
                Toast.makeText(this, "Ad loaded successfully", Toast.LENGTH_SHORT).show()
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isAdLoading = false
                    loadAdButton.isEnabled = true
                    dismissAd()
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
        adContainer6.removeAllViews()
        updateShimmerVisibility()
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
                visibility = RatingBar.GONE
                rating = it.toFloat()
            }
        } ?: run {
            adView.starRatingView?.visibility = RatingBar.GONE
        }

        adView.setNativeAd(nativeAd)
    }

    override fun onResume() {
        super.onResume()
        isActivityPaused = false
        if (nativeAd != null) {
            hideShimmer()
        } else {
            updateShimmerVisibility()
        }
        startAdRefresh()
    }

    override fun onPause() {
        super.onPause()
        isActivityPaused = true
        shimmerLayout.stopShimmer()
        stopAdRefresh()
    }

    override fun onDestroy() {
        nativeAd?.destroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
        shimmerLayout.stopShimmer()
        stopAdRefresh()
        super.onDestroy()
    }
}