package com.ankushchaudhary.acgrocerylist.viewmodel

import androidx.lifecycle.*
import com.ankushchaudhary.acgrocerylist.roomdb.Item
import com.ankushchaudhary.acgrocerylist.roomdb.ItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemViewModel(private val itemDao: ItemDao): ViewModel() {

    // Cache all items from the database using LiveData.
    val allItems: LiveData<List<Item>> = itemDao.getItem().asLiveData()

    // Returns true if the stocks is available, false otherwise.
    fun isStockAvailable(item: Item): Boolean {
        return (item.itemQuantity!! > 0)
    }

    /**
     * Updates an existing Items in database.
     */
    fun updateItem(
        itemId: Int, itemName: String,
        itemPrice: String, itemCount: String
    ){
        val updatedItems = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItems)
    }

    /**
     * Launching a new coroutine to update an item in a non-blocking way.
     */
    private fun updateItem(item: Item){
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.update(item)
        }
    }

    /**
     *  Decreases the stock by one unit and updates the database.
     */
    fun sellItem(item: Item){
        if (item.itemQuantity!! > 0) {
            // Decreases quantity by 1
            val newItem = item.copy(itemQuantity = item.itemQuantity - 1)
            updateItem(item)
        }
    }

    /**
     *  Inserts the new item to database.
     */
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String){
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    /**
     *  Launching the new coroutine to insert the item to database.
     */
    private fun insertItem(item: Item){
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    /**
     * Launching the new coroutine to delete the item from database.
     */
    private fun deleteItem(item: Item){
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    /**
     * Retrieve the item from the repository.
     */
    fun retrieveItem(id: Int): LiveData<Item>{
        return itemDao.getItem(id).asLiveData()
    }

    /**
     * Return true if the EditTexts are not Empty.
     */
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean{
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Return an instance of the [Item] entity class with the item info entered by the user
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            itemQuantity = itemCount.toInt()
        )
    }

    /**
     * Called to update an existing entry in the Inventory database.
     * Return an instance of the [Item] entity class with the
     * item info updated by the user
     */
    private fun getUpdatedItemEntry(
        itemId: Int, itemName: String,
        itemPrice: String, itemCount: String
    ): Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            itemQuantity = itemCount.toInt()
        )
    }

}

/**
 * Factory class to instantiate the [ViewModel] class
 */
class InventoryViewModelFactory(private val itemDao: ItemDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress ("UNCHECKED_CAST")
            return ItemViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}