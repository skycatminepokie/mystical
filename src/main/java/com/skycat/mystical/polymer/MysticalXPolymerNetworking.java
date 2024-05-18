package com.skycat.mystical.polymer;

import eu.pb4.polymer.networking.api.PolymerNetworking;

public class MysticalXPolymerNetworking { // Adapted from Zauber, which is licensed under MIT (I have no clue what this mod does, but it has networking examples I need)

    public static void init() {
        PolymerNetworking.registerCommonSimple(HelloPacket.PACKET_ID, HelloPacket.CODEC);
    }

}
