package com.hodges.kevin.kotlin.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.hodges.kevin.kotlin.R
import com.hodges.kevin.kotlin.datamanager.CacheUtil
import com.hodges.kevin.kotlin.enums.RestaurantPriceEnum
import com.hodges.kevin.kotlin.model.Restaurant
import com.hodges.kevin.kotlin.singleton.RestaurantListSingleton
import com.hodges.kevin.kotlin.singleton.RestaurantListSingleton.RESTAURANT_LIST_KEY
import com.hodges.kevin.kotlin.singleton.RestaurantListSingleton.type
import com.hodges.kevin.kotlin.utils.HideKeyboardUtil
import com.hodges.kevin.kotlin.viewmodel.RestaurantViewModel
import com.hodges.kevin.kotlin.viewmodel.viewmodelimpl.RestaurantViewModelImpl
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Kevin on 2/7/2018.
 */
class MainActivity : AppCompatActivity() {

    private val restaurantViewModel: RestaurantViewModel = RestaurantViewModelImpl()
    private var currentRestaurant: Restaurant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setButtonListeners()
    }

    override fun onResume() {
        super.onResume()

        val prefs = getSharedPreferences(RESTAURANT_LIST_KEY, Context.MODE_PRIVATE)

        if (prefs.contains(RESTAURANT_LIST_KEY)) {
            RestaurantListSingleton.list = CacheUtil.loadFromStorage(this, RESTAURANT_LIST_KEY, type)
        }

        if (RestaurantListSingleton.list.isNotEmpty()) {
            getRandomRestaurant()
        }
        refreshUI()
    }

    override fun onPause() {
        super.onPause()

        CacheUtil.saveToStorage(this, RESTAURANT_LIST_KEY, RestaurantListSingleton.list)
    }

    private fun addNewRestaurant(price: RestaurantPriceEnum) {
        val wasRestaurantAdded = restaurantViewModel.addNewRestaurant(this, Restaurant(getTextFromEditRestaurantEditText(), price))

        if (!wasRestaurantAdded) {
            showRestaurantAlreadyAddedDialog()
        } else {
            showAddedRestaurantToast()
            refreshUI()
            addRestaurantEditText.setText("")
        }
    }

    private fun getRandomRestaurant() {
        val priceList = mutableListOf<RestaurantPriceEnum>()

        if (cheapCheckBox.isChecked) {
            priceList.add(RestaurantPriceEnum.CHEAP)
        }

        if (averageCheckBox.isChecked) {
            priceList.add(RestaurantPriceEnum.AVERAGE)
        }

        if (priceyCheckBox.isChecked) {
            priceList.add(RestaurantPriceEnum.PRICEY)
        }

        currentRestaurant = restaurantViewModel.getRandomRestaurant(priceList)

        if (currentRestaurant == null) {
            showNoRestaurantsAvailableDialog()
        } else {
            setDecisionText(currentRestaurant?.name)
        }
    }

    private fun isAtLeastOnePriceCategorySelected(): Boolean {
        if (cheapCheckBox.isChecked || averageCheckBox.isChecked || priceyCheckBox.isChecked) {
            return true
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun refreshUI() {
        setButtonState(decideButton, !RestaurantListSingleton.list.isEmpty())
        setButtonState(deleteAllRestaurantsButton, !RestaurantListSingleton.list.isEmpty())
        determineDecisionButtonState()
        setupRemoveAllButton()

        if (decisionTextView.text.isNotEmpty()) {
            deleteCurrentRestaurantButton.visibility = View.VISIBLE
        } else {
            deleteCurrentRestaurantButton.visibility = View.GONE
        }
    }

    private fun getTextFromEditRestaurantEditText(): String {
        return addRestaurantEditText.text.toString()
    }

    private fun setDecisionText(restaurantName: String?) {
        decisionTextView.text = restaurantName
    }

    private fun setButtonState(button: Button, isEnabled: Boolean) {
        button.isEnabled = isEnabled

        if (isEnabled) {
            button.alpha = 1f
        } else{
            button.alpha = .25f
        }
    }

//    fun getCheckedState() {
//
//        val map = HashMap<RestaurantPriceEnum, Boolean>()
//        map.put(RestaurantPriceEnum.CHEAP, cheapCheckBox.isChecked)
//        map.put(RestaurantPriceEnum.AVERAGE, averageCheckBox.isChecked)
//        map.put(RestaurantPriceEnum.PRICEY, priceyCheckBox.isChecked)
//
//        for (box in map) {
//            if (box.value) {
//
//            }
//        }
//
//        restaurantViewModel.hasAnyRestaurantsAtThisPrice(map)
//
//    }

    private fun setupRemoveAllButton() {

        val restaurantLabel = "restaurant".pluralize(RestaurantListSingleton.list.size)
        deleteAllRestaurantsButton.text = "Remove ${RestaurantListSingleton.list.size} $restaurantLabel"

        if (RestaurantListSingleton.list.size == 0) {
            deleteAllRestaurantsButton.visibility = View.INVISIBLE
        } else {
            deleteAllRestaurantsButton.visibility = View.VISIBLE
        }
    }

    private fun clearCurrentSelectionText() {
        decisionTextView.text = ""
    }

    private fun determineDecisionButtonState() {
        if (!isAtLeastOnePriceCategorySelected()) {
            setButtonState(decideButton, false)
        } else {
            setButtonState(decideButton, true)
        }
    }

    private fun String.pluralize(count: Int): String? {
        return if (count > 1) {
            this + "s"
        }else {
            this
        }
    }

    private fun setButtonListeners() {

        addNewRestaurantButton.setOnClickListener{
            showPriceDialog()
            HideKeyboardUtil.hideKeyboard(this, addRestaurantEditText)
        }

        decideButton.setOnClickListener{
            if (isAtLeastOnePriceCategorySelected()) {
                getRandomRestaurant()
                refreshUI()
            }
        }

        // Deletion buttons
        deleteCurrentRestaurantButton.setOnClickListener{
            restaurantViewModel.removeRestaurant(this, currentRestaurant)
            clearCurrentSelectionText()
            refreshUI()
        }

        deleteAllRestaurantsButton.setOnClickListener{
            restaurantViewModel.removeAllRestaurants(this)
            clearCurrentSelectionText()
            refreshUI()
        }

        // Checkboxes
        cheapCheckBox.setOnCheckedChangeListener { _, isChecked ->
            determineDecisionButtonState()
//            if (restaurantViewModel.hasAnyRestaurantsAtThisPrice(RestaurantPriceEnum.CHEAP)) {
//                setButtonState(decideButton, true)
//            } else {
//                setButtonState(decideButton, false)
//            }
        }

        averageCheckBox.setOnCheckedChangeListener { _, isChecked ->
            determineDecisionButtonState()

//            if (restaurantViewModel.hasAnyRestaurantsAtThisPrice(RestaurantPriceEnum.AVERAGE)) {
//                setButtonState(decideButton, true)
//            } else {
//                setButtonState(decideButton, false)
//            }
        }

        priceyCheckBox.setOnCheckedChangeListener { _, isChecked ->
            determineDecisionButtonState()

//            if (restaurantViewModel.hasAnyRestaurantsAtThisPrice(RestaurantPriceEnum.PRICEY)) {
//                setButtonState(decideButton, true)
//            } else {
//                setButtonState(decideButton, false)
//            }
        }

        // Edit text listener
        addRestaurantEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                // Enable add button if text is entered
                if (s.toString().isEmpty()) {
                    setButtonState(addNewRestaurantButton, false)
                } else {
                    setButtonState(addNewRestaurantButton, true)
                }
            }
        })
    }

    private fun showPriceDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Price category")
        builder.setMessage("""Enter this restaurant under a specific priceCategory category so you can
            |filter your results based on how frugal you're feeling.""".trimMargin())

        builder.setNeutralButton("Cheap", { _, _ ->
            addNewRestaurant(RestaurantPriceEnum.CHEAP)
        })

        builder.setNegativeButton("Average", { _, _ ->
            addNewRestaurant(RestaurantPriceEnum.AVERAGE)
        })

        builder.setPositiveButton("Pricey", { _, _ ->
            addNewRestaurant(RestaurantPriceEnum.PRICEY)
        })
                .show()
    }

    // Dialogs
    private fun showAddedRestaurantToast() {
        val restaurantLabel = "restaurant".pluralize(RestaurantListSingleton.list.size)

        Toast.makeText(this, """${getTextFromEditRestaurantEditText()} added to list. You now have
                |${RestaurantListSingleton.list.size} $restaurantLabel in your list.""".trimMargin(), Toast.LENGTH_SHORT).show()
    }

    private fun showNoRestaurantsAvailableDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Oops")
        builder.setMessage("You don't have any restaurants at that price point. Add some now!")
        builder.setPositiveButton("Ok", { _, _ ->  })
        builder.show()
    }
    private fun showRestaurantAlreadyAddedDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Oops")
        builder.setMessage("This restaurant is already on your list. Try adding another one!")
        builder.setPositiveButton("Ok", { _, _ ->  })
        builder.show()
    }
}
