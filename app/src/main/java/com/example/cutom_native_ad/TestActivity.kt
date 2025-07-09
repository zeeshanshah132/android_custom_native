package com.example.cutom_native_ad

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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

class TestActivity : ComponentActivity() {

    private lateinit var adContainer1: FrameLayout
    private lateinit var adContainer2: FrameLayout
    private lateinit var adContainer3: FrameLayout
    private lateinit var adContainer4: FrameLayout
    var native_ad_key = "ca-app-pub-3940256099942544/1044960115"

    private lateinit var loadAdButton: Button
    private var nativeAd: NativeAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test)

        MobileAds.initialize(this)

        adContainer1 = findViewById(R.id.sm_native_1)
        adContainer2 = findViewById(R.id.sm_native_2)
        adContainer3 = findViewById(R.id.sm_native_3)
        adContainer4 = findViewById(R.id.sm_native_4)
        loadAdButton = findViewById(R.id.load_ad_button)

        loadAdButton.setOnClickListener {
//            loadNativeAd1()
//            loadNativeAdmob(
//                findViewById(R.id.nativeLayout),
//                native_ad_key,
//                R.layout.sm_native_v4,
//                {
//
//                }, {
//
//                })
//            loadNativeAd2()
//            loadNativeAd3()
            loadNativeAd4()
        }
    }


    private fun loadNativeAd1() {
        val adLoader = AdLoader.Builder(this, native_ad_key) // Test ad unit
            .forNativeAd { ad: NativeAd ->
                if (isDestroyed) {
                    ad.destroy()
                    return@forNativeAd
                }

                nativeAd?.destroy()
                nativeAd = ad

                // Inflate the layout using the parent (for <merge>)
                val adView = LayoutInflater.from(this)
                    .inflate(R.layout.small_native_v1, adContainer1, false) as NativeAdView

                populateNativeAdView(ad, adView)
                adContainer1.removeAllViews()
                adContainer1.addView(adView)
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Toast.makeText(
                        this@TestActivity, "Ad Load Failed: ${error.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }).withNativeAdOptions(NativeAdOptions.Builder().build()).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun loadNativeAd2() {
        val adLoader = AdLoader.Builder(this, native_ad_key) // Test ad unit
            .forNativeAd { ad: NativeAd ->
                if (isDestroyed) {
                    ad.destroy()
                    return@forNativeAd
                }

                nativeAd?.destroy()
                nativeAd = ad

                // Inflate the layout using the parent (for <merge>)
                val adView = LayoutInflater.from(this)
                    .inflate(R.layout.sm_native_v2, adContainer2, false) as NativeAdView

                populateNativeAdView(ad, adView)
                adContainer2.removeAllViews()
                adContainer2.addView(adView)
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Toast.makeText(
                        this@TestActivity, "Ad Load Failed: ${error.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }).withNativeAdOptions(NativeAdOptions.Builder().build()).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun loadNativeAd3() {
        val adLoader = AdLoader.Builder(this, native_ad_key) // Test ad unit
            .forNativeAd { ad: NativeAd ->
                if (isDestroyed) {
                    ad.destroy()
                    return@forNativeAd
                }

                nativeAd?.destroy()
                nativeAd = ad

                // Inflate the layout using the parent (for <merge>)
                val adView = LayoutInflater.from(this)
                    .inflate(R.layout.sm_native_v3, adContainer3, false) as NativeAdView

                populateNativeAdView(ad, adView)
                adContainer3.removeAllViews()
                adContainer3.addView(adView)
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Toast.makeText(
                        this@TestActivity, "Ad Load Failed: ${error.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }).withNativeAdOptions(NativeAdOptions.Builder().build()).build()

        adLoader.loadAd(AdRequest.Builder().build())
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


    private fun loadNativeAd4() {
        val builder =
            AdLoader.Builder(this, "ca-app-pub-3940256099942544/1044960115") // test ad unit ID

        builder.forNativeAd { ad: NativeAd ->
            // Clean up previous ad
            nativeAd?.destroy()
            nativeAd = ad

            val inflater = LayoutInflater.from(this)
                .inflate(R.layout.large_native_v1, adContainer4, false) as NativeAdView
            populateNativeMediaView(ad, inflater)
            adContainer4.removeAllViews()
            adContainer4.addView(inflater)
        }.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                Toast.makeText(
                    this@TestActivity, "Ad Load Failed: ${error.message}", Toast.LENGTH_SHORT
                ).show()
            }
        })
        val videoOptions = VideoOptions.Builder().setStartMuted(true).build()
        builder.withNativeAdOptions(NativeAdOptions.Builder().setVideoOptions(videoOptions).build())
        builder.build().loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeMediaView(nativeAd: NativeAd, adView: NativeAdView) {
        val mediaView = adView.findViewById<MediaView>(R.id.media_view)
        val headlineView = adView.findViewById<TextView>(R.id.primary)
        val bodyView = adView.findViewById<TextView>(R.id.secondary)
        val callToActionView = adView.findViewById<Button>(R.id.cta)
        val iconView = adView.findViewById<ImageView>(R.id.icon)
        val ratingBar = adView.findViewById<RatingBar>(R.id.rating_bar)

        adView.let {
            it.mediaView = mediaView
            it.headlineView = headlineView
            it.bodyView = bodyView
            it.callToActionView = callToActionView
            it.iconView = iconView
        }
        headlineView.text = nativeAd.headline
        callToActionView.text = nativeAd.callToAction
        bodyView.text = nativeAd.body

        val icon = nativeAd.icon

        iconView.let {
            if (icon != null) {
                it.setImageDrawable(icon.drawable)
                it.visibility = ImageView.VISIBLE
            } else {
                it.visibility = ImageView.GONE
            }
        }


        val rating = nativeAd.starRating?.toFloat()
        if (rating != null && rating > 0) {
            ratingBar.visibility = RatingBar.VISIBLE
            ratingBar.rating = rating
        } else {
            ratingBar.visibility = RatingBar.GONE
        }

        mediaView.mediaContent = nativeAd.mediaContent

        adView.setNativeAd(nativeAd)
    }


    override fun onDestroy() {
        nativeAd?.destroy()
        super.onDestroy()
    }
}