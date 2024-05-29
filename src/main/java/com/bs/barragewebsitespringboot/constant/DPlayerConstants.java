package com.bs.barragewebsitespringboot.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author morou
 */
public class DPlayerConstants {
    public static final int DPLAYER_SUCCESS_CODE = 0;

    public static final String DPLAYER_BARRAGE_ID = "0";
    public static final String DPLAYER_BARRAGE_AUTHOR = "DPlayer";
    public static final String DPLAYER_BARRAGE_TIME = "0";
    public static final String DPLAYER_BARRAGE_TEXT = "";
    public static final String DPLAYER_BARRAGE_COLOR = "16777215";
    public static final String DPLAYER_BARRAGE_TYPE = "0";

    public static final List barrage_init(List data){
        data = new ArrayList();
        data.add(DPLAYER_BARRAGE_TIME);
        data.add(DPLAYER_BARRAGE_TYPE);
        data.add(DPLAYER_BARRAGE_COLOR);
        data.add(DPLAYER_BARRAGE_AUTHOR);
        data.add(DPLAYER_BARRAGE_TEXT);
        return data;
    }
}
 