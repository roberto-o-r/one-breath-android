package com.isscroberto.onebreath.main

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.media.SoundPool
import android.opengl.Visibility
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.isscroberto.onebreath.R
import com.isscroberto.onebreath.data.Config
import kotlinx.android.synthetic.main.activity_breathe.*
import android.media.AudioManager
import android.view.WindowManager
import com.github.stkent.amplify.prompt.DefaultLayoutPromptView
import com.github.stkent.amplify.tracking.Amplify


class BreatheActivity : AppCompatActivity(), BreatheContract.View {

    internal lateinit var presenter: BreatheContract.Presenter
    internal lateinit var animatorSet: AnimatorSet
    internal lateinit var startAnimator : ValueAnimator
    internal lateinit var inhaleAnimator : ValueAnimator
    internal lateinit var exhaleAnimator : ValueAnimator
    internal lateinit var finishAnimator : ValueAnimator
    internal var breathing: Boolean = false
    internal var config: Config = Config()
    internal lateinit var vibrator : Vibrator
    internal lateinit var soundPool : SoundPool
    internal var soundId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breathe)

        // Prevent phone from going to sleep.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // On circle click start breathing.
        view_circle.setOnClickListener(View.OnClickListener {
            // Verify if breathing has not started.
            if(!breathing) {
                performNotification()
                // Set time of breathing.
                when (spinner_minutes.selectedItemPosition) {
                    0 -> config.minutes = 2
                    1 -> config.minutes = 5
                    2 -> config.minutes = 10
                }
                presenter.startBreathing(config)
            }
        })

        // Toggle sound on/off.
        image_sound.setOnClickListener(View.OnClickListener {
            if(config.sound) {
                config.sound = false;
                image_sound.setAlpha(0.5f)
            } else {
                config.sound = true;
                image_sound.setAlpha(1f)
            }
        })

        // Toggle vibration on/off.
        image_vibrate.setOnClickListener(View.OnClickListener {
            if(config.vibration) {
                config.vibration = false;
                image_vibrate.setAlpha(0.5f)
            } else {
                config.vibration = true;
                image_vibrate.setAlpha(1f)
            }
        })

        // Create vibrator.
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Create sound player.
        soundPool = SoundPool(5, AudioManager.STREAM_MUSIC, 0)
        soundId = soundPool.load(this, R.raw.ding, 1)

        // Feedback.
        if(savedInstanceState == null) {
            var promptView = prompt_view as DefaultLayoutPromptView
            Amplify.getSharedInstance().promptIfReady(promptView)
        }

        // Create the presenter.
        BreathePresenter(this);
        presenter.start();

    }

    override fun onPause() {
        super.onPause()
        if(breathing) {
            presenter.stopBreathing()
        }
    }

    override fun onBackPressed() {
        if(breathing) {
            presenter.stopBreathing()
        } else {
            super.onBackPressed()
        }
    }

    override fun setPresenter(presenter: BreatheContract.Presenter) {
        this.presenter = presenter;
    }

    override fun loadConfiguration(list: ArrayList<String>) {
        val adapter = ArrayAdapter(this, R.layout.spinner_item, list)
        spinner_minutes.setAdapter(adapter)
    }

    override fun showConfiguration() {
        layout_configuration.visibility = View.VISIBLE;
    }

    override fun hideConfiguration() {
        layout_configuration.visibility = View.INVISIBLE
    }

    override fun startAnimation() {
        // Breathing has started.
        breathing = true;

        // Setup start and finish animation.
        startAnimator = ValueAnimator.ofFloat(1f, 0f)
        startAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            view_circle.scaleX = value
            view_circle.scaleY = value
        }
        startAnimator.duration = 1000
        finishAnimator = ValueAnimator.ofFloat(0f, 1f)
        finishAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            view_circle.scaleX = value
            view_circle.scaleY = value
        }
        finishAnimator.duration = 1000
        // Setup inhale, exhale bundle animation.
        inhaleAnimator = ValueAnimator.ofFloat(0f, 5f)
        inhaleAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            view_circle.scaleX = value
            view_circle.scaleY = value
        }
        exhaleAnimator = ValueAnimator.ofFloat(5f, 0f)
        exhaleAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            view_circle.scaleX = value
            view_circle.scaleY = value
        }
        animatorSet = AnimatorSet()
        animatorSet.play(inhaleAnimator).before(exhaleAnimator)
        animatorSet.duration = 5000

        // Start animation.
        startAnimator.start()

        // Start animator listener.
        startAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}

            override fun onAnimationEnd(p0: Animator?) {
                performNotification()
                // Start breathing cycle animation.
                if(breathing) {
                    animatorSet.start()
                }
            }

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationStart(p0: Animator?) {}

        })

        // Breathing cycle animator listener.
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                if(breathing) {
                    animatorSet.start()
                } else {
                    finishAnimator.start()
                }
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}

        })

        inhaleAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                performNotification()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        exhaleAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                performNotification()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    override fun stopAnimation() {
        // Stop breathing.
        breathing = false
        // Show configuration.
        showConfiguration()
        // Fonce animation end.
        startAnimator.removeAllListeners()
        startAnimator.end()
        startAnimator.cancel()
        finishAnimator.removeAllListeners()
        finishAnimator.end()
        finishAnimator.cancel()
        inhaleAnimator.removeAllListeners()
        inhaleAnimator.end()
        inhaleAnimator.cancel()
        exhaleAnimator.removeAllListeners()
        exhaleAnimator.end()
        exhaleAnimator.cancel()
        animatorSet.end()
        animatorSet.removeAllListeners()
        animatorSet.cancel()
    }

    private fun performNotification() {
        if(config.vibration) {
            performHapticFeedback()
        }
        if(config.sound) {
            performSound()
        }
    }

    private fun performHapticFeedback() {
        view_circle.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
    }

    private fun performSound() {
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

}
