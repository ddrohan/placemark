package org.wit.placemark.views.placemark

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_placemark.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.placemark.R
import org.wit.placemark.helpers.readImageFromPath
import org.wit.placemark.models.PlacemarkModel

class PlacemarkView : AppCompatActivity(), AnkoLogger {

    lateinit var presenter: PlacemarkPresenter
    var placemark = PlacemarkModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placemark)
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        presenter = PlacemarkPresenter(this)

        chooseImage.setOnClickListener { presenter.doSelectImage() }

        placemarkLocation.setOnClickListener { presenter.doSetLocation() }
    }

    fun showPlacemark(placemark: PlacemarkModel) {
        val tempTitle = placemarkTitle.text.toString()
        val tempDes = description.text.toString()
        if(placemark.title.isBlank())
            placemark.title = tempTitle
        placemarkTitle.setText(placemark.title)
        if(placemark.description.isBlank())
            placemark.description = tempDes
        description.setText(placemark.description)
        if(!placemark.image.isBlank()) {
            placemarkImage.setImageBitmap(readImageFromPath(this, placemark.image))
            chooseImage.setText(getString(R.string.button_changeImage))
        }
        else
            placemarkImage.setImageResource(R.mipmap.ic_launcher_round)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        if (presenter.edit) menu.getItem(2).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_save -> {
                if (placemarkTitle.text.toString().isEmpty()) {
                    toast(R.string.error_Title)
                } else {
                    presenter.doAddOrSave(placemarkTitle.text.toString(), description.text.toString())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }
}