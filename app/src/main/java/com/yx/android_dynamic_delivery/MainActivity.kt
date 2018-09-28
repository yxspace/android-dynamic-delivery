package com.yx.android_dynamic_delivery

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View.OnClickListener
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.activity_main.*

private const val castFeatureName = "feature_cast"
private const val imageFeatureName = "feature_image"
private const val shareFeatureName = "feature_share"

class MainActivity : AppCompatActivity() {


    private lateinit var installManager: SplitInstallManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        castFeature.setOnClickListener(clickListener)
        imageFeature.setOnClickListener(clickListener)
        shareFeature.setOnClickListener(clickListener)

        installManager = SplitInstallManagerFactory.create(this)
    }

    override fun onResume() {
        super.onResume()

        installManager.registerListener(statusListener)
    }

    override fun onPause() {
        installManager.unregisterListener(statusListener)

        super.onPause()
    }

    private fun onCastFeatureClicked() {
        if (installManager.installedModules.contains(castFeatureName)) {
            Intent().setClassName(this, "com.yx.feature_cast.CastActivity")
                    .also { startActivity(it) }
        }
        else {
            val request = SplitInstallRequest.newBuilder()
                    .addModule(castFeatureName)
                    .build()

            installManager.startInstall(request)
        }
    }

    private fun onImageFeatureClicked() {
        if (installManager.installedModules.contains(imageFeatureName)) {
            Intent().setClassName(this, "com.yx.feature_image.ImageActivity")
                    .also { startActivity(it) }
        }
        else {
            val request = SplitInstallRequest.newBuilder()
                    .addModule(imageFeatureName)
                    .build()

            installManager.startInstall(request)
        }
    }

    private fun onShareFeatureClicked() {
        if (installManager.installedModules.contains(shareFeatureName)) {
            Intent().setClassName(this, "com.yx.feature_share.ShareActivity")
                    .also { startActivity(it) }
        }
        else {
            val request = SplitInstallRequest.newBuilder()
                    .addModule(shareFeatureName)
                    .build()

            installManager.startInstall(request)
        }
    }

    private val statusListener by lazy {
        SplitInstallStateUpdatedListener {

            var logTextStr = ""

            when (it.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> logTextStr = "$logTextStr\nstatus->DOWNLOADING"
                SplitInstallSessionStatus.CANCELED -> logTextStr = "$logTextStr\nstatus->CANCELED"
                SplitInstallSessionStatus.CANCELING -> logTextStr = "$logTextStr\nstatus->CANCELING"
                SplitInstallSessionStatus.DOWNLOADED -> logTextStr = "$logTextStr\nstatus->DOWNLOADED"
                SplitInstallSessionStatus.FAILED -> logTextStr = "$logTextStr\nstatus->FAILED"
                SplitInstallSessionStatus.INSTALLED -> logTextStr = "$logTextStr\nstatus->INSTALLED"
                SplitInstallSessionStatus.INSTALLING -> logTextStr = "$logTextStr\nstatus->INSTALLING"
                SplitInstallSessionStatus.PENDING -> logTextStr = "$logTextStr\nstatus->PENDING"
                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> logTextStr = "$logTextStr\nstatus->REQUIRES_USER_CONFIRMATION"
                SplitInstallSessionStatus.UNKNOWN -> logTextStr = "$logTextStr\nstatus->UNKNOWN"
            }

            logText.text = logTextStr
        }
    }

    private val clickListener by lazy {
        OnClickListener {
            when (it.id) {
                R.id.castFeature -> onCastFeatureClicked()
                R.id.imageFeature -> onImageFeatureClicked()
                R.id.shareFeature -> onShareFeatureClicked()
            }
        }
    }
}
