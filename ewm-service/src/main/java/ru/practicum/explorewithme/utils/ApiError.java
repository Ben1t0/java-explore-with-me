package ru.practicum.explorewithme.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ApiError {
    private final UUID id;
    private List<String> errors;
    private String message;
    private String reason;
    private ErrorStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;

    public ApiError(){
        id = UUID.randomUUID();
        timestamp = LocalDateTime.now();
    }
}
