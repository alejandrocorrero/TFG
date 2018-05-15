package com.correro.alejandro.tfg.ui.patient.consultfragment

import okhttp3.MultipartBody
import java.io.File

public class ImageItem(val path:File, var photo:MultipartBody.Part)