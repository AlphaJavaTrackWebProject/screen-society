package org.alphatrack.screensociety.exceptions;

import lombok.*;


import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ErrorObject {

    private int statusCode;

    private String error;

    private String message;

    private String path;

    private LocalDateTime timeStamp;
}
