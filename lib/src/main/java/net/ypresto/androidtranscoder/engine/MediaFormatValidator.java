/*
 * Copyright (C) 2015 Yuya Tanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ypresto.androidtranscoder.engine;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;

import net.ypresto.androidtranscoder.format.MediaFormatExtraConstants;

class MediaFormatValidator {

    public static void validateVideoOutputFormat(MediaFormat format) {
        String mime = format.getString(MediaFormat.KEY_MIME);
        // Refer: http://developer.android.com/guide/appendix/media-formats.html#core
        // Refer: http://en.wikipedia.org/wiki/MPEG-4_Part_14#Data_streams
        if (!validateEncoderMimeType(mime))
          throw new InvalidOutputFormatException("Video codecs other than AVC is not supported, actual mime type: " + mime);
    }

    public static void validateAudioOutputFormat(MediaFormat format) {
        String mime = format.getString(MediaFormat.KEY_MIME);
        if (!validateEncoderMimeType(mime))
          throw new InvalidOutputFormatException("Audio codecs other than AAC is not supported, actual mime type: " + mime);
    }

    private static boolean validateEncoderMimeType(String mime) {
        // See https://developer.android.com/reference/android/media/MediaCodecInfo
        // Code on that page was updated to use getCodecInfos rather than deprecated getCodeInfoAt()
        MediaCodecList list = new MediaCodecList(MediaCodecList.ALL_CODECS);
        MediaCodecInfo[] codecInfos = list.getCodecInfos();
        for (MediaCodecInfo info : codecInfos) {
            if (info.isEncoder()) {
                String[] types = info.getSupportedTypes();
                for (int j = 0; j < types.length; j++) {
                    if (types[j].equalsIgnoreCase(mime)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
