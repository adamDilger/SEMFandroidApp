package com.semfapp.adamdilger.semf;

import java.io.File;

/**
 * Created by adamdilger on 19/11/2015.
 */
public interface Communicator {
    void saveText(String text, int code);
    void addNewImage(File image);
}

