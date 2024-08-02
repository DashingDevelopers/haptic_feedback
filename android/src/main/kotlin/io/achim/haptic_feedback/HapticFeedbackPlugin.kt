package io.achim.haptic_feedback

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class HapticFeedbackPlugin : FlutterPlugin, MethodCallHandler {
  private lateinit var channel: MethodChannel
  private lateinit var vibrator: Vibrator

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "haptic_feedback")
    channel.setMethodCallHandler(this)
    vibrator = flutterPluginBinding.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "canVibrate") {
      canVibrate(result)
    } else {
      val pattern = Pattern.values().find { it.name == call.method }
      if (pattern != null) {
        vibratePattern(pattern, result)
      } else {
        result.notImplemented()
      }
    }
  }

  private fun canVibrate(result: Result) {
    result.success(vibrator.hasVibrator())
  }

  private fun vibratePattern(pattern: Pattern, result: Result) {
    try {
      if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator.hasAmplitudeControl()) {
        val effect = VibrationEffect.createWaveform(pattern.lengths, pattern.amplitudes, -1)
        vibrator.vibrate(effect)
      } else {
        vibrator.vibrate(pattern.lengths, -1)
      }
      result.success(null)
    } catch (e: Exception) {
      result.error("VIBRATION_ERROR", "Failed to vibrate", e.localizedMessage)
    }
  }

  private enum class Pattern(val lengths: LongArray, val amplitudes: IntArray) {
    success(longArrayOf(0,75, 75, 75), intArrayOf(0,178, 0, 255)),
    warning(longArrayOf(0,79, 119, 75), intArrayOf(0,227, 0, 178)),
    error(longArrayOf(0,75, 61, 79, 57, 75, 57, 97), intArrayOf(0,203, 0, 200, 0, 252, 0, 150)),
    light(longArrayOf(0,79), intArrayOf(0,154)),
    medium(longArrayOf(0,79), intArrayOf(0,203)),
    heavy(longArrayOf(0,75), intArrayOf(0,252)),
    rigid(longArrayOf(0,48), intArrayOf(0,227)),
    soft(longArrayOf(0,110), intArrayOf(0,178)),
    selection(longArrayOf(0,57), intArrayOf(0,150))
  }
}
