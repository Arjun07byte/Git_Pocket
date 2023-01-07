package com.example.daggerfirst.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import com.example.daggerfirst.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var sideBarMenuToggle: ActionBarDrawerToggle
    private lateinit var parentLayoutSideBarDrawer: DrawerLayout
    private var currFragment = "Home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // setting the state of the Home Screen BottomSheet as
        // expanded for better UI
        val parentLayoutBottomSheet: FrameLayout =
            findViewById(R.id.layout_home_screen_bottom_sheet)
        val expandedSheetBehavior = BottomSheetBehavior.from(parentLayoutBottomSheet)
        expandedSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        // setting up the side bar view variables
        setUpSideBarDrawer()

        val buttonOpenSideBarMenu: ImageButton = findViewById(R.id.button_home_openSideBar)
        val buttonDismissSideBarMenu: ImageButton = findViewById(R.id.button_sidebarMenu_dismiss)

        // setting up button to close and open the drawer
        buttonOpenSideBarMenu.setOnClickListener { parentLayoutSideBarDrawer.open() }
        buttonDismissSideBarMenu.setOnClickListener { parentLayoutSideBarDrawer.close() }

        setUpButtonListenerForFragmentNavigation()
    }

    private fun setUpSideBarDrawer() {
        parentLayoutSideBarDrawer = findViewById(R.id.layout_homeDrawer)
        sideBarMenuToggle = ActionBarDrawerToggle(
            this, parentLayoutSideBarDrawer, R.string.sideBarMenu_open, R.string.sideBarMenu_close
        )

        // setting up the Drawer functionality and adding listener to it
        parentLayoutSideBarDrawer.addDrawerListener(sideBarMenuToggle); sideBarMenuToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // function to set up the button listeners
    // of the side Bar Drawer
    private fun setUpButtonListenerForFragmentNavigation() {
        // setting up the fragment switching buttons
        val buttonHomeFragment: TextView = findViewById(R.id.button_sidebarMenu_home)
        val buttonSearchFragment: TextView = findViewById(R.id.button_sidebarMenu_search)
        val buttonLogoutUser: TextView = findViewById(R.id.button_sidebarMenu_logout)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_frag_container) as NavHostFragment
        val homeNavController = navHostFragment.navController

        buttonHomeFragment.setOnClickListener {
            if (currFragment == "Search") {
                deSelectButton(buttonSearchFragment); selectButton(buttonHomeFragment)
                parentLayoutSideBarDrawer.close(); currFragment = "Home"
                homeNavController.navigate(R.id.action_searchFragment_to_homeFragment)
            }
        }
        buttonSearchFragment.setOnClickListener {
            if (currFragment == "Home") {
                deSelectButton(buttonHomeFragment); selectButton(buttonSearchFragment)
                parentLayoutSideBarDrawer.close(); currFragment = "Search"
                homeNavController.navigate(R.id.action_homeFragment_to_searchFragment)
            }
        }

        // setting up the logout button
        // clearing the user credentials and starting the Login Activity
        buttonLogoutUser.setOnClickListener {
            getSharedPreferences(
                getString(R.string.shared_preference_key),
                MODE_PRIVATE
            ).edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // function to disable/deselect a button
    // of the side Bar Drawer
    private fun deSelectButton(givenButton: TextView) {
        givenButton.setBackgroundColor(getColor(R.color.home_screen_bottom_sheet_color))
        givenButton.setTextColor(getColor(R.color.sideBarItemDeSelectedColor))
        TextViewCompat.setCompoundDrawableTintList(
            givenButton,
            ColorStateList.valueOf(getColor(R.color.sideBarItemDeSelectedColor))
        )
    }

    // function to enable/select a button
    // of the side Bar Drawer
    private fun selectButton(givenButton: TextView) {
        givenButton.setBackgroundResource(R.drawable.bg_selected_drawer_item)
        givenButton.setTextColor(getColor(R.color.sideBarItemSelectedColor))
        TextViewCompat.setCompoundDrawableTintList(
            givenButton,
            ColorStateList.valueOf(getColor(R.color.sideBarItemSelectedColor))
        )
    }
}