package com.vendor.salon.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vendor.salon.R
import com.vendor.salon.adapters.InventoryOptionsItemRecyclerAdapter
import com.vendor.salon.adapters.InventoryRecyclerAdapters
import com.vendor.salon.data_Class.inventoryCategory.InventoryCategoriessResponse
import com.vendor.salon.data_Class.manage_inventory.DataItem
import com.vendor.salon.data_Class.manage_inventory.ManageInventoryResponse
import com.vendor.salon.databinding.ActivityInventoryBinding
import com.vendor.salon.networking.RetrofitClient
import com.vendor.salon.utilityMethod.FunctionCall
import com.vendor.salon.utilityMethod.OnClickListen
import com.vendor.salon.utilityMethod.OnClickListenerInterfaceKotlin
import com.vendor.salon.utilityMethod.loginResponsePref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InventoryActivity : AppCompatActivity(), OnClickListenerInterfaceKotlin, OnClickListen {
    var inventory_Category_Id: String = ""
    lateinit var inventoryOptionsItemRecyclerAdapter: InventoryOptionsItemRecyclerAdapter
    lateinit var listItemRecyclerAdapter: InventoryRecyclerAdapters
    private lateinit var binding: ActivityInventoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inventory)

        inventoryOptionsItemRecyclerAdapter = InventoryOptionsItemRecyclerAdapter(
            ArrayList<com.vendor.salon.data_Class.inventoryCategory.DataItem>(),
            this
        );
        binding.optiOnsRecYClErList.adapter = inventoryOptionsItemRecyclerAdapter

        binding.btnBack.setOnClickListener{
            finish()
        }
        listItemRecyclerAdapter = InventoryRecyclerAdapters(ArrayList<DataItem>(), this)
        binding.recyclerViewInventory.adapter = listItemRecyclerAdapter
        binding.btnAddInventory.setOnClickListener{
            var add_inventory_bottom_sheet_lays = BottomSheetDialog(this@InventoryActivity , R.style.BottomSheetDialogStyle )
            add_inventory_bottom_sheet_lays.setContentView(R.layout.new_inventory_bottom_sheet)
            add_inventory_bottom_sheet_lays.show()
            var item_name_lays =
                add_inventory_bottom_sheet_lays.findViewById<AppCompatEditText>(R.id.new_inventory_name)
            var inventory_qty_lays =
                add_inventory_bottom_sheet_lays.findViewById<AppCompatEditText>(R.id.new_inventory_qtities)


            add_inventory_bottom_sheet_lays.findViewById<AppCompatTextView>(R.id.btn_new_save)!!
                .setOnClickListener {
                    var new_items = item_name_lays!!.text.toString()
                    var inventoryqty = inventory_qty_lays!!.text.toString()
                    if (new_items.isEmpty()) {
                        item_name_lays.error = " Mandatory Field. "
                        item_name_lays.requestFocus()
                    } else if (inventoryqty.isEmpty()) {
                        inventory_qty_lays.error = " Mandatory Field. "
                        inventory_qty_lays.requestFocus()

                    } else {
                        addOrUpDatesInventory(
                            new_items,
                            inventoryqty,
                            "",
                            add_inventory_bottom_sheet_lays
                        )
                        add_inventory_bottom_sheet_lays.hide()
                    }
                }
        }

        var token: String = loginResponsePref.getInstance(this).token
        FunctionCall.showProgressDialog(this@InventoryActivity)
        var call: Call<InventoryCategoriessResponse> =
            RetrofitClient.getVendorService().getInventoryCategories("Bearer " + token);
        call.enqueue(object : Callback<InventoryCategoriessResponse> {
            override fun onResponse(
                call: Call<InventoryCategoriessResponse>,
                response: Response<InventoryCategoriessResponse>
            ) {
                FunctionCall.DismissDialog(this@InventoryActivity)
                if (response.isSuccessful && response.body() != null) {
                    binding.showNoDataText.visibility = View.GONE
                    inventoryOptionsItemRecyclerAdapter.refreshDataList(response.body()!!.data)
                    inventoryOptionsItemRecyclerAdapter.notifyDataSetChanged()
                    getInventoryData(String.format("%d", response.body()!!.data!![0]!!.id))

                } else {
                    binding.showNoDataText.visibility = View.VISIBLE
                    inventoryOptionsItemRecyclerAdapter.refreshDataList(ArrayList<com.vendor.salon.data_Class.inventoryCategory.DataItem>())
                    Log.d("inventory_category_hit", "onResponse: " + response!!.body())
                }
            }

            override fun onFailure(call: Call<InventoryCategoriessResponse>, t: Throwable) {
                FunctionCall.DismissDialog(this@InventoryActivity)
                binding.showNoDataText.visibility = View.VISIBLE
                Log.d("inventory_category_hit", "onFailure: " + t.message)
            }
        })
    }

    override fun onItemClick(id: Int) {
        getInventoryData(id.toString())
    }

    private fun getInventoryData(id: String) {
        this.inventory_Category_Id = id
        FunctionCall.showProgressDialog(this@InventoryActivity)
        var token: String = loginResponsePref.getInstance(this@InventoryActivity).token
        var call: Call<ManageInventoryResponse> = RetrofitClient.getVendorService()
            .getInventories("Bearer " + token, inventory_Category_Id)
        call.enqueue(object : Callback<ManageInventoryResponse> {
            override fun onResponse(
                call: Call<ManageInventoryResponse>,
                response: Response<ManageInventoryResponse>
            ) {
                FunctionCall.DismissDialog(this@InventoryActivity)
                if (response.isSuccessful && response.body()!!.isStatus) {
                    if ( response.body()!!.data!!.size > 0 ) {
                        binding.showNoDataText.visibility = View.GONE
                    }
                    else {
                        binding.showNoDataText.visibility = View.VISIBLE
                    }
                    listItemRecyclerAdapter.refreshLists(response.body()!!.data)
                    listItemRecyclerAdapter.notifyDataSetChanged()
                } else {
                    binding.showNoDataText.visibility = View.VISIBLE
                    Toast.makeText(
                        this@InventoryActivity,
                        " " + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("manage_inventory_hit", "onResponse: " + response.body())
                }
            }

            override fun onFailure(call: Call<ManageInventoryResponse>, t: Throwable) {
                FunctionCall.DismissDialog(this@InventoryActivity)
                binding.showNoDataText.visibility = View.VISIBLE
                Log.d("manage_inventory_hit", "onFailure: " + t.message)
            }
        })


    }

    private fun addOrUpDatesInventory(
        item: String,
        qty: String,
        inventory_id: String,
        add_inventory_bottom_sheet_lays: BottomSheetDialog
    ) {
        var token: String = loginResponsePref.getInstance(this@InventoryActivity).token
        FunctionCall.showProgressDialog(this@InventoryActivity)
        var call: Call<ManageInventoryResponse> = RetrofitClient.getVendorService()
            .editInventoryItems(
                "Bearer " + token,
                inventory_Category_Id,
                inventory_id,
                "updatecreate",
                item,
                qty
            )
        call.enqueue(object : Callback<ManageInventoryResponse> {
            override fun onResponse(
                call: Call<ManageInventoryResponse>,
                response: Response<ManageInventoryResponse>
            ) {
                FunctionCall.DismissDialog(this@InventoryActivity)
                if (response.isSuccessful && response.body()!!.isStatus) {
                    Toast.makeText(
                        applicationContext,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    getInventoryData(inventory_Category_Id)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("manage_inventory_hit", "onResponse: " + response)
                }
            }

            override fun onFailure(call: Call<ManageInventoryResponse>, t: Throwable) {
                Log.d("manage_inventory_hit", "onFailure: " + t.message)
                FunctionCall.DismissDialog(this@InventoryActivity)
            }

        })

    }

    override fun OnItemClick(
        context: Context,
        position: Int,
        inventory_id: String?,
        item_names: String?,
        qty: String?
    ) {
        var view: View =
            LayoutInflater.from(context).inflate(R.layout.inventory_item_alert_lays, null)
        var myinsItemAlertDialogs: AlertDialog.Builder = AlertDialog.Builder(this@InventoryActivity)
        myinsItemAlertDialogs.setView(view)
        val alertDialog = myinsItemAlertDialogs.create()
        alertDialog.show()

        var btn_edit_inventory_items: AppCompatTextView =
            view.findViewById(R.id.btn_edit_inventory_item)
        var btn_remove_inventory_item: AppCompatTextView =
            view.findViewById(R.id.btn_remove_inventory_items)
        btn_edit_inventory_items.setOnClickListener {
            var add_inventory_bottom_sheet_lays = BottomSheetDialog(this@InventoryActivity ,  R.style.BottomSheetDialogStyle)
            add_inventory_bottom_sheet_lays.setContentView(R.layout.new_inventory_bottom_sheet )
            add_inventory_bottom_sheet_lays.show()
            alertDialog.dismiss()
            var item_name_lays =
                add_inventory_bottom_sheet_lays.findViewById<AppCompatEditText>(R.id.new_inventory_name)
            var inventory_qty_lays =
                add_inventory_bottom_sheet_lays.findViewById<AppCompatEditText>(R.id.new_inventory_qtities)
            item_name_lays!!.setText(item_names)
            inventory_qty_lays?.setText(qty)

                add_inventory_bottom_sheet_lays.findViewById<AppCompatTextView>(R.id.btn_new_save)!!
                    .setOnClickListener {
                        var new_items = item_name_lays!!.text.toString()
                        var inventoryqty = inventory_qty_lays!!.text.toString()
                        if (new_items.isEmpty()) {
                            item_name_lays.error = " Mandatory Field. "
                            item_name_lays.requestFocus()
                        } else if (inventoryqty.isEmpty()) {
                            inventory_qty_lays.error = " Mandatory Field. "
                            inventory_qty_lays.requestFocus()

                        } else {
                            addOrUpDatesInventory(
                                new_items,
                                inventoryqty,
                                inventory_id!!,
                                add_inventory_bottom_sheet_lays
                            )
                            add_inventory_bottom_sheet_lays.hide()
                        }
                    }
        }
        btn_remove_inventory_item.setOnClickListener {
            alertDialog.dismiss()
            FunctionCall.showProgressDialog(this@InventoryActivity)
            var token: String = loginResponsePref.getInstance(this@InventoryActivity).token
            var call: Call<ManageInventoryResponse> =
                RetrofitClient.getVendorService().editInventoryItems(
                    "Bearer " + token,
                    inventory_Category_Id,
                    inventory_id.toString(),
                    "delete",
                    "", ""
                )

            call.enqueue(object : Callback<ManageInventoryResponse> {
                override fun onResponse(
                    call: Call<ManageInventoryResponse>,
                    response: Response<ManageInventoryResponse>
                ) {
                    FunctionCall.DismissDialog(this@InventoryActivity)
                    if (response.isSuccessful && response.body()!!.isStatus) {
                        getInventoryData(inventory_Category_Id)
                    } else {
                        Toast.makeText(
                            this@InventoryActivity,
                            "" + response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("manage_inventoryhit", "onResponse: " + response.body())
                    }
                }

                override fun onFailure(call: Call<ManageInventoryResponse>, t: Throwable) {
                    FunctionCall.DismissDialog(this@InventoryActivity)
                    Log.d("manage_inventoryhit", "onFailure: " + t.message)
                }
            })

        }

    }
}








