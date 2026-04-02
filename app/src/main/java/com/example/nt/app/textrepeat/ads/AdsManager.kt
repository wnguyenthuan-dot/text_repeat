package com.example.nt.app.textrepeat.ads

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.dino.ads.admob.AdmobUtils
import com.dino.ads.remote.BannerHolder
import com.dino.ads.remote.InterHolder
import com.dino.ads.remote.NativeHolder
import com.dino.ads.remote.NativeMultiHolder
import com.example.nt.app.textrepeat.RemoteConfig

object AdsManager {
    var isDebug = true

    fun loadAndShowInter(activity: Activity, holder: InterHolder, onFinished: () -> Unit) {
        AdmobUtils.loadAndShowInterstitial(activity as AppCompatActivity, holder) {
            onFinished()
        }
    }

    fun loadAndShowBanner(activity: AppCompatActivity, holder: BannerHolder, viewGroup: ViewGroup) {
        AdmobUtils.loadAndShowBanner(
            activity, holder, viewGroup,
            object : AdmobUtils.BannerCallback() {},
            object : AdmobUtils.NativeCallback() {})
    }

    fun loadAndShowNative(activity: AppCompatActivity, holder: NativeHolder, viewGroup: ViewGroup) {
        AdmobUtils.loadAndShowNative(
            activity,
            holder,
            viewGroup,
            object : AdmobUtils.NativeCallback() {})
    }

    fun loadAndShowNative(activity: Context, holder: NativeHolder, viewGroup: ViewGroup) {
        AdmobUtils.loadAndShowNative(
            activity as AppCompatActivity,
            holder,
            viewGroup,
            object : AdmobUtils.NativeCallback() {})
    }

    fun loadNativeFullscreen(context: Context, holder: NativeHolder) {
        AdmobUtils.loadNativeFull(context, holder, object : AdmobUtils.NativeCallback() {})
    }

    fun showNativeFullScreen(activity: Activity, holder: NativeHolder, viewGroup: ViewGroup) {
        AdmobUtils.showNativeFull(
            activity,
            holder,
            viewGroup,
            object : AdmobUtils.NativeCallbackSimple() {})
    }

    fun loadNativeLanguage(context: Context) {
        AdmobUtils.loadNativeLanguage(
            context,
            RemoteConfig.NATIVE_LANGUAGE,
            object : AdmobUtils.NativeCallback() {})
        AdmobUtils.loadNative(
            context,
            RemoteConfig.NATIVE_LANGUAGE_SMALL,
            object : AdmobUtils.NativeCallback() {})
    }

    fun showNativeLanguage(
        activity: Activity,
        holder: NativeMultiHolder,
        viewGroup: ViewGroup,
        position: Int,
    ) {
        AdmobUtils.showNativeLanguage(
            activity,
            holder,
            viewGroup,
            position,
            object : AdmobUtils.NativeCallbackSimple() {})
    }

    fun showNativeLanguageSmall(
        activity: AppCompatActivity,
        holder: NativeHolder,
        viewGroup: ViewGroup,
    ) {
        AdmobUtils.showNative(
            activity, holder, viewGroup,
            object : AdmobUtils.NativeCallbackSimple() {}
        )
    }

    fun loadNativeIntro(context: Context, holder: NativeMultiHolder) {
        AdmobUtils.loadNativeIntro(context, holder, object : AdmobUtils.NativeCallback() {})
    }

    fun loadNative(context: Context, holder: NativeHolder) {
        AdmobUtils.loadNative(context, holder, object : AdmobUtils.NativeCallback() {})
    }

    fun showNative(context: AppCompatActivity, holder: NativeHolder, viewGroup: ViewGroup) {
        AdmobUtils.showNative(
            context,
            holder,
            viewGroup,
            object : AdmobUtils.NativeCallbackSimple() {})
    }

    fun showNativeIntro(
        activity: Activity,
        holder: NativeMultiHolder,
        viewGroup: ViewGroup,
        position: Int,
    ) {
        AdmobUtils.showNativeIntro(
            activity,
            holder,
            viewGroup,
            position,
            object : AdmobUtils.NativeCallbackSimple() {})
    }

}