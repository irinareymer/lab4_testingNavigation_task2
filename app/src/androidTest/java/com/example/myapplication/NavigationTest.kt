package com.example.myapplication

import android.content.pm.ActivityInfo
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @Test
    fun testAbout() {
        launchActivity<MainActivity>()
        // in fragment 1
        openAbout()
        checkIsDisplaced(R.id.activity_about)
        pressBack()
        isDisplacedFragment1()
        pressButton(R.id.bnToSecond)
        // in fragment 2
        openAbout()
        checkIsDisplaced(R.id.activity_about)
        pressBack()
        isDisplacedFragment2()
        pressButton(R.id.bnToThird)
        // in fragment 3
        openAbout()
        checkIsDisplaced(R.id.activity_about)
        pressBack()
        isDisplacedFragment3()
    }

    @Test(expected = NoActivityResumedException::class)
    fun testBackStackFromFr1(){
        // Fr1 - back  -> noActivity
        launchActivity<MainActivity>()
        pressBack()
    }
    @Test(expected = NoActivityResumedException::class)
    fun testBackStackFromFr2(){
        // Fr1 -> Fr2 - back -> Fr1 -> Fr2 -> Fr1 - back -> noActivity
        launchActivity<MainActivity>()
        pressButton(R.id.bnToSecond)
        pressBack()
        checkIsDisplaced(R.id.fragment1)
        pressButton(R.id.bnToSecond)
        pressButton(R.id.bnToFirst)
        pressBack()
    }
    @Test(expected = NoActivityResumedException::class)
    fun testBackStackFromFr3(){
        // Fr1 -> Fr2 -> Fr3 - back -> Fr2 -> Fr3 -> Fr2 - back ->
        // -> Fr1 -> Fr2 -> Fr3 -> Fr1 - back -> noActivity
        launchActivity<MainActivity>()
        pressButton(R.id.bnToSecond)
        pressButton(R.id.bnToThird)
        pressBack()
        checkIsDisplaced(R.id.fragment2)
        pressButton(R.id.bnToThird)
        pressButton(R.id.bnToSecond)
        pressBack()
        checkIsDisplaced(R.id.fragment1)
        pressButton(R.id.bnToSecond)
        pressButton(R.id.bnToThird)
        pressButton(R.id.bnToFirst)
        pressBack()
    }

    @Test
    fun testOrientation(){
        val activityScenario = launchActivity<MainActivity>()
        // in fragment 1
        changeOrientation(activityScenario)
        isDisplacedFragment1()
        pressButton(R.id.bnToSecond)
        isDisplacedFragment2()
        pressButton(R.id.bnToFirst)
        changeOrientationBack(activityScenario)
        pressButton(R.id.bnToSecond)
        // in fragment 2
        changeOrientation(activityScenario)
        isDisplacedFragment2()
        pressButton(R.id.bnToThird)
        isDisplacedFragment3()
        pressButton(R.id.bnToSecond)
        changeOrientationBack(activityScenario)
        pressButton(R.id.bnToThird)
        // in fragment 3
        changeOrientation(activityScenario)
        isDisplacedFragment3()
        openAbout()
        checkIsDisplaced(R.id.activity_about)
        pressBack()
        changeOrientationBack(activityScenario)
        openAbout()
        // in AboutActivity
        changeOrientation(activityScenario)
        checkIsDisplaced(R.id.activity_about)
    }

    @Test
    fun testUpNavigation(){
        launchActivity<MainActivity>()
        pressButton(R.id.bnToSecond)
        pressButton(R.id.bnToThird)
        openAbout()
        // in AboutActivity
        navUp()
        isDisplacedFragment3()
        // in fragment 3
        navUp()
        isDisplacedFragment2()
        // in fragment 2
        navUp()
        isDisplacedFragment1()
    }

    private fun pressButton(id: Int){
        onView(withId(id))
            .perform(click())
    }

    private fun checkIsDisplaced(id: Int){
        onView(withId(id))
            .check(matches(isDisplayed()))
    }

    private fun isDisplacedFragment1(){
        checkIsDisplaced(R.id.fragment1)
        checkIsDisplaced(R.id.bnToSecond)
    }
    private fun isDisplacedFragment2(){
        checkIsDisplaced(R.id.fragment2)
        checkIsDisplaced(R.id.bnToFirst)
        checkIsDisplaced(R.id.bnToThird)
    }
    private fun isDisplacedFragment3(){
        checkIsDisplaced(R.id.fragment3)
        checkIsDisplaced(R.id.bnToSecond)
        checkIsDisplaced(R.id.bnToFirst)
    }

    private fun changeOrientation(activityScenario: ActivityScenario<MainActivity>){
        activityScenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Thread.sleep(1000)
    }

    private fun changeOrientationBack(activityScenario: ActivityScenario<MainActivity>){
        activityScenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        Thread.sleep(1000)
    }

    private fun navUp(){
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(click())
    }
}