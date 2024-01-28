package com.safonov.betparser.client;

import java.io.IOException;

public interface Client {

    String makeRequest(String path) throws IOException;

}
