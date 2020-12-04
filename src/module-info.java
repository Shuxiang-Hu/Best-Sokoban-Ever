/**
 * This module use Java JDK 15.0.1, JavaFX 15 and JUnit4 is used for tests
 */
module bestSokobanEverV6 {
    requires java.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.media;
    requires java.desktop;
    requires java.logging;
    opens controller;
    opens model;
    opens data;
    opens factory;
    opens main;
    opens viewer;
    opens object;
}