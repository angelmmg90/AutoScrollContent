package com.teseo.studios.autoscrollcontentexample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.teseo.studios.autoscrollcontentexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val lm = LinearLayoutManager(this)
    private var reverse = false
    private var loop = false
    private var canTouch = false
    private var orientationLm = LinearLayoutManager.HORIZONTAL
    private var currentSpeed = 40

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()
    }

    private fun initView() {
        lm.orientation = LinearLayoutManager.HORIZONTAL

        binding.rvAutoScrollContent.layoutManager = lm

        setUpAutoScrollContent(
            listOf(
                ExampleModel("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua.")
            )
        ) {
            Toast.makeText(this, "Item clicked", Toast.LENGTH_SHORT).show()
        }
        setUpSwLoopChecked()
        setUpSwReverseChecked()
        setUpSwHorizontalVerticalChecked()
        setUpSwCanTouchChecked()
        setUpSeekBarCurrentSpeed()
    }

    private fun setUpAutoScrollContent(messagesList: List<ExampleModel>, onItemClicked: (String) -> Unit) {

        val adapter = ExampleAdapter().apply {
            submitList(messagesList)
        }

        binding.rvAutoScrollContent.adapter = adapter

        binding.rvAutoScrollContent.setItemClickListener { viewHolder, position ->
            viewHolder?.let {
                adapter.onLinkItem(viewHolder, position, onItemClicked)
            }
        }
    }

    private fun setUpSwLoopChecked() {
        binding.swLoppContent.setOnCheckedChangeListener { _, isChecked ->
            loop = isChecked
            setUpLoop()
            setUpSpeed()
            setUpReverse()
            setUpCanTouch()
            setUpOrientationLm()
        }
    }

    private fun setUpSwReverseChecked() {
        binding.swReverseContent.setOnCheckedChangeListener { _, isChecked ->
            reverse = isChecked
            setUpLoop()
            setUpSpeed()
            setUpReverse()
            setUpCanTouch()
            setUpOrientationLm()
        }
    }

    private fun setUpSwCanTouchChecked() {
        binding.swCanTouchContent.setOnCheckedChangeListener { _, isChecked ->
            canTouch = isChecked
            setUpLoop()
            setUpSpeed()
            setUpReverse()
            setUpCanTouch()
            setUpOrientationLm()
        }
    }

    private fun setUpSwHorizontalVerticalChecked() {
        binding.swHorizontalVerticalContent.setOnCheckedChangeListener { _, isChecked ->
            orientationLm = if (isChecked) {
                LinearLayoutManager.VERTICAL
            } else {
                LinearLayoutManager.HORIZONTAL
            }

            setUpLoop()
            setUpSpeed()
            setUpReverse()
            setUpCanTouch()
            setUpOrientationLm()
        }
    }

    private fun setUpSeekBarCurrentSpeed(){
        binding.seekbar.progress = currentSpeed
        binding.seekbar.max = 100

        binding.seekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                progress: Int, fromUser: Boolean) {
                currentSpeed = seek.progress
                setUpSpeed()
            }

            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                currentSpeed = seek.progress
                setUpSpeed()
                Toast.makeText(this@MainActivity,
                    "Progress is: " + seek.progress + "%",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUpSpeed(){
        openAutoScroll()
    }

    private fun setUpLoop() {
        binding.rvAutoScrollContent.setLoopEnabled(loop)
        openAutoScroll()
    }

    private fun setUpReverse() {
        openAutoScroll()
    }

    private fun setUpCanTouch() {
        binding.rvAutoScrollContent.setCanTouch(canTouch)
    }

    private fun openAutoScroll(){
        binding.rvAutoScrollContent.openAutoScroll(speed = currentSpeed, reverse = reverse)
    }

    @SuppressLint("WrongConstant")
    private fun setUpOrientationLm() {
        lm.orientation = orientationLm
        binding.rvAutoScrollContent.layoutManager = lm
    }
}