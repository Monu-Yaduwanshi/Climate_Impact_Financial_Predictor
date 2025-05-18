package com.example.climatexpert.ML

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ClimateModelHelper(context: Context) {

    private var interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(context))
    }
    data class PredictionResult(
        val probability: Float,
        val message: String
    )

    @Throws(IOException::class)
    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun predict(inputData: FloatArray): String {
        val input = arrayOf(inputData)
        val output = Array(1) { FloatArray(1) }
        interpreter.run(input, output)
        return if (output[0][0] > 0.5) {
            "⚠️ Likely insurance claim needed due to disaster risk."
        } else {
            "✅ Low disaster risk and no likely claim needed."
        }
    }
}



//3
//import android.content.Context
//import org.tensorflow.lite.Interpreter
//import java.io.FileInputStream
//import java.io.IOException
//import java.nio.MappedByteBuffer
//import java.nio.channels.FileChannel
//
//class ClimateModelHelper(context: Context) {
//
//    private var interpreter: Interpreter
//
//    // Replace these with actual values from Python's StandardScaler!
//    private val MEANS = floatArrayOf(42.5f, 30.0f, 20.0f, 50000f, 0.5f)    // scaler.mean_
//    private val STD_DEVS = floatArrayOf(10.2f, 5.5f, 3.0f, 25000f, 0.5f)  // scaler.scale_
//
//    init {
//        interpreter = Interpreter(loadModelFile(context))
//    }
//
//    @Throws(IOException::class)
//    private fun loadModelFile(context: Context): MappedByteBuffer {
//        val fileDescriptor = context.assets.openFd("model.tflite")
//        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//        val fileChannel = inputStream.channel
//        val startOffset = fileDescriptor.startOffset
//        val declaredLength = fileDescriptor.declaredLength
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//    }
//
//    private fun scaleInput(input: FloatArray): FloatArray {
//        return input.mapIndexed { i, value ->
//            (value - MEANS[i]) / STD_DEVS[i]
//        }.toFloatArray()
//    }
//
//    fun predict(inputData: FloatArray): PredictionResult {
//        val scaledInput = scaleInput(inputData)
//        val output = Array(1) { FloatArray(1) }
//        interpreter.run(arrayOf(scaledInput), output)
//        val probability = output[0][0]
//
//        return PredictionResult(
//            probability = probability,
//            message = if (probability > 0.5) "⚠️ High risk" else "✅ Low risk",
//            suggestions = getSuggestions(probability)
//        )
//    }
//
//    private fun getSuggestions(probability: Float): List<String> {
//        return when {
//            probability > 0.8f -> listOf(
//                "1. Contact insurer immediately",
//                "2. Relocate assets to safer location"
//            )
//            probability > 0.5f -> listOf(
//                "1. Review insurance coverage",
//                "2. Install protective measures"
//            )
//            else -> listOf("No urgent actions needed")
//        }
//    }
//
//    data class PredictionResult(
//        val probability: Float,
//        val message: String,
//        val suggestions: List<String>
//    )
//}