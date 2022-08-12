package com.solbox.delivery.ktcloudSDK;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Etc {

    static String read(String path) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(path));
        String file = new String(data);
        return file;
    }
}
