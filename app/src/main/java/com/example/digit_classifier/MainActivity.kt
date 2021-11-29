package com.example.digit_classifier

import android.content.ContentValues.TAG
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import com.divyanshu.draw.widget.DrawView
import com.example.digitclassifier.DigitClassifier

class MainActivity : AppCompatActivity() {

    private var drawView: DrawView? = null
    private var clearButton: Button? = null
    private var predictionTextView : TextView? = null
    private var digitClassifier = DigitClassifier(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawView = findViewById(R.id.draw_view)
        drawView?.setStrokeWidth(70.0f)
        drawView?.setColor(Color.WHITE)
        drawView?.setBackgroundColor(Color.BLACK)
        predictionTextView = findViewById(R.id.prediction_text)

        clearButton = findViewById(R.id.clr_btn)

        clearButton?.setOnClickListener{
            drawView?.clearCanvas()
            predictionTextView?.text = getString(R.string.prediction_place_holder)
        }

        drawView?.setOnTouchListener { _, event ->
        drawView.onTouchEvent(event)
            if(event.action == MotionEvent.ACTION_UP){
                classifyDrawing()
            }
            true
        }
        digitClassifier.initialzise().addOnFailureListener { e-> Log.e(TAG,"onCreate: error",e) }
    }
    override fun onDestroy(){
        digitClassifier.close()
        super.onDestroy()
    }

    private fun classifyDrawing(){
        val bitmap = drawView?.getBitmap()
        if((bitmap!=null )&& (digitClassifier.isInitialized)){
            digitClassifier.classifyAsyn(bitmap).addOnSuccessListener { resultText ->
                predictionTextView?.text = resultText
            }
                .addOnFailureListener { e->
                    predictionTextView?.text = getString(R.string.classification_error)
                }
        }
    }
    companion object{
        private val TAG = "MainActivity"
    }
}