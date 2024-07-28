package com.mycompany.app;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Global {

    private static final LocalDateTime time = LocalDateTime.now();   //  Get the current date
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");  //  Format the date


    public static final String logsChannelId = "1113468093134557220";
    public static final String welcomeChannelId = "1113468093134557219";
    public static final String boosterChannelId = "1113468093134557219";
    public static final String boosterRoleId = "1173142323597672478";
    public static final String formattedTime = time.format(format);
    public static final Color CUSTOMRED = new Color(168, 52, 50);
    public static final Color CUSTOMGREEN = new Color(50, 168, 81);
}
