package com.org.tunestream.interfaces;

import com.google.protobuf.Any;
import com.org.tunestream.models.Playlist;
import com.org.tunestream.models.Song;

public interface OnClickInterface {
    void onItemClick(Playlist playlist);
}
